
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.ResourceAllocation;


public interface ResourceAllocationMapper {

	Integer addResourceAllocation(ResourceAllocation entity);
	
	Integer deleteResourceAllocation(@Param(value = "type")Integer type, @Param(value = "operatorNo")Long operatorNo, 
			@Param(value = "resourceIds")Long[] resourceIds,@Param(value = "operatorNos")Long[] operatorNos) throws Exception;
	
	List<Long> findAllocResourceIds(@Param(value = "type")Integer type, @Param(value = "operatorNo")Long operatorNo) throws Exception;
	
	List<Long> findAllocResourceIdsFilterCmd(@Param(value = "type")Integer type, @Param(value = "operatorNo")Long operatorNo,@Param(value = "cmdStr")String cmdStr) throws Exception;
	
	List<ResourceAllocation> findAllocResourceByIds(@Param(value = "resourceIds")Long[] resourceIds) throws Exception;
}
