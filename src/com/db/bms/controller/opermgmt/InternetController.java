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

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Internet;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.InternetService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.SysRoleService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/internet")
@Controller
public class InternetController {
     
    private final static Logger logger = Logger.getLogger(InternetController.class);
	
    @Autowired
    private InternetService internetService;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private OperatorService operatorService;
    
    @Autowired
    private CardRegionService cardRegionService;
    
    @Autowired
    private SysRoleService sysRoleService;
    
	@Autowired
	private PortalProcessor processor;

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
        return "opermgmt/internet/internetList";
    }
    
    @RequestMapping(value = "/internetEdit.action")
    public String internetEdit(HttpServletRequest request, Long id, ModelMap modelMap, Internet search) throws Exception {
        try {
            Operator curOper = SessionUtil.getActiveOperator(request);
            List<Internet> belongList = internetService.findAllInternets(curOper);
            Internet internet = new Internet();
            if (id != null) {
            	internet = this.internetService.findInternetByNo(id);
            }
            modelMap.put("search", search);
            modelMap.put("internet", internet);
            modelMap.put("belongList", belongList);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "opermgmt/internet/internetEdit";
    }
    
    @RequestMapping(value = "/internetCheck.action")
    @ResponseBody
    public String checkInternetId(Internet internet) throws Exception {
        boolean result = false;
        Internet search = new Internet();
        search.setInternetNo(internet.getInternetNo());
        search.setInternetId(internet.getInternetId());
        result = this.internetService.isInternetRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/internetCheckName.action", method = RequestMethod.POST)
    @ResponseBody
    public String checkInternetName(Internet internet) throws Exception {
        boolean result = false;
        Internet search = new Internet();
        search.setInternetNo(internet.getInternetNo());
        search.setInternetName(internet.getInternetName());
        result = this.internetService.isInternetRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/internetSave.action", method = RequestMethod.POST)
    @ResponseBody
    public String internetSaveOrUpdate(HttpServletRequest request, Internet internet, String filePath) throws Exception {
    	boolean result = false;
        String desc = "";
        String logStr = ""; 
        try {
            if (internet.getInternetNo() != null) { 
                this.internetService.saveOrUpdate(internet); 
                logStr = "更新[" + internet.getInternetId() + "]网络";
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
                Internet entity = internetService.findInternetById(internet.getInternetId());
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(entity.getParentId());
				notice.setResourceId(entity.getInternetNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            else { 
            	Operator curOper = SessionUtil.getActiveOperator(request); 
            	internet.setCreateBy(curOper.getOperatorNo()); 
                this.internetService.saveOrUpdate(internet); 
                logStr = "添加[" + internet.getInternetId() + "]网络"; 
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(internet.getParentId());
				notice.setResourceId(internet.getInternetNo());
				noticeList.add(notice);
				
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
        }
        catch (Exception e) {
        	logger.error("Save or update internet exception occurred, cause by:{}", e);
        	logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, false);
            result = false;	
        }
        return "{result: '" + result + "', desc : '" + desc + "'}";
        
    }

    @RequestMapping(value = "/internetDelete.action")
    @ResponseBody
    public String internetDelete(HttpServletRequest request, Long[] rtId) throws Exception { 
        boolean result = false;
        String desc = "";  
        try { 
            if (rtId != null && rtId.length > 0) {  
            	List<Internet> list = internetService.findInternetsByNo(Arrays.asList(rtId));    
            	internetService.deleteInternets(list);   
            	result = true;
            } 
        }
        catch (Exception e) { 
            result = false; 
            logger.error("Delete internets exception occurred, cause by:{}", e); 
        }  
        return "{result: '" + result + "', desc : '" + desc + "'}"; 
    }
    
    @RequestMapping(value = "/internetDetail.action")
    public String internetDetail(HttpServletRequest request, ModelMap modelMap, Long internetNo) throws Exception {
        try {
        	Internet internet = internetService.findInternetByNo(internetNo);
            modelMap.put("internet", internet);
        }
        catch (Exception e) {
        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
        }
        return "opermgmt/internet/internetDetail";
    }
    
   
    @RequestMapping(value = "/internetSelect.action")
    public String internetSelectList(HttpServletRequest request, ModelMap modelMap, Internet search) throws Exception {
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
    	return "opermgmt/internet/internetSelect";
    }
   
    
    @RequestMapping(value = "/internetCardRegionSelect.action")
    public String internetCardRegionSelect(HttpServletRequest request, ModelMap modelMap, Long internetNo, CardRegion search) throws Exception {
    	
    	List<CardRegion> regionList = internetService.findInternetCardRegions(internetNo, search);
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
    	modelMap.put("internetNo", internetNo);
		modelMap.put("search", search);
		modelMap.put("pageUtil", search.getPageUtil());
    	modelMap.put("list", regionList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "/opermgmt/region/internetCardRegionSelect";
    }
    
    @RequestMapping(value = "/internetCardRegionNoSelect.action")
    public String internetCardRegionNoSelect(HttpServletRequest request, ModelMap modelMap, Long internetNo, CardRegion search) throws Exception {
    	
    	List<CardRegion> regionList = internetService.findInternetCardRegionsNoSelect(internetNo, search);;
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
    	modelMap.put("internetNo", internetNo);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
    	modelMap.put("list", regionList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
        return "/opermgmt/region/internetCardRegionNoSelect";
    }
    
    @RequestMapping(value = "/saveInternetCardRegion.action", method = RequestMethod.POST)
    @ResponseBody
    public String saveInternetCardRegion(HttpServletRequest request, Long[] regionIds, Long internetNo) throws Exception {
        boolean result = false;
        String str = "";
        Internet internet = null;
        try {
            if (regionIds != null && regionIds.length > 0) {
                List<CardRegion> list = cardRegionService.findRegionsById(regionIds);
                for (int i = 0; i < list.size() - 1; i++) {
                    str += list.get(i).getRegionName() + Delimiters.COMMA;
                    if (str.length() > 200) {
                        str += "...";
                        break;
                    }
                }
                str += list.get(list.size() - 1).getRegionName();
                
                internet = internetService.findInternetByNo(internetNo);
                internetService.addInternetCardRegionMap(internetNo, regionIds);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(internet.getParentId());
				notice.setResourceId(internet.getInternetNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, "运营商[" + internet.getInternetName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "运营商[" + internet.getInternetName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Internet associated smart card regions exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/internetCardRegionDelete.action", method = RequestMethod.POST)
    @ResponseBody
    public String internetCardRegionDelete(HttpServletRequest request, Long[] regionIds, Long internetNo) throws Exception {
        boolean result = false;
        String str = "";
        Internet internet = null;
        try {
            if (regionIds != null && regionIds.length > 0) {
                List<CardRegion> list = cardRegionService.findRegionsById(regionIds);
                for (int i = 0; i < list.size() - 1; i++) {
                    str += list.get(i).getRegionName() + Delimiters.COMMA;
                    if (str.length() > 200) {
                        str += "...";
                        break;
                    }
                }
                str += list.get(list.size() - 1).getRegionName();
                
                internet = internetService.findInternetByNo(internetNo);
                internetService.deleteInternetCardRegionMaps(internetNo, regionIds);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(internet.getParentId());
				notice.setResourceId(internet.getInternetNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, "取消运营商[" + internet.getInternetName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "取消运营商[" + internet.getInternetName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Cancel internet associated smart card regions exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/publishInternetSelect.action")
    public String publishInternetSelect(HttpServletRequest request, ModelMap modelMap, Internet search) throws Exception {
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
    	return "opermgmt/internet/publishInternetSelect";
    }
}
