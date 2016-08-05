
package com.db.bms.dao;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.ResourceAlbumMap;


public interface ResourceAlbumMapMapper {

	Integer addResourceAlbumMap(ResourceAlbumMap map) throws Exception;
	
	Integer deleteResourceAlbumMaps(@Param(value = "type")Integer type, @Param(value = "resourceIds")Long[] resourceIds) throws Exception;
	
	Integer deleteResourceAlbumMapsByAlbumId(@Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId, @Param(value = "albumNos")Long[] albumNos) throws Exception;
	
	Integer getResourceAlbumMapCount(@Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId, @Param(value = "albumNo")Long albumNo) throws Exception;
	
	Integer getResourceAlbumMapCountByAlbumId(Long[] albumNos) throws Exception;
}
