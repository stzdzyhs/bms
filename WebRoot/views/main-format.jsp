<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="path.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" type="image/ico" href="${path}/images/favicon.ico" />
<title>数字电视多媒体视讯软件</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/menu.js" type="text/javascript"></script>
</head>
<body>
<table id="wrapper" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
<tr>
  <td class="leftWrap">
    <div class="logo_info"><a href="${path}/admin/login/main.action"><img src="${path}/images/logo.png" alt="db" title="db" /></a></div>
    <div class="bannerTitle">MIS 平 台</div>
    <div class="user_info">
      <p> 欢迎您，<strong>${activeOperator.operatorName }</strong></p>
      <br />
      <p>[<a href="javascript:;" onClick="changePassword();">基本资料</a> | <a href="javascript:;" onClick="logoutOpen();">退出</a>]</p>
    </div>
    <div id="menu_container">
      <div class="menu">
        <ul>
          <li class="showMenu" id="menu_0"><img src="${path}/images/monitor.png" />调度监控
            <script type="text/javascript">
            var menu_array_0 = new Array();
            menu_array_0.push({id:"-1", name:"调度监控", url:"${path}/caption/mainMonitor.action"});
            </script>
          </li>
          <c:forEach var="u"  items="${menuList}" > 
          	<c:forEach items="${activeList}" var="r">
          		<c:if test="${r.commandNo == u.commandNo}">
					<li class="showMenu" id="menu_${u.commandId }">
			          <img src="${path}/images/${u.moduleName }.png" />${u.commandName }
			          <c:if test="${u.isMulti == 1}">
			          <span class="menuArrows"><img src="${path}/images/menu_arrows.png" /></span>
			          </c:if>
			          <c:if test="${u.isMulti == 1}">
			            <ul class="subMenu-ul" style="display: none; ">
			            <c:forEach items="${u.commands}" var="u1">
				            <c:forEach items="${activeList}" var="r1">
					            <c:if test="${r1.commandNo == u1.commandNo}">
					            	<c:if test="${u1.commandLevel == 2}">
						              <li class="subMenu" id="menu_${u1.commandId }"><span>${u1.commandName }</span></li>
						              <script type="text/javascript">
						              	var menu_array_${u1.commandId } = new Array();
						              	<c:forEach items="${u1.commands}" var="u2">
							              	<c:forEach items="${activeList}" var="r2">
								              	<c:if test="${r2.commandNo == u2.commandNo}">
								              	   menu_array_${u1.commandId }.push({id:"${u2.commandId }", name:"${u2.commandName }", url:"${path}${u2.moduleName }"});
								              	</c:if>
							              	</c:forEach>
						              	</c:forEach>
						              </script>
					           	   </c:if>
					     	   </c:if>
				    	   </c:forEach>
			 		   </c:forEach>
			           </ul>
			      </c:if>
			     <c:if test="${u.isMulti == 0}">
		            <script type="text/javascript">
			            var menu_array_${u.commandId } = new Array();
			            <c:forEach items="${u.commands}" var="u1">
				            <c:forEach items="${activeList}" var="r1">
					            <c:if test="${u1.commandLevel == 3 && r1.commandNo == u1.commandNo && (activeOperator.level==2 && !(u1.moduleName=='/authorize/userFeature_list.action' && regionType==0) || activeOperator.level==1 && u1.moduleName!='/authorize/userFeature_list.action' && u1.moduleName!='/authorize/userNo_list.action')}">
					            	menu_array_${u.commandId }.push({id:"${u1.commandId }", name:"${u1.commandName }", url:"${path}${u1.moduleName }"});
					            </c:if>
				            </c:forEach>
			            </c:forEach>
		            </script>
          		</c:if>
         	</c:if>
        </c:forEach>
       </c:forEach>
          
        </ul>
      </div>
    </div>
  </td>
  <td class="rightWrap">
    <div id="main_nav" style="min-width: 1050px;">
      <ul class="">
        <li id="man_nav_1" class="ui-state-default ui-corner-top"><img src="${path}/images/home.png" style="vertical-align:middle" alt="首页"/></li>
      </ul>
    </div>
    <div id="main_content" style="min-width: 1050px;">
      <iframe id="mainFrame" name="mainFrame" style="display:block" frameborder="0" scrolling="no" src="${path}/welcome.html" onload="dyniframesize();"></iframe>
    </div>
  </td>
</tr>
</table>
<div class="switchLeft" onclick="switchMenu()"></div>
<div class="switchRight" onclick="switchMenu()"></div>
<div id="about">
  <div class="close"><!-- a title="关闭" onClick="$('#about').slideUp('slow');" href="javascript:void(0)">×</a> -->
    <a rel="nofollow" title="版本信息" href="javascript:void(0);" onClick="infoOpen();" class="version">版本信息</a>
    <a rel="nofollow" title="帮助手册" href="javascript:void(0);" onClick="helpOpen();" class="help">帮助手册</a>
  </div>
</div>
<div class="backToTop" title="返回顶部" style="display: none;">返回顶部</div>
</body>
</html>
