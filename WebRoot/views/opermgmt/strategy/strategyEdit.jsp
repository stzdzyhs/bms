<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>策略信息</title>
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<script type='text/javascript' src="${path}/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/css/themes/redmond/jquery-ui.css" />
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
 
<script src="${path}/js/swfupload/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/plugins/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/myhandlers.js" type="text/javascript"></script>


<script language="javascript" src="${path}/js/My97DatePicker/WdatePicker.js"></script>
 <script src="${path}/js/win/win.js" type="text/javascript"></script>
  <script src="${path}/js/selectlist.js" type="text/javascript"></script>

		<!-- 多选日期控件 -->
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


<script type="text/javascript">
$(document).ready(function(){
	var strategyCode="${strategy.strategyCode}".toString().split(",");		
	for(var i=0;i<20;i++){
		var length=strategyCode.indexOf(i.toString());
		if(length>=0){			
			$("#div_"+i).show();			
		}
	}
	
	
	
	
	
	//连续开始时间
	var beginTime = $("#beginTime");
	beginTime.addClass("Wdate");
	beginTime.eq(0).click(function(){WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	
	var endTime = $("#endTime");
	endTime.addClass("Wdate");
	endTime.eq(0).click(function(){WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	
	var dayBeginTime = $("#dayBeginTime");
	dayBeginTime.addClass("Wdate");
	dayBeginTime.eq(0).click(function(){WdatePicker({maxDate:'#F{$dp.$D(\'dayEndTime\')||\'23:59:59\'}',dateFmt:'HH:mm:ss'});});
	
	var dayEndTime = $("#dayEndTime");
	dayEndTime.addClass("Wdate");
	dayEndTime.eq(0).click(function(){WdatePicker({minDate:'#F{$dp.$D(\'dayBeginTime\')||\'00:00:00\'}',dateFmt:'HH:mm:ss'});});
	
	 $("#years").multiselect({
	        noneSelectedText: "--请选择--",
	        checkAllText: "全选",
	        uncheckAllText: '全不选',
	        selectedText: '已选择 #',
	        selectedList:7
	    });

	 $("#months").multiselect({
	        noneSelectedText: "--请选择--",
	        checkAllText: "全选",
	        uncheckAllText: '全不选',
	        selectedText: '已选择 #',
	        selectedList:7
	    });
	 
	 $("#monthDays").multiselect({
	        noneSelectedText: "--请选择--",
	        checkAllText: "全选",
	        uncheckAllText: '全不选',
	        selectedText: '已选择 #',
	        selectedList:7
	    });
	 
	 $("#weeks").multiselect({
	        noneSelectedText: "--请选择--",
	        checkAllText: "全选",
	        uncheckAllText: '全不选',
	        selectedText: '已选择 #',
	        selectedList:7
	    });
	 
	 $("#weekDays").multiselect({
	        noneSelectedText: "--请选择--",
	        checkAllText: "全选",
	        uncheckAllText: '全不选',
	        selectedText: '已选择 #',
	        selectedList:7
	    });
	
		$kit.ev({
			el : '#days',
			ev : 'focus',
			fn : function(e) {
				var d, ipt = e.target;
				d = e.target[$kit.ui.DatePicker.defaultConfig.kitWidgetName];
				if(d) {
					d.show();
				} else {
					d = new $kit.ui.DatePicker({
						date : ipt.value,
						dateFormat : 'yyyy-mm-dd'
					}).init();
					d.adhere($kit.el('#days'));
					d.show();
				}
			}
		});
		
		$kit.ev({
			el : document,
			ev : 'click',
			fn : function(e) {
				var input = $kit.el('#days');
				d = input[$kit.ui.DatePicker.defaultConfig.kitWidgetName];
				if(d && !$kit.contains(d.picker, e.target) && input != e.target) {
					d.hide();
				}
			}
		});
		

		var years = '${strategy.years}';
		if (years != null && typeof(years)!="undefined"){
			var yearArr = years.split(",");
			$("#years").multiselect("widget").find(":checkbox").each(function(){
				   for (var i=0; i < yearArr.length; i++){
					   if (this.value == yearArr[i]){
						   this.click();
					   }
				   }

				   
			});
		}
		
		var months = '${strategy.months}';
		if (months != null && typeof(months)!="undefined"){
			var monthArr = months.split(",");
			$("#months").multiselect("widget").find(":checkbox").each(function(){
				   for (var i=0; i < monthArr.length; i++){
					   if (this.value == monthArr[i]){
						   this.click();
					   }
				   }

				   
			});
		}
		
		var monthDays = '${strategy.monthDays}';
		if (monthDays != null && typeof(monthDays)!="undefined"){
			var monthDayArr = monthDays.split(",");
			$("#monthDays").multiselect("widget").find(":checkbox").each(function(){
				   for (var i=0; i < monthDayArr.length; i++){
					   if (this.value == monthDayArr[i]){
						   this.click();
					   }
				   }

				   
			});
		}
		
		var weeks = '${strategy.weeks}';
		if (weeks != null && typeof(weeks)!="undefined"){
			var weekArr = weeks.split(",");
			$("#weeks").multiselect("widget").find(":checkbox").each(function(){
				   for (var i=0; i < weekArr.length; i++){
					   if (this.value == weekArr[i]){
						   this.click();
					   }
				   }

				   
			});
		}
		
		var weekDays = '${strategy.weekDays}';
		if (weekDays != null && typeof(weekDays)!="undefined"){
			var weekDayArr = weekDays.split(",");
			$("#weekDays").multiselect("widget").find(":checkbox").each(function(){
				   for (var i=0; i < weekDayArr.length; i++){
					   if (this.value == weekDayArr[i]){
						   this.click();
					   }
				   }

				   
			});
		}
		
		
 		var strategyType = '${strategy.strategyType}';
		var childType = $("#childType");
		var childTypeValue = '${strategy.childType}';
		var timeType = $("#timeType");
		
			//childType.append("<c:forEach items='${timeStrategyChildTypeMap}' var='t'><option value='${t.key }' <c:if test='${strategy.childType==t.key }'> selected='selected' </c:if>>${t.value}</option></c:forEach>"); 
			if (childTypeValue == 1){
				timeType.append("<c:forEach items='${dayTimeTypeMap}' var='t'><option value='${t.key }' <c:if test='${strategy.timeType==t.key }'> selected='selected' </c:if>>${t.value}</option></c:forEach>"); 
			}else if (childTypeValue == 2){
				timeType.append("<c:forEach items='${weekTimeTypeMap}' var='t'><option value='${t.key }' <c:if test='${strategy.timeType==t.key }'> selected='selected' </c:if>>${t.value}</option></c:forEach>"); 
			}else if (childTypeValue == 3){
				timeType.append("<c:forEach items='${monthTimeTypeMap}' var='t'><option value='${t.key }' <c:if test='${strategy.timeType==t.key }'> selected='selected' </c:if>>${t.value}</option></c:forEach>"); 
		
		}

/* 		childType.bind('change',function(){
			onChildTypeChange(strategyType);
		}); */
		
		onStrategyTypeChangeInit();

	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/opermgmt/strategy/saveOrUpdateStrategy.action",
			beforeSend: function() { 
				var strategyType = $("#strategyType option:selected").val();
				if (strategyType == 4){
					var childType = $("#childType option:selected").val();
					
					
					var broadcastTimes = $("#broadcastTimes").val();
					
					var broadcastTimesReg = /^[1-9][0-9]*$/;
					
					
					if (childType == 1){
						var timeUnit = $("#timeUnit option:selected").val();
						if (timeUnit == null || timeUnit == ''){
							art.dialog.alert("请选择时间单位！");
							return false;
						}
					}
				}
				
				if (strategyType == '0'){
					var groupRotation = $("#groupRotation option:selected").val();
					if (groupRotation == 1){
						var rotationInterval = $("#rotationInterval").val();
						if (rotationInterval == null || rotationInterval == ''){
							art.dialog.alert("轮换间隔时间不能为空！");
							return false;
						}
						var rotationIntervalReg = /^([1-9][0-9]*)$/;
						if (!rotationIntervalReg.test(rotationInterval)){		
							art.dialog.alert("轮换间隔时间必须为正整数，且不能为0！");
							return false;
						}
					}
				}
				
				if (strategyType == 3){
					var childType = $("#childType option:selected").val();
		
					
					if (childType == 0){
						var beginTime = $("#beginTime").val();
						if (beginTime == null || beginTime == ''){
							art.dialog.alert("请选择连续开始时间！");
							return false;
						}
						
						var endTime = $("#endTime").val();
						if (endTime == null || endTime == ''){
							art.dialog.alert("请选择连续结束时间！");
							return false;
						}
					}else if (childType == 1){
						var timeType = $("#timeType option:selected").val();
						if (timeType == null || timeType == ''){
							art.dialog.alert("请选择连续时间类型！");
							return false;
						}
						
						if (timeType == 0){
							var days = $("#days").val();
							if (days == null || days == ''){
								art.dialog.alert("请选择连续投放日期！");
								return false;
							}
							
							var dayArr = days.split(",");
							if (dayArr.length > 31){
								art.dialog.alert("投放日期不能超过31天！");
								return false;
							}
						}else if (timeType == 1){
							var years = $("#years option:selected").val();
							if (years == null || years == ''){
								art.dialog.alert("请选择投放年份！");
								return false;
							}
						}

					}else if (childType == 2){
						var timeType = $("#timeType option:selected").val();
						if (timeType == null || timeType == ''){
							art.dialog.alert("请选择时间类型！");
							return false;
						}
						
						if (timeType == 0){
							var weeks = $("#weeks").val();
							if (weeks == null || weeks == ''){
								art.dialog.alert("请选择投放周！");
								return false;
							}
						}
						
						var years = $("#years option:selected").val();
						if (years == null || years == ''){
							art.dialog.alert("请选择投放年份！");
							return false;
						}
					}else if (childType == 3){
						var timeType = $("#timeType option:selected").val();
						if (timeType == null || timeType == ''){
							art.dialog.alert("请选择时间类型！");
							return false;
						}
						
						if (timeType == 0){
							var months = $("#months").val();
							if (months == null || months == ''){
								art.dialog.alert("请选择投放月份！");
								return false;
							}
						}
						
						var years = $("#years option:selected").val();
						if (years == null || years == ''){
							art.dialog.alert("请选择投放年份！");
							return false;
						}
					}
				}
			    
				var days = $("#days").val();
				if (days != null || days != ''){
					var dayArr = days.split(",");
					if (dayArr.length > 31){
						art.dialog.alert("投放日期不能超过31天！");
						return false;
					}
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
				}else if(rsobj.desc=='repeat'){
					art.dialog.alert("该资源已经存在！");
				}else{
					art.dialog.alert("保存失败！");
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});
	
	$("#strategyName").formValidator({onFocus:"策略名称不能为空,请确认",onCorrect:"&nbsp;",tipID:"strategyName_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"策略名称不能带空格,请确认"},min:1,onError:"策略名称不能为空,请确认"})
    .ajaxValidator({
    type : "POST",
	dataType : "html",
	async : true,
	url : "${path}/opermgmt/strategy/checkStrategyName.action?id=${strategy.id }",
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
	onError : "该策略名称已存在，请更换策略名称",
	onWait : "正在对策略名称进行合法性校验，请稍候..."
   }).defaultPassed();

	$("#companyNo").formValidator({onFocus:"所属运营商不能为空,请选择",onCorrect:"&nbsp;",tipID:"companyNo_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"名称前后不能带空格,请确认"},min:1,onError:"所属运营商不能为空,请选择"}).defaultPassed();
	//$("#strategyType").formValidator({onFocus:"策略类型不能为空,请选择",onCorrect:"&nbsp;",tipID:"strategyType_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"策略类型前后不能带空格,请确认"},min:1,onError:"策略类型不能为空,请选择"}).defaultPassed();

});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/strategy/strategyList.action";
	document.getElementById("form2").submit();

}

function onStrategyTypeChangeInit(){

	var strategyType = $("#strategyType option:selected").val();
	var childType = $("#childType");
	if (strategyType == "0"){
		//显示分组轮换详情
		$("#groupRotationSet").show();
		var groupRotation = '${strategy.groupRotation}';
		if (groupRotation == 1){
			$("#rotationIntervalNameTd").show();
			$("#rotationIntervalValueTd").show();
			$("#rotationIntervalNoDataTd").hide();
		}
	}else if (strategyType == 3){
		//显示策略分类
		$("#childTypeNameTd").show();
		$("#childTypeValueTd").show();
		$("#strategyTypeNoDataTd").hide();
	}else if(strategyType == 4){
		//显示策略分类
		$("#childTypeNameTd").show();
		$("#childTypeValueTd").show();
		$("#strategyTypeNoDataTd").hide();
	}
	
	onChildTypeChangeInit(strategyType);
	
/*     childType.bind('change',function(){
		onChildTypeChange();
	});  */
}

function onChildTypeChangeInit(strategyType){
	var childType = $("#childType option:selected").val();
	var timeType = $("#timeType");


		if (childType == '0'){
			//显示连续开始时间和结束时间
			$("#timeTr").show();
			$("#timeSet").show();
		}else if (childType == 1){
			//显示时间类型
			$("#timeTypeTr").show();
			$("#timeSet").show();
		}else if (childType == 2){
			$("#timeTypeTr").show();
			$("#timeSet").show();
		}else if (childType == 3){
			$("#timeTypeTr").show();
			$("#timeSet").show();
		}
	
		if (childType == 1){
			//显示时间单位
			$("#timeUnitNameTd").show();
			$("#timeUnitValueTd").show();
			$("#timeUnitNoDataTd").hide();
		}
		$("#frequencySet").show();
	
	
	onTimeTypeChangeInit(childType);
	onTimeTypeChange();
/*     timeType.bind('change',function(){
		onTimeTypeChange();
	});  */ 
}

function onTimeTypeChangeInit(childType){
	var strategyType = $("#strategyType option:selected").val();
	var timeType = $("#timeType option:selected").val();
	if (strategyType == 3){
        if (childType == 1){
			if (timeType == "0"){
				//显示投放日期
				$("#daysNameTd").show();
				$("#daysValueTd").show();
				$("#timeTypeNoDataTd").hide();
			}else if (timeType==1){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
			}
			
			//显示每天开始时间
			$("#dayTimeTr").show();
		}else if(childType == 2){
			
			if (timeType == "0"){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示投放周
				$("#weekDaysTr").show();
				$("#weeksNameTd").show();
				$("#weeksValueTd").show();
				$("#weeksNoDataTd").hide();
				
			}else if (timeType == 1){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示指定星期
				$("#weekDaysTr").show();
			}
			
			//显示每天开始时间
			$("#dayTimeTr").show();
		}else if(childType == 3){
			
			if (timeType == "0"){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示投放月份
				$("#monthDaysTr").show();
				$("#monthsNameTd").show();
				$("#monthsValueTd").show();
				$("#monthsNoDataTd").hide();
			} else if (timeType == 1){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示指定月中的天数
				$("#monthDaysTr").show();
			}
			
			//显示每天开始时间
			$("#dayTimeTr").show();
		}
	}
}


function onStrategyTypeChange(){
	var strategyType = $("#strategyType option:selected").val();
	var childType = $("#childType");
	 $("#childType option").each(function(){
		 if (this.index > 0){
			 this.remove();
		 }
	});
	 
	if (strategyType == "0"){
		//显示分组轮换详情
		$("#groupRotationSet").show();
		var groupRotation = '${strategy.groupRotation}';
		if (groupRotation == 1){
			$("#rotationIntervalNameTd").show();
			$("#rotationIntervalValueTd").show();
			$("#rotationIntervalNoDataTd").hide();
		}else{
			$("#rotationIntervalNameTd").hide();
			$("#rotationIntervalValueTd").hide();
			$("#rotationIntervalNoDataTd").show();
		}
	}else if (strategyType == 3){
		childType.append("<c:forEach items='${timeStrategyChildTypeMap}' var='t'><option value='${t.key }'>${t.value}</option></c:forEach>"); 
		//显示策略分类
		$("#childTypeNameTd").show();
		$("#childTypeValueTd").show();
		$("#strategyTypeNoDataTd").hide();
		$("#frequencySet").hide();

		//投放次数
		$("#broadcastTimes").val("");
		//时间单位
		$("#timeUnit").val("");
		//显示时间详情
		//$("#timeSet").show();
		
		//隐藏分组轮换详情
		$("#groupRotationSet").hide();
		$("#rotationIntervalNameTd").hide();
		$("#rotationIntervalValueTd").hide();
		$("#rotationIntervalNoDataTd").show();
		$("#groupRotation").val(0);
		$("#rotationInterval").val("");
	}else if(strategyType == 4){
		childType.append("<c:forEach items='${frequencyStrategyChildTypeMap}' var='t'><option value='${t.key }'>${t.value}</option></c:forEach>"); 
		//显示策略分类
		$("#childTypeNameTd").show();
		$("#childTypeValueTd").show();
		$("#strategyTypeNoDataTd").hide();
		$("#frequencySet").show();
		//显示频率详情
		$("#timeSet").hide();
		
		//连续开始时间
		$("#beginTime").val("");
		//连续结束时间
		$("#endTime").val("");
		
		//每天开始时间
		$("#dayBeginTime").val("");
		//每天结束时间
		$("#dayEndTime").val("");
		
		//投放日期
		$("#days").val("");
		//投放周
		$("#weeks").multiselect("uncheckAll");
		//投放月份
		$("#months").multiselect("uncheckAll");
		//投放年份
		$("#years").multiselect("uncheckAll");
		//指定星期
		$("#weekDays").multiselect("uncheckAll");
		//指定月中的天
		$("#monthDays").multiselect("uncheckAll");
		
		//隐藏分组轮换详情
		$("#groupRotationSet").hide();
		$("#rotationIntervalNameTd").hide();
		$("#rotationIntervalValueTd").hide();
		$("#rotationIntervalNoDataTd").show();
		$("#groupRotation").val(0);
		$("#rotationInterval").val("");
	}else{
		//隐藏策略分类、频率详情以及时间详情 
		$("#childTypeNameTd").hide();
		$("#childTypeValueTd").hide();
		$("#frequencySet").hide();
		$("#timeSet").hide();
		
		//投放次数
		$("#broadcastTimes").val("");
		//时间单位
		$("#timeUnit").val("");
		
		//连续开始时间
		$("#beginTime").val("");
		//连续结束时间
		$("#endTime").val("");
		
		//每天开始时间
		$("#dayBeginTime").val("");
		//每天结束时间
		$("#dayEndTime").val("");
		
		//投放日期
		$("#days").val("");
		//投放周
		$("#weeks").multiselect("uncheckAll");
		//投放月份
		$("#months").multiselect("uncheckAll");
		//投放年份
		$("#years").multiselect("uncheckAll");
		//指定星期
		$("#weekDays").multiselect("uncheckAll");
		//指定月中的天
		$("#monthDays").multiselect("uncheckAll");
		
		//隐藏分组轮换详情
		$("#groupRotationSet").hide();
		$("#rotationIntervalNameTd").hide();
		$("#rotationIntervalValueTd").hide();
		$("#rotationIntervalNoDataTd").show();
		$("#groupRotation").val(0);
		$("#rotationInterval").val("");
	}
	
/* 	childType.bind('change',function(){
		onChildTypeChange(strategyType);
	}); */
	
	onChildTypeChange();
}

function onChildTypeChange(){

	var childType = $("#childType option:selected").val();
	var timeType = $("#timeType");
	
	 $("#timeType option").each(function(){
		 if (this.index > 0){
			 this.remove();
		 }
	});
	
	//按时间投放
	

		if (childType == '0'){
			//显示连续开始时间和结束时间
			$("#timeTr").show();
			//隐藏时间类型
			$("#timeTypeTr").hide();
			//显示时间详情
			$("#timeSet").show();
			
/* 			//隐藏指定星期和指定月中的天
			$("#weekDaysTr").hide();
			$("#monthDaysTr").hide(); */
			
			//投放日期
			$("#days").val("");
			//投放年份
			$("#years").multiselect("uncheckAll");

		}else if (childType == 1){
			timeType.append("<c:forEach items='${dayTimeTypeMap}' var='t'><option value='${t.key }'>${t.value}</option></c:forEach>"); 
			//隐藏连续开始时间和结束时间
			$("#timeTr").hide();
			//显示时间类型
			$("#timeTypeTr").show();
			//显示时间详情
			$("#timeSet").show();
			
/* 			//隐藏指定星期和指定月中的天
			$("#weekDaysTr").hide();
			$("#monthDaysTr").hide(); */
			//连续开始时间
			$("#beginTime").val("");
			//连续结束时间
			$("#endTime").val("");
		}else if (childType == 2){
			timeType.append("<c:forEach items='${weekTimeTypeMap}' var='t'><option value='${t.key }'>${t.value}</option></c:forEach>"); 
			//隐藏连续开始时间和结束时间
			$("#timeTr").hide();
			//显示时间类型
			$("#timeTypeTr").show();
			//显示时间详情
			$("#timeSet").show();
			
/* 			//隐藏指定星期和指定月中的天
			$("#weekDaysTr").hide();
			$("#monthDaysTr").hide(); */
			//连续开始时间
			$("#beginTime").val("");
			//连续结束时间
			$("#endTime").val("");
		}else if (childType == 3){
			timeType.append("<c:forEach items='${monthTimeTypeMap}' var='t'><option value='${t.key }'>${t.value}</option></c:forEach>"); 
			//隐藏连续开始时间和结束时间
			$("#timeTr").hide();
			//显示时间类型
			$("#timeTypeTr").show();
			//显示时间详情
			$("#timeSet").show();
			
/* 			//隐藏指定星期和指定月中的天
			$("#weekDaysTr").hide();
			$("#monthDaysTr").hide(); */
			//连续开始时间
			$("#beginTime").val("");
			//连续结束时间
			$("#endTime").val("");
		}else{
			//隐藏连续开始时间和结束时间
			$("#timeTr").hide();
			//隐藏时间类型
			$("#timeTypeTr").hide();
			//隐藏时间详情
			$("#timeSet").hide();
			
/* 			//隐藏指定星期和指定月中的天
			$("#weekDaysTr").hide();
			$("#monthDaysTr").hide(); */

			//连续开始时间
			$("#beginTime").val("");
			//连续结束时间
			$("#endTime").val("");
			
			//每天开始时间
			$("#dayBeginTime").val("");
			//每天结束时间
			$("#dayEndTime").val("");
			
			//投放日期
			$("#days").val("");
			//投放周
			$("#weeks").multiselect("uncheckAll");
			//投放月份
			$("#months").multiselect("uncheckAll");
			//投放年份
			$("#years").multiselect("uncheckAll");
			//指定星期
			$("#weekDays").multiselect("uncheckAll");
			//指定月中的天
			$("#monthDays").multiselect("uncheckAll");
		}	
	
	
/* 	timeType.bind('change',function(){
		onTimeTypeChange(childType);
	});
	 */
	onTimeTypeChange();
}


function onTimeTypeChange(){
	
	var strategyType = $("#strategyType option:selected").val();
	var childType = $("#childType option:selected").val();
	var timeType = $("#timeType option:selected").val();

		if (childType == "0"){
			//隐藏每天开始时间
			$("#dayTimeTr").hide();
			//隐藏投放日期
			$("#daysNameTd").hide();
			$("#daysValueTd").hide();
			//隐藏投放年份
			$("#yearNameTd").hide();
			$("#yearValueTd").hide();
			$("#timeTypeNoDataTd").show();
			
			//隐藏指定星期
			$("#weekDaysTr").hide();
			//隐藏投放周
			$("#weeksNameTd").hide();
			$("#weeksValueTd").hide();
			$("#weeksNoDataTd").show();
			
			//隐藏指定月中的天
			$("#monthDaysTr").hide();
			//隐藏投放月份
			$("#monthsNameTd").hide();
			$("#monthsValueTd").hide();
			$("#monthsNoDataTd").show();
			
			//每天开始时间
			$("#dayBeginTime").val("");
			//每天结束时间
			$("#dayEndTime").val("");
			
			//投放日期
			$("#days").val("");
			//投放周
			$("#weeks").multiselect("uncheckAll");
			//投放月份
			$("#months").multiselect("uncheckAll");
			//投放年份
			$("#years").multiselect("uncheckAll");
			//指定星期
			$("#weekDays").multiselect("uncheckAll");
			//指定月中的天
			$("#monthDays").multiselect("uncheckAll");
		}else if (childType == 1){
			if (timeType == "0"){
				//显示投放日期
				$("#daysNameTd").show();
				$("#daysValueTd").show();
				//隐藏投放年份
				$("#yearNameTd").hide();
				$("#yearValueTd").hide();
				$("#timeTypeNoDataTd").hide();
				
				//投放年份
				$("#years").multiselect("uncheckAll");
				
			}else if (timeType==1){
				//隐藏投放日期
				$("#daysNameTd").hide();
				$("#daysValueTd").hide();
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//投放日期
				$("#days").val("");
		
			}else{
				//隐藏投放日期
				$("#daysNameTd").hide();
				$("#daysValueTd").hide();
				//隐藏投放年份
				$("#yearNameTd").hide();
				$("#yearValueTd").hide();
				$("#timeTypeNoDataTd").show();
				
				//投放日期
				$("#days").val("");
				//投放年份
				$("#years").multiselect("uncheckAll");
			}
			
			//显示每天开始时间
			$("#dayTimeTr").show();
			
			//隐藏指定星期
			$("#weekDaysTr").hide();
			//隐藏投放周
			$("#weeksNameTd").hide();
			$("#weeksValueTd").hide();
			$("#weeksNoDataTd").show();
			
			//隐藏指定月中的天
			$("#monthDaysTr").hide();
			//隐藏投放月份
			$("#monthsNameTd").hide();
			$("#monthsValueTd").hide();
			$("#monthsNoDataTd").show();
			
			
			//投放周
			$("#weeks").multiselect("uncheckAll");
			//投放月份
			$("#months").multiselect("uncheckAll");
			//指定星期
			$("#weekDays").multiselect("uncheckAll");
			//指定月中的天
			$("#monthDays").multiselect("uncheckAll");
		}else if(childType == 2){
			
			if (timeType == "0"){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示指定星期
				$("#weekDaysTr").show();
				//显示投放周
				$("#weeksNameTd").show();
				$("#weeksValueTd").show();
				$("#weeksNoDataTd").hide();
				
			}else if (timeType == 1){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示指定星期
				$("#weekDaysTr").show();
				//隐藏投放周
				$("#weeksNameTd").hide();
				$("#weeksValueTd").hide();
				$("#weeksNoDataTd").show();
				
				//投放周
				$("#weeks").multiselect("uncheckAll");
			}else{
				//隐藏投放年份
				$("#yearNameTd").hide();
				$("#yearValueTd").hide();
				$("#timeTypeNoDataTd").show();
				
				//隐藏指定星期
				$("#weekDaysTr").hide();
				//隐藏投放周
				$("#weeksNameTd").hide();
				$("#weeksValueTd").hide();
				$("#weeksNoDataTd").show();
				
				//投放年份
				$("#years").multiselect("uncheckAll");
				
				//投放周
				$("#weeks").multiselect("uncheckAll");
				//指定星期
				$("#weekDays").multiselect("uncheckAll");
			}
			
			//显示每天开始时间
			$("#dayTimeTr").show();
			
			//隐藏投放日期
			$("#daysNameTd").hide();
			$("#daysValueTd").hide();
			
			//隐藏指定月中的天
			$("#monthDaysTr").hide();
			//隐藏投放月份
			$("#monthsNameTd").hide();
			$("#monthsValueTd").hide();
			$("#monthsNoDataTd").show();
			
			
			//投放日期
			$("#days").val("");
			//投放月份
			$("#months").multiselect("uncheckAll");
			//指定月中的天
			$("#monthDays").multiselect("uncheckAll");
		}else if(childType == 3){
			
			if (timeType == "0"){
				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示指定月中的天
				$("#monthDaysTr").show();
				//显示投放月份
				$("#monthsNameTd").show();
				$("#monthsValueTd").show();
				$("#monthsNoDataTd").hide();
			} else if (timeType == 1){

				//显示投放年份
				$("#yearNameTd").show();
				$("#yearValueTd").show();
				$("#timeTypeNoDataTd").hide();
				
				//显示指定月中的天
				$("#monthDaysTr").show();
				//隐藏投放月份
				$("#monthsNameTd").hide();
				$("#monthsValueTd").hide();
				$("#monthsNoDataTd").show();
				
				//投放月份
				$("#months").multiselect("uncheckAll");
			}else{
				//隐藏指定年份
				$("#yearNameTd").hide();
				$("#yearValueTd").hide();
				$("#timeTypeNoDataTd").show();
				
				//隐藏指定月中的天
				$("#monthDaysTr").hide();
				//隐藏投放月份
				$("#monthsNameTd").hide();
				$("#monthsValueTd").hide();
				$("#monthsNoDataTd").show();
				
				//投放年份
				$("#years").multiselect("uncheckAll");
				//指定月中的天
				$("#monthDays").multiselect("uncheckAll");
				//投放月份
				$("#months").multiselect("uncheckAll");

			}
			
			//显示每天开始时间
			$("#dayTimeTr").show();
			
			//隐藏投放日期
			$("#daysNameTd").hide();
			$("#daysValueTd").hide();
			
			//隐藏指定星期
			$("#weekDaysTr").hide();
			//隐藏投放周
			$("#weeksNameTd").hide();
			$("#weeksValueTd").hide();
			$("#weeksNoDataTd").show();
			
			
			
			//投放日期
			$("#days").val("");
			//指定星期
			$("#weekDays").multiselect("uncheckAll");
			//投放周
			$("#weeks").multiselect("uncheckAll");
		
	}else{
		
		//隐藏每天开始时间
		$("#dayTimeTr").hide();
		//隐藏投放日期
		$("#daysNameTd").hide();
		$("#daysValueTd").hide();
		//隐藏投放年份
		$("#yearNameTd").hide();
		$("#yearValueTd").hide();
		$("#timeTypeNoDataTd").show();
		
		//隐藏指定星期
		$("#weekDaysTr").hide();
		//隐藏投放周
		$("#weeksNameTd").hide();
		$("#weeksValueTd").hide();
		$("#weeksNoDataTd").show();
		
		//隐藏指定月中的天
		$("#monthDaysTr").hide();
		//隐藏投放月份
		$("#monthsNameTd").hide();
		$("#monthsValueTd").hide();
		$("#monthsNoDataTd").show();
		
		
		//每天开始时间
		$("#dayBeginTime").val("");
		//每天结束时间
		$("#dayEndTime").val("");
		
		//投放日期
		$("#days").val("");
		//投放年份
		$("#years").multiselect("uncheckAll");
		

		//指定星期
		$("#weekDays").multiselect("uncheckAll");
		//投放周
		$("#weeks").multiselect("uncheckAll");

		//指定月中的天
		$("#monthDays").multiselect("uncheckAll");
		//投放月份
		$("#months").multiselect("uncheckAll");
	}
}

function onGroupRotationChange(){
	var groupRotation = $("#groupRotation option:selected").val();
	
	if (groupRotation == '0'){
		$("#rotationIntervalNameTd").hide();
		$("#rotationIntervalValueTd").hide();
		$("#rotationIntervalNoDataTd").show();
		$("#rotationInterval").val("");
	}else if (groupRotation == 1){
		$("#rotationIntervalNameTd").show();
		$("#rotationIntervalValueTd").show();
		$("#rotationIntervalNoDataTd").hide();
	}

}


//选择素材 materialList
function selectMaterial(){	
	
	art.dialog.data('positionId',$("#advPositionId").val());
	art.dialog.open("${path}/advmgmt/mtrmgmt/materialSelect.action?positionId=4962&isShares=1", 
			{
				title: "选择素材", //'请选择频道'
				width: 1040,
				height: 440,
				lock: true,
				close:function(){
					var ht=art.dialog.data('html');
					if(ht!=undefined&&ht.length>0){
					$("#num_materials").html("已选素材"+art.dialog.data('num')+"个");
					$("#showMaterials").html(art.dialog.data('html'));					
					art.dialog.data('num','');
					art.dialog.data('html','');
					art.dialog.data('positionId','');
					}
				}
			});
}


function addStrategyType(){
	var strategyType = $("#strategyType option:selected").val();
	$("#div_"+strategyType).show();
}

function delStrategyType(){
	var strategyType = $("#strategyType option:selected").val();
	$("#div_"+strategyType).hide();
	$("#show_"+strategyType+"_1").html('');
	$("#show_"+strategyType+"_2").html('');
	$("#show_"+strategyType).html("");
}




</script>
</head>

<body>
<div class="title"><h2>策略信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${strategy.id}" />
<input type="hidden" name="createTime" id="createTime" value="${strategy.createTime }" />
<input type="hidden" name="auditStatus" id="auditStatus" value="${strategy.auditStatus}" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${strategy.operatorNo}" />

<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue" width="10%">策略名称</td>
    <td class="tdBlue" width="40%">
      <input name="strategyName" id="strategyName" maxlength="50" value="${strategy.strategyName}" >
      <div id="strategyName_tip" style="width:60%; float:right"></div>
     </td>  
     
     <td class="tdBlue" width="10%">策略类型</td>
    <td class="tdBlue" width="40%">
      <select name="strategyType" id="strategyType" class="form130px" >
		<option value="">--请选择--</option>
		<c:forEach items="${strategyType}" var="t">
			<option value="${t.key }" <c:if test="${strategy.strategyType==t.key }"> selected="selected" </c:if>>${t.value}</option>
		</c:forEach>
	  </select>
	  <input type="button" value="添加"  onclick="addStrategyType()"/>
	  <input type="button" value="删除"  onclick="delStrategyType()"/>
  </tr>
</table>
</fieldset>

<div style="display: none;" id="div_0">
<fieldset><legend>&nbsp;运营商策略</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
            <td width="35%" class="tdBlue2">
							运营商 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick=" selectCompany('show_0')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_0">
				<c:forEach items="${strategy.companyList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.companyNo }" name="companyIds" id="companyIds" /> ${mate.companyName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr> 
        </table>
        </table>
</fieldset>
</div>

<div style="display: none;" id="div_1">
<fieldset><legend>&nbsp;区域策略</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
            <td width="35%" class="tdBlue2">
							区域 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick=" selectCardRegion('show_1')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_1">
				<c:forEach items="${strategy.cardRegionList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="cardRegionIds" id="cardRegionIds" /> ${mate.regionName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr> 
        </table>
        </table>
</fieldset>
</div>


<div style="display: none;" id="div_2">
<fieldset><legend>&nbsp;终端类型</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
            <td width="35%" class="tdBlue2">
							终端类型 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectTerminal('show_2')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_2">
				<c:forEach items="${strategy.terminaTypeList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="terminal" id="terminal" /> ${mate.typeName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr> 
        </table>
        </table>
</fieldset>
</div>


<div style="display: none;" id="div_4">
<fieldset><legend>&nbsp;用户类型</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
            <td width="35%" class="tdBlue2">
							用户类型 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectReceiver('show_4_1')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_4_1">
				<c:forEach items="${strategy.receiverList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="receiverIds" id="receiverIds" /> ${mate.smartCardNo} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr> 
		   
		    <tr>
            <td width="35%" class="tdBlue2">
							用户组 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectReceiverGroup('show_4_2')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_4_2">
				<c:forEach items="${strategy.receiverGroupList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="receiverGroupIds" id="receiverGroupIds" /> ${mate.groupName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
		   
        </table>
        </table>
</fieldset>
</div>




<div style="display: none;" id="div_10">
<fieldset><legend>&nbsp;节目</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
            <td width="35%" class="tdBlue2">
							节目 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectProgram('show_10_1')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_10_1">
				<c:forEach items="${strategy.programList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="programIds" id="programIds" /> ${mate.programName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
		     <tr>
            <td width="35%" class="tdBlue2">
							节目组 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectProgramGroup('show_10_2')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_10_2">
				<c:forEach items="${strategy.programGroupList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="materialId" id="materialId" /> ${mate.groupName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
        </table>
</fieldset>
</div>
<div style="display: none;" id="div_9">
<fieldset><legend>&nbsp;栏目策略</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
               <tr>
            <td width="35%" class="tdBlue2">
							栏目 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectColumn('show_9_1')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_9_1">
				<c:forEach items="${strategy.columnList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="columnIds" id="columnIds" /> ${mate.columnName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
		     <tr>
            <td width="35%" class="tdBlue2">
							栏目组 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectColumnGroup('show_9_2')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_9_2">
				<c:forEach items="${strategy.columnGroupList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="columnGroupIds" id="columnGroupIds" /> ${mate.groupName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
        </table>
</fieldset>
</div>
<div style="display: none;" id="div_5">
<fieldset><legend>&nbsp;关联频道</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
            <td width="35%" class="tdBlue2">
							频道 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectChannel('show_5_1')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_5_1">
				<c:forEach items="${strategy.channelList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.channelNo }" name="channelIds" id="channelIds" /> ${mate.channelName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
		     <tr>
            <td width="35%" class="tdBlue2">
							频道组 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectChannelGroup('show_5_2')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_5_2">
				<c:forEach items="${strategy.channelGroupList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="channelGroupIds" id="channelGroupIds" /> ${mate.groupName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr>
        </table>
</fieldset>
</div>
<div style="display: none;" id="div_12">
<fieldset><legend>&nbsp;关键词策略</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
            <td width="35%" class="tdBlue2">
							关键词 <input name="button" type="button" 
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectKeyword('show_12')" /> <div id="materialIdTip" style="width: 70%; float: right;"></div>
			</td>
			<td class="tdBlue">														
							<div class="checkboxitems" id="show_12">
				<c:forEach items="${strategy.keywordList}" var="mate">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${mate.id }" name="keywordIds" id="keywordIds" /> ${mate.keyword} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
		   </td>
		   </tr> 
        </table>
        </table>
</fieldset>
</div>
<div   id="div_20">
<fieldset  ><legend>&nbsp;时间策略</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td class="tdBlue"  colspan='2'>时间分类</td>
	<td class="tdBlue"  colspan='2'>
	  	 <select name="childType" id="childType" class="form130px" onChange="onChildTypeChange()">
		       <option value="">--请选择--</option>
		       <c:forEach items='${timeStrategyChildTypeMap}' var='t'>
		       <option value='${t.key }'  <c:if test="${t.key==strategy.childType}"> selected="selected" </c:if> >${t.value}</option>
		       </c:forEach>
	     </select>
	 </td>
  </tr>
  <tr style="display: none;" id="timeTr">
    <td class="tdBlue" width="10%">连续开始时间</td>
    <td class="tdBlue" width="40%">
      <input name="beginTime" id="beginTime" maxlength="20" value="${strategy.beginTime}" size="22" readOnly/>
     </td>
    <td class="tdBlue" width="10%">连续结束时间</td>
    <td class="tdBlue" width="40%">
      <input name="endTime" id="endTime" maxlength="20" value="${strategy.endTime}" size="22" readOnly/> 
     </td>
  </tr>
   <tr style="display: none;" id="timeTypeTr">
      <td class="tdBlue" width="10%">时间类型</td>
    <td class="tdBlue" width="40%">
	  	 <select name="timeType" id="timeType" class="form130px" onChange="onTimeTypeChange()">
		<option value="">--请选择--</option>
	   </select>
     </td>
    <td class="tdBlue" width="10%" style="display: none;" id="yearNameTd">投放年份</td>
    <td class="tdBlue" width="40%" style="display: none;" id="yearValueTd">
       <select id="years" name="years" multiple="multiple">  
			<c:forEach items="${yearMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
    </td>
    <td class="tdBlue" width="10%" style="display: none;" id="daysNameTd">投放日期</td>
    <td class="tdBlue" width="40%" style="display: none;" id="daysValueTd">
       <input name="days" id="days" maxlength="200" value="${strategy.days}" size="22" readOnly/>
    </td>
    <td class="tdBlue" colspan="2" id="timeTypeNoDataTd"></td>
  </tr>
  
   <tr style="display: none;" id="weekDaysTr">
    <td class="tdBlue" width="10%" >指定星期</td>
    <td class="tdBlue" width="40%">
		<select id="weekDays" name="weekDays" multiple="multiple">  
			<c:forEach items="${weekDayMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
        <div style="width:50%; float:right;color:green">请选择周中的星期，默认包含星期一至星期日</div>
     </td>
    <td class="tdBlue" style="display: none;" id="weeksNameTd">投放周</td>
    <td class="tdBlue" style="display: none;" id="weeksValueTd">
		<select id="weeks" name="weeks" multiple="multiple">  
			<c:forEach items="${weekMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
    </td>
    <td class="tdBlue" colspan="2" id="weeksNoDataTd"></td>
  </tr>
  
  <tr style="display: none;" id="monthDaysTr">

    <td class="tdBlue" width="10%" >指定月中的天数</td>
    <td class="tdBlue" width="40%">
		<select id="monthDays" name="monthDays" multiple="multiple">  
			<c:forEach items="${monthDayMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
        <div style="width:50%; float:right;color:green">请选择月中的天数，默认对应月份的所有天数</div>
     </td>
         <td class="tdBlue" style="display: none;" id="monthsNameTd">投放月份</td>
    <td class="tdBlue" style="display: none;" id="monthsValueTd">
        <select id="months" name="months" multiple="multiple" value="${strategy.months}">  
			<c:forEach items="${monthMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
    </td>
    <td class="tdBlue" id="monthsNoDataTd" colspan="2"></td>
    </tr>
   <tr style="display: none;" id="dayTimeTr">
    <td class="tdBlue">每天开始时间</td>
    <td class="tdBlue">
        <input name="dayBeginTime" id="dayBeginTime" maxlength="8" value="${strategy.dayBeginTime}" readOnly/>
         <div style="width:70%; float:right;color:green">请选择开始时间，默认以"00:00:00"为开始时间</div>
      </td>
    <td class="tdBlue">每天结束时间</td>
    <td class="tdBlue">
         <input name="dayEndTime" id="dayEndTime" maxlength="8" value="${strategy.dayEndTime}" readOnly/>
         <div style="width:70%; float:right; color:green">请选择结束时间，默认以"23:59:59"为结束时间</div>
     </td>
  </tr>
</table>
</fieldset>
</div>
<%-- <fieldset><legend>&nbsp;基本信息</legend>
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
	<td class="tdBlue" width="10%">策略类型</td>
    <td class="tdBlue" width="40%">
      <select name="strategyType" id="strategyType" class="form130px" onChange="onStrategyTypeChange()">
		<option value="">--请选择--</option>
		<c:forEach items="${strategyTypeMap}" var="t">
			<option value="${t.key }" <c:if test="${strategy.strategyType==t.key }"> selected="selected" </c:if>>${t.value}</option>
		</c:forEach>
	 </select>
	 <div id="strategyType_tip" style="width:60%; float:right"></div>
	</td>
	<td class="tdBlue" style="display: none;" id="childTypeNameTd">策略分类</td>
	<td class="tdBlue" style="display: none;" id="childTypeValueTd">
	  	 <select name="childType" id="childType" class="form130px" onChange="onChildTypeChange()">
		       <option value="">--请选择--</option>
	     </select>
	 </td>
	  <td class="tdBlue" colspan="2" id="strategyTypeNoDataTd"></td>
  </tr>


</table>
</fieldset>

<fieldset style="display: none;" id="groupRotationSet"><legend>&nbsp;频道分组轮换详情</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
	<td class="tdBlue" width="10%">分组轮换</td>
    <td class="tdBlue" width="40%">
      <select name="groupRotation" id="groupRotation" class="form130px" onChange="onGroupRotationChange()">
		<c:forEach items="${groupRotationMap}" var="r">
			<option value="${r.key }" <c:if test="${strategy.groupRotation==r.key }"> selected="selected" </c:if>>${r.value}</option>
		</c:forEach>
	 </select>
	</td>
    <td class="tdBlue" width="10%" style="display: none;" id="rotationIntervalNameTd">轮换间隔时间（小时）</td>
    <td class="tdBlue" width="40%" style="display: none;" id="rotationIntervalValueTd">
       <input name="rotationInterval" id="rotationInterval" maxlength="4" value="${strategy.rotationInterval}" >
	</td>
	<td class="tdBlue" colspan="2" id="rotationIntervalNoDataTd"></td>
  </tr>
</table>
</fieldset>

<fieldset style="display: none;" id="frequencySet"><legend>&nbsp;频率详情</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
    <td class="tdBlue" width="10%">投放次数</td>
    <td class="tdBlue" width="40%"><input name="broadcastTimes" id="broadcastTimes" maxlength="8" value="${strategy.broadcastTimes}" >
	</td>
    <td class="tdBlue" width="10%" style="display: none;" id="timeUnitNameTd">时间单位</td>
    <td class="tdBlue" width="40%" style="display: none;" id="timeUnitValueTd">
    <select name="timeUnit" id="timeUnit" class="form130px">
		<option value="">--请选择--</option>
		<c:forEach items="${timeUnitMap}" var="u">
			<option value="${u.key }" <c:if test="${strategy.timeUnit==u.key }"> selected="selected" </c:if>>${u.value}</option>
		</c:forEach>
	   </select>
	</td>
	<td class="tdBlue" colspan="2" id="timeUnitNoDataTd"></td>
  </tr>
</table>
</fieldset>

<fieldset style="display: none;" id="timeSet"><legend>&nbsp;时间详情</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr style="display: none;" id="timeTr">
    <td class="tdBlue" width="10%">连续开始时间</td>
    <td class="tdBlue" width="40%">
      <input name="beginTime" id="beginTime" maxlength="20" value="${strategy.beginTime}" size="22" readOnly/>
     </td>
    <td class="tdBlue" width="10%">连续结束时间</td>
    <td class="tdBlue" width="40%">
      <input name="endTime" id="endTime" maxlength="20" value="${strategy.endTime}" size="22" readOnly/> 
     </td>
  </tr>
   <tr style="display: none;" id="timeTypeTr">
      <td class="tdBlue" width="10%">时间类型</td>
    <td class="tdBlue" width="40%">
	  	 <select name="timeType" id="timeType" class="form130px" onChange="onTimeTypeChange()">
		<option value="">--请选择--</option>
	   </select>
     </td>
    <td class="tdBlue" width="10%" style="display: none;" id="yearNameTd">投放年份</td>
    <td class="tdBlue" width="40%" style="display: none;" id="yearValueTd">
       <select id="years" name="years" multiple="multiple">  
			<c:forEach items="${yearMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
    </td>
    <td class="tdBlue" width="10%" style="display: none;" id="daysNameTd">投放日期</td>
    <td class="tdBlue" width="40%" style="display: none;" id="daysValueTd">
       <input name="days" id="days" maxlength="200" value="${strategy.days}" size="22" readOnly/>
    </td>
    <td class="tdBlue" colspan="2" id="timeTypeNoDataTd"></td>
  </tr>
  
   <tr style="display: none;" id="weekDaysTr">
    <td class="tdBlue" width="10%" >指定星期</td>
    <td class="tdBlue" width="40%">
		<select id="weekDays" name="weekDays" multiple="multiple">  
			<c:forEach items="${weekDayMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
        <div style="width:50%; float:right;color:green">请选择周中的星期，默认包含星期一至星期日</div>
     </td>
    <td class="tdBlue" style="display: none;" id="weeksNameTd">投放周</td>
    <td class="tdBlue" style="display: none;" id="weeksValueTd">
		<select id="weeks" name="weeks" multiple="multiple">  
			<c:forEach items="${weekMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
    </td>
    <td class="tdBlue" colspan="2" id="weeksNoDataTd"></td>
  </tr>
  
  <tr style="display: none;" id="monthDaysTr">

    <td class="tdBlue" width="10%" >指定月中的天数</td>
    <td class="tdBlue" width="40%">
		<select id="monthDays" name="monthDays" multiple="multiple">  
			<c:forEach items="${monthDayMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
        <div style="width:50%; float:right;color:green">请选择月中的天数，默认对应月份的所有天数</div>
     </td>
         <td class="tdBlue" style="display: none;" id="monthsNameTd">投放月份</td>
    <td class="tdBlue" style="display: none;" id="monthsValueTd">
        <select id="months" name="months" multiple="multiple" value="${strategy.months}">  
			<c:forEach items="${monthMap}" var="m" > 
				<option value="${m.key }">${m.value }</option>
			</c:forEach> 
        </select>
    </td>
    <td class="tdBlue" id="monthsNoDataTd" colspan="2"></td>
    </tr>
   <tr style="display: none;" id="dayTimeTr">
    <td class="tdBlue">每天开始时间</td>
    <td class="tdBlue">
        <input name="dayBeginTime" id="dayBeginTime" maxlength="8" value="${strategy.dayBeginTime}" readOnly/>
         <div style="width:70%; float:right;color:green">请选择开始时间，默认以"00:00:00"为开始时间</div>
      </td>
    <td class="tdBlue">每天结束时间</td>
    <td class="tdBlue">
         <input name="dayEndTime" id="dayEndTime" maxlength="8" value="${strategy.dayEndTime}" readOnly/>
         <div style="width:70%; float:right; color:green">请选择结束时间，默认以"23:59:59"为结束时间</div>
     </td>
  </tr>




</table>
</fieldset> --%>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
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
