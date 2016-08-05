function topicPublish(url) {
	var activeOpNo = $("#activeOpNo").val();
	var sets = $("input[name='topicIds']:checked");
	if (sets.length <= 0) {
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}
	for ( var i = 0; i < sets.length; i++) {
		var createdBy = $("#createdBy" + sets[i].value).val();
		if(activeOpNo != -1 && createdBy != activeOpNo){
			art.dialog.alert("无权操作分配的专题！");
			return false;
		}
	}
	for ( var i = 0; i < sets.length; i++) {
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 2) {
			art.dialog.alert("发布的专题必须为【审核通过】状态！");
			return false;
		}
	}

	for ( var i = 0; i < sets.length; i++) {
		var templateId = $("#templateId" + sets[i].value).val();
		var type = $("#type" + sets[i].value).val();
		if (type == 1) {
			if (templateId == null || templateId == '') {
				art.dialog.alert("生成页面类型的专题没有关联模板，不能发布！");
				return false;
			}
		}
	}

	var topicIds = "";
	for ( var i = 0; i < sets.length; i++) {
		topicIds = topicIds + sets[i].value + ',';
	}

	topicIds = topicIds.substring(0, topicIds.length - 1);
	art.dialog.open(url + "?topicIds=" + topicIds, {
		title : "发布专题",
		width : 600,
		height : 400,
		lock : true,
		close : function() {
			$("#btnQuery").click();
		},
		okVal : "确定",
		ok : function() {
			var iframe = this.iframe.contentWindow;
			iframe.publish();
			return false;
		},
		cancel : function() {
			return true;
		}

	});
}

function topicUnPublish(url) {
	var activeOpNo = $("#activeOpNo").val();
	var sets = $("input[name='topicIds']:checked");
	if (sets.length <= 0) {
		art.dialog.alert("请选择要取消的选项！");
		return false;
	}
	for ( var i = 0; i < sets.length; i++) {
		var createdBy = $("#createdBy" + sets[i].value).val();
		if(activeOpNo != -1 && createdBy != activeOpNo){
			art.dialog.alert("无权操作分配的专题！");
			return false;
		}
	}
	for ( var i = 0; i < sets.length; i++) {
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 4) {
			art.dialog.alert("取消发布的专题必须为【发布】状态！");
			return false;
		}
	}

	var topicIds = "";
	for ( var i = 0; i < sets.length; i++) {
		topicIds = topicIds + sets[i].value + ',';
	}

	topicIds = topicIds.substring(0, topicIds.length - 1);
	art.dialog.open(url + "?topicIds=" + topicIds, {
		title : "取消发布专题",
		width : 350,
		height : 120,
		lock : true,
		close : function() {
			$("#btnQuery").click();
		},
		okVal : "确定",
		ok : function() {
			var iframe = this.iframe.contentWindow;
			iframe.unPublish();
			return false;
		},
		cancel : function() {
			return true;
		}

	});
}

function pubMgmt(topicId, url) {

	art.dialog.open(url + "?topicId=" + topicId, {
		title : "发布管理",
		width : 600,
		height : 400,
		lock : true,
		close : function() {
			$("#btnQuery").click();
		},
		okVal : "确定",
		ok : function() {
			var iframe = this.iframe.contentWindow;
			iframe.publish();
			return false;
		},
		cancel : function() {
			return true;
		}

	});
}