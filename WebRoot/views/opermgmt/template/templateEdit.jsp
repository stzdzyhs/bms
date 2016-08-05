<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板信息</title>
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
			url: "${path}/opermgmt/template/saveOrUpdateTemplate.action",
			beforeSend: function() {
				
				var contentPath = $("#contentPath").val();
				if (contentPath == null || typeof(contentPath)=="undefined" || contentPath == ''){
					art.dialog.alert("请上传模板文件！");
					return false;
				}
				
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
					art.dialog.alert("保存失败！");
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});


  $("#templateName").formValidator({onFocus:"模板名称不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:30,onError:"模板名称不能为空，请确认"}).regexValidator({regExp:"resname",dataType:"enum",onError:"模板名称由中文、大小写英文字母、数字、以及下划线组成"})
  .ajaxValidator({
   type : "POST",
   dataType : "html",
   async : true,
   url : "${path}/opermgmt/template/checkData.action?id=${template.id}",
   success : function(data){
	eval("var rsobj = "+data+";");
	if(rsobj.result=="true"){
		return true;
	}else{
		return false;
	}
  },
  buttons: $("#button"),
  error: function(jqXHR, textStatus, errorThrown){art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);},
  onError : "该模板名称已存在，请更换模板名称",
  onWait : "正在对模板名称进行合法性校验，请稍候..."
}).defaultPassed();

$("#type").formValidator({onFocus:"请选择模板类型",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择模板类型"},min:1,onError:"请选择模板类型"}).defaultPassed();


	
	//上传组件初始化
	var param = new Object();
	param.ele = document.getElementById("selectfiles"); //dom  
	param.loadingEle = document.getElementById("showUploadFiles"); //显示上传中图标的位置
	param.fileType = "*.zip"; //格式限制，中间用英文分号隔开  Id : '';
	var operatorNo = '${activeOperator.operatorNo}';
	var parameter = "?operatorNo=" + operatorNo;
	param.uploadurl = "${path}/opermgmt/template/templateUploadFile.action" + parameter;
	param.fileSizeLimit = "30 MB";
	param.paddingLeft=17;
	(new DjwlSWF()).init(param, showUploadFile);
	
	//初始化图片
	var picArray = '${template.filePath}';
	if(picArray){
		var strs= new Array(); //定义一数组
		strs=picArray.split(","); //字符分割 
		var showRegion = document.getElementById("showUploadFiles");
		var html="";
		for (i=0;i<strs.length ;i++ )    
	    {   
	    	html += '<span title="存放路径：'+strs[i]+'">';
			html += '<img src="${path}/images/zip_logo.png" value="' + strs[i] + '" height="44" title="'+strs[i]+'" style="cursor: pointer" />';
			html +='<input type="hidden" name="filePath" id="contentPath" value="'+ strs[i] + '" />';
			html += '</span><img src="${path}/images/common/icon/icon30_14-0.gif" title="'+ strs[i] + '" width="20" height="20" onclick="removeFile(this)" title="删除" style="cursor: pointer" />';
	    }
	    showRegion.innerHTML = showRegion.innerHTML + html; 
	}
	

});

function showUploadFile(data){
	var rsObj = eval("("+data+")");
	if (rsObj!="undefined") {
		if(rsObj.result==true || rsObj.result=="true"){
			var curFileNum = parseInt($("#fileNum").val());
			if(curFileNum>=1){
				var filePath = rsObj.filePath;
				if(filePath!=null && filePath!=""){
					var url = "${path}/opermgmt/template/templateDeleteFile.action?filePath="+filePath;
					$.ajax({
						type: "post",
						url: url
					});
				}
				art.dialog.alert("最多只能上传1个文件！");
				return;
			}
			var showRegion = document.getElementById("showUploadFiles");
			if(showRegion){
				var html = '<span title="存放路径：'+rsObj.filePath+'">';
				html +='<input type="hidden" name="filePath" id="contentPath" value="' + rsObj.filePath + '" />';
				html += '<img src="${path}/images/zip_logo.png" value="' + rsObj.filePath + '" height="44" title="'+rsObj.fileName+'" style="cursor: pointer" />';
				html += '</span><img src="${path}/images/common/icon/icon30_14-0.gif" title="' + rsObj.filePath + '" width="20" height="20" onclick="removeFile(this)" title="删除" style="cursor: pointer" />';
				showRegion.innerHTML = showRegion.innerHTML + html;
				
			}
			$("#fileNum").val(curFileNum+1);
		}else{
			if(rsObj.desc == 'spaceSizeLimit'){
				art.dialog.alert("上传失败，您的使用空间不足，请与管理员联系！");
			}else{
				art.dialog.alert("上传失败！");
			}
		}
	}
}

function removeFile(delImg){
	art.dialog.confirm('您确认删除操作？', function(){
		var filePath = delImg.title;
		if(filePath!=null && filePath!=""){
			var url = "${path}/opermgmt/template/templateDeleteFile.action";
			$.ajax({
				type: "post",
				async: false,
				data: {'filePath':filePath},
				dataType: "json",
				url: url,
				success:function(data){
					art.dialog.alert("删除文件成功！");
					$(delImg).prev().remove();
					$(delImg).remove();
					$("#fileNum").val(parseInt($("#fileNum").val())-1);
				}
			});
		}
	}
	)
}

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/template/templateList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>模板信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${template.id}" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td width="35%" class="tdBlue2">模板名称</td>
    <td class="tdBlue">
      <input name="templateName" id="templateName" maxlength="30" value="${template.templateName}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">类型</td>
    <td class="tdBlue">
         <select id="type" name="type" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${templateTypeMap}" var="m" > 
			   <option value="${m.key }" <c:if test="${template.type==m.key }"> selected="selected" </c:if>>${m.value }</option>
			</c:forEach> 
		</select>
      </td>
  </tr>
    <tr height="80">
    <td width="35%" class="tdBlue2">上传模板</td>
    <td class="tdBlue">
	<div style="width:250px; float:left;">
		<span id="selectfiles">请选择</span>
	</div>
	<c:if test="${template.filePath == null or template.filePath == ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="0" />
	</c:if>
	<c:if test="${template.filePath != null and template.filePath != ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="1" />
	</c:if>
	<div id="fileNumTip" style="width:250px; float:left;"></div>
	<div id="showUploadFiles" style="width:100%; float:left; vertical-align:bottom"></div>
    </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">描述</td>
    <td class="tdBlue">
       <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 300)" name="templateDesc">${template.templateDesc}</textarea>
	</td>
  </tr>
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="sysName"  value="${search.templateName}" />
<input type="hidden" name="sysName"  value="${search.type}" />
<input type="hidden" name="status"  value="${search.status}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
