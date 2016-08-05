function toEditPage(url, type){

	var param = "";
	if (type == 0){
		var regionTree = $.fn.zTree.getZTreeObj("tree");
		var nodes = regionTree.getSelectedNodes();
		if (nodes == ''){
			art.dialog.alert("请选择需要添加区域的节点！");
			return;
		}
		
		if (nodes.length > 1){
			art.dialog.alert("只能选择一个节点！");
			return;
		}
		
		param = "?parentId=" + nodes[0].id;
	}else if (type == 1){
		var regionTree = $.fn.zTree.getZTreeObj("tree");
		var nodes = regionTree.getSelectedNodes();
		if (nodes == ''){
			art.dialog.alert("请选择需要修改的节点！");
			return;
		}
		
		if (nodes.length > 1){
			art.dialog.alert("只能选择一个节点！");
			return;
		}
		
		param = "?regionId=" + nodes[0].id;
	}
	
	var regionTree = $.fn.zTree.getZTreeObj("tree");
	var parentNode = type == 0 ? regionTree.getSelectedNodes()[0] : regionTree.getSelectedNodes()[0].getParentNode();
	art.dialog.open(url + param, 
			{
						title: type == 0 ? "新增" : "修改", 
						width: 750,
						height: 400,
						lock: true,
						okVal:"保存",
						ok:function(){
							var iframe = this.iframe.contentWindow;
							iframe.saveRegion();
							regionTree.reAsyncChildNodes(parentNode, "refresh");
							return false;
						},
						cancel:function(){
							return true;
						}

			});
	

}

function regionMultiSelect(url){
	var regionId = $("#regionId").val();
	var param = regionId != '' && regionId != null ? "?regionId="+regionId : '';
	art.dialog.open(url + param, 
			{
				title: "区域管理", 
				width: 900,
				height: 450,
				lock: true,
				close:function(){
					$("#btnQuery").click();
				},
				okVal:"保存",
				ok:function(){
					var iframe = this.iframe.contentWindow;
					iframe.saveRegion();
					return false;
				}
			});
}

function formatRegionCode(regionCode){
	if (regionCode == null || regionCode == ''){
		return null;
	}
	
	if (regionCode.length >= 4){
		return regionCode;
	}
	
	var len = regionCode.length;
	for (var i = 0; i < 4 - len; i++){
		regionCode = "0" + regionCode;
	}
	return regionCode;
}