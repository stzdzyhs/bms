
/**
 * call this to set default handler of uploader
 * @param settting
 * @returns
 */
function MyFileUpload(settting) {

	// default handler
	
	this.defSwfUploadPreLoad = function() {
		if (!this.support.loading) {
			alert("你当前的Flash版本太低导致不能正常使用上传控件，请安装Flash Player 9或以上版本.");
			return false;
		}
	};
	
	this.defSwfUploadLoadFailed =  function () {
		alert("上传控件初始化出错，请刷新页面");
	};

	this.defFileQueueError = function (file, errorCode, message) {
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
					art.dialog.tips(errorName, 3);
				}else{
					alert(errorName);
				}
			}
		} 
		catch(ex) {
			this.debug(ex);
		}
	}

	this.defFileQueued = function (file) {
		var queue = this.customSettings.queue || new Array();
		queue.push(file);
		this.customSettings.queue = queue;
	};


	this.defFileDialogComplete = function (numFilesSelected, numFilesQueued) {
		try {
			if (numFilesQueued > 0) {
				if(typeof(this.swfObj.djwlBeforeUpload)=="function"){
					var queue = this.customSettings.queue || new Array();
					if(!swfObj.djwlBeforeUpload(queue)){
				        while (queue.length > 0) {
				            this.cancelUpload(queue.pop().id, false);
				        }
				        return false;
					}
				}
					
				if(swfObj.loadingEle!=""){
					swfObj.loadingEle.innerHTML = swfObj.loadingEle.innerHTML + '<img src="'+projectPath+'/images/loading.gif" width="25" height="25" id="loading" />';
				}
				if(typeof(swfObj.getPostParam)=="function"){
					var postParam = swfObj.getPostParam();
					swfObj.swfu.setPostParams(postParam);
				}
				this.startUpload();
			}
		}
		catch(ex) {
			this.debug(ex);
		}
	}
	
	
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
	if(upload_complete_handler==null) {
		setting.upload_complete_handler = defUploadComplete;
	}
	if(setting.queue_complete_handler==null) {
		setting.queue_complete_handler = defqQueueComplete;
	}

	this.upload = new FileUpload(setting);
}

	
	
	
}


		
		uploadProgress: function(file, bytesLoaded) {
			try {
				var percent = Math.ceil((bytesLoaded / file.size) * 100);
				var progress = new FileProgress(file, this.customSettings.upload_target);
				progress.setProgress(percent);
				if (percent === 100) {
					progress.setStatus("Creating thumbnail...");
					progress.toggleCancel(false, this);
				} else {
					progress.setStatus("Uploading...");
					progress.toggleCancel(true, this);
				}
			} catch(ex) {
				this.debug(ex);
			}
		},
	
		uploadSuccess: function(file, serverData) {
			try {
				if(swfObj.loadingEle!=""){
					var lastChild = swfObj.loadingEle.lastChild;
					if("loading"==lastChild.id){
						swfObj.loadingEle.removeChild(lastChild);
					}
				}
				swfObj.djwlCallBackFun(serverData, file);
			} catch(ex) {
				this.debug(ex);
			}
		},
	
		uploadComplete: function(file) {
			try {
				//  I want the next upload to continue automatically so I'll call startUpload here  
				var queue = this.customSettings.queue || new Array();
				for(var i in queue){
					if(queue[i].id==file.id){
						queue.splice(i, 1);
						break;
					}
				}
				if (this.getStats().files_queued > 0) {
					if(typeof(swfObj.djwlBeforeUpload)=="function"){
						if(!swfObj.djwlBeforeUpload(queue)){
							//var queue = this.customSettings.queue || new Array();
					        while (queue.length > 0) {
					            this.cancelUpload(queue.pop().id, false);
					        }
					        return false;
						}
					}
					if(swfObj.loadingEle!=""){
						swfObj.loadingEle.innerHTML = swfObj.loadingEle.innerHTML + '<img src="'+projectPath+'/images/loading.gif" width="25" height="25" id="loading" />';
					}
					this.startUpload();
				}
			} catch(ex) {
				this.debug(ex);
			}
		},
	
		uploadError: function(file, errorCode, message) {
			if(swfObj.loadingEle!=""){
				var lastChild = swfObj.loadingEle.lastChild;
				if("loading"==lastChild.id){
					swfObj.loadingEle.removeChild(lastChild);
				}
			}
			var errMsg = "";
			switch (errorCode) {
				case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
					errMsg = "网络有问题，请重试!";
					break;
				case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
					errMsg = "上传配置有误!";
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
					errMsg = "网络有问题，请重试!";
					break;
				case SWFUpload.UPLOAD_ERROR.IO_ERROR:
					errMsg = "网络有问题，请重试!";
					break;
				case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
					errMsg = "浏览器安全设置过高，导致无法上传!";
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
					errMsg = "文件大小不能超过"+swfObj.djwl_size_limit+"!";
					break;
				case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
					errMsg = "无法获取上传文件，请检查文件是否存在!";
					break;
				case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
					errMsg = "验证失败!";
					break;
				case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
					errMsg = "已取消上传!";
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
					errMsg = "上传中止!";
					break;
				default:
					errMsg = "未知错误!";
					break;
			}
			if(typeof(art)=="function"){
				art.dialog.tips(file.name+'上传失败。'+errMsg, 3);
			}else{
				alert(file.name+'上传失败。'+errMsg);
			}
		}
	};
	return swfObj;
};