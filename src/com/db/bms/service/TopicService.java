
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Album;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Topic;
import com.db.bms.sync.portal.protocal.GetTopicREQT;
import com.db.bms.sync.portal.protocal.GetTopicRESP;


public interface TopicService {

	Topic findTopicById(Long topicId) throws Exception;
	
	List<Topic> findTopicsById(Long[] topicIds) throws Exception;
	
	List<Topic> findTopics(Topic search) throws Exception;

	/**
	 * query topic and its strategy
	 * @param search
	 * @return
	 * @throws Exception
	 */
	List<Topic> findTopicWithStrategy(Topic topic) throws Exception;

	void addTopic(Topic topic) throws Exception;
	
	void updateTopic(Topic topic) throws Exception;
	
	void auditTopic(Integer status, Long[] topicIds, boolean acascade, boolean pcascade,ResourcePublishMap publishMap) throws Exception;
	
	void deleteTopics(Long[] topicIds) throws Exception;
	
	List<Album> findResourceAlbums(Integer type,Long resourceId, Album search) throws Exception;
	
	List<Album> findResourceAlbumsNoSelect(Integer type, Long resourceId, Album search) throws Exception;
	
	List<Topic> findAllTopics(Topic search) throws Exception;
	
	GetTopicRESP getTopicList(GetTopicREQT request) throws Exception;
	
	void addResourceAlbumMap(Integer type, Long resourceId, Long[] albumNos,Long createdBy) throws Exception;
	
	void deleteResourceAlbumMapsByAlbumId(Integer type,Long resourceId, Long[] albumNos) throws Exception;
	
	boolean isReferenceResource(Long[] albumNos) throws Exception;
	
	void publishUpdate(ResourcePublishMap publish) throws Exception;
	
	List<Topic> findAllResourceTopicNoPublishs(Long[] resourceIds, Topic topic) throws Exception;
	
	List<Topic> findResourceTopicNoPublishs(Long resourceId, Topic topic) throws Exception;
	
	Integer getTopicCountByCompanyNo(Long companyNo)throws Exception;
	
	List<Album> findColumnAlbumsNoSelect(Integer type, Long resourceId, Album search,Long topicId) throws Exception;
}
