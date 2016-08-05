function pubMgmt(type, resourceId, path){
	var templateId = $("#templateId" + resourceId).val();
	if(templateId==null||templateId=="") {
		var msg;
		if(type==ENTITY_TYPE_ALBUM) {
			msg = "不能发布没有设置模板的相册";
		}
		else if(type==ENTITY_TYPE_ARTICLE) {
			msg = "不能发布没有设置模板的文章";
		}
		else {
			msg = "模板没设置? 请通知管理员....";
		}
		art.dialog.alert(msg);
		return;
	}
	art.dialog.open(path + "?type=" + type + "&resourceId=" + resourceId, 
			{
				title: "发布管理", 
				width: 980,
				height: 450,
				lock: true,close:function(){
					$("#btnQuery").click();
				}

			});
}

function albumSinglePublish(path,parentType,resourceId){
	var sets = $("input[name='parentIds']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}

	
	var parentIds = "";
	for (var i=0; i < sets.length; i++){
		parentIds = parentIds + sets[i].value + ',';
	}
	
	parentIds = parentIds.substring(0,parentIds.length - 1);
	art.dialog.open(path + "?parentType=" + parentType + "&parentIds=" + parentIds + "&resourceId=" + resourceId, 
			{
				title: "发布相册", 
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

function articleSinglePublish(path,resourceId){
	var sets = $("input[name='parentIds']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}

	
	var parentIds = "";
	for (var i=0; i < sets.length; i++){
		parentIds = parentIds + sets[i].value + ',';
	}
	
	parentIds = parentIds.substring(0,parentIds.length - 1);
	art.dialog.open(path + "?parentIds=" + parentIds + "&resourceId=" + resourceId, 
			{
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

