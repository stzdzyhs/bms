function albumPublish(path){
	var sets = $("input[name='albumNos']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择要发布的选项！");
		return false;
	}
	
	for (var i=0; i<sets.length; i++){
		var oldStatus = $("#oldStatus" + sets[i].value).val();
		if (oldStatus != 2 && oldStatus != 4){
			art.dialog.alert("发布的相册必须为【审核通过】状态！");
			return false;
		}
	}
	
	for (var i=0; i<sets.length; i++){
		var templateId = $("#templateId" + sets[i].value).val();
		var captureFlag = $("#captureFlag" + sets[i].value).val();
		if (captureFlag == 0 && (templateId == null || templateId == '')){
			art.dialog.alert("相册没有关联模板，不能发布！");
			return false;
		}
	}
	
	var captureFlag = $("#captureFlag" + sets[0].value).val();
	for (var i=0; i<sets.length - 1 ; i++){
	     captureFlag = $("#captureFlag" + sets[i].value).val();
		 var captureFlag2 = $("#captureFlag" + sets[i+1].value).val();
		if (captureFlag != captureFlag2){
				art.dialog.alert("不能同时发布普通相册和截图相册！");
				return false;
			 }
	}
	
	if (captureFlag == 1){
		art.dialog.confirm('你确认发布操作？', function(){
			var options = {
				url: path + "/picmgmt/album/albumPublish.action",
				dataType: 'html',
				beforeSend: function() {
					art.dialog.through({
						id: 'broadcastLoading',
						title: "正在发布相册",
					    content: '<img src="${path }/images/08.gif" />',
					    lock: true
					});
				},
				error: function(a, b) {
					art.dialog.list['broadcastLoading'].close();
					art.dialog.alert("发布失败！");
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true" || rsobj.result=="true"){
						art.dialog.alert('发布成功！',goBack);

					}else{
						if (rsobj.desc == 'noTopic'){
							art.dialog.alert("发布失败，未能找到截图专题！");
						}else{
						    art.dialog.alert("发布失败！");
						}

					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
	}else if (captureFlag == 0){
		var albumNos = "";
		for (var i=0; i < sets.length; i++){
			albumNos = albumNos + sets[i].value + ',';
		}
		
		albumNos = albumNos.substring(0,albumNos.length - 1);
		art.dialog.open(path + "/picmgmt/album/toAlbumPublish.action?albumNos=" + albumNos, 
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
	
}

