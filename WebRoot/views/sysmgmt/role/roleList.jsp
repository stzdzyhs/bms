<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>角色管理</title>
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
	$("#roleId").keyup(function(){
		var roleId = $(this).val();
		var roleIdReg = /^\w+$/;
		if (roleId != null && roleId != '' && !roleIdReg.test(roleId)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	$("#roleName").keyup(function(){
		var roleName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (roleName != null && roleName != '' && !resNameReg.test(roleName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	} else {
		var aa = document.getElementsByName("rtId");
		for (var i=0; i<aa.length; i++)
			aa[i].checked = false;
	}
	
	var type = ${activeOperator.type};
	if (type == 2){
		art.dialog.alert("您没有删除角色的权限！");
		return false;
	}
	
	var param = id != '' ? "?rtId="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/sysmgmt/role/roleDelete.action" + param,
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
						if (rsobj.desc=='reference') {
							art.dialog.alert("该角色被操作员引用，不能删除！");
						} else {
							art.dialog.alert("删除失败！");
						}
					}
				}
			};
			jQuery('#roleForm').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作');
	});
}

function goBack(){
	document.getElementById("roleForm").action = "${path}/sysmgmt/role/roleList.action";
	document.getElementById("roleForm").submit();
}
//进入新增编辑页面
function toEditPage(id){
	var type = ${activeOperator.type};
	if (type == 2){
		art.dialog.alert("您没有添加或编辑角色的权限！");
		return false;
	}
	
	if(id==null || typeof(id)=="undefined"){
		id = "";
	}
	$("#id").val(id);
	document.getElementById("roleForm").action = "${path}/sysmgmt/role/roleEdit.action";
	document.getElementById("roleForm").submit();
}
//进入授权页面
function toAuthorizePage(type, id){
	
	var operType = ${activeOperator.type};
	if (operType == 2){
		art.dialog.alert("您没有分配权限的权限！");
		return false;
	}
	
	if(id==null || typeof(id)=="undefined"){
		id = "";
	}
	$("#id").val(id);
	if (type=="1") {
		document.getElementById("roleForm").action = "${path}/sysmgmt/role/permission.action";
	} else if (type=="2") {
		document.getElementById("roleForm").action = "${path}/sysmgmt/role/authorization.action";
	}
	document.getElementById("roleForm").submit();
}

function detail(id){

	art.dialog.open("${path}/sysmgmt/role/roleDetail.action?roleNo=" + id, 
			{
				title: "角色详情", 
				width: 450,
				height: 300,
				lock: true

			});
}


//选择广告位
function selectPosition(roleId){
	art.dialog.open("${path}/advmgmt/advpst/advPositionRoleSelect.action?roleId="+roleId, 
			{
				title: "选择广告位", //'请选择频道'
				width: 800,
				height: 650,
				lock: true,
				close:function(){
					$("#advPositionId").val(art.dialog.data('ids'));
 					$("#advPositionName").val(art.dialog.data('names'));
 					$("#advPositionType").val(art.dialog.data('types'));					
					art.dialog.data('ids','');
					art.dialog.data('names','');
					art.dialog.data('types','');
				}
			});
}
</script>
</head>
<body>
<div class="title">
  <h2>角色列表</h2>
</div>
<form action="${path}/sysmgmt/role/roleList.action" method="post" name="roleForm" id="roleForm">
<input type="hidden" name="id" id="id" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                 <c:if test="${activeOperator.type ==0 || activeOperator.type==1}">
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${belongList}" var="u" > 
							<option value="${u.companyNo}" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                 </c:if>
                  <td width="90px" height="30">角色ID：</td>
                  <td width="160"><input class="form120px" type="text" id="roleId" name="roleId" maxlength="20" value="${search.roleId}" autocomplete="off" oncontextmenu="return false;"></td>
                  <td width="90px" height="30">角色名称：</td>
                  <td width="160"><input class="form120px" type="text" id="roleName" name="roleName" maxlength="50" value="${search.roleName}"
                  onMouseOver="toolTip('角色名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"></td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                   <select name="status" class="form130px"  >
	                  	<option value="">--请选择--</option>
	                  	<option value="0" <c:if test="${search.status==0 }"> selected="selected" </c:if>>启用</option>
	                  	<option value="1" <c:if test="${search.status==1 }"> selected="selected" </c:if>>禁用</option>
				  </select>
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
    <c:set var="add" value="false" />
    <c:set var="delete" value="false" />
    <c:set var="edit" value="false" />
    <c:set var="permission" value="false" />
    <c:set var="authorization" value="false" />
    <c:set var="detail" value="false" />
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
			 <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
			 <c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'edit'}">   
			<c:set var="edit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'permission'}">   
			<c:set var="permission" value="true" />  
		</c:if> 
		<c:if test="${item eq 'authorization'}">   
			<c:set var="authorization" value="true" />  
		</c:if> 
		<c:if test="${item eq 'detail'}">   
			<c:set var="detail" value="true" />  
		</c:if> 
	</c:forEach>
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'delete'}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
			<c:set var="delete" value="true" />  
		</c:if> 
	</c:forEach>
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">角色ID</th>
          <th width="*%">角色名称</th>
          <th width="*%">状态</th>
          <th width="*%">运营商</th>
          <th width="*%">创建用户</th>
          <th width="*%">创建时间</th>
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
      	<c:forEach items="${list}" var="u">
        <tr>
          <td align="center"><input type="checkbox" name="rtId" value="${u.roleNo }"></td>
          <td align="center">${u.roleId }</td>
          <td align="center">${u.roleName }</td>
          <td align="center">
            <c:if test="${u.status == 0}">
                                             启用
            </c:if>
            <c:if test="${u.status == 1}">
                                             禁用
            </c:if>
          </td>
          <td align="center">
            <c:if test="${u.company == null && u.companyNo == -1}">
                                               超级管理员
            </c:if>
            <c:if test="${u.company != null}">
                 ${u.company.companyName }
            </c:if>
          </td>
          <td align="center">
             <c:if test="${u.operator != null}">
                ${u.operator.operatorId }
             </c:if>
          </td>
          <td align="center">${u.createTime }</td>
          <td class="tdOpera2" align="center">
          <c:if test="${detail}"><a href='javascript:;' onclick='detail(${u.roleNo})'>详情</a></c:if>
          <c:if test="${edit && activeOperator.type != 2}"><a href="javascript:;" onclick="toEditPage('${u.roleNo }')">编辑</a></c:if>
          <c:if test="${delete && activeOperator.type != 2}"> <a href="javascript:;" onclick="deleteSets('${u.roleNo }')">删除</a></c:if>
          <c:if test="${permission && activeOperator.type != 2}"> <a href="javascript:;" onclick="toAuthorizePage('1', '${u.roleNo }')">权限分配</a></c:if>
          <c:if test="${authorization}"> <a href="javascript:;" onclick="selectPosition('${u.roleNo }')<%-- toAuthorizePage('2', '${u.roleNo }') --%>">资源授权</a></c:if></td>
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
  <div class="hidden" id="parentNames"></div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>