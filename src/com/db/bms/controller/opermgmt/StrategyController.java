package com.db.bms.controller.opermgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Client;
import com.db.bms.entity.Company;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Internet;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.Space;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.FeatureCodeService;
import com.db.bms.service.InternetService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.SpaceService;
import com.db.bms.service.StrategyService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.Result2;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/strategy")
@Controller
public class StrategyController {

	private final static Logger logger = Logger.getLogger(StrategyController.class);
	
	@Autowired
	private StrategyService strategyService;

	@Autowired
	private LogService logService;

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
    private SpaceService spaceService;
	
	@Autowired
    private InternetService internetService;
	
	@Autowired
	private CardRegionService cardRegionService;
	
	@Autowired
	private FeatureCodeService featureCodeService;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	/**
	 * 查询策略
	 */
	@RequestMapping(value = "/strategyList.action")
    public String strategyList(HttpServletRequest request, ModelMap modelMap, Strategy search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:   
        	break;
        case COMPANY_ADMIN:
        	search.setOperatorNo(curOper.getOperatorNo());
        	search.setCreateBy(curOper.getOperatorNo());
        	break;
        case ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	// ORDINARY_OPER can see the strategies created by its creator ?
        	//search.setCreateBy(curOper.getCreateBy());
        	search.setCreateBy(curOper.getOperatorNo());
        	break;
        }
		PageUtil page = search.getPageUtil();
    	page.setPaging(true);
		List<Company> companyList = companyService.findAllCompanys(curOper);
		List<Strategy> list = strategyService.findStrategys(search);
		List<Operator> operatorList = operatorService.findAllOperators(curOper);
		modelMap.put("search", search);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("companyList", companyList);
		modelMap.put("auditStatusMap", ConstConfig.auditStatusMap);
		modelMap.put("operatorList", operatorList);
		
		return "opermgmt/strategy/strategyList";
	 } 
	 
    @RequestMapping(value = "/getStrategyAllData.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String getStrategyAllData(HttpServletRequest request, Long strategyId) throws Exception {
		Result2<Strategy> result = new Result2<Strategy>();
		try {
			Strategy st = strategyService.getStrategyAllData(strategyId);
			result.data = st;
		}
		catch (Exception e) {
			result = ResultCode.convertException(e);
			logger.error(e);
		}
		String ret = result.toString();
		return ret;
    }
    
	
	/** 
	 * 保存策略
	 */
	// save strategy. add/ update
	@RequestMapping(value = "/saveStrategy.action", produces=ConstConfig.APP_JSON )
	@ResponseBody
	public String saveStrategy(HttpServletRequest request,String strategy) throws Exception{
		Result2<Object> ret = new Result2<Object>();
		String logStr = ""; 
		try {
			Operator curOper = SessionUtil.getActiveOperator(request);
			Strategy st = JSONObject.parseObject(strategy, Strategy.class);
			logStr = "保存策略[" + st.getStrategyName() + "]";
			st.setCurOper(curOper);
			if(st.getStrategyNo() != null){
				st.setAuditStatus(AuditStatus.DRAFT);
			}
			this.strategyService.saveStrategy(st);

			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		}
		catch(Exception e) {
			ret  = ResultCode.convertException(e);
			logger.error("",e);
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
		}	
		 
		String s = ret.toString(); 
		return s;
	}
	
	 /** 
	  * 删除策略
	  */
	 @RequestMapping(value = "/deleteStrategy.action", produces = ConstConfig.APP_JSON)
	 @ResponseBody
	 public String deleteStrategy(HttpServletRequest request, Long[] strategyNos) throws Exception { 
	        boolean result = false;
	        String str = "";
	        try { 
	            if (strategyNos != null && strategyNos.length > 0) {  
	            	strategyService.deleteStrategysByNo(strategyNos);   
	            } 
	            result = true;
				logService.logToDB(request, "删除[" + str + "]投放策略",
						LogUtil.LOG_INFO, true, true);
	        }
	        catch (Exception e) { 
	        	str = e.getMessage();
	        	logService.logToDB(request, "删除[" + str + "]投放策略",
						LogUtil.LOG_INFO, false, true);
				result = false;
	            logger.error("Delete internets exception occurred, cause by:{}", e); 
	        }  
	        return "{result: '" + result + "', desc : '"+str+"'}";
	    }
	 
	 /** 
	  * 策略详情
	  */
	 @RequestMapping(value = "/strategyDetail.action")
	 public String strategyDetail(HttpServletRequest request, ModelMap modelMap, Long strategyNo) throws Exception {
		 Strategy strategy = strategyService.getStrategyAllData(strategyNo);
	     modelMap.put("strategy", strategy);
	     return "opermgmt/strategy/strategyDetail";
	 }
	 
	@RequestMapping(value = "/strategyUpdateAuditStatus.action", method = RequestMethod.POST)
	@ResponseBody
	public String strategyAudit(HttpServletRequest request,Integer auditStatus, Long[] strategyNos) throws Exception {
		boolean result = false;
		String str = "";
		try {						
			if (strategyNos != null && strategyNos.length > 0) {
				List<Long> noList = Arrays.asList(strategyNos);
				List<Strategy> list = strategyService.findStrategyByNos(noList);
				for (Strategy strategy : list) {
					str += strategy.getStrategyName()+Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}								
				strategyService.auditStrategy(auditStatus, strategyNos);

			}
			result = true;
			
			if(auditStatus==null) {
				throw new ResultCodeException(ResultCode.INVALID_PARAM, "status null");
			}
			switch (auditStatus) {
			case AuditStatus.AUDITING:
				logService.logToDB(request, "策略[" + str + "]提交审核",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.AUDIT_PASS:
				logService.logToDB(request, "策略[" + str + "]审核通过",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.AUDIT_NO_PASS:
				logService.logToDB(request, "策略[" + str + "]审核不通过",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.PUBLISH:
				logService.logToDB(request, "策略[" + str + "]发布",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.UNPUBLISH:
//				processor.putNoticeToQueue(publish.getNoticeList());
				logService.logToDB(request, "策略[" + str + "]取消发布",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (auditStatus) {
			case AuditStatus.AUDITING:
				logService.logToDB(request, "策略[" + str + "]提交审核",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Strategy on submit audit exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.AUDIT_PASS:
				logService.logToDB(request, "策略[" + str + "]审核通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Strategy on audit pass exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.AUDIT_NO_PASS:
				logService.logToDB(request, "策略[" + str + "]审核不通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Strategy on audit no pass exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.PUBLISH:
				logService.logToDB(request, "发布策略[" + str + "]",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Strategy on publish exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.UNPUBLISH:
				logService.logToDB(request, "取消发布策略[" + str + "]",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Strategy on unpublish exception occurred, cause by:{}",
						e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}
	 
	 /**
	  * 新增页面
	  */
    @RequestMapping(value = "/strategyEdit2.action")
	public String strategyEdit(HttpServletRequest request, ModelMap modelMap, Long strategyNo) throws Exception {
    	modelMap.put("strategyNo", strategyNo);

		Operator curOper = SessionUtil.getActiveOperator(request);
		Company company = new Company();
		if(!curOper.isSuperAdmin()) {
			company.setCompanyNo(curOper.getCompanyNo());
		}
		List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.put("companyList",companyList);
		return "/opermgmt/strategy/strategyEdit2";
		
//		  Strategy strategy = new Strategy();
//		  Operator curOper = SessionUtil.getActiveOperator(request);
//		  List<Company> companyList = companyService.findAllCompanys(curOper);
//		  if(strategyNo != null){ 
//			  strategy = strategyService.findStrategyByNo(strategyNo); 
//		  }
//		  modelMap.put("companyList", companyList);
//		  modelMap.put("strategy", strategy);
//		  List<Space> space = new ArrayList<Space>();
//		  modelMap.put("space",space);
//		  return "opermgmt/strategy/strategyEdit2";
    }
	 
    /**
	 * 空分组列表
	 */
	@RequestMapping(value = "/spaceList.action")
	public String spaceList(HttpServletRequest request, ModelMap modelMap, Space search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		List<Space> belongList = null;
		List<Space> list = null;
		List<Long> distSpaceList = new ArrayList<Long>();
		switch (OperatorType.getType(curOper.getType())){
		case ORDINARY_OPER:
			List<Space> spaceList = spaceService.findDistSpaceByOperNo(curOper.getOperatorNo());
			if (spaceList != null && spaceList.size() > 0){
				for (Space cmpy : spaceList){
					distSpaceList.add(cmpy.getSpaceNo());
				}
			}
	        break;
		}
	        
		belongList =  spaceService.findAllSpaces(distSpaceList);
		list = this.spaceService.findSpaces(search,distSpaceList);
		modelMap.put("belongList", belongList);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		return "opermgmt/space/spaceListGrounp";
	}
	 
	/**
	 * 保存空分组
	 */
	@RequestMapping(value = "/saveSpace.action")
	@ResponseBody
	public String saveSpace(HttpServletRequest request, ModelMap modelMap, String rtIs) throws Exception {
		List<Space> list = null;
		Space space = new Space();
		try{
			String[] ids = rtIs.split(",");
			for(int i=0;i<ids.length;i++){
				if(ids[i] != " "){ 
					space = spaceService.findSpaceById(ids[i]);
				}
			}
		}catch(Exception e){
			logger.error("Delete internets exception occurred, cause by:{}", e); 
		}
		modelMap.put("list",list);
		modelMap.put("space",space);
		return "opermgmt/strategy/strategyEdit2";
	}
	 
	/**
	 * 网络ID列表
	 */
	@RequestMapping(value = "/internetList.action")
	public String internetList(HttpServletRequest request, ModelMap modelMap, Internet search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		List<Internet> belongList = null;
		List<Internet> list = null;
		List<Long> distInternetList = new ArrayList<Long>();
		switch (OperatorType.getType(curOper.getType())){
		case ORDINARY_OPER:
			List<Internet> internetList = internetService.findDistInternetByOperNo(curOper.getOperatorNo());
			if (internetList != null && internetList.size() > 0){
				for (Internet cmpy : internetList){
					distInternetList.add(cmpy.getInternetNo());
				}
			}
			break;
		}
	        
		belongList =  internetService.findAllInternets(distInternetList);
		list = this.internetService.findInternets(search,distInternetList);
		modelMap.put("belongList", belongList);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		return "opermgmt/internet/internetListGrounp";
	}
	 
	 
	/**
	 * 区域码列表
	 */
	@RequestMapping(value = "/cardRegionList.action")
	public String cardRegionList(HttpServletRequest request, ModelMap modelMap, CardRegion search)
			throws Exception {

		List<CardRegion> regionList = cardRegionService.findRegions(search);
		Iterator<CardRegion> it = regionList.iterator();
		while (it.hasNext()){
			CardRegion region = it.next();
			switch (RegionCodeType.getType(region.getCodeType())){
			case REGION:
				region.setRegionCode(Integer.valueOf(region.getRegionCode()).toString());
				break;
			case SECTION:
				region.setRegionSectionBegin(Integer.valueOf(region.getRegionSectionBegin()).toString());
				region.setRegionSectionEnd(Integer.valueOf(region.getRegionSectionEnd()).toString());
				break;
			}
		}
		modelMap.addAttribute("list", regionList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "opermgmt/region/cardRegionListGrounp";
	}
	 
	/**
	 * 特征码列表
	 */
	@RequestMapping(value="/featureCodeList.action")
	public String featureCodeList(HttpServletRequest request, ModelMap modelMap, 
			FeatureCode search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		switch (OperatorType.getType(curOper.getType())){
		case SUPER_ADMIN:
			break;
		case COMPANY_ADMIN:
			search.setGroupId(curOper.getOperatorNo());
			break;
		case ORDINARY_OPER:
			search.setOperatorNo(curOper.getOperatorNo());
			List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(
					ResourceAllocation.ResourceType.FEATURE_CODE.getIndex(), curOper.getOperatorNo());
			search.setAllocResourceIds(allocResourceIds);
			break;
		}
		List<FeatureCode> list = this.featureCodeService.getFeatureCodeList(search);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);			
		return "opermgmt/featureCode/featureCodeListGrounp";
	}
	 
	/**
	 * 保存特征码
	 */
	@RequestMapping(value="/saveFeatureCode.action")
	public String saveFeatureCode(HttpServletRequest request, ModelMap modelMap, Long[] feaNos) throws Exception{
		boolean result = false;
		List<FeatureCode> list = null;
		try{
			if(feaNos.length>0){
				list = featureCodeService.findFeatureCodesById(feaNos);
			}
		}catch(Exception e){
			logger.error("Delete internets exception occurred, cause by:{}", e); 
		}
		modelMap.put("list",list);
		return "{result: '" + result + "', desc : ''}";
	}
	 
	 
    @RequestMapping(value = "/strategyCardRegionNoSelect.action")
	public String strategyCardRegionNoSelect(HttpServletRequest request, ModelMap modelMap, Long strategyNo,
			Long companyNo,
			CardRegion search, Long[] excludeIds) throws Exception {
    	try {
    		search.getPageUtil().setPaging(true);
	    	List<CardRegion> list = strategyService.findStrategyCardRegionNoSelect(strategyNo, companyNo, search, excludeIds);
		    	
		    modelMap.put("strategyNo", strategyNo);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
		    modelMap.put("list", list);
			modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
			modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
    	}
    	catch(Exception e) {
    		logger.error(e); 
    	}
	    return "/opermgmt/region/strategyCardRegionNoSelect";
    }
    
    /*
    @RequestMapping(value = "/strategyInternetNoSelect.action")
	public String strategyInternetNoSelect(HttpServletRequest request, ModelMap modelMap, Long strategyNo,
			Long companyNo,
			Internet search, Long[] excludeIds) throws Exception {
    	try {
	    	List<Internet> list = strategyService.findStrategyInternetNoSelect(strategyNo, companyNo, search, excludeIds);
		    	
		    modelMap.put("strategyNo", strategyNo);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
		    modelMap.put("list", list);
    	}
    	catch(Exception e) {
	    	 logger.error(e); 
    	}
    	return "/opermgmt/internet/strategyInternetNoSelect";
	}
	*/
    
    @RequestMapping(value = "/strategyCompanyNoSelect.action")
	public String strategyCompanyNoSelect(HttpServletRequest request, ModelMap modelMap, Long strategyNo,
			Long companyNo,
			Company search, Long[] excludeIds) throws Exception {
    	try {
    		search.getPageUtil().setPaging(true);
	    	List<Company> list = strategyService.findStrategyCompanyNoSelect(strategyNo, companyNo, search, excludeIds);

	    	String q = request.getQueryString();
	    	//System.out.println("*** q:" + q);
	    	modelMap.put("selectUrl", "/opermgmt/strategy/strategyCompanyNoSelect.action?"+ q );
	    	modelMap.put("multiselect", true);
	    	
		    modelMap.put("strategyNo", strategyNo);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
		    modelMap.put("list", list);
    	}
    	catch(Exception e) {
	    	 logger.error(e); 
    	}
    	return "/opermgmt/company/companySelect";
	}
    
    
    @RequestMapping(value = "/strategySpaceNoSelect.action")
	public String strategySpaceNoSelect(HttpServletRequest request, ModelMap modelMap, Long strategyNo,
			Long companyNo, Space search, Long[] excludeIds) throws Exception {
    	try {
    		search.getPageUtil().setPaging(true);
	    	List<Space> list = strategyService.findStrategySpaceNoSelect(strategyNo, companyNo, search, excludeIds);
		    	
		    modelMap.put("strategyNo", strategyNo);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
		    modelMap.put("list", list);
    	}
    	catch(Exception e) {
	    	 logger.error(e); 
    	}
	    return "/opermgmt/space/strategySpaceNoSelect";
    }
    
    @RequestMapping(value = "/strategyFeatureCodeNoSelect.action")
	public String strategyFeatureCodeNoSelect(HttpServletRequest request, ModelMap modelMap, Long strategyNo,
			Long companyNo,
			FeatureCode search, Long[] excludeIds) throws Exception {
    	search.getPageUtil().setPaging(true);
    	try {
	    	List<FeatureCode> list = strategyService.findStrategyFeatureCodeNoSelect(strategyNo, companyNo, search, excludeIds);
		    	
		    modelMap.put("strategyNo", strategyNo);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
		    modelMap.put("list", list);
		    
			modelMap.put("featureCodeMap", ConstConfig.featureCodeMap);
    	}
    	catch(Exception e) {
	    	 logger.error(e); 
    	}
	    return "/opermgmt/featureCode/strategyFeatureCodeNoSelect";
    }
    
    @RequestMapping(value = "/strategyClientNoSelect.action")
	public String strategyClientNoSelect(HttpServletRequest request, ModelMap modelMap, Long strategyNo,
			Long companyNo,
			Client search, Long[] excludeIds) throws Exception {
    	try {
    		search.getPageUtil().setPaging(true);
	    	List<Client> list = strategyService.findStrategyClientNoSelect(strategyNo, companyNo, search, excludeIds);
		    	
		    modelMap.put("strategyNo", strategyNo);
		    modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
		    modelMap.put("list", list);
			modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
			modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
    	}
    	catch(Exception e) {
	    	 logger.error(e); 
    	}
	    return "/opermgmt/client/strategyClientNoSelect";
    }

    
    @RequestMapping(value = "/publishStrategySelect.action")
	public String strategyClientNoSelect(HttpServletRequest request, ModelMap modelMap, Strategy search) throws Exception {
    	try {
    		Operator curOper = SessionUtil.getActiveOperator(request);
    		switch (OperatorType.getType(curOper.getType())){
            case SUPER_ADMIN:
            	break;
            case COMPANY_ADMIN:
            	search.setCompanyNo(curOper.getCompanyNo());
            	break;
            case ORDINARY_OPER:
            	search.setCompanyNo(curOper.getCompanyNo());
            	break;
    		}
    		search.setCurOper(curOper);
    		search.setAuditStatus(AuditStatus.PUBLISH);
    		search.getPageUtil().setPaging(true);
    		List<Strategy> list = strategyService.findStrategys(search);
		    	
		    modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
			
			modelMap.put("selectUrl",  "/opermgmt/strategy/publishStrategySelect.action");
			modelMap.put("multiselect",  true);
			
		    modelMap.put("list", list);
    	}
    	catch(Exception e) {
	    	 logger.error(e); 
    	}
	    return "/opermgmt/strategy/strategySelect";
    }
    
    
}
