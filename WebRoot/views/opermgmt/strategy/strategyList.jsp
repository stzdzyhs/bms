<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>投放策略列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/common.js"></script>
<script src="${path}/js/opermgmt/strategy/strategy.js"></script>
<script type="text/javascript">
//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='strategyNos']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		
		for (var i=0; i < sets.length; i++){
			var strategyNo = sets[i].value;
			var isSelf = $("#isSelf" + strategyNo).val();
			if (isSelf == 0){
				art.dialog.alert("您没有删除该资源的权限！");
				return false;
			}
			var oldAuditStatus = $("#auditStatus" + strategyNo).val();
			if(oldAuditStatus !=0 && oldAuditStatus != 3){
				art.dialog.alert("该状态的策略不能删除");
				return false;
			}
		}
	} else {
		var aa = document.getElementsByName("strategyNos");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
			var strategyNo = aa[i].value;
		}
		var oldAuditStatus = $("#auditStatus" + id).val();
		if(oldAuditStatus !=0 && oldAuditStatus != 3){
			art.dialog.alert("该状态的策略不能删除");
			return false;
		}
	}
	
	var param = id != '' ? "?strategyNos="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/opermgmt/strategy/deleteStrategy.action" + param,
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
						if (rsobj.desc == 'schedule'){
							art.dialog.alert('策略已经被排期引用，不能删除！');
						}else{
							art.dialog.alert("删除失败！"+rsobj.desc);
						}
					}
				}
			};
			jQuery('#strategyForm').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("strategyForm").action = "${path}/opermgmt/strategy/strategyList.action";
	document.getElementById("strategyForm").submit();
}

function toEditPage(strategyNo){
	var oldAuditStatus = $("#auditStatus" + strategyNo).val();
	if(typeof(strategyNo)!='undefined' && oldAuditStatus !=0 && oldAuditStatus != 3){
		art.dialog.alert("该状态的策略不能编辑");
		return false;
	}
	if (strategyNo!=null && typeof(strategyNo)!="undefined"){
		$("#strategyNo").val(strategyNo);
		document.getElementById("strategyForm").action = "${path}/opermgmt/strategy/strategyEdit2.action?strategyNo="+strategyNo;
		document.getElementById("strategyForm").submit();
		jQuery('#strategyForm').ajaxSubmit(options);
	}else{
		$("#strategyNo").val(strategyNo);
		document.getElementById("strategyForm").action = "${path}/opermgmt/strategy/strategyEdit2.action";
		document.getElementById("strategyForm").submit();
	}
}

function detail(strategyNo) {
	art.dialog.open("${path}/opermgmt/strategy/strategyDetail.action?strategyNo=" + strategyNo, 
	{
		title: "策略详情",
		width: 750,
		height: 600,
		lock: true
	});
}

function strategyAudit(auditStatus) {
	var strategyNos = $("input[name='strategyNos']:checked");
	for (var i=0; i < strategyNos.length; i++){
		var strategyNo = strategyNos[i].value;
		var isSelf = $("#isSelf" + strategyNo).val();
		if (isSelf == 0){
			if (auditStatus == 1){
				art.dialog.alert("您没有提交审核该资源的权限！");
			}else if (auditStatus == 2){
				art.dialog.alert("您没有审核该资源的权限！");
			}else if (auditStatus == 3){
				art.dialog.alert("您没有审核该资源的权限！");
			}else if (auditStatus == 4){
				art.dialog.alert("您没有启用该资源的权限！");
			}else if (auditStatus == 5){
				art.dialog.alert("您没有禁用该资源的权限！");
			}else {
				art.dialog.alert("您没有操作该资源的权限！");
			}
			return false;
		}
	}
	
	if(strategyNos.length<=0){
		if (auditStatus == 1){
			art.dialog.alert("请选择需要提交审核的选项！");
		}else if (auditStatus == 2){
			art.dialog.alert("请选择需要审核通过的选项！");
		}else if (auditStatus == 3){
			art.dialog.alert("请选择需要审核不通过的选项！");
		}else if (auditStatus == 4){
			art.dialog.alert("请选择需要启用的选项！");
		}else if (auditStatus == 5){
			art.dialog.alert("请选择需要禁用的选项！");
		}
		return false;
	}
	
	for (var i=0; i<strategyNos.length; i++){
		var oldAuditStatus = $("#auditStatus" + strategyNos[i].value).val();
		if (auditStatus == 1){
			if (oldAuditStatus != 0 && oldAuditStatus != 3 ){
				art.dialog.alert("提交审核的投放策略必须为【草稿】或【审核不通过】！");
				return false;
			}

		}else if (auditStatus == 2){
			if (oldAuditStatus != 1 ){
				art.dialog.alert("审核通过的投放策略必须为【正在审核】状态！");
				return false;
			}
		}else if (auditStatus == 3){
			if (oldAuditStatus != 1 && oldAuditStatus != 2 && oldAuditStatus != 5){
				art.dialog.alert("审核不通过的投放策略必须为【正在审核】或【审核通过】或【禁用】状态！");
				return false;
			}
		}else if (auditStatus == 4){
			if (oldAuditStatus != 2 && oldAuditStatus != 5){
				art.dialog.alert("启用的投放策略必须为【审核通过】或【禁用】状态！");
				return false;
			}
		}/* else if (auditStatus == 5){
			if (oldAuditStatus != 4){
				art.dialog.alert("禁用的投放策略必须为【启用】状态！");
				return false;
			}
		} */
	}
	
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: "${path}/opermgmt/strategy/strategyUpdateAuditStatus.action?auditStatus=" + auditStatus,
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
					if (auditStatus == 1){
						art.dialog.alert("提交审核失败！",goBack);
					}else if (auditStatus == 2){
						art.dialog.alert("审核通过失败！",goBack);
					}else if (auditStatus == 3){
						art.dialog.alert("审核不通过失败！",goBack);
					}else if (auditStatus == 4){
						art.dialog.alert("启用失败！",goBack);
					}else if (auditStatus == 5){
						art.dialog.alert("禁用失败！",goBack);
					}
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true"){
						if (auditStatus == 1){
							art.dialog.alert("提交审核成功！",goBack);
						}else if (auditStatus == 2){
							art.dialog.alert("审核通过成功！",goBack);
						}else if (auditStatus == 3){
							art.dialog.alert("审核不通过成功！",goBack);
						}else if (auditStatus == 4){
							art.dialog.alert("启用成功！",goBack);
						}else if (auditStatus == 5){
							art.dialog.alert("禁用成功！",goBack);
						}

					}else{
						if (auditStatus == 1){
							art.dialog.alert("提交审核失败！",goBack);
						}else if (auditStatus == 2){
							art.dialog.alert("审核通过失败！",goBack);
						}else if (auditStatus == 3){
							art.dialog.alert("审核不通过失败！",goBack);
						}else if (auditStatus == 4){
							art.dialog.alert("启用失败！",goBack);
						}else if (auditStatus == 5){
							art.dialog.alert("禁用失败！",goBack);
						}
					}
				}
			};
			jQuery('#strategyForm').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

</script>
</head>
<body>
<div class="title">
  <h2>投放策略列表</h2>
</div>
<form action="${path}/opermgmt/strategy/strategyList.action" method="post" name="strategyForm" id="strategyForm">
<input type="hidden" name="strategyNo" id="strategyNo" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">策略名称：</td>
                  <td width="160">
                  	<input name="strategyName" class="form120px" value="${search.strategyName}" maxlength="50"/>
                  </td>
                  <c:if test="${activeOperator.type != 0 }">
                   <td width="90px" height="30">审核状态：</td>
                  <td width="160">
                  <select name="auditStatus" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${auditStatusMap}" var="s" > 
							<option value="${s.key}" <c:if test="${search.auditStatus==s.key }"> selected="selected" </c:if>>${s.value}</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td><input class="btnQuery" name="" type="submit" value="查询"></td>
                  </tr>
                  </c:if>
                  <c:if test="${activeOperator.type == 0 }">
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${companyList}" var="c" > 
							<option value="${c.companyNo }" <c:if test="${search.companyNo==c.companyNo }"> selected="selected" </c:if>>${c.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="createBy" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.createBy==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                </tr>
                <tr>
                 <td width="90px" height="30">审核状态：</td>
                  <td width="160">
                  <select name="auditStatus" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${auditStatusMap}" var="s" > 
							<option value="${s.key}" <c:if test="${search.auditStatus==s.key }"> selected="selected" </c:if>>${s.value}</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td colspan="4"><input class="btnQuery" name="" type="submit" value="查询"></td>
                </tr>
                  </c:if>
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
    <c:set var="dist" value="false" />
    <c:set var="share" value="false" />
    <c:set var="submit" value="false" />
    <c:set var="approved" value="false" />
    <c:set var="rejected" value="false" />
    <c:set var="enable" value="false" />
    <c:set var="disable" value="false" />
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
		<c:if test="${item eq 'dist'}">   
			<c:set var="dist" value="true" />  
		</c:if> 
		<c:if test="${item eq 'share'}">   
			<c:set var="share" value="true" />  
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
		<c:if test="${item eq 'enable'}">   
			<c:set var="enable" value="true" />  
		</c:if> 
		<c:if test="${item eq 'disable'}">   
			<c:set var="disable" value="true" />  
		</c:if>  
	</c:forEach>
    <c:if test="${delete}">   
    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')"/> 
	</c:if> 
    <c:if test="${submit}">   
    <input type="button" class="btn btn80" value="提交审核" onclick="strategyAudit(1)"/> 
	</c:if> 
    <c:if test="${approved}">   
    <input type="button" class="btn btn80" value="审核通过" onclick="strategyAudit(2)"/> 
	</c:if> 
    <c:if test="${rejected}">   
    <input type="button" class="btn btn80" value="审核不通过" onclick="strategyAudit(3)"/> 
	</c:if> 
    <c:if test="${enable}">   
    <input type="button" class="btn btn80" value="启用" onclick="strategyAudit(4)"/> 
	</c:if> 
    <c:if test="${disable}">   
    <input type="button" class="btn btn80" value="禁用" onclick="strategyAudit(5)"/> 
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'strategyNos')"></th>
          <th width="*%">策略名称</th><%--
          <th width="*%">策略类型</th>
          <th width="*%">策略分类</th>
          --%><th width="*%">审核状态</th>
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
			<td colspan="11">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      <c:forEach items="${list}" var="s">
      <input type="hidden" name="isSelf${s.strategyNo}" id="isSelf${s.strategyNo}" value="${s.isSelf}" />
       <input type="hidden" name="auditStatus${s.strategyNo}" id="auditStatus${s.strategyNo}" value="${s.auditStatus}" />
        <tr>
          <td align="center">
	         <input type="checkbox" name="strategyNos" value="${s.strategyNo}">
          </td>
          <td align="center">
            <c:choose>
             <c:when test="${fn:length(s.strategyName) > 10}">
                 ${fn:substring(s.strategyName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${s.strategyName }
             </c:otherwise>
            </c:choose> 
          </td><%--
          <td align="center">
            <c:forEach items="${strategyTypeMap}" var="t" > 
					<c:if test="${s.strategyType==t.key }"> ${t.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             <c:if test="${s.strategyType == 3}">
                <c:forEach items="${timeStrategyChildTypeMap}" var="t" > 
					<c:if test="${s.childType==t.key }"> ${t.value }</c:if>
		        </c:forEach>                    
             </c:if>
             <c:if test="${s.strategyType == 4}">
               <c:forEach items="${frequencyStrategyChildTypeMap}" var="t" > 
					<c:if test="${s.childType==t.key }"> ${t.value }</c:if>
		        </c:forEach>  
             </c:if>
          </td>
          --%><td align="center">
             <c:forEach items="${auditStatusMap}" var="t" > 
					<c:if test="${s.auditStatus==t.key }"> ${t.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">${s.company.companyName }</td>
          <td align="center">${s.operator.operatorName }</td>
          <td align="center">${s.createTime }</td>
          <td align="center">${s.updateTime }</td>
          <td class="tdOpera2" align="center">
          <c:if test="${detail}">  <a href='javascript:;' onclick='detail(${s.strategyNo})'>详情</a> </c:if>
          <c:if test="${edit}">
             <c:if test="${s.isSelf==1 && s.auditStatus!=2}">
              <a href="javascript:;" onclick="toEditPage('${s.strategyNo }')">编辑</a>
             </c:if>
          </c:if><%--
          <c:if test="${s.isSelf==1 && s.strategyType == 0}">
                 <a href="javascript:;" onclick="contentMgmt('${s.id }')">内容管理</a>
          </c:if>
           <c:if test="${s.isSelf==1 && s.strategyType == 1}">
                 <a href="javascript:;" onclick="regionMgmt('${s.id}')">区域管理</a>
          </c:if> 
          <c:if test="${s.isSelf==1 && s.strategyType == 2}">
                 <a href="javascript:;" onclick="userMgmt('${s.id }')">受众管理</a>
          </c:if>
          --%><c:if test="${s.isSelf==1 && delete}">
              <a href="javascript:;" onclick="deleteSets('${s.strategyNo}')">删除</a>
         </c:if>
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
      <input class="btn btn80" type="button" value="新增" onclick="toEditPage()"/>
    </c:if>
    <c:if test="${delete}">
      <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')"/>
    </c:if>
    <c:if test="${submit}">   
    <input type="button" class="btn btn80" value="提交审核" onclick="strategyAudit(1)"/> 
	</c:if> 
    <c:if test="${approved}">   
    <input type="button" class="btn btn80" value="审核通过" onclick="strategyAudit(2)"/> 
	</c:if> 
    <c:if test="${rejected}">   
    <input type="button" class="btn btn80" value="审核不通过" onclick="strategyAudit(3)"/> 
	</c:if> 
    <c:if test="${enable}">   
    <input type="button" class="btn btn80" value="启用" onclick="strategyAudit(4)"/> 
	</c:if> 
    <c:if test="${disable}">   
    <input type="button" class="btn btn80" value="禁用" onclick="strategyAudit(5)"/> 
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>