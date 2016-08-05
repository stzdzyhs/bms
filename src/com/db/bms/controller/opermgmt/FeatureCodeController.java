package com.db.bms.controller.opermgmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.db.bms.common.file.UploadResult;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.entity.FeatureCode.FeatureCodeStatus;
import com.db.bms.service.FeacodeGroupMapService;
import com.db.bms.service.FeatureCodeService;
import com.db.bms.service.LogService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.StrategyConditionService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.Result;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping(value="opermgmt/featureCode")
@Controller
public class FeatureCodeController {
	
	private final static Logger logger = Logger.getLogger(PortalController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private FeatureCodeService featureCodeService;
	
	@Autowired
	private FeacodeGroupMapService feacodeGroupMapService;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	@Autowired
	private StrategyConditionService strategyConditionService; 
	
	@RequestMapping(value="/featureCodeList.action")
	public String featureCodeList(HttpServletRequest request, ModelMap modelMap, 
											FeatureCode search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		PageUtil page = search.getPageUtil();
    	page.setPaging(true);
    	switch (curOper.getType()){
	    	case Operator.TYPE_SUPER_ADMIN:
	        	break;
	        case Operator.TYPE_COMPANY_ADMIN:
	        	search.setGroupId(curOper.getOperatorNo());
	        	break;
	        case Operator.TYPE_ORDINARY_OPER:
	        	search.setOperatorNo(curOper.getOperatorNo());
	        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(
	        					ResourceAllocation.ResourceType.FEATURE_CODE.getIndex(), curOper.getOperatorNo());
	        	search.setAllocResourceIds(allocResourceIds);
				break;
			default:
				ResultCode.throwResultCodeException(ResultCode.LOGIN_ERROR);
				break;
    	}
		List<FeatureCode> list = this.featureCodeService.getFeatureCodeList(search);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);			
		return "opermgmt/featureCode/featureCodeList";
	}
	
	@RequestMapping(value="/saveOrUpdateFeatureCode.action")
	@ResponseBody
	public String saveOrUpdateFeatureCode(HttpServletRequest request, HttpServletResponse response, 
															FeatureCode featureCode) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);	
		featureCode.setCurOper(curOper);
		Result result = new Result();
		String logStr = "";
		try {
			if(featureCode.getFeatureCodeNo() != null && featureCode.getFeatureCodeNo() > 0) { //update	
/*				boolean isPermit = this.featureCodeService.checkForWrite(featureCode);
				if (isPermit) { //有更新权限
					result.setResult(true);
					featureCode.setUpdateTime(DateUtil.getCurrentTime());
					this.featureCodeService.updateFeature(featureCode);
				} else {
					//result.setResult(false);
            		result.setDesc("您没有操作权限");
				}*/
				logStr = "更新[ " + featureCode.getFeatureCodeVal() + " ]特征码";
				this.featureCodeService.updateFeature(featureCode);
				result.setResult(true);
			} else { //new 
				logStr = "新增[ " + featureCode.getFeatureCodeVal() + " ]特征码";
				this.featureCodeService.saveFeature(featureCode);
				result.setResult(true);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, result.isResult(), true);
	
		} catch (Exception e) {
			
			if (featureCode.getFeatureCodeNo() == null) {
				logger.error("Add featureCode system exception occurred, cause by:{}", e);
			} else {
				logger.error("Update featureCode system exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, result.isResult(), true);
			result.setDesc("");
			result.setResult(false);
		}
		
		return JSON.toJSONString(result);
		//return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value="/featureCodeDetail.action")
	public String featureCodeDetail(HttpServletRequest request, ModelMap modelMap,
												Long featureCodeNo) throws Exception {
		FeatureCode featureCode = this.featureCodeService.getFeatureCodeDetail(featureCodeNo);
		
		modelMap.put("featureCode", featureCode);
		modelMap.put("featureCodeMap", ConstConfig.featureCodeMap);
		return "opermgmt/featureCode/featureCodeDetail";
	}
	
	
	@RequestMapping(value="/featureCodeEdit.action")
	public String featureCodeEdit(HttpServletRequest request, ModelMap modelMap,
									FeatureCode search, Long featureCodeNo) throws Exception {
		//Operator curOper = SessionUtil.getActiveOperator(request);
		FeatureCode featureCode = new FeatureCode();
		if(featureCodeNo != null && featureCodeNo > 0) {
			featureCode = this.featureCodeService.findFeatureCodeById(featureCodeNo);
		}
		modelMap.put("featureCode", featureCode);
		modelMap.put("search", search);
		
		return "opermgmt/featureCode/featureCodeEdit";
	}
	
	@RequestMapping(value="/checkfeatureCodeVal.action")
	@ResponseBody
	public String checkfeatureCodeVal(FeatureCode featureCode) throws Exception {
		boolean result = false;
		FeatureCode data = new FeatureCode();
        data.setFeatureCodeNo(featureCode.getFeatureCodeNo());
        data.setFeatureCodeVal(featureCode.getFeatureCodeVal());
        result = this.featureCodeService.isRepeatFeatureCode(data);
        return "{result: '" + !result + "', desc : ''}";
	}
	
	
	@RequestMapping(value="/featureCodeDelete.action")
	@ResponseBody
	public String featureCodeDelete(HttpServletRequest request, HttpServletResponse response, 
										Long[] feaNos) throws Exception {		
		boolean result = false;
		String str = "";
		try {
			if(feaNos !=null && feaNos.length > 0) {
				if(this.strategyConditionService.isRefStrategyCondition(new Long(StrategyCondition.TYPE_FEATURE_CODE), feaNos)){
					return "{result: '" + result + "', desc : 'reference'}";
				}
				List<FeatureCode> feaList = featureCodeService.findFeatureCodesById(feaNos);
				for(int i=0; i<feaList.size()-1; i++) {
					str += feaList.get(i).getFeatureCodeVal() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				
				str += feaList.get(feaList.size() - 1).getFeatureCodeVal();
				this.feacodeGroupMapService.featureCodeNos(feaNos);
				
				this.featureCodeService.deleteFeatureCodes(feaNos);
			}
				result = true;
				logService.logToDB(request, "删除[" + str + "]特征码", LogUtil.LOG_INFO, true, true);
			} catch (Exception e) {
				logService.logToDB(request, "删除[" + str + "]特征码", LogUtil.LOG_ERROR,false, true);
				result = false;
				logger.error("Delete featureCode system exception occurred, cause by:{}", e);
			}
		
		return "{result: '" + result + "', desc : ''}";
	}
	
	/**
	 * 特征码上传
	 * @param request
	 * @param file
	 * @param filename
	 * @param feature 特征码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadFeatureCode.action",method=RequestMethod.POST ,produces = {ConstConfig.APP_JSON,ConstConfig.TEXT_PLAIN})
	@ResponseBody
	public String uploadFeatureCode(HttpServletRequest request ,@RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String filename, FeatureCode feature) throws Exception {
		UploadResult result = new UploadResult();
		File dirPath = this.featureCodeService.getFeatureCodeDirFile();
    	String genFileName = FileUpload.geneFileName(filename);
    	boolean isok = FileUpload.saveFile(file, dirPath.toString(), genFileName);
    	File f = null;
    	if (isok) {  //上传成功
    		f = new File(dirPath, genFileName);
			String charset = FileUtils.get_charset(f);
			if(!"UTF-8".equals(charset)) { //检查编码
				if(f.exists() && f.isFile()) {
					f.delete();
				}
				//throw new Exception("特征码只支持UTF-8编码，请确认！");
				result.setDesc("特征码只支持UTF-8编码，请确认！");
			} else  {
    			if(f.exists() && f.isFile()){
    				result.setResult(true);    				
            		result.setFilePath(f.getName());
    			}
        	}
    	}
        
        String ret = JSON.toJSONString(result);
        return ret;
	}
	
	@RequestMapping(value="/saveToDb.action" ,method = {RequestMethod.POST}, produces = {ConstConfig.APP_JSON,ConstConfig.TEXT_PLAIN})
    @ResponseBody
	public String saveToDb (HttpServletRequest request ,HttpServletResponse response ,String fileName) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		Result result = new Result();
		FeatureCode fea = new FeatureCode();
		fea.setCurOper(curOper);
		String logStr = "";
		try {  
			String dirPath = this.featureCodeService.getFeatureCodeDirFile().toString();
			File f = new File(dirPath, fileName);			
			//解析上传文档
			Integer totalCount = this.featureCodeService.resolveFeatureCode(f, fea);
			
			if(totalCount > 0) {
    			logStr = "新增[" + totalCount + "]条特征码记录";
    			logService.logToDB(request,logStr, LogUtil.LOG_INFO, true, true);
				result.setResult(true);
    		} else if (totalCount == 0) {
    			logStr = "上传特征码文件重复或者不正确";
    			logService.logToDB(request,logStr, LogUtil.LOG_INFO, true, false);
    		} else {
    			logStr = "特征码值不支持中文";
    			logService.logToDB(request,logStr, LogUtil.LOG_INFO, true, false);
    		}
			result.setDesc(logStr);
			//删除上传的文件
    		if(f.exists() && f.isFile()) {
				f.delete();
    		}
			
		} catch (Exception e) {
			result.setDesc("");
			logger.error("occured an exception when save featureCode to DB caused by", e);
		}
		
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "/featureCodeAudit.action", method = RequestMethod.POST)
	@ResponseBody
	public String featureCodeAudit(HttpServletRequest request,
			Integer status, Long[] feaNos) throws Exception {
		boolean result = false;
		String str = "";
		try {
			if (feaNos != null && feaNos.length > 0) {
				List<FeatureCode> list = this.featureCodeService.findFeatureCodesById(feaNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getFeatureCodeVal() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getFeatureCodeVal();
				this.featureCodeService.auditFeatureCode(status, feaNos);
			}
			result = true;
			switch (FeatureCodeStatus.getStatus(status)) {
			case ENABLE:
				logService.logToDB(request, "特征码[" + str + "]启用",
						LogUtil.LOG_INFO, true, true);
				break;
			case DISABLE:
				logService.logToDB(request, "特征码[" + str + "]禁用",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (FeatureCodeStatus.getStatus(status)) {
			case ENABLE:
				logService.logToDB(request, "特征码[" + str + "]启用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Enable featureCode exception occurred, cause by:{}",
						e);
				break;
			case DISABLE:
				logService.logToDB(request, "特征码[" + str + "]禁用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Disable featureCode exception occurred, cause by:{}",
						e);
				break;
			}

		}
		
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value="/featureCodeSelect.action")
	public String featureCodeSelect(HttpServletRequest request, ModelMap modelMap, 
											FeatureCode search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		search.setStatus(FeatureCodeStatus.ENABLE.getIndex());
		List<FeatureCode> list = this.featureCodeService.getFeatureCodeList(search);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
			
		return "opermgmt/featureCode/featureCodeSelect";
	}
	
	@RequestMapping(value = "/downloadFeatureCodeTemplate.action")
	@ResponseBody
	public String downloadTemplate(HttpServletRequest request,
			HttpServletResponse response, String fileName) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String ctxPath = request.getSession().getServletContext()
				.getRealPath("/");
		String downloadPath = ctxPath + ConstConfig.DOWNLOAD_PATH + 
					ConstConfig.FEATURECODE_PATH + fileName;
		System.out.println("downloadPath-->" + downloadPath);
		
		try {
			long length = new File(downloadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ new String(fileName.getBytes("UTF-8"), "ISO8859-1")
					+ "\"");
			response.setHeader("Content-Length", String.valueOf(length));
			bis = new BufferedInputStream(new FileInputStream(downloadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int len;
			while (-1 != (len = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, len);
			}
			logService.logToDB(request, "下载特征码模板【" + fileName + "】",
					LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "下载特征码模板【" + fileName + "】",
					LogUtil.LOG_ERROR, false, true);
			logger.error(
					"Download FeatureCode template file exception occurred, cause by:{}",
					e);
		} finally {
			if (bis != null) {
				bis.close();
			}

			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}

}
