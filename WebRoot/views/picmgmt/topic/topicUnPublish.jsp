<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发布专题</title>
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
 <script src="${path}/js/picmgmt/topic/topic.js"></script>
<script type="text/javascript">
/* $(document).ready(function(){
	$("#acascade").change(function() {
		var acascade = $("#acascade option:selected").val();
		if (acascade == 1) {
			$("#pcascadeTr").show();
		} else {
			$("#pcascadeTr").hide();
			$("#pcascade").val(0);
		}
	});
	
}); */

function unPublish() {
		art.dialog.confirm('你确认取消发布操作？', function(){
				var options = {
					url: "${path}/picmgmt/topic/topicUnPublish.action",
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
						art.dialog.alert("取消发布失败！");
					},
					success: function(data) {
						art.dialog.list['broadcastLoading'].close();
						eval("var rsobj = "+data+";");
						if(rsobj.result=="true" || rsobj.result=="true"){
							art.dialog.alert('取消发布成功！', function(){
								artDialog.open.origin.document.getElementById('btnQuery').click();
								}); 
						}else{
							art.dialog.alert("取消发布失败！");
						}
					}
				};
				jQuery('#form1').ajaxSubmit(options);
			}, function(){
		    art.dialog.tips('你取消了操作！');
		});
	}
</script>
</head>

<body>
<form id="form1" name="form1">
<input type="hidden" name="topicIds" id="topicIds" value="${topicIds }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">取消发布相册</td>
    <td class="tdBlue">
      <select id="acascade" name="acascade" class="form130px"  >
               <option value="0">否</option>
               <option value="1">是</option>
	</select>
  </tr>
<!--   <tr id="pcascadeTr" style="display: none;" >
    <td width="35%" class="tdBlue2">取消发布图片</td>
    <td class="tdBlue">
      <select id="pcascade" name="pcascade" class="form130px"  >
               <option value="0">否</option>
               <option value="1">是</option>
	</select>
  </tr> -->
</table>
</form>
</body>
</html>
