<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>视频信息</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
});

</script>
</head>

<body>
<div class="title"><h2>视频信息</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  	<td class="tdBlue" width="20%" align="right">资产ID</td>
    <td class="tdBlue" width="30%">${video.assetId}</td>
    <td class="tdBlue" width="20%" align="right">视频名称</td>
    <td class="tdBlue" width="30%">${video.videoName} </td>
  </tr>
  <tr>
    <td class="tdBlue" align="right">下载地址</td>
    <td class="tdBlue">
       <c:out value="${video.sourceUrl }"></c:out>
    </td>
    <td class="tdBlue" width="20%" align="right">状态</td>
    <td class="tdBlue" width="30%">
                 <c:forEach items="${videoStatusMap}" var="m" > 
					<c:if test="${video.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">创建用户</td>
    <td class="tdBlue" width="30%">
    ${video.operator.operatorName}
     </td>
    <td width="20%" class="tdBlue" align="right">所属运营商</td>
    <td class="tdBlue" width="30%">${video.company.companyName }
      </td>
    </tr>
  <tr>

    <td width="20%" class="tdBlue" align="right">创建时间</td>
    <td class="tdBlue" width="30%">${video.createTime}
      </td>
          <td class="tdBlue" width="20%" align="right">更新时间</td>
    <td class="tdBlue" width="30%">
    ${video.updateTime}
     </td>
  </tr>

</table>
</fieldset>

<fieldset><legend>&nbsp;详细信息</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue"  width="20%" align="right">用户名</td>
    <td class="tdBlue"  width="30%">${video.userName}

	</td>
    <td class="tdBlue" width="20%" align="right">密码</td>
    <td class="tdBlue" width="30%">
    ${video.password}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">图片宽度</td>
    <td class="tdBlue" width="30%">${video.width}
	</td>
    <td class="tdBlue" width="20%" align="right">图片高度</td>
    <td class="tdBlue" width="30%">${video.height}
	</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">间隔时间（单位：秒）</td>
    <td class="tdBlue" width="30%">
         ${video.interval}
     </td>
    <td class="tdBlue" width="20%" align="right">发送时间</td>
    <td class="tdBlue" width="30%">
    ${video.sendTime}
	</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">失败原因</td>
    <td class="tdBlue" width="30%">
         ${video.failReason}
     </td>
    <td class="tdBlue" width="20%"></td>
    <td class="tdBlue" width="30%">
	</td>
  </tr>
</table>
</fieldset>
</body>
</html>
