/*弹出页面*/
var ContentWindow = {
	checkedBoxName : "channelNo", /*checkbox的name,默认是channelNo,可通过register方法注册进来*/
	dataKey:"ChannelData" /*存放数据的key*/
};
ContentWindow.initWinSelect = function (){
	//alert(ContentWindow.dataKey);
	var json = art.dialog.data(ContentWindow.dataKey);
	//alert(json);
	if(json!=null && typeof(json)!='undefined' && json!=""){
		var dgData = eval(json);
		var items = dgData.items;
		for(var i in items){
			$("input[name='"+ContentWindow.checkedBoxName+"'][value='"+items[i].channelNo+"']").attr("checked",true);
		}
	}
}
//单选框
ContentWindow.updateChannel= function (e){
	var json = art.dialog.data(ContentWindow.dataKey);
	var dgData ;
	var items,delItems ;
	if(json!=null && typeof(json)!='undefined' && json!="" ){
		dgData = eval(json);
		if(dgData.items == null){
			dgData.items= new Array();
		}
		if(dgData.delItems == null){
			dgData.delItems= new Array();
		}
		
	}else{
		dgData = new  Object();
		dgData.items = new Array();
		dgData.delItems= new Array();
	}
	items = dgData.items;
	delItems = dgData.delItems;
	if(e.checked){//add
		//alert("checked");
		var item = new Object();
		item.channelNo=ContentWindow.getChannelNo(e);
		item.channelName = ContentWindow.getChannelName(e);
		item.epgNo = ContentWindow.getEpgNo(e);
		items.push(item);
		//检查delItems中是否已经包含该元素,如果有去除
		for(var i in delItems){
			if(delItems[i].channelNo==ContentWindow.getChannelNo(e)){
				dgData.delItems.splice(i,1);
			}
		}
		//alert(JSON.stringify(dgData));
	}else{//delete
		//alert("no checked");
		for(var i in items){
			//alert(items[i].channelNo);
			if(items[i].channelNo==ContentWindow.getChannelNo(e)){
				//delete dgData.items[i];
				dgData.items.splice(i,1);
				break;
			}
		}
		//检查delItems中是否已经包含该元素
		var isNew = true;
		for(var i in delItems){
			if(delItems[i].channelNo==ContentWindow.getChannelNo(e)){
				isNew = false;
				break;
			}
		}
		//如果没有包含，添加进去
		if(isNew){
			var delItem = new Object();
			delItem.channelNo = ContentWindow.getChannelNo(e);
			delItems.push(delItem);
		}
	}
	//var jsonStr = dgData.toJSONString();  
	//var jsonStr = dgData.stringify();
	var jsonStr = JSON.stringify(dgData);
	art.dialog.data(ContentWindow.dataKey, "(" + jsonStr + ")");
}
//复选框
ContentWindow.updateAllChannel = function (e){
	var json = art.dialog.data(ContentWindow.dataKey);
	var dgData ;
	var items ,delItems;
	if(json!=null && typeof(json)!='undefined' && json!="" ){
		dgData = eval(json);
		if(dgData.items == null){
			dgData.items= new Array();
		}
		if(dgData.delItems == null){
			dgData.delItems= new Array();
		}
	}else{
		dgData = new  Object();
		dgData.items = new Array();
		dgData.delItems= new Array();
		
	}
	items = dgData.items;
	delItems = dgData.delItems;
	if(e.checked){//add
		$("input[name='"+ContentWindow.checkedBoxName+"']").each(function(){
			var isNew = true;
			//检查items中是否已经包含该元素
			for(var i in items){
				if(items[i].channelNo==ContentWindow.getChannelNo(this)){
					isNew = false;
					break;
				}
			}
			//如果没有包含，添加进去
			if(isNew){
				var item = new Object();
				item.channelNo=ContentWindow.getChannelNo(this);
				item.channelName =ContentWindow.getChannelName(this);
				item.epgNo = ContentWindow.getEpgNo(this);
				items.push(item);
			}
			//检查delItems中是否已经包含该元素,如果有去除
			for(var i in delItems){
				if(delItems[i].channelNo==ContentWindow.getChannelNo(this)){
					dgData.delItems.splice(i,1);
				}
			}
		});
	}else{//unchecked
	    $("input[name='"+ContentWindow.checkedBoxName+"']").each(function(){
	    	for(var i in items){
				//alert(items[i].channelNo);
				//检查items中是否已经包含该元素,如果有去除
				if(items[i].channelNo==ContentWindow.getChannelNo(this)){
					//delete dgData.items[i];
					dgData.items.splice(i,1);
					break;
				}
			}
			//检查delItems中是否已经包含该元素
			var isNew = true;
			for(var j in delItems){
				if(delItems[j].channelNo==ContentWindow.getChannelNo(this)){
					isNew = false;
					break;
				}
			}
			//如果没有包含，添加进去
			if(isNew){
				var delItem = new Object();
				delItem.channelNo = ContentWindow.getChannelNo(this);
				delItems.push(delItem);
		   }
		});
	}
	//var jsonStr = dgData.toJSONString();  
	//var jsonStr = dgData.stringify();
	var jsonStr = JSON.stringify(dgData);
	//alert(jsonStr);
	art.dialog.data(ContentWindow.dataKey, "(" + jsonStr + ")");
}
ContentWindow.getChannelNo= function (e){
	return $(e).val();
}
ContentWindow.getChannelName= function (e){
	return $(e).parent().next().html().replaceAll("'", "&#8242;");
}
ContentWindow.getEpgNo= function (e){
	return $(e).next().val();
}

ContentWindow.setCheckBoxName = function(checkBoxName){
	/*if(typeof(checkBoxName)!='undefined' && checkBoxName!=""){
		ContentWindow.checkedBoxName = checkBoxName;
	}*/
	ContentWindow.checkedBoxName  = checkBoxName || this.checkedBoxName;
}
ContentWindow.register = function(getChannelNoF,getChannelNameF,checkBoxName,dataName,getEpgNoF){
	ContentWindow.setCheckBoxName(checkBoxName);
	ContentWindow.setDataKey(dataName);
	ContentWindow.initWinSelect();
	if(typeof(getChannelNoF)=='function'){
		ContentWindow.getChannelNo = getChannelNoF;
	}
	if(typeof(getChannelNameF)=='function'){
		ContentWindow.getChannelName = getChannelNameF;
	}
	if(typeof(getEpgNoF)=='function'){
		ContentWindow.getEpgNo = getEpgNoF;
	}
}
ContentWindow.setDataKey = function(dataName){
		this.dataKey = dataName || this.dataKey ;
}