function browseFtpServer(ftpServerId,url){
	var albumNo = $("#albumNo").val();
	var param = "?ftpServerId="+ftpServerId + "&albumNo=" + albumNo;
	art.dialog.open(url + param, 
			{
				title: "FTP服务器文件列表", 
				width: 900,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.selectFiles();
					return false;
				}
			});
}

