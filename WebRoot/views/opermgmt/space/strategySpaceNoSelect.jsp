<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>空分组列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/opermgmt/strategy/strategy.js"></script>
<script type="text/javascript">
//提交表单
function saveResource(){
	var sets = $("input[name='resourceIds']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要添加的选项！");
		return null;
	}
	
	var data = [];
	sets.each(function() {
		var id = $(this).val();
		var d = {spaceNo:id, spaceName: $("#name"+id).text() };
		data.push(d);
	});
	
	return data;	
}

function goBack(){
	setTimeout(function(){
		art.dialog.close();
	}, 100);
// 	document.getElementById("form1").action = "${path}/opermgmt/strategy/strategyCardRegionNoSelect.action";
// 	document.getElementById("form1").submit();
}

function detail(no){
	art.dialog.open("${path}/opermgmt/space/spaceDetail.action?spaceNo=" + no, 
	{
		title: "智能卡区域详情", 
		width: 450,
		height: 450,
		lock: true
	});
}
</script>
</head>
<body>
<div class="title">
	<h2>未关联空分组列表</h2>
</div>

<form action="${path}/opermgmt/strategy/strategySpaceNoSelect.action" method="post" name="form1" id="form1">
<input type="hidden" name="strategyNo" id="strategyNo" value="${strategyNo}" />
<input type="hidden" name="companyNo" id="companyNo" value="${companyNo}" />
<input type="hidden" name="resType" id="resType" value="7" />

<div class="searchWrap">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tbody>
        	<tr>
            	<td width="90px" height="30" align="right">名称：</td>
                <td width="160">
      				<input name="spaceName" class="form130px" value="${search.spaceName}"  maxlength="50"/>
				</td>
				<td><input class="btnQuery" name="" type="submit" value="查询"></td>
			</tr>
            <tr>
			</tr>
		</tbody>
	</table>
</div>
<!--标题 end-->
<div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
</div>
<!--工具栏 end-->
<div class="tableWrap">
	<table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
          		<th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'resourceIds')"></th>
          		<th width="*%">ID</th>
          		<th width="*%">名称</th>
          		<th width="*%">描述</th>
          		<th width="*%">创建时间</th>
          		<th width="*%">操作</th>
          	</tr>
      	</thead>
      	<tbody>
      		<c:if test="${list == null || fn:length(list) == 0}">
			<tr>
				<td colspan="9">
					抱歉，没有相关的结果。
				</td>
			</tr>
	  		</c:if>
			<c:forEach items="${list}" var="r">
        	<tr>
          		<td align="center"><input type="checkbox" name="resourceIds" value="${r.spaceNo}">
          		</td>
          		<td align="center" >${r.spaceId}</td>
          		<td align="center" id='name${r.spaceNo}'>${r.spaceName}</td>

          		<td align="center">${r.spaceDescribe}</td>
          		<td align="center">${r.createTime}</td>
          		<td class="tdOpera2" align="center">
             		<a href="javascript:;" onclick="detail('${r.spaceNo}')">详情</a>
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
</div>
</body>
</html>
