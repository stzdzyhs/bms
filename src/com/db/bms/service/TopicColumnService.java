
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.TopicColumn;
import com.db.bms.sync.portal.protocal.GetMenuREQT;
import com.db.bms.sync.portal.protocal.GetMenuRESP;


public interface TopicColumnService {

	TopicColumn findColumnById(Long columnId) throws Exception;
	
	List<TopicColumn> findColumnsById(Long[] columnIds) throws Exception;
	
	List<TopicColumn> findColumns(TopicColumn search) throws Exception;
	
	List<TopicColumn> findAllColumns(TopicColumn search) throws Exception;
	
	void addColumn(TopicColumn column) throws Exception;
	
	void updateColumn(TopicColumn column) throws Exception;
	
	void deleteColumns(Long[] columnIds) throws Exception;

	GetMenuRESP getMenuList(GetMenuREQT request) throws Exception;
	
	List<TopicColumn> findResourceColumnNoPublishs(Long resourceId, TopicColumn search) throws Exception;
	
	List<TopicColumn> findAllResourceColumnNoPublishs(Long[] resourceIds, TopicColumn search) throws Exception;
	
	List<TopicColumn> findAllResourceColumnNoPublishsWithTopicIds(Long[] topicIds,Long[] resourceIds, TopicColumn search) throws Exception;
}
