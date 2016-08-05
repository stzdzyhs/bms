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
import com.db.bms.entity.Company;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.AlbumService;
import com.db.bms.service.ArticleService;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.StrategyConditionService;
import com.db.bms.service.SysRoleService;
import com.db.bms.service.TopicService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/company")
@Controller
public class CompanyController {

	private final static Logger logger = Logger.getLogger(CompanyController.class);
	
    @Autowired
    private CompanyService companyService;
    
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
	
	@Autowired
    private ArticleService articleService;
    
    @Autowired
    private ColumnService columnService;
    
    @Autowired
	private AlbumService albumService;
    
    @Autowired
	private TopicService topicService;
    
    @Autowired
    private StrategyConditionService strategyConditionService;

    @RequestMapping(value = "/companyList.action")
    public String companyList(HttpServletRequest request, ModelMap modelMap, Company search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        List<Company> belongList = null;
        List<Company> list = null;
        List<Long> distCompanyList = new ArrayList<Long>();
        switch (OperatorType.getType(curOper.getType())){
        case ORDINARY_OPER:
			List<Company> companyList = companyService.findDistCompanyByOperNo(curOper.getOperatorNo());
			if (companyList != null && companyList.size() > 0){
				for (Company cmpy : companyList){
					distCompanyList.add(cmpy.getCompanyNo());
				}
			}
        	break;
        }
        
        belongList =  companyService.findAllCompanys(distCompanyList);
    	list = this.companyService.findCompanys(search,distCompanyList);
        modelMap.put("belongList", belongList);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
        return "opermgmt/company/companyList";
    }
    
    @RequestMapping(value = "/companyEdit.action")
    public String companyEdit(HttpServletRequest request, Long id, ModelMap modelMap, Company search) throws Exception {
        try {
        	
            Operator curOper = SessionUtil.getActiveOperator(request);
            List<Company> belongList = companyService.findAllCompanys(curOper);
            Company company = new Company();
            if (id != null) {
                company = this.companyService.findCompanyByNo(id);
                if (company.getParentId() != -1){
                    boolean isExist = false;
                    Iterator<Company> it = belongList.iterator();
                    while (it.hasNext()){
                    	if (company.getParentId() == it.next().getCompanyNo()){
                    		isExist = true;
                    	}
                    }
                    
                    if (!isExist){
                    	belongList.add(company.getParent());
                    }
                }
            }
            modelMap.put("search", search);
            modelMap.put("company", company);
            modelMap.put("belongList", belongList);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "opermgmt/company/companyEdit";
    }
    
    @RequestMapping(value = "/companyCheck.action")
    @ResponseBody
    public String checkCompanyId(Company company) throws Exception {
        boolean result = false;
        Company search = new Company();
        search.setCompanyNo(company.getCompanyNo());
        search.setCompanyId(company.getCompanyId());
        result = this.companyService.isCompanyRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/companyCheckName.action", method = RequestMethod.POST)
    @ResponseBody
    public String checkCompanyName(Company company) throws Exception {
        boolean result = false;
        Company search = new Company();
        search.setCompanyNo(company.getCompanyNo());
        search.setCompanyName(company.getCompanyName());
        result = this.companyService.isCompanyRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/companySave.action", method = RequestMethod.POST)
    @ResponseBody
    public String companySaveOrUpdate(HttpServletRequest request, Company company, String filePath) throws Exception {
    	boolean result = false;
        String desc = "";
        String logStr = "";
        try {
            if (company.getCompanyNo() != null) {
                this.companyService.saveOrUpdate(company);
                logStr = "更新[" + company.getCompanyId() + "]运营商";
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
                Company entity = companyService.findCompanyById(company.getCompanyId());
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(entity.getParentId());
				notice.setResourceId(entity.getCompanyNo());
				noticeList.add(notice);
				
				this.processor.putNoticeToQueue(noticeList);
            }
            else {
            	Operator curOper = SessionUtil.getActiveOperator(request);
            	company.setCreateBy(curOper.getOperatorNo());
                this.companyService.saveOrUpdate(company);
                logStr = "添加[" + company.getCompanyId() + "]运营商";
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(company.getParentId());
				notice.setResourceId(company.getCompanyNo());
				noticeList.add(notice);
				
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
        }
        catch (Exception e) {
        	logger.error("Save or update company exception occurred, cause by:{}", e);
        	logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, false);
            result = false;
        }
        return "{result: '" + result + "', desc : '" + desc + "'}";
        
    }

    @RequestMapping(value = "/companyDelete.action")
    @ResponseBody
    public String companyDelete(HttpServletRequest request, Long[] rtId) throws Exception {
        boolean result = false;
        String desc = "";
        StringBuffer str = new StringBuffer();
        Operator curOper = SessionUtil.getActiveOperator(request);
        List<Company> childCompanys = null;
        try {
            if (rtId != null && rtId.length > 0) {
         
                int count = this.operatorService.getOperatorsByCompanyNo(rtId);
                if (count > 0) {
                    desc = "operator";
                    return "{result: '" + result + "', desc : '" + desc + "'}";
                }
                
                count = sysRoleService.getRolesByCompanyNo(rtId);
                if (count > 0){
                    desc = "role";
                    return "{result: '" + result + "', desc : '" + desc + "'}";
                }
                if(this.strategyConditionService.isRefStrategyCondition(new Long(StrategyCondition.TYPE_COMPANY), rtId)){
                	return "{result: '" + result + "', desc : 'reference'}";
                }
                for (int i = 0; i < rtId.length; i++) {
                	childCompanys = companyService.findCompanyByParentId(rtId[i]);
                	if(childCompanys != null && !childCompanys.isEmpty()){
                		desc = "child";
                        return "{result: '" + result + "', desc : '" + desc + "'}";                        
                	}
				}
                //检查专题、相册、版块、文章等表中是否有引用
                for (int i = 0; i < rtId.length; i++) {
                	count = this.articleService.getArticleCountByCompanyNo(rtId[i]);
                	if(count > 0){
                		desc = "article";
                        return "{result: '" + result + "', desc : '" + desc + "'}";                        
                	}
                	count = this.columnService.getColumnCountByCompanyNo(rtId[i]);
                	if(count > 0){
                		desc = "column";
                        return "{result: '" + result + "', desc : '" + desc + "'}";                        
                	}
                	count = this.albumService.getAlbumCountByCompanyNo(rtId[i]);
                	if(count > 0){
                		desc = "album";
                        return "{result: '" + result + "', desc : '" + desc + "'}";                        
                	}
                	count = this.topicService.getTopicCountByCompanyNo(rtId[i]);
                	if(count > 0){
                		desc = "topic";
                        return "{result: '" + result + "', desc : '" + desc + "'}";                        
                	}
				}
    
                List<Company> list = companyService.findCompanysByNo(Arrays.asList(rtId));
                for (int i = 0; i < list.size() - 1; i++) {
                	Company company = list.get(i);
                    str.append(company.getCompanyName() + Delimiters.COMMA);
                    if (str.length() > 200) {
                    	str.append("...");
                        break;
                    }
                }
                str.append(list.get(list.size() - 1).getCompanyName());
                
                List<Company> childList = companyService.findCompanysWithSubByNo(Arrays.asList(rtId));
                companyService.deleteCompanys(childList);
                result = true;
                logService.logToDB(request, "删除[" + str + "]运营商", LogUtil.LOG_INFO, result, false);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
                for (Company company : list){
    				PortalPublishNotice notice = new PortalPublishNotice();
    				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
    				notice.setResourceType(EntityType.TYPE_CARD_REGION);
    				notice.setParentId(company.getParentId());
    				notice.setResourceId(company.getCompanyNo());
    				noticeList.add(notice);
                }
				this.processor.putNoticeToQueue(noticeList);

            }
        }
        catch (Exception e) {
            result = false;
            logService.logToDB(request, "删除[" + str + "]运营商", LogUtil.LOG_ERROR, result, false);
            logger.error("Delete companys exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : '" + desc + "'}";
    }
    
    @RequestMapping(value = "/companyDetail.action")
    public String companyDetail(HttpServletRequest request, ModelMap modelMap, Long companyNo) throws Exception {
        try {
        	Company company = companyService.findCompanyByNo(companyNo);
            modelMap.put("company", company);
        }
        catch (Exception e) {
        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
        }
        return "opermgmt/company/companyDetail";
    }
    
    /*
     * 运营商选择
     */
    @RequestMapping(value = "/companySelect.action")
    public String companySelectList(HttpServletRequest request, ModelMap modelMap, Company search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        List<Company> belongList = null;
        belongList = companyService.findCompanyAndChildrenByNo(search.getCompanyNo(),search,null);
        modelMap.put("selectUrl", "/opermgmt/company/companySelect.action");
        modelMap.put("multiselect", true);
        modelMap.put("belongList", belongList);
        modelMap.put("list", belongList);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
    	return "opermgmt/company/companySelect";
    }
   
    
    @RequestMapping(value = "/companyCardRegionSelect.action")
    public String companyCardRegionSelect(HttpServletRequest request, ModelMap modelMap, Long companyNo, CardRegion search) throws Exception {
    	
    	List<CardRegion> regionList = companyService.findCompanyCardRegions(companyNo, search);
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
    	modelMap.put("companyNo", companyNo);
		modelMap.put("search", search);
		modelMap.put("pageUtil", search.getPageUtil());
    	modelMap.put("list", regionList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "/opermgmt/region/companyCardRegionSelect";
    }
    
    @RequestMapping(value = "/companyCardRegionNoSelect.action")
    public String companyCardRegionNoSelect(HttpServletRequest request, ModelMap modelMap, Long companyNo, CardRegion search) throws Exception {
    	
    	List<CardRegion> regionList = companyService.findCompanyCardRegionsNoSelect(companyNo, search);;
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
    	modelMap.put("companyNo", companyNo);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
    	modelMap.put("list", regionList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
        return "/opermgmt/region/companyCardRegionNoSelect";
    }
    
    @RequestMapping(value = "/saveCompanyCardRegion.action", method = RequestMethod.POST)
    @ResponseBody
    public String saveCompanyCardRegion(HttpServletRequest request, Long[] regionIds, Long companyNo) throws Exception {
        boolean result = false;
        String str = "";
        Company company = null;
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
                
                company = companyService.findCompanyByNo(companyNo);
                companyService.addCompanyCardRegionMap(companyNo, regionIds);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(company.getParentId());
				notice.setResourceId(company.getCompanyNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, "运营商[" + company.getCompanyName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "运营商[" + company.getCompanyName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Company associated smart card regions exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/companyCardRegionDelete.action", method = RequestMethod.POST)
    @ResponseBody
    public String companyCardRegionDelete(HttpServletRequest request, Long[] regionIds, Long companyNo) throws Exception {
        boolean result = false;
        String str = "";
        Company company = null;
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
                
                company = companyService.findCompanyByNo(companyNo);
                companyService.deleteCompanyCardRegionMaps(companyNo, regionIds);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(company.getParentId());
				notice.setResourceId(company.getCompanyNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, "取消运营商[" + company.getCompanyName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "取消运营商[" + company.getCompanyName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Cancel company associated smart card regions exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/publishCompanySelect.action")
    public String publishCompanySelect(HttpServletRequest request, ModelMap modelMap, Company search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        List<Company> belongList = null;
        List<Company> list = null;
        List<Long> distCompanyList = new ArrayList<Long>();
        switch (OperatorType.getType(curOper.getType())){
        case ORDINARY_OPER:
			List<Company> companyList = companyService.findDistCompanyByOperNo(curOper.getOperatorNo());
			if (companyList != null && companyList.size() > 0){
				for (Company cmpy : companyList){
					distCompanyList.add(cmpy.getCompanyNo());
				}
			}
        	break;
        }
        
        belongList =  companyService.findAllCompanys(distCompanyList);
    	list = this.companyService.findCompanys(search,distCompanyList);

        modelMap.put("belongList", belongList);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
    	return "opermgmt/company/publishCompanySelect";
    }
    
}
