package com.db.bms.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.StrategyConditionMapper;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.service.StrategyConditionService;

@Service("strategyConditionService")
public class StrategyConditionServiceImpl implements StrategyConditionService {

	@Autowired
	StrategyConditionMapper strategyConditionMapper; 
	
	/**
	 * find strategy one kind of condition.
	 * search.type and search.strategyId must set !!!
	 * @param search
	 * @return
	 * @throws Exception
	 */
    public List<StrategyCondition> findStrategyConditionEntity(StrategyCondition search) throws Exception {
         int cnt = strategyConditionMapper.findStrategyConditionEntityCount(search);
         search.getPageUtil().setRowCount(cnt);
         List<StrategyCondition> list = strategyConditionMapper.findStrategyConditionEntity(search);  
         return list;
     }
    
	/**
	 * delete strategy condition by id (primary key).
	 * @param no
	 * @throws Exception
	 */
	public void delStrategyConditionById(@Param("id")Long id) throws Exception {
		this.strategyConditionMapper.delStrategyConditionById(id);
	}

	@Override
	public boolean isRefStrategyCondition(Long type, Long[] typeNos) throws Exception {
		int cnt = this.strategyConditionMapper.findStrategyConditionCountByTypeNos(type, typeNos);
		return (cnt>0?true:false);
	}
    
}
