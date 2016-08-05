
package com.db.bms.controller.sysmgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.Command;
import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Role;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Role.RoleStatus;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.SysCommandService;
import com.db.bms.service.SysRoleService;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;


@RequestMapping("sysmgmt/role")
@Controller
public class RoleController {

	private final static Logger logger = Logger.getLogger(RoleController.class);
	
    @Autowired
    private OperatorService operatorService;
    
    @Autowired
    private SysRoleService sysRoleService;
    
    @Autowired
    private SysCommandService sysCommandService;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private CompanyService companyService;
    
    @RequestMapping(value = "/roleList.action")
    public String roleList(HttpServletRequest request, ModelMap modelMap, Role search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        List<Role> list = null;
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	list = this.sysRoleService.findRoles(search);
        	break;
        case COMPANY_ADMIN:
        case ORDINARY_OPER:
        	search.setCreateBy(curOper.getOperatorNo());
        	list = this.sysRoleService.findRoles(search);
        	break;
        }
        
        List<Company> belongList = companyService.findAllCompanys(curOper);
        request.setAttribute("belongList", belongList);
        request.setAttribute("list", list);
        modelMap.put("pageUtil", search.getPageUtil());//分页页面时要写这个
        modelMap.put("search", search);
        return "/sysmgmt/role/roleList";
    }
    
    @RequestMapping(value = "/roleEdit.action")
    public String roleEdit(HttpServletRequest request, ModelMap modelMap, Role search, Long id) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        List<Company> belongList = companyService.findAllCompanys(curOper);

        Role role = new Role();;
        if (id != null && id > 0) {
            role = this.sysRoleService.findRoleById(id);
        }
        modelMap.put("search", search);
        modelMap.put("role", role);
        modelMap.put("belongList", belongList);
        return "/sysmgmt/role/roleEdit";
    }
    
    @RequestMapping(value = "/roleCheckId.action")
    @ResponseBody
    public String checkRoleId(Role role) throws Exception {
        boolean result = false;
        Role search = new Role();
        search.setRoleNo(role.getRoleNo());
        search.setRoleId(role.getRoleId());
        result = this.sysRoleService.isRoleRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/roleCheckName.action", method = RequestMethod.POST)
    @ResponseBody
    public String checkName(Role role) throws Exception {
        boolean result = false;
        Role search = new Role();
        search.setRoleNo(role.getRoleNo());
        search.setRoleName(role.getRoleName());
        result = this.sysRoleService.isRoleRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }

    @RequestMapping(value = "/roleSave.action", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdateRole(HttpServletRequest request, Role role) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
        boolean result = false;
        String logStr = "";
        try{
            if (role != null) {

            	if (role.getCompanyNo() == null){
            		role.setCompanyNo(curOper.getCompanyNo());
            	}
            	
                if (role.getRoleNo() != null && role.getRoleNo() > 0) {
                    this.sysRoleService.updateByPrimaryKey(role);
                    logStr = "更新[" + role.getRoleId() + "]角色";
                }
                else {
                    if (curOper != null) {
                        role.setCreateBy(curOper.getOperatorNo());                        
                    }
                    
                    role.setCreateTime(DateUtil.getCurrentTime());
                    this.sysRoleService.addRole(role);
                    logStr = "添加[" + role.getRoleId() + "]角色";
                }
                result = true;
            }
            logService.logToDB(request, logStr, LogUtil.LOG_INFO, result, false);

        }catch (Exception e){
        	if (role.getRoleNo() != null && role.getRoleNo() > 0){
            	logger.error("Update role exception occurred, cause by:{}", e);
        	}else{
            	logger.error("Add role exception occurred, cause by:{}", e);
        	}

        	logService.logToDB(request, logStr, LogUtil.LOG_INFO, result, false);
        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/roleDetail.action")
    public String roleDetail(HttpServletRequest request, ModelMap modelMap, Long roleNo) throws Exception {

        Role role = this.sysRoleService.findRoleById(roleNo);
        modelMap.put("role", role);
        return "/sysmgmt/role/roleDetail";
    }

    @RequestMapping(value = "/permission.action")
    public String permission(HttpServletRequest request, ModelMap modelMap, Long id, Role search) throws Exception {
        Role role = this.sysRoleService.findRoleById(id);
        List<Command> commandList = this.sysCommandService.findCommandList(1l);
        modelMap.put("role", role);
        modelMap.put("search", search);
        modelMap.put("commandList", commandList);
        return "/sysmgmt/role/permission";
    }

    @RequestMapping(value = "/savePermission.action", method = RequestMethod.POST)
    @ResponseBody
    public String savePermission(HttpServletRequest request, Role role) throws Exception {
        boolean result = false;
        String roleId = "";
        try{
        	String[] commandNos = request.getParameterValues("commandNo");
            List<Command> commands = this.sysCommandService.getCommandsByCommandNos(commandNos);
            role = this.sysRoleService.findRoleById(role.getRoleNo());
            role.setCommands(commands);
            this.sysRoleService.updateRoleCommands(role);
            result = true;
            roleId = role.getRoleId() == null ? String.valueOf(role.getRoleNo()) : role.getRoleId();
            logService.logToDB(request, "分配[" + roleId + "]权限", LogUtil.LOG_INFO, result, false);

        }catch (Exception e){
        	result = false;
        	roleId = role.getRoleId() == null ? String.valueOf(role.getRoleNo()) : role.getRoleId();
        	logger.error("Distribution [" + roleId + "] permission exception occurred, cause by:{}", e);
        	logService.logToDB(request, "分配[" + roleId + "]权限", LogUtil.LOG_INFO, result, false);
        }
        return "{result: '" + result + "', desc : ''}";
    }

    @RequestMapping(value = "/roleDelete.action", method = RequestMethod.POST)
    @ResponseBody
    public String deleteRole(HttpServletRequest request, Long[] rtId) throws Exception {
        boolean result = false;
        String desc = "";
        StringBuffer str = new StringBuffer();
        try {
            if (rtId != null && rtId.length > 0) {
                int count = this.operatorService.findOperatorCountByRoleNo(rtId);
                if (count > 0) {
                    desc = "reference";
                    return "{result: '" + result + "', desc : '" + desc + "'}";
                }
                else {
                    List<Role> list = this.sysRoleService.findRolesById(rtId);
                    for (int i = 0; i < list.size() - 1; i++) {
                        str.append(list.get(i).getRoleName() + Delimiters.COMMA);
                        if (str.length() > 200) {
                        	str.append("...");
                            break;
                        }
                    }
                    str.append(list.get(list.size() - 1).getRoleName());
                    this.sysRoleService.deleteRoles(list);
                    result = true;
                    logService.logToDB(request, "删除[" + str + "]角色", LogUtil.LOG_INFO, result, false);
                }
            }
        }
        catch (Exception e) {
            result = false;
            logService.logToDB(request, "删除[" + str + "]角色", LogUtil.LOG_INFO, result, false);
            logger.error("Delete roles exception occurred, cause by:{}", e);
        }
        return "{result: '" + result + "', desc : '" + desc + "'}";
    }
    
    @RequestMapping(value = "/roleSelect.action")
    public String roleSelect(HttpServletRequest request, ModelMap modelMap, Role search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        search.setStatus(RoleStatus.NORMAL.getIndex());
        List<Role> list = null;
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	list = this.sysRoleService.findRoles(search);
        	break;
        case COMPANY_ADMIN:
        case ORDINARY_OPER:
        	search.setCreateBy(curOper.getOperatorNo());
        	list = this.sysRoleService.findRoles(search);
        	break;
        }
        
        List<Company> belongList = companyService.findAllCompanys(curOper);
        request.setAttribute("belongList", belongList);
        request.setAttribute("list", list);
        modelMap.put("pageUtil", search.getPageUtil());
        modelMap.put("search", search);
        return "/sysmgmt/role/roleSelect";
    }
    
}
