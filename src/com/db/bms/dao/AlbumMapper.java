
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Album;


public interface AlbumMapper {

	Long getPrimaryKey() throws Exception;
	
	Album findAlbumByAlbumId(@Param(value = "albumId")String albumId) throws Exception;
	
	Album findAlbumById(Long albumNo) throws Exception;
	
	List<Album> findAlbumsById(Long[] albumNos) throws Exception;
	
	Integer findAlbumCount(Album search) throws Exception;
	
	List<Album> findAlbums(Album search) throws Exception;
	
	Integer addAlbum(Album album) throws Exception;
	
	Integer updateAlbum(Album album) throws Exception;
	
	Integer updateAlbumStatus(@Param(value = "status")Integer status, @Param(value = "albumNos")Long[] albumNos, @Param(value = "updateTime")String updateTime) throws Exception;
	
	Integer deleteAlbumsById(Long[] albumNos) throws Exception;
	
	Integer findResourceAlbumCount(@Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId, @Param(value = "abm")Album search) throws Exception;
	
	List<Album> findResourceAlbums(@Param(value = "type")Integer type,@Param(value = "resourceId")Long resourceId, @Param(value = "abm")Album search) throws Exception;
	
	Integer findResourceAlbumCountNoSelect(@Param(value = "type")Integer type,@Param(value = "resourceId")Long resourceId, @Param(value = "abm")Album search) throws Exception;
	
	List<Album> findResourceAlbumsNoSelect(@Param(value = "type")Integer type,@Param(value = "resourceId")Long resourceId, @Param(value = "abm")Album search) throws Exception;
	
	/**
	 * find all albums in this topic.   [type, status, resourceIds=topicIds]
	 * @param type
	 * @param status
	 * @param resourceIds
	 * @return
	 * @throws Exception
	 */
	List<Album> findAllResourceAlbum(@Param(value = "type")Integer type, @Param(value = "status")Integer status, @Param(value = "resourceIds")Long[] resourceIds) throws Exception;

	Integer findTopicPublishAlbumCount(@Param(value = "abm")Album search, @Param(value = "parentType")Integer parentType, 
			@Param(value = "parentId")Long parentId, @Param(value = "type")Integer type) throws Exception;
	
	/**
	 * find topic publish albums [parentType, parentId, type, parentIds]
	 * @param search
	 * @param parentType
	 * @param parentId
	 * @param type
	 * @param parentIds
	 * @return
	 * @throws Exception
	 */
	List<Album> findTopicPublishAlbums(@Param(value = "abm")Album search, @Param(value = "parentType")Integer parentType, 
			@Param(value = "parentId")Long parentId, @Param(value = "type")Integer type,@Param(value = "parentIds")Long[] parentIds) throws Exception;
	
	Integer getAlbumCountByTemplateId(Long templateId) throws Exception;
	
	/**
	 * 相册策略
	 */
	List<Album> findAlbumWithStrategy(@Param(value = "album")Album search) throws Exception;
	Integer findAlbumWithStrategyCount(@Param(value = "album")Album search) throws Exception;
	
	Integer findColumnAlbumCountNoSelect(@Param(value = "type")Integer type,@Param(value = "resourceId")Long resourceId, @Param(value = "abm")Album search,@Param(value = "topicId")Long topicId) throws Exception;
	
	List<Album> findColumnAlbumsNoSelect(@Param(value = "type")Integer type,@Param(value = "resourceId")Long resourceId, @Param(value = "abm")Album search,@Param(value = "topicId")Long topicId) throws Exception;
}
