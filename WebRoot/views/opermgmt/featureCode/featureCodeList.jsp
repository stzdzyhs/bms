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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>特征码列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<!-- 上传组件 -->
<script src="${path}/js/swfupload2.5/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload2.5/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/newUploadHandler0.js" type="text/javascript"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">

function beforeDialogOpen() {
	alert("before dialog open");
	return false;
}
function loadedHandler() {
	//alert("loaded");
	var function1 = $("#eUploadBody").click;
	if(function1==null) {
		alert("????");
	}
	
	$("#eUploadBody").click(function() {
		var x = beforeDialogOpen();
		if(x==true) {
			if(function1!=null) {
				function1();
			}
		}
		else {
			alert("dilog is not open...");
		}
		
	});
}
$(document).ready(function(){
	$("#featureCodeVal").keyup(function(){
		var featureCodeVal = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (featureCodeVal != null && featureCodeVal != '' && !resNameReg.test(featureCodeVal)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	$("#featureCodeType").keyup(function(){
		var featureCodeType = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (featureCodeType != null && featureCodeType != '' && !resNameReg.test(featureCodeType)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	//初始化上传组件
	var settings1 = {
		flash_url : "../../js/swfupload2.5/swfupload.swf",
		flash9_url :"../../js/swfupload2.5/swfupload_fp9.swf",
		upload_url: "${path}/opermgmt/featureCode/uploadFeatureCode.action",
		file_size_limit : "100 MB",
		file_types : "*.txt",
		file_types_description : "All Files",
		file_upload_limit : 100,
		file_queue_limit : 0, // single file
		debug: false,

		// Button Settings
		button_placeholder_id : "eUploadBody",
		button_width: 61,
		button_height: 22,
		button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
		button_cursor: SWFUpload.CURSOR.HAND,
		
		button_action : SWFUpload.BUTTON_ACTION.NONE,
		swfupload_loaded_handler: loadedHandler
	};

	swfu1 = createFileUpload(settings1);
	swfu1.beforeUpload=function(queue) {
		if(queue.length>1) {
			art.dialog.alert("请选择一个文件");
			return false;
		}
		return true;
	};
	swfu1.afterUploadSucc = function() {
		art.dialog.alert("文件上传成功，确定保存？", function (){
			$('#txtFileName').val(swfu1.serverData.filePath);
			saveToDb();
		});
	}
	
	var function1 = $("#eUploadBody").click;
	if(function1==null) {
		alert("????");
	}
	
	$("#eUploadBody").click(function() {
		var x = beforeDialogOpen();
		if(x==true) {
			if(function1!=null) {
				function1();
			}
		}
		else {
			alert("dilog is not open...");
		}
		
	});
	
});	

//特征码入库
function saveToDb () {
	var fileName = $('#txtFileName').val(); 
	var options = {
		url: "${path}/opermgmt/featureCode/saveToDb.action?fileName="+fileName,
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
			art.dialog.alert("保存失败！");
		},
		success: function(data) {
			art.dialog.list['broadcastLoading'].close();
			eval("var rsobj = "+data+";");
			if(rsobj.result){
				art.dialog.alert("保存成功！", goBack);
			}else{
				art.dialog.alert("保存失败！" +rsobj.desc);
			}
		}
	};
	jQuery('#form1').ajaxSubmit(options);
}

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='feaNos']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		for (var i=0; i<sets.length; i++){
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus == 1){
				art.dialog.alert("不能直接删除启用状态的特征码！");
				return false;
			}
		}
	} else {
		var aa = document.getElementsByName("feaNos");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus == 1){
			art.dialog.alert("不能直接删除启用状态的特征码！");
			return false;
		}
	}

	var param = id != '' ? "?feaNos="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/opermgmt/featureCode/featureCodeDelete.action"+param,
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
					if(rsobj.result=="true"){
						art.dialog.alert("删除成功！", goBack);
					}else{
						if(rsobj.desc == 'reference'){
							art.dialog.alert("该特征码被策略引用，不能删除！");
						}else{
							art.dialog.alert("删除失败！");
						}
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}
function goBack() {
	document.getElementById("form1").action = "${path}/opermgmt/featureCode/featureCodeList.action";
	document.getElementById("form1").submit();
}

//进入新增编辑页面
function toEditPage(featureCodeNo){
	var oldStatus = $("#oldStatus" + featureCodeNo).val();
	if (oldStatus == 1){
		art.dialog.alert("不能编辑启用状态的特征码！");
		return false;
	}
	if(featureCodeNo==null || typeof(featureCodeNo)=="undefined"){
		featureCodeNo = "";
	}
	$("#featureCodeNo").val(featureCodeNo);
	document.getElementById("form1").action = "${path}/opermgmt/featureCode/featureCodeEdit.action";
	document.getElementById("form1").submit();
}

//详情
function detail(featureCodeNo) {
	art.dialog.open("${path}/opermgmt/featureCode/featureCodeDetail.action?featureCodeNo=" + featureCodeNo, 
		{
			title: "特征码详情", 
			width: 450,
			height: 300,
			lock: true
		});
}

function featureCodeAudit(status) {
	var feaNos = $("input[name='feaNos']:checked");
	if(feaNos.length<=0){
		if (status == 1){
			art.dialog.alert("请选择需要启用的选项！");
		}else if (status == 0){
			art.dialog.alert("请选择需要禁用的选项！");
		}
		return false;
	}
	
	/* var type = ${activeOperator.type};
	if (type == 2){
		if (status == 1){
			art.dialog.alert("您没有启用Portal的权限！");
		}else if (status == 2){
			art.dialog.alert("您没有禁用Portal的权限！");
		}

		return false;
	} */
	
	art.dialog.confirm('你确认该操作？', function(){
		var options = {
			url: "${path}/opermgmt/featureCode/featureCodeAudit.action?status=" + status,
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
				if (status == 1){
					art.dialog.alert("启用失败！",goBack);
				}else if (status == 0){
					art.dialog.alert("禁用失败！",goBack);
				}
			},
			success: function(data) {
				art.dialog.list['broadcastLoading'].close();
				eval("var rsobj = "+data+";");
				if(rsobj.result=="true"){
					if (status == 1){
						art.dialog.alert("启用成功！",goBack);
					}else if (status == 0){
						art.dialog.alert("禁用成功！",goBack);
					}

				}else{
					if (status == 1){
						art.dialog.alert("启用失败！",goBack);
					}else if (status == 0){
						art.dialog.alert("禁用失败！",goBack);
					}
				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
	}, function(){
    art.dialog.tips('你取消了操作！');
});
}

//上传模板下载
function download(){
	//alert(121);
	document.getElementById("form3").action = "${path}/opermgmt/featureCode/downloadFeatureCodeTemplate.action?fileName=featureCode.zip";
	document.getElementById("form3").submit();
} 
</script>
</head>

<body>
<div class="title">
<h2>特征码列表</h2>
</div>
<form id="form3" name="form3" method="post">
 </form>
 <form id="form2" name="form2" method="post">
 	<input type="hidden" id="txtFileName" name="txtFileName"/>
 </form>
<form action="${path}/opermgmt/featureCode/featureCodeList.action" method="post" name="form1" id="form1">
<input type="hidden" name="featureCodeNo" id="featureCodeNo" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">特征码值：</td>
                  <td width="160">
                  	<input id="featureCodeVal" name="featureCodeVal" class="form120px" value="${search.featureCodeVal }" 
                  	onMouseOver="toolTip('特征码值由字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>

                  <td width="90px" height="30">特征值类型：</td>
                  <td width="160">
                  	<select name="featureCodeType" id="featureCodeType" class="form130px">
			    		<option value="">--请选择--</option>
			    		<option value="0" <c:if test="${search.featureCodeType=='0'}">selected</c:if>>喜好特征</option>
			    		<option value="1" <c:if test="${search.featureCodeType=='1'}">selected</c:if>>地理位置</option>
			    		<option value="2" <c:if test="${search.featureCodeType=='2'}">selected</c:if>>大客户</option>
			    		<option value="3" <c:if test="${search.featureCodeType=='3'}">selected</c:if>>年龄特征</option>
			    	</select>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  	<select name="status" class="form130px" id="status">
	                  	<option value="">--请选择--</option>
	                  	<option value="0" <c:if test="${search.status=='0'}">selected</c:if>>禁用</option>
	                  	<option value="1" <c:if test="${search.status=='1'}">selected</c:if>>启用</option>
	                </select>
                  </td>
                  <td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"/></td>
                         	 
                  <td width="90px" height="30">上传特征码：</td>
			        <td width="160">
			    		<span id="eUploadBody"></span>
			    		<input value="上传" type="button" class="btnQuery" />
			    	</td>  	
			      <!-- <td width="90px" height="30">上传模板选择：</td>
                  <td width="160">
                  <select name="logFile" class="form130px" id="logFile">
                  	<option value="">--请选择--</option>
                  	<option value="1">文本模板</option>
                  </select>
                 </td>
                 <td><input class="btnQuery" type="button" value="下载" onclick="download();"></td>  -->
               </tr>
              </tbody>
            </table>
          </td>
          <td class="searchRight"></td>
        </tr>
        <tr>
          <td class="searchButtomLeft"></td>
          <td class="searchButtom"></td>
          <td class="searchButtoRight"></td>
        </tr>
      </tbody>
    </table>
  </div>
  <!--标题 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
	<c:set var="add" value="false" />
    <c:set var="delete" value="false" />
    <c:set var="edit" value="false" />
    <c:set var="detail" value="false" />
    <c:set var="enable" value="false" />
    <c:set var="disable" value="false" />
	<c:set var="download" value="false" />
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
			<input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
			<c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'detail'}">
			<c:set var="detail" value="true"/>
		</c:if>
		<c:if test="${item eq 'edit'}">
			<c:set var="edit" value="true"></c:set>
		</c:if>
		<c:if test="${item eq 'delete'}">   
			<c:set var="delete" value="true" />  
		</c:if>  
		<c:if test="${item eq 'enable'}">   
			<c:set var="enable" value="true" />  
		</c:if> 
		<c:if test="${item eq 'disable'}">   
			<c:set var="disable" value="true" />  
		</c:if> 
		<c:if test="${item eq 'download'}">   
			<c:set var="download" value="true" />  
		</c:if> 
	</c:forEach>
		<c:if test="${delete}">   
			<input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
		</c:if> 
		<c:if test="${enable}">   
			<input type="button" class="btn btn80" value="启用" onclick="featureCodeAudit(1)">
		</c:if> 
		<c:if test="${disable}">   
			<input type="button" class="btn btn80" value="禁用" onclick="featureCodeAudit(0)">
		</c:if> 
		<c:if test="${download}">   
			<input type="button" class="btn btn80" value="模板下载" onclick="download()">
		</c:if>
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'feaNos')"></th>
          <th width="*%">特征码值</th>
          <th width="*%">特征码描述</th>
          <th width="*%">特征码类型</th>
          <th width="*%">创建者</th>
          <th width="*%">创建时间</th>
          <th width="*%">状态</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="8">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      <c:forEach items="${list}" var="u">
        <tr>
          <td align="center">
	          <c:if test="${u.featureCodeNo != null && u.featureCodeNo != -1}">
	          	<input type="checkbox" name="feaNos" value="${u.featureCodeNo }">
	          	<input type="hidden" name="oldStatus${u.featureCodeNo}" id="oldStatus${u.featureCodeNo}" value="${u.status}" />
	          </c:if>
          </td>
          <td align="center">${u.featureCodeVal }</td>
          <td align="center">${u.featureCodeDesc}</td>
          <td align="center">
          	<c:if test="${u.featureCodeType=='0'}">喜好特征</c:if>
          	<c:if test="${u.featureCodeType=='1'}">地理特征</c:if>
          	<c:if test="${u.featureCodeType=='2'}">大客户</c:if>
          	<c:if test="${u.featureCodeType=='3'}">年龄特征</c:if>
          </td>
          <td align="center">${u.operator.operatorName}</td>
          <td align="center">${u.createTime}</td>
          <td align="center">
          	<c:if test="${u.status==0}">禁用</c:if>
          	<c:if test="${u.status==1}">启用</c:if>
          </td>
          <td class="tdOpera2" align="center">
	         <c:if test="${detail}"><a href="javascript:;" onclick="detail('${u.featureCodeNo}')">详情</a></c:if>
             <c:if test="${edit}"><a href="javascript:;" onclick="toEditPage('${u.featureCodeNo}')">编辑</a></c:if>
             <c:if test="${delete}"> <a href="javascript:;" onclick="deleteSets('${u.featureCodeNo}')">删除</a></c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
  </form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
	<div class="operation">
		<c:if test="${add}">     
			<input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
		</c:if> 
		<c:if test="${delete}">   
			<input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
		</c:if> 
		<c:if test="${enable}">   
		    <input type="button" class="btn btn80" value="启用" onclick="featureCodeAudit(1)">
		</c:if> 
		<c:if test="${disable}">   
		    <input type="button" class="btn btn80" value="禁用" onclick="featureCodeAudit(0)">
		</c:if>
		<c:if test="${download}">   
			<input type="button" class="btn btn80" value="模板下载" onclick="download()">
		</c:if> 
	</div>
    <!--对表格数据的操作 end-->
  </div>

</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
