function companyOperNoDist(url){
	var companyNo = $("#companyNo").val();
	var isSelf = $("#isSelf").val();
	art.dialog.open(url + "?companyNo=" + companyNo + "&isSelf=" + isSelf, 
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

function resourceOperNoAlloc(url){
	var createOperNo = $("#createOperNo").val();
	var resourceId = $("#resourceId").val();
	var type = $("#type").val();
	art.dialog.open(url + "?resourceId=" + resourceId + "&type=" + type + "&createOperNo=" + createOperNo, 
			{
				title: "分配管理", 
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