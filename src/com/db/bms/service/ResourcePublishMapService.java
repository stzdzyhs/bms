
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourcePublishMap;


public interface ResourcePublishMapService {
	
	ResourcePublishMap findResourcePublishMapsById(Long id) throws Exception;
	
	List<ResourcePublishMap> findResourcePublishMapById(Long[] ids) throws Exception;

	/**
	 * find resource publsh map. [parentType, parentId, type, resourceId]
	 * @param parentType
	 * @param parentId
	 * @param type
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	List<ResourcePublishMap> findResourcePublishMapById(Integer parentType, Long parentId, Integer type, Long resourceId) throws Exception;
	
	/**
	 * find resource-publish-map data by rpm.[parentType, parentId, type, resourceId], 支持分页
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	List<ResourcePublishMap> findResourcePublishMapByResId2(ResourcePublishMap rpm) throws Exception;

	/**
	 * find published picture. picture data is set in ResourcePublishMap.
	 * rpm.sortKey can be createTime, frameNum, rpm.sortType asc/desc
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	List<ResourcePublishMap> findPublishedPicture(ResourcePublishMap rpm) throws Exception;


	void addResourcePublishMap(ResourcePublishMap map) throws Exception;
	
	void deleteResourcePublishMaps(Integer parentType, Long parentId, Integer type, Long resourceId) throws Exception;
	
	List<ResourcePublishMap> findResourcePublishMaps(ResourcePublishMap publish) throws Exception;
	
	void deleteResourcePublishMapsById(Operator curOper, Long[] ids) throws Exception;	
}
