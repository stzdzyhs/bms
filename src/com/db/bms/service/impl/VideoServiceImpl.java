
package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.VideoMapper;
import com.db.bms.entity.Video;
import com.db.bms.entity.Video.VideoStatus;
import com.db.bms.service.VideoService;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.VideoCutReportREQT;
import com.db.bms.sync.portal.protocal.VideoCutReportREQT.ReportStatusCode;
import com.db.bms.sync.portal.protocal.VideoCutReportRESP;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("videoService")
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideoMapper videoMapper;
	
	@Override
	public Video findVideoById(Long videoId) throws Exception {
		return videoMapper.findVideoById(videoId);
	}

	@Override
	public List<Video> findVideosById(Long[] videoIds) throws Exception {
		return videoMapper.findVideosById(videoIds);
	}

	@Override
	public List<Video> findVideos(Video search) throws Exception {
		PageUtil page = search.getPageUtil();
		int count = videoMapper.findVideoCount(search);
		page.setRowCount(count);
		return videoMapper.findVideos(search);
	}

	@Override
	public void addVideo(Video video) throws Exception {
		videoMapper.addVideo(video);
	}

	@Override
	public void updateVideo(Video video) throws Exception {
		videoMapper.updateVideo(video);
	}

	@Override
	public void deleteVideos(Long[] videoIds) throws Exception {
		videoMapper.deleteVideosById(videoIds);
	}

	@Override
	public VideoCutReportRESP videoCutReport(VideoCutReportREQT request)
			throws Exception {
		VideoCutReportRESP response = new VideoCutReportRESP();
		response.setSerialNo(request.getSerialNo());
		response.setAssetId(request.getAssetId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		
		Video video = videoMapper.findVideoByAssetId(request.getAssetId());
	    if (video == null){
			response.setResultCode(CommonResultCode.NOT_FOUND_ASSET_ID.getResultCode());
			response.setResultDesc("Could not find the [assetId= " + request.getAssetId() + "].");
			return response;
	    }
	    
	    video.setUpdateTime(DateUtil.getCurrentTime());
	    video.setOriginResult(request.getResultCode());
	    video.setFailReason(request.getResultDesc());
	    switch (ReportStatusCode.getResultCode(request.getResultCode())){
	    case ASSET_ID_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("资产ID错误！");
	    	}
	    	break;
	    case SOURCE_URL_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("视频下载地址错误！");
	    	}
	    	break;
	    case WIDTH_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("图片宽度错误！");
	    	}
	    	break;
	    case HEIGHT_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("图片高度错误！");
	    	}
	    	break;
	    case INTERVAL_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("图片高度错误！");
	    	}
	    	break;
	    case REPORT_URL_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("截图报告地址错误！");
	    	}
	    	break;
	    case INJECT_IMAGE_URL_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("注入图片地址错误！");
	    	}
	    	break;
	    case DOWNLOAD_FILE_FAILED:
	    	video.setStatus(VideoStatus.DOWNLOAD_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("下载文件失败！");
	    	}
	    	break;
	    case VIDEO_FILE_FORMAT_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("视频文件格式错误！");
	    	}
	    	break;
	    case CAPTURE_IMAGE_ERROR:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("截图发生错误！");
	    	}
	    	break;
	    case CAPTURE_IMAGE_FAILED:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("截图失败！");
	    	}
	    	break;
	    case DOWNLOADING_VIDEO:
	    	video.setStatus(VideoStatus.DOWNLOADING.getIndex());
	    	break;
	    case DOWNLOADING_VIDEO_SUCCESS:
	    	video.setStatus(VideoStatus.CAPTURING.getIndex());
	    	break;
	    case CAPTURE_IAMGE_SUCCESS:
	    	video.setStatus(VideoStatus.INJECTING.getIndex());
	    	break;
	    case CAPTURE_IMAGE_FINISHED:
	    	video.setStatus(VideoStatus.CAPTURE_FINISHED.getIndex());
	    	break;
	    case OTHER:
	    	video.setStatus(VideoStatus.CAPTURE_FAILED.getIndex());
	    	if (StringUtils.isEmpty(video.getFailReason())){
	    		video.setFailReason("截图系统内部错误！");
	    	}
	    	break;
	    }
	    
	    videoMapper.updateVideo(video);
		return response;
	}

}
