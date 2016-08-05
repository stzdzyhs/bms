
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Video;


public interface VideoMapper {

	Video findVideoById(Long videoId) throws Exception;
	
	List<Video> findVideosById(Long[] videoIds) throws Exception;
	
	Video findVideoByAssetId(@Param(value = "assetId")String assetId) throws Exception;
	
	Integer findVideoCount(Video search) throws Exception;
	
	List<Video> findVideos(Video search) throws Exception;
	
	Integer addVideo(Video video) throws Exception;
	
	Integer updateVideo(Video video) throws Exception;
	
	Integer deleteVideosById(Long[] videoIds) throws Exception;
}
