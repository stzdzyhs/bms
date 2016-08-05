package com.db.bms.controller.opermgmt;


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

import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.PortalService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;
@RequestMapping("opermgmt/portal")
@Controller
public class PortalController {

	private final static Logger logger = Logger.getLogger(PortalController.class);
	
	@Autowired
	private LogService logService;
	
    @Autowired
    private PortalService portalService;
    
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;

	@RequestMapping(value = "/portalList.action")
	public String portalList(HttpServletRequest request, ModelMap modelMap, Portal search)
			throws Exception {

        Operator curOper = SessionUtil.getActiveOperator(request);
        
        List<Portal> list = portalService.findPortals(search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("portalStatusMap", ConstConfig.portalStatusMap);
		return "opermgmt/portal/portalList";
	}
	
    @RequestMapping(value = "/portalEdit.action")
    public String topicEdit(HttpServletRequest request, ModelMap modelMap, Portal search, Long sysNo) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	Portal portal = new Portal();
    	if (sysNo != null && sysNo > 0){
    		portal = portalService.findPortalById(sysNo);
    	}
    	List<Company> companyList = companyService.findAllCompanys(curOper);
    	modelMap.put("portal", portal);
    	modelMap.put("search", search);
    	modelMap.addAttribute("companyList", companyList);
    	return "opermgmt/portal/portalEdit";
    }
    
    @RequestMapping(value = "/checkData.action")
    @ResponseBody
    public String checkData(Portal portal) throws Exception {
        boolean result = false;
        Portal data = new Portal();
        data.setSysNo(portal.getSysNo());
        data.setSysId(portal.getSysId());
        data.setSysName(portal.getSysName());
        result = this.portalService.isRepeatPortal(data);
        return "{result: '" + !result + "', desc : ''}";
    }

	@RequestMapping(value = "/saveOrUpdatePortal.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdatePortal(HttpServletRequest request,
			HttpServletResponse response, Portal portal) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {
			
			if (portal.getCompanyNo() == null){
				portal.setCompanyNo(curOper.getCompanyNo());
			}
			
			if (portal.getSysNo() != null && portal.getSysNo() > 0) {
				logStr = "更新[" + portal.getSysName() + "]Portal";
				portal.setStatus(AuditStatus.DRAFT);
				portal.setUpdateTime(DateUtil.getCurrentTime());
				portalService.updatePortal(portal);
			} else {
				logStr = "添加[" + portal.getSysName() + "]Portal";
				portal.setStatus(AuditStatus.DRAFT);
				portal.setCreateTime(DateUtil.getCurrentTime());
				portal.setOperatorNo(curOper.getOperatorNo());
				portalService.addPortal(portal);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (portal.getSysNo() == null) {
				logger.error("Add portal system exception occurred, cause by:{}", e);
			} else {
				logger.error("Update portal system exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/portalDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String portalDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] sysNos) throws Exception {
		boolean result = false;
		String str = "";
		try {
			
			if (sysNos != null && sysNos.length > 0) {

				List<Portal> list = portalService.findPortalsById(sysNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getSysName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getSysName();
				portalService.deletePortalsById(sysNos);

			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]Portal", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]Portal", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete Portal system exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/portalDetail.action")
	public String topicDetail(HttpServletRequest request, ModelMap modelMap,
			Long sysNo) throws Exception {

		Portal portal = this.portalService.findPortalById(sysNo);
		modelMap.put("portal", portal);
		modelMap.put("portalStatusMap", ConstConfig.portalStatusMap);
		return "opermgmt/portal/portalDetail";
	}
	
	@RequestMapping(value = "/portalAudit.action", method = RequestMethod.POST)
	@ResponseBody
	public String portalAudit(HttpServletRequest request,
			Integer status, Long[] sysNos) throws Exception {
		boolean result = false;
		String str = "";
		try {
			if (sysNos != null && sysNos.length > 0) {
				List<Portal> list = portalService.findPortalsById(sysNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getSysName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getSysName();
				portalService.auditPortal(status, sysNos);

			}
			result = true;
			switch (PortalStatus.getStatus(status)) {
			case ENABLE:
				logService.logToDB(request, "Portal[" + str + "]启用",
						LogUtil.LOG_INFO, true, true);
				break;
			case DISABLE:
				logService.logToDB(request, "Portal[" + str + "]禁用",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (PortalStatus.getStatus(status)) {
			case ENABLE:
				logService.logToDB(request, "Portal[" + str + "]启用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Enable portal exception occurred, cause by:{}",
						e);
				break;
			case DISABLE:
				logService.logToDB(request, "Portal[" + str + "]禁用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Disable portal exception occurred, cause by:{}",
						e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}
}
