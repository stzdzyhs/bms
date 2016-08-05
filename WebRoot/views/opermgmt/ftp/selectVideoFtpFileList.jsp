<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>FTP服务器文件列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/opermgmt/ftp/ftp.js"></script>
<script type="text/javascript">

function toSelect(fileName,fileSize){
	var name = fileName;
	var index = fileName.lastIndexOf('.');
	if (index != -1){
		name = fileName.substring(0,index);
	}
	artDialog.open.origin.artDialog.open.origin.document.getElementById('videoName').value = name;
	artDialog.open.origin.artDialog.open.origin.document.getElementById('sourceUrl').value = '${videoFileUrl}' + fileName;
	artDialog.open.origin.artDialog.open.origin.document.getElementById('userName').value = '${ftpServer.userName}'
	artDialog.open.origin.artDialog.open.origin.document.getElementById('password').value = '${ftpServer.password}';
	 $.each(artDialog.open.origin.artDialog.open.origin.art.dialog.list, function (index, item) {

         item.close();

     });

	      
}

function toBrowse(path, name){
	$("#workDirectory_id").val(path + "/" + name);
	document.getElementById("ftpServerForm").action = "${path}/picmgmt/video/selectVideoFtpFileList.action?first=true";
	document.getElementById("ftpServerForm").submit();
}

function previousDir(){
	var workDirectory = $("#workDirectory_id").val() + "/..";
	$("#workDirectory_id").val(workDirectory);
	document.getElementById("ftpServerForm").action = "${path}/picmgmt/video/selectVideoFtpFileList.action";
	document.getElementById("ftpServerForm").submit();
}

function rootDir(){
	$("#workDirectory_id").val(".");
	document.getElementById("ftpServerForm").action = "${path}/picmgmt/video/selectVideoFtpFileList.action";
	document.getElementById("ftpServerForm").submit();
}
</script>
</head>
<body>
<div class="title">
  <h2>FTP服务器文件列表</h2>
</div>
<form action="${path}/picmgmt/video/selectVideoFtpFileList.action" method="post" name="ftpServerForm" id="ftpServerForm">
<input type="hidden" name="ftpServerId" id="ftpServerId" value="${ftpServerId}" />
<input type="hidden" name="workDirectory" id="workDirectory_id" value="${workDirectory}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">名称：</td>
                  <td width="160">
                     <input name="name" class="form130px" value="${search.name}"  />
                  </td>
                  <td><input class="btnQuery" name="" type="submit" value="查询"></td>
                </tr>
              </tbody>
            </table></td>
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
      <input class="btn btn80" type="button" value="主目录" onclick="rootDir()">
        <input class="btn btn80" type="button" value="上一级" onclick="previousDir()">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="*%">名称</th>
          <th width="*%">类型</th>
          <th width="20%">文件大小（字节）</th>
          <th width="*%">更新时间</th>
          <th width="*%">操作</th> 
        </tr>
      </thead>
      <tbody>
      <c:if test="${subFileList == null || fn:length(subFileList) == 0}">
		<tr>
			<td colspan="10">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      	<c:forEach items="${subFileList}" var="f">
        <tr>
          <td align="center">${f.name}</td>
          <td align="center">
          <c:if test="${f.type == 0 }">
                             文件
          </c:if>
         <c:if test="${f.type == 1 }">
                             目录
          </c:if>
          </td>
          <td align="center">${f.size }</td>
          <td align="center">${f.updateTime }</td>
          <td class="tdOpera2" align="center">
         <c:if test="${f.type == 0}">
            <a href="javascript:;" onclick="toSelect('${f.name}','${f.size}')">选择</a>
          </c:if> 
          <c:if test="${f.type == 1}">
             <a href="javascript:;" onclick="toBrowse('${f.path}', '${f.name }')">浏览</a>
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
      <input class="btn btn80" type="button" value="主目录" onclick="rootDir()">
         <input class="btn btn80" type="button" value="上一级" onclick="previousDir()">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
