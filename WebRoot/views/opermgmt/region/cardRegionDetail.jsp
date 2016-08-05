<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智能卡区域信息</title>
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
 <script src="${path}/js/win/win.js" type="text/javascript"></script>
</head>

<body>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">名称</td>
    <td class="tdBlue">
      ${region.regionName}
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">类型</td>
    <td class="tdBlue">
			<c:forEach items="${cardRegionTypeMap}" var="t" > 
				<c:if test="${region.type==t.key }">${t.value }</c:if>
			</c:forEach> 
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">所属分段</td>
    <td class="tdBlue">
      ${region.parent.regionName}
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">区域码类型</td>
    <td class="tdBlue">
			<c:forEach items="${cardRegionCodeTypeMap}" var="t" > 
				<c:if test="${region.codeType==t.key }">${t.value }</c:if>
			</c:forEach> 
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">区域码</td>
    <td class="tdBlue">
      ${region.regionCode}
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">分段起始值</td>
    <td class="tdBlue">
${region.regionSectionBegin}
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">分段终止值</td>
    <td class="tdBlue">
${region.regionSectionEnd}
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">创建时间</td>
    <td class="tdBlue">
${region.createTime}   
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">更新时间</td>
    <td class="tdBlue">
${region.updateTime}   
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">描述</td>
    <td class="tdBlue">
<c:out value="${region.depict}"></c:out>
     </td>
  </tr>
</table>

</body>
</html>
