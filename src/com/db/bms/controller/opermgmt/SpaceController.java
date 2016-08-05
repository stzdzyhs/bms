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
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.Space;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.SpaceService;
import com.db.bms.service.StrategyConditionService;
import com.db.bms.service.SysRoleService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.Result2;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/space")
@Controller
public class SpaceController {
     
    private final static Logger logger = Logger.getLogger(SpaceController.class);
	
    @Autowired
    private SpaceService spaceService;
    
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
	private StrategyConditionService strategyConditionService;

    @RequestMapping(value = "/spaceList.action")
    public String spaceList(HttpServletRequest request, ModelMap modelMap, Space search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        List<Space> belongList = null;
        List<Space> list = null;        
        List<Long> distSpaceList = new ArrayList<Long>();

        /*
        SQL error
        switch (OperatorType.getType(curOper.getType())){
        case ORDINARY_OPER:
			List<Space> spaceList = spaceService.findDistSpaceByOperNo(curOper.getOperatorNo());
			if (spaceList != null && spaceList.size() > 0){
				for (Space cmpy : spaceList){
					distSpaceList.add(cmpy.getSpaceNo());
				}
			}
        	break;
        default:
        	break;
        }
        */
        
        belongList =  spaceService.findAllSpaces(distSpaceList);
    	list = this.spaceService.findSpaces(search,distSpaceList);
        modelMap.put("belongList", belongList);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
        return "opermgmt/space/spaceList";
    }
    
    @RequestMapping(value = "/spaceEdit.action")
    public String spaceEdit(HttpServletRequest request, Long id, ModelMap modelMap, Space search) throws Exception {
        try {
            //Operator curOper = SessionUtil.getActiveOperator(request);
            //List<Space> belongList = spaceService.findAllSpaces(curOper);
            Space space = new Space();
            if (id != null) {
            	space = this.spaceService.findSpaceByNo(id);
            }
            modelMap.put("search", search);
            modelMap.put("space", space);
            //modelMap.put("belongList", belongList);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "opermgmt/space/spaceEdit";
    }
    
    @RequestMapping(value = "/spaceCheck.action")
    @ResponseBody
    public String checkSpaceId(Space space) throws Exception {
        boolean result = false;
        Space search = new Space();
        search.setSpaceNo(space.getSpaceNo());
        search.setSpaceId(space.getSpaceId());
        result = this.spaceService.isSpaceRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/spaceCheckName.action", method = RequestMethod.POST)
    @ResponseBody
    public String checkSpaceName(Space space) throws Exception {
        boolean result = false;
        Space search = new Space();
        search.setSpaceNo(space.getSpaceNo());
        search.setSpaceName(space.getSpaceName());
        result = this.spaceService.isSpaceRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/spaceSave.action", method = RequestMethod.POST)
    @ResponseBody
    public String spaceSaveOrUpdate(HttpServletRequest request, Space space, String filePath) throws Exception {
    	boolean result = false;
        String desc = "";
        String logStr = ""; 
        try {
            if (space.getSpaceNo() != null) { 
                this.spaceService.saveOrUpdate(space); 
                logStr = "更新[" + space.getSpaceId() + "]网络";
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
                Space entity = spaceService.findSpaceById(space.getSpaceId());
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(entity.getParentId());
				notice.setResourceId(entity.getSpaceNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            else { 
            	Operator curOper = SessionUtil.getActiveOperator(request); 
            	space.setCreateBy(curOper.getOperatorNo()); 
                this.spaceService.saveOrUpdate(space); 
                logStr = "添加[" + space.getSpaceId() + "]网络"; 
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(space.getParentId());
				notice.setResourceId(space.getSpaceNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
        }
        catch (Exception e) {
        	logger.error("Save or update space exception occurred, cause by:{}", e);
        	logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, false);
            result = false;	
        }
        return "{result: '" + result + "', desc : '" + desc + "'}";
        
    }

    @RequestMapping(value = "/spaceDelete.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String spaceDelete(HttpServletRequest request, Long[] rtId) throws Exception { 
        Result2<Object> result = new Result2<Object>();
        try {
        	Operator curOper = SessionUtil.getActiveOperator(request);
            if (rtId != null && rtId.length > 0) {  
            	if(this.strategyConditionService.isRefStrategyCondition(new Long(StrategyCondition.TYPE_SPACE), rtId)){
            		throw new ResultCodeException(ResultCode.REF_ERROR, "reference");
            	}
            	List<Space> list = spaceService.findSpacesByNo(Arrays.asList(rtId));    
            	spaceService.deleteSpaces(curOper, list);   
            } 
        }
        catch (Exception e) {
        	ResultCodeException.convertException(result, e);
            logger.error("Delete spaces exception occurred, cause by:{}", e); 
        }
        String s = result.toString();
        return s; 
    }
    
    @RequestMapping(value = "/spaceDetail.action")
    public String spaceDetail(HttpServletRequest request, ModelMap modelMap, Long spaceNo) throws Exception {
        try {
        	Space space = spaceService.findSpaceByNo(spaceNo);
            modelMap.put("space", space);
        }
        catch (Exception e) {
        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
        }
        return "opermgmt/space/spaceDetail";
    }
    
   
    @RequestMapping(value = "/spaceSelect.action")
    public String spaceSelectList(HttpServletRequest request, ModelMap modelMap, Space search) throws Exception {
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
        default:
        	break;
        }
        
        belongList =  spaceService.findAllSpaces(distSpaceList);
    	list = this.spaceService.findSpaces(search,distSpaceList);

        modelMap.put("belongList", belongList);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
    	return "opermgmt/space/spaceSelect";
    }
   
    
    @RequestMapping(value = "/spaceCardRegionSelect.action")
    public String spaceCardRegionSelect(HttpServletRequest request, ModelMap modelMap, Long spaceNo, CardRegion search) throws Exception {
    	
    	List<CardRegion> regionList = spaceService.findSpaceCardRegions(spaceNo, search);
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
    	modelMap.put("spaceNo", spaceNo);
		modelMap.put("search", search);
		modelMap.put("pageUtil", search.getPageUtil());
    	modelMap.put("list", regionList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
		return "/opermgmt/region/spaceCardRegionSelect";
    }
    
    @RequestMapping(value = "/spaceCardRegionNoSelect.action")
    public String spaceCardRegionNoSelect(HttpServletRequest request, ModelMap modelMap, Long spaceNo, CardRegion search) throws Exception {
    	
    	List<CardRegion> regionList = spaceService.findSpaceCardRegionsNoSelect(spaceNo, search);;
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
    	modelMap.put("spaceNo", spaceNo);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
    	modelMap.put("list", regionList);
		modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
		modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
        return "/opermgmt/region/spaceCardRegionNoSelect";
    }
    
    @RequestMapping(value = "/saveSpaceCardRegion.action", method = RequestMethod.POST)
    @ResponseBody
    public String saveSpaceCardRegion(HttpServletRequest request, Long[] regionIds, Long spaceNo) throws Exception {
        boolean result = false;
        String str = "";
        Space space = null;
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
                
                space = spaceService.findSpaceByNo(spaceNo);
                spaceService.addSpaceCardRegionMap(spaceNo, regionIds);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(space.getParentId());
				notice.setResourceId(space.getSpaceNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, "运营商[" + space.getSpaceName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "运营商[" + space.getSpaceName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Space associated smart card regions exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/spaceCardRegionDelete.action", method = RequestMethod.POST)
    @ResponseBody
    public String spaceCardRegionDelete(HttpServletRequest request, Long[] regionIds, Long spaceNo) throws Exception {
        boolean result = false;
        String str = "";
        Space space = null;
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
                
                space = spaceService.findSpaceByNo(spaceNo);
                spaceService.deleteSpaceCardRegionMaps(spaceNo, regionIds);
                
                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_CARD_REGION);
				notice.setParentId(space.getParentId());
				notice.setResourceId(space.getSpaceNo());
				noticeList.add(notice);
				this.processor.putNoticeToQueue(noticeList);
            }
            result = true;
            logService.logToDB(request, "取消运营商[" + space.getSpaceName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "取消运营商[" + space.getSpaceName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Cancel space associated smart card regions exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/publishSpaceSelect.action")
    public String publishSpaceSelect(HttpServletRequest request, ModelMap modelMap, Space search) throws Exception {
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
        default:
        	break;
        }
        
        belongList =  spaceService.findAllSpaces(distSpaceList);
    	list = this.spaceService.findSpaces(search,distSpaceList);

        modelMap.put("belongList", belongList);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
    	return "opermgmt/space/publishSpaceSelect";
    }
}
