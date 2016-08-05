
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Video;
import com.db.bms.sync.portal.protocal.VideoCutReportREQT;
import com.db.bms.sync.portal.protocal.VideoCutReportRESP;


public interface VideoService {

	Video findVideoById(Long videoId) throws Exception;
	
	List<Video> findVideosById(Long[] videoIds) throws Exception;
	
	List<Video> findVideos(Video search) throws Exception;
	
	void addVideo(Video video) throws Exception;
	
	void updateVideo(Video video) throws Exception;
	
	void deleteVideos(Long[] videoIds) throws Exception;
	
	VideoCutReportRESP videoCutReport(VideoCutReportREQT request) throws Exception;
}
