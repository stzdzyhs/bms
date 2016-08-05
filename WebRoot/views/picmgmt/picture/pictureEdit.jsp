<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图片信息</title>
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

<!-- 日期控件 -->
<script src="${path}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
	
	var videoTime = $("#videoTime");
	videoTime.addClass("Wdate");
	videoTime.eq(0).click(function(){WdatePicker({dateFmt:'HH:mm:ss'});});
	
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/picmgmt/picture/saveOrUpdatePicture.action",
			beforeSend: function() {
				
				var contentPath = $("#contentPath").val();
				if (contentPath == null || typeof(contentPath)=="undefined" || contentPath == ''){
					art.dialog.alert("请上传图片！");
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
					art.dialog.alert("保存失败！"+rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#picName").formValidator({onFocus:"图片名称不能为空，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"resname",dataType:"enum",onError:"图片名称由中文、大小写英文字母、数字、以及下划线组成"}).defaultPassed();
	$("#frameNum").formValidator({empty:true,onFocus:"视频帧数只支持正整数，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"视频帧数只支持正整数，请确认"}).defaultPassed();
	
	$("#showOrder").formValidator({onFocus:"显示顺序只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"显示顺序只支持正整数且不能为0"}).defaultPassed();

	//上传组件初始化
	var param = new Object();
	param.ele = document.getElementById("selectfiles"); //dom  
	param.loadingEle = document.getElementById("showUploadFiles"); //显示上传中图标的位置
	var fileType = "*.gif;*.jpg;*.png";
	var picFormatPattern = '${album.picFormatPattern}';
	if (picFormatPattern != null && picFormatPattern != ''){
		fileType = picFormatPattern;
	}
	param.fileType = fileType; //格式限制，中间用英文分号隔开  
	var albumNo = '${album.albumNo}';
	var operatorNo = '${activeOperator.operatorNo}';
	var parameter = "?operatorNo=" + operatorNo + "&albumNo=" + albumNo;
	var pictureId = '${picture.id}';
    parameter += (pictureId != null && pictureId != '') ? "&pictureId="+pictureId : '';
	param.uploadurl = "${path}/picmgmt/picture/pictureUploadFile.action" + parameter;
	var fileSizeLimit = "30 MB";
	var picSize = '${album.picSize}';
	if (picSize != null && picSize > 0){
		fileSizeLimit = picSize + " KB";
	}
	param.fileSizeLimit = fileSizeLimit;
	//param.beforeUpload= beforeUploadFile;
	param.paddingLeft=17;
	(new DjwlSWF()).init(param, showUploadFile);
	
	//初始化图片
	var picArray = '${picture.picPath}';
	if(picArray){
		var strs= new Array(); //定义一数组
		strs=picArray.split(","); //字符分割 
		var showRegion = document.getElementById("showUploadFiles");
		var html="";
		for (i=0;i<strs.length ;i++ )    
	    {   
	    	html += '<span title="存放路径：'+strs[i]+'">';
			html += '<img src="${path}/'+strs[i]+'" value="' + strs[i] + '" height="44" title="'+strs[i]+'" style="cursor: pointer" />';
			html +='<input type="hidden" name="picPath" id="contentPath" value="'+ strs[i] + '" />';
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
					var url = "${path}/picmgmt/picture/pictureDeleteFile.action.action?filePath="+filePath;
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
				html +='<input type="hidden" name="picPath" id="contentPath" value="' + rsObj.filePath + '" />';
				html += '<img src="${path}/'+rsObj.filePath+'" value="' + rsObj.filePath + '" height="44" title="'+rsObj.fileName+'" style="cursor: pointer" />';
				html += '</span><img src="${path}/images/common/icon/icon30_14-0.gif" title="' + rsObj.filePath + '" width="20" height="20" onclick="removeFile(this)" title="删除" style="cursor: pointer" />';
				showRegion.innerHTML = showRegion.innerHTML + html;
				
			}
			$("#fileNum").val(curFileNum+1);
		}else{
			if(rsObj.desc == 'spaceSizeLimit'){
				art.dialog.alert("上传失败，您的使用空间不足，请与管理员联系！");
			}else if(rsObj.desc =='resolutionLimit'){
				art.dialog.alert("图片分辨率超出最大限制！");
			}else{
				art.dialog.alert("图片上传失败！");
			}
		}
	}
}

function removeFile(delImg){
	art.dialog.confirm('您确认删除操作？', function(){
		var filePath = delImg.title;
		if(filePath!=null && filePath!=""){
			var url = "${path}/picmgmt/picture/pictureDeleteFile.action";
			$.ajax({
				type: "post",
				async: false,
				data: {'pictureId':'${picture.id}','filePath':filePath},
				dataType: "json",
				url: url,
				success:function(data){
					art.dialog.alert("删除图片成功！");
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
	document.getElementById("form2").action = "${path}/picmgmt/picture/pictureList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>图片信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${picture.id}" />
<input type="hidden" name="albumNo" id="albumNo" value="${album.albumNo }" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${picture.operatorNo }" />
<input type="hidden" name="createTime" id="createTime" value="${picture.createTime }" />
<input type="hidden" name="checkCode" id="checkCode" value="${picture.checkCode}" />
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
      <td class="tdBlue" width="10%">显示顺序</td>
    <td class="tdBlue" width="40%">
            <input <c:if test="${album.captureFlag == 1}">disabled</c:if> name="showOrder" id="showOrder" maxlength="8" value="${picture.showOrder == null ? 100 : picture.showOrder}" size="5">
     </td>
    <td class="tdBlue" width="10%">图片名称</td>
    <td class="tdBlue" width="40%">
      <input name="picName" id="picName" maxlength="${album.picNameLen != null and album.picNameLen < 30 ? album.picNameLen : 30 }" value="${picture.picName}" >
     </td>
  </tr>
  <tr height="80">
      <td class="tdBlue" width="10%">图片标签</td>
    <td class="tdBlue" width="40%">
      <input name="picLabel" id="picLabel" maxlength="100" value="${picture.picLabel}" >
     </td>
    <td class="tdBlue">图片文件</td>
    <td class="tdBlue">
	<div style="width:250px; float:left;">
		<span id="selectfiles">请选择</span>
	</div>
	<c:if test="${picture.picPath == null or picture.picPath == ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="0" />
	</c:if>
	<c:if test="${picture.picPath != null and picture.picPath != ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="1" />
	</c:if>
	<div id="fileNumTip" style="width:250px; float:left;"></div>
	<div id="showUploadFiles" style="width:100%; float:left; vertical-align:bottom"></div>
    </td>
  </tr>
  <tr>
  
    <td class="tdBlue" width="10%">图片简介</td>
    <td class="tdBlue" width="40%">
      <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, ${album.picDescLen != null and album.picDescLen < 300 ? album.picDescLen : 300 })" name="picDesc">${picture.picDesc}</textarea>
     </td>
      <td class="tdBlue" width="10%"></td>
    <td class="tdBlue" width="40%">
     </td>

  </tr>
</table>
</fieldset>

<fieldset><legend>&nbsp;详细信息</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue"  width="10%">图片作者</td>
    <td class="tdBlue"  width="40%">
           <input name="picAuthor" id="picAuthor" maxlength="30" value="${picture.picAuthor}">
	</td>
    <td class="tdBlue" width="10%">图片来源</td>
    <td class="tdBlue" width="40%">
     <input name="picSource" id="picSource" maxlength="30" value="${picture.picSource}">
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="10%">参与投票</td>
    <td class="tdBlue" width="40%">
             <select id="voteFlag" name="voteFlag" class="form130px"  >
			<c:forEach items="${pictureVoteFlagMap}" var="m" > 
			   <option value="${m.key }" <c:if test="${picture.voteFlag==m.key }"> selected="selected" </c:if>>${m.value }</option>
			</c:forEach> 
		</select>
	</td>
    <td class="tdBlue">视频时间</td>
    <td class="tdBlue">
         <input name="videoTime" id="videoTime" maxlength="8" value="${picture.videoTime}" />
     </td>
  </tr>
  <tr>
    <td class="tdBlue" width="10%">视频帧数</td>
    <td class="tdBlue" width="40%">
 <input name="frameNum" id="frameNum" maxlength="8" size="8" value="${picture.frameNum}" />
	</td>
    <td class="tdBlue"></td>
    <td class="tdBlue">
     </td>
  </tr>
</table>
</fieldset>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
<form id="form2" name="form2" method="post">
<input type="hidden" name="albumNo"  value="${search.albumNo}" />
<!-- 缓存查询条件 start -->
<input type="hidden" name="picName" id="picName" value="${search.picName}" />
<input type="hidden" name="status" id="status" value="${search.status}" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
