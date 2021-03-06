<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>视频信息</title>
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
	
 	
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/picmgmt/video/saveOrUpdateVideo.action",
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
				art.dialog.alert("保存失败！");
				return false;
			},
			success: function(data) {
				art.dialog.list['broadcastLoading'].close();
				eval("var rsobj = "+data+";");
				if(rsobj.result=="true"){
					art.dialog.alert("保存成功！", goBack);
				}else{
					art.dialog.alert("保存失败！" + rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#videoName").formValidator({onFocus:"视频名称不能为空，请确认",onCorrect:"&nbsp;",tipID:"videoName_tip"}).regexValidator({regExp:"resname",dataType:"enum",onError:"视频名称由中文、大小写英文字母、数字、以及下划线组成"}).defaultPassed();
	$("#sourceUrl").formValidator({onFocus:"下载地址不能为空，请确认",onCorrect:"&nbsp;",tipID:"sourceUrl_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"下载地址不能为空，请确认"},min:1,onError:"下载地址不能为空，请确认"}).defaultPassed();
	$("#width").formValidator({empty:true,onFocus:"图片宽度只支持正整数，请确认",onCorrect:"&nbsp;",tipID:"width_tip"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"图片宽度只支持正整数，请确认"}).defaultPassed();
	$("#height").formValidator({empty:true,onFocus:"图片高度只支持正整数，请确认",onCorrect:"&nbsp;",tipID:"height_tip"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"图片高度只支持正整数，请确认"}).defaultPassed();
	$("#interval").formValidator({empty:true,onFocus:"间隔时间只支持正整数，请确认",onCorrect:"&nbsp;",tipID:"interval_tip"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"间隔时间只支持正整数，请确认"}).defaultPassed();
	
	var type = ${activeOperator.type};
	if (type == 0){
		$("#companyNo").formValidator({onFocus:"请选择角色所属运营商",onCorrect:"&nbsp;",tipID:"companyNo_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择角色所属运营商"},min:1,onError:"请选择角色所属运营商"}).defaultPassed();
	}
	
	var userName = '${video.userName}';
	if (userName != null && userName != ''){
		$("#ftp").attr("checked","checked");
		$("#browseDiv").show();
		$("#userNameTr").show();
		$("#passwordTr").show();
		$("#userName").formValidator({onFocus:"用户名不能为空，请确认",onCorrect:"&nbsp;",tipID:"userName_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"用户名前后不能带空格，请确认"},min:1,onError:"用户名不能为空，请确认"}).defaultPassed();
		$("#password").formValidator({onFocus:"密码不能为空，请确认",onCorrect:"&nbsp;",tipID:"password_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码前后不能带空格，请确认"},min:1,onError:"密码不能为空，请确认"}).defaultPassed();
	}else{
		$("#http").attr("checked","checked");
	}
	
 	$("input[name='downType']").change(function() {
		var uploadType = $("input[name='downType']:checked").val();
		if (uploadType == 1) {
			$("#browseDiv").show();
			$("#userNameTr").show();
			$("#passwordTr").show();
			$("#userName").formValidator({onFocus:"用户名不能为空，请确认",onCorrect:"&nbsp;",tipID:"userName_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"用户名前后不能带空格，请确认"},min:1,onError:"用户名不能为空，请确认"}).defaultPassed();
			$("#password").formValidator({onFocus:"密码不能为空，请确认",onCorrect:"&nbsp;",tipID:"password_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码前后不能带空格，请确认"},min:1,onError:"密码不能为空，请确认"}).defaultPassed();
		} else {
			$("#browseDiv").hide();
			$("#userNameTr").hide();
			$("#passwordTr").hide();
			$("#userName").val("");
			$("#password").val("");
			$("#userName").formValidator(null);
			$("#password").formValidator(null);
		}
	});
});
function goBack(){
	document.getElementById("form2").action = "${path}/picmgmt/video/videoList.action";
	document.getElementById("form2").submit();
}

function browseFtpServer()
{
	art.dialog.open("${path}/picmgmt/video/browseVideoFtpServerList.action", 
			{
				title: "FTP服务器列表", 
				width: 980,
				height: 450,
				lock: true

			});
}
</script>
</head>

<body>
<div class="title"><h2>视频信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${video.id }" />
<input type="hidden" name="assetId" id="assetId" value="${video.assetId}" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${video.operatorNo }" />
<input type="hidden" name="createTime" id="createTime" value="${video.createTime }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">视频名称</td>
    <td class="tdBlue">
      <input name="videoName" id="videoName" maxlength="30" value="${video.videoName}" >
      <div id="videoName_tip" style="width: 60%; float: right"></div>
      </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">下载方式</td>
    <td class="tdBlue">
      <input id="http" name="downType" type="radio" value="0">http &nbsp;&nbsp;&nbsp;<input id="ftp" name="downType" type="radio" value="1">ftp
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">下载地址</td>
    <td class="tdBlue">
       <input name="sourceUrl" id="sourceUrl" maxlength="200" value="${video.sourceUrl}" >
       <div id="browseDiv" style="display: none;width: 78%; float: right">
          <input type="button" class="btnQuery" value="请选择" onClick="browseFtpServer()" >
       </div> <div id="sourceUrl_tip" style="width: 60%; float: right"></div>
       
    </td>
  </tr>
  <tr id="userNameTr" style="display: none;">
    <td width="35%" class="tdBlue2">用户名</td>
    <td class="tdBlue">
       <input name="userName" id="userName" maxlength="50" value="${video.userName}"/>
          <div id="userName_tip" style="width: 60%; float: right"></div>
    </td>
  </tr>
    <tr id="passwordTr" style="display: none;">
    <td width="35%" class="tdBlue2">密码</td>
    <td class="tdBlue">
       <input name="password" id="password" maxlength="50" value="${video.password}" >
         <div id="password_tip" style="width: 60%; float: right"></div>
    </td>
  </tr>
      <tr>
    <td width="35%" class="tdBlue2">图片宽度</td>
    <td class="tdBlue">
       <input name="width" id="width" maxlength="5" value="${video.width == null ? 640 : video.width}" size="6">
       <div id="width_tip" style="width: 60%; float: right"></div>
    </td>
  </tr>
      <tr>
    <td width="35%" class="tdBlue2">图片高度</td>
    <td class="tdBlue">
       <input name="height" id="height" maxlength="5" value="${video.height == null ? 360 : video.height}" size="6">
       <div id="height_tip" style="width: 60%; float: right"></div>
    </td>
  </tr>
      <tr>
    <td width="35%" class="tdBlue2">间隔时间（单位：秒）</td>
    <td class="tdBlue">
       <input name="interval" id="interval" maxlength="5" value="${video.interval == null ? 60 : video.interval}" size="6">
       <div id="interval_tip" style="width: 60%; float: right"></div>
    </td>
  </tr>
 <c:if test="${activeOperator.type == 0}">
  <tr>
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
         <select id="companyNo" name="companyNo" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${video.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		</select>
		 <div id="companyNo_tip" style="width: 60%; float: right"></div>
      </td>
  </tr>
 </c:if>
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="videoName" id="videoName" value="${search.videoName}" />
<input type="hidden" name="status" id="status" value="${search.status}" />
<input type="hidden" name="companyNo"  value="${search.companyNo}" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
