<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>相册信息</title>
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
	$("legend").click(function(){
		$(this).next().toggle();
	});
	
	$("#picWidth").focus(function(){
		var picWidth = $(this).val();
		if (picWidth.trim() == '宽'){
			$(this).val('');
		}
	});
	
	$("#picWidth").blur(function(){
		var picWidth = $(this).val();
		if (picWidth.trim() == ''){
			$(this).val('宽');
		}
	});
	
	$("#picHeight").focus(function(){
		var picHeight = $(this).val();
		if (picHeight.trim() == '高'){
			$(this).val('');
		}
	});
	
	$("#picHeight").blur(function(){
		var picHeight = $(this).val();
		if (picHeight.trim() == ''){
			$(this).val('高');
		}
	});

	
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/picmgmt/album/saveOrUpdateAlbum.action",
			beforeSend: function() {
				
				var picHeight = $("#picHeight").val();
				var picWidth = $("#picWidth").val();
				var picResolutionReg = /^[1-9][0-9]*$/;
				if (picHeight != "高" && picHeight != '' && !picResolutionReg.test(picHeight)){		
					art.dialog.alert("图片分辨率高必须为正整数，且不能为0！");
					$("#picHeight").focus();
					return false;
				}
				
				if (picWidth != "宽" && picWidth != '' && !picResolutionReg.test(picWidth)){		
					art.dialog.alert("图片分辨率宽必须为正整数，且不能为0！");
					$("#picWidth").focus();
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
					art.dialog.alert("保存失败！" + rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#albumName").formValidator({onFocus:"相册名称不能为空，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"resname",dataType:"enum",onError:"相册名称由中文、大小写英文字母、数字、以及下划线组成"}).defaultPassed();
	$("#picSize").formValidator({empty:true,onFocus:"图片大小只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"图片大小只支持正整数且不能为0"}).defaultPassed();
	$("#picNameLen").formValidator({empty:true,onFocus:"图片名称最大长度只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"图片名称最大长度只支持正整数且不能为0"}).defaultPassed();
	$("#picDescLen").formValidator({empty:true,onFocus:"图片简介最大长度只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"图片简介最大长度只支持正整数且不能为0"}).defaultPassed();
	var type = ${activeOperator.type};
	if (type == 0){
		$("#companyNo").formValidator({onFocus:"请选择角色所属运营商",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择角色所属运营商"},min:1,onError:"请选择角色所属运营商"}).defaultPassed();
	}
	
	$("#showOrder").formValidator({onFocus:"显示顺序只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"显示顺序只支持正整数且不能为0"}).defaultPassed();
	
	//上传组件初始化
	var param = new Object();
	param.ele = document.getElementById("selectfiles"); //dom  
	param.loadingEle = document.getElementById("showUploadFiles"); //显示上传中图标的位置
	param.fileType = "*.gif;*.jpg;*.png"; //格式限制，中间用英文分号隔开  
	var operatorNo = '${activeOperator.operatorNo}';
	var parameter = "?operatorNo=" + operatorNo;
	var albumNo = '${album.albumNo}';
    parameter = parameter + ((albumNo != null && albumNo != '') ? "&albumNo="+albumNo : '');
	param.uploadurl = "${path}/picmgmt/album/albumUploadCover.action" + parameter;
	param.fileSizeLimit = "30 MB";
	param.paddingLeft=17;
	(new DjwlSWF()).init(param, showUploadFile);
	
	//初始化图片
	var picArray = '${album.albumCover}';
	if(picArray){
		var strs= new Array(); //定义一数组
		strs=picArray.split(","); //字符分割 
		var showRegion = document.getElementById("showUploadFiles");
		var html="";
		for (i=0;i<strs.length ;i++ )    
	    {   
	    	html += '<span title="存放路径：'+strs[i]+'">';
			html += '<img src="${path}/'+strs[i]+'" value="' + strs[i] + '" height="44" title="'+strs[i]+'" style="cursor: pointer" />';
			html +='<input type="hidden" name="albumCover" id="contentPath" value="'+ strs[i] + '" />';
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
					var url = "${path}/picmgmt/album/albumDeleteCover.action?filePath="+filePath;
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
				html +='<input type="hidden" name="albumCover" id="contentPath" value="' + rsObj.filePath + '" />';
				html += '<img src="${path}/'+rsObj.filePath+'" value="' + rsObj.filePath + '" height="44" title="'+rsObj.fileName+'" style="cursor: pointer" />';
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
			var url = "${path}/picmgmt/album/albumDeleteCover.action";
			$.ajax({
				type: "post",
				async: false,
				data: {'albumNo':'${album.albumNo}','filePath':filePath},
				dataType: "json",
				url: url,
				success:function(data){
					art.dialog.alert("删除封面成功！");
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
	document.getElementById("form2").action = "${path}/picmgmt/album/albumList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>相册信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="albumNo" id="albumNo" value="${album.albumNo }" />
<input type="hidden" name="captureFlag" id="captureFlag" value="${album.captureFlag}" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${album.operatorNo }" />
<input type="hidden" name="groupId" id="groupId" value="${album.groupId}" />
<input type="hidden" name="createTime" id="createTime" value="${album.createTime }" />
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue" width="10%">显示顺序</td>
    <td class="tdBlue" width="40%">
      <input name="showOrder" id="showOrder" maxlength="8" value="${album.showOrder == null ? 100 : album.showOrder}" size="5">
     </td>
    <td class="tdBlue" width="10%">相册名称</td>
    <td class="tdBlue" width="40%">
      <input name="albumName" id="albumName" maxlength="30" value="${album.albumName}" >
     </td>
  </tr>
  <tr height="80">
    <td class="tdBlue">相册封面</td>
    <td class="tdBlue">
	<div style="width:250px; float:left;">
		<span id="selectfiles">请选择</span>
	</div>
	<c:if test="${cloumn.cover == null or column.cover == ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="0" />
	</c:if>
	<c:if test="${column.cover != null and column.cover != ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="1" />
	</c:if>
	<div id="fileNumTip" style="width:250px; float:left;"></div>
	<div id="showUploadFiles" style="width:100%; float:left; vertical-align:bottom"></div>
    </td>
        <td class="tdBlue" width="10%">相册标签</td>
    <td class="tdBlue" width="40%">
      <input name="albumLabel" id="albumLabel" maxlength="100" value="${album.albumLabel}" >
     </td>
  </tr>
  <tr>
  
      <td class="tdBlue" width="10%">关联模板</td>
    <td class="tdBlue" width="40%">
             <select id="templateId" name="templateId" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${templateList}" var="t" > 
			   <option value="${t.id }" <c:if test="${album.templateId==t.id }"> selected="selected" </c:if>>${t.templateName }</option>
			</c:forEach> 
		</select>

     </td>
    <td class="tdBlue">相册简介</td>
    <td class="tdBlue">
          <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 300)" name="albumDesc">${album.albumDesc}</textarea>
    </td>
  </tr>
  <c:if test="${activeOperator.type == 0}">
  <tr>
    <td class="tdBlue" width="10%">所属运营商</td>
    <td class="tdBlue" width="40%">
         <select id="companyNo" name="companyNo" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${album.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		</select>
      </td>
    <td class="tdBlue" colspan="2"></td>
    </tr>
 </c:if>
</table>
</fieldset>

<fieldset><legend>&nbsp;图片格式信息</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue"  width="10%">图片大小（单位：KB）</td>
    <td class="tdBlue"  width="40%">
           <input name="picSize" id="picSize" maxlength="10" value="${album.picSize}">
	</td>
    <td class="tdBlue" width="10%">图片格式</td>
    <td class="tdBlue" width="40%">
     <c:forEach items="${pictureFormatMap}" var="f">			
			<label><input name="picFormat"  type="checkbox"  value="${f.key}" <c:if test="${fn:indexOf(album.picFormat, f.key)>=0}"> checked </c:if>/>${f.value}&nbsp&nbsp&nbsp</label>	
	</c:forEach>
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="10%">图片分辨率</td>
    <td class="tdBlue" width="40%">
    <input name="picWidth" id="picWidth" maxlength="5" value="${album.picWidth != null ? album.picWidth : '宽' }" size="5" />  * 
    <input name="picHeight" id="picHeight" maxlength="5" value="${album.picHeight != null ? album.picHeight : '高' }" size="5" />
	</td>
    <td class="tdBlue">图片名称最大长度</td>
    <td class="tdBlue">
         <input name="picNameLen" id="picNameLen" maxlength="8" value="${album.picNameLen}" />
     </td>
  </tr>
  <tr>
    <td class="tdBlue" width="10%">图片简介最大长度</td>
    <td class="tdBlue" width="40%">
    <input name="picDescLen" id="picDescLen" maxlength="8" value="${album.picDescLen}" />
	</td>
    <td class="tdBlue" colspan="2"></td>

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
<!-- 缓存查询条件 start -->
<input type="hidden" name="albumName" id="albumName" value="${search.albumName}" />
<input type="hidden" name="status" id="status" value="${search.status}" />
<input type="hidden" name="companyNo"  value="${search.companyNo}" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
