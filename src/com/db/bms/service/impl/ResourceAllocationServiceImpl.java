
package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.ResourceAllocationMapper;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.utils.DateUtil;


@Service("resourceAllocationService")
public class ResourceAllocationServiceImpl implements ResourceAllocationService {

	@Autowired
	private ResourceAllocationMapper resAllocMapper;
	
	
	@Override
	public void addResourceAllocation(Operator curOper,Integer type, Long resourceId, Long[] operatorNos,String cmdStr) throws Exception {
		for (Long operatorNo : operatorNos){
			ResourceAllocation entity = new ResourceAllocation();
			entity.setType(type);
			entity.setResourceId(resourceId);
			entity.setOperatorNo(operatorNo);
			entity.setAllocBy(curOper.getOperatorNo());
			entity.setAllocTime(DateUtil.getCurrentTime());
			entity.setCmdStr(cmdStr);
			resAllocMapper.addResourceAllocation(entity);
		}

	}

	@Override
	public void deleteResourceAllocation(Integer type, Long operatorNo,
			Long[] resourceIds, Long[] operatorNos) throws Exception {
		resAllocMapper.deleteResourceAllocation(type, operatorNo, resourceIds,operatorNos);
	}

	@Override
	public List<Long> findAllocResourceIds(Integer type, Long operatorNo)
			throws Exception {
		return resAllocMapper.findAllocResourceIds(type, operatorNo);
	}
	
	@Override
	public List<Long> findAllocResourceIdsFilterCmd(Integer type, Long operatorNo,String cmdStr) throws Exception{
		return resAllocMapper.findAllocResourceIdsFilterCmd(type, operatorNo,cmdStr);
	}
	
	@Override
	public List<ResourceAllocation> findAllocResourceByIds(Long[] resourceIds) throws Exception{
		return this.resAllocMapper.findAllocResourceByIds(resourceIds);
	}

}
