package com.db.bms.controller.opermgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResStrategyMap;
import com.db.bms.entity.Strategy;
import com.db.bms.service.CompanyService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.ResStrategyMapService;
import com.db.bms.service.StrategyService;
import com.db.bms.utils.spring.SessionUtil;


@RequestMapping("opermgmt/resStrategyMap")
@Controller
public class ResStrategyMapController {

	private final static Logger logger = Logger.getLogger(ResStrategyMapController.class);
	
	@Autowired
	private ResStrategyMapService resStrategyMapService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private StrategyService strategyService;
	
	/**
	 * 查询策略
	 */
	@RequestMapping(value = "/strategyList.action")
    public String strategyList(HttpServletRequest request, ModelMap modelMap, Strategy search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		if(!curOper.isSuperAdmin()) {
			search.setCompanyNo(curOper.getCompanyNo());
			search.setOperatorNo(curOper.getOperatorNo());
		}
		
		List<Company> companyList = companyService.findAllCompanys(curOper);
		List<Strategy> list = strategyService.findStrategys(search);
		List<Operator> operatorList = operatorService.findAllOperators(curOper);
		 
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("companyList", companyList);
		modelMap.put("operatorList", operatorList);
		return "opermgmt/strategy/strategyList";
	 } 
	
	/**
	 * 添加资源策略
	 */
	@RequestMapping(value = "/addResStrategyMap.action")
	public String addResStrategyMap(HttpServletRequest request,Long type,Long resId,Long strategyNo) throws Exception{
		boolean result = true;
		ResStrategyMap resStrategyMap = new ResStrategyMap();
		if(type != null){
		   resStrategyMap.setType(type);
		}
		if(resId != null){
		   resStrategyMap.setResId(resId);	
		}
		if(strategyNo != null){
		   resStrategyMap.setStrategyNo(strategyNo);	
		}
		resStrategyMapService.addResStrategyMap(resStrategyMap);
		return "{result: '" + result + "', desc : 'repeatName'}";
	}
	
	/**
	 * 查找资源策略
	 */
	@RequestMapping(value = "/findResStrategyMap.action")
	public String findResStrategyMap(HttpServletRequest request, ModelMap modelMap,Long type,Long resId) throws Exception{
		boolean result = true;
		ResStrategyMap resStrategyMap = new ResStrategyMap();
		if(type != null){
		   resStrategyMap.setType(type);
		}
		if(resId != null){
		   resStrategyMap.setResId(resId);	
		}
		List<ResStrategyMap> list = resStrategyMapService.findResStrategyMap(resStrategyMap);
		modelMap.put("list", list);
		return "{result: '" + result + "', desc : 'repeatName'}";
	}
	
}
