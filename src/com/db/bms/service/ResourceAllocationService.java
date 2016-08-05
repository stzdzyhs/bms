
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;


public interface ResourceAllocationService {

	void addResourceAllocation(Operator curOper,Integer type, Long resourceId, Long[] operatorNos,String cmdStr) throws Exception;
	
	void deleteResourceAllocation(Integer type, Long operatorNo, Long[] resourceIds, Long[] operatorNos) throws Exception;
	
	List<Long> findAllocResourceIds(Integer type, Long operatorNo) throws Exception;
	
	List<Long> findAllocResourceIdsFilterCmd(Integer type, Long operatorNo,String cmdStr) throws Exception;
	
	List<ResourceAllocation> findAllocResourceByIds(Long[]resourceIds) throws Exception;
}
