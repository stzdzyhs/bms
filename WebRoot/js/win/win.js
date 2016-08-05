String.prototype.trim = function () {
return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
}
//=====弹出选择频道=====
//弹出选择频道窗口
var Win = {
    width : 750,
    height : 440,
	showPlaceId : "showChannels",
	checkedBoxName : "channelNo", /*父窗口checkbox的name,默认是channelNo,可通过openWin方法注册进来*/
	actionName:"",
	dataKey:"ChannelData" /*存放数据的key*/
};

//默认可直接调用方法Win.register()，如果需要自己处理点击确定按钮后的方法，可以传递回调方法====
Win.openWin = function (actionName,title,checkedBoxName,placeId,getChannelNoF,getChannelNameF,callbackFun,getSelectChannelsF,dataName,winWidth,winHeight){
	Win.width = winWidth || this.width;
	Win.height = winHeight || this.height;
	Win.setCheckBoxName(checkedBoxName);
	Win.showPlaceId = placeId || this.showPlaceId;
	Win.dataKey = dataName || this.dataKey ;
	if(typeof(getSelectChannelsF)=='function'){
		getSelectChannelsF();
	}else
		Win.getSelectChannels();
	/*注册getChannelNo方法*/
	if(typeof(getChannelNoF)=='function'){
		Win.getChannelNo = getChannelNoF;
	}
	/*注册getChannelNameF方法*/
	if(typeof(getChannelNameF)=='function'){
		Win.getChannelName = getChannelNameF;
	}
	

	art.dialog.open(actionName, //'business/epgWin.action'
		{
			title: title, //'请选择频道'
			width: Win.width,
			height: Win.height,
			lock: true,
			button: [
				{
					name: '确定',
					callback: function () {
						var iframe = this.iframe.contentWindow;//获得弹出窗口元素	
						if(typeof(callbackFun)!='function'){
							callbackFun = Win.repainChannel;
						}
						callbackFun(art.dialog.data(Win.dataKey));					
						Win.clearData();
						return true;
					},
					focus: true
				},
				{
					name: '取消',
					callback: function () {
						Win.clearData();
						return true;
					}
				}
			]
		});
	return this;
}
//额外增加的自定义回调方法
Win.addCallBack = function(callback){
	this.extFun = callback || this.extFun;
}
//获取已选择频道，用于传递到弹出频道窗口内容
Win.getSelectChannels = function () {
	Win.cleanSelectChannels();
	var json = "";
	$("input[name='"+Win.checkedBoxName+"']:checked").each(function(){
		if(json!=""){
			json += ",";
		}
		json += "{channelNo: '"+Win.getChannelNo(this)+"',channelName:'"+Win.getChannelName(this)+"'}";
	});
	if(json!=""){
		json = "({items:["+json+"]})";
	}
	art.dialog.data(Win.dataKey, json);
}
//删除当前页面中已经取消选中的项
Win.cleanSelectChannels = function () {
	var items = document.getElementsByName(Win.checkedBoxName);
	for (var i=(items.length-1); i>=0; i--){
		if(!items[i].checked){
			$(items[i]).parent().parent().remove();
		}
	}
}
//重新刷新已选择频道内容
Win.repainChannel = function (json){
	var html = "", delItems=null;
	if(json!=null && typeof(json)!='undefined' && json!=""){
		$("#"+Win.showPlaceId ).empty();
		var dgData = eval(json);
		//if($("#channelType").length<=0 || $("#channelType").val()!=dgData.channelType){
		//	$("#"+Win.showPlaceId).html('<input type="hidden" name="channelType" id="channelType" value="'+dgData.channelType+'" />');
		//}
		var items = dgData.items;//所有被勾选的选项
		delItems = dgData.delItems;
		for(var i in items){
			if($("input[name="+Win.checkedBoxName+"][value='"+items[i].channelNo+"']").length<=0){//如果父页面已经存在的（原来就已经勾选好的），不用管。如果不存在（新勾选上的），加上
				html += '<li><label><input name="'+Win.checkedBoxName+'" id="'+Win.checkedBoxName+'" type="checkbox" checked value="'+items[i].channelNo+'" />'+items[i].channelName;
				html += '</label></li>';
			}
		}
		$(html).appendTo("#"+Win.showPlaceId);
		if(delItems!=null && delItems.length>0){
			for(var i in delItems){
				$("input[name='"+Win.checkedBoxName+"'][value='"+delItems[i].channelNo+"']").parent().parent().remove();
			}
		}
	}
	var number = $("input[name='"+Win.checkedBoxName+"']").length;
	$("#num_"+Win.showPlaceId).html("已选择"+number+"个");
}
Win.getChannelNo= function (e){
	return $(e).val();
}
Win.getChannelName= function (e){
	return $(e).parent().text().trim();
}
Win.setCheckBoxName = function(checkBoxName){
		Win.checkedBoxName =   checkBoxName || this.checkedBoxName;
}
Win.setDataKey = function(dataName){
		Win.dataKey = dataName || this.dataKey ;
}
Win.clearData = function(){
	art.dialog.data(Win.dataKey, '');
}
