<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>特征码组</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#featureCodeVal").keyup(function(){
		var featureCodeVal = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (featureCodeVal != null && featureCodeVal != '' && !resNameReg.test(featureCodeVal)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});	

function goBack() {
	document.getElementById("form1").action = "${path}/opermgmt/featureCodeGroup/showFeacodesOutGroup.action";
	document.getElementById("form1").submit();
}

//详情
function detail(featureCodeNo) {
	art.dialog.open("${path}/opermgmt/featureCode/featureCodeDetail.action?featureCodeNo=" + featureCodeNo, 
		{
			title: "特征码详情", 
			width: 450,
			height: 300,
			lock: true
		});
}

function saveToGroup() {
	var sets = $("input[name='featureCodeNos']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要添加的选项！");
		return false;
	}
	
	art.dialog.confirm('您确认该操作吗？', function(){
		var options = {
			url: "${path}/opermgmt/featureCodeGroup/saveToGroup.ation",
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
				art.dialog.alert("添加失败！");
			},
			success: function(data) {
				art.dialog.list['broadcastLoading'].close();
				eval("var rsobj = "+data+";");
				if(rsobj.result=="true"){
					art.dialog.alert("添加成功！", goBack);
				}else{
					if (rsobj.desc != null && rsobj.desc != ""){
						art.dialog.alert(rsobj.desc);
					}else{
						art.dialog.alert("删除失败！");
					}
				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
	}, function(){
    art.dialog.tips('您取消了操作！');
});
	
}



</script>
</head>

<body>
<div class="title">
<h2>选择新增特征码</h2>
</div>
<form action="${path}/opermgmt/featureCodeGroup/showFeacodesOutGroup.action" method="post" name="form1" id="form1">
<input type="hidden" name="groupNo" id="groupNo" value="${groupNo }" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">特征码值：</td>
                  <td width="160">
                  	<input id="featureCodeVal" name="featureCodeVal" class="form120px" value="${search.featureCodeVal }" 
                  	onMouseOver="toolTip('特征码ID由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  	
                	<td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"/></td>
                </tr>
              </tbody>
            </table>
          </td>
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
    
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'featureCodeNos')"></th>
          <th width="*%">特征码值</th>
          <th width="*%">特征码描述</th>
          <th width="*%">特征码类型</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="8">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      <c:forEach items="${list}" var="u">
		<%-- <input type="hidden" name="oldStatus${u.featureCodeNo}" id="oldStatus" value="" /> --%>
        <tr>
          <td align="center">
	          <c:if test="${u.featureCodeNo != null && u.featureCodeNo != -1}">
	          	<input type="checkbox" name="featureCodeNos" value="${u.featureCodeNo }">
	          </c:if>
          </td>
          <td align="center">${u.featureCodeVal }</td>
          <td align="center">${u.featureCodeDesc}</td>
          <td align="center">
          	<c:if test="${u.featureCodeType=='0'}">喜好特征</c:if>
          	<c:if test="${u.featureCodeType=='1'}">地理特征</c:if>
          	<c:if test="${u.featureCodeType=='2'}">大客户</c:if>
          	<c:if test="${u.featureCodeType=='3'}">年龄特征</c:if>
          </td>
          <td class="tdOpera2" align="center">
	         <a href="javascript:;" onclick="detail('${u.featureCodeNo}')">详情</a>
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
		
	</div>
    <!--对表格数据的操作 end-->
  </div>
	
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>