<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>日志列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<!-- 日期控件 -->
<script src="${path}/js/My97DatePicker/WdatePicker.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#keyWord").keyup(function(){
		var keyWord = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (keyWord != null && keyWord != '' && !resNameReg.test(keyWord)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	//连续开始时间
	var startDate = $("#startDate");
	startDate.addClass("Wdate");
	startDate.eq(0).click(function(){WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	
	var endDate = $("#endDate");
	endDate.addClass("Wdate");
	endDate.eq(0).click(function(){WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'});});
});
function changeLogConfig() {
	var win = art.dialog.open('${path}/sysmgmt/log/logConfigDetail.action', {
		title: '日志配置',
		width: 600,
		height: 300,
	    drag: false,
	    resize: false,
		lock: true,
		ok: function () {
			var iframe = this.iframe.contentWindow;
			iframe.document.getElementById('btn_submit').click();
			return false;
		},
		cancel: true
	});
}

function backUp() {
	if ($("#startDate").val() == '') {
		art.dialog.alert("请选择开始时间");
		return false;
	}
	if ($("#endDate").val() == '') {
		art.dialog.alert("请选择结束时间");
		return false;
	}
	art.dialog.confirm('备份后自动清除相应的日志，你确认对日志进行备份？', function(){
		var options = {
			url: "${path}/sysmgmt/log/backUp.action",
			dataType: 'html',
			error: function(a, b) {
				art.dialog.alert("备份失败！");
				return false;
			},
			beforeSend: function() {
				art.dialog.through({
					id: 'loadingDialog',
					title: false,
				    content: '<img src="${path}/images/08.gif" />',
				    lock: true
				});
			},
			success: function(data) {
				art.dialog.list['loadingDialog'].close();
				var rsObj = eval("("+data+")");
				if(rsObj.result==true || rsObj.result=="true"){
					art.dialog.alert("备份成功", goBack);
				}else{
					if(rsObj.desc!=''){
						art.dialog.alert(rsObj.desc);
					}else{
						art.dialog.alert("备份失败");
					}
				}
			}
		};
		jQuery('#LogForm').ajaxSubmit(options);
	}, function(){});
}

function downloadLogFile() {
	if ($("#logFile").val() == '') {
		art.dialog.alert("请选择历史日志文件");
		return false;
	}
	art.dialog.confirm('你确认下载选中的历史日志？', function(){
		window.open('${path}/sysmgmt/log/download.action?fileName='+$("#logFile").val());
	}, function(){});
}

function goBack(){
	window.location = "${path}/sysmgmt/log/logList.action";
}

function detail(id){

	art.dialog.open("${path}/sysmgmt/log/detail.action?logNo=" + id, 
			{
				title: "详情", 
				width: 450,
				height: 280,
				lock: true

			});
}
</script>
</head>
<body>
<div class="title">
  <h2>日志列表</h2>
</div>
<form action="${path}/sysmgmt/log/logList.action" method="post" name="LogForm" id="LogForm">
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="100px" height="30">关键字：</td>
                  <td width="150px">
			      	<input id="keyWord" name="keyWord" class="form130px" maxlength="50" value="${search.keyWord }"  autocomplete="off" oncontextmenu="return false;"/>
			      </td>
                  <td width="100px" height="30">用户账号：</td>
                  <td width="150px">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="u" > 
							<option value="${u.operatorNo }" <c:if test="${search.operatorNo==u.operatorNo }"> selected="selected" </c:if>>${u.operatorId }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="100px" height="30">日志类型：</td>
                  <td width="150px">
                  	<select name="logTypeNo" class="form130px"  >
                  		<option value="">--请选择--</option>
						<option value="1" <c:if test="${search.logTypeNo==1 }"> selected="selected" </c:if>>登录日志</option>
						<option value="2" <c:if test="${search.logTypeNo==2 }"> selected="selected" </c:if>>操作日志</option>
						<option value="3" <c:if test="${search.logTypeNo==3 }"> selected="selected" </c:if>>错误日志</option>
				  </select>
                  </td>
                   <td colspan="2"><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"></td>
                </tr>
                <tr>
                  <td width="100px" height="30">开始时间：</td>
                  <td width="150px"><input type="text" id="startDate" name="startDate" value="${search.startDate }"></td>
                  <td width="100px" height="30">结束时间：</td>
                  <td width="150px"><input type="text" id="endDate" name="endDate" value="${search.endDate }"></td>

                  <td width="100px" height="30">历史日志：</td>
                  <td width="150px">
                  <select name="logFile" class="form130px" id="logFile">
                  	<option value="">--请选择--</option>
                  	<c:forEach items="${logFilesList}" var="u" varStatus="st" > 
                  		<option value="${u.key }" title="${u.value }">${u.value }</option>
                  	</c:forEach>
                  </select>
                 </td>
                 <td colspan="2">
                  <input class="btnQuery" type="button" value="下载" onclick="downloadLogFile();">
                 </td>
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
       <c:set var="config" value="false" />
       <c:set var="manual" value="false" />
       <c:set var="detail" value="false" />
	   <c:forEach var="item" items="${functionList}">   
	    <c:if test="${item eq 'config'}">     
	         <input class="btn btn80" type="button" value="备份配置" onclick="changeLogConfig();"/>
			 <c:set var="config" value="true" />  
		</c:if> 
	    <c:if test="${item eq 'manual'}">     
			 <c:set var="manual" value="true" />  
		</c:if> 
	    <c:if test="${item eq 'detail'}">     
			 <c:set var="detail" value="true" />  
		</c:if> 
	   </c:forEach>
	   		<c:if test="${manual}">     
	     	 <input class="btn btn80" name="" type="button" onclick="backUp();" value="手动备份"/>
		</c:if> 
    </div>
    
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">序号</th>
          <th width="*%">用户账号</th>
          <th width="*%">运营商</th>
          <th width="*%">日志类型</th>
          <th width="*%">日志描述</th>
          <th width="*%">日志时间</th>
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
	  <c:forEach items="${list}" var="u" varStatus="st">
        <tr>
          <td align="center"><input type="checkbox" name="rtId" value="${u.logNo }" data="">
          </td>
          <td align="center">${ st.count}</td>
          <td align="center">${u.operator.operatorId }</td>
          <td align="center">
              <c:if test="${u.company == null && u.companyNo == -1}">
                                            超级管理员
              </c:if>
              <c:if test="${u.company != null}">
                 ${u.company.companyName}
              </c:if>
          </td>
          <td align="center">${u.logType.logTypeName }</td>
          <td>
            <c:choose>
             <c:when test="${fn:length(u.logDescribe) > 30}">
                 ${fn:substring(u.logDescribe, 0, 30)}...
             </c:when>
             <c:otherwise>
                 ${u.logDescribe }
             </c:otherwise>
            </c:choose>  
          </td>
          <td align="center">
          	${u.logTime}
          </td>
          <td class="tdOpera2" align="center">
              <c:if test="${detail}"><a href='javascript:;' onclick='detail(${u.logNo})'>详情</a></c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
   <div class="hidden" id="parentNames"></div>
</form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
	   <c:if test="${config}">     
	         <input class="btn btn80" type="button" value="备份配置" onclick="changeLogConfig();"/>
		</c:if> 
		<c:if test="${manual}">     
	     	 <input class="btn btn80" name="" type="button" onclick="backUp();" value="手动备份"/>
		</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
