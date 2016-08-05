
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Album;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.sync.portal.protocal.GetAlbumREQT;
import com.db.bms.sync.portal.protocal.GetAlbumRESP;


public interface AlbumService {
	
	Album findAlbumById(Long albumNo) throws Exception;
	
	List<Album> findAlbumsById(Long[] albumNos) throws Exception;
	
	List<Album> findAlbums(Album search) throws Exception;
	
	void addAlbum(Album album) throws Exception;
	
	void updateAlbum(Album album) throws Exception;
	
	void auditAlbum(Integer status, Long[] albumNos, Long topicId, boolean pcascade, Integer mapType,ResourcePublishMap publish) throws Exception;
	
	void deleteAlbums(Long[] albumNos) throws Exception;
	
	GetAlbumRESP getAlbumList(GetAlbumREQT request) throws Exception;
	
	List<Album> findAllResourceAlbum(Integer type, Integer status, Long[] resourceIds) throws Exception;
	
	void albumSinglePublish(Integer status, Long[] parentIds,ResourcePublishMap publish) throws Exception;
	
	List<Album> findAllResourceAlbums(Integer type,Long resourceId, Album search) throws Exception;
	
	List<Album> findAllTopicPublishAlbums(Album search, Integer parentType, Long parentId, Integer type) throws Exception;
	
	Album findAlbumByAlbumId(String albumId) throws Exception; 
	
	List<Album> findAlbumByNo(Operator operatorNo) throws Exception;
	
	/**
	 * 相册策略
	 */
	List<Album> findAlbumWithStrategy(Album album) throws Exception;
	
	Integer getAlbumCountByCompanyNo(Long companyNo)throws Exception;
	
	
	/**
	 * unpublish album 
	 * @param topicNo - topic no. NOT null
	 * @param albumNos - album nos. not null
	 * @param noticeList - must not null
	 * @return - 
	 */
	public void unpublishAlbum(Long topicNo, Long[] albumNos, List<PortalPublishNotice> noticeList) throws Exception;

}
