<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>相册列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>
<script src="${path}/js/const.js"></script>
<script src="${path}/js/common.js"></script>
<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
<script src="${path}/js/picmgmt/album/album.js"></script>
<script src="${path}/js/picmgmt/publish/publish.js"></script>
<script src="${path}/js/opermgmt/template/template.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#albumName").keyup(function(){
		var albumName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (albumName != null && albumName != '' && !resNameReg.test(albumName)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});
//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='albumNos']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		
		for (var i=0; i<sets.length; i++){
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
				art.dialog.alert("不能直接删除该状态的相册！");
				return false;
			}
			var allocRes = $("#allocRes" + sets[i].value).val();
			if(allocRes == 1){
				art.dialog.alert("无权操作分配的相册！");
				return false;
			}
		}
	} else {
		var aa = document.getElementsByName("albumNos");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
			art.dialog.alert("不能直接删除该状态的相册！");
			return false;
		}
		var allocRes = $("#allocRes" + id).val();
		if(allocRes == 1){
			art.dialog.alert("无权操作分配的相册！");
			return false;
		}
	}

	var param = id != '' ? "?albumNos="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/picmgmt/album/albumDelete.action"+param,
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
						if (rsobj.desc == 'topic'){
							art.dialog.alert("相册已经被专题使用，不能删除！");
						}else{
							art.dialog.alert("删除失败！");
						}

					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/picmgmt/album/albumList.action";
	document.getElementById("form1").submit();
}
//进入新增编辑页面
function toEditPage(albumNo){
	var oldStatus = $("#oldStatus" + albumNo).val();
	if (typeof(albumNo)!='undefined' && oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
		art.dialog.alert("该状态的相册，不能编辑！");
		return false;
	}
	
	if(albumNo==null || typeof(albumNo)=="undefined"){
		albumNo = "";
	}
	$("#albumNo").val(albumNo);
	document.getElementById("form1").action = "${path}/picmgmt/album/albumEdit.action";
	document.getElementById("form1").submit();
}

function detail(albumNo){

	art.dialog.open("${path}/picmgmt/album/albumDetail.action?albumNo=" + albumNo, 
			{
				title: "相册详情", 
				width: 750,
				height: 450,
				lock: true

			});
}

function albumAudit(status) {
	var albumNos = $("input[name='albumNos']:checked");
	if(albumNos.length<=0){
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
	
	for (var i=0; i<albumNos.length; i++){
		var allocRes = $("#allocRes" + albumNos[i].value).val();
		if(allocRes == 1){
			art.dialog.alert("无权操作分配的相册！");
			return false;
		}
		var oldStatus = $("#oldStatus" + albumNos[i].value).val();
		if (status == 1){
			if (oldStatus != 0 && oldStatus != 3){
				art.dialog.alert("提交审核的相册必须为【编辑】或【审核不通过】状态！");
				return false;
			}

		}else if (status == 2){
			if (oldStatus != 1 ){
				art.dialog.alert("审核通过的相册必须为【提交审核】状态！");
				return false;
			}
		}else if (status == 3){
			if (oldStatus != 1 && oldStatus != 2 && oldStatus != 5){
				art.dialog.alert("审核不通过的相册必须为【提交审核】或【审核通过】或【取消发布】状态！");
				return false;
			}
		}else if (status == 4){
			if (oldStatus != 2 && oldStatus != 5){
				art.dialog.alert("发布的相册必须为【审核通过】或【取消发布】状态！");
				return false;
			}
		}else if (status == 5){
			if (oldStatus != 4){
				art.dialog.alert("取消发布的相册必须为【发布】状态！");
				return false;
			}
		} 
	}
	
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: "${path}/picmgmt/album/albumAudit.action?status=" + status,
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

function picMgmt(albumNo,cmdStr)
{
	if(albumNo==null || typeof(albumNo)=="undefined"){
		albumNo = "";
	}
	document.getElementById("form2").action = "${path}/picmgmt/picture/pictureList.action?albumNo=" + albumNo+"&cmdStr="+cmdStr;
	document.getElementById("form2").submit();
}

/* function albumPublish(){
	var sets = $("input[name='albumNos']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}
	
	for (var i=0; i < sets.length; i++){
		var albumNo = sets[i].value;
		var isSelf = $("#isSelf" + albumNo).val();
		if (isSelf == 0){
			art.dialog.alert("您没有发布该资源的权限！");
			return false;
		}
	}
	
	for (var i=0; i<sets.length; i++){
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 2 && oldStatus != 5){
			art.dialog.alert("发布的相册必须为【审核通过】状态！");
			return false;
		}
	}
	
	var albumNos = "";
	for (var i=0; i < sets.length; i++){
		albumNos = albumNos + sets[i].value + ',';
	}
	
	albumNos = albumNos.substring(0,albumNos.length - 1);
	art.dialog.open("${path}/picmgmt/album/toAlbumPublish.action?albumNos=" + albumNos, 
			{
				title: "发布相册", 
				width: 450,
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
  <h2>相册列表</h2>
</div>
    <form action="${path}/picmgmt/album/albumList.action" method="post" name="form2" id="form2">
</form>
<form action="${path}/picmgmt/album/albumList.action" method="post" name="form1" id="form1">
<input type="hidden" name="albumNo" id="albumNo" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">相册名称：</td>
                  <td width="150px">
                     <input id="albumName" name="albumName" class="form130px" maxlength="30" value="${search.albumName}" 
                     onMouseOver="toolTip('相册名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${albumStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">截图标志：</td>
                  <td width="160">
                  <select name="captureFlag" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${captureFlagMap}" var="o" > 
							<option value="${o.key }" <c:if test="${search.captureFlag==o.key }"> selected="selected" </c:if>>${o.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>

                 </tr>
                 <tr>

                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${companyList}" var="u" > 
							<option value="${u.companyNo }" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                                    <c:if test="${activeOperator.type != 2 }">
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
    <c:set var="pic" value="false" />
    <c:set var="alloc" value="false" />
    <c:set var="pubmgmt" value="false" />
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
		<c:if test="${item eq 'pic'}">   
			<c:set var="pic" value="true" />  
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
		    <input type="button" class="btn btn80" value="提交审核" onclick="albumAudit(1)">
	</c:if> 
	<c:if test="${approved}">   
		    <input type="button" class="btn btn80" value="审核通过" onclick="albumAudit(2)">
	</c:if> 
	<c:if test="${rejected}">   
		    <input type="button" class="btn btn80" value="审核不通过" onclick="albumAudit(3)">
	</c:if> 
	<!-- 
	<c:if test="${publish}">   
		    <input type="button" class="btn btn80" value="发布" onclick="albumPublish('${path}')">
	</c:if>
	 --> 
<%-- 	<c:if test="${unpublish}">   
		    <input type="button" class="btn btn80" value="取消发布" onclick="albumAudit(5)">
	</c:if>  --%>
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'albumNos')"></th>
          <th width="*%">相册ID</th>
          <th width="*%" id="albumNameKey" class="sortRow">相册名称</th>
          <th width="*%">相册封面</th>
          <th width="*%">状态</th>
          <th width="*%">所属运营商</th>
          <th width="*%">创建用户</th>
          <th width="*%" id="createTime" class="sortRow">创建时间</th>
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
      	<c:forEach items="${list}" var="a">
    	 	<input type="hidden" name="oldStatus${a.albumNo}" id="oldStatus${a.albumNo}" value="${a.status}" />
    	 	<input type="hidden" name="captureFlag${a.albumNo}" id="captureFlag${a.albumNo}" value="${a.captureFlag}" />
    	 	<input type="hidden" name="templateId${a.albumNo}" id="templateId${a.albumNo}" value="${a.templateId}" />
    	 	<input type="hidden" name="cmds${a.albumNo}" id="cmds${a.albumNo}" value="${a.cmds}" />
    	 	<input type="hidden" name="allocRes${a.albumNo}" id="allocRes${a.albumNo}" value="${a.allocRes}" />
        <tr>
          <td align="center"><input type="checkbox" name="albumNos" value="${a.albumNo}">
          </td>
         <td align="center">
          ${a.albumId}
          </td>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(a.albumName) > 10}">
                 ${fn:substring(a.albumName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${a.albumName}
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:if test="${a.albumCover != null and a.albumCover != '' }">
	      		<img src="${path}/${a.albumCover}" height="50" onMouseOver="toolTip('<img src=${path}/${a.albumCover} style=\'width:200px; max-height:200px\' id=\'toolTipImg\' />')" onMouseOut="toolTip()"/>
	      	 </c:if>
          </td>
          <td align="center">
             <c:forEach items="${albumStatusMap}" var="m" > 
					<c:if test="${a.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
           ${a.company.companyName}
          </td>
          <td align="center">
             ${a.operator.operatorName}
          </td>
          <td align="center">${a.createTime}</td>
          <td align="center">${a.updateTime}</td>
          <td class="tdOpera2" align="center">
             <c:if test="${detail}"><a href="javascript:;" onclick="detail('${a.albumNo}')">详情</a></c:if>
             <c:if test="${edit and (a.allocRes == null or a.allocRes == '')}"><a href="javascript:;" onclick="toEditPage('${a.albumNo}')">编辑</a></c:if>
             <c:if test="${delete and (a.allocRes == null or a.allocRes == '')}"> <a href="javascript:;" onclick="deleteSets('${a.albumNo}')">删除</a></c:if>
             <c:if test="${preview and a.templateId != null}">  <a href="javascript:;" onclick="preview('${path}','${a.albumNo }',2)">预览</a></c:if>
             <c:if test="${pic}"> <a href="javascript:;" onclick="picMgmt('${a.albumNo}','${a.cmds}')">图片管理</a></c:if>
             <c:if test="${alloc && activeOperator.superAdmin}">
              	 <a href="javascript:;" onclick="allocMgmt('${a.albumNo}','2','${a.operatorNo }')">分配管理</a>
			 </c:if>
           	 <c:if test="${pubmgmt && a.status>=2 && a.status!=3 && a.captureFlag == 0}"> 
           		<a href="javascript:;" onclick="pubMgmt(ENTITY_TYPE_ALBUM,'${a.albumNo}','${path}/picmgmt/publish/pubMgmt.action')">发布管理</a>
            </c:if> 
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
		    <input type="button" class="btn btn80" value="提交审核" onclick="albumAudit(1)">
	</c:if> 
	<c:if test="${approved}">   
		    <input type="button" class="btn btn80" value="审核通过" onclick="albumAudit(2)">
	</c:if> 
	<c:if test="${rejected}">   
		    <input type="button" class="btn btn80" value="审核不通过" onclick="albumAudit(3)">
	</c:if> 
	<!-- 
	<c:if test="${publish}">   
		    <input type="button" class="btn btn80" value="发布" onclick="albumPublish('${path}')">
	</c:if>
	 --> 
<%-- 	<c:if test="${unpublish}">   
		    <input type="button" class="btn btn80" value="取消发布" onclick="albumAudit(5)">
	</c:if>  --%>
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>

