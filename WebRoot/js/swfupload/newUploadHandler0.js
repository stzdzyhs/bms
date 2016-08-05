var projectPath;
if(typeof(projectPath)=="undefined" || projectPath==null){
	projectPath = "../..";
}

//default handler

function defSwfUploadPreLoad() {
	if (!this.support.loading) {
		alert("你当前的Flash版本太低导致不能正常使用上传控件，请安装Flash Player 9或以上版本.");
		return false;
	}
};

function defSwfUploadLoaded() {
	//alert("loaded");
};
	
function defSwfUploadLoadFailed() {
	alert("上传控件初始化出错，请刷新页面");
};

function defFileQueueError(file, errorCode, message) {
	this.errCnt++;
	
	try {
		var imageName = "error.gif";
		var errorName = "";
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			errorName = "上传文件数量超过限制，还能再上传"+message+"个文件.";
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			errorName = "不能上传大小是0的文件";
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			errorName = "上传文件大小不能超过"+swfObj.djwl_size_limit+"!";
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			errorName = "文件类型不支持!";
			break;
		default:
			errorName = message;
			break;
		}
		if (errorName !== "") {
			if(typeof(art)=="function"){
				//art.dialog.tips(errorName, 3);
			}
			else{
				//alert(errorName);
			}
		}
	} 
	catch(ex) {
		this.debug(ex);
	}
}

function  defFileQueued(file) {
	try {
		var queue = this.customSettings.queue || new Array();
		queue.push(file);
		this.customSettings.queue = queue;
	}
	catch(e) {
		alert(e);
	}
};

function defFileDialogComplete (numFilesSelected, numFilesQueued) {
	try {
		var queue = this.customSettings.queue || new Array();
		this.customSettings.queue = queue;
		
		this.errCnt=0;
		this.serverData={};
		this.fileSeq = 0;
		
		if(this.settings.file_queue_limit !=0) {
			if(numFilesSelected>this.settings.file_queue_limit) {
				//art.dialog.alert("请只选择" + this.settings.file_queue_limit + "个文件");
				return false;
			}
		}
		
		if(this.beforeUpload!=null) {
			var queue = this.customSettings.queue || new Array();
			var t = this.beforeUpload(queue);
			if(t!=true) {
				// clear queue
				queue.length = 0;
				return true;
			}
		}
		
		this.startUpload();
	}
	catch(ex) {
		alert(ex);
	}
}



function concatParam(url, param) {
	if(url.indexOf("?")!=-1) {
		url = url + "&" + param;
	}
	else {
		url = url + "?" + param;
	}
	return url;
}

function defUploadStart (file) {
	try {
		this.fileSeq ++;
	
		var url = concatParam(this.customSettings.uploadUrl0, "fileSeq=" + this.fileSeq);
		this.setUploadURL(url);
	}
	catch(e){
		alert("Error: " + e)
	}
}


function defUploadProgress(file, bytesLoaded) {
	try {
		var percent = Math.ceil((bytesLoaded / file.size) * 100);
		
		var d = art.dialog({
			id: 'dlgUploading',
			title: "上传文件:" + file.name + " " + percent + "%",
		    content: '<img src="' + projectPath + '/images/08.gif" />',
		    height:150,
		    lock: true
		});
		
		d.show();
	} 
	catch(ex) {
		alert(ex);
	}
}

function defUploadSuccess(file, serverData) {
	try {
		var rsObj = eval("("+serverData+")");
		
		this.serverData = rsObj;
		
		if (rsObj!="undefined") {
			if (rsObj.result == 'true' || rsObj.result == true){
			}
			else {
				this.errCnt++;
			}
		}
		else{
			this.errCnt++;
		}
	}
	catch(ex) {
		alert(ex);
	}
}

function defUploadError(file, errorCode, message) {
	this.errCnt++;
	
	// clear queue
	var queue = this.customSettings.queue || new Array();
	queue.length = 0;
	
	this.serverData.result = false;
	
	var errMsg = "";
	switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			this.serverData.desc = "网络有问题，请重试!";
			break;
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
			this.serverData.desc = "上传配置有误!";
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			this.serverData.desc = "网络有问题，请重试!";
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			this.serverData.desc = "网络有问题，请重试!";
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			this.serverData.desc = "浏览器安全设置过高，导致无法上传!";
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			this.serverData.desc = "文件大小不能超过"+swfObj.djwl_size_limit+"!";
			break;
		case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
			this.serverData.desc = "无法获取上传文件，请检查文件是否存在!";
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			this.serverData.desc = "验证失败!";
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			this.serverData.desc = "已取消上传!";
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			this.serverData.desc = "上传中止!";
			break;
		default:
			this.serverData.desc = "未知错误!";
			break;
	}
	
//	if(typeof(art)=="function"){
//		art.dialog.tips(file.name+'上传失败。'+errMsg, 3);
//	}
//	else{
//		art.dialog.tips(file.name+'上传失败。'+errMsg);
//	}
}

function defUploadComplete(file) {
	try {
		//  I want the next upload to continue automatically so I'll call startUpload here  
		var queue = this.customSettings.queue || new Array();
		for(var i in queue){
			if(queue[i].id==file.id){
				queue.splice(i, 1);
				break;
			}
		}
		
		if(this.errCnt==0) {
			if (queue.length > 0) {
				this.startUpload();
			}
			else {
				var d = art.dialog({id: 'dlgUploading'});
				d.close();
				
				// invoke callback 
				if(this.afterUploadSucc!=null) {
					this.afterUploadSucc();
				}
				//art.dialog.alert("上传文件成功！", this.afterUploadSucc);
			}
		}
		else {
			var d = art.dialog({id: 'dlgUploading'});
			d.close();
			
			if(this.serverData.desc!=null) {
				art.dialog.alert("上传文件失败:" + this.serverData.desc );
			}
			else {
				art.dialog.alert("上传文件失败");
			}
		}
	} 
	catch(ex) {
		alert(ex);
	}
}

/**
 * call this to set default handler of upload
 * @param settting
 * @returns
 */
function createFileUpload(setting) {
	
	if(setting.swfupload_preload_handler==null) {
		setting.swfupload_preload_handler = defSwfUploadPreLoad;
	}
	if(setting.swfupload_load_failed_handler==null) {
		 setting.swfupload_load_failed_handler = defSwfUploadLoadFailed; 
	}
	if(setting.swfupload_loaded_handler==null) {
		setting.swfupload_loaded_handler = defSwfUploadLoaded; 
	}
	if(setting.file_queued_handler==null) {
		setting.file_queued_handler = defFileQueued;
	}
	if(setting.file_queue_error_handler==null) {
		setting.file_queue_error_handler =defFileQueueError; 
	}
	if( setting.file_dialog_complete_handler==null) {
		setting.file_dialog_complete_handler=defFileDialogComplete;
	}
	if(setting.upload_start_handler==null) {
		setting.upload_start_handler = defUploadStart;
	}
	if(setting.upload_progress_handler==null) {
		setting.upload_progress_handler = defUploadProgress;
	}
	if(setting.upload_error_handler==null) {
		setting.upload_error_handler = defUploadError;
	}
	if(setting.upload_success_handler ==null) {
		setting.upload_success_handler = defUploadSuccess;
	}
	if(setting.upload_complete_handler==null) {
		setting.upload_complete_handler = defUploadComplete;
	}
	
	/* no this event
	if(setting.queue_complete_handler==null) {
		setting.queue_complete_handler = defqQueueComplete;
	}
	*/

	var upload = new SWFUpload(setting);
	upload.customSettings.uploadUrl0 = setting.upload_url;
	return upload;
}
