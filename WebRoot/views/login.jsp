<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="path.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/ico" href="images/favicon.ico" />
<title>BMS</title>
<script language="JavaScript"> if (window != top) top.location.href = location.href; </script>
<link href="${path}/css/styles.css" type="text/css" media="screen" rel="stylesheet" />
<link href="${path}/css/keyboard.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<script src="${path}/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.keyboard.extension-typing.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.keyboard.js"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

</head>
<script type="text/javascript">
$(document).ready(function() {
	$('#user_password').keyboard({
		openOn: null,
		stayOpen: true,
		layout: 'qwerty'
	}).addTyping();

	$('#passwd').click(function() {
		$('#user_password').getkeyboard().reveal();
	});

	$(".logininput").blur(function() {
		if ($(this).val() == "") {
			$(this).css("border-color", "red");
							}
		else
			$(this).css("border-color", "#D9D6C4");
	});

	$("#loginbtn").click(function() {
		var k = 0;
		var ajaxhtml = "";
		$(".logininput").each(function(i, obj) {
			if ($(obj).val() == "") {
				k++;
				$(this).css("border-color", "red");
				$(this).focus();
				return false;
			}
		});
		
		if (k != 0) return;
		ajaxhtml = $("#loginbtn").html();
		$("#loginbtn").html("Loading....  <img src='${path}/images/loading.gif' alt='Announcement' /> ");
		$("#loginbtn").attr("disabled", "disabled");
		
		var options = {
			url: "${path}/login.action",
			dataType: 'html',
			error: function(a, b) {
				art.dialog.alert("网络有问题，请重试！", function(){
					$("#loginbtn").html("<img src='${path}/images/key.png' alt='Announcement' />Login");
					k = 0;
					$("#loginbtn").removeAttr("disabled");
				});
				return false;
			},
			success: function(data) {
				eval("var rsobj = "+data+";");
				//var rsobj = eval("("+data+")");
				if(rsobj.result=="true"){
					goBack();
				}else{
					if (rsobj.desc=="isSuspend") {
						art.dialog.alert("您的账号己被禁用，请联系管理员！", function(){
							$("#loginbtn").html("<img src='${path}/images/key.png' alt='Announcement' />Login");
							k = 0;
							$("#loginbtn").removeAttr("disabled");
						});
					} else {
						art.dialog.alert("用户账号或密码错误，请重试！", function(){
							$("#loginbtn").html("<img src='${path}/images/key.png' alt='Announcement' />Login");
							k = 0;
							$("#loginbtn").removeAttr("disabled");
						});
					}
				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
	});

	document.getElementById("user_password").onkeydown = keydown;
	document.getElementById("user_name").onkeydown = keydown;
	
	if ($("#user_name").val() == "") {
		$("#user_name").focus();
	} else {
		$("#user_password").focus();
	}
	
});

function goBack(){
	window.location = "${path}/main.action";
}

function keydown(e){
	var currKey = 0, e = e || event;
	if(e.keyCode == 13)
		$("#loginbtn").click();
}

</script>
<body id="login">
<div id="wrappertop"></div>
<div id="wrapper">
<div id="content">
<div id="header">
  <h1><a href="">
    <img src="${path}/images/web_logo.png" height="60" alt="db">
    </a></h1>
</div>
<div id="darkbanner" class="banner438">
  <h2>BMS</h2>
  <h4 style="color:white;">basic management system</h4>
</div>
<div id="darkbannerwrap"> </div>
<form name="form1" id="form1" method="post" action="">
<fieldset class="form">
<p>
  <label class="loginlabel" for="user_name"> Username:</label>
  <input class="logininput ui-keyboard-input ui-widget-content ui-corner-all" name="operatorId" id="user_name" type="text" value="admin" maxlength="20" />
</p>
<p>
  <label class="loginlabel" for="user_password"> Password:</label>
  <span>
  <input class="logininput" name="operatorPwd" id="user_password" type="password" value="123456" maxlength="20" />
  <img id="passwd" class="tooltip" alt="Click to open the virtual keyboard" title="Click to open the virtual keyboard" src="${path}/images/keyboard.png" />
  </span>
</p>
<button id="loginbtn" type="button" class="positive" name="Submit"><img src="${path}/images/key.png" alt="Announcement" />Login</button>
<!--<ul id="forgottenpassword">
  <li class="boldtext">|</li>
  <li>
    <input type="checkbox" name="remember" id="rememberMe" />
    <label for="rememberMe">Remember me</label>
  </li>
</ul>-->
</fieldset>
</form>
</div>
</div>
<div id="wrapperbottom_branding">
  <div id="wrapperbottom_branding_text">浏览器: <a href="http://windows.microsoft.com/zh-CN/internet-explorer/products/ie/home" target="_blank">Internet Explorer 7.0以上</a> | <a href="http://firefox.com.cn/" target="_blank">Mozilla Firefox</a> | <a href="http://www.google.cn/chrome" target="_blank">Google Chrome</a></div>
</div>
<!-- key -->
<!-- JXU1RjAyJXU2QjY1JXU4QkY3JXU2QzQyJXU2Q0ExJXU3NjdCJXU5N -->
</body>
</html>
