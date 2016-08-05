<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提示</title>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<style type="text/css">
<!--
#apDiv1 {
	position:absolute;
	width:100%;
	height:100px;
	margin-top:300px;
}
-->
</style>
<script type="text/javascript">
$(document).ready(function(){
	var desc = '${desc}';
	if (desc != null && desc.length > 0){
		art.dialog.alert(desc, function(){
			 $.each(artDialog.open.origin.art.dialog.list, function (index, item) {

		         item.close();

		     });
		});
	}
});

</script>
</head>
<body>
</body>
</html>