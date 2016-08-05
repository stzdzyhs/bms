// common utilities

/**
 * is result success
 */
function isResultSucc(result) { 
	if(typeof(result.result)=='boolean' && result.result==false){
		return false;
	}
    if(result.result==0 || result.result=="true" || result.result==true) { 
        return true; 
    }
    return false; 
} 

// column status const
var STATUS_EDIT=0;
var STATUS_SUBMIT=1;
var STATUS_PASS=2;
var STATUS_FAIL=3;
var STATUS_PUBLISH=4;
var STATUS_UNPUBLISH=5;


/**
 * string format, e.g:
 * String.format("hello {0}", "world");
 * returns "hello world"
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

/**
 * format by paramName
 * e.g: String.format2("hello {name}", {name:'world'});
 * returns hello world
 * @returns
 */
String.format2 = function() {
	if (arguments.length !=2) {
		return "";
	}
	var str = arguments[0];
	var data = arguments[1];
	var re;	
	for(var prop in data) {
		re = new RegExp('\\{' + prop + '\\}', 'gm');
		if(data[prop]==null) {
			str = str.replace(re, "");
		}
		else {
			str = str.replace(re, data[prop]);
		}
	}
	return str;
};

// almost same with format2
String.format3 = function() {
	if (arguments.length !=2) {
		return "";
	}
	var str = arguments[0];
	var data = arguments[1];
	var re;	
	for(var prop in data) {
		re = new RegExp('_' + prop + '_', 'gm');
		if(data[prop]==null) {
			str = str.replace(re, "");
		}
		else {
			str = str.replace(re, data[prop]);
		}
	}
	return str;
};


/**
 * string endsWith() 
 * @param str
 * @param suffix
 * @returns {Boolean}
 */
function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

// always ends with /
function determineRoot() {
	try {
		var s = window.location.pathname;
		var idx = s.indexOf("/", 1);
		if(idx==-1) {
			return "/gtms/";
		}
		s = s.substring(0,idx+1);
		return s;
	}
	catch(e) {
		return "/gtms/";
	}
}
var ROOT = determineRoot();

function isEmpty(str) {
	if(str==null) {
		return true;
	}
	str = "" + str;
	if(str=="") {
		return true;
	}
	return false;
}

var scriptRoot="/gtms";

//load company colum into select
function loadCompanyColumn(compnayNo, eleId, selectedVal, excludeColumnNo) {
	//alert("compnayNo: " + compnayNo);
	jQuery.get(	
		scriptRoot + "/textmgmt/column/getCompanyColumns.action",
		{
			companyNo:compnayNo,
			columnNo:excludeColumnNo
		},
		function(rsobj) {
			$(eleId).empty();
			$(eleId).append($('<option>', {
		        value: '',
		        text : '请选择'
		    }));
			
			if (isResultSucc(rsobj)){
				$.each(rsobj.data, function (i, item) {
				    $(eleId).append($('<option>', {
				        value: item.columnNo,
				        text : item.columnName
				    }));
				});
				
				if(!isEmpty(selectedVal)) {
					$(eleId).val(selectedVal);
				}
			}
			else{
				art.dialog.alert("查询板块失败,请重试");
			}
		},
		'json'
	);
}

/**
 * audit entity status.
 * @param name - 版块， 文章 etc
 * @param status
 * @param url 
 * @param formId
 * @returns {Boolean}
 */
function audit(name, url, status, formId) {
	var cmpIds = $("input[name='rtId']:checked");
	if (cmpIds.length<=0){
		if (status == STATUS_SUBMIT){
			art.dialog.alert("请选择需要提交审核的选项！");
		}
		else if (status == STATUS_PASS){
			art.dialog.alert("请选择需要审核通过的选项！");
		}
		else if (status == STATUS_FAIL){
			art.dialog.alert("请选择需要审核不通过的选项！");
		}
		else if (status == STATUS_PUBLISH){
			art.dialog.alert("请选择需要发布的选项！");
		}
		else if (status == STATUS_EDIT){
			art.dialog.alert("请选择需要初始的选项！");
		}
		else if(status==STATUS_UNPUBLISH) {
			art.dialog.alert("请选择需要取消发布的选项！");			
		} 
		else {
			art.dialog.alert("shoud not here");
		}
		return false;
	}
	
	var msg;
	for (var i=0; i<cmpIds.length; i++){
		var allocRes = $("#allocRes" + cmpIds[i].value).val();
		if(allocRes == 1){
			msg = String.format("无权操作分配的资源", name);
			art.dialog.alert(msg);
			return false;
		}
		var oldStatus = $("#oldStatus" + cmpIds[i].value).val();
		if (status == STATUS_EDIT){
			return false;
		}
		else if (status == STATUS_SUBMIT){
			if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL) {
				msg = String.format("提交审核的{0}必须为【初始】或【审核不通过】状态！", name);
				art.dialog.alert(msg);
				return false;
			}
		}
		else if (status == STATUS_PASS){
			if (oldStatus != STATUS_SUBMIT ){
				msg = String.format("审核通过的{0}必须为【提交审核】状态！",name);
				art.dialog.alert(msg);
				return false;
			}
		}
		else if (status == STATUS_FAIL){
			if (oldStatus != STATUS_SUBMIT && oldStatus!=STATUS_PASS && oldStatus != STATUS_UNPUBLISH){
				msg = String.format("审核不通过的{0}必须为【提交审核】或【审核通过】或【取消发布】状态！",name);						
				art.dialog.alert(msg);
				return false;
			}
		}
		else if (status == STATUS_PUBLISH){
			if (oldStatus != STATUS_PASS && oldStatus != STATUS_UNPUBLISH){
				msg = String.format("发布的{0}必须为【审核通过】或【取消发布】状态！",name);
				art.dialog.alert(msg);
				return false;
			}
		}
		else if (status == STATUS_UNPUBLISH){
			if (oldStatus != STATUS_PUBLISH){
				msg = String.format("取消发布的{0}必须为【发布】状态！",name);
				art.dialog.alert(msg);
				return false;
			}
		}
		else {
			art.dialog.alert("1==0 ?");
			return false;
		} 
	}
	
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: url + "?status=" + status,
				dataType: 'html',
				beforeSend: function() {
					art.dialog.through({
						id: 'broadcastLoading',
						title: false,
					    content: '<img src="' + scriptRoot + '/images/08.gif" />',
					    lock: true
					});
				},
				error: function(a, b) {
					art.dialog.list['broadcastLoading'].close();
					if (status == STATUS_SUBMIT){
						art.dialog.alert("提交审核失败！");
					}
					else if (status == STATUS_PASS){
						art.dialog.alert("审核通过失败！",goBack);
					}
					else if (status == STATUS_FAIL){
						art.dialog.alert("审核不通过失败！",goBack);
					}
					else if (status == STATUS_PUBLISH){
						art.dialog.alert("发布失败！",goBack);
					}
					else if (status == STATUS_UNPUBLISH){
						art.dialog.alert("取消发布失败！",goBack);
					}
					else {
						art.dialog.alert("1+1==3?",goBack);
					}
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(isResultSucc(rsobj)){
						if(status == STATUS_EDIT){
							art.dialog.alert("初始成功！",goBack);
						}
						else if (status == STATUS_SUBMIT){
							art.dialog.alert("提交审核成功！",goBack);
						}
						else if (status == STATUS_PASS){
							art.dialog.alert("审核通过成功！",goBack);
						}
						else if (status == STATUS_FAIL){
							art.dialog.alert("审核不通过成功！",goBack);
						}
						else if (status == STATUS_PUBLISH){
							art.dialog.alert("发布成功！",goBack);
						}
						else if (status == STATUS_UNPUBLISH){
							art.dialog.alert("取消发布成功！",goBack);
						}
						else {
							art.dialog.alert("1==0",goBack);
						}
					}
					else{
						if (status == STATUS_EDIT){
							art.dialog.alert("初始失败！" + rsobj.desc,goBack);
						}
						else if (status == STATUS_SUBMIT){
							art.dialog.alert("提交审核失败！" + rsobj.desc,goBack);
						}
						else if (status == STATUS_PASS){
							art.dialog.alert("审核通过失败！" + rsobj.desc,goBack);
						}
						else if (status == STATUS_FAIL){
							art.dialog.alert("审核不通过失败！" + rsobj.desc,goBack);
						}
						else if (status == STATUS_PUBLISH){
							art.dialog.alert("发布失败:" + rsobj.desc, goBack);
						}
						else if (status == STATUS_UNPUBLISH){
							art.dialog.alert("取消发布失败:" + rsobj.desc, goBack);
						}
						else {
							art.dialog.alert("1==0",goBack);
						}
					}
				}
			};
			jQuery(formId).ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

/**
 * is ui/operation for add
 */
function isOpAdd() {
	try {
		if(isEmpty(dataId)) {
			return true;
		}
	}
	catch(e) {
	}
	return false;
}

/**
 * is ui/operation for update, or modify 
 */
function isOpUpdate() {
	try {
		if(!isEmpty(dataId)) {
			return true;
		}
	}
	catch(e) {
	}
	return false;
}

function addSelectValue(selectId, val,name) {
	$(selectId).append(
		$('<option>',{ value:val,   text : name}
	));
}
function clearSelect(selectId) {
	$(selectId + " option").remove();
}

function emptyFunc() {
}

/**
 * test if the array has the key value
 * return -1 if no found
 * @param array
 * @param objectPropertyName
 * @param val
 */
function findIdx(array, objKeyName, keyVal) {
	var i;
	for(i=0;i<array.length;i++) {
		if(array[i]!=null && array[i][objKeyName] == keyVal) {
			return i;
		}
	}
	return -1;
}

function keyDownClose() {
	$(document).keydown(function(e) {
		try {
		    // ESCAPE key pressed
		    if (e.keyCode == 27) {
		    	art.dialog.close();
		    }
		}
		catch(e) {
			//console.log("*** key down error:" + e);
		}
	});
}
