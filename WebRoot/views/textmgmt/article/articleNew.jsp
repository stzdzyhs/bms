<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
	.swfupload {
		position: absolute;
		z-index: 1;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文章信息</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- 文件上传控件 -->
<script src="${path}/js/swfupload2.5/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload2.5/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/newUploadHandler0.js" type="text/javascript"></script>

<script src="${path}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">

var saveAndModify = false;

$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
	
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/textmgmt/article/addArticle.action",
			beforeSend: function() {
				art.dialog.through({
					id: 'broadcastLoading',
					title: false,
				    content: '<img src="${path}/images/08.gif" />',
				    lock: true
				});
			},
			error: function(a, b) {
				art.dialog.list['broadcastLoading'].close();
				art.dialog.alert("保存失败！");
				return false;
			},
			success: function(data) {
				art.dialog.list['broadcastLoading'].close();
				eval("var rsobj = "+data+";");
				if(isResultSucc(rsobj)){
					$("#form2_articleNo").val(rsobj.desc);
					art.dialog.alert("保存成功！", goBack);
				}
				else{
					art.dialog.alert("保存失败！"+rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	//异步检查ID是否可用
	$("#eArticleId").formValidator({onFocus:"文章ID只支持字母和数字",onCorrect:"&nbsp;"})
		.inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能带空格,请确认"}, min:1,max:30,onError:"文章ID长度1-30个字符,请确认"})
		.regexValidator({regExp:"username",dataType:"enum",onError:"文章ID只支持字母和数字"})
	    .ajaxValidator({
	    	type : "POST",
			dataType : "html",
			async : true,
			url : "${path}/textmgmt/article/checkArticle.action?status=0",
 			//data: "status=0",
			success : function(data) {
				eval("var rsobj = "+data+";");
				if(isResultSucc(rsobj)){
					return true;
				}
				else {
					return false;
				}
			},
			buttons: $("#button"),
			error: function(jqXHR, textStatus, errorThrown){
				art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);
			},
			onError : "该文章ID已存在，请更换文章ID",
			onWait : "正在对文章ID进行合法性校验，请稍候..."
		}
	).defaultPassed();
	
	$("#eArticleName").formValidator({onFocus:"文章名称不能为空,请确认",onCorrect:"&nbsp;"})
		.inputValidator({min:1, onError:"文章不能为空,请确认"})
	    .ajaxValidator({
		    type : "POST",
			dataType : "html",
			async : true,
			url : "${path}/textmgmt/article/checkArticle.action?status=1&articleNo=${article.articleNo}",
 			data: "status=1",
			success : function(data) {
				eval("var rsobj = "+data+";");
				if(isResultSucc(rsobj)){
					return true;
				}
				else{
					return false;
				}
			},
			buttons: $("#button"),
			error: function(jqXHR, textStatus, errorThrown) {
				art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);
			},
			onError : "该文章名称已存在，请更换文章名称",
			onWait : "正在对文章名称进行合法性校验，请稍候..."
		}
	).defaultPassed();

	$("#eTitle").formValidator({onFocus:"文章标题不能为空，请确认",onCorrect:"&nbsp;"}).
	inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"文章标题前后不能带空格，请确认"},min:1,onError:"文章标题不能为空，请确认"}).defaultPassed();

<% // only check if is super admin %> 	
<c:if test="${activeOperator.superAdmin}">
	$("#eCompanyNo").formValidator({onFocus:"运营商不能为空，请选择",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"运营商不能为空，请选择"},min:1,onError:"运营商不能为空，请选择"});
</c:if>
	
	$("#showOrder").formValidator({onFocus:"显示顺序只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"显示顺序只支持正整数且不能为0"}).defaultPassed();
	//上传组件初始化
	var settings1 = {	
			flash_url : "../../js/swfupload2.5/swfupload.swf",
			flash9_url :"../../js/swfupload2.5/swfupload_fp9.swf",
			upload_url: "${path}/textmgmt/article/uploadArticleBody.action",
			file_size_limit : "100 MB",
			file_types : "*.txt;",
			file_types_description : "All Files",
			file_upload_limit : 100,
			file_queue_limit : 0, // single file
//			custom_settings : {
//				progressTarget : "fsUploadProgress",
//				cancelButtonId : "btnCancel" 
//			},
			debug: false,

			// Button Settings
			button_placeholder_id : "aUploadBody",
			button_width: 61,
			button_height: 22,
			button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor: SWFUpload.CURSOR.HAND,
		};
		swfu1 = createFileUpload(settings1);
		swfu1.beforeUpload=function(queue) {
			if(queue.length>1) {
				art.dialog.alert("请只选择一个文件");
				return false;
			}
			return true;
		};
		swfu1.afterUploadSucc = function() {
			art.dialog.alert("保存成功！", function() {
				$('#txtFileName').val(swfu1.serverData.filePath);
			});
		};

});

function onSave() {
	saveAndModify = false;
	$("#form1").submit();
}

function onSaveAndModify() {
	saveAndModify = true;
	$("#form1").submit();
}

function goBack() {
	var url = "${path}/textmgmt/article/articleList.action";
	
	if( saveAndModify) {
		url = "${path}/textmgmt/article/articleEdit.action";
	}

	document.getElementById("form2").action = url; 
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>文章信息</h2></div>

<div>
	<form id="form1" name="form1">
<%-- 	<input type="hidden" name="albumNo" id="albumNo" value="${article.albumNo}" /> --%>
	
	<input type="hidden" name="saveAndModify" id="saveAndModify" value="0" />	
	
	<fieldset>
		<legend>&nbsp;基本信息</legend>
		<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  		 <tr>
    		<td class="tdBlue" width="10%">显示顺序</td>
    		<td class="tdBlue" width="40%">
      			<input id="showOrder" name="showOrder" maxlength="30" value="${article.showOrder == null ? 100 : article.showOrder}" >
     		</td>
     		<td class="tdBlue" width="10%">所属运营商</td>
		    <td class="tdBlue">
    			<!-- do not show select for admin -->
				<c:set var="showCompany" value="block" />
      			<c:if test="${activeOperator.superAdmin==false}">
      				${companyList[0].companyName}
					<c:set var="showCompany" value="none" />
	    		</c:if>
        
				<select id="eCompanyNo" name="companyNo" class="form130px" style="display:${showCompany}">
					<option value="">--请选择--</option>
					<c:forEach items="${companyList}" var="u" > 
					<option value="${u.companyNo }" <c:if test="${article.companyNo==u.companyNo}"> selected="selected" </c:if>>${u.companyName}</option>
					</c:forEach> 
				</select>
		    </td>
		 </tr>
     	</tr>
  		<tr>
    		<td class="tdBlue" width="10%">文章名称</td>
    		<td class="tdBlue" width="40%">
      			<input id="eArticleName" name="articleName" maxlength="100" value="" >
     		</td>
     		<td class="tdBlue" width="10%">标题</td>
    		<td class="tdBlue" width="40%">
      			<input id="eTitle" name="title" maxlength="30" value="" >
     		</td>
  		</tr>
  		<tr>
    		<td class="tdBlue" width="10%">副标题</td>
    		<td class="tdBlue" width="40%">
      			<input id="eTitle2" name="title2" maxlength="100" value="">
     		</td>
     		<td class="tdBlue" width="10%">关联模板</td>
    		<td class="tdBlue" width="40%">
      		      <select id="templateId" name="templateId" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${templateList}" var="t" > 
			   <option value="${t.id }" <c:if test="${article.templateId==t.id }"> selected="selected" </c:if>>${t.templateName }</option>
			</c:forEach> 
		</select>
     		</td>
  		</tr>
   		<tr>
     		<td class="tdBlue" width="10%">简介</td>
    		<td class="tdBlue" width="40%">
      			<textarea id="eIntroduction" name="introduction" rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 1000)">${article.introduction}</textarea>
     		</td>
     		<td class="tdBlue" width="10%">上传正文</td>
    		<td class="tdBlue" width="40%">		
    			<span id="aUploadBody"></span>
    			<input value="上传" type="button" class="btnQuery" />
    			<div>
    				<input type="text" value="${article.body}" id="txtFileName" name="body" readonly="readonly" style="border: solid 0px; background-color: transparent;"/>
    			</div>
    		</td>
  		</tr>
		</table>
	</fieldset>

	<div style="width:100%; text-align:center; margin-top:10px;">
		<input value="保存" type="button" class="btnQuery" onclick="onSave()"/>&nbsp;&nbsp;
<!-- 		<input value="保存&修改" type="button" class="btnQuery" onclick="onSaveAndModify()"/>&nbsp;&nbsp; -->
		<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
	</div>
</form>
</div>

<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="articleNo" id="form2_articleNo" value="" />
<input type="hidden" name="articleId" id="form2_articleId" value="${search.articleId}" />
<input type="hidden" name="articleName" id="form2_articleName" value="${search.articleName}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>

</body>
</html>
