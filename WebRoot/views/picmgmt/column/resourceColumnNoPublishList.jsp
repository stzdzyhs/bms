<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>栏目列表</title>
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
<script src="${path}/js/picmgmt/column/column.js"></script>
<script src="${path}/js/picmgmt/publish/publish.js"></script>
<script src="${path}/js/const.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#columnName").keyup(function(){
		var columnName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (columnName != null && columnName != '' && !resNameReg.test(columnName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

function goBack(){
	document.getElementById("form1").action = "${path}/picmgmt/column/resourceColumnNoPublishList.action";
	document.getElementById("form1").submit();
}

function detail(columnId){

	art.dialog.open("${path}/picmgmt/column/columnDetail.action?columnId=" + columnId, 
			{
				title: "栏目详情", 
				width: 450,
				height: 300,
				lock: true

			});
}



function saveResource(){
	var sets = $("input[name='albumNos']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要添加的选项！");
		return false;
	}
	
	art.dialog.confirm('您确认该操作吗？', function(){
			var options = {
				url: "${path}/picmgmt/topic/saveTopicAlbum.action",
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
						if (rsobj.authText != null && rsobj.authText != ""){
							art.dialog.alert(rsobj.authText);
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
  <h2>栏目列表</h2>
</div>
<form action="${path}/picmgmt/column/resourceColumnNoPublishList.action" method="post" name="form1" id="form1">
<input type="hidden" name="resourceId" id="resourceId" value="${resourceId }" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">栏目名称：</td>
                  <td width="150px">
                     <input id="columnName" name="columnName" class="form130px" maxlength="30"  value="${search.columnName}" 
onMouseOver="toolTip('栏目名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
<%--                   <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${columnStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td> --%>
                   <c:if test="${activeOperator.type != 2}">
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.operatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
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
     	onclick="albumSinglePublish('${path}/picmgmt/album/toAlbumSinglePublish.action',ENTITY_TYPE_MENU,'${resourceId }')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'parentIds')"></th>
          <th width="*%" id="columnNameKey" class="sortRow">栏目名称</th>
          <th width="*%">状态</th>
          <th width="*%">创建用户</th>
          <th width="*%" id="createTime" class="sortRow">创建时间</th>
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
      	<c:forEach items="${list}" var="c">
        <tr>
          <td align="center"><input type="checkbox" name="parentIds" value="${c.id }">
          </td>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(c.columnName) > 10}">
                 ${fn:substring(c.columnName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${c.columnName }
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:forEach items="${columnStatusMap}" var="m" > 
					<c:if test="${c.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             ${c.operator.operatorName}
          </td>
          <td align="center">${c.createTime}</td>
          <td align="center">${c.updateTime}</td>
          <td class="tdOpera2" align="center">
             <a href="javascript:;" onclick="detail('${c.id }')">详情</a>
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
     <jsp:include page="/common/sortRow.jsp" />
  </form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
            <div class="operation">
     <input class="btn btn80" type="button" value="发布" 
     onclick="albumSinglePublish('${path}/picmgmt/album/toAlbumSinglePublish.action',ENTITY_TYPE_MENU,'${resourceId }')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
