
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.TopicColumn;


public interface TopicColumnMapper {

	TopicColumn findColumnById(Long columnId) throws Exception;
	
	List<TopicColumn> findColumnsById(Long[] columnIds) throws Exception;
	
	Integer findColumnCount(TopicColumn search) throws Exception;
	
	List<TopicColumn> findColumns(TopicColumn search) throws Exception;
	
	Integer getColumnCountByName(TopicColumn search) throws Exception;
	
	Integer addColumn(TopicColumn column) throws Exception;
	
	Integer updateColumn(TopicColumn column) throws Exception;
	
	Integer deleteColumnsById(Long[] columnIds) throws Exception;
	
	Integer deleteColumnByTopicIds(Long[] topicIds) throws Exception;
	
	Integer findResourceColumnNoPublishCount(@Param(value = "resourceIds")Long[] resourceIds, @Param(value = "col")TopicColumn search) throws Exception;
	
	List<TopicColumn> findResourceColumnNoPublishs(@Param(value = "resourceIds")Long[] resourceIds, @Param(value = "col")TopicColumn search) throws Exception;
	
	List<TopicColumn> findResourceColumnNoPublishsWithTopicIds(@Param(value = "topicIds")Long[] topicIds,@Param(value = "resourceIds")Long[] resourceIds, @Param(value = "col")TopicColumn search) throws Exception;
}
