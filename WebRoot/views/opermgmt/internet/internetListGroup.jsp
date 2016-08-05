<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>网络列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){ 
	
	$("#internetName").keyup(function(){
		var internetName = $(this).val();
		var internetNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (internetName != null && internetName != '' && !internetNameReg.test(internetName)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	}); 
	
	$("#internetId").keyup(function(){
		var internetId = $(this).val();
		var internetIdReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (internetId != null && internetId != '' && !internetIdReg.test(internetId)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});


//提交表单
function deleteSets(id) {
    
	var url="${path}/opermgmt/internet/internetDelete.action";
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	} else {
		url="${path}/opermgmt/internet/internetDelete.action?rtId="+id;
		var rtId = document.getElementsByName("rtId");
		for (var i=0; i<rtId.length; i++)
			rtId[i].checked = false;
	}
	
	var type = ${activeOperator.type};
	if (type == 2){
		art.dialog.alert("您没有删除网络的权限！");
		return false;
	}
	
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: url,
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
						if (rsobj.desc == 'operator') {
							art.dialog.alert("该网络或者所包含的下属网络被操作员引用，不能删除！");
						}else if (rsobj.desc == 'role') {
							art.dialog.alert("该运营商或者所包含的下属网络被角色引用，不能删除！");
						}else {
							art.dialog.alert("删除失败！");
						}
					}
				}
			};
			jQuery('#internetForm').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('您取消了操作！');
	});
}

function goBack(){
	document.getElementById("internetForm").action = "${path}/opermgmt/internet/internetList.action";
	document.getElementById("internetForm").submit();
}
//进入新增编辑页面
function toEditPage(id){
	var type = ${activeOperator.type};
	if (type == 2){
		art.dialog.alert("您没有添加或编辑运营商的权限！");
		return false;
	}
	
	if(id==null || typeof(id)=="undefined"){
		id = "";
	}
	$("#id").val(id);
	document.getElementById("internetForm").action = "${path}/opermgmt/internet/internetEdit.action";
	document.getElementById("internetForm").submit();
}

function detail(id){

	art.dialog.open("${path}/opermgmt/internet/internetDetail.action?internetNo=" + id, 
			{
				title: "网络详情", 
				width: 450,
				height: 400,
				lock: true

			});
}

function regionMgmt(internetNo){
	art.dialog.open("${path}/opermgmt/internet/internetCardRegionSelect.action?internetNo=" + internetNo, 
			{
				title: "区域管理", 
				width: 980,
				height: 450,
				lock: true

			});
}
</script>
</head>
<body>
<div class="title">
  <h2>网络列表</h2>
</div>
<form action="${path}/opermgmt/internet/internetList.action" method="post" name="internetForm" id="internetForm">
<input type="hidden" name="id" id="id" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody> 
                <tr> 
                <td width="90px" height="30">网络ID：</td>
                  <td width="160">
                  	<input id="internetId" name="internetId" class="form120px" value="${search.internetId}"   maxlength="50" 
                  	onMouseOver="toolTip('网络ID大小写英文字母、数字组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                
                  <td width="90px" height="30">网络名称：</td>
                  <td width="160">
                  	<input id="internetName" name="internetName" class="form120px" value="${search.internetName}"   maxlength="50" 
                  	onMouseOver="toolTip('网络名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
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
    <c:set var="add" value="false" />
    <c:set var="delete" value="false" />
    <c:set var="edit" value="false" />
    <c:set var="detail" value="false" />
    <c:set var="dist" value="false" />
    <c:set var="region" value="false" />
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">网络ID</th>
          <th width="*%">网络名称</th>
          <th width="*%">网络描述</th>
          <th width="*%">创建者</th>
          <th width="*%">创建时间</th>
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
        <tr>
          <td align="center">
	          <c:if test="${u.internetNo != null && u.internetNo != -1}">
	          	<input type="checkbox" name="rtId" value="${u.internetNo }">
	          </c:if>
          </td>
          <td align="center">${u.internetId }</td>
          <td align="center">${u.internetName }</td>
          <td align="center">${u.internetDescribe }</td>
          <td align="center">
             <c:if test="${u.operator != null }">
                ${u.operator.operatorName }
             </c:if>
          </td>
          <td align="center">${u.createTime }</td>
          <td class="tdOpera2" align="center">
          <a href='javascript:;' onclick='detail(${u.internetNo})'>详情</a>
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
    <!--对表格数据的操作 end-->
  </div>
   <div style="width:100%; text-align:center; margin-top:10px;">
        <input value="保存" type="submit" class="btnQuery" onClick="saveSpace()"/>
          &nbsp;&nbsp;
        <input value="取消" type="button" class="btnQuery" onClick="goBack()" />
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>