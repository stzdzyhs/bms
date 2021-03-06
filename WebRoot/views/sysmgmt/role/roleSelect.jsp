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
<script src="${path}/js/win/contentWindow.js"></script>
<script src="${path}/js/win/json.js"></script>
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

//弹出窗口 begin 
String.prototype.trim=function(){
　　 return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
}
/*当此页面被用作弹出窗口时,
*初始化已经被
*选中的社区*/
$(document).ready(function(){
	ContentWindow.register(null,null,'selectedId');
	initWinSelect();
})


function initWinSelect(){
	var json = art.dialog.data('ChannelData');
	if(json!=null && typeof(json)!='undefined' && json!=""){
		var dgData = eval(json);
		var items = dgData.items;
		for(var i in items){
			$("input[name='selectedId'][value='"+items[i].channelNo+"']").attr("checked",true);
		}
	}
}
/*当此页面被用作弹出窗口时,
*获取已选择的社区
*/
function getSelectItems(){
	var json = "", checkNos = "", unCheckNos = "";
	$("input[name='selectedId']").each(function(){
		if(this.checked){
			if(checkNos!=""){
				checkNos += ",";
			}
			checkNos += "{'channelNo':'"+$(this).val()+"', 'channelName':'"+$("#channelName"+$(this).val()).html().trim().replaceAll("'", "&#8242;")
					+"' }";
		}else{
			if(unCheckNos!=""){
				unCheckNos += ",";
			}
			unCheckNos += "{'channelNo':'"+$(this).val()+"'}";
		}
	});
	json = "({ ";
	if(checkNos!=""){
		json += " 'items':["+checkNos+"],";
	}
	if(unCheckNos!=""){
		json += " 'delItems':["+unCheckNos+"]";
	}
	json += "})";
	art.dialog.data('ChannelData', json);
}
</script>
</head>
<body>
<div class="title">
  <h2>角色列表</h2>
</div>
<form action="${path}/sysmgmt/role/roleSelect.action" method="post" name="roleForm" id="roleForm">
<input type="hidden" name="id" id="id" value="" />
<input type="hidden" name="companyNo" id="companyNo" value="${search.companyNo}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
<%--                  <c:if test="${activeOperator.type == 0}">
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${belongList}" var="u" > 
							<option value="${u.companyNo }" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                 </c:if> --%>
                  <td width="90px" height="30">角色ID：</td>
                  <td width="160"><input class="form120px" type="text" id="roleId" name="roleId" value="${search.roleId}" autocomplete="off" oncontextmenu="return false;"></td>
                  <td width="90px" height="30">角色名称：</td>
                  <td width="160"><input class="form120px" type="text" id="roleName" name="roleName" value="${search.roleName}"
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
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="ContentWindow.updateAllChannel(this);checkAll(this, 'selectedId')"></th>
          <th width="*%">角色名称</th>
          <th width="*%">角色ID</th>
          <th width="*%">状态</th>
          <th width="*%">运营商</th>
          <th width="*%">创建用户</th>
          <th width="*%">创建时间</th>
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
          <td align="center"><input type="checkbox" name="selectedId" value="${u.roleNo }" onclick="ContentWindow.updateChannel(this)"></td>
         <td align="center">${u.roleName }</td>
          <td align="center">${u.roleId }</td>
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
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  </form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
  </div>

</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
