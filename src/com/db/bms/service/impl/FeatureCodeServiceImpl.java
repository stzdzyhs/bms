package com.db.bms.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.db.bms.dao.FeatureCodeMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Portal;
import com.db.bms.entity.FeatureCode.FeatureCodeStatus;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.FeatureCodeService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetFeatureCodeREQT;
import com.db.bms.sync.portal.protocal.GetFeatureCodeRESP;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.core.PageUtil;

@Service("featureCodeService")
public class FeatureCodeServiceImpl implements FeatureCodeService {
	
	@Value("${featureCode.dir}")
	private String featureCodeDir;
	
	static File featureCodeDirFile = null;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	StrategyService strategyService;
	
	public File getFeatureCodeDirFile() throws Exception {
		if(featureCodeDirFile==null) {
			featureCodeDirFile = new File(featureCodeDir);
			if(!featureCodeDirFile.exists()) {
				featureCodeDirFile.mkdirs();
			}
		}
		return featureCodeDirFile;
	}
	
	public String getFilePath(String filename) {
		return this.featureCodeDir + "/" + filename;
	}

	@Autowired
	private FeatureCodeMapper featureCodeMapper;
	
	@Override
	public List<FeatureCode> getFeatureCodeList(FeatureCode featureCode) throws Exception {
		PageUtil page = featureCode.getPageUtil();
		page.setPaging(true);
		int count = featureCodeMapper.getFeatureCodesCount(featureCode);
		page.setRowCount(count);
		
		List<FeatureCode> ret = this.featureCodeMapper.getFeatureCodes(featureCode);
		
		return ret;
	}

	@Override
	public List<FeatureCode> findFeatureCodesById(Long[] feaNos) throws Exception {
		return this.featureCodeMapper.getFeatureCodesById(feaNos);
	}

	@Override
	public void deleteFeatureCodes(Long[] feaNos) throws Exception {
		this.featureCodeMapper.deletefeatureCodesById(feaNos);
	}

	@Override
	public FeatureCode findFeatureCodeById(Long featureCodeNo) {
		return this.featureCodeMapper.getFeatureCodeById(featureCodeNo);
	}

	@Override
	public boolean isRepeatFeatureCode(FeatureCode featureCode) {
		Integer count = this.featureCodeMapper.checkFeatureCode(featureCode);
		return count > 0 ? true : false ;
	}

	@Override
	public void updateFeature(FeatureCode featureCode) {
		featureCode.setUpdateTime(DateUtil.getCurrentTime());
		featureCode.setStatus(FeatureCode.STATUS_ENABLED); // enable, if disaled before  
		this.featureCodeMapper.update(featureCode);
	}
	
/*	@Override
	public boolean checkForWrite(FeatureCode featureCode) throws Exception {
		boolean result = false;
		Integer count = this.featureCodeMapper.isFeatureCodeExit(featureCode);
		if (count > 0) {  //有管理权限
			result = true;
		}
		return result;
	}*/

	@Override
	public void saveFeature(FeatureCode featureCode) {
		Long featureCodeNo = this.featureCodeMapper.getPrimaryKey();
		featureCode.setFeatureCodeNo(featureCodeNo);
		featureCode.setGroupId(featureCode.getCurOper().getCreateBy());
		featureCode.setOperatorNo(featureCode.getCurOper().getOperatorNo());
		String now = DateUtil.getCurrentTime(); 
		featureCode.setCreateTime(now);
		featureCode.setUpdateTime(now); // always set update for sorting
		featureCode.setStatus(FeatureCodeStatus.DISABLE.getIndex()); //禁用
		this.featureCodeMapper.save(featureCode);
	}

	@Override
	public FeatureCode getFeatureCodeDetail(Long featureCodeNo) throws Exception {
		return this.featureCodeMapper.featureCodeDetail(featureCodeNo);
	}

	/*@Override
	public Integer uploadFeatureCode(MultipartFile file, String filename,
			FeatureCode feature) throws Exception {
		String dirPath = getFeatureCodeDirFile().getCanonicalPath();
    	String genFileName = FileUpload.geneFileName(filename);
    	String filePath = getFilePath(genFileName);
    	boolean isok = FileUpload.saveFile(file, dirPath, genFileName);
    	Integer count = 0;
    	File f = new File(filePath);
    	//获取文件扩展名
    	int exIndex = genFileName.lastIndexOf(".");
		String extension = genFileName.substring(exIndex);
    	//1.文件上传成功
    	if (isok && f.exists()) {
    		if(".txt".equals(extension)) { //是文本文件
	        	String charset = FileUtils.get_charset(f);
	    		if(!"UTF-8".equals(charset)) { //不是UTF-8编码
	    			if(f.isFile()) {
	    				f.delete();
	    			}
	    			throw new Exception("编码不支持:" + charset);
	    		}	
	    		//2.按行读取目录内容，并截取，构建对象
	    		RandomAccessFile raf = new RandomAccessFile(filePath, "r");
	    		String line_record = raf.readLine();
	    		String[] fields = null;	
	    		String rex = "^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$";
				while(line_record != null && line_record.length() > 0) {
					String newLine_record = new String(line_record.getBytes("ISO-8859-1"),"UTF-8");
					// remove BOM special char
					if (newLine_record.length()>0 && (newLine_record.charAt(0)=='\ufffe' ||newLine_record.charAt(0)=='\ufeff' )) {
						newLine_record = newLine_record.substring(1);
					}
					//3.解析记录
					fields = newLine_record.split(",");
					String featureCodeVal = fields[0].trim().substring(fields[0].lastIndexOf(".")+1).toString();
					//特征码值的正则校验
					if (!featureCodeVal.matches(rex)) {
						throw new Exception("特征码值格式不正确" + featureCodeVal);
					}
					feature.setFeatureCodeVal(featureCodeVal);
					if(this.isRepeatFeatureCode(feature)) { //特征码值重复
						line_record = raf.readLine();//读取下一行
						continue;
					}
					String featureCodeType = fields[1].trim().toString();
					String featureCodeDesc = fields[2].trim().toString();				  				
					feature.setFeatureCodeType(featureCodeType);
					feature.setFeatureCodeDesc(featureCodeDesc);
					feature.setOperatorNo(feature.getOperator().getOperatorNo());
					feature.setCreateTime(DateUtil.getCurrentTime());
					//4.存入数据库
					this.saveFeature(feature);
					line_record = raf.readLine();
					count++;
				}
				raf.close();
    		} else if (".xls".equals(extension)) { //excel文档
    			//解析excel文档
    			System.out.println("解析excel文档...");
    			//TODO:--add analysis excel
    		}
    	} 	
    	//5.删除文件
		FileUtils.del(filePath);
    	return count;
	}*/
	
	@Override
	public Integer resolveFeatureCode(File file, FeatureCode feature) throws Exception {
		InputStream is = new FileInputStream(file);
		BufferedReader raf = new BufferedReader(new InputStreamReader(is, ConstConfig.DEF_ENCODING)); 
		String line = raf.readLine();
		String[] fields = null;	
		Integer count = 0;
		boolean flag = true;
		String rex = "^\\w+$"; //英文、数字、下划线
		while (line != null && line.length() > 0 && flag) {
			// remove BOM special char
			if (line.length()>0 && (line.charAt(0)=='\ufffe' ||line.charAt(0)=='\ufeff' )) {
				line = line.substring(1);
			}
			fields = line.split(",");
			if(fields.length != 3) { //避免上传文件不正确造成数组下标越界异常
				flag = false;
				count = 0;
			} else {
				String featureCodeVal = fields[0].trim().substring(fields[0].lastIndexOf(".")+1).toString();
				if (!featureCodeVal.matches(rex)) { //特征码值的正则校验
					flag = false;
					count = -1;  
				} else {
					feature.setFeatureCodeVal(featureCodeVal);
					if(this.isRepeatFeatureCode(feature)) { //特征码值重复
						line = raf.readLine();//读取下一行
						continue;
					}
					String featureCodeType = fields[1].trim().toString();
					String featureCodeDesc = fields[2].trim().toString();				  				
					feature.setFeatureCodeType(featureCodeType);
					feature.setFeatureCodeDesc(featureCodeDesc);
					//存入数据库
					this.saveFeature(feature);
					line = raf.readLine();
					count++;
				}
			}
		}		
		raf.close();
		return count;
	}	


	@Override
	public GetFeatureCodeRESP getFeatureCodeList(GetFeatureCodeREQT request)
			throws Exception {
		GetFeatureCodeRESP response = new GetFeatureCodeRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<String> featureCodeList = new ArrayList<String>();
		response.setFeatureCodeList(featureCodeList);
		
		Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
	    if (portal == null){
			response.setResultCode(CommonResultCode.NOT_FOUND_SYSTEM.getResultCode());
			response.setResultDesc("Could not find the system.");
			return response;
	    }
	    
	    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
			response.setResultCode(CommonResultCode.NO_ACCESS_RIGHTS.getResultCode());
			response.setResultDesc("No access rights.");
			return response;
	    }
		
		FeatureCode search = new FeatureCode();
		search.setStatus(FeatureCodeStatus.ENABLE.getIndex());
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = featureCodeMapper.getFeatureCodesCount(search);
		page.setRowCount(totalCount);
		List<FeatureCode> featureList = featureCodeMapper.getFeatureCodes(search);
		Iterator<FeatureCode> it = featureList.iterator();
		while (it.hasNext()){
			FeatureCode featureCode = it.next();
			featureCodeList.add(featureCode.getFeatureCodeVal());
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}

	@Override
	public void auditFeatureCode(Integer status, Long[] feaNos)
			throws Exception {
		featureCodeMapper.updateFeatureCodeStatus(status, feaNos, DateUtil.getCurrentTime());
	}

	@Override
	public List<FeatureCode> findFeatureCodeWithStrategy(FeatureCode featureCode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
	
}
