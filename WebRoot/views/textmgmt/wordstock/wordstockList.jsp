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
<title>生僻字列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<!-- 上传控件 -->
<script src="${path}/js/swfupload2.5/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload2.5/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/newUploadHandler0.js" type="text/javascript"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	$("#word").keyup(function(){
		var word = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]{1}$/;
		if (word != null && word != '' && !resNameReg.test(word)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	//上传组件初始化
	var swfu3;
	var settings3 = {
		flash_url : "../../js/swfupload2.5/swfupload.swf",
		flash9_url :"../../js/swfupload2.5/swfupload_fp9.swf",
		upload_url: "${path}/textmgmt/wordstock/uploadWordstocks.action",
		file_size_limit : "10 MB",
		file_types : "*.txt",
		file_types_description : "All Files",
		file_upload_limit : 100,
		file_queue_limit : 0, // single file
		debug: false,

		// Button Settings
		button_placeholder_id : "wUploadBody",
		button_width: 61,
		button_height: 22,
		button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
		button_cursor: SWFUpload.CURSOR.HAND,
	};
	swfu3 = createFileUpload(settings3);
	swfu3.beforeUpload=function(queue) {
		if(queue.length>1) {
			art.dialog.alert("请只选择一个文件");
			return false;
		}
		return true;
	};
	swfu3.afterUploadSucc = function() {
		art.dialog.alert("文件上传成功，确定保存？", function() {
			$('#txtFileName').val(swfu3.serverData.filePath);
			saveToDb();
		});
	};	

});

//生僻字入库
function saveToDb () {
	var fileName = $('#txtFileName').val();
	var options = {
			url: "${path}/textmgmt/wordstock/saveToDb.action?fileName="+fileName,
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
		jQuery('#wordstockForm').ajaxSubmit(options);
}

//提交表单
function deleteSets(id) {
	var url="${path}/textmgmt/wordstock/wordstockDelete.action";
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	}
	else {
		var rtId = document.getElementsByName("rtId");
		for (var i=0; i<rtId.length; i++) {
			rtId[i].checked = false;
		}
		url="${path}/textmgmt/wordstock/wordstockDelete.action?rtId="+id;
	}
	art.dialog.confirm('你确认删除操作？', function(){
		var options = {
			url: url,
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
					art.dialog.alert("删除成功！", goBack);
				}
				else{
					art.dialog.alert("删除失败! " + rsobj.desc);
				}
			}
		};
		jQuery('#wordstockForm').ajaxSubmit(options);
	}, function(){
		art.dialog.tips('您取消了操作！');
	});
}

function goBack(){
	document.getElementById("wordstockForm").action = "${path}/textmgmt/wordstock/wordstockList.action";
	document.getElementById("wordstockForm").submit();
}
//进入新增编辑页面
function toEditPage(id){
	if(id==null || typeof(id)=="undefined") {
		id = "";
	}
	$("#id").val(id);
	document.getElementById("wordstockForm").action = "${path}/textmgmt/wordstock/wordstockEdit.action";
	document.getElementById("wordstockForm").submit();
}

function detail(id){
	art.dialog.open("${path}/textmgmt/wordstock/wordstockDetail.action?wordstockNo=" + id, {
		title: "生僻字详情", 
		width: 450,
		height: 400,
		lock: true
	});
}
//上传模板下载
function download(){
	//alert(121);
	document.getElementById("form3").action = "${path}/textmgmt/wordstock/downloadwordstockTemplate.action?fileName=wordstock.zip";
	document.getElementById("form3").submit();
} 
</script>
</head>
<body>
<div class="title">
  <h2>生僻字列表</h2>
</div>
<form id="form3" name="form3" method="post">
 </form>
<form id="form2" name="form2" method="post">
	<input type="hidden" id="txtFileName" name="txtFileName"/>
</form>
<form action="${path}/textmgmt/wordstock/wordstockList.action" method="post" name="wordstockForm" id="wordstockForm">
<input type="hidden" name="id" id="id" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">生僻字:</td>
                  <td width="160">
                  	<input id="word" name="word" class="form120px" value="${search.word}" 
                  	onMouseOver="toolTip('请输入一个生僻字')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                	<td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"/></td>
                	<td width="90px" height="30">上传生僻字:</td>
                	<td width="160">		
						<span id="wUploadBody"></span>
						<input value="上传" type="button" class="btnQuery" />
					</td>
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
    <c:set var="dist" value="false" />
    <c:set var="region" value="false" />
    
    <c:set var="submit" value="false" />
    <c:set var="approved" value="false" />
    <c:set var="rejected" value="false" />
    <c:set var="publish" value="false" />
    <c:set var="unpublish" value="false" />
    <c:set var="download" value="false" />
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
		<c:if test="${item eq 'download'}">   
			<c:set var="download" value="true" />  
		</c:if>
	</c:forEach>
	
    <c:if test="${add}">   
		 <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
	</c:if>
    <c:if test="${delete}">   
	    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
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
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">生僻字</th>
          <th width="*%">创建者</th>
          <th width="*%">创建时间</th>
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
			<input type="checkbox" name="rtId" value="${u.wordNo }">
          </td>
          <td align="center">${u.word}</td>
          <td align="center">${u.operator.operatorName}</td>
          <td align="center">${u.createTime}</td>
          
          <td class="tdOpera2" align="center">
          <c:if test="${edit}"><a href="javascript:;" onclick="toEditPage(${u.wordNo})">编辑</a></c:if>
          <c:if test="${delete}">
                <a href="javascript:;" onclick="deleteSets(${u.wordNo})">删除</a>
         </c:if>
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
    </div>
    <!--对表格数据的操作 end-->
  </div>

</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
