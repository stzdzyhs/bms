<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>板块列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/common.js"></script>
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
	document.getElementById("form1").action = "${path}/textmgmt/article/articleColumnSelect.action";
	document.getElementById("form1").submit();
}

function detail(id){
	art.dialog.open("${path}/textmgmt/column/columnDetail.action?columnNo=" + id, {
		title: "板块详情", 
		width: 750,
		height: 450,
		lock: true
	});
}


function saveArticleColumnMap(){
	var sets = $("input[name='columnNo']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要添加的选项！");
		return false;
	}
	
	art.dialog.confirm('您确认该操作吗？', 
		function() {
			var options = {
				url: "${path}/textmgmt/article/addColumnArticleMap.action",
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
					if(isResultSucc(rsobj)){
						art.dialog.alert("添加成功！",goBack);
					}
					else{
						if (rsobj.desc != null && rsobj.desc != ""){
							art.dialog.alert(rsobj.desc);
						}
						else{
							art.dialog.alert("添加失败！");
						}
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		},  
		function(){
    		art.dialog.tips('您取消了操作！');
		}
	);

}

</script>
</head>
<body>
<div class="title">
  <h2>未关联板块列表</h2>
</div>
<form action="${path}/textmgmt/article/articleAddNewColumn.action" method="post" name="form1" id="form1">
<input type="hidden" name="articleNo" id="articleNo" value="${articleNo }" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
				<td width="90px" height="30">板块名称：</td>
                  <td width="160">
      				<input id="columnName" name="columnName" class="form130px" value="${search.columnName}"  maxlength="50"
      				onMouseOver="toolTip('版块名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off"//>
      			  </td>
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
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'columnNo')"></th>
          <th width="*%">板块ID</th>
          <th width="*%">板块名称</th>
          <th width="*%">所属板块</th>
          <th width="*%">状态</th>
          <th width="*%">创建者</th>
          <th width="*%">创建时间</th>
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
      	<c:forEach items="${list}" var="p">
        <tr>
          <td align="center"><input type="checkbox" id="columnNo" name="columnNo" value="${p.columnNo}">
          </td>
          <td align="center">${p.columnName}</td>
          <td align="center">${p.columnName} </td>
          <td align="center">${p.parent.columnName}</td>
          <td align="center">${p.statusName} </td>
          <td align="center">${p.operator.operatorName}</td>
          <td align="center">${p.createTime}</td>
          <td class="tdOpera2" align="center">
             <a href="javascript:;" onclick="detail('${p.columnNo}')">详情</a>
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
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
