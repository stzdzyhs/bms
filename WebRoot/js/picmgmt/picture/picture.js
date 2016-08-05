function picturePublish(url){
	var sets = $("input[name='pictureIds']:checked");
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