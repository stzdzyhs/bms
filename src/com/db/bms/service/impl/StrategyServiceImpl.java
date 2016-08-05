package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.ClientMapper;
import com.db.bms.dao.CompanyMapper;
import com.db.bms.dao.FeatureCodeMapper;
import com.db.bms.dao.InternetMapper;
import com.db.bms.dao.OperatorMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.dao.SpaceMapper;
import com.db.bms.dao.StrategyConditionMapper;
import com.db.bms.dao.StrategyMapper;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Client;
import com.db.bms.entity.Company;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Space;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.PublishStrategy;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.core.PageUtil;

@Service("strategyService")
public class StrategyServiceImpl implements StrategyService{
	private Logger log = Logger.getLogger(StrategyServiceImpl.class);
	
	@Autowired
	StrategyMapper strategyMapper;
	
	@Autowired
	StrategyConditionMapper  strategyConditionMapper;
	
	@Autowired
	ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private OperatorMapper operatorMapper;

	@Autowired
	CardRegionMapper cardRegionMapper;
	@Autowired
	InternetMapper internetMapper;
	@Autowired
	SpaceMapper spaceMapper;
	@Autowired
	FeatureCodeMapper featureCodeMapper;
	@Autowired
	ClientMapper clientMapper;

	@Autowired
	CompanyMapper companyMapper;
	
	/** 
	 * 查找策略
	 */
	@Override
	public List<Strategy> findStrategys(Strategy strategy) throws Exception {
		int cnt = this.strategyMapper.findStrategysCount(strategy);
		strategy.getPageUtil().setRowCount(cnt);
		List<Strategy> list = strategyMapper.findStrategys(strategy);
		return list;
	}
    
	/** 
	 * 更新策略
	 */
	@Override
	public void updateStrategy(Strategy strategy) throws Exception {
		strategyMapper.updateStrategy(strategy);
	} 
	
	/**
	 * add / update 策略
	 * strategy.curOper must set.
	 */
	@Override
	public void saveStrategy(Strategy strategy) throws Exception {
		Operator op = strategy.getCurOper();
		
		if(strategy.strategyNo==null) {
			// add
			if(strategy.companyNo==null) {
				throw new ResultCodeException(ResultCode.INVALID_PARAM, "companyNo");
			}
			strategy.strategyNo = this.strategyMapper.getPrimaryKey();
			strategy.createTime = DateUtil.getCurrentTime();
			strategy.companyNo = strategy.companyNo;
			strategy.auditStatus = AuditStatus.DRAFT;
			strategy.createBy = op.operatorNo;

			String now=DateUtil.getCurrentTime();
			strategy.createTime = now;
			strategy.updateTime = now;

			this.strategyMapper.addStrategy(strategy);
		}
		else {
			// update
			strategy.updateTime = DateUtil.getCurrentTime();
			this.strategyMapper.updateStrategy(strategy);
			this.strategyMapper.deleteStrategyAllCondition(strategy.strategyNo);
		}

		// add resource
		
		StrategyCondition m;
		
		class Info {
			public List<StrategyCondition> list;
			public int id;
			public Info(List<StrategyCondition> l, int id) {
				this.list = l;
				this.id = id;
			}
		}
		
		Info[] a = new Info[]{
			new Info(strategy.cardRegionList, StrategyCondition.TYPE_CARD_REGION),
			//new Info(strategy.internetList, StrategyCondition.TYPE_INTERNET),
			new Info(strategy.companyList, StrategyCondition.TYPE_COMPANY),
			new Info(strategy.spaceList, StrategyCondition.TYPE_SPACE),
			new Info(strategy.featureCodeList, StrategyCondition.TYPE_FEATURE_CODE),
			new Info(strategy.clientList, StrategyCondition.TYPE_CLIENT),
		};
		
		for(int i=0;i<a.length;i++) {
			for(int j=0;j<ArrayUtils.getSize(a[i].list);j++) {
				m = a[i].list.get(j);
				m.strategyNo = strategy.strategyNo;
				m.conditionType = a[i].id;
				if(m.conditionId==null) {
					throw new ResultCodeException(ResultCode.INVALID_PARAM, "conditionId null");
				}
				this.strategyConditionMapper.addStrategyCondition(m);
			}
		}
	}
    
	/** 
	 * 删除策略
	 */
	@Override
	public void deleteStrategysByNo(Long[] nos) throws Exception {
		//应该要先检查策略是否被引用，如果被引用，则提示用户被引用，不能删除
		Integer refCount = null;
		for (int i = 0; i < nos.length; i++) {			
			refCount = this.resourcePublishMapMapper.getPublishCountByStrategyNo(nos[i]);
			if(refCount != null && refCount.intValue() > 0){
				throw new Exception("schedule");
			}
		}
		//删除策略时，必须要连带删除策略条件表中的数据
		this.strategyConditionMapper.deleteStrategyConditionByStrategyNo(nos);
		strategyMapper.deleteStrategysByNo(nos);
	}
	
	@Override
	public void deleteStrategyAllCondition(Long strategyNo) throws Exception {
		this.strategyMapper.deleteStrategyAllCondition(strategyNo);
	}
    
	/** 
	 * 根据ID查找策略
	 */
	@Override
	public List<Strategy> findStrategyByNos(List<Long> Ids) throws Exception {
		return strategyMapper.findStrategyByNos(Ids);
	}

	@Override
	public Strategy findStrategyByNo(Long no) throws Exception {
		return strategyMapper.findStrategyByNo(no);
	}

	@Override
	public Strategy findStrategyByNoWithCondition(Long strategyId) throws Exception {
		Strategy st = this.strategyMapper.findStrategyByNoWithCondition(strategyId);
		return st;		
	}
	
	Set<Integer> getTypeSet(Strategy st) {
		Set<Integer> ret = new HashSet<Integer>();
		for(int i=0;i<ArrayUtils.getSize(st.conditionList); i++) {
			ret.add(st.conditionList.get(i).conditionType);
		}
		return ret;
	}
	
	/**
	 * get strategy all data
	 */
	@Override
	public Strategy getStrategyAllData(Long strategyNo) throws Exception {
		Strategy search = new Strategy();
		search.strategyNo = strategyNo;
		
		Strategy st = this.strategyMapper.findStrategyByNoWithCondition(strategyNo);
		if(st==null) {
			return null;
		}
		
		Set<Integer> type = getTypeSet(st);
		
		StrategyCondition sr = new StrategyCondition();
		sr.strategyNo = strategyNo;
		
		/*
		for(int i=1;i<6;i++) {
			sr.conditionType = i;
			strategyConditionMapper.findStrategyConditionEntity(sr);
		}
		*/
		
		for (Integer t: type) {
			sr.conditionType = t;
			if(sr.conditionType==null || sr.strategyNo==null) {
				throw new Exception("????");
			}
			List<StrategyCondition> srList = strategyConditionMapper.findStrategyConditionEntity(sr);
			st.setData(t, srList);
		}
		return st;
	}

	@Override
	public List<CardRegion> findStrategyCardRegionNoSelect(Long strategyNo, Long companyNo, CardRegion cardRegion, Long[] excludeIds) throws Exception {
		if(strategyNo==null&& companyNo==null) {
			// card region no belong to any company
			//throw new ResultCodeException(ResultCode.INVALID_PARAM, "strategyNo companyNo both null");
		}

		PageUtil page = cardRegion.getPageUtil();
		int count = cardRegionMapper.findStrategyCardRegionNoSelectCount(strategyNo, companyNo,cardRegion, excludeIds);
		page.setRowCount(count);
		List<CardRegion> list = cardRegionMapper.findStrategyCardRegionNoSelect(strategyNo, companyNo, cardRegion, excludeIds); 
		return list;
	}
	
	/*
	@Override
	public List<Internet> findStrategyInternetNoSelect(Long strategyNo, Long companyNo, Internet internet, Long[] excludeIds) throws Exception {
		if(strategyNo==null&& companyNo==null) {
			throw new ResultCodeException(ResultCode.INVALID_PARAM, "strategyNo companyNo both null");
		}
		PageUtil page = internet.getPageUtil();
		int count = internetMapper.findStrategyInternetNoSelectCount(strategyNo, companyNo, internet, excludeIds);
		page.setRowCount(count);
		List<Internet> list = internetMapper.findStrategyInternetNoSelect(strategyNo, companyNo, internet, excludeIds); 
		return list;
	}
	*/

	@Override
	public List<Company> findStrategyCompanyNoSelect(Long strategyNo, Long companyNo, Company company, Long[] excludeIds) throws Exception {
		if(strategyNo==null && companyNo==null) {
			//throw new ResultCodeException(ResultCode.INVALID_PARAM, "strategyNo companyNo both null");
		}
		PageUtil page = company.getPageUtil();
		int count = companyMapper.findStrategyCompanyNoSelectCount(strategyNo, companyNo, company, excludeIds);
		page.setRowCount(count);
		List<Company> list = companyMapper.findStrategyCompanyNoSelect(strategyNo, companyNo, company, excludeIds); 
		return list;
	}
	
	@Override
	public List<Space> findStrategySpaceNoSelect(Long strategyNo, Long companyNo, Space space, Long[] excludeIds) throws Exception {
		if(strategyNo==null&& companyNo==null) {
			// space no belong to any company
			//throw new ResultCodeException(ResultCode.INVALID_PARAM, "strategyNo companyNo both null");
		}

		PageUtil page = space.getPageUtil();
		int count = spaceMapper.findStrategySpaceNoSelectCount(strategyNo, companyNo, space, excludeIds);
		page.setRowCount(count);
		List<Space> list = spaceMapper.findStrategySpaceNoSelect(strategyNo, companyNo, space, excludeIds); 
		return list;
	}

	@Override
	public List<FeatureCode> findStrategyFeatureCodeNoSelect(Long strategyNo, Long companyNo, FeatureCode featureCode, Long[] excludeIds) throws Exception {
		if(strategyNo==null&& companyNo==null) {
			// feature code no belong to any company
			//throw new ResultCodeException(ResultCode.INVALID_PARAM, "strategyNo companyNo both null");
		}

		PageUtil page = featureCode.getPageUtil();
		int count = featureCodeMapper.findStrategyFeatureCodeNoSelectCount(strategyNo, companyNo, featureCode, excludeIds);
		page.setRowCount(count);
		List<FeatureCode> list = featureCodeMapper.findStrategyFeatureCodeNoSelect(strategyNo, companyNo, featureCode, excludeIds); 
		return list;
	}

	@Override
	public List<Client> findStrategyClientNoSelect(Long strategyNo, Long companyNo, Client client, Long[] excludeIds) throws Exception {
		if (strategyNo==null&& companyNo==null) {
			// client no belong to any company
			//throw new ResultCodeException(ResultCode.INVALID_PARAM, "strategyNo companyNo both null");
		}

		PageUtil page = client.getPageUtil();
		int count = clientMapper.findStrategyClientNoSelectCount(strategyNo, companyNo, client, excludeIds);
		page.setRowCount(count);
		List<Client> list = clientMapper.findStrategyClientNoSelect(strategyNo, companyNo, client, excludeIds); 
		return list;
	}
	
	/**
	 * 根据策略编号，获取策略的具体内容集合
	 */
	@Override
	public List<PublishStrategy> getPublishStrategyByNos(String strategyNoStr){
		log.debug("get publish strtegy by nos:"+strategyNoStr);
		List<PublishStrategy> res = new ArrayList<PublishStrategy>();
		String[] strategyNoArray = strategyNoStr.split(Delimiters.COMMA);
		Strategy strategy = null;
		for (int i = 0; i < strategyNoArray.length; i++) {
			try {
				strategy = this.getStrategyAllData(Long.valueOf(strategyNoArray[i]));
			} catch (Exception e) {
				log.error("Ocurred exception when get strategy by strategyNo:"+strategyNoArray[i]);
				e.printStackTrace();
				continue;
			}
			if(strategy == null){
				log.error("strategy is null with strategyNo:"+strategyNoArray[i]);
				continue;
			}
			PublishStrategy pubStr = new PublishStrategy();
			pubStr.setStrategyName(strategy.getStrategyName());
			pubStr.setAndOr(strategy.getStrategyForm().intValue()==Strategy.FORM_AND?"and":"or");
			res.add(pubStr);
			log.debug("fond a strategy with no:["+strategyNoArray[i]+"]");
			if(strategy.companyList != null && !strategy.companyList.isEmpty()){
				for (StrategyCondition  strategyCondition: strategy.companyList) {
					if(strategyCondition.company == null){
						continue;
					}
					pubStr.getNetworkId().add(Long.valueOf(strategyCondition.company.getCompanyId()));
				}
			}
			if(strategy.cardRegionList != null && !strategy.cardRegionList.isEmpty()){
				for (StrategyCondition  strategyCondition: strategy.cardRegionList) {
					if(strategyCondition.cardRegion == null){
						continue;
					}
					pubStr.getRegionCode().add(strategyCondition.cardRegion);
				}
			}
			if(strategy.featureCodeList != null && !strategy.featureCodeList.isEmpty()){
				for (StrategyCondition  strategyCondition: strategy.featureCodeList) {
					if(strategyCondition.featureCode == null){
						continue;
					}
					pubStr.getFeatureId().add(strategyCondition.featureCode.getFeatureCodeVal());
				}
			}
			if(strategy.spaceList != null && !strategy.spaceList.isEmpty()){
				for (StrategyCondition  strategyCondition: strategy.spaceList) {
					if(strategyCondition.space == null){
						continue;
					}
					pubStr.getTsId().add(strategyCondition.space.getSpaceId());
				}
			}
			if(strategy.clientList != null && !strategy.clientList.isEmpty()){
				for (StrategyCondition  strategyCondition: strategy.clientList) {
					if(strategyCondition.client == null){
						continue;
					}
					pubStr.getClient().add(strategyCondition.client.getClientId());
				}
			}
		}
		return res;
	}
	
	public void auditStrategy(Integer status, Long[] strategyNos)throws Exception{
		this.strategyMapper.updateStrategyStatus(status, strategyNos);
	}
}
