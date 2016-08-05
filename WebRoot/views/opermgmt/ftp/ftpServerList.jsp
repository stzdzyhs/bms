<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>FTP服务器列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#ip").keyup(function(){
		var ip = $(this).val();
		var ipReg = /^[0-9.]*$/;
		if (ip != null && ip != '' && !ipReg.test(ip)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
});

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='ftpServerIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	} else {
		var aa = document.getElementsByName("ftpServerIds");
		for (var i=0; i<aa.length; i++)
			aa[i].checked = false;
	}
	
	var param = id != '' ? "?ftpServerIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/opermgmt/ftp/ftpServerDelete.action"+param,
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
						art.dialog.alert("删除失败！");
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/opermgmt/ftp/ftpServerList.action";
	document.getElementById("form1").submit();
}
//进入新增编辑页面
function toEditPage(ftpServerId){
	if(ftpServerId==null || typeof(ftpServerId)=="undefined"){
		ftpServerId = "";
	}
	$("#ftpServerId").val(ftpServerId);
	document.getElementById("form1").action = "${path}/opermgmt/ftp/ftpServerEdit.action";
	document.getElementById("form1").submit();
}

function browseFtpServer(ftpServerId){
	
	art.dialog.open("${path}/opermgmt/ftp/browseFtpServer.action?select=false&ftpServerId=" + ftpServerId,
			{
				title: "FTP服务器文件列表", 
				width: 950,
				height: 450,
				lock: true,
				opacity:0.0
			});
		return this;
}
</script>
</head>
<body>
<div class="title">
  <h2>FTP服务器列表</h2>
</div>
<form action="${path}/opermgmt/ftp/ftpServerList.action" method="post" name="form1" id="form1">
<input type="hidden" name="ftpServerId" id="ftpServerId" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <c:if test="${activeOperator.type == 0}">
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${companyList}" var="u" > 
							<option value="${u.companyNo }" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                 </c:if>
                 <c:if test="${activeOperator.type == 0 || activeOperator.type == 1}">
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.operatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  </c:if>
				  <td width="100px" height="30">服务器地址</td>
                  <td width="150px">
                     <input id="ip" name="ip" class="form130px" value="${search.ip}"  maxlength="50" 
                     onMouseOver="toolTip('例如：172.16.201.18')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td><input id="btnQuery"  class="btnQuery" name="" type="submit" value="查询"></td>
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
    <c:set var="add" value="false" />
    <c:set var="delete" value="false" />
    <c:set var="edit" value="false" />
    <c:set var="browse" value="false" />
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
			 <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
			 <c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'delete'}">   
			<c:set var="delete" value="true" />  
		</c:if> 
		<c:if test="${item eq 'edit'}">   
			<c:set var="edit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'browse'}">   
			<c:set var="browse" value="true" />  
		</c:if> 
	</c:forEach>
	<c:if test="${delete}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'ftpServerIds')"></th>
          <th width="*%">服务器地址</th>
          <th width="*%">端口</th>
          <th width="*%">用户名</th>
          <th width="*%">密码</th>
          <th width="*%">所属运营商</th>
          <th width="*%">创建用户</th>
          <th width="*%">创建时间</th>
          <th width="*%">更新时间</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="10">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      	<c:forEach items="${list}" var="f">
        <tr>
          <td align="center"><input type="checkbox" name="ftpServerIds" value="${f.id }">
          </td>
          <td align="center">${f.ip}</td>
          <td align="center">${f.port}</td>
          <td align="center">${f.userName }</td>
          <td align="center">${f.password }</td>
          <td align="center">
           ${f.company.companyName}
          </td>
          <td align="center">
             ${f.operator.operatorName}
          </td>
          <td align="center">${f.createTime}</td>
          <td align="center">${f.updateTime}</td>
          <td class="tdOpera2" align="center">
             <c:if test="${browse}"><a href="javascript:;" onclick="browseFtpServer('${f.id }')">浏览</a></c:if>
             <c:if test="${edit}"><a href="javascript:;" onclick="toEditPage('${f.id }')">编辑</a></c:if>
             <c:if test="${delete}"> <a href="javascript:;" onclick="deleteSets('${f.id }')">删除</a></c:if>
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
