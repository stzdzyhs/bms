应用背景：这是一个选择弹出窗口的js功能通用组件，一共包括contentWindow.js和win.js。
第三方支持：其中还需要一个第三方开源的json.js文件，该文件的位置是js/json.js。
试用方法：
	1， 在父界面中引入win.js
		在弹出界面中引入json.js和contentWindow.js.
	2，父界面中只要调用openWin方法，方法参数为(actionName,title,checkedBoxName,placeId,getChannelNoF,getChannelNameF,callbackFun,getSelectChannelsF,dataName)
		actionName:弹出页面的action
		title:弹出页面的标题
		checkedBoxName:父界面的单选框（checkbox）的name
		placeId:单选框区域的div的id
		getChannelNoF:取得父界面单选框的的值方法，可以重写，不重写默认调用win.js中的getChannelNo的方法
		getChannelNameF：取得父界面单选框的的显示的内容方法，可以重写，不重写默认调用win.js中的getChannelName的方法
		callbackFun：回调方法。
		getSelectChannelsF:获得父页面的已选元素
		dataName:存放数据的key,也可用于多栏目选。如频道组/频道
		
		如Win.openWin('business/epgWin.action','请选择频道',checkedBoxName,placeId,getChannelNoF,getChannelNameF,callbackFun,getSelectChannelsF,dataName)
	3，弹出界面：
		1）加入$(document).ready(function(){
			//加入方法
			ContentWindow.register(getChannelNoF,getChannelNameF,checkBoxName);
	  	 	})
	   		参数说明：
				getChannelNoF:取得弹出界面中checkbox的的值方法，可以重写，不重写默认调用ContentWindow.js中的getChannelNo的方法
				getChannelNameF：取得弹出界面checkbox的显示的内容方法，可以重写，不重写默认调用ContentWindow.js中的getChannelName的方法
				checkedBoxName:弹出界面的单选框（checkbox）的name
		
		2）复选框的onclick函数中添加ContentWindow.updateAllChannel(this)
		3）单选框的onclick函数中添加ContentWindow.updateChannel(this)
		
	4.存放路径	
		win.js:    <script src="${path }/js/win/win.js" type="text/javascript"></script>
		json.js和contentWindow.js:   <script src="${path }/js/json.js" type="text/javascript"></script>
									 <script src="${path }/js/win/contentWindow.js" type="text/javascript"></script>
									 
	5.涉及到构造值，一律用channelNo和channelName
		
		
		