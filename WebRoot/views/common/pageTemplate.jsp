<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="pageDown"> &nbsp;数据列共:${pageUtil.rowCount}条&nbsp;
  <c:if test="${!pageUtil.hasPreviousPage}">上一页</c:if>
  <c:if test="${pageUtil.hasPreviousPage}"><a href="javascript:Pager.prototype.goPage('${pageUtil.pageId-1}');">上一页</a></c:if>
  |
  <c:if test="${!pageUtil.hasNextPage}">下一页</c:if>
  <c:if test="${pageUtil.hasNextPage}"><a href="javascript:Pager.prototype.goPage('${1+pageUtil.pageId}');">下一页</a></c:if>
  &nbsp;共${pageUtil.pageCount}页<span class="font048">每页
  <select id="pgpagesize" name="pageUtil.pageSize" onchange="javascript:Pager.prototype.doSubmit(this.value);">
    <option value="5" <c:if test="${pageUtil.pageSize==5 }"> selected="selected" </c:if>> 5 </option>
    <option value="10" <c:if test="${pageUtil.pageSize==10 }"> selected="selected" </c:if>> 10 </option>
    <option value="20" <c:if test="${pageUtil.pageSize==20 }"> selected="selected" </c:if>> 20 </option>
    <option value="50" <c:if test="${pageUtil.pageSize==50 }"> selected="selected" </c:if>> 50 </option>
    <option value="100" <c:if test="${pageUtil.pageSize==100 }"> selected="selected" </c:if>> 100 </option>
    <option value="200" <c:if test="${pageUtil.pageSize==200 }"> selected="selected" </c:if>> 200 </option>
    <option value="500" <c:if test="${pageUtil.pageSize==500 }"> selected="selected" </c:if>> 500 </option>
  </select>
  条&nbsp;第
  <select name="pageUtil.pageId" style="width:45px"  onchange="javascript:Pager.prototype.skipPage(this.value);">
	  	<c:forEach items="${pageUtil.pageList}" var="pageNo" > 
			<option value="${pageNo }" <c:if test="${pageUtil.pageId==pageNo }"> selected="selected" </c:if>>${pageNo }</option>
 		</c:forEach> 
  </select>
  &nbsp;页</span>
</div>
<script type="text/javascript">
function Pager() {}

Pager.prototype.doSubmit = function(value) {
	//document.forms[document.forms.length - 1].action = "";
	var object = document.getElementsByName("pageUtil.pageSize");
	object[0].value = parseInt(value);
	/*
	for ( var  i = 0 ;i < object.length;i ++ ) {
		if(i==0){
			object[i].value = parseInt(value);
		}else{
			object[i].name = '';
		}
	}
	*/
	//Pager.prototype.leaveOnePageNo();
	var f = document.forms[document.forms.length - 1]; 
	f.submit();
};

Pager.prototype.selPageSize = function(s) {
	if (0 == s) {
		document.forms[document.forms.length - 1].reset();
	}
	Pager.prototype.doSubmit(${pageUtil.pageSize });
};

Pager.prototype.skipPage = function(toPage) {
	//var toPage = toPageIn.previousSibling.previousSibling.firstChild.nextSibling.nextSibling.nextSibling.value;
	
	if (!isNaN(toPage)) {
		if (parseInt(toPage) > 0) {
			if (parseInt(toPage) <= ${pageUtil.pageCount}) {
				Pager.prototype.changePageNo(toPage);
				Pager.prototype.doSubmit(${pageUtil.pageSize });
			} else {
				art.dialog.alert("您输入的页码超出范围！");
			}
		} else {
			art.dialog.alert("页码请输入正整数！");
		}
	} else {
		art.dialog.alert("页码请输入正整数！");
	}
}

Pager.prototype.goPage = function(toPage) {
	Pager.prototype.changePageNo(toPage);
	Pager.prototype.doSubmit(${pageUtil.pageSize });
};

Pager.prototype.changePageNo = function(toPage) {
	var object = document.getElementsByName("pageUtil.pageId");
	object[0].value = parseInt(toPage);
}

Pager.prototype.leaveOnePageNo = function() {
	var object = document.getElementsByName("pageUtil.pageId");
	for ( var  i = 1 ;i < object.length;i ++ ) {
			object[i].name = '';
	}
}

</script>
