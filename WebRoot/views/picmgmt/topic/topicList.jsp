<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>专题列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/checkbox.js"></script>
 <script src="${path}/js/picmgmt/topic/topic.js"></script>
 <script src="${path}/js/opermgmt/template/template.js"></script>
<script type="text/javascript">
var activeOpNo;
$(document).ready(function(){
	$("#topicName").keyup(function(){
		var topicName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (topicName != null && topicName != '' && !resNameReg.test(topicName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
	var type = ${activeOperator.type};
	activeOpNo = ${activeOperator.operatorNo};
	if(type == 2){
		activeOpNo = ${activeOperator.createBy};
	}
	document.getElementById("activeOpNo").value=activeOpNo;
});
//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='topicIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		for (var i=0; i<sets.length; i++){
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
				art.dialog.alert("不能直接删除该状态的专题！");
				return false;
			}
			var createdBy = $("#createdBy" + sets[i].value).val();
			if(activeOpNo != -1 && createdBy != activeOpNo){
				art.dialog.alert("无权操作分配的专题！");
				return false;
			}
		}
	} else {
		var aa = document.getElementsByName("topicIds");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
			art.dialog.alert("不能直接删除该状态的专题！");
			return false;
		}
		var createdBy = $("#createdBy" + id).val();
		if(activeOpNo != -1 && createdBy != activeOpNo){
			art.dialog.alert("无权操作分配的专题！");
			return false;
		}
	}

	var param = id != '' ? "?topicIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/picmgmt/topic/topicDelete.action"+ param,
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
					art.dialog.alert("删除失败！");
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true"){
						art.dialog.alert("删除成功！", goBack);
					}else{
						art.dialog.alert("删除失败！");
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/picmgmt/topic/topicList.action";
	document.getElementById("form1").submit();
}
//进入新增编辑页面
function toEditPage(topicId){
	var oldStatus = $("#oldStatus" + topicId).val();
	if (typeof(topicId)!='undefined' && oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
		art.dialog.alert("该状态的专题，不能编辑！");
		return false;
	}
	
	if(topicId==null || typeof(topicId)=="undefined"){
		topicId = "";
	}
	$("#topicId").val(topicId);
	document.getElementById("form1").action = "${path}/picmgmt/topic/topicEdit.action";
	document.getElementById("form1").submit();
}

function detail(topicId){

	art.dialog.open("${path}/picmgmt/topic/topicDetail.action?topicId=" + topicId, 
			{
				title: "专题详情", 
				width: 750,
				height: 450,
				lock: true

			});
}

function topicAudit(status) {
	var topicIds = $("input[name='topicIds']:checked");
	if(topicIds.length<=0){
		if (status == 1){
			art.dialog.alert("请选择需要提交审核的选项！");
		}else if (status == 2){
			art.dialog.alert("请选择需要审核通过的选项！");
		}else if (status == 3){
			art.dialog.alert("请选择需要审核不通过的选项！");
		}else if (status == 4){
			art.dialog.alert("请选择需要发布的选项！");
		}else if (status == 5){
			art.dialog.alert("请选择需要取消发布的选项！");
		}
		return false;
	}
	for (var i=0; i<topicIds.length; i++){
		var createdBy = $("#createdBy" + topicIds[i].value).val();
		if(activeOpNo != -1 && createdBy != activeOpNo){
			art.dialog.alert("无权操作分配的专题！");
			return false;
		}
		var oldStatus = $("#oldStatus" + topicIds[i].value).val();
		if (status == 1){
			if (oldStatus != 0 && oldStatus != 3){
				art.dialog.alert("提交审核的专题必须为【编辑】或【审核不通过】状态！");
				return false;
			}

		}else if (status == 2){
			if (oldStatus != 1 ){
				art.dialog.alert("审核通过的专题必须为【提交审核】状态！");
				return false;
			}
		}else if (status == 3){
			if (oldStatus != 1 && oldStatus != 2 && oldStatus != 5){
				art.dialog.alert("审核不通过的专题必须为【提交审核】或【审核通过】或【取消发布】状态！");
				return false;
			}
		}else if (status == 4){
			if (oldStatus != 2 && oldStatus != 5){
				art.dialog.alert("发布的专题必须为【审核通过】或【取消发布】状态！");
				return false;
			}
		}else if (status == 5){
			if (oldStatus != 4){
				art.dialog.alert("取消发布的专题必须为【发布】状态！");
				return false;
			}
		} 
	}
	
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: "${path}/picmgmt/topic/topicAudit.action?status=" + status,
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
					if (status == 1){
						art.dialog.alert("提交审核失败！",goBack);
					}else if (status == 2){
						art.dialog.alert("审核通过失败！",goBack);
					}else if (status == 3){
						art.dialog.alert("审核不通过失败！",goBack);
					}else if (status == 4){
						art.dialog.alert("发布失败！",goBack);
					}else if (status == 5){
						art.dialog.alert("取消发布失败！",goBack);
					}
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true"){
						if (status == 1){
							art.dialog.alert("提交审核成功！",goBack);
						}else if (status == 2){
							art.dialog.alert("审核通过成功！",goBack);
						}else if (status == 3){
							art.dialog.alert("审核不通过成功！",goBack);
						}else if (status == 4){
							art.dialog.alert("发布成功！",goBack);
						}else if (status == 5){
							art.dialog.alert("取消发布成功！",goBack);
						}

					}else{
						if (status == 1){
							art.dialog.alert("提交审核失败！",goBack);
						}else if (status == 2){
							art.dialog.alert("审核通过失败！",goBack);
						}else if (status == 3){
							art.dialog.alert("审核不通过失败！",goBack);
						}else if (status == 4){
							art.dialog.alert("发布失败！",goBack);
						}else if (status == 5){
							art.dialog.alert("取消发布失败！",goBack);
						}
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function albumMgmt(topicId,cmdStr){
	art.dialog.open("${path}/picmgmt/topic/topicAlbumSelect.action?topicId=" + topicId+"&cmdStr="+cmdStr, 
			{
				title: "相册管理", 
				width: 980,
				height: 450,
				lock: true

			});
}

function columnMgmt(topicId)
{
	if(topicId==null || typeof(topicId)=="undefined"){
		topicId = "";
	}
	document.getElementById("form2").action = "${path}/picmgmt/column/columnList.action?topicId=" + topicId;
	document.getElementById("form2").submit();
}

/* function topicPublish(){
	var sets = $("input[name='topicIds']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}
	
	for (var i=0; i<sets.length; i++){
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 2 && oldStatus != 5){
			art.dialog.alert("发布的专题必须为【审核通过】状态！");
			return false;
		}
	}
	
	var topicIds = "";
	for (var i=0; i < sets.length; i++){
		topicIds = topicIds + sets[i].value + ',';
	}
	
	topicIds = topicIds.substring(0,topicIds.length - 1);
	art.dialog.open("${path}/picmgmt/topic/toTopicPublish.action?topicIds=" + topicIds, 
			{
				title: "发布专题", 
				width: 350,
				height: 150,
				lock: true

			});
} */

/* function topicUnPublish(){
	var sets = $("input[name='topicIds']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要取消的选项！");
		return false;
	}
	
	for (var i=0; i<sets.length; i++){
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 4){
			art.dialog.alert("取消发布的专题必须为【发布】状态！");
			return false;
		}
	}
	
	var topicIds = "";
	for (var i=0; i < sets.length; i++){
		topicIds = topicIds + sets[i].value + ',';
	}
	
	topicIds = topicIds.substring(0,topicIds.length - 1);
	art.dialog.open("${path}/picmgmt/topic/toTopicUnPublish.action?topicIds=" + topicIds, 
			{
				title: "取消发布专题", 
				width: 350,
				height: 150,
				lock: true

			});
} */

function allocMgmt(resourceId,type,createOperNo){
	
	art.dialog.open("${path}/sysmgmt/user/resourceOperAllocList.action?resourceId=" + resourceId + "&type=" + type + "&createOperNo=" + createOperNo, 
			{
				title: "分配管理", 
				width: 980,
				height: 450,
				lock: true

			});
}

</script>
</head>
<body>
<div class="title">
  <h2>专题列表</h2>
</div>
  <form action="${path}/picmgmt/column/columnList.action" method="post" name="form2" id="form2">
</form>
<form action="${path}/picmgmt/topic/topicList.action" method="post" name="form1" id="form1">
<input type="hidden" name="topicId" id="topicId" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">专题名称：</td>
                  <td width="150px">
                     <input id="topicName" name="topicName" class="form130px" maxlength="30"  value="${search.topicName}" 
onMouseOver="toolTip('专题名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">类型：</td>
                  <td width="160">
                  <select name="type" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${topicTypeMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.type==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${topicStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.operatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
               </tr>
               <tr>
                  <td width="90px" height="30">截图标志：</td>
                  <td width="160">
                  <select name="captureFlag" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${captureFlagMap}" var="o" > 
							<option value="${o.key }" <c:if test="${search.captureFlag==o.key }"> selected="selected" </c:if>>${o.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${companyList}" var="u" > 
							<option value="${u.companyNo }" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"></td>
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
    <c:set var="add" value="false" />
    <c:set var="delete" value="false" />
    <c:set var="edit" value="false" />
    <c:set var="detail" value="false" />
    <c:set var="submit" value="false" />
    <c:set var="approved" value="false" />
    <c:set var="rejected" value="false" />
    <c:set var="publish" value="false" />
    <c:set var="unpublish" value="false" />
    <c:set var="album" value="false" />
    <c:set var="column" value="false" />
    <c:set var="alloc" value="false" />
    <c:set var="pubmgmt" value="false"/>
    <c:set var="preview" value="false"/>
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
			 <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
			 <c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'delete'}">   
			<c:set var="delete" value="true" />  
		</c:if> 
		<c:if test="${item eq 'edit'}">   
			<c:set var="edit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'detail'}">   
			<c:set var="detail" value="true" />  
		</c:if> 
		<c:if test="${item eq 'submit'}">   
			<c:set var="submit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'approved'}">   
			<c:set var="approved" value="true" />  
		</c:if> 
		<c:if test="${item eq 'rejected'}">   
			<c:set var="rejected" value="true" />  
		</c:if> 
		<c:if test="${item eq 'publish'}">   
			<c:set var="publish" value="true" />  
		</c:if> 
		<c:if test="${item eq 'unpublish'}">   
			<c:set var="unpublish" value="true" />  
		</c:if> 
		<c:if test="${item eq 'album'}">   
			<c:set var="album" value="true" />  
		</c:if> 
		<c:if test="${item eq 'column'}">   
			<c:set var="column" value="true" />  
		</c:if> 
		<c:if test="${item eq 'alloc'}">   
			<c:set var="alloc" value="true" />  
		</c:if> 
		<c:if test="${item eq 'pubmgmt'}">   
			<c:set var="pubmgmt" value="true" />  
		</c:if>
		<c:if test="${item eq 'preview'}">   
			<c:set var="preview" value="true" />  
		</c:if> 
	</c:forEach>
	<c:if test="${delete}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if> 
	<c:if test="${submit}">   
		    <input type="button" class="btn btn80" value="提交审核" onclick="topicAudit(1)">
	</c:if> 
	<c:if test="${approved}">   
		    <input type="button" class="btn btn80" value="审核通过" onclick="topicAudit(2)">
	</c:if> 
	<c:if test="${rejected}">   
		    <input type="button" class="btn btn80" value="审核不通过" onclick="topicAudit(3)">
	</c:if> 
	<c:if test="${publish}">   
		    <input type="button" class="btn btn80" value="发布" onclick="topicPublish('${path}/picmgmt/topic/toTopicPublish.action')">
	</c:if> 
	<c:if test="${unpublish}">   
		    <input type="button" class="btn btn80" value="取消发布" onclick="topicUnPublish('${path}/picmgmt/topic/toTopicUnPublish.action')">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'topicIds')"></th>
          <th width="*%">专题ID</th>
          <th width="*%">专题名称</th>
          <th width="*%">封面</th>
          <th width="*%">类型</th>
          <th width="*%">状态</th>
          <th width="*%">所属运营商</th>
          <th width="*%">创建用户</th>
          <th width="*%">创建时间</th>
          <th width="*%">更新时间</th>
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
	  <input type="hidden" name="activeOpNo" id="activeOpNo" value="" />
      	<c:forEach items="${list}" var="t">
      	       <input type="hidden" name="oldStatus${t.id}" id="oldStatus${t.id}" value="${t.status}" />
      	       <input type="hidden" name="templateId${t.id}" id="templateId${t.id}" value="${t.templateId}" />
      	       <input type="hidden" name="type${t.id}" id="type${t.id}" value="${t.type}" />
      	       <input type="hidden" name="cmds${t.id}" id="cmds${t.id}" value="${t.cmds}" />
      	       <input type="hidden" name="createdBy${t.id}" id="createdBy${t.id}" value="${t.createdBy}" />
        <tr>
          <td align="center"><input type="checkbox" name="topicIds" value="${t.id }">
          </td>
          <td align="center">
          	${t.topicId }
          </td>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(t.topicName) > 10}">
                 ${fn:substring(t.topicName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${t.topicName }
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:if test="${t.topicCover != null and t.topicCover != '' }">
	      		<img src="${path}/${t.topicCover}" height="50" onMouseOver="toolTip('<img src=${path}/${t.topicCover} style=\'width:200px; max-height:200px\' id=\'toolTipImg\' />')" onMouseOut="toolTip()"/>
	      	 </c:if>
          </td>
          <td align="center">
             <c:forEach items="${topicTypeMap}" var="m" > 
					<c:if test="${t.type==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             <c:forEach items="${topicStatusMap}" var="m" > 
					<c:if test="${t.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
           ${t.company.companyName}
          </td>
          <td align="center">
             ${t.operator.operatorName}
          </td>
          <td align="center">${t.createTime}</td>
          <td align="center">${t.updateTime}</td>
          <td class="tdOpera2" align="center">
             <c:if test="${detail}"><a href="javascript:;" onclick="detail('${t.id }')">详情</a></c:if>
             <c:if test="${edit and ((activeOperator.superAdmin) or (activeOperator.type==2 and activeOperator.createBy == t.createdBy) or (activeOperator.type!=2 and activeOperator.operatorNo==t.createdBy))}"><a href="javascript:;" onclick="toEditPage('${t.id }')">编辑</a></c:if>
             <c:if test="${delete and ((activeOperator.superAdmin) or (activeOperator.type==2 and activeOperator.createBy == t.createdBy) or (activeOperator.type!=2 and activeOperator.operatorNo==t.createdBy))}"> <a href="javascript:;" onclick="deleteSets('${t.id }')">删除</a></c:if>
              <c:if test="${preview and t.templateId != null}"> <a href="javascript:;" onclick="preview('${path}','${t.id }',1)">预览</a></c:if>
             <c:if test="${album}"> <a href="javascript:;" onclick="albumMgmt('${t.id }','${t.cmds}')">相册管理</a></c:if>
             <c:if test="${column}"> <a href="javascript:;" onclick="columnMgmt('${t.id}')">栏目管理</a></c:if>
             <c:if test="${alloc && activeOperator.type == 0}"> <a href="javascript:;" onclick="allocMgmt('${t.id}','1','${t.operatorNo }')">分配管理</a></c:if>
             
             <%-- <c:if test="${pubmgmt && t.status==4}"> <a href="javascript:;" onclick="pubMgmt('${t.id}','${path}/picmgmt/topic/publishMgmt.action')">发布管理</a></c:if> --%>
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  </form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
    <c:if test="${add}">
      <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
    </c:if>
    <c:if test="${delete}">
      <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
    </c:if>
	<c:if test="${submit}">   
		    <input type="button" class="btn btn80" value="提交审核" onclick="topicAudit(1)">
	</c:if> 
	<c:if test="${approved}">   
		    <input type="button" class="btn btn80" value="审核通过" onclick="topicAudit(2)">
	</c:if> 
	<c:if test="${rejected}">   
		    <input type="button" class="btn btn80" value="审核不通过" onclick="topicAudit(3)">
	</c:if> 
	<c:if test="${publish}">   
		    <input type="button" class="btn btn80" value="发布" onclick="topicPublish('${path}/picmgmt/topic/toTopicPublish.action')">
	</c:if> 
	<c:if test="${unpublish}">   
		    <input type="button" class="btn btn80" value="取消发布" onclick="topicUnPublish('${path}/picmgmt/topic/toTopicUnPublish.action')">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
