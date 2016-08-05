<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>智能卡区域列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/opermgmt/region/region.js"></script>
<script type="text/javascript">
function selectCardRegion(){
	var regionId = $('input[name="regionId"]:checked').val();
	var regionName = $("#regionName" + regionId).val();
	var regionCode = "";
	var codeType = $("#codeType" + regionId).val();
	if (codeType == 0){
		regionCode = $("#regionCode" + regionId).val();
		regionCode = formatRegionCode(regionCode);
	}else if (codeType == 1){
		var regionSectionBegin = $("#regionSectionBegin" + regionId).val();
		regionSectionBegin = formatRegionCode(regionSectionBegin);
		var regionSectionEnd = $("#regionSectionEnd" + regionId).val();
		regionSectionEnd = formatRegionCode(regionSectionEnd);
		regionCode = regionSectionBegin + "-" + regionSectionEnd;
	}
	
	 artDialog.open.origin.document.getElementById('selectRegionId').value = regionId;
	 artDialog.open.origin.document.getElementById('regionCode').value = regionCode;
	 artDialog.open.origin.document.getElementById('regionName').value = regionName;
	 $.each(artDialog.open.origin.art.dialog.list, function (index, item) {

         item.close();

     });
}

function detail(regionId){

	art.dialog.open("${path}/opermgmt/region/cardRegionDetail.action?regionId=" + regionId, 
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
  <h2>智能卡区域列表</h2>
</div>
<form action="${path}/opermgmt/region/cardRegionSelect.action" method="post" name="form1" id="form1">
<input type="hidden" name="companyNo" id="companyNo" value="${search.companyNo}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">名称：</td>
                  <td width="160">
      				<input name="regionName" class="form130px" value="${search.regionName}"  maxlength="50"/>
      			  </td>
      			 <td width="90px" height="30">区域码类型：</td>
                  <td width="160">
      				<select name="codeType" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${cardRegionCodeTypeMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.codeType==t.key }"> selected="selected" </c:if>>${t.value}</option>
				 		</c:forEach> 
				  </select>
      			  </td>
                  <td width="90px" height="30">区域码：</td>
                  <td width="160">
                       <input name="regionCode" class="form130px" value="${search.regionCode}"  maxlength="4"/>
      			  </td>
                </tr>
                <tr>
                  <td width="90px" height="30">分段起始值：</td>
                  <td width="160">
      				<input name="regionSectionBegin" class="form130px" value="${search.regionSectionBegin}"  maxlength="4"/>
      			  </td>
      			 <td width="90px" height="30">分段终止值：</td>
                  <td width="160">
                       <input name="regionSectionEnd" class="form130px" value="${search.regionSectionEnd}"  maxlength="4"/>
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
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"></th>
          <th width="*%">名称</th>
          <th width="*%">类型</th>
          <th width="*%">区域码类型</th>
          <th width="*%">区域码</th>
          <th width="*%">区域分段</th>
          <th width="*%">所属分段</th>
          <th width="*%">创建时间</th>
          <th width="*%">更新时间</th>
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
          <td align="center"><input type="radio" name="regionId" value="${r.id}" <c:if test="${r.id==selectRegionId}"> checked </c:if>>
          <input type="hidden" name="regionName${r.id}" id="regionName${r.id}" value="${r.regionName}" />
          <input type="hidden" name="codeType${r.id}" id="codeType${r.id}" value="${r.codeType}" />
          <input type="hidden" name="regionCode${r.id}" id="regionCode${r.id}" value="${r.regionCode}" />
          <input type="hidden" name="regionSectionBegin${r.id}" id="regionSectionBegin${r.id}" value="${r.regionSectionBegin}" />
          <input type="hidden" name="regionSectionEnd${r.id}" id="regionSectionEnd${r.id}" value="${r.regionSectionEnd}" />
          </td>
          <td align="center">${r.regionName}</td>
          <td align="center">
          <c:forEach items="${cardRegionTypeMap}" var="t" > 
			<c:if test="${r.type==t.key }"> ${t.value }</c:if>
		  </c:forEach> 
          </td>
          <td align="center">
          <c:forEach items="${cardRegionCodeTypeMap}" var="t" > 
			<c:if test="${r.codeType==t.key }"> ${t.value }</c:if>
		  </c:forEach> 
          </td>
          <td align="center">${r.regionCode}</td>
          <td align="center">${r.regionSectionBegin}-${r.regionSectionEnd}</td>
          <td align="center">${r.parent.regionName}</td>
          <td align="center">${r.createTime}</td>
          <td align="center">${r.updateTime}</td>
          <td class="tdOpera2" align="center">
             <a href="javascript:;" onclick="detail('${r.id }')">详情</a>
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
