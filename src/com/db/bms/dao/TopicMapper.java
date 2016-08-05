
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Topic;


public interface TopicMapper {

	/**
	 * find topic by primary key (PK)
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	Topic findTopicById(Long topicId) throws Exception;
	
	List<Topic> findTopicsById(Long[] topicIds) throws Exception;
	
	Integer findTopicCount(@Param(value = "topic")Topic search) throws Exception;	
	List<Topic> findTopics(@Param(value = "topic")Topic search) throws Exception;

	List<Topic> findTopicWithStrategy(@Param(value = "topic")Topic search) throws Exception;
	Integer findTopicWithStrategyCount(@Param(value = "topic")Topic search) throws Exception;
		
	Integer addTopic(@Param(value = "topic")Topic topic) throws Exception;
	
	Integer updateTopic(@Param(value = "topic")Topic topic) throws Exception;
	
	Integer updateTopicStatus(@Param(value = "status")Integer status, @Param(value = "ids")Long[] topicIds, @Param(value = "updateTime")String updateTime) throws Exception;
	
	Integer deleteTopicsById(@Param(value = "ids")Long[] ids) throws Exception;
	
	List<Topic> findAllTopics(@Param(value = "topic")Topic search) throws Exception;
	
	Integer findResourceTopicNoPublishCount(@Param(value = "resourceIds")Long[] resourceId, @Param(value = "topic")Topic topic) throws Exception;
	
	List<Topic> findResourceTopicNoPublishs(@Param(value = "resourceIds")Long[] resourceId, @Param(value = "topic")Topic topic) throws Exception;
	
	Integer getTopicCountByTemplateId(Long templateId) throws Exception;
}
