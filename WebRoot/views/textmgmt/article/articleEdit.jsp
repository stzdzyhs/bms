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

<script src="${path}/js/swfupload2.5/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload2.5/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/newUploadHandler0.js" type="text/javascript"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">

var articleNo = "${article.articleNo}";
var djSwf2=null;

$(document).ready(function(){

	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "post",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/textmgmt/article/updateArticle.action",
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
				if(isResultSucc(rsobj)) {
					art.dialog.alert("保存成功！", goBack);
				}
				else{
					art.dialog.alert("保存失败 " + rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#eArticleId").formValidator({onFocus:"文章Id不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"文章名称前后不能带空格，请确认"},min:1,onError:"文章名称不能为空，请确认"}).defaultPassed();
	$("#eArticleName").formValidator({onFocus:"文章名称不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"文章名称前后不能带空格，请确认"},min:1,onError:"文章名称不能为空，请确认"}).defaultPassed();
	$("#eTitle").formValidator({onFocus:"标题不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"标题前后不能带空格，请确认"},min:1,onError:"标题不能为空，请确认"}).defaultPassed();
	$("#showOrder").formValidator({onFocus:"显示顺序只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"显示顺序只支持正整数且不能为0"}).defaultPassed();
	
	var JSESSIONID="<c:out value='${pageContext.session.id}'/>";
	var operatorNo = "${operator.operatorNo}";
	var parameter = String.format(";jsessionid={0}?articleNo={1}&operatorNo={2}", JSESSIONID, articleNo,operatorNo);

	//上传组件初始化
	// the first upload article body
	try {
		postParam = {};//{jsessionid:JSESSIONID};
		// chrome ok, firefox not work
		//djSwf.swfu.setPostParams(postParam);
		//alert('init ok: ' + JSESSIONID);
	}
	catch (e) {
		alert('error: : ' +  e + " " + JSESSIONID);
	}

	var settings1 = {
		flash_url : "../../js/swfupload2.5/swfupload.swf",
		flash9_url :"../../js/swfupload2.5/swfupload_fp9.swf",
		upload_url: "${path}/textmgmt/article/uploadArticleBody.action" + parameter,
		file_size_limit : "100 MB",
		file_types : "*.txt;",
		file_types_description : "All Files",
		file_upload_limit : 100,
		file_queue_limit : 0, // single file
//		custom_settings : {
//			progressTarget : "fsUploadProgress",
//			cancelButtonId : "btnCancel" 
//		},
		debug: false,

		// Button Settings
		button_placeholder_id : "eUploadBody",
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

	var settings2 = {
		flash_url : "../../js/swfupload2.5/swfupload.swf",
		flash9_url :"../../js/swfupload2.5/swfupload_fp9.swf",
		upload_url: "${path}/textmgmt/article/uploadArticleImage.action" + parameter,
		file_size_limit : "100 MB",
		file_types : "*.jpg;*.png;*.gif",
		file_types_description : "图片",
		file_upload_limit :100,
		file_queue_limit : 0,
// 		custom_settings : {
// 			progressTarget : "fsUploadProgress",
// 			cancelButtonId : "btnCancel"
// 		},
		debug: false,

		// Button Settings
		button_placeholder_id : "eUploadImages",
		button_width: 61,
		button_height: 22,
		button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
		button_cursor: SWFUpload.CURSOR.HAND,
	};
	swfu2 = createFileUpload(settings2);
	swfu2.afterUploadSucc= function() {
		art.dialog.alert("保存成功,请注意:调整图片编号！", refreshThisPage);
	};
	//alert("ok");
});
//-----------------------------------------------------------------------------

function showUploadFile2(data){
	art.dialog.list['broadcastLoading'].close();
	var rsObj = eval("("+data+")");
	if (rsObj!="undefined") {
		if (isResultSucc(rsObj)) {
			art.dialog.alert("上传文件成功！" + rsObj.filePath, refreshThisPage);
		}
		else {
			art.dialog.alert(rsObj.desc);
		}
	}
	else{
		art.dialog.alert("上传文件失败！");
	}
}
//-----------------------------------------------------------------------------

function batchUpload() {
	try {
		djSwf2.swfu.selectFile();
		alert("bu");
	}
	catch(e) {
		alert("Error: " + e);
	}
}
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='pictureIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		
		for (var i=0; i<sets.length; i++){
			var isSelf = $("#isSelf" + sets[i].value).val();
			if (isSelf == 0){
				art.dialog.alert("您没有删除该资源的权限！");
				return false;
			}
			
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus == 4){
				art.dialog.alert("不能直接删除已经发布的图片！");
				return false;
			}
		}
	}
	else {
		var aa = document.getElementsByName("pictureIds");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus == 4){
			art.dialog.alert("不能直接删除已经发布的图片！");
			return false;
		}
	}

	var param = id != '' ? "?pictureIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/picmgmt/picture/pictureDelete.action"+param,
				dataType: 'html',
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
					art.dialog.alert("删除失败！");
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(isResultSucc(rsobj)){
						art.dialog.alert("删除成功！", refreshThisPage);
					}
					else{
						if(rsobj.desc==null) {
							rsobj.desc = "";
						}
						art.dialog.alert("删除失败！" + rsobj.desc);
					}
				}
			};
			jQuery('#form2').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}


function removeFile(delImg){
	art.dialog.confirm('您确认删除操作？', function() {
		var filePath = delImg.title;
		if(filePath!=null && filePath!=""){
			var url = "${path}/picmgmt/album/albumDeleteCover.action";
			$.ajax({
				type: "post",
				async: false,
				data: {'albumNo':'${album.albumNo}','filePath':filePath},
				dataType: "json",
				url: url,
				success:function(data){
					art.dialog.alert("删除封面成功！");
					$(delImg).prev().remove();
					$(delImg).remove();
					$("#fileNum").val(parseInt($("#fileNum").val())-1);
				}
			});
		}
	}
	)
}


var articleNo = "${article.articleNo}";


function onSave() {
	$("#form1").submit();
}

function goBack(){
	document.getElementById("form2").action = "${path}/textmgmt/article/articleList.action";
	document.getElementById("form2").submit();
}

function setCover(id) { // pic id
	alert("TODO....");	
}

function detail(pictureId){
	art.dialog.open("${path}/picmgmt/picture/pictureDetail.action?pictureId=" + pictureId,	{
		title: "图片详情", 
		width: 800,
		height: 450,
		lock: true

	});
}


function refreshThisPage() {
	//document.getElementById("form2").action = "${path}/textmgmt/article/articleEdit.action?articleNo=" + "${article.articleNo}";
	document.getElementById("form2").submit();
}

// show edit picture dialog
function toEditPage(pid) {
	//alert("edit pic");
	art.dialog.open("${path}/picmgmt/picture/pictureEditBasicInfo.action?id=" + pid, {
		title: "图片修改", 
		width: 980,
		height: 350,
		lock: true,
		close:function(){
		},
		okVal:"保存",
		ok:function(){
			var iframe = this.iframe.contentWindow;

			if(iframe.saveBasicInfo!=null) {
				iframe.saveBasicInfo(refreshThisPage);
			}
			else {
				alert("NULL ????");
			}
			return false;
		},
		cancel:function(){
			return true;
		}
	});
}

function articleBodyEdit(articleNo) {
	var txtFileName=document.getElementById("txtFileName").value;  
	art.dialog.open("${path}/textmgmt/article/articleBodyEdit.action?articleNo="+articleNo +"&txtFileName="+txtFileName,{
		title: "文章正文", 
		width: 600,
		height: 450,
		lock: true
	});
}


</script>
</head>

<body>
<div class="title"><h2>文章信息</h2></div>

<div>
	<form id="form1" name="form1" method="post">
	<input type="hidden" name="articleNo" id="articleNo" value="${article.articleNo}" />
	
	<input type="hidden" name="saveAndModify" id="saveAndModify" value="0" />	
	
	<fieldset>
		<legend>&nbsp;基本信息</legend>
		<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">	
		<tr>
    		<td class="tdBlue" width="10%">显示顺序</td>
    		<td class="tdBlue" width="40%">
      			<input id="showOrder" name="showOrder" maxlength="30" value="${article.showOrder == null ? 100 : article.showOrder}" >
     		</td>
     		<td class="tdBlue" width="10%">文章名称</td>
    		<td class="tdBlue" width="40%">
      			<input id="eArticleName" name="articleName" maxlength="100" value="${article.articleName}" >
     		</td>
     		<!-- 
     		<td class="tdBlue" width="10%">文章Id</td>
    		<td class="tdBlue" width="40%">
      			<input id="eArticleId" name="articleId" maxlength="30" value="${article.articleId}" >
     		</td>
     		 -->
     	</tr>
  		<tr>
     		<td class="tdBlue" width="10%">标题</td>
    		<td class="tdBlue" width="40%">
      			<input id="eTitle" name="title" maxlength="30" value="${article.title}" >
     		</td>
     		<td class="tdBlue" width="10%">副标题</td>
    		<td class="tdBlue" width="40%">
      			<input id="eTitle2" name="title2" maxlength="100" value="${article.title2}">
     		</td>
  		</tr>
  		<tr>  			
     		<td class="tdBlue" width="10%">上传正文</td>
    		<td class="tdBlue" width="40%">		
    			<span id="eUploadBody"></span>
    			<input value="上传" type="button" class="btnQuery" />
    			<!-- <span> 注意:正文请使用UTF-8编码!</span> -->
    			<div>
    				<input type="text" value="${article.body}" id="txtFileName" name="body" readonly="readonly" style="border: solid 0px; background-color: transparent;"/>
    			</div>
    		</td>
    		<td class="tdBlue" width="10%">编辑正文</td>
    		<td class="tdBlue" width="40%">		
    			<input value="编辑" type="button" class="btnQuery" onclick="articleBodyEdit('${article.articleNo}')"/> 
    			<!-- <span> 注意:正文需要使用UTF-8编码!</span>-->
    		</td>
  		</tr>
  		<tr height="80">
     		<td class="tdBlue" width="10%">关联模板</td>
    		<td class="tdBlue" width="40%">
    		      		         <select id="templateId" name="templateId" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${templateList}" var="t" > 
			   <option value="${t.id }" <c:if test="${article.templateId==t.id }"> selected="selected" </c:if>>${t.templateName }</option>
			</c:forEach> 
			</select>
    		</td>
       		<td class="tdBlue" width="10%">简介</td>
    		<td class="tdBlue" width="40%">
      			<textarea id="eIntroduction" name="introduction" rows="3" cols="80" onkeyup="this.value = this.value.slice(0, 1000)"> ${article.introduction} </textarea>
   			</td>
		</table>

		<div style="width:100%; text-align:center; margin-top:10px;">
			<input value="保存" type="button" class="btnQuery" onclick="onSave()"/>&nbsp;&nbsp;
			<input value="取消" type="button" class="btnQuery" onclick="goBack()" />
		</div>
	</fieldset>

</form>
</div>

<form action="${path}/textmgmt/article/articleEdit.action" method="post" name="form2" id="form2">
<input type="hidden" name="articleNo" id="articleNo" value="${article.articleNo}" />
<!-- toolbar -->
<div class="toolBar">

<jsp:include page="/views/common/pageTemplate.jsp" />
<!--翻页 end-->

<div style="width:1px;height:1px;" id="selectFiles" ></div>
<div class="operation">
   	<c:set var="add" value="false" />
   	<c:set var="delete" value="false" />
   	<c:set var="edit" value="false" />
   	<c:set var="detail" value="false" />
   	<c:set var="submit" value="false" />
   	<c:set var="approved" value="false" />
   	<c:set var="rejected" value="false" />
   	<c:set var="publish" value="false" />
   	<c:set var="unpublish" value="false" />
   	<c:set var="batch" value="false" />
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
		 	<c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'delete'}">   
			<c:set var="delete" value="true" />  
		</c:if> 
		<c:if test="${item eq 'edit'}">   
			<c:set var="edit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'detail'}">   
			<c:set var="detail" value="true" />  
		</c:if> 
		<c:if test="${item eq 'submit'}">   
			<c:set var="submit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'approved'}">   
			<c:set var="approved" value="true" />  
		</c:if> 
		<c:if test="${item eq 'rejected'}">   
			<c:set var="rejected" value="true" />  
		</c:if> 
		<c:if test="${item eq 'publish'}">   
			<c:set var="publish" value="true" />  
		</c:if> 
		<c:if test="${item eq 'unpublish'}">   
			<c:set var="unpublish" value="true" />  
		</c:if> 
		<c:if test="${item eq 'batch'}">   
			<c:set var="batch" value="true" />  
		</c:if> 
	</c:forEach>
	<span id="eUploadImages"></span>
	<input class="btnQuery" type="button" value="批量上传">
	<input class="btnQuery" type="button" value="删除" onclick="deleteSets('')">
<!--  TODO:   <input type="button" class="btn btn80" value="设为封面" onclick="setCover()"> -->
</div>
</div> <!--工具栏 end-->
  
<!-- picture list of article -->
<div class="tableWrap">
<table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
	<thead>
    	<tr>
        	<th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'pictureIds')"></th>
          	<th width="*%">图片名称</th>
          	<th width="*%">图片文件</th>
          	<th width="*%">状态</th>
          	<th width="*%">参与投票</th>
          	<th width="*%">创建用户</th>
          	<th width="*%">创建时间</th>
          	<th width="*%">图片编号(位置)</th>
          	<th width="*%">操作</th>
		</tr>
	</thead>
    <tbody>
      	<c:if test="${article.pictures == null || fn:length(article.pictures) == 0}">
		<tr>
			<td colspan="9">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  	</c:if>
	  	
      	<c:forEach items="${article.pictures}" var="p">
        <tr>
        	<td align="center"><input type="checkbox" id="pictureIds" name="pictureIds" value="${p.id}">
          	</td>
          	<td align="center">
             	${p.shortPicName} 
          	</td>
          	<td align="center">
				<c:choose>
	    	    <c:when test="${p.image}"> <!-- is an image test -->
		      		<img src="${path}${p.picPath2}" style='width:32px; height:32px' id='toolTipImg'/>
	        	</c:when>
		        <c:otherwise>
		        	&nbsp;
	        	</c:otherwise>
		    	</c:choose>
          	</td>
          	<td align="center">
          		${p.statusName}
          	</td>
          	
          	<td align="center">
          		${p.voteFlagName}
          	</td>
          	<td align="center">
            	${p.operator.operatorName}
          	</td>
          	<td align="center">${p.createTime}</td>
          	<td align="center">${p.resNo}</td>
          	<td class="tdOpera2" align="center">
             	<a href="javascript:;" onclick="detail('${p.id}')">详情</a>
             	<a href="javascript:;" onclick="toEditPage('${p.id}')">编辑</a>
             	<a href="javascript:;" onclick="deleteSets('${p.id}')">删除</a>
          	</td>
        </tr>
        </c:forEach>
	</tbody>
    </table>
</div>

</form>

</body>


<!-- <form id="form2" name="form2" method="post"> -->
	<!-- 缓存查询条件 start -->
<%-- 	<input type="hidden" name="articleId" value="${search.articleId}" /> --%>
<%-- 	<input type="hidden" name="articleName" value="${search.articleName}" /> --%>
<%-- 	<input type="hidden" name="status"  value="${search.status}" /> --%>
<%-- 	<input type="hidden" name="companyNo"  value="${search.companyNo}" /> --%>
<%-- 	<input type="hidden" name="operatorNo"  value="${search.operatorNo}" /> --%>
<%-- 	<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" /> --%>
<%-- 	<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" /> --%>
	<!-- 缓存查询条件 end -->
<!-- </form> -->
</html>
<script src="${path}/js/ToolTip.js"></script>

