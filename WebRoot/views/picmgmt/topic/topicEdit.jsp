<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专题信息</title>
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
			url: "${path}/picmgmt/topic/saveOrUpdateTopic.action",
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
					if (rsobj.desc == 'capture'){
						art.dialog.alert("截图专题已经存在，只能有一个截图专题！");
					}else{
						art.dialog.alert("保存失败！" + rsobj.desc);
					}
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#topicName").formValidator({onFocus:"专题名称不能为空，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"resname",dataType:"enum",onError:"专题名称由中文、大小写英文字母、数字、以及下划线组成"}).defaultPassed();
	$("#type").formValidator({onFocus:"请选择专题类型",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择专题类型"},min:1,onError:"请选择专题类型"}).defaultPassed();
	
	var type = ${activeOperator.type};
	if (type == 0){
		$("#companyNo").formValidator({onFocus:"请选择专题所属运营商",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择专题所属运营商"},min:1,onError:"请选择专题所属运营商"}).defaultPassed();
	}
	
	
	//上传组件初始化
	var param = new Object();
	param.ele = document.getElementById("selectfiles"); //dom  
	param.loadingEle = document.getElementById("showUploadFiles"); //显示上传中图标的位置
	param.fileType = "*.gif;*.jpg;*.png"; //格式限制，中间用英文分号隔开  
	var operatorNo = '${activeOperator.operatorNo}';
	var parameter = "?operatorNo=" + operatorNo;
	var topicId = '${topic.id}';
    parameter = parameter + ((topicId != null && topicId != '') ? "&topicId="+topicId : '');
	param.uploadurl = "${path}/picmgmt/topic/topicUploadCover.action" + parameter;
	param.fileSizeLimit = "30 MB";
	param.paddingLeft=17;
	(new DjwlSWF()).init(param, showUploadFile);
	
	//初始化图片
	var picArray = '${topic.topicCover}';
	if(picArray){
		var strs= new Array(); //定义一数组
		strs=picArray.split(","); //字符分割 
		var showRegion = document.getElementById("showUploadFiles");
		var html="";
		for (i=0;i<strs.length ;i++ )    
	    {   
	    	html += '<span title="存放路径：'+strs[i]+'">';
			html += '<img src="${path}/'+strs[i]+'" value="' + strs[i] + '" height="44" title="'+strs[i]+'" style="cursor: pointer" />';
			html +='<input type="hidden" name="topicCover" id="contentPath" value="'+ strs[i] + '" />';
			html += '</span><img src="${path}/images/common/icon/icon30_14-0.gif" title="'+ strs[i] + '" width="20" height="20" onclick="removeFile(this)" title="删除" style="cursor: pointer" />';
	    }
	    showRegion.innerHTML = showRegion.innerHTML + html; 
	}
	
});

function showUploadFile(data){
	var rsObj = eval("("+data+")");
	if (rsObj!="undefined") {
		if(rsObj.result==true){
			var curFileNum = parseInt($("#fileNum").val());
			if(curFileNum>=1){
				var filePath = rsObj.filePath;
				if(filePath!=null && filePath!=""){
					var url = "${path}/picmgmt/topic/topicDeleteCover.action?filePath="+filePath;
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
				html +='<input type="hidden" name="topicCover" id="contentPath" value="' + rsObj.filePath + '" />';
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
			var url = "${path}/picmgmt/topic/topicDeleteCover.action";
			$.ajax({
				type: "post",
				async: false,
				data: {'topicId':'${topic.id}','filePath':filePath},
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
	document.getElementById("form2").action = "${path}/picmgmt/topic/topicList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>专题信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${topic.id }" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${topic.operatorNo }" />
<input type="hidden" name="groupId" id="groupId" value="${topic.groupId }" />
<input type="hidden" name="createTime" id="createTime" value="${topic.createTime }" /> 
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">专题名称</td>
    <td class="tdBlue">
      <input name="topicName" id="topicName" maxlength="30" value="${topic.topicName}" ></td>
  </tr>
  <tr height="80">
    <td width="35%" class="tdBlue2">专题封面</td>
    <td class="tdBlue">
	<div style="width:250px; float:left;">
		<span id="selectfiles">请选择</span>
	</div>
	<c:if test="${topic.topicCover == null or topic.topicCover == ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="0" />
	</c:if>
	<c:if test="${topic.topicCover != null and topic.topicCover != ''}">
	   <input type="hidden" name="fileNum" id="fileNum" value="1" />
	</c:if>
	<div id="fileNumTip" style="width:250px; float:left;"></div>
	<div id="showUploadFiles" style="width:100%; float:left; vertical-align:bottom"></div>
    </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">类型</td>
    <td class="tdBlue">
       <select id="type" name="type" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${topicTypeMap}" var="t" > 
			   <option value="${t.key }" <c:if test="${topic.type==t.key }"> selected="selected" </c:if>>${t.value }</option>
			</c:forEach> 
	</select>
    </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">截图标志</td>
    <td class="tdBlue">
       <select id="captureFlag" name="captureFlag" class="form130px"  >
			<c:forEach items="${captureFlagMap}" var="t" > 
			   <option value="${t.key }" <c:if test="${topic.captureFlag==t.key }"> selected="selected" </c:if>>${t.value }</option>
			</c:forEach> 
	</select>
    </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">关联模板</td>
    <td class="tdBlue">
         <select id="templateId" name="templateId" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${templateList}" var="t" > 
			   <option value="${t.id }" <c:if test="${topic.templateId==t.id }"> selected="selected" </c:if>>${t.templateName }</option>
			</c:forEach> 
		</select>
      </td>
  </tr>
 <c:if test="${activeOperator.type == 0}">
  <tr>
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
         <select id="companyNo" name="companyNo" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${topic.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		</select>
      </td>
  </tr>
 </c:if>
 
  <tr>
    <td width="35%" class="tdBlue2">专题简介</td>
    <td class="tdBlue">
       <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 300)" name="topicDesc">${topic.topicDesc}</textarea>
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
<input type="hidden" name="topicName" id="topicName" value="${search.topicName}" />
<input type="hidden" name="type" id="type" value="${search.type}" />
<input type="hidden" name="status" id="status" value="${search.status}" />
<input type="hidden" name="companyNo"  value="${search.companyNo}" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
