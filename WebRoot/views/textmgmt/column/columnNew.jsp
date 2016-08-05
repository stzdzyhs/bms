<!-- this file is OBSOLETED, see columnEdit.jsp -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>版块信息</title>
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
<!-- 上传组件控件 -->
<script src="${path}/js/swfupload2.5/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload2.5/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/newUploadHandler0.js" type="text/javascript"></script>

<script src="${path}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
		type : "POST",
		dataType : "html",
		buttons:$("#button"),
		url: "${path}/textmgmt/column/columnSave.action",
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
			//是否登陆超时	
		    ifLoginTimeout(data,goBack);	
			    
			art.dialog.list['broadcastLoading'].close();
			eval("var rsobj = "+data+";");
			if(isResultSucc(rsobj)){
				art.dialog.alert("保存成功！", goBack);
			}
			else{
				art.dialog.alert("保存失败！"+rsobj.desc);
			}
		}
	}});
	
	//异步检查ID是否可用
	$("#column_columnId").formValidator({onFocus:"版块ID只支持字母和数字",onCorrect:"&nbsp;"}).inputValidator({min:1,max:20,onError:"版块ID长度1-20个字符,请确认"}).regexValidator({regExp:"username",dataType:"enum",onError:"版块ID只支持字母和数字"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "json",
		async : true,
		url : "${path}/textmgmt/column/isIdUnique.action?columnNo=${column.columnNo }",
		success : function(rsobj){
			if(isResultSucc(rsobj)){
				return true;
			}
			else{
				return false;
			}
		},
		buttons: $("#button"),
		error: function(jqXHR, textStatus, errorThrown){art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);},
		onError : "该版块ID已存在，请更换版块ID",
		onWait : "正在对版块ID进行合法性校验，请稍候..."
	}).defaultPassed();
	
	$("#column_columnName").formValidator({onFocus:"版块名称不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"名称前后不能带空格,请确认"},min:1,onError:"版块名称不能为空,请确认"}).defaultPassed();   
	/* .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/textmgmt/column/isNameUnique.action?columnNo=${column.columnNo}",
		success : function(data){
			eval("var rsobj = "+data+";");
			if(isResultSucc(rsobj)){
				return true;
			}
			else{
				return false;
			}
		},
		buttons: $("#button"),
		error: function(jqXHR, textStatus, errorThrown){art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);},
		onError : "该版块名称已存在，请更换版块名称",
		onWait : "正在对版块名称进行合法性校验，请稍候..."
	}).defaultPassed(); */
	
	$("#column_columnDescribe").formValidator({onFocus:"&nbsp;",onCorrect:"&nbsp;"}).inputValidator({min:0,max:500,onErrorMax:"版块描述不能超过500个字符,请确认"});
	
	$("#column_parentId").formValidator({onFocus:"所属版块不能为空，请选择",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"所属版块不能为空，请选择"},min:1,onError:"所属版块不能为空，请选择"});

	<% // only check if is super admin %> 	
<c:if test="${activeOperator.superAdmin}">
	$("#eCompanyNo").formValidator({onFocus:"运营商不能为空，请选择",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"运营商不能为空，请选择"},min:1,onError:"运营商不能为空，请选择"});
</c:if>

	$("#showOrder").formValidator({onFocus:"显示顺序只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"显示顺序只支持正整数且不能为0"}).defaultPassed();
	
	// reload parent column when company change
	$('#eCompanyNo').click( function(){
		$("#eParentNo").empty();

		var cid = $(this).val(); 
		//alert("ccc: " + $(this).val());
		if (cid==null || cid=="") {
			return;
		} 

		//loadCompanyColumn(cid, "#eParentNo");		
    });
	
	// select the first default
	if($('#eCompanyNo')[0].options.length>=2) {
		$("#eCompanyNo")[0].selectedIndex = 0;
		//loadCompanyColumn( $("#eCompanyNo").val(), "#eParentNo");
	}   
	
	var JSESSIONID="<c:out value='${pageContext.session.id}'/>";
	var operatorNo = "${activeOperator.operatorNo}";
	var parameter = String.format(";jsessionid={0}?operatorNo={1}", JSESSIONID ,operatorNo);

	var settings1 = {
		flash_url : "../../js/swfupload2.5/swfupload.swf",
		flash9_url :"../../js/swfupload2.5/swfupload_fp9.swf",
		upload_url: "${path}/textmgmt/column/uploadColumnCover.action" + parameter,
		file_size_limit : "100 MB",
		file_types : "*.png;*.jpg;*.gif;",
		file_types_description : "All Files",
		file_upload_limit : 100,
		file_queue_limit : 0, // single file
//		custom_settings : {
//			progressTarget : "fsUploadProgress",
//			cancelButtonId : "btnCancel"
//		},
		debug: false,

		// Button Settings
		button_placeholder_id : "cUploadCover",
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
		//art.dialog.alert("保存成功！", function() {
		//$('#txtFileName').val(swfu1.serverData.filePath);
		var rsObj = swfu1.serverData;
		if (rsObj!="undefined") {
			if(rsObj.result==true || rsObj.result=="true"){
				
				var curFileNum = parseInt($("#fileNum").val());
				if(curFileNum>=1){
					var filePath = rsObj.filePath;
					if(filePath!=null && filePath!="") {
						var url = "${path}/textmgmt/column/columnCoverDelete.action?filePath="+filePath;
						$.ajax({type: "post",
							url: url
						});
					}
					art.dialog.alert("最多只能上传1个文件！");
					return;
				}
				var showRegion = document.getElementById("showUploadFiles");
				if(showRegion) {
					var html = '<span title="存放路径：'+rsObj.filePath+'">';
					html +='<input type="hidden" name="cover" id="contentPath" value="' + rsObj.filePath + '" />';
					html += '<img src="${path}/'+rsObj.filePath+'" value="' + rsObj.filePath + '" height="44" title="'+rsObj.fileName+'" style="cursor: pointer" />';
					html += '</span><img src="${path}/images/common/icon/icon30_14-0.gif" title="' + rsObj.filePath + '" width="20" height="20" onclick="removeFile(this)" title="删除" style="cursor: pointer" />';
					showRegion.innerHTML = showRegion.innerHTML + html;
					
				}
				$("#fileNum").val(curFileNum+1);
			}
			else {
				if(rsObj.desc == 'spaceSizeLimit'){
					art.dialog.alert("上传失败，您的使用空间不足，请与管理员联系！");
				}
				else{
					art.dialog.alert("上传失败！");
				}
			}
		}
		//});
	};
	
});

function removeFile(delImg){
	art.dialog.confirm('您确认删除操作？', function(){
		var filePath = delImg.title;
		if(filePath!=null && filePath!=""){
			var url = "${path}/textmgmt/column/columnDeleteCover.action";
			$.ajax({
				type: "post",
				async: false,
				data: {'filePath':filePath},
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

function goBack(){
	document.getElementById("form2").action = "${path}/textmgmt/column/columnList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>版块信息</h2></div>
<form id="form1" name="form1" method="post">
<input type="hidden" name="columnNo" id="columnNo" value="${column.columnNo }" />
<%-- <input type="hidden" name="createBy" id="createBy" value="${column.createBy}" /> --%>
<%-- <c:if test="${column != null}"> --%>
<%--   <input type="hidden" name="parentNo" id="parentNo" value="${column.parentNo}" /> --%>
<%-- </c:if> --%>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td class="tdBlue2" width="10%">显示顺序</td>
    <td class="tdBlue" width="40%">
      <input name="showOrder" id="showOrder" maxlength="8" value="${column.showOrder == null ? 100 : column.showOrder}" size="5">
     </td>
  </tr>
  <tr>
  
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
    	<!-- do not show select for admin -->
      	<c:if test="${activeOperator.superAdmin}">
          	<c:set var="showCompany" value="block" />
      	</c:if>
      	<c:if test="${activeOperator.superAdmin==false}">
          	<c:set var="showCompany" value="none" />
      		${companyList[0].companyName}
      	</c:if>
        
		<select id="eCompanyNo" name="companyNo" class="form130px" style="display:${showCompany}">
			<option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="u" > 
			<option value="${u.companyNo }" <c:if test="${column.companyNo==u.companyNo}"> selected="selected" </c:if>>${u.companyName}</option>
			</c:forEach> 
		</select>
    </td>
  </tr>

  <tr>
    <td width="35%" class="tdBlue2">所属版块</td>
    <td class="tdBlue">
      <select id="eParentNo" name="parentNo" class="form130px" >
        <option value="">--请选择--</option>
 		<c:forEach items="${belongList}" var="u" > 
 	      <option value="${u.columnNo }" <c:if test="${column.parentNo==u.columnNo}"> selected="selected" </c:if>>${u.columnName}</option>
 		</c:forEach>
	  </select>
    </td>
  </tr>
		    <tr>
    <td width="35%" class="tdBlue2">关联模板</td>
    <td class="tdBlue">
         <select id="templateId" name="templateId" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${templateList}" var="t" > 
			   <option value="${t.id }" <c:if test="${column.templateId==t.id }"> selected="selected" </c:if>>${t.templateName }</option>
			</c:forEach> 
		</select>
      </td>
  </tr>
  
  <tr>
    <td width="35%" class="tdBlue2">版块名称</td>
    <td class="tdBlue">
      <input name="columnName" value="${column.columnName}" id="column_columnName" maxlength="50" />
    </td>
  </tr>
  
  <tr height="80">  
	<td width="35%" class="tdBlue2">版块封面</td>
	<td class="tdBlue">
	  <span id="cUploadCover"></span>
	  	<input value="上传封面" type="button" class="btnQuery" />
<%-- 		<input type="text" value="${column.cover}" id="txtFileName" name="cover" readonly="readonly" style="border: solid 0px; background-color: transparent;"/>
 --%>		 
 		<c:if test="${column.cover == null or column.cover == ''}">
		   <input type="hidden" name="fileNum" id="fileNum" value="0" />
		</c:if>
		<c:if test="${column.cover != null and column.cover != ''}">
		   <input type="hidden" name="fileNum" id="fileNum" value="1" />
		</c:if> 
		<div id="fileNumTip" style="width:250px; float:left;"></div> 
		<div id="showUploadFiles" style="width:100%; float:left; vertical-align:bottom"></div>
	</td>
  </tr> 
  
  <tr>
    <td width="35%" class="tdBlue2">版块描述</td>
    <td class="tdBlue">
	  <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 500)" id="column_columnDesc" name="columnDesc">${column.columnDesc}</textarea>
	</td>
  </tr>
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="path"  value="${search.path}" />
<input type="hidden" name="columnName"  value="${search.columnName}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
