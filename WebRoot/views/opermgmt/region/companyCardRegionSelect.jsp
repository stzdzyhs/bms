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
<script src="${path}/js/opermgmt/company/company.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#regionName").keyup(function(){
		var regionName = $(this).val();
		var regionNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (regionName != null && regionName != '' && !regionNameReg.test(regionName)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	var regionCodeReg = /^[0-9]{1,4}$/;
	$("#regionCode").keyup(function(){
		var regionCode = $(this).val();

		if (regionCode != null && regionCode != '' && !regionCodeReg.test(regionCode)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	$("#regionSectionBegin").keyup(function(){
		var regionSectionBegin = $(this).val();

		if (regionSectionBegin != null && regionSectionBegin != '' && !regionCodeReg.test(regionSectionBegin)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	$("#regionSectionEnd").keyup(function(){
		var regionSectionEnd = $(this).val();

		if (regionSectionEnd != null && regionSectionEnd != '' && !regionCodeReg.test(regionSectionEnd)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='regionIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	} else {
		var aa = document.getElementsByName("regionIds");
		for (var i=0; i<aa.length; i++)
			aa[i].checked = false;
	}
	
	var param = id != '' ? "?regionIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/opermgmt/company/companyCardRegionDelete.action" + param,
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
						if (rsobj.desc=='reference'){
							art.dialog.alert("区域分段已经被引用，不能删除！");
						}else{
							art.dialog.alert("删除失败！");
						}

					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/opermgmt/company/companyCardRegionSelect.action";
	document.getElementById("form1").submit();
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
  <h2>已关联智能卡区域列表</h2>
</div>
<form action="${path}/opermgmt/company/companyCardRegionSelect.action" method="post" name="form1" id="form1">
<input type="hidden" name="companyNo" id="companyNo" value="${companyNo}" />
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
      				<input id="regionName" name="regionName" class="form130px" value="${search.regionName}"  maxlength="50"
      				onMouseOver="toolTip('系统名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
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
                       <input id="regionCode" name="regionCode" class="form130px" value="${search.regionCode}"  maxlength="4"
                       onMouseOver="toolTip('区域码由1-4个数字组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
      			  </td>
                </tr>
                <tr>
                  <td width="90px" height="30">分段起始值：</td>
                  <td width="160">
      				<input id="regionSectionBegin" name="regionSectionBegin" class="form130px" value="${search.regionSectionBegin}"  maxlength="4"
      				 onMouseOver="toolTip('分段起始值由1-4个数字组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
      			  </td>
      			 <td width="90px" height="30">分段终止值：</td>
                  <td width="160">
                       <input id="regionSectionEnd" name="regionSectionEnd" class="form130px" value="${search.regionSectionEnd}"  maxlength="4"
                        onMouseOver="toolTip('分段终止值由1-4个数字组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
      			  </td>
                  <td><input  id="btnQuery" class="btnQuery" name="" type="submit" value="查询"></td>
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
	        <input class="btn btn80" type="button" value="新增" onclick="companyCardRegionNoSelect('${path}/opermgmt/company/companyCardRegionNoSelect.action')">
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'regionIds')"></th>
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
          <td align="center"><input type="checkbox" name="regionIds" value="${r.id}">
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
             <a href="javascript:;" onclick="deleteSets('${r.id}')">删除</a>
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
	        <input class="btn btn80" type="button" value="新增" onclick="companyCardRegionNoSelect('${path}/opermgmt/company/companyCardRegionNoSelect.action')">
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>