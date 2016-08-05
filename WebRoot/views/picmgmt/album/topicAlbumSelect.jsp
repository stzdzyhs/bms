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

<script src="${path}/js/checkbox.js"></script>
 <script src="${path}/js/win/win.js" type="text/javascript"></script>
 
 <script src="${path}/js/picmgmt/topic/topic.js"></script>
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
	var type = ${activeOperator.type};
	var activeOpNo = ${activeOperator.operatorNo};
	if(type == 2){
		activeOpNo = ${activeOperator.createBy};
	}
	if (id == '') {
		var sets = $("input[name='albumNos']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		if(activeOpNo != -1){
			for (var i=0; i<sets.length; i++){
				var createdBy = $("#createdBy" + sets[i].value).val();
				if(activeOpNo != createdBy){
					art.dialog.alert("无权删除分配专题的相册关联！");
					return false;
				}
			}
		}
	} else {
		if(activeOpNo != -1){
			var createdBy = $("#createdBy" + id).val();
			if(activeOpNo != createdBy){
				art.dialog.alert("无权删除分配专题的相册关联！");
				return false;
			}
		}
		var aa = document.getElementsByName("albumNos");
		for (var i=0; i<aa.length; i++)
			aa[i].checked = false;
	}
	
	var param = id != '' ? "?albumNos="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/picmgmt/topic/topicAlbumDelete.action" + param,
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
						if (rsobj.authText != null && rsobj.authText != ""){
							art.dialog.alert(rsobj.authText);
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
	document.getElementById("form1").action = "${path}/picmgmt/topic/topicAlbumSelect.action";
	document.getElementById("form1").submit();
}


function detail(albumNo){

	art.dialog.open("${path}/picmgmt/album/albumDetail.action?albumNo=" + albumNo+"&topicId="+"${topicId}", 
			{
				title: "相册详情", 
				width: 750,
				height: 450,
				lock: true

			});
}

function topicAlbumNoSelect(url){
	var type = ${activeOperator.type};
	var activeOpNo = ${activeOperator.operatorNo};
	if(type == 2){
		activeOpNo = ${activeOperator.createBy};
	}
	var topicCreatedBy = $("#topicCreatedBy").val();
	if(activeOpNo != -1 && activeOpNo != topicCreatedBy){
		var cmds = "${cmdStr}";
		if(cmds == null || typeof(cmds)=="undefined" || cmds.indexOf("1")<0){
			art.dialog.alert("无权为分配的专题添加相册关联！");
			return false;
		}
	}
	var topicId = $("#topicId").val();
	art.dialog.open(url + "?topicId=" + topicId, 
			{
				title: "相册管理", 
				width: 980,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.saveAlbum();
					return false;
				},
				cancel:function(){
					return true;
				}

			});
}

</script>
</head>
<body>
<div class="title">
  <h2>已选择相册列表</h2>
</div>
<form action="${path}/picmgmt/topic/topicAlbumSelect.action" method="post" name="form1" id="form1">
<input type="hidden" name="topicId" id="topicId" value="${topicId}" />
<input type="hidden" name="topicCreatedBy" id="topicCreatedBy" value="${topicCreatedBy}" />
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
                  <!-- 
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${albumStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                   -->
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
    <input class="btn btn80" type="button" value="新增关联" onclick="topicAlbumNoSelect('${path}/picmgmt/topic/topicAlbumNoSelect.action')">
    <input type="button" class="btn btn80" value="删除关联" onclick="deleteSets('')">
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
      	<input type="hidden" name="isSelf${a.albumNo}" id="isSelf${a.albumNo}" value="${a.isSelf}" />
      	 <input type="hidden" name="oldStatus${a.albumNo}" id="oldStatus${a.albumNo}" value="${a.status}" />
      	 <input type="hidden" name="createdBy${a.albumNo}" id="createdBy${a.albumNo}" value="${a.resAlbumMap.createdBy}" />
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
             <a href="javascript:;" onclick="detail('${a.albumNo}')">详情</a>
             <c:if test="${(activeOperator.superAdmin) or(activeOperator.type==2 and activeOperator.createBy == a.resAlbumMap.createdBy) or (activeOperator.type!=2 and activeOperator.operatorNo==a.resAlbumMap.createdBy)}">
             	<a href="javascript:;" onclick="deleteSets('${a.albumNo}')">删除</a>
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
    <input class="btn btn80" type="button" value="新增关联" onclick="topicAlbumNoSelect('${path}/picmgmt/topic/topicAlbumNoSelect.action')">
    <input type="button" class="btn btn80" value="删除关联" onclick="deleteSets('')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
