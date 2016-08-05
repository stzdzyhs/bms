function columnPublish(url){
	var sets = $("input[name='rtId']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}
	
	for (var i=0; i<sets.length; i++){
		var allocRes = $("#allocRes" + sets[i].value).val();
		if(allocRes == 1){
			msg = String.format("无权操作分配的资源", name);
			art.dialog.alert(msg);
			return false;
		}
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 2){
			art.dialog.alert("发布的版块必须为【审核通过】状态！");
			return false;
		}
	}
	
	for (var i=0; i<sets.length; i++){
		var templateId = $("#templateId" + sets[i].value).val();
		if (templateId == null || templateId == ''){
			art.dialog.alert("版块没有关联模板，不能发布！");
			return false;
		}
	}
	
	var columnNos = "";
	for (var i=0; i < sets.length; i++){
		columnNos = columnNos + sets[i].value + ',';
	}
	
	columnNos = columnNos.substring(0,columnNos.length - 1);
	art.dialog.open(url + "?columnNos=" + columnNos, 
			{
				title: "发布版块", 
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