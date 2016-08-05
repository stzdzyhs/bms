<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图片列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>
<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
<script src="${path}/js/picmgmt/picture/picture.js"></script>
<script type="text/javascript">
var activeOpNo;
$(document).ready(function(){
	$("#picName").keyup(function(){
		var picName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (picName != null && picName != '' && !resNameReg.test(picName)){		
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
});
//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='pictureIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		for (var i=0; i<sets.length; i++){
			if(activeOpNo != -1){
				var createdBy = $("#createdBy" + sets[i].value).val();
				if(activeOpNo != createdBy){
					art.dialog.alert("无权删除分配相册下的图片！");
					return false;
				}
			}
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
				art.dialog.alert("不能直接删除该状态的图片！");
				return false;
			}
		}
	} else {
		if(activeOpNo != -1){
			var createdBy = $("#createdBy" + id).val();
			if(activeOpNo != createdBy){
				art.dialog.alert("无权删除分配相册下的图片！");
				return false;
			}
		}
		var aa = document.getElementsByName("pictureIds");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
			art.dialog.alert("不能直接删除该状态的图片！");
			return false;
		}
	}

	var param = id != '' ? "?pictureIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/picmgmt/picture/pictureDelete.action"+param,
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
	document.getElementById("form1").action = "${path}/picmgmt/picture/pictureList.action";
	document.getElementById("form1").submit();
}
//进入新增编辑页面
function toEditPage(pictureId){
	var oldStatus = $("#oldStatus" + pictureId).val();
	if (typeof(pictureId)!="undefined" && oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
		art.dialog.alert("该状态的图片，不能编辑！");
		return false;
	}else{
		var albumCreatedBy = $("#albumCreatedBy").val();
		if(activeOpNo != -1 && activeOpNo != albumCreatedBy){
			var cmds = "${cmdStr}";
			if(cmds == null || typeof(cmds)=="undefined" || cmds.indexOf("3")<0){
				art.dialog.alert("无权为分配的相册添加图片！");
				return false;
			}
		}
	}
	$("#pictureId").val(pictureId);
	document.getElementById("form1").action = "${path}/picmgmt/picture/pictureEdit.action";
	document.getElementById("form1").submit();
}

function detail(pictureId){

	art.dialog.open("${path}/picmgmt/picture/pictureDetail.action?pictureId=" + pictureId, 
			{
				title: "图片详情", 
				width: 750,
				height: 450,
				lock: true

			});
}

function pictureAudit(status) {
	var pictureIds = $("input[name='pictureIds']:checked");
	if(pictureIds.length<=0){
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
	var cmds = "${cmdStr}";
	for (var i=0; i<pictureIds.length; i++){
		var createdBy = $("#createdBy"+pictureIds[i].value).val();
		if(activeOpNo != -1 && activeOpNo != createdBy){
			art.dialog.alert("无权对分配相册下的图片进行操作！");
			return false;
		}
		var oldStatus = $("#oldStatus" + pictureIds[i].value).val();
		if (status == 1){
			if (oldStatus != 0 && oldStatus != 3){
				art.dialog.alert("提交审核的图片必须为【编辑】或【审核不通过】状态！");
				return false;
			}

		}else if (status == 2){
			if (oldStatus != 1 ){
				art.dialog.alert("审核通过的图片必须为【提交审核】状态！");
				return false;
			}
		}else if (status == 3){
			if (oldStatus != 1 && oldStatus != 2 && oldStatus != 5){
				art.dialog.alert("审核不通过的图片必须为【提交审核】或【审核通过】或【取消发布】状态！");
				return false;
			}
		}else if (status == 4){
			if (oldStatus != 2 && oldStatus != 5){
				art.dialog.alert("发布的图片必须为【审核通过】或【取消发布】状态！");
				return false;
			}
			
			var albumStatus = '${album.status}';
			if (albumStatus != 4){
				art.dialog.alert("只有发布相册之后，才能发布相册的图片！");
				return false;
			}
		}else if (status == 5){
			if (oldStatus != 4){
				art.dialog.alert("取消发布的图片必须为【发布】状态！");
				return false;
			}
		} 
	}
	
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: "${path}/picmgmt/picture/pictureAudit.action?status=" + status,
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

function picturePublish(url){
	var sets = $("input[name='pictureIds']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}
	var cmds = "${cmdStr}";
	for (var i=0; i<sets.length; i++){
		var createdBy = $("#createdBy"+sets[i].value).val();
		if(activeOpNo != -1 && activeOpNo != createdBy){
			art.dialog.alert("无权对分配相册下的图片进行操作！");
			return false;
		}
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 2){
			art.dialog.alert("发布的图片必须为【审核通过】状态！");
			return false;
		}
	}
	
	var pictureIds = "";
	for (var i=0; i < sets.length; i++){
		pictureIds = pictureIds + sets[i].value + ',';
	}
	
	pictureIds = pictureIds.substring(0,pictureIds.length - 1);
	art.dialog.open(url + "?pictureIds=" + pictureIds, 
			{
				title: "发布图片", 
				width: 600,
				height: 400,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"确定",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.publish();
					return false;
				},
				cancel:function(){
					return true;
				}

			});
}

function batchUpload(){
	var albumCreatedBy = $("#albumCreatedBy").val();
	if(activeOpNo != -1 && activeOpNo != albumCreatedBy){
		var cmds = "${cmdStr}";
		if(cmds == null || typeof(cmds)=="undefined" || cmds.indexOf("3")<0){
			art.dialog.alert("无权为分配的相册添加图片！");
			return false;
		}
	}
	var albumNo = $("#albumNo").val();
	art.dialog.open("${path}/picmgmt/picture/toBatchUpload.action?albumNo=" + albumNo, 
			{
				title: "批量上传图片", 
				width: 500,
				height: 300,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				}

			});
}

</script>
</head>
<body>
<div class="title">
  <h2>图片列表</h2>
</div>
<form action="${path}/picmgmt/picture/pictureList.action" method="post" name="form1" id="form1">
<input type="hidden" name="albumNo" id="albumNo" value="${search.albumNo}" />
<input type="hidden" name="pictureId" id="pictureId" value="" />
<input type="hidden" name="albumCreatedBy" id="albumCreatedBy" value="${albumCreatedBy}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">图片名称：</td>
                  <td width="150px">
                     <input id="picName" name="picName" maxlength="30"  class="form130px" value="${search.picName}"  
                     onMouseOver="toolTip('图片名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${pictureStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                   <c:if test="${activeOperator.type != 2}">
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.operatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  </c:if>
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
    <c:set var="batch" value="false" />
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
		<c:if test="${item eq 'batch'}">   
			<c:set var="batch" value="true" />  
		</c:if> 
	</c:forEach>
	<c:if test="${delete}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if> 
	<c:if test="${submit}">   
		    <input type="button" class="btn btn80" value="提交审核" onclick="pictureAudit(1)">
	</c:if> 
	<c:if test="${approved}">   
		    <input type="button" class="btn btn80" value="审核通过" onclick="pictureAudit(2)">
	</c:if> 
	<c:if test="${rejected}">   
		    <input type="button" class="btn btn80" value="审核不通过" onclick="pictureAudit(3)">
	</c:if> 
	<c:if test="${publish}">   
		    <input type="button" class="btn btn80" value="发布" onclick="picturePublish('${path}/picmgmt/picture/toPicturePublish.action')">
	</c:if> 
	<c:if test="${unpublish}">   
		    <input type="button" class="btn btn80" value="取消发布" onclick="pictureAudit(5)">
	</c:if> 
	<c:if test="${batch}">   
		    <input type="button" class="btn btn80" value="批量上传" onclick="batchUpload()"/>
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'pictureIds')"></th>
          <th width="*%">图片ID</th>
          <c:if test="${album.captureFlag == 1}">
          <th width="*%" id="frameNum" class="sortRow">视频帧数</th>
          </c:if>
          <th width="*%" <c:if test="${album.captureFlag == 0}">id="picNameKey" class="sortRow"</c:if>>图片名称</th>
          <th width="*%">图片文件</th>
          <th width="*%">状态</th>
          <th width="*%">参与投票</th>
          <th width="*%">创建用户</th>
          <th width="*%" <c:if test="${album.captureFlag == 0}">id="createTime" class="sortRow"</c:if>>创建时间</th>
          <th width="*%">更新时间</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="9">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      	<c:forEach items="${list}" var="p">
      	 <input type="hidden" name="oldStatus${p.id}" id="oldStatus${p.id}" value="${p.status}" />
      	 <input type="hidden" name="createdBy${p.id}" id="createdBy${p.id}" value="${p.createdBy}" />
        <tr>
          <td align="center"><input type="checkbox" name="pictureIds" value="${p.id}">
          </td>
          <td align="center">
          	${p.pictureId}
          </td>
          <c:if test="${album.captureFlag == 1}">
          <td align="center">
          ${p.frameNum}
          </td>
          </c:if>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(p.picName) > 10}">
                 ${fn:substring(p.picName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${p.picName}
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:if test="${p.picPath != null and p.picPath != '' }">
	      		<img src="${path}/${p.picPath}" height="50" onMouseOver="toolTip('<img src=${path}/${p.picPath} style=\'width:200px; max-height:200px\' id=\'toolTipImg\' />')" onMouseOut="toolTip()"/>
	      	 </c:if>
          </td>
          <td align="center">
             <c:forEach items="${pictureStatusMap}" var="m" > 
					<c:if test="${p.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
                        <c:forEach items="${pictureVoteFlagMap}" var="m" > 
					<c:if test="${p.voteFlag==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             ${p.operator.operatorName}
          </td>
          <td align="center">${p.createTime}</td>
          <td align="center">${p.updateTime}</td>
          <td class="tdOpera2" align="center">
             <c:if test="${detail}"><a href="javascript:;" onclick="detail('${p.id}')">详情</a></c:if>
             <c:if test="${edit and ((activeOperator.superAdmin)or (activeOperator.type!=2 and activeOperator.operatorNo==p.createdBy) or (activeOperator.type==2 and activeOperator.createBy == p.createdBy) )}"><a href="javascript:;" onclick="toEditPage('${p.id}')">编辑</a></c:if>
             <c:if test="${delete and ((activeOperator.superAdmin)or (activeOperator.type!=2 and activeOperator.operatorNo==p.createdBy) or (activeOperator.type==2 and activeOperator.createBy == p.createdBy) )}"> <a href="javascript:;" onclick="deleteSets('${p.id}')">删除</a></c:if>
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
   <jsp:include page="/common/sortRow.jsp" />
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
		    <input type="button" class="btn btn80" value="提交审核" onclick="pictureAudit(1)">
	</c:if> 
	<c:if test="${approved}">   
		    <input type="button" class="btn btn80" value="审核通过" onclick="pictureAudit(2)">
	</c:if> 
	<c:if test="${rejected}">   
		    <input type="button" class="btn btn80" value="审核不通过" onclick="pictureAudit(3)">
	</c:if> 
	<c:if test="${publish}">   
		     <input type="button" class="btn btn80" value="发布" onclick="picturePublish('${path}/picmgmt/picture/toPicturePublish.action')">
	</c:if> 
	<c:if test="${unpublish}">   
		    <input type="button" class="btn btn80" value="取消发布" onclick="pictureAudit(5)">
	</c:if> 
	<c:if test="${batch}">   
		    <input type="button" class="btn btn80" value="批量上传" onclick="batchUpload()"/>
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
