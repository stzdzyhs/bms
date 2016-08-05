<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户详情</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
});
</script>
</head>
<body>
  	<table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td width="20%" class="tdBlue2">用户账号</td>
			<td class="tdBlue">${ operator.operatorId}</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">用户名称</td>
			<td class="tdBlue">${ operator.operatorName}</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">用户类型</td>
			<td class="tdBlue">
             <c:forEach items="${userTypeMap}" var="m" > 
					<c:if test="${operator.type==m.key }"> ${m.value }</c:if>
		    </c:forEach>
			</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">用户状态</td>
			<td class="tdBlue">
			<c:if test="${operator.status == 0}">
                                                启用
              </c:if>
             <c:if test="${operator.status == 1}">
                                                禁用
             </c:if>
			</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">所属运营商</td>
			<td class="tdBlue">
			${operator.company.companyName}
			</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">电子邮箱</td>
			<td class="tdBlue">
			 ${operator.operatorEmail}
			</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">电话号码</td>
			<td class="tdBlue">${operator.operatorTel}</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">创建者</td>
			<td class="tdBlue">${operator.operator.operatorName}</td>
		</tr>
	    <tr>
			<td width="20%" class="tdBlue2">创建时间</td>
			<td class="tdBlue">${operator.createTime}</td>
		</tr>
		<tr>
			<td width="20%" class="tdBlue2">拥有角色</td>
			<td class="tdBlue">
		    <fieldset style="width: 90%" id="channelFSet">
			<legend id="num_showChannels">
				<c:if test="${operator.roles == null }">
					已选角色0个
				</c:if>
				<c:if test="${operator.roles != null }">
					已选角色${fn:length(operator.roles) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showChannels" style="display: none">
				<c:forEach items="${operator.roles}" var="role">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${role.roleNo }" name="channelNo" id="channelNo" /> ${role.roleName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	        </fieldset>
			</td>
		</tr>
		<c:if test="${operator.type == 2}">
		<tr>
			<td width="20%" class="tdBlue2">拥有运营商</td>
			<td class="tdBlue">
		    <fieldset style="width: 90%" id="channelFSet">
			<legend id="num_showChannels">
				<c:if test="${operator.companys == null }">
					已选运营商0个
				</c:if>
				<c:if test="${operator.companys != null }">
					已选运营商${fn:length(operator.companys) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showCompanys" style="display: none">
				<c:forEach items="${operator.companys}" var="cmpy">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${cmpy.companyNo }" name="cmpyNo" id="cmpyNo" /> ${cmpy.companyName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	        </fieldset>
			</td>
		</tr>
		</c:if>
	    <tr>
	    <td width="20%" class="tdBlue2">可用空间</td>
	    <td class="tdBlue">
	     <c:if test="${operator.totalSize != -1 }">
	       <c:choose>
	        <c:when test="${operator.usedSize == 0 }">
	         ${operator.totalSize }G
	       </c:when>
	       <c:otherwise>
	            ${operator.usedSizeFmt } / ${operator.totalSize }G
	       </c:otherwise>
	       </c:choose>
	     </c:if>
	     <c:if test="${operator.totalSize == -1 }">
	      	 不限制
	     </c:if>
	       </td>
	   </tr>
	   <tr>
			<td width="20%" class="tdBlue2">用户描述</td>
			<td class="tdBlue">${operator.operatorDescribe}</td>
		</tr>
		<c:if test="${allocCommands != null }">
			<tr>
			<td width="20%" class="tdBlue2">拥有权限</td>
			<td class="tdBlue">
		    <fieldset style="width: 90%" id="cmdFSet">
			<legend id="num_showAllocCmd">
				<c:if test="${allocCommands == null }">
					拥有权限0个
				</c:if>
				<c:if test="${allocCommands != null }">
					拥有权限${fn:length(allocCommands)}个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showCmds" style="display: none">
				<c:forEach items="${allocCommands}" var="cmd">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${cmd.id }" name="cmdId" id="cmdId" /> ${cmd.cmdName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	        </fieldset>
			</td>
		</tr>
		</c:if>
	</table>
</body>
</html>