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
import com.db.bms.entity.Client;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.CardRegionService;
import com.db.bms.service.ClientService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.StrategyConditionService;
import com.db.bms.service.SysRoleService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.sync.portal.protocal.PublishNoticeREQT;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;
@RequestMapping("opermgmt/client")
@Controller
public class ClientController {

	 private final static Logger logger = Logger.getLogger(ClientController.class);
		
	    @Autowired
	    private ClientService clientService;
	    
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

	    @RequestMapping(value = "/clientList.action")
	    public String clientList(HttpServletRequest request, ModelMap modelMap, Client search) throws Exception {
	        Operator curOper = SessionUtil.getActiveOperator(request);
	        search.setCurOper(curOper);
	        search.getPageUtil().setPaging(true);
	        List<Client> belongList = null;
	        List<Client> list = null;
	        List<Long> distClientList = new ArrayList<Long>();
	        switch (OperatorType.getType(curOper.getType())){
	        case ORDINARY_OPER:
				List<Client> clientList = clientService.findDistClientByOperNo(curOper.getOperatorNo());
				if (clientList != null && clientList.size() > 0){
					for (Client client : clientList){
						distClientList.add(client.getClientNo());
					}
				}
	        	break;
	        }
	        
	        belongList =  clientService.findAllClients(distClientList);
	    	list = this.clientService.findClients(search,distClientList);
	        modelMap.put("belongList", belongList);
	        modelMap.put("list", list);
	        modelMap.put("pageUtil", search.getPageUtil());
	        modelMap.put("search", search);
	        return "opermgmt/client/clientList";
	    }
	    
	    @RequestMapping(value = "/clientEdit.action")
	    public String clientEdit(HttpServletRequest request, Long id, ModelMap modelMap, Client search) throws Exception {
	        try {
	            Operator curOper = SessionUtil.getActiveOperator(request);
	            List<Client> belongList = clientService.findAllClients(curOper);
	            Client client = new Client();
	            if (id != null) {
	            	client = this.clientService.findClientByNo(id);
	            }
	            modelMap.put("search", search);
	            modelMap.put("client", client);
	            modelMap.put("belongList", belongList);
	        }
	        catch (Exception e) {
	        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
	        }
	        return "opermgmt/client/clientEdit";
	    }
	    
	    @RequestMapping(value = "/clientCheck.action")
	    @ResponseBody
	    public String checkClientId(Client client) throws Exception {
	        boolean result = false;
	        Client search = new Client();
	        search.setClientNo(client.getClientNo());
	        search.setClientId(client.getClientId());
	        result = this.clientService.isClientRepeateIdOrName(search);
	        return "{result: '" + !result + "', desc : ''}";
	    }

	    @RequestMapping(value = "/clientCheckName.action", method = RequestMethod.POST)
	    @ResponseBody
	    public String checkClientName(Client client) throws Exception {
	        boolean result = false;
	        Client search = new Client();
	        search.setClientNo(client.getClientNo());
	        search.setClientName(client.getClientName());
	        result = this.clientService.isClientRepeateIdOrName(search);
	        return "{result: '" + !result + "', desc : ''}";
	    }

	    @RequestMapping(value = "/clientSave.action", method = RequestMethod.POST)
	    @ResponseBody
	    public String clientSaveOrUpdate(HttpServletRequest request, Client client, String filePath) throws Exception {
	    	boolean result = false;
	        String desc = "";
	        String logStr = ""; 
	        try {
	            if (client.getClientNo() != null) { 
	                this.clientService.saveOrUpdate(client); 
	                logStr = "更新[" + client.getClientId() + "]卡号";
	                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
	                Client entity = clientService.findClientById(client.getClientId());
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
					notice.setResourceType(EntityType.TYPE_CARD_REGION);
					notice.setParentId(entity.getParentId());
					notice.setResourceId(entity.getClientNo());
					noticeList.add(notice);
					this.processor.putNoticeToQueue(noticeList);
	            }
	            else { 
	            	Operator curOper = SessionUtil.getActiveOperator(request); 
	            	client.setCreateBy(curOper.getOperatorNo()); 
	                this.clientService.saveOrUpdate(client); 
	                logStr = "添加[" + client.getClientId() + "]卡号"; 
	                
	                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
					notice.setResourceType(EntityType.TYPE_CARD_REGION);
					notice.setParentId(client.getParentId());
					notice.setResourceId(client.getClientNo());
					noticeList.add(notice);
					
					this.processor.putNoticeToQueue(noticeList);
	            }
	            result = true;
	            logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
	        }
	        catch (Exception e) {
	        	logger.error("Save or update client exception occurred, cause by:{}", e);
	        	logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, false);
	            result = false;	
	        }
	        return "{result: '" + result + "', desc : '" + desc + "'}";
	        
	    }

	    @RequestMapping(value = "/clientDelete.action")
	    @ResponseBody
	    public String clientDelete(HttpServletRequest request, Long[] rtId) throws Exception { 
	        boolean result = false;
	        String desc = "";  
	        try {
	            if (rtId != null && rtId.length > 0) {  
	            	if(this.strategyConditionService.isRefStrategyCondition(new Long(StrategyCondition.TYPE_CLIENT), rtId)){
	            		return "{result: '" + result + "', desc : 'reference'}";
	            	}
	            	List<Client> list = clientService.findClientsByNo(Arrays.asList(rtId));    
	            	clientService.deleteClients(list);   
	            	result = true;
	            } 
	        }
	        catch (Exception e) { 
	            result = false; 
	            logger.error("Delete clients exception occurred, cause by:{}", e); 
	        }  
	        return "{result: '" + result + "', desc : '" + desc + "'}"; 
	    }
	    
	    @RequestMapping(value = "/clientDetail.action")
	    public String clientDetail(HttpServletRequest request, ModelMap modelMap, Long clientNo) throws Exception {
	        try {
	        	Client client = clientService.findClientByNo(clientNo);
	            modelMap.put("client", client);
	        }
	        catch (Exception e) {
	        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
	        }
	        return "opermgmt/client/clientDetail";
	    }
	    
	   
	    @RequestMapping(value = "/clientSelect.action")
	    public String clientSelectList(HttpServletRequest request, ModelMap modelMap, Client search) throws Exception {
	        Operator curOper = SessionUtil.getActiveOperator(request);
	        search.setCurOper(curOper);
	        List<Client> belongList = null;
	        List<Client> list = null;
	        List<Long> distClientList = new ArrayList<Long>();
	        switch (OperatorType.getType(curOper.getType())){
	        case ORDINARY_OPER:
				List<Client> clientList = clientService.findDistClientByOperNo(curOper.getOperatorNo());
				if (clientList != null && clientList.size() > 0){
					for (Client client : clientList){
						distClientList.add(client.getClientNo());
					}
				}
	        	break;
	        }
	        
	        belongList =  clientService.findAllClients(distClientList);
	    	list = this.clientService.findClients(search,distClientList);

	        modelMap.put("belongList", belongList);
	        modelMap.put("list", list);
	        modelMap.put("pageUtil", search.getPageUtil());
	        modelMap.put("search", search);
	    	return "opermgmt/client/clientSelect";
	    }
	   
	    
	    @RequestMapping(value = "/clientCardRegionSelect.action")
	    public String clientCardRegionSelect(HttpServletRequest request, ModelMap modelMap, Long clientNo, CardRegion search) throws Exception {
	    	
	    	List<CardRegion> regionList = clientService.findClientCardRegions(clientNo, search);
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
	    	modelMap.put("clientNo", clientNo);
			modelMap.put("search", search);
			modelMap.put("pageUtil", search.getPageUtil());
	    	modelMap.put("list", regionList);
			modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
			modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
			return "/opermgmt/region/clientCardRegionSelect";
	    }
	    
	    @RequestMapping(value = "/clientCardRegionNoSelect.action")
	    public String clientCardRegionNoSelect(HttpServletRequest request, ModelMap modelMap, Long clientNo, CardRegion search) throws Exception {
	    	
	    	List<CardRegion> regionList = clientService.findClientCardRegionsNoSelect(clientNo, search);;
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
	    	modelMap.put("clientNo", clientNo);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
	    	modelMap.put("list", regionList);
			modelMap.addAttribute("cardRegionTypeMap", ConstConfig.cardRegionTypeMap);
			modelMap.addAttribute("cardRegionCodeTypeMap", ConstConfig.cardRegionCodeTypeMap);
	        return "/opermgmt/region/clientCardRegionNoSelect";
	    }
	    
	    @RequestMapping(value = "/saveClientCardRegion.action", method = RequestMethod.POST)
	    @ResponseBody
	    public String saveClientCardRegion(HttpServletRequest request, Long[] regionIds, Long clientNo) throws Exception {
	        boolean result = false;
	        String str = "";
	        Client client = null;
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
	                
	                client = clientService.findClientByNo(clientNo);
	                clientService.addClientCardRegionMap(clientNo, regionIds);
	                
	                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
					notice.setResourceType(EntityType.TYPE_CARD_REGION);
					notice.setParentId(client.getParentId());
					notice.setResourceId(client.getClientNo());
					noticeList.add(notice);
					this.processor.putNoticeToQueue(noticeList);
	            }
	            result = true;
	            logService.logToDB(request, "运营商[" + client.getClientName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, true, true);
	        }
	        catch (Exception e) {
	        	logService.logToDB(request, "运营商[" + client.getClientName() + "]关联[" + str + "]智能卡区域", LogUtil.LOG_INFO, false, true);
	            result = false;
	            logger.error("Client associated smart card regions exception occurred, cause by:{}", e);
	        }
	        return "{result: '" + result + "', desc : ''}";
	    }
	    
	    @RequestMapping(value = "/clientCardRegionDelete.action", method = RequestMethod.POST)
	    @ResponseBody
	    public String clientCardRegionDelete(HttpServletRequest request, Long[] regionIds, Long clientNo) throws Exception {
	        boolean result = false;
	        String str = "";
	        Client client = null;
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
	                
	                client = clientService.findClientByNo(clientNo);
	                clientService.deleteClientCardRegionMaps(clientNo, regionIds);
	                
	                List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
					notice.setResourceType(EntityType.TYPE_CARD_REGION);
					notice.setParentId(client.getParentId());
					notice.setResourceId(client.getClientNo());
					noticeList.add(notice);
					this.processor.putNoticeToQueue(noticeList);
	            }
	            result = true;
	            logService.logToDB(request, "取消运营商[" + client.getClientName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, true, true);
	        }
	        catch (Exception e) {
	        	logService.logToDB(request, "取消运营商[" + client.getClientName() + "]关联智能卡区域[" + str + "]", LogUtil.LOG_INFO, false, true);
	            result = false;
	            logger.error("Cancel client associated smart card regions exception occurred, cause by:{}", e);
	        }
	        return "{result: '" + result + "', desc : ''}";
	    }
	    
	    @RequestMapping(value = "/publishClientSelect.action")
	    public String publishClientSelect(HttpServletRequest request, ModelMap modelMap, Client search) throws Exception {
	        Operator curOper = SessionUtil.getActiveOperator(request);
	        search.setCurOper(curOper);
	        List<Client> belongList = null;
	        List<Client> list = null;
	        List<Long> distClientList = new ArrayList<Long>();
	        switch (OperatorType.getType(curOper.getType())){
	        case ORDINARY_OPER:
				List<Client> clientList = clientService.findDistClientByOperNo(curOper.getOperatorNo());
				if (clientList != null && clientList.size() > 0){
					for (Client client : clientList){
						distClientList.add(client.getClientNo());
					}
				}
	        	break;
	        }
	        
	        belongList =  clientService.findAllClients(distClientList);
	    	list = this.clientService.findClients(search,distClientList);

	        modelMap.put("belongList", belongList);
	        modelMap.put("list", list);
	        modelMap.put("pageUtil", search.getPageUtil());
	        modelMap.put("search", search);
	    	return "opermgmt/client/publishClientSelect";
	    }
}
