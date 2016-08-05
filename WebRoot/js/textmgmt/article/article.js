
function articlePublish(url){
	var sets = $("input[name='rtId']:checked");
	if (sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}

	var i;
	for (i=0; i<sets.length; i++) {
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != AUDIT_STATUS_AUDIT_PASS && oldStatus != AUDIT_STATUS_PUBLISH){
			art.dialog.alert("发布的文章必须为【审核通过/发布】状态！");
			return false;
		}
	}
	
	for (i=0; i<sets.length; i++){
		var templateId = $("#templateId" + sets[i].value).val();
		if (templateId == null || templateId == ''){
			art.dialog.alert("文章没有关联模板，不能发布！");
			return false;
		}
	}
	
	var articleNos = "";
	for (i=0; i < sets.length; i++){
		articleNos = articleNos + sets[i].value + ',';
	}
	articleNos = articleNos.substring(0,articleNos.length - 1);
	
	art.dialog.open(url + "?articleNos=" + articleNos, {
		title: "发布文章", 
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
