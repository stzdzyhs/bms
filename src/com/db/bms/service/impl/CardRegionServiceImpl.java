
package com.db.bms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CardRegionMapper;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Strategy;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.StrategyService;
import com.db.bms.utils.core.PageUtil;


@Service("cardRegionService")
public class CardRegionServiceImpl implements CardRegionService {

	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Autowired
	StrategyService strategyService;
	
	@Override
	public CardRegion findRegionById(Long regionId) throws Exception {
		return cardRegionMapper.findRegionById(regionId);
	}

	@Override
	public List<CardRegion> findRegionsById(Long[] regionIds) throws Exception {
		return cardRegionMapper.findRegionsById(regionIds);
	}

	@Override
	public List<CardRegion> findRegions(CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findRegionCount(region);
		page.setRowCount(count);
		return cardRegionMapper.findRegions(region);
	}

	@Override
	public boolean isRegionRepeat(CardRegion region) throws Exception {
		int count = cardRegionMapper.getRegionCountByCode(region);
		return count > 0 ? true : false;
	}

	@Override
	public void addRegion(CardRegion region) throws Exception {
		cardRegionMapper.addRegion(region);
	}

	@Override
	public void updateRegion(CardRegion region) throws Exception {
		cardRegionMapper.updateRegion(region);
	}

	@Override
	public void deleteRegions(Long[] regionIds) throws Exception {
		cardRegionMapper.deleteRegions(regionIds);
	}

	@Override
	public List<CardRegion> findAllRegions(CardRegion region) throws Exception {
		return cardRegionMapper.findAllRegions(region);
	}

	@Override
	public boolean isReferenceRegion(Long[] parentIds) throws Exception {
		int count = cardRegionMapper.getChildRegionCount(parentIds);
		return count > 0 ? true : false;
	}

	@Override
	public CardRegion findRegionByCode(CardRegion region) throws Exception {
		return cardRegionMapper.findRegionByCode(region);
	}
	
}
