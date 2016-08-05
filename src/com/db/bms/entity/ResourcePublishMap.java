
package com.db.bms.entity;

import java.util.ArrayList;
import java.util.List;

import com.db.bms.sync.portal.protocal.PublishStrategy;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;


public class ResourcePublishMap extends BaseModel {

	public Long id;

	//@see ConstConfig.resourceTypeMap
	/*  NOT used any more, but not remove....
    public static final int TYPE_TOPIC=1; // 专题
    public static final int TYPE_MENU=2; // 栏目 ??
    public static final int TYPE_ALBUM=3; // 相册
    public static final int TYPE_PICTURE=4; // 图片
    public static final int TYPE_COLUMN=5; // 版块
    public static final int TYPE_ARTICLE=6; // 文章
    */
    
    //@see EntityType
	public Integer type;

	// @see ConstConfig.parentTypeMap
	// parent type const,
	/*  NOT used any more, but not remove....
	public static final int PARENT_TYPE_TOPIC=0; //专题
	public static final int PARENT_TYPE_MENU=1;// 栏目 ??
	public static final int PARENT_TYPE_ALBUM=2; // 相册
	public static final int PARENT_TYPE_COLUMN=3; //版块
	public static final int PARENT_TYPE_OTHER=-1; //其他
	*/
	
    //@see EntityType
	public Integer parentType;

	public Long parentId = -1L;

	public Long resourceId;

	public String companyId;

	public String regionCode;

	public String featureId;

	public String featureGroupId;

	public List<Company> companys;

	public List<CardRegion> regions;

	public List<FeatureCode> featureCodes;

	public List<FeatureCodeGroup> featureCodeGroups;

	public String[] companyIds;

	public Long[] regionIds;

	public Long[] featureGroupIds;

	public String[] featureIds;

	public Boolean acascade = false;

	public Boolean pcascade = false;

	public String companyNames;

	public String regionNames;

	public String featureGroupNames;

	public String featureNames;

	public List<PortalPublishNotice> noticeList;

	public String parentName;

	public String resourceName;
	
	public String strategyNo; // 逗号分隔的strategy No
	
	public Topic topic;
	public Album album;
	public Picture picture;
	public Column column;
	public Article article;
	
	//辅助参数，用于零时存储页面选中的策略号，以便后台的业务处理
	public Long[] strategyNos;
	public String strategyNames;
	public List<Strategy> strategies;
	
	//added by MiaoJun for display strategy detail
	public List<PublishStrategy> publishStrategy;
	//added by MiaoJun on 2016-07-09 for recording item's created operatorNo
	private Long createdBy;
	private Long allocRes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getParentType() {
		return parentType;
	}

	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getFeatureGroupId() {
		return featureGroupId;
	}

	public void setFeatureGroupId(String featureGroupId) {
		this.featureGroupId = featureGroupId;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
		if (companys != null && companys.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (Company company : companys) {
				buffer.append(company.getCompanyName()).append(Delimiters.COMMA);
			}
			this.companyNames = buffer.substring(0, buffer.length() - 1);
		}
	}

	public List<CardRegion> getRegions() {
		return regions;
	}

	public void setRegions(List<CardRegion> regions) {
		this.regions = regions;
		if (regions != null && regions.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (CardRegion region : regions) {
				buffer.append(region.getRegionName()).append(Delimiters.COMMA);
			}
			this.regionNames = buffer.substring(0, buffer.length() - 1);
		}
	}

	public List<FeatureCode> getFeatureCodes() {
		return featureCodes;
	}

	public void setFeatureCodes(List<FeatureCode> featureCodes) {
		this.featureCodes = featureCodes;
	}

	public List<FeatureCodeGroup> getFeatureCodeGroups() {
		return featureCodeGroups;
	}

	public void setFeatureCodeGroups(List<FeatureCodeGroup> featureCodeGroups) {
		this.featureCodeGroups = featureCodeGroups;
		if (featureCodeGroups != null && featureCodeGroups.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (FeatureCodeGroup group : featureCodeGroups) {
				buffer.append(group.getGroupName()).append(Delimiters.COMMA);
			}
			this.featureGroupNames = buffer.substring(0, buffer.length() - 1);
		}
	}

	public String[] getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(String[] companyIds) {
		this.companyIds = companyIds;
		if (companyIds != null && companyIds.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String companyId : companyIds) {
				buffer.append(companyId).append(Delimiters.COMMA);
			}
			this.companyId = buffer.substring(0, buffer.length() - 1);
		}
	}

	public Long[] getRegionIds() {
		return regionIds;
	}

	public void setRegionIds(Long[] regionIds) {
		this.regionIds = regionIds;
		if (regionIds != null && regionIds.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (Long regionId : regionIds) {
				buffer.append(regionId).append(Delimiters.COMMA);
			}
			this.regionCode = buffer.substring(0, buffer.length() - 1);
		}
	}

	public Long[] getFeatureGroupIds() {
		return featureGroupIds;
	}

	public void setFeatureGroupIds(Long[] featureGroupIds) {
		this.featureGroupIds = featureGroupIds;
		if (featureGroupIds != null && featureGroupIds.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (Long groupId : featureGroupIds) {
				buffer.append(groupId).append(Delimiters.COMMA);
			}
			this.featureGroupId = buffer.substring(0, buffer.length() - 1);
		}
	}

	public String[] getFeatureIds() {
		return featureIds;
	}

	public void setFeatureIds(String[] featureIds) {
		this.featureIds = featureIds;
		if (featureIds != null && featureIds.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String featureId : featureIds) {
				buffer.append(featureId).append(Delimiters.COMMA);
			}
			this.featureId = buffer.substring(0, buffer.length() - 1);
		}
	}
	
	/**
	 * 是否为顶级发布
	 * @return
	 */
	public boolean isTopest() {
		if(this.parentType==null) {
			if(this.type!=null && (this.type==EntityType.TYPE_TOPIC || this.type==EntityType.TYPE_COLUMN)) {
				return true;
			}
		}
		return false;		
	}
	
	public String getParentTypeName() {
		String s = ConstConfig.resourceTypeMap.get(this.parentType);
		return s;
	}

	public String getTypeName() {
		String s = ConstConfig.resourceTypeMap.get(this.type);
		return s;
	}

	public Boolean getAcascade() {
		return acascade;
	}

	public void setAcascade(Boolean acascade) {
		this.acascade = acascade;
	}

	public Boolean getPcascade() {
		return pcascade;
	}

	public void setPcascade(Boolean pcascade) {
		this.pcascade = pcascade;
	}

	public String getCompanyNames() {
		return companyNames;
	}

	public void setCompanyNames(String companyNames) {
		this.companyNames = companyNames;
	}

	public String getRegionNames() {
		return regionNames;
	}

	public void setRegionNames(String regionNames) {
		this.regionNames = regionNames;
	}

	public String getFeatureGroupNames() {
		return featureGroupNames;
	}

	public void setFeatureGroupNames(String featureGroupNames) {
		this.featureGroupNames = featureGroupNames;
	}

	public String getFeatureNames() {
		return featureNames;
	}

	public void setFeatureNames(String featureNames) {
		this.featureNames = featureNames;
	}

	public List<PortalPublishNotice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<PortalPublishNotice> noticeList) {
		this.noticeList = noticeList;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;	
	}

	/*
	 @deprecated !!!
	public static enum ParentType {
		TOPIC(0), MENU(1), ALBUM(2), COLUMN(3), OTHER(-1);

		private final int index;

		private ParentType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static ParentType getType(int index) {
			switch (index) {
			case 0:
				return TOPIC;
			case 1:
				return MENU;
			case 2:
				return ALBUM;
			case 3:
				return COLUMN;
			}
			return OTHER;
		}
	}
	*/

	/*
	public static enum ResourceType {
		TOPIC(1), MENU(2), ALBUM(3), PICTURE(4), COLUMN(5), ARTICLE(6);

		private final int index;

		private ResourceType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static ResourceType getType(int index) {
			switch (index) {
			case 1:
				return TOPIC;
			case 2:
				return MENU;
			case 3:
				return ALBUM;
			case 4:
				return PICTURE;
			case 5:
				return COLUMN;
			case 6:
				return ARTICLE;
			}
			return TOPIC;
		}
	}
	*/

	public String getStrategyNo() {
		return strategyNo;
	}

	public void setStrategyNo(String strategyNo) {
		this.strategyNo = strategyNo;
	}

	@Override
	public void setDefaultNull() {

	}

	/**
	 * @return the strategyNos
	 */
	public Long[] getStrategyNos() {
		return strategyNos;
	}

	/**
	 * @param strategyNos the strategyNos to set
	 */
	public void setStrategyNos(Long[] strategyNos) {
		this.strategyNos = strategyNos;
		if (strategyNos != null && strategyNos.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (Long strategyNo : strategyNos) {
				buffer.append(strategyNo).append(Delimiters.COMMA);
			}
			this.strategyNo = buffer.substring(0, buffer.length() - 1);
		}
	}

	/**
	 * @return the strategyNames
	 */
	public String getStrategyNames() {
		return strategyNames;
	}

	/**
	 * @param strategyNames the strategyNames to set
	 */
	public void setStrategyNames(String strategyNames) {
		this.strategyNames = strategyNames;
	}

	/**
	 * @return the strategies
	 */
	public List<Strategy> getStrategies() {
		return strategies;
	}

	/**
	 * @param strategies the strategies to set
	 */
	public void setStrategies(List<Strategy> strategies) {
		strategies = strategies;
		if (strategies != null && strategies.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (Strategy strategy : strategies) {
				buffer.append(strategy.getStrategyName()).append(Delimiters.COMMA);
			}
			this.strategyNames = buffer.substring(0, buffer.length() - 1);
		}
	}
	
	public void addStrategy(Strategy s) {
		if(this.strategies==null) {
			this.strategies = new ArrayList<Strategy>();
		}
		this.strategies.add(s);
	}

	public List<PublishStrategy> getPublishStrategy() {
		return publishStrategy;
	}

	public void setPublishStrategy(List<PublishStrategy> publishStrategy) {
		this.publishStrategy = publishStrategy;
	}
	
	
	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getAllocRes() {
		return allocRes;
	}

	public void setAllocRes(Long allocRes) {
		this.allocRes = allocRes;
	}	
	
}
