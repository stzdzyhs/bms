function companyCardRegionNoSelect(url){
	var companyNo = $("#companyNo").val();
	art.dialog.open(url + "?companyNo=" + companyNo, 
			{
				title: "智能卡区域管理", 
				width: 980,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.saveCardRegion();
					return false;
				},
				cancel:function(){
					return true;
				}

			});
}