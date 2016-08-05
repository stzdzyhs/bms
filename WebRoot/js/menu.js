var preClassName = "";
var projectPath = "gtms";
/**
 * 更新 mainFrame 
 * @param Id
 * @param sortname
 * @param url
 * @return
 */
function list_sub_nav(Id, sortname, url) {
	if (preClassName != "") {
		$("#"+preClassName).addClass("ui-tabs-selected ui-state-active");
	}
	if ($("#"+Id).hasClass("ui-tabs-selected ui-state-active")) {
		$("#"+Id).removeClass("ui-tabs-selected ui-state-active");
		preClassName = Id;
		$("#mainFrame").attr("src", url);
	}
}
function setMainTable(id, defaultNavId){

	var menu_array = eval("menu_array_"+id);
	//var menu, navText = '<li id="man_nav_0" onclick="list_sub_nav(\'man_nav_0\', \'main\', \'welcome.html\')" class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><img src="images/home.png" alt="首页" style="vertical-align:middle" /></li></a>';
	var menu, navText = '<li id="man_nav_0" onclick="list_sub_nav(\'man_nav_0\', \'main\', \'' + projectPath + '/welcome.action\')" class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><img src="images/home.png" alt="首页" style="vertical-align:middle" /></li></a>';
	preClassName = "";
	var navId, navName, navUrl;
	for(var i in menu_array){
		menu = menu_array[i];
		if(menu!=undefined && menu.name!=undefined && menu.name!=''){
			navText += '<li id="man_nav_'+menu.id+'" onclick="list_sub_nav(id,\''+menu.name+'\',\''+menu.url+'\')"  class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">'+menu.name+'</li>';
			if(defaultNavId!=null && defaultNavId==menu.id){
				navId = menu.id;
				navName = menu.name;
				navUrl = menu.url;
			}else if(i==0){
				navId = menu.id;
				navName = menu.name;
				navUrl = menu.url;
			}
		}
	}
	$("#main_nav ul").html(navText);
	list_sub_nav('man_nav_'+navId, navName, navUrl);
	return false;
}

$(window).load(function(){
    $(".showMenu").click(function(){
        $(".subMenu-ul").hide();
		var ul = $(this).children(".subMenu-ul");
		if(ul.is("ul")){
			ul.show();
		}else{
			var id = $(this).attr("id");
			id = id.replace("menu_", "");
			setMainTable(id);
		}
		
		$(".showMenu").each(function () {
			$(this).removeClass("menu-active");
		});
		$(this).addClass("menu-active");
    });

    
    /**
     * 子菜单
     * 触发事件
     */
    $(".subMenu").click(function(){		
		$(".subMenu").each(function () {
			$(this).removeClass("submenu-active");
		});
		$(this).addClass("submenu-active");
		
		var id = $(this).attr("id");
		id = id.replace("menu_", "");
		setMainTable(id);
    });
	
    $(".shrink>a").click(function() {
        $("#left").toggle($("#left").css('display') == 'none');
        $(this).toggleClass("expand");
    });
	
	var $backToTopTxt = "返回顶部", $backToTopEle = $('<div class="backToTop"></div>').appendTo($("body"))
        .text($backToTopTxt).attr("title", $backToTopTxt).click(function() {
            $("html, body").animate({ scrollTop: 0 }, 120);
    }), $backToTopFun = function() {
        var st = $(document).scrollTop(), winh = $(window).height();
        (st > 0)? $backToTopEle.show(): $backToTopEle.hide();    
        //IE6下的定位
        if (!window.XMLHttpRequest) {
            $backToTopEle.css("top", st + winh - 166);    
        }
    };
    $(window).bind("scroll", $backToTopFun);
    $(function() { $backToTopFun(); });
    //首先将#back-to-top隐藏

	$(".backToTop").hide();
	
	//当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
	
	$(function () {
		$(window).scroll(function(){
			if ($(window).scrollTop()>100){
				$(".back-to-top").fadeIn(1500);
				$("#about").slideUp("slow");
			}
			else
			{
				$(".back-to-top").fadeOut(1500);
				$("#about").slideDown("slow");
			}
		});
	});
	
	setInterval(dyniframesize, 500);
});

function helpOpen() {
	art.dialog.open('views/help.html', {
		title: '帮助文档',
		lock: true,
		width: 800,
		height: 600
	}, true);
}

function infoOpen(url) {
//	art.dialog.open('util/info.html', {
	art.dialog.open(url, {
		title: '版本信息',
		lock: true,
		width: 400,
		height: 230
	}, true);
}

function logoutOpen(url) {
	art.dialog.confirm('你确认退出系统？', function(){
		art.dialog.tips('退出成功');
		document.location=url;
	}, function(){
		art.dialog.tips('你取消了操作');
	});
}

function changePassword(url) {
	var win = art.dialog.open(url, {
		title: '修改基本资料',
		width: 600,
		height: 300,
	    drag: false,
	    resize: false,
		lock: true,
		ok: function () {
		
			var iframe = this.iframe.contentWindow;
			iframe.document.getElementById('btn_submit').click();
			return false;
		},
		cancel: true
	});
}

//** iframe自动适应页面 **//
//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
function getWinHei(){
	var windowHeight;
	if (self.innerHeight) {  // all except Explorer
		windowHeight = self.innerHeight;
	} else {
		if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
			windowHeight = document.documentElement.clientHeight;
		} else {
			if (document.body) { // other Explorers
				windowHeight = document.body.clientHeight;
			}
		}
	}
	return windowHeight;
}

var height = 600;
function dyniframesize() {
	var height = getWinHei()-64;
	if(height<600){
		height==600;
	}
	var dyniframe = null;
	//自动调整iframe高度
	dyniframe = document.getElementById('mainFrame');
	if (dyniframe && !window.opera) {
		if (dyniframe.contentDocument && dyniframe.contentDocument.body && dyniframe.contentDocument.body.offsetHeight) { //如果用户的浏览器是NetScape
			if (height < (dyniframe.contentDocument.body.offsetHeight+20))
			{
				height = dyniframe.contentDocument.body.offsetHeight+20;
			}
		} else if (dyniframe.Document && dyniframe.Document.body && dyniframe.Document.body.scrollHeight) { //如果用户的浏览器是IE
			if (height < (dyniframe.Document.body.scrollHeight+18))
			{
				height = dyniframe.Document.body.scrollHeight+18;
			}
		}
		$('.leftWrap').height(height + 64);
		$('#wrapper').height(height + 64);
		dyniframe.height = height;
		$('#main_content').height(height);
	}
	if ($(window).scrollTop()>100){
		$(".back-to-top").fadeIn(1500);
		$("#about").slideUp("slow");
	}
	else
	{
		$(".back-to-top").fadeOut(1500);
		$("#about").slideDown("slow");
	}
	/*
	if (600 < getWinHei())
	{
		height = getWinHei();
	}
	var dyniframe = null;
	//自动调整iframe高度
	dyniframe = document.getElementById('mainFrame');
	if (dyniframe && !window.opera) {
		if (dyniframe.contentDocument && dyniframe.contentDocument.body.offsetHeight) { //如果用户的浏览器是NetScape
			if (height < (dyniframe.contentDocument.body.offsetHeight+20))
			{
				height = dyniframe.contentDocument.body.offsetHeight+20;
			}
		} else if (dyniframe.Document && dyniframe.Document.body.scrollHeight) { //如果用户的浏览器是IE
			if (height < (dyniframe.Document.body.scrollHeight+20))
			{
				height = dyniframe.Document.body.scrollHeight+20;
			}
		}
		dyniframe.height = height;
	}
	dyniframe.style.display = "";
	*/
}
function switchMenu(){
	$(".leftWrap").toggle();
	$(".switchLeft").toggle();
	$(".switchRight").toggle();	
}