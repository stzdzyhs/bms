/**
* 去掉字符串开头和结尾的空格	
**/
String.prototype.trim = function()
{
   return this.replace(/^\s*(.*)\s*$/,"$1");
}

/**
 * string format, e.g:
 * String.format("hello {0}", "world");
 * returns "hello world"
 * NOTE: can not add prototype !
 */
String.format = function() {
    if (arguments.length == 0) {
        return null;
    }
    var str = arguments[0];
    for ( var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};

function Adv(node)
{
	var advDivNode = null;
	var self = this;
	var url = "";
	var advType = "";
	var providerId = "";
	var assetId = "";
	var parentAssetId = "";
	var regionCode = "";
	var smardCard = "";
	var folderType = "";
	var advId = "";
	var clientType = "";
	var catalogId = "";
	var channelId = "";
//	var vertical = "";
	var margin = "0px";
//	var hnum = 1;
//	var vnum = 1;
	
	var idSuffix = Math.random()+'';
	idSuffix = idSuffix.substring(idSuffix.indexOf('.')+1);
	
	//待插入广告位ID
	var insertedId = node.getAttribute("id");
	if(null == insertedId || "" == insertedId)
	{
		insertedId = "id" + idSuffix + new Date().getTime();
		node.setAttribute("id", insertedId)
	}
	
	var xmlHttp$ = null;
	
	var requestXML$ = "";
	var timeout$ = -1;
		
	this.nextId$ = "";
	this.interval$ = 5; 
	this.advWidth = "0px";
	this.advHeight = "0px";
	
	var direction = "1";
	var intervalIdXY = -1;
	var speed = 6;
//	var tail = 2000;
	/*	
	function scrollUp()
	{
		clearTimeout(intervalIdXY);
		var obj = document.getElementById('marquee' + insertedId);
		
		var scrollTopY = obj.scrollTop;
		scrollTopY = scrollTopY + parseInt(obj.clientHeight) - 10;
		obj.scrollTop = scrollTopY;
		if((scrollTopY + parseInt(obj.clientHeight)) >= obj.scrollHeight)
		{
			intervalIdXY = setTimeout(function(){
			//	var obj = document.getElementById('marquee' + insertedId);
			//	obj.scrollTop = 0;
			//	clearTimeout(intervalIdXY);
			//	intervalIdXY = setTimeout(function(){scrollUp();}, (speed+tail));
				connection();
			}, (speed+tail));
		}
		else
			intervalIdXY = setTimeout(function(){scrollUp();}, speed);
	}
	
	var scrollDownTemp;
	function scrollDown()
	{
		clearTimeout(intervalIdXY);
		var obj = document.getElementById('marquee' + insertedId);
		
		scrollDownTemp = scrollDownTemp - parseInt(obj.clientHeight) + 10;
		obj.scrollTop = scrollDownTemp;
		if(0 >= scrollDownTemp)
		{
			intervalIdXY = setTimeout(function(){
			//	var obj = document.getElementById('marquee' + insertedId);
			//	scrollDownTemp = obj.scrollHeight - obj.clientHeight;
			//	obj.scrollTop = scrollDownTemp;
			//	clearTimeout(intervalIdXY);
			//	intervalIdXY = setTimeout(function(){scrollDown();}, (speed+tail));
				connection();
			}, (speed+tail));
		}
		else
			intervalIdXY = setTimeout(function(){scrollDown();}, speed);
	}
	
	function stopXY()
	{
		clearTimeout(intervalIdXY);
	}
	
	function startXY()
	{
		if('1' == direction)
			intervalIdXY = setTimeout(function(){scrollLeft();}, speed);
		else if('2' == direction)
			intervalIdXY = setTimeout(function(){scrollRight();}, speed);
		else if('3' == direction)
			intervalIdXY = setTimeout(function(){scrollUp();}, speed);
		else if('4' == direction)
			intervalIdXY = setTimeout(function(){scrollDown();}, speed);
	}
	
	function scrollLeft()
	{
		clearTimeout(intervalIdXY);
		var obj = document.getElementById('marquee' + insertedId);
		
		var scrollLeftX = obj.scrollLeft;
		obj.scrollLeft = ++scrollLeftX;
		if((scrollLeftX + parseInt(obj.clientWidth)) >= obj.scrollWidth)
		{
			intervalIdXY = setTimeout(function(){
			//	var obj = document.getElementById('marquee' + insertedId);
			//	obj.scrollLeft = 0;
			//	clearTimeout(intervalIdXY);
			//	intervalIdXY = setTimeout(function(){scrollLeft();}, (speed+tail));
				connection();
			}, (speed+tail));
		}	
		else
			intervalIdXY = setTimeout(function(){scrollLeft();}, speed);
	}
	
	var scrollRightTemp;
	function scrollRight()
	{
		clearTimeout(intervalIdXY);
		var obj = document.getElementById('marquee' + insertedId);
		
		obj.scrollLeft = --scrollRightTemp;
		if(0 >= scrollRightTemp)
		{
			intervalIdXY = setTimeout(function(){
			//	var obj = document.getElementById('marquee' + insertedId);
			//	scrollRightTemp = obj.scrollWidth - obj.clientWidth;
			//	obj.scrollLeft = scrollRightTemp;
			//	clearTimeout(intervalIdXY);
			//	intervalIdXY = setTimeout(function(){scrollRight();}, (speed+tail));
				connection();
			}, (speed+tail));
		}	
		else
			intervalIdXY = setTimeout(function(){scrollRight();}, speed);
	}
	*/	
	function createPictureRequestXML$(providerId$, assetId$, parentAssetId$, regionCode$,
			smardCard$, folderType$, advId$, clientType$, catalogId$, channelId$)
	{
		return '<?xml version="1.0" encoding="UTF8"?>' + 
			'<GetPicAdv ' + 
			'providerId="' + providerId$ + '" ' + 
			'assetId="' + assetId$ + '" ' + 
			'parentAssetld ="' + parentAssetId$ + '" ' + 
			'regionCode="' + regionCode$ + '" ' + 
			'smardCard="' + smardCard$ + '" ' +
			'folderType="' + folderType$ + '" ' + 
			'advId="' + advId$ + '" ' +
			'clientType="' + clientType$ + '" ' +
			'catalogId="' + catalogId$ + '" ' + 
			'channelId="' + channelId$ + '"/>';
	}	
	
	function createTextRequestXML$(providerId$, assetId$, parentAssetId$, regionCode$,
			smardCard$, folderType$, advId$, clientType$, catalogId$, channelId$)
	{
		return '<?xml version="1.0" encoding="UTF8"?>' + 
			'<GetTxtAdv ' + 
			'providerId="' + providerId$ + '" ' + 
			'assetId="' + assetId$ + '" ' + 
			'parentAssetld ="' + parentAssetId$ + '" ' + 
			'regionCode="' + regionCode$ + '" ' + 
			'smardCard="' + smardCard$ + '" ' +
			'folderType="' + folderType$ + '" ' + 
			'advId="' + advId$ + '" ' +
			'clientType="' + clientType$ + '" ' +
			'catalogId="' + catalogId$ + '" ' + 
			'channelId="' + channelId$ + '"/>';
	}
	
	function handleStateChange$()
	{	
	    if(xmlHttp$.readyState == 4 && xmlHttp$.status == 200) 
	    {
           parseResults$(xmlHttp$.responseXML);
	    }
	}
	
	function parseResults$(data)
	{
		try
		{
			var insertedNode = document.getElementById(insertedId);
			var root = data.getElementsByTagName("PicAdv")[0];
			var txtRoot = data.getElementsByTagName("TextAdv")[0];
			//处理图片广告
			if(null != root && "undefined" != root)
			{
				var resultCode = root.getAttribute("resultCode");
			//	var firstId = '';
			//	var isInterval = true;
				if(0 == resultCode || '0' == resultCode)
				{
					var nodeArray = root.getElementsByTagName("PicAdvItem");
					var rows = root.getAttribute("rows");
					var columns = root.getAttribute("columns");
					var newNodeStr = "";
					var index = 0;
					
					if(null == rows || "" ==rows || "undefined" == rows)
						rows = 1;
					else
						rows = parseInt(rows, 10);
						
					if(null == columns || "" ==columns || "undefined" == columns)
						columns = 1;
					else
						columns = parseInt(columns, 10);
						
					//垂直广告位循环
					for(var vi=0; vi<rows && index<nodeArray.length; vi++)
					{
						var hi = 0;
						newNodeStr += '<div style="display:block;posistion:relative;overflow:hidden;margin:0px;padding:0px" align="left" name="name' + insertedId + '">';
						//水平广告位循环
						while(index<nodeArray.length && hi<columns)
						{
						//	for(var i=0; i<nodeArray.length; i++)
						//	{
								//获取从server返回的数据
								var imageSrc = nodeArray[index].getAttribute("file");
								var assetId = nodeArray[index].getAttribute("assetId");
								var interval = nodeArray[index].getAttribute("interval");
								var next = nodeArray[index].getAttribute("next");
								var href = nodeArray[index].getAttribute("href");
								
						//		if(0 == i)
						//			firstId = assetId;
									
						//		if(null == next || "" == next.trim())
						//		{
						//			next = assetId;
						//			isInterval = false;
						//		}
								
						//		if(null == interval || "" == interval.trim() || 0 == parseInt(interval))
						//			interval = 5;
								//设置上边空白
								var marginTop = '';
								if(0 == vi)
									marginTop = ' margin-top:0px; ';
								else
									marginTop = ' margin-top:' + margin + '; ';
								//设置水平空白	
								var marginLeft = '';
								if(0 == hi)
									marginLeft = ' margin-left:0px; ';
								else
									marginLeft = ' margin-left:' + margin + '; ';
								
								newNodeStr += '<div style="display:inline;posistion:relative;overflow:hidden;margin:0px;padding:0px" id="id' + insertedId + assetId + '" name="' + insertedId + 'Item">';
								if(null != href && "" != href)
									newNodeStr += '<a href="' + href + '"><image style="display:inline;width:' + self.advWidth + '; height:' + self.advHeight + ';padding:0px;' + marginTop + marginLeft +'" src="' + imageSrc + '"/></a>'
								else
									newNodeStr += '<image style="display:inline;width:' + self.advWidth + '; height:' + self.advHeight + ';padding:0px;' + marginTop + marginLeft + '" src="' + imageSrc + '"/>'
								newNodeStr += '</div>';
								hi++;
								index++; 
						//	}
						}
						newNodeStr += '</div>'
					}
					insertedNode.innerHTML = newNodeStr;
					/*
					self.view(firstId);
					if(isInterval && 1 < nodeArray.length)
				    	timeout$ = setInterval(function(){self.view(self.nextId$)}, parseInt(self.interval$)*1000);
				    */ 
				} else {
					insertedNode.innerHTML = "";
				}
				/*
				clearTimeout(intervalIdXY);
				intervalIdXY = setTimeout(function(){connection();}, speed);
				*/
			}
			//处理文字广告
			else if(null != txtRoot && "undefined" != txtRoot)
			{
				var txtReturnCode = txtRoot.getAttribute("resultCode");
				var txtNodeStr = "";
				if(0 == txtReturnCode || "0" == txtReturnCode)
				{
					var txtNodeArray = txtRoot.getElementsByTagName("TextAdvItem");
					var txtNewNodeStr = "";
					for(var j = 0; j<txtNodeArray.length; j++)
					{
						direction = txtNodeArray[j].getAttribute("text_Direct");
						var txtInterval = txtNodeArray[j].getAttribute("interval");
						var txtHref = txtNodeArray[j].getAttribute("href");
						var txtNext = txtNodeArray[j].getAttribute("next");
						var txtNext = txtNodeArray[j].getAttribute("next");
						var txtContentNode = txtNodeArray[j].getElementsByTagName("TextContent")[0];
						var textContent = '';
						
						if(null != txtContentNode.childNodes[0].data && "" != txtContentNode.childNodes[0].data.trim())
							textContent = txtContentNode.childNodes[0].data;
						else
							textContent = txtContentNode.childNodes[1].data;
						
						if(null != txtHref && "" != txtHref)
							txtNewNodeStr += '<a href="' + txtHref + '">' + textContent + '</a>';
						else
							txtNewNodeStr += textContent;
					}
					/*
					//direction值为1向左移动
					if('1' == direction)
					{
						insertedNode.innerHTML = '<div id="marquee' + insertedId + '" style="white-space:nowrap;;overflow:hidden;height:' + self.advHeight + ';width:' + self.advWidth + ';">' 
							                     + txtNewNodeStr 
							                     + '</div>';
						var scrollLeftObj = document.getElementById('marquee' + insertedId);
						scrollLeftObj.scrollLeft = 0;
						clearTimeout(intervalIdXY);
						intervalIdXY = setTimeout(function(){scrollLeft();}, (speed+tail));	
					}
					//direction值为2向右移动
					else if('2' == direction)
					{
						insertedNode.innerHTML = '<div id="marquee' + insertedId + '" style="white-space:nowrap;;overflow:hidden;height:' + self.advHeight + ';width:' + self.advWidth + ';">' 
							                     + txtNewNodeStr 
							                     + '</div>';
						var scrollRightObj = document.getElementById('marquee' + insertedId);
						scrollRightTemp = scrollRightObj.scrollWidth - scrollRightObj.clientWidth;
						scrollRightObj.scrollLeft = scrollRightTemp;
						clearTimeout(intervalIdXY);
						intervalIdXY = setTimeout(function(){scrollRight();}, (speed+tail));
					}
					//direction值为3向上移动
					else if('3' == direction)
					{
						insertedNode.innerHTML = '<div id="marquee'+ insertedId + '" style="word-break:break-all;word-wrap:break-word;overflow:hidden;height:' + self.advHeight + ';width:' + self.advWidth + '">' 
							                     + txtNewNodeStr 
							                     + '</div>';
						var scrollTopObj = document.getElementById('marquee' + insertedId)
						scrollTopObj.scrollTop = 0;
						clearTimeout(intervalIdXY);
						intervalIdXY = setTimeout(function(){scrollUp();}, (speed+tail));
					}
					//direction值为4向下移动
					else if('4' == direction)
					{
						insertedNode.innerHTML = '<div id="marquee'+ insertedId + '" style="word-break:break-all;word-wrap:break-word;overflow:hidden;height:' + self.advHeight + ';width:' + self.advWidth + '">' 
							                     + txtNewNodeStr 
							                     + '</div>';
						var scrollDownObj = document.getElementById('marquee' + insertedId)
						scrollDownTemp = scrollDownObj.scrollHeight - scrollDownObj.clientHeight;
						scrollDownObj.scrollTop = scrollDownTemp;
						clearTimeout(intervalIdXY);
						intervalIdXY = setTimeout(function(){scrollDown();}, (speed+tail));
					}
					//direction值为0不移动
					else
					{
						insertedNode.innerHTML = '<div id="marquee'+ insertedId + '" style="word-break:break-all;word-wrap:break-word;overflow:hidden;height:' + self.advHeight + ';width:' + self.advWidth + '">' 
							                     + txtNewNodeStr 
							                     + '</div>';
						//speed + tail微秒后重新连接
						clearTimeout(intervalIdXY);
						intervalIdXY = setTimeout(function(){connection();}, (speed+tail));
					}
					
					//给走马灯添加事件
					if('0' != direction)
					{
						var marqueeObj = document.getElementById('marquee' + insertedId);
						if(window.addEventListener)
						{
							marqueeObj.addEventListener("mouseover", stopXY, false);
							marqueeObj.addEventListener("mouseout", startXY, false);
						}
						else if(window.attachEvent)
						{
							marqueeObj.attachEvent("onmouseover", stopXY);
							marqueeObj.addEventListener("onmouseout", startXY, false);
						}
						else
						{
							marqueeObj.onmouseover = stopXY;
							marqueeObj.onmouseout = startXY;
						}
					}
					*/
					//direction值为1向左移动
					if('1' == direction)
					{
						insertedNode.innerHTML = '<marquee onmouseout="this.start()" onmouseover="this.stop()" direction="left" scrollamount="' + speed + '" style="white-space:nowrap;height:' + self.advHeight + '; width:' + self.advWidth + ';">'
												+ txtNewNodeStr
												+ '</marquee>';
					}
					//direction值为2向右移动
					else if('2' == direction)
					{
						insertedNode.innerHTML = '<marquee onmouseout="this.start()" onmouseover="this.stop()" direction="right" scrollamount="' + speed + '" style="white-space:nowrap;height:' + self.advHeight + '; width:' + self.advWidth + ';">'
												+ txtNewNodeStr
												+ '</marquee>';
					}
					//direction值为3向上移动
					else if('3' == direction)
					{
						insertedNode.innerHTML = '<marquee onmouseout="this.start()" onmouseover="this.stop()" direction="up" scrollamount="' + speed + '" style="word-break:break-all;word-wrap:break-word;height:' + self.advHeight + '; width:' + self.advWidth + ';">'
												+ txtNewNodeStr
												+ '</marquee>';
					}
					//direction值为4向下移动
					else if('4' == direction)
					{
						insertedNode.innerHTML = '<marquee onmouseout="this.start()" onmouseover="this.stop()" direction="down" scrollamount="' + speed + '" style="word-break:break-all;word-wrap:break-word;height:' + self.advHeight + '; width:' + self.advWidth + ';">'
												+ txtNewNodeStr
												+ '</marquee>';
					}
					//direction值为0不移动
					else
					{
						insertedNode.innerHTML = '<div id="marquee'+ insertedId + '" style="word-break:break-all;word-wrap:break-word;overflow:hidden;height:' + self.advHeight + ';width:' + self.advWidth + '">' 
							                     + txtNewNodeStr 
							                     + '</div>';
					}
				} else {
					insertedNode.innerHTML = "";
					/*
					clearTimeout(intervalIdXY);
					intervalIdXY = setTimeout(function(){connection();}, (speed+tail));
					*/
				}
			}
		} 
		catch(e){console.info(e);}
	}

	try
	{
		//获取广告位参数
		advDivNode = document.getElementById(insertedId);
//		url = advDivNode.getAttribute("url");
		providerId = advDivNode.getAttribute("providerId");
		assetId = advDivNode.getAttribute("assetId");
		parentAssetId = advDivNode.getAttribute("parentAssetId");
		regionCode = advDivNode.getAttribute("regionCode");
		smardCard = advDivNode.getAttribute("smardCard");
		folderType = advDivNode.getAttribute("folderType");
		advId = advDivNode.getAttribute("advId");
		clientType = advDivNode.getAttribute("clientType");
		catalogId = advDivNode.getAttribute("catalogId");
		channelId = advDivNode.getAttribute("channelId");
		advType = advDivNode.getAttribute("advType");
//		vertical = advDivNode.getAttribute("vertical");
		try 
		{
		//	var width = advDivNode.clientWidth;
		//	var height = advDivNode.clientHeight;
			
		//	if(0 == width || "undefined" == width || null == width || width == document.body.clientWidth)
			var width = advDivNode.getAttribute("itemWidth");
				
		//	if(0 == height || "undefined" == height || null == height)
			var height = advDivNode.getAttribute("itemHeight");
			
		//	this.advWidth = parseInt(width, 10);
		//	this.advHeight = parseInt(height, 10);
			if(isNaN(width))
				this.advWidth = width;
			else
				this.advWidth = width+"px";
			
			if(isNaN(height))
				this.advHeight = height;
			else
				this.advHeight = height+"px";
			
			margin = advDivNode.getAttribute("margin");
			if(null == margin || "" == margin.trim() || "undefined" == margin)
				margin = "0px";
				
			speed = advDivNode.getAttribute("speed");
			if(null == speed || "" == speed.trim() || "undefined" == speed)
				speed = 6;
			else
				speed = parseInt(speed, 10);
			/*	
			tail = advDivNode.getAttribute("tail");
			if(null == tail || "" == tail.trim() || "undefined" == tail)
				tail = 2000;
			else
				tail = parseInt(tail, 10);
			*/
			/*
			hnum = advDivNode.getAttribute("hnum");	
			if(null == hnum || "" == hnum.trim() || "undefined" == hnum)
				hnum = 1;
			else
				hnum = parseInt(hnum, 10);
			
			vnum = advDivNode.getAttribute("vnum");
			if(null == vnum || "" == vnum.trim() || "undefined" == vnum)
				vnum = 1;
			else
				vnum = parseInt(vnum, 10);
			*/
		}
		catch(e){console.info(e);}
		
		if(window.XMLHttpRequest)
			xmlHttp$ = new XMLHttpRequest();
	  	else if(window.ActiveXObject)
	  		xmlHttp$ = new ActiveXObject("Microsoft.XMLHTTP");
	}
	catch(e)
	{console.info(e);}
	
	//定义连接函数
	function connection()
	{
		try
		{
			url = advDivNode.getAttribute("url");
			if(-1 < url.indexOf("?"))
				url = url + "&timestamp=" + (new Date().getTime()) + "&randomcode=" + idSuffix;
			else
				url = url + "?timestamp=" + (new Date().getTime()) + "&randomcode=" + idSuffix;
				
		    xmlHttp$.open("POST", url, true);
		    xmlHttp$.onreadystatechange = handleStateChange$;
		    xmlHttp$.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		    if("text" == advType)
			    xmlHttp$.send(createTextRequestXML$(providerId, assetId, parentAssetId, regionCode, smardCard, folderType, advId, clientType, catalogId, channelId));
		    else
		    	xmlHttp$.send(createPictureRequestXML$(providerId, assetId, parentAssetId, regionCode, smardCard, folderType, advId, clientType, catalogId, channelId));
		} catch(e)
		{console.info(e);}
	}
	//调用连接函数
	connection();
	
	/*
	this.view = function(id)
	{
		try
		{
			//var picAdvItemDivs = document.getElementsByName("picAdvItemDiv");
			var   picAdvItemDivs = document.getElementsByTagName("div");
			for(var i=0; i<picAdvItemDivs.length; i++)
			{
				if((insertedId + "Item") == picAdvItemDivs[i].getAttribute("name"))
					picAdvItemDivs[i].style.cssText="display:none;overflow:hidden;margin:0px;padding:0px"
			}
			var obj = document.getElementById((insertedId + id));
			if(null != obj)
			{
				obj.style.cssText="display:inline;overflow:hidden;margin:0px;padding:0px"
				this.nextId$ = obj.getAttribute("next");
				this.interval$ = obj.getAttribute("interval");	
			}
		} catch(e){}
	}
	*/
}

function onDivLoad(event)
{
	var nodes = document.getElementsByTagName("div");
	for(var i=0; i<nodes.length; i++)
	{
		var cssClasses = nodes[i].className;
		if(-1 < cssClasses.toLowerCase().indexOf("advitem"))
			new Adv(nodes[i]);
	}
}