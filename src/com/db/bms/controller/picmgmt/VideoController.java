
package com.db.bms.controller.picmgmt;

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
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Company;
import com.db.bms.entity.FileInfo;
import com.db.bms.entity.FtpServerInfo;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Video;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Video.VideoStatus;
import com.db.bms.service.AlbumService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.FtpServerInfoService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.VideoService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FtpProcessor;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("picmgmt/video")
@Controller
public class VideoController {

	private final static Logger logger = Logger
	.getLogger(TopicController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private FtpServerInfoService ftpServerInfoService;
	
	@Autowired
	private AlbumService albumService;
	
	@RequestMapping(value = "/videoList.action")
	public String videoList(HttpServletRequest request, ModelMap modelMap, Video search)
			throws Exception {

        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	break;
        case ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	break;
        }

        List<Video> list = videoService.findVideos(search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("videoStatusMap", ConstConfig.videoStatusMap);
		return "picmgmt/video/videoList";
	}
	
    @RequestMapping(value = "/videoEdit.action")
    public String videoEdit(HttpServletRequest request, ModelMap modelMap, Video search, Long videoId) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	Video video = new Video();
    	if (videoId != null && videoId > 0){
    		video = videoService.findVideoById(videoId);
    	}
    	List<Company> companyList = companyService.findAllCompanys(curOper);
    	modelMap.put("video", video);
    	modelMap.put("search", search);
    	modelMap.addAttribute("companyList", companyList);
    	return "picmgmt/video/videoEdit";
    }

	@RequestMapping(value = "/saveOrUpdateVideo.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateVideo(HttpServletRequest request,
			HttpServletResponse response, Video video) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {
			
			if (video.getCompanyNo() == null){
				video.setCompanyNo(curOper.getCompanyNo());
			}
			
			if (video.getId() != null && video.getId() > 0) {
				logStr = "更新[" + video.getVideoName() + "]视频";
				video.setStatus(VideoStatus.DRAFT.getIndex());
				video.setUpdateTime(DateUtil.getCurrentTime());
				videoService.updateVideo(video);
			} else {
				logStr = "添加[" + video.getVideoName() + "]视频";
				video.setAssetId(StringUtils.generateAssetId());
				video.setStatus(VideoStatus.DRAFT.getIndex());
				video.setCreateTime(DateUtil.getCurrentTime());
				video.setOperatorNo(curOper.getOperatorNo());
				video.setGroupId(curOper.getCreateBy());
				videoService.addVideo(video);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (video.getId() == null) {
				logger.error("Add video exception occurred, cause by:{}", e);
			} else {
				logger.error("Update video exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/videoDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String videoDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] videoIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			
			if (videoIds != null && videoIds.length > 0) {

				List<Video> list = videoService.findVideosById(videoIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getVideoName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getVideoName();
				videoService.deleteVideos(videoIds);
			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]视频", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]视频", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete video exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/videoDetail.action")
	public String videoDetail(HttpServletRequest request, ModelMap modelMap,
			Long videoId) throws Exception {

		Video video = this.videoService.findVideoById(videoId);
		modelMap.put("video", video);
		modelMap.put("videoStatusMap", ConstConfig.videoStatusMap);
		return "picmgmt/video/videoDetail";
	}
	
	@RequestMapping(value = "/browseVideoFtpServerList.action")
	public String browseftpServerList(HttpServletRequest request, ModelMap modelMap, FtpServerInfo search)
			throws Exception {

        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
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
		return "opermgmt/ftp/browseVideoFtpServerList";
	}
	
	@RequestMapping(value = "/selectVideoFtpFileList.action")
	public String selectFtpFileList(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap,Long ftpServerId,String workDirectory,FileInfo search,boolean first) throws Exception {

		try {
			Map<String, String> fileExtMap = new HashMap<String, String>();
			fileExtMap.put("ts", "ts");
			fileExtMap.put("mp4", "mp4");

			FtpServerInfo ftpServer = ftpServerInfoService.findFtpServerById(ftpServerId);
			FtpProcessor ftp = new FtpProcessor(ftpServer.getIp(),
					ftpServer.getPort(), ftpServer.getUserName(),
					ftpServer.getPassword());
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
			modelMap.addAttribute("ftpServer", ftpServer);
			modelMap.put("workDirectory", workDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/opermgmt/ftp/selectVideoFtpFileList";
	}
	
	@RequestMapping(value = "/videoCapture.action", method = RequestMethod.POST)
	@ResponseBody
	public String videoCapture(HttpServletRequest request, Long[] videoIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			if (videoIds != null && videoIds.length > 0) {
				List<Video> list = videoService.findVideosById(videoIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getVideoName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getVideoName();
				
				for (Video video : list){
					Album album = albumService.findAlbumByAlbumId(video.getAssetId());
					if (album != null && album.getStatus() == AuditStatus.PUBLISH){
						return "{result: '" + result + "', desc : 'published'}";
					}
				}
				
				for (Video video : list){
					video.setStatus(VideoStatus.COMMITTING.getIndex());
					video.setUpdateTime(DateUtil.getCurrentTime());
					video.setSendTime(DateUtil.getCurrentTime());
					videoService.updateVideo(video);
					this.processor.putVideoToQueue(video);
				}
			}
			result = true;
			logService.logToDB(request, "开始提交视频[" + str + "]截图", LogUtil.LOG_INFO, true, false);
		} catch (Exception e) {
			result = false;
			logService.logToDB(request, "提交视频[" + str + "]截图", LogUtil.LOG_INFO, false, true);
			logger.error("Video capture exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	private String packageVideoFileUrl(FtpServerInfo entity,
			String workDirectory) {
		StringBuffer tempVideoFileUrl = new StringBuffer();
		tempVideoFileUrl.append("ftp://")
				.append(entity.getIp()).append(":").append(entity.getPort())
				.append(workDirectory);

		if (!"/".equals(workDirectory)) {
			tempVideoFileUrl.append("/");
		}
		return tempVideoFileUrl.toString();
	}
}
