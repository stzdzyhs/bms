package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.FeatureCode;

public interface FeatureCodeMapper {

	List<FeatureCode> getFeatureCodes(@Param("featureCode")FeatureCode featureCode);
	
	Integer getFeatureCodesCount(@Param("featureCode")FeatureCode featureCode);
	
	List<FeatureCode> getFeatureCodesById(Long[] feaNos);
	
	void deletefeatureCodesById(Long[] feaNos);
	
	FeatureCode getFeatureCodeById(Long featureCodeNo);
	
	Integer checkFeatureCode(FeatureCode featureCode);
	
	void update(FeatureCode featureCode);
	
	void save(FeatureCode featureCode);
	
	FeatureCode featureCodeDetail(Long featureCode);

	List<FeatureCode> findFeatureCodesByVal(String[] codeVals) throws Exception;
	
	Long getPrimaryKey();
	
	//Integer isFeatureCodeExit(@Param(value="featureCode")FeatureCode featureCode) throws Exception;
	
	void updateFeatureCodeStatus(@Param(value="status")Integer status, @Param(value="feaNos")Long[] feaNos,  @Param(value = "updateTime")String updateTime) throws Exception;

	

	Integer findStrategyFeatureCodeNoSelectCount(@Param(value = "strategyNo")Long strategyNo,
			@Param(value = "companyNo")Long companyNo, 
			@Param(value = "featureCode")FeatureCode space,@Param("excludeIds") Long[] excludeIds) 
					throws Exception;

	List<FeatureCode> findStrategyFeatureCodeNoSelect(@Param(value = "strategyNo")Long strategyNo,
			@Param(value = "companyNo")Long companyNo, 
			@Param(value = "featureCode")FeatureCode space,@Param("excludeIds") Long[] excludeIds) 
					throws Exception;
	
}
