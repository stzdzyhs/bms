<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>专题列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/picmgmt/topic/topic.js"></script>
<script src="${path}/js/picmgmt/publish/publish.js"></script>
<script src="${path}/js/const.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#topicName").keyup(function(){
		var topicName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (topicName != null && topicName != '' && !resNameReg.test(topicName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

function goBack(){
	document.getElementById("form1").action = "${path}/picmgmt/topic/resourceTopicNoPublishList.action";
	document.getElementById("form1").submit();
}

function detail(topicId){

	art.dialog.open("${path}/picmgmt/topic/topicDetail.action?topicId=" + topicId, 
			{
				title: "专题详情", 
				width: 750,
				height: 450,
				lock: true

			});
}
</script>
</head>
<body>
<div class="title">
  <h2>专题列表</h2>
</div>
<form action="${path}/picmgmt/topic/resourceTopicNoPublishList.action" method="post" name="form1" id="form1">
<input type="hidden" name="resourceId" id="resourceId" value="${resourceId}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">专题名称：</td>
                  <td width="150px">
                     <input id="topicName" name="topicName" class="form130px" maxlength="30"  value="${search.topicName}" 
onMouseOver="toolTip('专题名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">类型：</td>
                  <td width="160">
                  <select name="type" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${topicTypeMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.type==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
<%--                   <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${topicStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td> --%>
                  <td width="90px" height="30">截图标志：</td>
                  <td width="160">
                  <select name="captureFlag" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${captureFlagMap}" var="o" > 
							<option value="${o.key }" <c:if test="${search.captureFlag==o.key }"> selected="selected" </c:if>>${o.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                 </tr>
                 <tr>
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.operatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
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
     <input class="btn btn80" type="button" value="发布" 
        onclick="albumSinglePublish('${path}/picmgmt/album/toAlbumSinglePublish.action',ENTITY_TYPE_TOPIC,'${resourceId }')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'parentIds')"></th>
          <th width="*%">专题名称</th>
          <th width="*%">封面</th>
          <th width="*%">类型</th>
          <th width="*%">状态</th>
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
      	<c:forEach items="${list}" var="t">
        <tr>
          <td align="center"><input type="checkbox" name="parentIds" value="${t.id }">
          </td>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(t.topicName) > 10}">
                 ${fn:substring(t.topicName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${t.topicName }
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:if test="${t.topicCover != null and t.topicCover != '' }">
	      		<img src="${path}/${t.topicCover}" height="50" onMouseOver="toolTip('<img src=${path}/${t.topicCover} style=\'width:200px; max-height:200px\' id=\'toolTipImg\' />')" onMouseOut="toolTip()"/>
	      	 </c:if>
          </td>
          <td align="center">
             <c:forEach items="${topicTypeMap}" var="m" > 
					<c:if test="${t.type==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             <c:forEach items="${topicStatusMap}" var="m" > 
					<c:if test="${t.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
           ${t.company.companyName}
          </td>
          <td align="center">
             ${t.operator.operatorName}
          </td>
          <td align="center">${t.createTime}</td>
          <td align="center">${t.updateTime}</td>
          <td class="tdOpera2" align="center">
             <a href="javascript:;" onclick="detail('${t.id }')">详情</a>
      
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
     <input class="btn btn80" type="button" value="发布" 
     	onclick="albumSinglePublish('${path}/picmgmt/album/toAlbumSinglePublish.action',ENTITY_TYPE_TOPIC,'${resourceId }')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
