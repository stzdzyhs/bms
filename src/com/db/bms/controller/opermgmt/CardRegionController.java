package com.db.bms.controller.opermgmt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.entity.CardRegion.CardRegionType;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.StrategyConditionService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/region")
@Controller
public class CardRegionController {

	private final static Logger logger = Logger
			.getLogger(CardRegionController.class);

	@Autowired
	private CardRegionService cardRegionService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private StrategyConditionService strategyConditionService;
	
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
		return "opermgmt/region/cardRegionList";
	}

	@RequestMapping(value = "/cardRegionDetail.action")
	public String cardRegionDetail(HttpServletRequest request, ModelMap modelMap,
			Long regionId) throws Exception {
		CardRegion region = cardRegionService.findRegionById(regionId);
		switch (RegionCodeType.getType(region.getCodeType())){
		case REGION:
			region.setRegionCode(Integer.valueOf(region.getRegionCode()).toString());
			break;
		case SECTION:
			region.setRegionSectionBegin(Integer.valueOf(region.getRegionSectionBegin()).toString());
			region.setRegionSectionEnd(Integer.valueOf(region.getRegionSectionEnd()).toString());
			break;
		}
		modelMap.addAttribute("region", region);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "opermgmt/region/cardRegionDetail";
	}
	
    @RequestMapping(value = "/cardRegionEdit.action")
    public String cardRegionEdit(HttpServletRequest request, ModelMap modelMap, CardRegion search, Long regionId, Long parentId) throws Exception {
    	
    	CardRegion region = new CardRegion();
    	if (regionId != null && regionId > 0){
    		region = cardRegionService.findRegionById(regionId);
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
    	
    	CardRegion data = new CardRegion();
    	data.setType(CardRegionType.SECTION.getIndex());
    	List<CardRegion> parentList = cardRegionService.findAllRegions(data);
    	modelMap.put("parentId", parentId);
    	modelMap.put("region", region);
    	modelMap.put("search", search);
    	modelMap.put("parentList", parentList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
    	return "/opermgmt/region/cardRegionEdit";
    }

	@RequestMapping(value = "/checkName.action")
	@ResponseBody
	public String checkName(CardRegion region) throws Exception {
		boolean result = false;
		CardRegion data = new CardRegion();
		data.setId(region.getId());
		data.setRegionName(region.getRegionName());
		result = this.cardRegionService.isRegionRepeat(data);
		return "{result: '" + !result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/checkCardRegionCode.action")
	@ResponseBody
	public String checkCardRegionCode(CardRegion region) throws Exception {
		boolean result = false;
		CardRegion data = new CardRegion();
		data.setId(region.getId());
		data.setRegionCode(StringUtils.formatRegionCode(region.getRegionCode()));
		data.setRegionSectionBegin(StringUtils.formatRegionCode(region.getRegionSectionBegin()));
		data.setRegionSectionEnd(StringUtils.formatRegionCode(region.getRegionSectionEnd()));
		data.setType(region.getType());
		result = this.cardRegionService.isRegionRepeat(data);
		return "{result: '" + !result + "', desc : ''}";
	}

	@RequestMapping(value = "/saveOrUpdateCardRegion.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateCardRegion(HttpServletRequest request,
			HttpServletResponse response, CardRegion region) throws Exception {
		boolean result = false;
		String logStr = "";
		try {
			CardRegion data = new CardRegion();
			data.setId(region.getId());
			data.setRegionCode(StringUtils.formatRegionCode(region.getRegionCode()));
			data.setRegionSectionBegin(StringUtils.formatRegionCode(region.getRegionSectionBegin()));
			data.setRegionSectionEnd(StringUtils.formatRegionCode(region.getRegionSectionEnd()));
			data.setType(region.getType());
			boolean isRepeat = this.cardRegionService.isRegionRepeat(data);
			if (isRepeat){
				return "{result: '" + result + "', desc : 'repeatCode'}";
			}
			
			data = new CardRegion();
			data.setId(region.getId());
			data.setRegionName(region.getRegionName());
		    isRepeat = this.cardRegionService.isRegionRepeat(data);
			if (isRepeat){
				return "{result: '" + result + "', desc : 'repeatName'}";
			}
			
			
			region.setRegionCode(StringUtils.formatRegionCode(region.getRegionCode()));
			region.setRegionSectionBegin(StringUtils.formatRegionCode(region.getRegionSectionBegin()));
			region.setRegionSectionEnd(StringUtils.formatRegionCode(region.getRegionSectionEnd()));
			if (region.getId() != null && region.getId() > 0) {
				logStr = "更新[" + region.getRegionName() + "]智能卡区域";
				region.setUpdateTime(DateUtil.getCurrentTime());
				if (region.getParentId() == null){
					region.setParentId(-1L);
				}
				cardRegionService.updateRegion(region);
			} else {
				logStr = "添加[" + region.getRegionName() + "]智能卡区域";
				region.setCreateTime(DateUtil.getCurrentTime());
				if (region.getParentId() == null){
					region.setParentId(-1L);
				}
				cardRegionService.addRegion(region);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (region.getId() == null) {
				logger.error("Add smart card region exception occurred, cause by:{}", e);
			} else {
				logger.error("Update smart card region exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/cardRegionDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String cardRegionDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] regionIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			if (regionIds != null && regionIds.length > 0) {
				if(this.strategyConditionService.isRefStrategyCondition(new Long(StrategyCondition.TYPE_CARD_REGION), regionIds)){
					return "{result: '" + result + "', desc : 'reference'}";
				}
				List<CardRegion> list = cardRegionService.findRegionsById(regionIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getRegionName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getRegionName();
				cardRegionService.deleteRegions(regionIds);

			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]智能卡区域", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]智能卡区域", LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error("Delete smart card region exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/cardRegionSelect.action")
	public String cardRegionSelect(HttpServletRequest request, ModelMap modelMap, CardRegion search, Long selectRegionId)
			throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		if (OperatorType.getType(curOper.getType()) == OperatorType.SUPER_ADMIN || curOper.getCompany().getParentId() == -1){
			search.setCompanyNo(null);
		}
		search.setType(CardRegionType.REGION.getIndex());
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
		modelMap.put("selectRegionId", selectRegionId);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "opermgmt/region/cardRegionSelect";
	}
	
	@RequestMapping(value = "/cardRegionMultiSelect.action")
	public String cardRegionMultiSelect(HttpServletRequest request, ModelMap modelMap, CardRegion search,String regionId)
			throws Exception {

		search.setType(CardRegionType.REGION.getIndex());
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
		modelMap.put("regionId", regionId);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "opermgmt/region/cardRegionMultiSelect";
	}

	@RequestMapping(value = "/checkCardRegionReference.action", method = RequestMethod.POST)
	@ResponseBody
	public String checkCardRegionReference(HttpServletRequest request, Long[] regionIds)
			throws Exception {
		boolean result = false;
		try {

			result = true;

		} catch (Exception e) {
			logger.error(
					"Reference check on the card region whether be strategy exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/publishCardRegionSelect.action")
	public String publishCardRegionSelect(HttpServletRequest request, ModelMap modelMap, CardRegion search)
			throws Exception {

		search.setType(CardRegionType.REGION.getIndex());
        Operator curOper = SessionUtil.getActiveOperator(request);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	break;
        case ORDINARY_OPER:
        	Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
        	List<Company> allocCompanys = operator.getCompanys();
        	List<Long> companyNos = new ArrayList<Long>();
        	if (allocCompanys != null && allocCompanys.size() > 0){
        		for (Company company : allocCompanys){
            		companyNos.add(company.getCompanyNo());
        		}

        		search.setCompanyNos(companyNos);
        	}
        	break;
        }
        
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
		return "opermgmt/region/publishCardRegionSelect";
	}
}
