function flashChecker() {
	var hasFlash = 0; //是否安装了flash
	var flashVersion = 0; //flash版本
	var isIE = /*@cc_on!@*/0; //是否IE浏览器
	if (isIE) {
		var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
		if (swf) {
			hasFlash = 1;
			VSwf = swf.GetVariable("$version");
			flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
		}
	} else {
		if (navigator.plugins && navigator.plugins.length > 0) {
			var swf = navigator.plugins["Shockwave Flash"];
			if (swf) {
				hasFlash = 1;
				var words = swf.description.split(" ");
				for (var i = 0; i < words.length; ++i) {
					if (isNaN(parseInt(words[i]))) continue;
					flashVersion = parseInt(words[i]);
				}
			}
		}
	}
	return {
		f: hasFlash,
		v: flashVersion
	};
}
function DjwlSWF(){
	if(typeof(projectPath)=="undefined" || projectPath==null){
		projectPath = "../..";
	}
	var flashUrl = projectPath+"/js/swfupload/Flash/swfupload.swf";
	if(flashChecker().v<10){
		flashUrl = projectPath+"/js/swfupload/Flash/swfupload_fp9.swf";
	}
	var swfObj = {
		djwl_upload_url: projectPath+"/admin/system/companyUploadFile11.action",
		djwl_size_limit: "50 MB",	// 单个文件的大小限制  
		djwl_types: "*.jpg;*.gif;*.png;*.bmp",
		djwl_types_description: "文件类型",
		djwl_upload_limit: 0,
		getPostParam: "",
		djwlCallBackFun: "",
		djwlBeforeUpload: "",
		djwl_btnImg: projectPath+"/js/swfupload/2.png",
		djwl_btnHeight: 22,
		djwl_btnWidth: 71,
		loadingEle:"",
		swfu:"",
		/** 
	     * 初始化 
	     * @param {Object} swfu 
	     */
		init: function(param, fun) {
			if (!param.ele) {
				alert('上传控件初始化出错，请刷新页面');
				return null;
			}
			this.djwl_types = param.fileType || this.djwl_types;
			this.djwl_upload_limit = param.fileCount || this.djwl_upload_limit;
			this.djwl_size_limit = param.fileSizeLimit || this.djwl_size_limit;
			this.djwl_upload_url = param.uploadurl || this.djwl_upload_url;
			this.djwl_btnImg = param.img || this.djwl_btnImg;
			this.djwl_btnHeight = param.height || this.djwl_btnHeight;
			this.djwl_btnWidth = param.width || this.djwl_btnWidth;
			this.djwlCallBackFun = fun || this.djwlCallBackFun;
			this.djwlBeforeUpload = param.beforeUpload || this.djwlBeforeUpload;
			this.getPostParam = param.getPostParam || this.getPostParam;
			this.loadingEle = param.loadingEle || this.loadingEle;
			this.swfu = new SWFUpload({
				upload_url: swfObj.djwl_upload_url,
				file_size_limit: swfObj.djwl_size_limit,
				file_types: swfObj.djwl_types,
				file_types_description: swfObj.djwl_types_description,
				file_upload_limit: swfObj.djwl_upload_limit,
	
				//file_dialog_start_handler: DjwlHandle.fileDialogStart,
				swfupload_preload_handler: DjwlHandle.preLoad,
				swfupload_load_failed_handler: DjwlHandle.loadFailed,
				file_queued_handler: DjwlHandle.fileQueued,
				file_queue_error_handler: DjwlHandle.fileQueueError,
				file_dialog_complete_handler: DjwlHandle.fileDialogComplete,
				upload_error_handler: DjwlHandle.uploadError,
				//upload_success_handler: function(file, serverData){swfObj.djwlswfcallback(serverData, file)},
				upload_success_handler: DjwlHandle.uploadSuccess,
				upload_complete_handler: DjwlHandle.uploadComplete,
				upload_progress_handler:param.uploadProgress || DjwlHandle.uploadProgress,
	
				button_image_url: swfObj.djwl_btnImg || "",
				button_width: swfObj.djwl_btnWidth || 66,
				button_height: swfObj.djwl_btnHeight || 26,
				button_placeholder_id: param.ele.id,
				button_text: param.ele.innerHTML || "浏览",
				button_text_style: '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; text-align:center } .buttonSmall { font-size: 10pt; }',
				button_text_top_padding: param.paddingTop || 1,
				button_text_left_padding: param.paddingLeft || 23,
				button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
				button_cursor: SWFUpload.CURSOR.HAND,
	
				// Flash Settings  
				flash_url: param.flash || flashUrl,
	
				// A container where developers can place their own settings associated with this instance.  
				//custom_settings: {
				//	upload_target : "divFileProgressContainer"  
				//},
	
				// Debug Settings  
				debug: false
			});
		}
	}
	/** 
	 * DjwlHandle 
	 * @author copy of handlers.js 
	 */
	var DjwlHandle = {
		preLoad: function() {
			if (!this.support.loading) {
				alert("你当前的Flash版本太低导致不能正常使用上传控件，请安装Flash Player 9或以上版本.");
				return false;
			}
		},
		loadFailed: function() {
			alert("上传控件初始化出错，请刷新页面");
		},
		fileQueueError: function(file, errorCode, message) {
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
			} catch(ex) {
				this.debug(ex);
			}
		},
		fileQueued: function(file){
			var queue = this.customSettings.queue || new Array();
			queue.push(file);
			this.customSettings.queue = queue;
		},
		fileDialogComplete: function(numFilesSelected, numFilesQueued) {
			try {
				if (numFilesQueued > 0) {
					if(typeof(swfObj.djwlBeforeUpload)=="function"){
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
			} catch(ex) {
				this.debug(ex);
			}
		},
		
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