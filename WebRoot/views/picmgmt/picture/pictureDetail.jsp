<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图片信息</title>
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

<script src="${path}/js/swfupload/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/plugins/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/myhandlers.js" type="text/javascript"></script>


<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
});


</script>
</head>

<body>
<div class="title"><h2>图片信息</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue" width="20%" align="right">图片ID</td>
    <td class="tdBlue" width="30%">
       ${picture.pictureId}
     </td>
    <td class="tdBlue" width="20%" align="right">图片名称</td>
    <td class="tdBlue" width="30%">
       ${picture.picName}
     </td>
  </tr>
  <tr height="80">
    <td class="tdBlue" align="right">图片文件</td>
    <td class="tdBlue">
       	<c:if test="${picture.picPath != null and picture.picPath != '' }">
	    	<img src="${path}/${picture.picPath2}" height="50" />
		</c:if>
    </td>
     <td class="tdBlue" width="20%" align="right">图片校验码</td>
    <td class="tdBlue" width="30%">
      ${picture.checkCode}
     </td>

  </tr>
  <tr>
  	<td class="tdBlue" width="20%" align="right">显示顺序</td>
    <td class="tdBlue" width="30%">${picture.showOrder} </td>
    <td class="tdBlue" width="20%" align="right">图片标签</td>
    <td class="tdBlue" width="30%">
      <c:out value="${picture.picLabel}"></c:out>
     </td>
    </tr>
  <tr>
      <td class="tdBlue" width="20%" align="right">创建用户</td>
    <td class="tdBlue" width="30%">
    ${picture.operator.operatorName}
     </td>
      <td width="20%" class="tdBlue" align="right">创建时间</td>
    <td class="tdBlue" width="30%">${picture.createTime}
      </td>
    </tr>
     <tr>
	    <td class="tdBlue" width="20%" align="right">更新时间</td>
	    <td class="tdBlue" width="30%">
	    ${picture.updateTime}
	     </td>
      	<td class="tdBlue" width="20%" align="right">图片简介</td>
    	<td class="tdBlue" width="30%">
     	<c:out value="${picture.picDesc}"></c:out>
     	</td>

     </td>
    </tr>
</table>
</fieldset>

<fieldset><legend>&nbsp;详细信息</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue"  width="20%" align="right">图片作者</td>
    <td class="tdBlue"  width="30%">
        <c:out value="${picture.picAuthor}"></c:out>
	</td>
    <td class="tdBlue" width="20%" align="right">图片来源</td>
    <td class="tdBlue" width="30%">
     <c:out value="${picture.picSource}"></c:out>
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">参与投票</td>
    <td class="tdBlue" width="30%">
			<c:forEach items="${pictureVoteFlagMap}" var="m" > 
			<c:if test="${picture.voteFlag==m.key }">${m.value }</c:if>
			</c:forEach> 
	</td>
    <td class="tdBlue" align="right">视频时间</td>
    <td class="tdBlue">
         ${picture.videoTime}
     </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">视频帧数</td>
    <td class="tdBlue" width="30%">${picture.frameNum}
	</td>
    <td class="tdBlue"></td>
    <td class="tdBlue">
     </td>
  </tr>
</table>
</fieldset>

<c:if test="${publish != null}">
<c:forEach items="${publish}" var="p">
	<fieldset><legend>&nbsp;图片发布到相册 ${p.album.albumId} 的关联策略&nbsp;</legend>
		<c:if test="${p.publishStrategy!= null}">
			<c:forEach items="${p.publishStrategy}" var="ps">
				<div>策略名称: ${ps.strategyName}</div>						
				 <table class="tableCommon tdBorWhite" width="100%" border="0"
						cellspacing="0" cellpadding="0">
				  <tr>
				    <td class="tdBlue" width="20%" align="right">网络ID</td>
				    <td class="tdBlue" width="30%">${ps.networkIdStr}</td>
				    <td class="tdBlue" width="20%" align="right">区域码</td>
				    <td class="tdBlue" width="30%">
				      ${ps.regionCodeStr}
				    </td>
				  </tr>
				  <tr>
				    <td class="tdBlue" width="20%" align="right">卡号</td>
				    <td class="tdBlue" width="30%">${ps.cardNoStr}</td>
				    <td class="tdBlue" width="20%" align="right">特征码</td>
				    <td class="tdBlue" width="30%">
				      ${ps.featureIdStr}
				    </td>
				  </tr>
				  <tr>
				    <td class="tdBlue" width="20%" align="right">空分组</td>
				    <td class="tdBlue" width="30%">${ps.tsIdStr}</td>
				    <td class="tdBlue" width="20%" align="right">条件关系</td>
				    <td class="tdBlue" width="30%">
				      ${ps.conditionStr}
				    </td>
				  </tr>
				 </table>
			</c:forEach>		
		</c:if>
		<c:if test="${p.publishStrategy== null}">
			继承使用相册的策略
		</c:if>
	</fieldset>
</c:forEach>	
</c:if>
<c:if test="${publish == null}">
	此图片没有发布信息
</c:if>

</body>
</html>
