
package com.db.bms.controller.sysmgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.Album;
import com.db.bms.entity.AllocCommand;
import com.db.bms.entity.Article;
import com.db.bms.entity.Column;
import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.Role;
import com.db.bms.entity.Topic;
import com.db.bms.entity.Operator.OperatorStatus;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.AlbumService;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.SysRoleService;
import com.db.bms.service.TopicService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.EncryptData;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;


@RequestMapping("sysmgmt/user")
@Controller
public class UserController {

	private final static Logger logger = Logger.getLogger(UserController.class);
	
    @Autowired
    private OperatorService operatorService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysRoleService sysRoleService;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private ResourceAllocationService resourceAllocationService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private ColumnService columnService;
    
    @Autowired
    private ArticleService articleService;

    
    @RequestMapping(value = "/userList.action")
    public String findPageList(HttpServletRequest request, ModelMap modelMap, Operator search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        List<Operator> list = null;
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	list = this.operatorService.findOperatorsPage(search);
        	break;
        case COMPANY_ADMIN: 
        	search.setCreateBy(curOper.getOperatorNo());
        	list = this.operatorService.findOperatorsPage(search);
        	break;
        case ORDINARY_OPER:
        	search.setCreateBy(curOper.getOperatorNo());
        	list = this.operatorService.findOperatorsPage(search);
        	break;
        }

        List<Company> belongList = companyService.findAllCompanys(curOper);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("belongList", belongList);
        modelMap.put("search", search);
        modelMap.put("userTypeMap", ConstConfig.userTypeMap);
        return "/sysmgmt/user/userList";
    }

    @RequestMapping(value = "/userEdit.action")
    public String operatorEdit(HttpServletRequest request, ModelMap modelMap, Operator search, Long id) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        List<Company> belongList = companyService.findAllCompanys(curOper);
        Map<Integer, String> userTypeMap = new LinkedHashMap<Integer, String>(0);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	userTypeMap = ConstConfig.userTypeMap;
        	break;
        case COMPANY_ADMIN:
        	userTypeMap.put(OperatorType.ORDINARY_OPER.getIndex(), ConstConfig.userTypeMap.get(OperatorType.ORDINARY_OPER.getIndex()));
        	break;
        case ORDINARY_OPER:
        	userTypeMap = new LinkedHashMap<Integer, String>(0);
        	//userTypeMap.put(OperatorType.ORDINARY_OPER.getIndex(), ConstConfig.userTypeMap.get(OperatorType.ORDINARY_OPER.getIndex()));
        	break;
        }
        
        Operator  operator = new Operator();
        if (id != null && id > 0) {
        	EncryptData encryptData = new EncryptData();
            operator = this.operatorService.findOperatorById(id);
            String pwd = encryptData.decrypt(operator.getOperatorPwd());
            operator.setOperatorPwd(pwd);
        }

        modelMap.put("operator", operator);
        modelMap.put("userTypeMap", userTypeMap);
        modelMap.put("userStatusMap", ConstConfig.userStatusMap);
        modelMap.put("belongList", belongList);
        modelMap.put("search", search);
        return "/sysmgmt/user/userEdit";
    }
    
    @RequestMapping(value = "/userCheckId.action")
    @ResponseBody
    public String checkOperatorId(Operator operator) throws Exception {
        boolean result = false;
        Operator search = new Operator();
        search.setOperatorNo(operator.getOperatorNo());
        search.setOperatorId(operator.getOperatorId());
        result = this.operatorService.isOperatorRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/userCheckName.action")
    @ResponseBody
    public String checkOperatorName(Operator operator) throws Exception {
        boolean result = false;
        Operator search = new Operator();
        search.setOperatorNo(operator.getOperatorNo());
        search.setOperatorName(operator.getOperatorName());
        result = this.operatorService.isOperatorRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/saveOrUpdateUser.action", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdateUser(HttpServletRequest request, Operator operator, Long[] channelNo, Long[] cmpyNo)
            throws Exception { 
    	Operator curOper = SessionUtil.getActiveOperator(request);
        boolean result = false;
        String logStr = "";
        try {

/*        	switch (OperatorType.getType(operator.getType())){
        	case COMPANY_ADMIN:
            	List<Operator> list = operatorService.findAllOperators(operator);
            	if (list != null && list.size() > 0){
            		return "{result: '" + result + "', desc : 'onlyOneAdmin'}"; 
            	}
        		break;
        	}*/
        	
            if (operator.getOperatorNo() != null){
            	Operator oper = operatorService.findOperatorById(operator.getOperatorNo());
            	if (operator.getTotalSize() != null){
            		long totalSize = operator.getTotalSize() * 1024 * 1024 * 1024;
            		if (totalSize < oper.getUsedSize()){
            			return "{result: '" + result + "', desc : 'exceedUsedSpace'}"; 
            		}
            	}
            }
        	
        	if (operator.getCompanyNo() == null){
        		operator.setCompanyNo(curOper.getCompanyNo());
        	}
        	
            if (channelNo != null && channelNo.length > 0) {
                List<Role> roleList = this.sysRoleService.findRolesById(channelNo);
                operator.setRoles(roleList);
            }
            
            if (operator.getTotalSize() == null){
            	operator.setTotalSize(-1L);
            }
            
            EncryptData encryptData = new EncryptData();
            operator.setOperatorPwd(encryptData.encrypt(operator.getOperatorPwd()));
            if (operator.getOperatorNo() == null) {

                Operator curOoper = SessionUtil.getActiveOperator(request);
                if (curOoper != null) {
                    operator.setCreateBy(curOoper.getOperatorNo());
                }
                operator.setCreateTime(DateUtil.getCurrentTime());
            }
            
            this.operatorService.saveOrUpdateUser(operator,cmpyNo);
            logStr = "更新[" + operator.getOperatorId() + "]用户";
            if (operator.getOperatorNo() == null){
            	logStr = "添加[" + operator.getOperatorId() + "]用户";
            }
            result = true;
            logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
            if (operator.getOperatorNo() == null){
            	logger.error("Add user exception occurred, cause by:{}", e);
            }else{
            	logger.error("Update user exception occurred, cause by:{}", e);
            }
        	logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
            result = false;
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/userDetail.action")
    public String userDetail(HttpServletRequest request, ModelMap modelMap, Long operatorNo,String cmdStr) throws Exception {

        Operator operator = this.operatorService.findOperatorById(operatorNo);
        modelMap.put("operator", operator);
        modelMap.put("userTypeMap", ConstConfig.userTypeMap);
        List<AllocCommand> allocCommands = operator.getAllocCommands(cmdStr);
        modelMap.put("allocCommands", allocCommands);
        return "/sysmgmt/user/userDetail";
    }

    @RequestMapping(value = "/userDelete.action", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOperator(HttpServletRequest request, Long[] rtId) throws Exception {
        boolean result = false;
        String str = "";
        try {
            if (rtId != null && rtId.length > 0) {
            	List<Long> userNos = new ArrayList<Long>();
            	for (Long userNo : rtId){
            		if (userNo != null){
            			userNos.add(userNo);
            		}
            	}
                List<Operator> list = operatorService.findOperatorsById(userNos);
                for (int i = 0; i < list.size() - 1; i++) {
                    str += list.get(i).getOperatorId() + ",";
                    if (str.length() > 200) {
                        str += "...";
                        break;
                    }
                }
                str += list.get(list.size() - 1).getOperatorId();
                operatorService.deleteUsers(list);

            }
            result = true;
            logService.logToDB(request, "删除[" + str + "]用户", LogUtil.LOG_INFO, true, true);
        }
        catch (Exception e) {
        	logService.logToDB(request, "删除[" + str + "]用户", LogUtil.LOG_INFO, false, true);
            result = false;
            logger.error("Delete user exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : ''}";
    }

	@RequestMapping(value = "/resourceOperAllocList.action")
	public String resourceOperAllocList(HttpServletRequest request,
			ModelMap modelMap, Long createOperNo,Integer type, Long resourceId, Operator search)
			throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        List<Operator> list = null;
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	list = this.operatorService.findResourceAllocOperators(search, type, resourceId);
        	break;
        case COMPANY_ADMIN:
        case ORDINARY_OPER:
        	search.setCreateBy(curOper.getOperatorNo());
        	list = this.operatorService.findResourceAllocOperators(search, type, resourceId);
        	break;
        }
        
        //List<Company> belongList = companyService.findAllCompanys(curOper);
        
		modelMap.put("createOperNo", createOperNo);
		modelMap.put("type", type);
		modelMap.put("resourceId", resourceId);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        //modelMap.put("belongList", belongList);
        modelMap.put("search", search);
        modelMap.put("userTypeMap", ConstConfig.userTypeMap);
		return "/sysmgmt/user/resourceUserAllocList";
	}

	@RequestMapping(value = "/resourceOperNoAllocList.action")
	public String resourceOperNoAllocList(HttpServletRequest request,
			ModelMap modelMap, Long createOperNo,Integer type, Long resourceId, Operator search)
			throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        search.setOperatorNo(createOperNo);
        //modified by MiaoJun on 2016-05-10,just query company administrator
        search.setType(OperatorType.COMPANY_ADMIN.getIndex());
        search.setStatus(OperatorStatus.NORMAL.getIndex());
        Operator op = this.operatorService.findOperatorById(createOperNo);
        if(op != null){
        	search.setCreateBy(op.getCreateBy());
        }
        List<Operator> list = null;
        list = this.operatorService.findResourceNoAllocOperators(search, type, resourceId);
//        switch (OperatorType.getType(curOper.getType())){
//        case SUPER_ADMIN:
//        	list = this.operatorService.findResourceNoAllocOperators(search, type, resourceId);
//        	break;
//        case COMPANY_ADMIN: 
//        case ORDINARY_OPER:
//        	list = this.operatorService.findResourceNoAllocOperators(search, type, resourceId);
//        	break;
//        }

        List<Company> belongList = companyService.findAllCompanys(curOper);
        modelMap.put("createOperNo", createOperNo);
		modelMap.put("type", type);
		modelMap.put("resourceId", resourceId);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("belongList", belongList);
        modelMap.put("search", search);
        modelMap.put("userTypeMap", ConstConfig.userTypeMap);
        return "/sysmgmt/user/resourceUserNoAllocList";
	}

	@RequestMapping(value = "/saveResourceDistOper.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveResourceDistOper(HttpServletRequest request,
			Long[] operatorNos, Long resourceId, Integer type,String cmdStr) throws Exception {
		boolean result = false;
		String str = "";
		String desc = "";
		String logDesc = "";
		Operator curOper = SessionUtil.getActiveOperator(request);
		try {
			if (operatorNos != null && operatorNos.length > 0) {
				List<Operator> list = operatorService.findOperatorsById(Arrays
						.asList(operatorNos));
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getOperatorName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getOperatorName();
				
				switch (ResourceAllocation.ResourceType.getType(type)){
				case TOPIC:
					Topic topic = topicService.findTopicById(resourceId);
					logDesc = "分配 专题[" + topic.getTopicName()
					+ "]给[" + str + "]用户";
					break;
				case ALBUM:
					Album album = albumService.findAlbumById(resourceId);
					logDesc = "分配 相册[" + album.getAlbumName()
					+ "]给[" + str + "]用户";
					break;
				case COLUMN:
					Column column = columnService.selectByNo(resourceId);
					logDesc = "分配 版块[" + column.getColumnName()
					+ "]给[" + str + "]用户";
					break;
				case ARTICLE:
					Article article = articleService.selectByNo(resourceId);
					logDesc = "分配 文章[" + article.getArticleName()
					+ "]给[" + str + "]用户";
					break;
				}
				
				resourceAllocationService.addResourceAllocation(curOper, type, resourceId, operatorNos,cmdStr);

			}
			result = true;
			logService.logToDB(request, logDesc, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, logDesc, LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error(
					"Allocation of resources the operator exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc:'" + desc + "'}";
	}

	@RequestMapping(value = "/resourceAllocOperDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String resourceAllocOperDelete(HttpServletRequest request,
			Long[] operatorNos, Long resourceId, Integer type) throws Exception {
		boolean result = false;
		String str = "";
		String logDesc = "";
		Operator curOper = SessionUtil.getActiveOperator(request);
		try {
			if (operatorNos != null && operatorNos.length > 0) {
				List<Operator> list = operatorService.findOperatorsById(Arrays
						.asList(operatorNos));
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getOperatorName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getOperatorName();

				switch (ResourceAllocation.ResourceType.getType(type)){
				case TOPIC:
					Topic topic = topicService.findTopicById(resourceId);
					logDesc = "取消专题[" + topic.getTopicName()
					+ "]分配[" + str + "]用户";
					break;
				case ALBUM:
					Album album = albumService.findAlbumById(resourceId);
					logDesc = "取消 相册[" + album.getAlbumName()
					+ "]分配[" + str + "]用户";
					break;
				case COLUMN:
					Column column = columnService.selectByNo(resourceId);
					logDesc = "取消 版块[" + column.getColumnName()
					+ "]分配[" + str + "]用户";
					break;
				case ARTICLE:
					Article article = articleService.selectByNo(resourceId);
					logDesc = "取消 文章[" + article.getArticleName()
					+ "]分配[" + str + "]用户";
					break;
				}
				
				resourceAllocationService.deleteResourceAllocation(type, null, new Long[]{resourceId}, operatorNos);

			}
			result = true;
			logService.logToDB(request, logDesc, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, logDesc, LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error(
					"Cancel the allocation of resources the operator exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
    
}
