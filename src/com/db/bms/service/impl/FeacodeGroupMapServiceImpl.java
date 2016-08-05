package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.FeacodeGroupMapMapper;
import com.db.bms.entity.FeacodeGroupMap;
import com.db.bms.service.FeacodeGroupMapService;
import com.db.bms.utils.core.PageUtil;

@Service("feaCodeGroupMapService")
public class FeacodeGroupMapServiceImpl implements FeacodeGroupMapService{

	@Autowired
	private FeacodeGroupMapMapper FeacodeGroupMapMapper;
	
	@Override
	public List<FeacodeGroupMap> findFeacodeGroupMaps(
			FeacodeGroupMap FeacodeGroupMap) throws Exception {
		PageUtil page = FeacodeGroupMap.getPageUtil();
		page.setPaging(true);
		int count = FeacodeGroupMapMapper.getFeatureCodeGroupsCount(FeacodeGroupMap);
		page.setRowCount(count);
		return this.FeacodeGroupMapMapper.getFeacodeGroupMaps(FeacodeGroupMap);
	}

	@Override
	public List<FeacodeGroupMap> findFeacodesOutGroup(
			FeacodeGroupMap FeacodeGroupMap) throws Exception {
		PageUtil page = FeacodeGroupMap.getPageUtil();
		int count = FeacodeGroupMapMapper.getFeacodeOutGroupsCount(FeacodeGroupMap);
		page.setPaging(true);
		page.setRowCount(count);
		System.out.println(count);
		return this.FeacodeGroupMapMapper.getFeacodeOutGroups(FeacodeGroupMap);
	}

	@Override
	public void add(Long groupNo, Long[] featureCodeNos) throws Exception {
		for (int i=0; i<featureCodeNos.length; i++) {
			FeacodeGroupMap fea = new FeacodeGroupMap();
			fea.setFeacodeGroupMapNo(this.FeacodeGroupMapMapper.getPrimaryKey());
			fea.setFeatureCodeNo(featureCodeNos[i]);
			fea.setGroupNo(groupNo);
			int count = this.FeacodeGroupMapMapper.addFeacodeGroupMap(fea);
			if (count != 1) {
				throw new Exception(groupNo+"添加特征码[ "+ featureCodeNos[i] +" ]失败");
			}
		}
				
		
	}

	@Override
	public void deleteInGroupMap(Long groupNo, Long[] featureCodeNos)
			throws Exception {
		this.FeacodeGroupMapMapper.delete(groupNo ,featureCodeNos);
	}

	@Override
	public void deleteGroupNos(Long[] groupNo) throws Exception {
		this.FeacodeGroupMapMapper.deleteGroupNos(groupNo);
		
	}

	@Override
	public void featureCodeNos(Long[] feaNos) throws Exception {
		this.FeacodeGroupMapMapper.deleteFeaNos(feaNos);
	}

	@Override
	public List<FeacodeGroupMap> findAllFeacodeGroupMaps(
			FeacodeGroupMap FeacodeGroupMap) throws Exception {
		return this.FeacodeGroupMapMapper.getFeacodeGroupMaps(FeacodeGroupMap);
	}
	
}
