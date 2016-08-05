<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>相册信息</title>
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
<div class="title"><h2>相册信息</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue" width="20%" align="right">相册ID</td>
    <td class="tdBlue" width="30%">${album.albumId} </td>
    <td class="tdBlue" width="20%" align="right">相册名称</td>
    <td class="tdBlue" width="30%">${album.albumName} </td>
  </tr>
  <tr height="80">
      <td class="tdBlue" align="right">相册封面</td>
    <td class="tdBlue">
       <c:if test="${album.albumCover != null and album.albumCover != '' }">
	      		<img src="${path}/${album.albumCover}" height="50" />
	      	 </c:if>
    </td>
    <td class="tdBlue" width="20%" align="right">封面校验码</td>
    <td class="tdBlue" width="30%">${album.checkCode}</td>
  </tr>
  <tr>
  	<td class="tdBlue" width="20%" align="right">显示顺序</td>
    <td class="tdBlue" width="30%">${album.showOrder} </td>
    <td class="tdBlue" width="20%" align="right">相册标签</td>
    <td class="tdBlue" width="30%"><c:out value="${album.albumLabel}"></c:out> </td>
  </tr>
  <tr>
    <td width="20%" class="tdBlue" align="right">截图标志</td>
    <td class="tdBlue" width="30%">
    			  	<c:forEach items="${captureFlagMap}" var="o" > 
							<c:if test="${album.captureFlag==o.key }"> ${o.value }</c:if>
				 		</c:forEach> 
      </td>
    <td width="20%" class="tdBlue" align="right">关联模板</td>
    <td class="tdBlue" width="30%">
        ${album.template.templateName }	 
      </td>
   </tr>
  <tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">所属运营商</td>
    <td class="tdBlue" width="30%">
    ${album.company.companyName }
     </td>
    <td width="20%" class="tdBlue" align="right">创建用户</td>
    <td class="tdBlue" width="30%">
    ${album.operator.operatorName}
      </td>
    </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">创建时间</td>
    <td class="tdBlue" width="30%">
    ${album.createTime}
     </td>
    <td width="20%" class="tdBlue" align="right">更新时间</td>
    <td class="tdBlue" width="30%">
    ${album.updateTime}
      </td>
    </tr>
    <tr>
	    <td class="tdBlue" width="20%" align="right">相册简介</td>
	    <td class="tdBlue" width="80%" colspan="3">
	    <c:out value="${album.albumDesc}"></c:out>
	     </td>
  </tr>
</table>
</fieldset>

<fieldset><legend>&nbsp;图片格式信息</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue"  width="20%" align="right">图片大小（单位：KB）</td>
    <td class="tdBlue"  width="30%">${album.picSize}

	</td>
    <td class="tdBlue" width="20%" align="right">图片格式</td>
    <td class="tdBlue" width="30%">
    ${album.picFormatStr}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">图片分辨率高</td>
    <td class="tdBlue" width="30%">${album.picHeight}
	</td>
    <td class="tdBlue" width="20%" align="right">图片分辨率宽</td>
    <td class="tdBlue" width="30%">${album.picWidth}
	</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">图片名称最大长度</td>
    <td class="tdBlue" width="30%">
         ${album.picNameLen}
     </td>
    <td class="tdBlue" width="20%" align="right">图片简介最大长度</td>
    <td class="tdBlue" width="30%">
    ${album.picDescLen}
	</td>
  </tr>
</table>
</fieldset>

<c:if test="${publish != null}">
<c:forEach items="${publish}" var="p">
	<fieldset><legend>&nbsp;相册发布到专题 ${p.topic.topicId} 的关联策略&nbsp;</legend>
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
			继承使用专题的策略
		</c:if>
	</fieldset>
</c:forEach>	
</c:if>
<c:if test="${publish == null}">
	此相册没有发布信息
</c:if>

</body>
</html>
