package com.db.bms.controller.opermgmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.Album;
import com.db.bms.entity.Company;
import com.db.bms.entity.FileInfo;
import com.db.bms.entity.FtpServerInfo;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.AlbumService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.FtpServerInfoService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FtpProcessor;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/ftp")
@Controller
public class FtpServerInfoController {

	private final static Logger logger = Logger
	.getLogger(FtpServerInfoController.class);
	
	@Autowired
	private FtpServerInfoService ftpServerInfoService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private AlbumService albumService;
	
	@RequestMapping(value = "/ftpServerList.action")
	public String ftpServerList(HttpServletRequest request, ModelMap modelMap, FtpServerInfo search)
			throws Exception {

        Operator curOper = SessionUtil.getActiveOperator(request);
        List<FtpServerInfo> list = null;
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	list = this.ftpServerInfoService.findFtpServers(search);
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	list = this.ftpServerInfoService.findFtpServers(search);
        	break;
        case ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	list = this.ftpServerInfoService.findFtpServers(search);
        	break;
        }

        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		return "opermgmt/ftp/ftpServerList";
	}
	
    @RequestMapping(value = "/ftpServerEdit.action")
    public String ftpServerEdit(HttpServletRequest request, ModelMap modelMap, FtpServerInfo search, Long ftpServerId) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	FtpServerInfo ftpServer = new FtpServerInfo();
    	if (ftpServerId != null && ftpServerId > 0){
    		ftpServer = ftpServerInfoService.findFtpServerById(ftpServerId);
    	}
    	List<Company> companyList = companyService.findAllCompanys(curOper);
    	modelMap.put("ftpServer", ftpServer);
    	modelMap.put("search", search);
    	modelMap.addAttribute("companyList", companyList);
    	return "/opermgmt/ftp/ftpServerEdit";
    }

	@RequestMapping(value = "/saveOrUpdateFtpServer.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateFtpServer(HttpServletRequest request,
			HttpServletResponse response, FtpServerInfo ftpServer) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {
			
			if (ftpServer.getCompanyNo() == null){
				ftpServer.setCompanyNo(curOper.getCompanyNo());
			}
			
			if (ftpServer.getId() != null && ftpServer.getId() > 0) {
				logStr = "更新[" + ftpServer.getIp() + "]FTP服务器";
				ftpServer.setUpdateTime(DateUtil.getCurrentTime());
				ftpServerInfoService.updateFtpServer(ftpServer);
			} else {
				logStr = "添加[" + ftpServer.getIp() + "]FTP服务器";
				ftpServer.setCreateTime(DateUtil.getCurrentTime());
				ftpServer.setOperatorNo(curOper.getOperatorNo());
				ftpServer.setGroupId(curOper.getCreateBy());
				ftpServerInfoService.addFtpServer(ftpServer);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (ftpServer.getId() == null) {
				logger.error("Add ftp server info exception occurred, cause by:{}", e);
			} else {
				logger.error("Update ftp server info exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/ftpServerDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String ftpServerDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] ftpServerIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			
			if (ftpServerIds != null && ftpServerIds.length > 0) {

				List<FtpServerInfo> list = ftpServerInfoService.findFtpServersById(ftpServerIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getIp() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getIp();
				ftpServerInfoService.deleteFtpServersById(ftpServerIds);

			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]FTP服务器", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]FTP服务器", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete ftp server info exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/browseFtpServer.action")
	public String browseFtpServer(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Long ftpServerId,String workDirectory,FileInfo search,boolean select,boolean first) throws Exception {

		try {
			FtpServerInfo ftpServer = ftpServerInfoService.findFtpServerById(ftpServerId);
			FtpProcessor ftp = new FtpProcessor(ftpServer.getIp(),
					ftpServer.getPort(), ftpServer.getUserName(),
					ftpServer.getPassword());
			if (!ftp.connectServer()){
				modelMap.put("desc", "无法登陆FTP服务器，用户名或密码错误！");
				return "/common/closeTip";
			}
			FTPFile[] files = ftp.listFileFilter(workDirectory, search.getName(),null);
			List<FileInfo> fileList = new ArrayList<FileInfo>();
			for (int i = 0; i < files.length; i++) {
				FTPFile file = files[i];
				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(file.getName());
				fileInfo.setType(file.getType());
				fileInfo.setSize(file.getSize());
				Date updateTime = file.getTimestamp().getTime();
				String path = ftp.getWorkingDirectory();
				fileInfo.setPath(path);
				fileInfo.setUpdateTime(DateUtil.format(updateTime,
						"yyyy-MM-dd HH:mm:ss"));
				fileList.add(fileInfo);
			}

			PageUtil page = search.getPageUtil();
			if (first){
				page.setPageId(1);
			}
			page.setRowCount(fileList.size());
			page.setRslist(fileList);
			List subFileList = page.getPageRslist();
			workDirectory = ftp.getWorkingDirectory();
			String videoFileUrl = packageVideoFileUrl(ftpServer, workDirectory);
			
			modelMap.addAttribute("subFileList", subFileList);
			modelMap.addAttribute("videoFileUrl", videoFileUrl);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("ftpServerId", ftpServerId);
			modelMap.put("search", search);
			modelMap.put("workDirectory", workDirectory);
			modelMap.put("select", select);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/opermgmt/ftp/browseFtpFileList";
	}
	
	private String packageVideoFileUrl(FtpServerInfo entity,
			String workDirectory) {
		StringBuffer tempVideoFileUrl = new StringBuffer();
		tempVideoFileUrl.append("ftp://").append(entity.getUserName())
				.append(":").append(entity.getPassword()).append("@")
				.append(entity.getIp()).append(":").append(entity.getPort())
				.append(workDirectory);

		if (!"/".equals(workDirectory)) {
			tempVideoFileUrl.append("/");
		}
		return tempVideoFileUrl.toString();
	}
	
	@RequestMapping(value = "/browseFtpServerList.action")
	public String browseftpServerList(HttpServletRequest request, ModelMap modelMap, FtpServerInfo search, Long albumNo)
			throws Exception {

        Operator curOper = SessionUtil.getActiveOperator(request);
        List<FtpServerInfo> list = null;

        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	list = this.ftpServerInfoService.findFtpServers(search);
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	list = this.ftpServerInfoService.findFtpServers(search);
        	break;
        case ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	list = this.ftpServerInfoService.findFtpServers(search);
        	break;
        }

        Album album = albumService.findAlbumById(albumNo);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("album", album);
		return "opermgmt/ftp/browseFtpServerList";
	}
	
	@RequestMapping(value = "/selectFtpFileList.action")
	public String selectFtpFileList(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Long albumNo, Long ftpServerId,String workDirectory,FileInfo search,boolean first) throws Exception {

		try {
			Album album = albumService.findAlbumById(albumNo);
			Map<String, String> fileExtMap = new HashMap<String, String>();
			fileExtMap.put("xlsx", "xlsx");
			fileExtMap.put("xls", "xls");
			if (StringUtils.isNotEmpty(album.getPicFormatStr())){
				String[] picFormatArr = album.getPicFormatStr().split(Delimiters.COMMA);
				for (String fileExt : picFormatArr){
					fileExtMap.put(fileExt, fileExt);
				}
			}else{
			   for (String fileExt : ConstConfig.pictureFormatMap.values()){
				   fileExtMap.put(fileExt, fileExt);
			   }
			}

			FtpServerInfo ftpServer = ftpServerInfoService.findFtpServerById(ftpServerId);
			FtpProcessor ftp = new FtpProcessor(ftpServer.getIp(),
					ftpServer.getPort(), ftpServer.getUserName(),
					ftpServer.getPassword());
			if (!ftp.connectServer()){
				modelMap.put("desc", "无法登陆FTP服务器，用户名或密码错误！");
				return "/common/closeTip";
			}
			FTPFile[] files = ftp.listFileFilter(workDirectory, search.getName(),fileExtMap);
			List<FileInfo> fileList = new ArrayList<FileInfo>();
			for (int i = 0; i < files.length; i++) {
				FTPFile file = files[i];
				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(file.getName());
				fileInfo.setType(file.getType());
				fileInfo.setSize(file.getSize());
				Date updateTime = file.getTimestamp().getTime();
				String path = ftp.getWorkingDirectory();
				fileInfo.setPath(path);
				fileInfo.setUpdateTime(DateUtil.format(updateTime,
						"yyyy-MM-dd HH:mm:ss"));
				fileList.add(fileInfo);
			}

			PageUtil page = search.getPageUtil();
			if (first){
				page.setPageId(1);
			}
			page.setRowCount(fileList.size());
			page.setRslist(fileList);
			List subFileList = page.getPageRslist();
			workDirectory = ftp.getWorkingDirectory();
			String videoFileUrl = packageVideoFileUrl(ftpServer, workDirectory);
			
			modelMap.addAttribute("subFileList", subFileList);
			modelMap.addAttribute("videoFileUrl", videoFileUrl);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("ftpServerId", ftpServerId);
			modelMap.put("search", search);
			modelMap.put("workDirectory", workDirectory);
			modelMap.put("album", album);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/opermgmt/ftp/selectFtpFileList";
	}
}
