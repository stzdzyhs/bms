package com.db.bms.service;

import java.io.File;
import java.util.List;

import com.db.bms.entity.FeatureCode;
import com.db.bms.sync.portal.protocal.GetFeatureCodeREQT;
import com.db.bms.sync.portal.protocal.GetFeatureCodeRESP;

public interface FeatureCodeService {

	List<FeatureCode> getFeatureCodeList(FeatureCode featureCode) throws Exception;
	
	List<FeatureCode> findFeatureCodesById(Long[] feaNos) throws Exception;
	
	void deleteFeatureCodes(Long[] feaNos) throws Exception;
	
	FeatureCode findFeatureCodeById(Long featureCodeNo) throws Exception;
	
	boolean isRepeatFeatureCode(FeatureCode featureCode) throws Exception;
	
	void updateFeature(FeatureCode featureCode) throws Exception;
	
	void saveFeature(FeatureCode featureCode) throws Exception;
	
	FeatureCode getFeatureCodeDetail(Long featureCodeNo) throws Exception;
		
	//Integer uploadFeatureCode(MultipartFile file, String filename, FeatureCode feature) throws Exception;
	
	GetFeatureCodeRESP getFeatureCodeList(GetFeatureCodeREQT request) throws Exception;

	File getFeatureCodeDirFile() throws Exception;
	
	String getFilePath(String filename) throws Exception;
	
	Integer resolveFeatureCode(File file, FeatureCode feature) throws Exception;
	
	//boolean checkForWrite(FeatureCode feature) throws Exception;
	
	void auditFeatureCode(Integer status, Long[] feaNos) throws Exception;
	
	/**
	 * 特征码
	 */
	List<FeatureCode> findFeatureCodeWithStrategy(FeatureCode featureCode) throws Exception;
}
