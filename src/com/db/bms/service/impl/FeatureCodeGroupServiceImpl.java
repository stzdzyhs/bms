package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.FeatureCodeGroupMapper;
import com.db.bms.entity.FeatureCodeGroup;
import com.db.bms.service.FeatureCodeGroupService;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.core.PageUtil;

@Service("featureCodeGroupService")
public class FeatureCodeGroupServiceImpl implements FeatureCodeGroupService {
	
	@Autowired
	private FeatureCodeGroupMapper featureCodeGroupMapper;

	@Override
	public List<FeatureCodeGroup> findFeatureCodeGroups(
			FeatureCodeGroup featureCodeGroup) throws Exception {
		PageUtil page = featureCodeGroup.getPageUtil();
		page.setPaging(true);
		int count = featureCodeGroupMapper.getFeatureCodeGroupsCount(featureCodeGroup);
		page.setRowCount(count);
		List<FeatureCodeGroup> ret = 
					this.featureCodeGroupMapper.getFeatureCodeGroups(featureCodeGroup);
		return ret;
	}

	@Override
	public FeatureCodeGroup findFeaGroupByNo(Long groupNo) throws Exception {
		return this.featureCodeGroupMapper.getFeaGroupByNo(groupNo);
	}

	@Override
	public FeatureCodeGroup findFeaGroupDetail(Long groupNo)
			throws Exception {
		
		return this.featureCodeGroupMapper.getFeaGroupDetail(groupNo);
	}

	@Override
	public boolean isRepeatGroupName(FeatureCodeGroup featureCodeGroup)throws Exception {
		Integer count = this.featureCodeGroupMapper.checkGroupName(featureCodeGroup);
		return count > 0 ? true : false;
	}
	
	@Override
	public boolean isRepeatGroupId(FeatureCodeGroup featureCodeGroup)throws Exception {
		Integer count = this.featureCodeGroupMapper.checkGroupId(featureCodeGroup);
		return count > 0 ? true : false;
	}

	@Override 
	public void updateGroup(FeatureCodeGroup featureCodeGroup)
			throws Exception {
		this.featureCodeGroupMapper.updateGroup(featureCodeGroup);
	}

	@Override
	public void saveGroup(FeatureCodeGroup featureCodeGroup) throws Exception {
		featureCodeGroup.groupNo = this.featureCodeGroupMapper.getPrimaryKey();
		String now = DateUtil.getCurrentTime();
		featureCodeGroup.setCreateTime(now);
		featureCodeGroup.setUpdateTime(now);
		
		this.featureCodeGroupMapper.addGroup(featureCodeGroup);
	}

	@Override
	public List<FeatureCodeGroup> findGroups(Long[] groupNos)
			throws Exception {
		return this.featureCodeGroupMapper.getGroups(groupNos);
	}

	@Override
	public void deleteGroups(Long[] groupNos) throws Exception {
		this.featureCodeGroupMapper.deleteGroups(groupNos);		
	}

}
