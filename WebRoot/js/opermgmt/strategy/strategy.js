function strategyResourceNoSelect(url, title){
	var strategyId = $("#strategyId").val();
	art.dialog.open(url + "?strategyId=" + strategyId, 
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
}

function strategyOperNoDist(url){
	var strategyId = $("#strategyId").val();
	var isSelf = $("#isSelf").val();
	art.dialog.open(url + "?strategyId=" + strategyId + "&isSelf=" + isSelf, 
			{
				title: "用户管理", 
				width: 980,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.saveUser();
					return false;
				},
				cancel:function(){
					return true;
				}

			});
}

function strategyOperNoShare(url){
	var strategyId = $("#strategyId").val();
	var permissions = $("#permissions").val();
	art.dialog.open(url + "?strategyId=" + strategyId + "&permissions=" + permissions, 
			{
				title: "用户管理", 
				width: 980,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.saveUser();
					return false;
				},
				cancel:function(){
					return true;
				}

			});
}


/////////////////////////////////////////////////////////////////////////////
//ajax part
/////////////////////////////////////////////////////////////////////////////

function findDispatchStrategyById(strategyId, succFunc) {
	$.ajax({
		url : "../../opermgmt/strategy/findDispatchStrategyById.action",
		type : "POST",
		dataType : "json",
		data : {
			strategyId : strategyId
		},

		success : function(data) {
			if (isResultSucc(data)) {
				// art.dialog.alert("保存成功！", refreshThisPage);
				if (succFunc != null) {
					succFunc(data);
				}
			} 
			else {
				if (data.desc == null) {
					data.desc = "";
				}
				art.dialog.alert("策略失败！" + data.desc);
			}
		},
		error : function(a, b) {
			art.dialog.alert("策略失败！");
		},
		complete:function() {
		}
	});
}

function getStrategyAllData(strategyId, succFunc) {
	$.ajax({
		url : "../../opermgmt/strategy/getStrategyAllData.action",
		type : "POST",
		dataType : "json",
		data : {
			strategyId : strategyId
		},

		success : function(data) {
			if (isResultSucc(data)) {
				if (succFunc != null) {
					succFunc(data);
				}
			} 
			else {
				if (data.desc == null) {
					data.desc = "";
				}
				art.dialog.alert("策略失败！" + data.desc);
			}
		},
		error : function(a, b) {
			art.dialog.alert("策略失败！");
		}
	});
}

function saveStrategyResource(strategyId, type, resourceIds, succFunc) {
	$.ajax({
		url : "../../opermgmt/strategy/saveStrategyResource.action",
		type : "POST",
		dataType : "json",
		data : {
			strategyId : strategyId,
			type : type,
			resourceIds : resourceIds
		},

		success : function(data) {
			art.dialog.list['broadcastLoading'].close();
			if (isResultSucc(data)) {
				// art.dialog.alert("保存成功！", refreshThisPage);
				if (succFunc != null) {
					succFunc();
				}
			}
			else {
				if (rsobj.desc == null) {
					rsobj.desc = "";
				}
				art.dialog.alert("删除失败！" + rsobj.desc);
			}
		},
		error : function(a, b) {
			art.dialog.list['broadcastLoading'].close();
			art.dialog.alert("删除失败！");
		},
		complete : function() {
		}
	});
}

// resourceIds: if resourceIds null, will delete all strateId,type map
function deleteStrategyResource(strategyId, type, resourceIds, succFunc) {
	$.ajax({
		url : "../../opermgmt/strategy/deleteStrategyResource.action",
		type : "POST",
		dataType : "json",
		data : {
			strategyId : strategyId,
			type : type,
			resourceIds : resourceIds
		},

		success : function(data) {
			//art.dialog.list['broadcastLoading'].close();
			if (isResultSucc(data)) {
				// art.dialog.alert("保存成功！", refreshThisPage);
				if (succFunc != null) {
					succFunc();
				}
			}
			else {
				if (data.desc == null) {
					data.desc = "";
				}
				art.dialog.alert("删除失败！" + data.desc);
			}
		},
		error : function(a, b) {
			//art.dialog.list['broadcastLoading'].close();
			art.dialog.alert("删除失败！");
		},
		complete : function() {
		}
	});
}

function deleteStrategyResourceMapById(id, succFunc) {
	$.ajax({
		url : "../../opermgmt/strategy/deleteStrategyResourceMapById.action",
		type : "POST",
		dataType : "json",
		data : {
			id:id
		},

		success : function(data) {
			if (isResultSucc(data)) {
				// art.dialog.alert("保存成功！", refreshThisPage);
				if (succFunc != null) {
					succFunc();
				}
			}
			else {
				if (data.desc == null) {
					data.desc = "";
				}
				art.dialog.alert("删除失败！" + data.desc);
			}
		},
		error : function(a, b) {
			art.dialog.alert("删除失败！");
		},
		complete : function() {
		}
	});
}
