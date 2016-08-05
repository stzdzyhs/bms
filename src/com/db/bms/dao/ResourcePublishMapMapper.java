
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.ResourcePublishMap;


public interface ResourcePublishMapMapper {
	
	ResourcePublishMap findResourcePublishMapsById(Long id) throws Exception;
	
	List<ResourcePublishMap> findResourcePublishMapById(Long[] ids) throws Exception;


	/**
	 * find resource-publish-map data by [parentType, parentId, type, resourceId] 
	 * @param parentType
	 * @param parentId
	 * @param type
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	List<ResourcePublishMap> findResourcePublishMapByResId(@Param(value = "parentType")Integer parentType, @Param(value = "parentId")Long parentId,
			@Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId ) throws Exception;

	
	/**
	 * find resource-publish-map data by rpm.[parentType, parentId, type, resourceId], 支持分页
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	List<ResourcePublishMap> findResourcePublishMapByResId2(@Param(value = "rpm")ResourcePublishMap rpm) throws Exception;	
	Integer findResourcePublishMapByResId2Count(@Param(value = "rpm")ResourcePublishMap rpm) throws Exception;

	/**
	 * find published picture. picture data is set in ResourcePublishMap.
	 * rpm.sortKey can be createTime, frameNum, rpm.sortType asc/desc
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	List<ResourcePublishMap> findPublishedPicture(@Param(value = "rpm")ResourcePublishMap rpm) throws Exception;
	/**
	 * find published picture count
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	Integer findPublishedPictureCount(@Param(value = "rpm")ResourcePublishMap rpm) throws Exception;
	

	Integer addResourcePublishMap(ResourcePublishMap map) throws Exception;
	
	Integer updateResourcePublishMap(ResourcePublishMap publish) throws Exception;
	
	Integer updateResourcePublishMapByParentId(ResourcePublishMap publish) throws Exception;
	
	/**
	 * delete resource-publish-map data by [parentType, parentId, type, resourceId] 
	 * @param parentType
	 * @param parentId
	 * @param type
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	Integer deleteResourcePublishMaps(@Param(value = "parentType")Integer parentType, @Param(value = "parentId")Long parentId, 
			@Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId) throws Exception;
	
	Integer findResourcePublishMapCount(ResourcePublishMap publish) throws Exception;
	
	List<ResourcePublishMap> findResourcePublishMaps(ResourcePublishMap publish) throws Exception;
	
	Integer deleteResourcePublishMapsById(Long[] ids) throws Exception;
	
	Integer getPublishCountByStrategyNo(@Param(value = "strategyNo")Long strategyNo)throws Exception;
}
