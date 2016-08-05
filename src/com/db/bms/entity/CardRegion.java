
package com.db.bms.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.db.bms.utils.StringUtils;


public class CardRegion extends BaseModel {

	public Long id;

	public String regionName;

	public Integer type;

	public Integer codeType;

	public String regionCode;

	public String regionSectionBegin;

	public String regionSectionEnd;

	public Long parentId;

	public String depict;

	public String createTime;

	public String updateTime;

	@JSONField(serialize=false)
	private CardRegion parent;
	@JSONField(serialize=false)
	private Long companyNo; 
	@JSONField(serialize=false)
	private Long internetNo; 
	@JSONField(serialize=false)
	private Long spaceNo; 
	@JSONField(serialize=false)
	private boolean checked;
	@JSONField(serialize=false)
	private Integer regionSectionBeginNum;
	@JSONField(serialize=false)
	private Integer regionSectionEndNum;
	@JSONField(serialize=false)
	private List<Long> companyNos; 
	@JSONField(serialize=false)
	private List<Long> internetNos;  
	@JSONField(serialize=false)
	private List<Long> spaceNos; 
	@JSONField(serialize=false)
	private List<Long> strategyIds;
	
	//关联的strategy, 可以为null
	@JSONField(serialize=false)
    public  Strategy strategy;
	 
    
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public List<Long> getStrategyIds() {
		return strategyIds;
	}

	public void setStrategyIds(List<Long> strategyIds) {
		this.strategyIds = strategyIds;
	}

	public Long getSpaceNo() {
		return spaceNo;
	}

	public void setSpaceNo(Long spaceNo) {
		this.spaceNo = spaceNo;
	}

	public List<Long> getSpaceNos() {
		return spaceNos;
	}

	public void setSpaceNos(List<Long> spaceNos) {
		this.spaceNos = spaceNos;
	}

	public Long getInternetNo() {
		return internetNo;
	}

	public void setInternetNo(Long internetNo) {
		this.internetNo = internetNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCodeType() {
		return codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionSectionBegin() {
		return regionSectionBegin;
	}

	public void setRegionSectionBegin(String regionSectionBegin) {
		this.regionSectionBegin = regionSectionBegin;
		if (StringUtils.isNotEmpty(this.regionSectionBegin)) {
			this.regionSectionBeginNum = Integer
					.valueOf(this.regionSectionBegin);
		}
	}

	public String getRegionSectionEnd() {
		return regionSectionEnd;
	}

	public void setRegionSectionEnd(String regionSectionEnd) {
		this.regionSectionEnd = regionSectionEnd;
		if (StringUtils.isNotEmpty(this.regionSectionEnd)) {
			this.regionSectionEndNum = Integer.valueOf(this.regionSectionEnd);
		}
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@JSONField(serialize=false)
	public Integer getRegionSectionBeginNum() {
		return regionSectionBeginNum;
	}

	public void setRegionSectionBeginNum(Integer regionSectionBeginNum) {
		this.regionSectionBeginNum = regionSectionBeginNum;
	}
	@JSONField(serialize=false)
	public Integer getRegionSectionEndNum() {
		return regionSectionEndNum;
	}

	public void setRegionSectionEndNum(Integer regionSectionEndNum) {
		this.regionSectionEndNum = regionSectionEndNum;
	}

	public static enum CardRegionType {
		REGION(0), SECTION(1);

		private final int index;

		private CardRegionType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static CardRegionType getType(int index) {
			switch (index) {
			case 0:
				return REGION;
			case 1:
				return SECTION;
			}
			return REGION;
		}
	}

	public static enum RegionCodeType {
		REGION(0), SECTION(1);

		private final int index;

		private RegionCodeType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static RegionCodeType getType(int index) {
			switch (index) {
			case 0:
				return REGION;
			case 1:
				return SECTION;
			}
			return REGION;
		}
	}

	public CardRegion getParent() {
		return parent;
	}

	public void setParent(CardRegion parent) {
		this.parent = parent;
	}

	public Long getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Long companyNo) {
		this.companyNo = companyNo;
	}
	@JSONField(serialize=false)
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<Long> getCompanyNos() {
		return companyNos;
	}

	public void setCompanyNos(List<Long> companyNos) {
		this.companyNos = companyNos;
	}
     
	public List<Long> getInternetNos() {
		return internetNos;
	}

	public void setInternetNos(List<Long> internetNos) {
		this.internetNos = internetNos;
	} 
	
	@Override
	public void setDefaultNull() {

	}

}
