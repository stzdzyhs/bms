<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>投放策略</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>

<script type='text/javascript' src="${path}/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/css/themes/redmond/jquery-ui.css" />

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

<script src="${path}/js/My97DatePicker/WdatePicker.js"></script>

<script type='text/javascript' src="${path}/js/kitJs/kit.js"></script>
<!--[if IE]>
<script type='text/javascript' src="${path}/js/kitJs/ieFix.js"></script>
<![endif]-->
<script type='text/javascript' src="${path}/js/kitJs/array.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/date.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/dom.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/selector.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/datepicker.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/js/kitJs/datepicker.css" />

<script type='text/javascript' src="${path}/js/multiselect/jquery.multiselect.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/js/multiselect/jquery.multiselect.css" />

<script src="${path}/js/common.js" type="text/javascript"></script>
<script src="${path}/js/const.js" type="text/javascript"></script>
<script src="${path}/js/opermgmt/strategy/strategy.js" type="text/javascript"></script>

<script src="${path}/js/strategyEdit2.js" type="text/javascript"></script>

<style type="text/css">
.cont-detail{
	height: 100px;border:1px solid #ccc;
	margin:10px 0;position:relative;
}
.delt-btn{
	display:block;width:20px;height:20px;position:absolute;bottom:0;right:0;
	background:#e90;cursor:pointer;text-align:center;	
}

td {
    position: relative;
}

.btn-del-little {
	width:16px;
	height:16px;
	background:#e90;
	cursor:pointer;
}


.fieldset-wrap {
	width:100%;
	margin:0px;
	padding:0px;
}

.fieldset1 {
    background: #ecf6ff!important;
	padding:10px;
}

</style>

<script type="text/javascript">

/**
 * 选择
 */
var i=0;
function btnSelectOnClick(id) {
	//art.dialog.data('positionId',$("#advPositionId").val());
	//art.dialog.data('selectedstrategyNo', selectedstrategyNo);
	//art.dialog.data('strategyTypes', strategyTypes);
	
	var m1 = strategyModel.getMap1Data(id);
	//art.dialog.data('data',data.data);
	
	/*
	art.dialog.open(url + "?strategyNo=" + strategyNo, 
	{
		title: title, 
		width: 980,
		height: 450,
		lock: true,
		close:function(){
			$("#btnQuery").click();
		},
		okVal:"保存",
		ok:function(){
			var iframe = this.iframe.contentWindow;
			iframe.saveResource();
			return false;
		},
		cancel:function(){
			return true;
		}
	});
	*/
	
	var companyNo = $("#companyNo").val();
	if(id==STRATEGY_CONDITION_TYPE_COMPANY && isEmpty(companyNo)) {
		art.dialog.alert("请首先选择运营商");
		return;
	}
	
	var exIds = strategyModel.getRefIds(id);
	
	var ps = "companyNo=" + companyNo;
	if(!isEmpty(strategyModel.strategyNo)) {
		ps = ps + "&strategyNo="+ strategyModel.strategyNo;
	}

	var p = strategyModel.toUrlParamStr(exIds,"excludeIds");
	if(p!="") {
		ps = ps + "&" + p;
	}

	var url = m1.selectUrl;
	if(ps!="") {
		url = url + "?" + ps;
	}
	
	art.dialog.open(url, {
		title: "请选择" + m1.name, //'请选择频道'
		width: 1040,
		height: 440,
		lock: true,

		okVal:"选择",
		ok:function(){
			var iframe = this.iframe.contentWindow;
			var data = iframe.saveResource();
			if(data==null) {
				return false;
			}
			
			for(i=0;i<data.length;i++) {
				var w = strategyModel.wrapRefData(id,data[i]);
				strategyModel.addKey(id, w);	
			}
			return true;
		},
		cancel:function(){
			return true;
		},
		
		close:function() {
			i++;
			console.log(i + " **** on close");
		}		
	});
}

var view = {
	// 已有条件
	//strategyConditionList:[],
	createConditionDiv:function(id) {
    	if (this.hasPanel(id)) {
    		return false;
    	}
    	
   		//this.strategyConditionList.push(iVal);
   		var data = strategyModel.getMap1Data(id);
    	//var t = 	
/*     		 "<tr data-val='{conditionTypeVal}' id='pl{conditionTypeVal}'>" +
	    		    "<td width='35%' class='tdBlue2'>{conditionTypeName}" + 
    	        	"<input type='button' class='delt-btn' onclick=\"btnDelDivOnClick('{conditionTypeVal}');\">-</button></div>" +    	        	
    		    "</td>" + 
    		    "<td class='tdBlue'>" + 
    		    	"<div >" + 
    		    		"<input name='button' type='button' class='btnQuery' id='btn_selectChannel' value='请选择'" + 
    						"onClick='btnSelectOnClick({conditionTypeVal})' /> <div id='materialIdTip' style='width: 70%; float: right;'></div>" + 
    				"</div>" + 
					"<fieldset style='width: 99%' id='channelFSet'>" + 
    					"<legend id='key{conditionTypeVal}cnt'>" +
    							"已选{conditionTypeName}0个" + 
    						"<input type='hidden'  id='materialNum' value='${fn:length(materialList) }'/>" + 
    					"</legend>" + 
    					"<div class='checkboxitems' id='key{conditionTypeVal}'>" + 
    						"<li>" + 
    							"<label> " + 
    							     //"<input type='checkbox' class='checkbox' checked value='${mate.id }' name='materialId' id='materialId' /> test " +
    							     //"<input type='button' class='btn-del-little' checked value='${mate.id }' name='materialId' id='materialId' /> test " + 
    							"</label>" + 
							"</li>" + 
    					"</div>" + 
    			  "</fieldset>" +	    			  
    		    "</td>" + 
    		  "</tr>";
 */    		  
 		var t = $("#pl_conditionTypeVal_")[0].outerHTML;
 		var s = String.format3(t, {conditionTypeVal:id, conditionTypeName:data.name})
		//var s = String.format2(t, {conditionTypeVal:id, conditionTypeName:data.name});	    	
    		
		$("#plCondition").append(s);
		$("#pl" + id).show();
		return true;
	},
			
	deleteConditionDiv:function(id) {
	  	$('#pl' + id).remove();
	},

	clearConditionDiv:function(id) {
		this.checkPanel(id);
		p = $("#pl" + id + " .checkboxitems").children().remove();
	},
	
	checkPanel:function(id) {
		var p = $('#pl' + id);
		if(p.length!=1) {
			throw new Error("panel error: " + id);
		}
	},
	
	hasPanel:function(id) {
		if(id==-1) {
			throw new Error("-1...");
		}
		var p = $('#pl' + id);
		if(p.length==1) {
			return true;
		}
		return false;
	},
	
	// id : the select id
	// dataKey: the data key
	// data: 
	addKey:function(id, dataKey, data) {
		var k = strategyModel.getDataId(id,dataKey);
		var refK = strategyModel.getDataRefId(id,dataKey);
		var refName = strategyModel.getDataRefName(id,dataKey);
		
		var p = $("#key" + id);
		if(p.length!=1) {
			throw new Error("keys 错误: " + id);
		}
		
		var t = 
		"<li id='{eid}'>" + 
	     	"<input type='button' class='btn-del-little' value='-' onclick='delKey({id},{dataKey});' /> " +
		  	"<a onclick=\"showDetailDlg({id},'{url}?{refIdName}={refId}');\"> {name}" + 
		 	"</a>" + 
	 	"</li>";
	 	
	 	var m1 = strategyModel.getMap1Data(id);
	 	
	 	var refId = strategyModel.getDataRefId(id,dataKey);
	 	
	 	var eid = "key" + id + "_" + dataKey;
	 	var s = String.format2(t, {
	 		eid:eid,
	 		url:m1.detailUrl,
	 		refIdName:m1.detailIdName,
	 		refId: refId,
	 		name:refName,
	 		id:id,
	 		dataKey:dataKey
	 	});
	  	$('#pl' + id + " .checkboxitems").append(s);
	},
	
	delKey:function(id,dataKey) {
	 	var eid = "key" + id + "_" + dataKey;
		
		var p = $("#" + eid);
		if(p.length!=1) {
			throw new Error("key 错误: " + id + " " + dataKey);
		}
		p.remove();
	},
	
	// update keycnt
	updateKeyCnt:function(id, num) {
		eid = "#key" + id + "cnt";
		num = strategyModel.getDataSize(id);
		$(eid).text("已选" + strategyModel.getMap1Data(id).name + num + "个");
	},

	updateBasicInfo:function(data) {
		$("#strategyName").val(data.strategyName);
		$('#strategyForm').val(data.strategyForm);
		$("#companyNo").val(data.companyNo);
	}	
};

var dataId = null;
try {
	dataId=parseInt("${strategyNo}");
	if(dataId==null || isNaN(dataId)) {
		dataId = null;
	}
}
catch(e) {
	dataId = null;
}

//更新 strategyName
function succCallback1(data) {
	$("#eStrategyName").text( "策略:" + data.data.strategyName + "");
	$('#strategyForm').val(data.data.strategyForm);
	$("#companyNo").val(data.data.companyNo);		
}

$(document).ready(function() {
	if(isOpUpdate()) {
		$("#companyNo").attr("disabled","disabled");	
	}
	
	//连续开始时间
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
		type : "POST",
		dataType : "html",
		buttons:$("#button"),
		url: "${path}/opermgmt/strategy/saveOrUpdateStrategy.action",
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
			}else if(rsobj.desc=='repeat'){
				art.dialog.alert("该资源已经存在！");
			}else{
				art.dialog.alert("保存失败！");
			}
		}
	}, submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'});
	
	strategyModel.setView(view);
	
	strategyModel.strategyNo=dataId;
	
	strategyModel.loadData(succCallback1);
	
	var x;
	addSelectValue("#eCondition", "", "请选择分类");
	var m1 = strategyModel.getMap1();
	for(id in m1) {
		addSelectValue("#eCondition", id, strategyModel.getMap1Data(id).name);
	}
	
	//$("#eCondition").val(STRATEGY_CONDITION_TYPE_CARD_REGION);
	
});

function showDetailDlg(id, url) {
	var m1 = strategyModel.getMap1Data(id);	
	art.dialog.open(url, {
		title: m1.name + "详情", 
		width: 750,
		height: 600,
		lock: true
	});
}
/////////////////////////// btn click event ////////////////////////////

function btnCreateDivOnClick() {
	var sTxt = $("#eCondition").find("option:selected").text(),
    iVal = $("#eCondition").val();
	if(isEmpty(iVal)) {
		art.dialog.alert("请选择具体的分类");
		return;
	}
	if(iVal>=0) {
		var t = view.createConditionDiv(iVal);
		if(!t) {
			art.dialog.alert("该策略条件已存在,请选择其他分类策略条件");
		}
	}
	else if(iVal==-1) { // freq
		$("#plFreq").show();
		onFreqTypeChange();
	}
	else {
		throw new Error("error ival:" + iVal);
	}
}

function btnDeleteDivOnClick() {
	var id = $("#eCondition").val();
	
	if(isEmpty(id)) {
		art.dialog.alert("请选择具体的分类");
		return;
	}
	
	if(id==-1) {
		art.dialog.confirm("确认删除频率?", function(){
			$("#plFreq").hide();
		});
	}
	else {
		if (strategyModel.hasCondition(id)) {
			var m1 = strategyModel.getMap1Data(id);
			
			art.dialog.confirm('确认删除所有的' + m1.name + "?", function(){
				strategyModel.deleteConditionType(id);
			});
		}
		else {
			view.deleteConditionDiv(id);
		}
	}
}

function btnDebugOnClick() {
	var s = JSON.stringify(strategyModel.strategyData);
	console.log(s);
	s = "no: " + strategyModel.strategyNo;
	console.log(s);
}

function delKey(id,idx) {
	//var m1 = strategyModel.getMap1Data(id);
	
	var name = strategyModel.getDataName(id,idx);
	art.dialog.confirm('确认删除:' + name + "?", function(){
		strategyModel.deleteKey(id,idx);
	});
}

function saveStrategy() {
	if($("#strategyName").val()=="") {
		art.dialog.alert("请输入策略名称");
		return;
	}
	if($("#strategyForm").val()=="") {
		art.dialog.alert("请输入条件关系");
		return;
	}
	
	if($("#companyNo").val()=="") {
		art.dialog.alert("请选择运营商");
		return;
	}

	if(!strategyModel.hasData()) {
		art.dialog.alert("请选择策略关联的条件");
		return;
	}
	
	strategyModel.strategyData.strategyName = $("#strategyName").val();
	strategyModel.strategyData.strategyForm = $("#strategyForm").val();
	strategyModel.strategyData.companyNo=$("#companyNo").val();

	var s = JSON.stringify(strategyModel.strategyData);
	$.ajax({
		url : ROOT + "/opermgmt/strategy/saveStrategy.action",
		type : "POST",
		dataType : "json",
		data : {
			strategy: s
		},

		success : function(data) {
			if (isResultSucc(data)) {
				art.dialog.alert("保存成功！", goBack);
			}
			else {
				if (data.desc == null) {
					data.desc = "";
				}
				art.dialog.alert("保存策略失败！" + data.desc);
			}
		},
		error : function(a, b) {
			art.dialog.alert("保存策略失败！");
		},
		complete:function() {
		}
	});
}

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/strategy/strategyList.action";
	document.getElementById("form2").submit();
}

function debug1() {
	var s = JSON.stringify(strategyModel);
	console.log(s);
}

</script>
</head>

<body style="margin:0px;padding:0px;margin:20px;">

<div class="title">
<!-- 	<h2 id='eStrategyName'>策略</h2> -->
</div>
<input type="hidden" name="isShares" id="isShares"/>
<form id="form1" name="form1">
<div style="padding:5px;">
<fieldset style="width:85%;margin:0px;">
	<legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td class="tdBlue" width="10%">策略名称</td>
    	<td class="tdBlue" width="40%">
      		<input name="strategyName" id="strategyName" maxlength="50" value="${strategy.strategyName}" >
      		<div id="strategyName_tip" style="width:60%; float:right"></div>
     	</td>
     	
	    <td class="tdBlue" width="10%">所属运营商</td>
	    <td class="tdBlue" width="40%">
	      <select name="companyNo" id="companyNo" class="form130px">
			<option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="c">
				<option value="${c.companyNo }" <c:if test="${strategy.companyNo==c.companyNo }"> selected="selected" </c:if>>${c.companyName}</option>
			</c:forEach>
		 </select>
		 <div id="companyNo_tip" style="width:60%; float:right"></div>
		</td>
	</tr>
	<tr>
     	<td class="tdBlue" width="10%">条件关系</td>
    	<td class="tdBlue" width="40%">
			<select id="strategyForm">
				<option value="">请选择分类</option>
				<option value="1">与</option>
				<option value="2">或</option>
			</select>
		</td>	  	

     	<td class="tdBlue" width="10%">策略条件类型</td>
     	
    	<td class="tdBlue" width="40%">
			<select id="eCondition">								
	 		</select>
	  		<input class="btnQuery" type="button" value="添加"  onclick="btnCreateDivOnClick();"/>
	  		<input class="btnQuery" type="button" value="删除"  onclick="btnDeleteDivOnClick();"/>
	  		<input class="btnQuery" type="button" value="Debug"  onclick="debug1();"/>
<!-- 	  		<input type="button" value="删除"  onclick="delStrategyType()"/> -->
	  	</td>
  	</tr>
</table>
</fieldset>
</div>


<div id="plCondition">
<!-- ------------------------0  -->
<div id="pl_conditionTypeVal_" style="display:none" class="fieldset-wrap">
	<fieldset class="fieldset1"><legend>&nbsp;_conditionTypeName_</legend>
		<div id='' class="tableCommon tdBorWhite" style="overflow:visible;width:100%;border:0;cellspacing:0;cellpadding:0">
			<table id='' class="tableCommon tdBorWhite" style="overflow:visible;" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr id="" id='pl{conditionTypeVal}'> 
				<td width='35%' class='tdBlue2'>
<!-- 		    		<input type='button' class='delt-btn' onclick="btnDelDivOnClick(_conditionTypeVal_);" value="-"> -->
				</td>  
		    	<td class='tdBlue'>
		       		<div >
		    			<input name='button' type='button' class='btnQuery' id='' value='请选择'  
		    				onClick='btnSelectOnClick(_conditionTypeVal_);' />
		    			<div id='materialIdTip' style='width: 70%; float: right;'></div>  
					</div>  
					<fieldset style='width: 99%' id='channelFSet'>  
		    			<legend id='key_conditionTypeVal_cnt'> 
		    				已选0个  
		    			</legend>  
		    			<div >
		    				<ul id='key_conditionTypeVal_' class='checkboxitems'>
<%-- 			    				<li>  
			    					<label>   
			    					    <input type='checkbox' class='checkbox' checked value='${mate.id }' name='materialId' id='materialId' /> test  
			    						 <input type='button' class='btn-del-little' checked value='${mate.id }' name='materialId' id='materialId' /> test   
			
									</label>  
								</li>
 --%>
 							</ul>  
		    			</div>  
		    		</fieldset> 	    			  
		    	</td>  
		    </tr>
		    </table>
		</div>
	</fieldset>
</div>

</div>

<div style="width:100%; text-align:center; margin-top:10px;">
	<input value="保存" type="button" class="btnQuery" onclick="saveStrategy();"/>
	&nbsp;&nbsp;
	<input value="返回" type="button" class="btnQuery" onClick="goBack()" />
	<!-- 
	<input value="debug" type="button" class="btnQuery" onclick="btnDebugOnClick();"/>
	 -->
</div>
</form>

<div style="height:40px;">
</div>
 
<form id="form2" name="form2" method="post">
	<!-- 缓存查询条件 start -->
	<input type="hidden" name="strategyName"  value="${search.strategyName}" />
	<input type="hidden" name="strategyType"  value="${search.strategyType}" />
	<input type="hidden" name="auditStatus"  value="${search.auditStatus}" />
	<input type="hidden" name="searchCompanyNo"  value="${search.searchCompanyNo}" />
	<input type="hidden" name="searchOperatorNo"  value="${search.searchOperatorNo}" />
	<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
	<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
	<!-- 缓存查询条件 end -->
</form>

</body>
</html>
