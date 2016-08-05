function columnAlbumNoSelect(url){
	var columnId = $("#columnId").val();
	art.dialog.open(url + "?columnId=" + columnId, 
			{
				title: "相册管理", 
				width: 980,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.saveAlbum();
					return false;
				},
				cancel:function(){
					return true;
				}

			});
}