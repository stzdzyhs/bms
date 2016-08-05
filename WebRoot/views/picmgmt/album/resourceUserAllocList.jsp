<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>

<script src="${path}/js/sysmgmt/user/user.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#operatorId").keyup(function(){
		var operatorId = $(this).val();
		var userNameReg = /^\w+$/;
		if (operatorId != null && operatorId != '' && !userNameReg.test(operatorId)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='operatorNos']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	} else {
		var aa = document.getElementsByName("operatorNos");
		for (var i=0; i<aa.length; i++)
			aa[i].checked = false;
	}
	
	var param = id != '' ? "?operatorNos="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/sysmgmt/user/resourceAllocOperDelete.action" + param,
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
	document.getElementById("form1").action = "${path}/sysmgmt/user/resourceOperAllocList.action";
	document.getElementById("form1").submit();
}

function detail(id){

	art.dialog.open("${path}/sysmgmt/user/userDetail.action?operatorNo=" + id, 
			{
				title: "用户详情", 
				width: 500,
				height: 450,
				lock: true

			});
}
</script>
</head>
<body>
<div class="title">
  <h2>已分配用户列表</h2>
</div>
<form action="${path}/sysmgmt/user/resourceOperAllocList.action" method="post" name="form1" id="form1">
<input type="hidden" name="createOperNo" id="createOperNo" value="${createOperNo}" />
<input type="hidden" name="resourceId" id="resourceId" value="${resourceId}" />
<input type="hidden" name="type" id="type" value="${type}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${belongList}" var="u" > 
							<option value="${u.companyNo }" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">用户账号：</td>
                  <td width="160">
      				<input id="operatorId" name="operatorId" class="form130px" value="${search.operatorId}"  maxlength="20" autocomplete="off" oncontextmenu="return false;"/>
      			  </td>
				  <td width="100px" height="30">用户状态：</td>
                  <td width="150px">
	                  <select name="status" class="form130px" >
	                  	<option value="-1">--请选择--</option>
	                  	<option value="0" <c:if test="${search.status==0 }"> selected="selected" </c:if>>启用</option>
	                  	<option value="1" <c:if test="${search.status==1 }"> selected="selected" </c:if>>禁用</option>
	                  </select>
                  </td>
                  <td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"></td>
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
	        <input class="btn btn80" type="button" value="新增" onclick="resourceOperNoAlloc('${path}/sysmgmt/user/resourceOperNoAllocList.action')">
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'operatorNos')"></th>
          <th width="*%">用户账号</th>
          <th width="*%">用户名称</th>
          <th width="*%">邮件地址</th>
          <th width="*%">用户状态</th>
          <th width="*%">用户类型</th>
          <th width="*%">可用空间</th>
          <th width="*%">所属运营商</th>
          <th width="*%">创建时间</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="11">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      	<c:forEach items="${list}" var="u">
        <tr>
          <td align="center"><input type="checkbox" name="operatorNos" value="${u.operatorNo }">
          </td>
          <td align="center">${u.operatorId}</td>
          <td align="center">${u.operatorName}</td>
          <td align="center"><a href="mailto:${u.operatorEmail }">${u.operatorEmail}</a></td>
          <td align="center"><c:if test="${u.status == 0 }">启用</c:if><c:if test="${u.status == 1 }">禁用</c:if></td>
          <td align="center">
             <c:forEach items="${userTypeMap}" var="m" > 
					<c:if test="${u.type==m.key }"> ${m.value }</c:if>
		    </c:forEach>
          </td>
          <td align="center">
     <c:if test="${u.totalSize != -1 }">
       <c:choose>
        <c:when test="${u.usedSize == 0 }">
         ${u.totalSize }G
       </c:when>
       <c:otherwise>
            ${u.usedSizeFmt } / ${u.totalSize }G
       </c:otherwise>
       </c:choose>
     </c:if>
     <c:if test="${u.totalSize == -1 }">
       不限制
     </c:if>
          </td>
          <td align="center">${u.company.companyName}</td>
          <td align="center">${u.createTime}</td>
          <td class="tdOpera2" align="center">
             <a href="javascript:;" onclick="detail('${u.operatorNo }')">详情</a>
             <a href="javascript:;" onclick="deleteSets('${u.operatorNo }')">删除</a>
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
	        <input class="btn btn80" type="button" value="新增" onclick="resourceOperNoAlloc('${path}/sysmgmt/user/resourceOperNoAllocList.action')">
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <div class="hidden" id="parentNames"></div>
</body>
</html>
