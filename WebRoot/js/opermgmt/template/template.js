function preview(path,resourceId,type){
	var options = {
			url: path + "/opermgmt/template/preview.action?resourceId=" + resourceId + "&type=" + type,
			dataType: 'html',
			beforeSend: function() {
				art.dialog.through({
					id: 'broadcastLoading',
					title: "正在加载...",
				    content: '<img src="' + path + '/images/08.gif" />',
				    lock: true
				});
			},
			error: function(a, b) {
				art.dialog.list['broadcastLoading'].close();
				art.dialog.alert("预览失败！");
			},
			success: function(data) {
				art.dialog.list['broadcastLoading'].close();
				eval("var rsobj = "+data+";");
				if(rsobj.result=="true"){
					window.open(path + "/" + rsobj.file);   
				}else{
					art.dialog.alert("预览失败！");
				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
}