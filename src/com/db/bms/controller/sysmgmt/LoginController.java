package com.db.bms.controller.sysmgmt;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.db.bms.entity.Command;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Role;
import com.db.bms.entity.Command.CommandStatus;
import com.db.bms.entity.Operator.OperatorStatus;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Role.RoleStatus;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.SysCommandService;
import com.db.bms.sync.portal.server.PortalServer;
import com.db.bms.utils.EncryptData;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.HardwareUtil;
import com.db.bms.utils.HtmlUtil;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.PjAuthorizeUtil;
import com.db.bms.utils.spring.SessionUtil;

@Controller
public class LoginController {

    //private static final Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private SysCommandService sysCommandService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private LogService logService;

    /**
     * {用户登陆}
     * @return json
     * @throws Exception
     * @author: wanghaotao
     */
    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response, Operator operator) throws Exception {
        boolean result = false;
        String desc = "";
        if (operator != null && operator.getOperatorId() != null) {
            EncryptData encryptData = new EncryptData();
            operator.setOperatorPwd(encryptData.encrypt(operator.getOperatorPwd()));
            List<Operator> operatorList = this.operatorService.findOperatorsWithRole(operator);
            if (operatorList != null && operatorList.size() > 0) {
                operator = operatorList.get(0);
                if (operator != null) {

                    List<Command> commandList = new ArrayList<Command>(0);
                    Map<String, List<String>> moduleMap = new HashMap<String, List<String>>(0);
                    if (operator.getOperatorNo() != null) {
                        // 是否有效
                        if (OperatorStatus.getStatus(operator.getStatus()) == OperatorStatus.SUSPEND) {
                            result = false;
                            desc = "isSuspend";
                        }
                        else {
                            // 是否为超级管理员
                        	if (OperatorType.getType(operator.getType()) == OperatorType.SUPER_ADMIN) {
                                // 获取全部权限
                                commandList = this.sysCommandService.findCommandList(null);
                            }
                            else {
                                // 获取分配的权限
                                Map<Long, String> commandMap = new HashMap<Long, String>(0);
                                for (int i = 0; i < operator.getRoles().size(); i++) {
                                    Role role = operator.getRoles().get(i);
                                    //                                    System.out.println(role);
                                    if (RoleStatus.getSource(role.getStatus()) != RoleStatus.SUSPEND) {
                                        //获取角色权限
                                        for (int j = 0; j < role.getCommands().size(); j++) {
                                            Command command = role.getCommands().get(j);
                                            //对status为-2的权限过滤掉
                                            if (CommandStatus.getSource(command.getStatus()) == CommandStatus.SUSPEND 
                                            		|| commandMap.containsKey(command.getCommandNo())) {
                                                continue;
                                            }
                                            
                                            commandList.add(command);
                                            commandMap.put(command.getCommandNo(), command.getCommandId());
                                        }
                                    }
                                }
                            }
                            // 重新组装权限操作
                            if (commandList != null && commandList.size() > 0) {
                                for (int i = 0; i < commandList.size(); i++) {
                                    if (commandList.get(i).getCommandLevel().longValue() == 3 || commandList.get(i).getCommandLevel().longValue() == 2) {
                                        List<String> funList = new ArrayList<String>(0);
                                        for (int j = 0; j < commandList.size(); j++) {
                                            if (commandList.get(j).getParentId() != null
                                                    && commandList.get(j).getParentId().longValue() == commandList.get(
                                                            i).getCommandNo().longValue()) {
                                                funList.add(commandList.get(j).getModuleName());
                                            }
                                        }
                                        moduleMap.put(commandList.get(i).getModuleName(), funList);
                                    }
                                }
                            }
                            result = true;
                        }
                    }
                    else {
                        result = true;
                    }
                    // 登录成功
                    if (result) {
                        SessionUtil.setAttr(request, "activeList", commandList);
                        SessionUtil.setAttr(request, "moduleMap", moduleMap);
                        SessionUtil.setAttr(request, "activeOperator", operator);
                    }
                }
            }
            else {
                result = false;
            }
        }
        else {
            result = false;
        }
        logService.logToDB(request, "用户"
                + (operator.getOperatorId() == null ? "" : "[" + operator.getOperatorId() + "]") + "登陆",
                LogUtil.LOG_LOGIN, result, true);
        String responseText = "{result: '" + result + "', desc : '" + desc + "'}";
        return responseText;
    }

    @RequestMapping("/main.action")
    public String main(HttpServletRequest request, ModelMap modelMap) throws Exception {
        List<Command> list = this.sysCommandService.findCommandList(1l);
        modelMap.put("menuList", list);
        return "/main";
    }

    @RequestMapping("/logout.action")
    public String logout(HttpServletRequest request) throws Exception {
        if (SessionUtil.getAttr(request, "activeOperator") != null) {
            SessionUtil.removeAttr(request, "activeOperator");
            SessionUtil.removeAttr(request, "activeList");
            SessionUtil.removeAttr(request, "moduleMap");
        }
        return "/login";
    }
    
    @RequestMapping("/toChangePassword.action")
    public String toChangePassword(HttpServletRequest request, ModelMap modelMap) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
    	//如果普通用户，则获取其管理者的空间信息
    	Operator mgOp = null;
		if(curOper.getType() == 2){
			mgOp = this.operatorService.findOperator(curOper.getCreateBy());
		}
		else{
			mgOp = curOper;
		}
		operator.setTotalSize(mgOp.getTotalSize());
		operator.setUsedSize(mgOp.getUsedSize());
    	modelMap.put("operator", operator);
        return "/changePassword";
    }
    
    @RequestMapping(value = "/changePwd.action", method = RequestMethod.POST)
    @ResponseBody
    public String changePwd(HttpServletRequest request, HttpServletResponse response, Operator operator, String oldPassword, String newPassword) throws Exception {
        boolean result = false;
        String desc = "";
        if (operator != null){
        	Operator oldOperator = operatorService.findOperatorById(operator.getOperatorNo());
        	EncryptData encryptData = new EncryptData();
        	if (oldPassword.equals(encryptData.decrypt(oldOperator.getOperatorPwd()))){
        		operator.setOperatorPwd(encryptData.encrypt(newPassword));
        		operatorService.updatePwd(operator);
        		result = true;
        	}
        }
        
        logService.logToDB(request, "用户"
                + (operator.getOperatorId() == null ? "" : "[" + operator.getOperatorId() + "]") + "修改密码",
                LogUtil.LOG_INFO, result, true);
        
        String responseText = "{result: '" + result + "', desc : '" + desc + "'}";
        return responseText;
    }
    
    
    @RequestMapping(value = "/aboutInfo.action")
    public String authInfo(ModelMap modelMap) {
        modelMap.put("authLimitDate", PjAuthorizeUtil.limitDateStr.isEmpty() ? "无" : PjAuthorizeUtil.limitDateStr);
        modelMap.put("authCompanyNum", String.valueOf(PjAuthorizeUtil.companyNum));
        modelMap.put("version", PortalServer.getVer());
        return "/aboutInfo";
    }

    /**
     * {更新授权信息}
     */
    @RequestMapping(value = "/toUpdatePjAuth.action")
    public String toUpdatePjAuth() {
        return "/updatePjAuth";
    }

    /**
     * 生成注册文件
     */
    @RequestMapping(value = "/createRegistFile.action")
    public void createRegistFile(HttpServletResponse response) {
        String mac = HardwareUtil.getMac();
        EncryptData enc = new EncryptData();
        String registInfo = enc.encrypt("db" + mac);
        HtmlUtil.outPutFile("regist.dat", registInfo, response);
    }

    /**
     * 上传授权
     */
    @RequestMapping(value = "/uploadAuthFile.action")
    public void uploadAuthFile(HttpServletRequest request,HttpServletResponse response, @RequestParam(value = "Filedata", required = false)
    MultipartFile file) {
        boolean isok = false;
        String desc = "上传文件失败";
        String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        String authFileName = PjAuthorizeUtil.authFileName;
        String relatePath = "files/upload/authInfo/";
        try {
            boolean getfile = FileUpload.saveFile(file, rootPath + relatePath, authFileName);
            if (getfile) {
                String content = FileUtils.readFileContent(rootPath + relatePath + authFileName);
                if (content != null && !content.isEmpty()) {
                    EncryptData enc = new EncryptData();
                    String authInfo = enc.decrypt(content);
                    String mac = HardwareUtil.getMac();
                    if (authInfo.startsWith("db" + mac)) {
                        String[] array = authInfo.split(";");
                        if (array != null && array.length == 4) {
                            try {
                                Integer.parseInt(array[2]);
                                isok = true;
                                FileUtils.copyFile(rootPath + relatePath + authFileName, rootPath + "WEB-INF/classes/",
                                        authFileName);
                                PjAuthorizeUtil.initAuthInfo();
                                desc = "授权已更新，更新后<br/>授权到期日期：" + PjAuthorizeUtil.limitDateStr + "<br/>授权运营商数量："
                                        + PjAuthorizeUtil.companyNum;
                                logService.logToDB(request, "更新授权，到期日期：" + PjAuthorizeUtil.limitDateStr + "，授权运营商数量："
                                        + PjAuthorizeUtil.companyNum, LogUtil.LOG_INFO, true, false);
                            }
                            catch (NumberFormatException e) {
                                isok = false;
                                desc = "无效的文件";
                            }
                        }
                        else {
                            desc = "无效的文件";
                        }
                    }
                    else {
                        desc = "无效的文件";
                    }
                }
                else {
                    desc = "无效的文件";
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            isok = false;
            desc = "上传文件失败";
        }
        finally {
            File tempfile = new File(rootPath + relatePath + authFileName);
            if (tempfile != null && tempfile.exists()) {
                tempfile.delete();
            }
        }
        outPrint("{result: " + isok + ", desc:'" + desc + "'}", response);
    }
    
    @RequestMapping(value = "/unauthorizedAsyn.action")
    public void unauthorizedAsyn (HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) {
		String responseText = "{result: '" + false + "', authText : '" + PjAuthorizeUtil.overdueTip
		+ "'}";
		outPrint(responseText, response);
    }
    
    @RequestMapping(value = "/unauthorized.action")
    public String unauthorized (HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) {
    	modelMap.put("desc", PjAuthorizeUtil.overdueTip);
    	return "common/tip";
    }
    
    @RequestMapping(value = "/welcome.action")
    public String welcome() {
        return "/welcome";
    }
    
    @RequestMapping(value="/containWordstock.action")
    public void containWordstock(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) {
    	String responseText = "{result: '" + false + "', desc : '包含生僻字'}";
    	outPrint(responseText, response);
    }

	protected void outPrint(String responseText, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			byte[] rt = responseText.getBytes("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentLength(rt.length);
			out = response.getWriter();
			out.print(new String(rt, "UTF-8"));
			out.flush();
		} catch (Exception e) {
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
