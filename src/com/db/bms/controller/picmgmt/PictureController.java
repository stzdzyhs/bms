package com.db.bms.controller.picmgmt;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.db.bms.common.file.BatchUploadResult;
import com.db.bms.common.file.FileHead;
import com.db.bms.common.file.FileType;
import com.db.bms.common.file.BatchUploadResult.ResultCode;
import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.FtpServerInfo;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.service.AlbumService;
import com.db.bms.service.ArticleService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.FtpServerInfoService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.PictureService;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FTPUtil;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.MD5Engine;
import com.db.bms.utils.Result;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("picmgmt/picture")
@Controller
public class PictureController {

	private final static Logger logger = Logger.getLogger(PictureController.class);

	@Autowired
	private LogService logService;

	@Autowired
	private AlbumService albumService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private FtpServerInfoService ftpServerInfoService;
	
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private ArticleService articleSerivce;
	
	@Autowired
	private ResourcePublishMapService resourcePublishMapService;

	@RequestMapping(value = "/pictureList.action")
	public String albumList(HttpServletRequest request, ModelMap modelMap,
			Picture search,String cmdStr) throws Exception {

		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		PageUtil page = search.getPageUtil();
    	page.setPaging(true);
		Album album = albumService.findAlbumById(search.getAlbumNo());
		if (StringUtils.isEmpty(search.getSortKey())){
			if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.YES){
				search.setSortKey("frameNum");
				search.setSortType("asc");
			}else{
				search.setSortKey("createTime");
				search.setSortType("desc");
			}
		}
		
		List<Picture> list = pictureService.findPictures(search);
		List<Operator> operatorList = operatorService.findAllOperators(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("album", album);
		Long createdBy = null;
		createdBy = album.getOperatorNo();
		if(album.getOperator().getType().intValue() == 2){
			createdBy = album.getOperator().getCreateBy();
		}
		modelMap.put("albumCreatedBy", createdBy);
		modelMap.put("pictureStatusMap", ConstConfig.pictureStatusMap);
		modelMap.put("pictureVoteFlagMap", ConstConfig.pictureVoteFlagMap);
		if(cmdStr != null){
			SessionUtil.setAttr(request, "cmdStr", cmdStr);
		}
		return "picmgmt/picture/pictureList";
	}

	@RequestMapping(value = "/pictureEdit.action")
	public String albumEdit(HttpServletRequest request, ModelMap modelMap,
			Picture search, Long pictureId) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		Picture picture = new Picture();
		if (pictureId != null && pictureId > 0) {
			picture = pictureService.findPictureById(pictureId);
		}

		Album album = albumService.findAlbumById(search.getAlbumNo());
		modelMap.put("picture", picture);
		modelMap.put("search", search);
		modelMap.put("album", album);
		modelMap.put("pictureStatusMap", ConstConfig.pictureStatusMap);
		modelMap.put("pictureVoteFlagMap", ConstConfig.pictureVoteFlagMap);
		return "picmgmt/picture/pictureEdit";
	}
	
	@RequestMapping(value = "/pictureEditBasicInfo.action")
	public String pictureEditBasicInfo(HttpServletRequest request, ModelMap modelMap,
			Picture search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		
		Picture picture = new Picture();
		if (search.getId() != null && search.getId() > 0) {
			picture = pictureService.findPictureById(search.getId());
		}

		//Album album = albumService.findAlbumById(search.getAlbumNo());
		modelMap.put("picture", picture);
		modelMap.put("search", search);
		//modelMap.put("album", album);
		modelMap.put("pictureVoteFlagMap", ConstConfig.pictureVoteFlagMap);
		return "picmgmt/picture/pictureEditBasicInfo";
	}
	
	@RequestMapping(value = "/isResNoUnique.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String isResNoUnique(HttpServletRequest request, Picture picture) throws Exception {
        Result result = new Result();
        result.setResult(this.pictureService.isResNoUnique(picture));
        String ret = JSON.toJSONString(result);
        return ret;
	}

	

	@RequestMapping(value = "/saveOrUpdatePicture.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String saveOrUpdatePicture(HttpServletRequest request,
			HttpServletResponse response, Picture picture) throws Exception {
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {
			if (picture.getCompanyNo() == null) {
				Album album = albumService.findAlbumById(picture.getAlbumNo());
				picture.setCompanyNo(album.getCompanyNo());
			}

			if (StringUtils.isNotEmpty(picture.getPicPath())) {

				MD5Engine engine = new MD5Engine(false);
				String md5 = engine.calculateMD5(rootPath
						+ picture.getPicPath());
				if (StringUtils.isNotEmpty(md5)){
					picture.setCheckCode(md5);
				}
			}

			if (picture.getId() != null && picture.getId() > 0) {
				logStr = "更新[" + picture.getPicName() + "]图片";
				picture.setStatus(PictureStatus.DRAFT.getIndex());
				picture.setUpdateTime(DateUtil.getCurrentTime());
				if(picture.getShowOrder() == null){
					picture.setShowOrder(100);
				}
				pictureService.updatePicture(picture);
			} else {
				logStr = "添加[" + picture.getPicName() + "]图片";
				picture.setPictureId(StringUtils.generateFixPrefixId("PIC"));
				picture.setStatus(AuditStatus.DRAFT);
				picture.setCreateTime(DateUtil.getCurrentTime());
				picture.setOperatorNo(curOper.getOperatorNo());
				pictureService.addPicture(picture);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (picture.getId() == null) {
				logger.error("Add picture exception occurred, cause by:{}", e);
			} else {
				logger.error("Update picture exception occurred, cause by:{}",
						e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	/**
	 * update picture basic info(picName, picLabe, picDesc, picAuthor, picSource, voteFlag)
	 * @param request
	 * @param response
	 * @param picture
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pictureUpdateBasicInfo.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String pictureUpdateBasicInfo(HttpServletRequest request, Picture picture) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		picture.setCurOper(curOper);
		
		Result result = new Result();
		String logStr = "";
		try {
			if (picture.getId()==null) {
				logStr = "图片错误";
				throw new Exception("图片错误");
			}

			pictureService.updatePictureBasicInfo(picture);
			logStr = "更新图片基本信息";
			
			result.setResult(true);
			
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (picture.getId() == null) {
				logger.error("Add picture exception occurred, cause by:{}", e);
			} else {
				logger.error("Update picture exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, false);
		}
		
		String ret = JSON.toJSONString(result);
		return ret;
	}

	@RequestMapping(value = "/pictureDelete.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String pictureDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] pictureIds) throws Exception {
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		boolean result = false;
		String str = "";
		try {

			if (pictureIds != null && pictureIds.length > 0) {

				List<Picture> list = pictureService
						.findPicturesById(pictureIds);
				for (int i = 0; i < list.size() - 1; i++) {					
					str += list.get(i).getPicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getPicName();
				pictureService.deletePicturesById(pictureIds);

				/*for (Picture pic : list) {
					Operator curOper = SessionUtil.getActiveOperator(request);
				    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
				    File file = new File(rootPath + pic.getPicPath());
				    operatorService.calculateUsedSpace(operator, file, false);
					FileUtils.delFile(rootPath + pic.getPicPath());
				}*/
				
				for (Picture pic : list) {
 					Operator curOper = SessionUtil.getActiveOperator(request);
 				    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
				   // File file = new File(rootPath + pic.getPicPath());
				    File file = null;
					if(pic.isArticlePic()) {
						File dir = this.articleSerivce.getFileDir();
						file = new File(dir, pic.getPicPath());
						operatorService.calculateUsedSpace(operator, file, false);
	 					FileUtils.delFile(dir + "/" +pic.getPicPath());
					}
					else { 
				    	file = new File(rootPath + pic.getPicPath());
				    	operatorService.calculateUsedSpace(operator, file, false);
	 					FileUtils.delFile(rootPath + pic.getPicPath());
					}
 				}
			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]图片", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]图片", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete picture exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/pictureDetail.action")
	public String albumDetail(HttpServletRequest request, ModelMap modelMap,
			Long pictureId) throws Exception {

		Picture picture = this.pictureService.findPictureById(pictureId);
	
		List<ResourcePublishMap> publishList = resourcePublishMapService.findResourcePublishMapById(EntityType.TYPE_ALBUM, picture.getAlbumNo(), EntityType.TYPE_PICTURE, pictureId);
		if(ArrayUtils.isEmpty(publishList)) {
			publishList = null;
		}

		modelMap.put("publish", publishList);
		modelMap.put("picture", picture);
		modelMap.put("pictureStatusMap", ConstConfig.pictureStatusMap);
		modelMap.put("pictureVoteFlagMap", ConstConfig.pictureVoteFlagMap);
		return "picmgmt/picture/pictureDetail";
	}

	@RequestMapping(value = "/pictureAudit.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String pictureAudit(HttpServletRequest request, Integer status,
			Long[] pictureIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			Operator curOper = SessionUtil.getActiveOperator(request);
			Long createdBy = curOper.getOperatorNo();
			if(curOper.getOperator().getType().intValue() == 2){
				createdBy = curOper.getOperator().getCreateBy();
			}
			ResourcePublishMap publish = new ResourcePublishMap();
			publish.setCreatedBy(createdBy);
			if (pictureIds != null && pictureIds.length > 0) {
				List<Picture> list = pictureService
						.findPicturesById(pictureIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getPicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getPicName();
				pictureService.auditPicture(status, pictureIds,publish);

			}
			result = true;
			switch (PictureStatus.getStatus(status)) {
			case AUDITING:
				logService.logToDB(request, "图片[" + str + "]提交审核",
						LogUtil.LOG_INFO, true, true);
				break;
			case AUDIT_PASS:
				logService.logToDB(request, "图片[" + str + "]审核通过",
						LogUtil.LOG_INFO, true, true);
				break;
			case AUDIT_NO_PASS:
				logService.logToDB(request, "图片[" + str + "]审核不通过",
						LogUtil.LOG_INFO, true, true);
				break;
			case PUBLISH:
				processor.putNoticeToQueue(publish.getNoticeList());
				logService.logToDB(request, "图片[" + str + "]发布",
						LogUtil.LOG_INFO, true, true);
				break;
			case UNPUBLISH:
				processor.putNoticeToQueue(publish.getNoticeList());
				logService.logToDB(request, "图片[" + str + "]取消发布",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (PictureStatus.getStatus(status)) {
			case AUDITING:
				logService.logToDB(request, "图片[" + str + "]提交审核",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Picture on submit audit exception occurred, cause by:{}",
						e);
				break;
			case AUDIT_PASS:
				logService.logToDB(request, "图片[" + str + "]审核通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Picture on audit pass exception occurred, cause by:{}",
						e);
				break;
			case AUDIT_NO_PASS:
				logService.logToDB(request, "图片[" + str + "]审核不通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Picture on audit no pass exception occurred, cause by:{}",
						e);
				break;
			case PUBLISH:
				logService.logToDB(request, "发布图片[" + str + "]",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Picture on publish exception occurred, cause by:{}", e);
				break;
			case UNPUBLISH:
				logService.logToDB(request, "取消发布图片[" + str + "]",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Picture on unpublish exception occurred, cause by:{}",
						e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/toPicturePublish.action")
	public String toTopicPublish(HttpServletRequest request,
			ModelMap modelMap, String pictureIds) throws Exception {
		modelMap.put("pictureIds", pictureIds);
		return "picmgmt/picture/picturePublish";
	}
	
	@RequestMapping(value = "/picturePublish.action")
	@ResponseBody
	public String picturePublish(HttpServletRequest request,
			ModelMap modelMap, String pictureIds,ResourcePublishMap publish) throws Exception {

		boolean result = false;
		String str = "";
		try {
			Operator curOper = SessionUtil.getActiveOperator(request);
			Long createdBy = curOper.getOperatorNo();
			if(curOper.getOperator().getType().intValue() == 2){
				createdBy = curOper.getOperator().getCreateBy();
			}
			publish.setCreatedBy(createdBy);
			String[] tempIdArr = pictureIds.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			if (ids != null && ids.length > 0) {
				List<Picture> list = pictureService.findPicturesById(ids);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getPicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getPicName();
				pictureService.auditPicture(PictureStatus.PUBLISH.getIndex(), ids, publish);
				processor.putNoticeToQueue(publish.getNoticeList());
			}
			
			result = true;
			logService.logToDB(request, "发布图片[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "发布图片[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error(
					"Publish pictures exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	

	@RequestMapping(value = "/pictureUploadFile.action",produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String pictureUploadFile(HttpServletRequest request,
			@RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String Filename, Long albumNo,
			Long operatorNo,Long pictureId) throws Exception {

		String desc = "";
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		String fileName = FileUpload.geneFileName(Filename);
		String dirPath = ConstConfig.UPLOAD_PATH + ConstConfig.PIC_FILE_PATH;
		String filePath = dirPath + fileName;
        Operator curOper = operatorService.findOperatorById(operatorNo);
        if (!operatorService.validateUsedSpace(curOper, file.getSize())){
        	return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:'spaceSizeLimit'}";
        }
        
        
		boolean isok = FileUpload.saveFile(file, rootPath + dirPath, fileName);
		File pic = new File(rootPath + dirPath + fileName);
		BufferedImage image = FileUtils.readPictureResolution(pic);
		Album album = albumService.findAlbumById(albumNo);
		if (StringUtils.isNotEmpty(album.getPicHeight())
				&& image.getHeight() > Integer.valueOf(album.getPicHeight())) {
			desc = "resolutionLimit";
			isok = false;
			FileUtils.delFile(rootPath + dirPath + fileName);
			return "{result:" + isok + ",filePath:'" + filePath
					+ "',fileName:'" + Filename + "',desc:'" + desc + "'}";
		}

		if (StringUtils.isNotEmpty(album.getPicWidth())
				&& image.getWidth() > Integer.valueOf(album.getPicWidth())) {
			desc = "resolutionLimit";
			isok = false;
			FileUtils.delFile(rootPath + dirPath + fileName);
		}

		if (pictureId != null && isok) {
			MD5Engine engine = new MD5Engine(false);
			String md5 = engine.calculateMD5(rootPath + dirPath + fileName);
			Picture picture = pictureService.findPictureById(pictureId);
			picture.setCheckCode(md5);
			picture.setPicPath(filePath);
			picture.setUpdateTime(DateUtil.getCurrentTime());
			pictureService.updatePicture(picture);
		}
		
		//上传成功计算使用空间
		if (isok){
			operatorService.calculateUsedSpace(curOper, new File(rootPath + dirPath + fileName), true);
		}
		return "{result:" + isok + ",filePath:'" + filePath + "',fileName:'"
				+ Filename + "',desc:'" + desc + "'}";
	}

	@RequestMapping(value = "/pictureDeleteFile.action",produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String pictureDeleteFile(HttpServletRequest request, Long pictureId,
			@RequestParam("filePath") String filePath) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		FileUtils.delFile(rootPath + filePath);
		String str = "删除图片";
		if (pictureId != null) {
			Picture picture = pictureService.findPictureById(pictureId);
/*			picture.setCheckCode(null);
			picture.setPicPath(filePath);
			picture.setUpdateTime(DateUtil.getCurrentTime());
			pictureService.updatePicture(picture);*/
			str = "删除图片【" + picture.getPicName() + "】";
			
		    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
		    File file = new File(rootPath + filePath);
		    operatorService.calculateUsedSpace(operator, file, false);
		}
		logService.logToDB(request, str, LogUtil.LOG_INFO, true, true);
		return "";
	}

	@RequestMapping(value = "/toBatchUpload.action")
	public String toBatchUpload(HttpServletRequest request, ModelMap modelMap,
			Long albumNo) throws Exception {

		Album album = albumService.findAlbumById(albumNo);
		modelMap.put("album", album);
		return "picmgmt/picture/batchUpload";
	}

	@RequestMapping(value = "/downloadPicturerTemplate.action")
	public String downloadTemplate(HttpServletRequest request,
			HttpServletResponse response, String fileName) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String ctxPath = request.getSession().getServletContext()
				.getRealPath("/");
		String downloadPath = ctxPath + ConstConfig.DOWNLOAD_PATH
				+ ConstConfig.PIC_FILE_PATH + fileName;

		try {
			long length = new File(downloadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ new String(fileName.getBytes("UTF-8"), "ISO8859-1")
					+ "\"");
			response.setHeader("Content-Length", String.valueOf(length));
			bis = new BufferedInputStream(new FileInputStream(downloadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int len;
			while (-1 != (len = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, len);
			}
			logService.logToDB(request, "下载图片模板文件【" + fileName + "】",
					LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "下载图片模板文件【" + fileName + "】",
					LogUtil.LOG_ERROR, false, true);
			logger.error(
					"Download picture template file exception occurred, cause by:{}",
					e);
		} finally {
			if (bis != null) {
				bis.close();
			}

			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}

	@RequestMapping(value = "/pictureBatchUploadFile.action")
	@ResponseBody
	public String pictureBatchUploadFile(HttpServletRequest request,
			@RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String Filename, Long albumNo,
			Long operatorNo) throws Exception {

		// Operator curOper = SessionUtil.getActiveOperator(request);
		String desc = "";
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		String fileName = FileUpload.geneFileName(Filename);
		String dirPath = ConstConfig.UPLOAD_PATH + ConstConfig.PIC_FILE_PATH;
		String filePath = dirPath + fileName;
		boolean result = false;
		String logStr = "";
		Operator curOper = null;
		try {

	        curOper = operatorService.findOperatorById(operatorNo);
	        if (!operatorService.validateUsedSpace(curOper, file.getSize())){
	        	return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:'spaceSizeLimit'}";
	        }
	        
			boolean isok = FileUpload.saveFile(file, rootPath + dirPath, fileName);
			File pic = new File(rootPath + dirPath + fileName);
			BufferedImage image = FileUtils.readPictureResolution(pic);
			Album album = albumService.findAlbumById(albumNo);

			String picName = Filename.substring(0,
					Filename.lastIndexOf(Delimiters.DOT));
			if (album.getPicNameLen() != null
					&& picName.length() > album.getPicNameLen()) {
				picName = picName.substring(0, album.getPicNameLen());
				if (picName.length() > 30){
					picName = picName.substring(0, 30);
				}
			}
			
			if (album.getPicNameLen() == null && picName.length() > 30){
				picName = picName.substring(0, 30);
			}

			if (StringUtils.isNotEmpty(album.getPicHeight())
					&& image.getHeight() > Integer.valueOf(album.getPicHeight())) {
				desc = "resolutionLimit";
				isok = false;
				FileUtils.delFile(rootPath + dirPath + fileName);
				return "{result:" + isok + ",filePath:'" + filePath
						+ "',fileName:'" + Filename + "',desc:'" + desc + "'}";
			}

			if (StringUtils.isNotEmpty(album.getPicWidth())
					&& image.getWidth() > Integer.valueOf(album.getPicWidth())) {
				desc = "resolutionLimit";
				isok = false;
				FileUtils.delFile(rootPath + dirPath + fileName);
				return "{result:" + isok + ",filePath:'" + filePath
						+ "',fileName:'" + Filename + "',desc:'" + desc + "'}";
			}

			MD5Engine engine = new MD5Engine(false);
			String md5 = engine.calculateMD5(rootPath + dirPath + fileName);
			Picture picture = new Picture();
			picture.setPictureId(StringUtils.generateFixPrefixId("PIC"));
			picture.setShowOrder(100);
			picture.setPicName(picName);
			picture.setPicPath(filePath);
			picture.setCheckCode(md5);
			picture.setStatus(PictureStatus.DRAFT.getIndex());
			picture.setAlbumNo(albumNo);
			picture.setCompanyNo(album.getCompanyNo());
			picture.setOperatorNo(operatorNo);
			picture.setCreateTime(DateUtil.getCurrentTime());
			pictureService.addPicture(picture);
			operatorService.calculateUsedSpace(curOper, new File(rootPath + dirPath + fileName), true);
			result = true;
			logService.logToDB(curOper, "本地批量上传【" + Filename + "】",
					LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logger.error("Local batch upload pictures exception occurred, cause by:{}",
					e);
			logService.logToDB(curOper, "本地批量上传【" + Filename + "】", LogUtil.LOG_ERROR, false, true);
			result = false;
		}
		
		return "{result:" + result + ",filePath:'" + filePath + "',fileName:'"
				+ Filename + "',desc:'" + desc + "'}";
	}

	@RequestMapping(value = "/pictureFtpBatchUploadFile.action",produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String pictureFtpBatchUploadFile(HttpServletRequest request, String[] filePaths
			,Long albumNo, Long operatorNo, Integer voteFlag) throws Exception{
		
		BatchUploadResult result = new BatchUploadResult();
		result.setResultCode(ResultCode.SUCCESS.getIndex());
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		String dirPath = ConstConfig.UPLOAD_PATH + ConstConfig.PIC_FILE_PATH;
        Album album = albumService.findAlbumById(albumNo);
		Map<String, String> fileExtMap = new HashMap<String, String>();
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
		
		try{
            Operator curOper = operatorService.findOperatorById(operatorNo);
			 for (int i = 0; i < filePaths.length; i++){
		        	result.fileTotalIncrement();
		        	//String filePath = new String(filePaths[i].getBytes("ISO-8859-1"),"utf-8");
		        	String filePath = filePaths[i];
		        	String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		        	String suffix = fileName.substring(fileName.lastIndexOf(Delimiters.DOT) + 1);
		        	String ftpServerId = filePath.substring(0,filePath.indexOf("/"));
		        	String remoteFile = filePath.substring(filePath.indexOf("/"));
		    		String localFileName = FileUpload.geneFileName(fileName);
		       		FtpServerInfo ftpServerInfo = ftpServerInfoService.findFtpServerById(Long.valueOf(ftpServerId));
		        	if (fileExtMap.containsKey(suffix)){
		        		result.totalIncrement();
		        		String localFile = rootPath + dirPath + localFileName;
		        		boolean success = FTPUtil.downFile(ftpServerInfo.getIp(), ftpServerInfo.getPort(), ftpServerInfo.getUserName(), ftpServerInfo.getPassword(), remoteFile, localFile);
		        		if (success){
		            		String picName = fileName.substring(0,
		            				fileName.lastIndexOf(Delimiters.DOT));
		            		if (album.getPicNameLen() != null
		            				&& picName.length() > album.getPicNameLen()) {
		            			picName = picName.substring(0, album.getPicNameLen());
		            			if (picName.length() > 30){
		            				picName = picName.substring(0, 30);
		            			}
		            		}
		            		
		            		if (album.getPicNameLen() == null && picName.length() > 30){
		            			picName = picName.substring(0, 30);
		            		}

		            		File pic = new File(localFile);
		                    if (!operatorService.validateUsedSpace(curOper, pic.length())){
		            			result.failCountIncrement();
		            			result.setDesc("spaceSizeLimit");
		            			result.failDescIncrement("超出相册设置空间的限制");
		            			FileUtils.delFile(localFile);
		            			break;
		                    }
		                    
		                    
		            		if (album.getPicSize() != null && pic.length() > (album.getPicSize() * 1024)){
		            			result.failCountIncrement();
		            			result.setDesc("fileSizeLimit");
		            			result.failDescIncrement("["+pic.getName()+"]大小["+pic.length()+"]超出相册设置的大小限制["+album.getPicSize()+"]");
		            			FileUtils.delFile(localFile);
		            			continue;
		            		}
		            		BufferedImage image = FileUtils.readPictureResolution(pic);
		            		if (StringUtils.isNotEmpty(album.getPicHeight())
		            				&& image.getHeight() > Integer.valueOf(album.getPicHeight())) {
		            			result.failCountIncrement();
		            			result.setDesc("resolutionLimit");
		            			result.failDescIncrement("["+pic.getName()+"]高度["+image.getHeight()+"]超出相册设置的高度限制["+album.getPicHeight()+"]");
		            			FileUtils.delFile(localFile);
		            			continue;
		            		}

		            		if (StringUtils.isNotEmpty(album.getPicWidth())
		            				&& image.getWidth() > Integer.valueOf(album.getPicWidth())) {
		            			result.failCountIncrement();
		            			result.setDesc("resolutionLimit");
		            			result.failDescIncrement("["+pic.getName()+"]宽度["+image.getWidth()+"]超出相册设置的宽度限制["+album.getPicWidth()+"]");
		            			FileUtils.delFile(localFile);
		            			continue;
		            		}

		            		MD5Engine engine = new MD5Engine(false);
		            		String md5 = engine.calculateMD5(localFile);
		            		Picture picture = new Picture();
		            		picture.setPictureId(StringUtils.generateFixPrefixId("PIC"));
		            		picture.setShowOrder(100);
		            		picture.setPicName(picName);
		            		picture.setPicPath(dirPath + localFileName);
		            		picture.setCheckCode(md5);
		            		picture.setStatus(PictureStatus.DRAFT.getIndex());
		            		picture.setVoteFlag(voteFlag);
		            		picture.setAlbumNo(albumNo);
		            		picture.setCompanyNo(album.getCompanyNo());
		            		picture.setOperatorNo(operatorNo);
		            		picture.setCreateTime(DateUtil.getCurrentTime());
		            		pictureService.addPicture(picture);
		            		result.successCountIncrement();
		            		operatorService.calculateUsedSpace(curOper, pic, true);
		        		}else{
		        			result.failCountIncrement();
		        		}

		        	}else if ("xls".equals(suffix)){
		        		
		        		String localFile = rootPath + ConstConfig.UPLOAD_PATH + ConstConfig.TEMP_PIC_FILE_PATH + localFileName;
		        		boolean success = FTPUtil.downFile(ftpServerInfo.getIp(), ftpServerInfo.getPort(), ftpServerInfo.getUserName(), ftpServerInfo.getPassword(), remoteFile, localFile);
		        		if (success){
		        			result = handleExcel(new File(localFile), FileType.Excel, result, operatorNo,  album, ftpServerInfo,  rootPath, dirPath, fileExtMap);
		        		}else{
		        			result.failCountIncrement();
		        		}
		        		
		        	}else if ("xlsx".equals(suffix)){
		        		String localFile = rootPath + ConstConfig.UPLOAD_PATH + ConstConfig.TEMP_PIC_FILE_PATH + localFileName;
		        		boolean success = FTPUtil.downFile(ftpServerInfo.getIp(), ftpServerInfo.getPort(), ftpServerInfo.getUserName(), ftpServerInfo.getPassword(), remoteFile, localFile);
		        		if (success){
		            		result = handleExcel(new File(localFile), FileType.ExcelX, result, operatorNo,  album, ftpServerInfo,  rootPath, dirPath, fileExtMap);
		        		}else{
		        			result.failCountIncrement();
		        		}
		        	}else{
		        		result.failCountIncrement();
		        	}
		        }
			 
			 if (result.getSuccessCount() > 0){
					logService.logToDB(
							request,
							"FTP批量上传图片成功，总文件数：" + result.getFileTotal() + "，总条数："
									+ result.getTotal() + "，成功条数："
									+ result.getSuccessCount() + "，失败条数："
									+ result.getFailCount(), LogUtil.LOG_INFO,
							true, false);
			 }else{
				    result.setResultCode(ResultCode.UPLOAD_FAILED.getIndex());
					logService.logToDB(request, "FTP批量上传图片失败，总文件数：" + result.getFileTotal(),
							LogUtil.LOG_ERROR, true, false);
			 }
		}catch (Exception e) {

			logService.logToDB(request, "FTP批量上传图片失败，总文件数：" + result.getFileTotal(),
					LogUtil.LOG_ERROR, true, false);
			logger.error(
					"The FTP batch upload pictures exception occurred, cause by:{}", e);
		} 

		return result.toJson();
	}
	
	public BatchUploadResult handleExcel(File file, FileType type, BatchUploadResult result,Long operatorNo, Album album,FtpServerInfo ftpServerInfo, String rootPath, String dirPath, Map<String, String> fileExtMap) throws Exception {
		InputStreamReader reader = null;
		try {
			Operator curOper = operatorService.findOperatorById(operatorNo);
			InputStream input = new FileInputStream(file);
			Workbook wb = null;
			if (type == FileType.Excel) {
				wb = new HSSFWorkbook(input);
			} else if (type == FileType.ExcelX) {
				wb = new XSSFWorkbook(input);
			}

			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			boolean first = true;
			while (rows.hasNext()) {
				Row row = rows.next();
				if (row == null) {
					continue;
				}

				int lastCellNum = row.getLastCellNum();
				String[] cells = readCells(row, lastCellNum);

				if (first) {
					if (!handleExcelHead(cells)) {
						result.setResultCode(ResultCode.INVALID_CONTENT_TITLE
								.getIndex());
						return result;
					}
					first = false;
					continue;
				}

				// 总条数
				result.totalIncrement();
/*				if (cells.length != FileHead.PICTURE.length) {
					result.failCountIncrement();
					continue;
				}*/

				//图片名称
				String picName = cells[0].trim();
				if (StringUtils.isEmpty(picName)) {
					result.failCountIncrement();
					continue;
				}
        		if (album.getPicNameLen() != null
        				&& picName.length() > album.getPicNameLen()) {
        			picName = picName.substring(0, album.getPicNameLen());
        			if (picName.length() > 30){
        				picName = picName.substring(0, 30);
        			}
        		}
        		if (album.getPicNameLen() == null && picName.length() > 30){
        			picName = picName.substring(0, 30);
        		}
        		if (album.getPicNameLen() != null
        				&& picName.length() > album.getPicNameLen()) {
        			picName = picName.substring(0, album.getPicNameLen());
        		}

				String picPath = cells[1].trim();
				if (StringUtils.isEmpty(picPath)) {
					result.failCountIncrement();
					continue;
				} 

				String voteFlag = "0";
				if (cells.length >= 3){
				    voteFlag = cells[2].trim();
					if (StringUtils.isEmpty(voteFlag)) {
						voteFlag = "0";
					} 
					
					Pattern p = Pattern.compile("^[0-1]{1}$");
					Matcher m = p.matcher(voteFlag);
					if (!m.matches()) {
						voteFlag = "0";
					}
				}

/*				String videoTime = null;
				if (cells.length >= 4){
					videoTime = cells[3].trim();
					Pattern p = Pattern.compile(Regexs.VIDEO_TIME);
					Matcher m = p.matcher(videoTime);
					if (StringUtils.isNotEmpty(videoTime) && !m.matches()){
						result.failCountIncrement();
						continue;
					}
				}*/
				
	        	String fileName = picPath.substring(picPath.lastIndexOf("/") + 1);
	        	String suffix = fileName.substring(fileName.lastIndexOf(Delimiters.DOT) + 1);
	        	if (!fileExtMap.containsKey(suffix)){
					result.failCountIncrement();
					continue;
	        	}
	        	
				String localFileName = FileUpload.geneFileName(fileName);
				String localFile = rootPath + dirPath + localFileName;
        		boolean success = FTPUtil.downFile(ftpServerInfo.getIp(), ftpServerInfo.getPort(), ftpServerInfo.getUserName(), ftpServerInfo.getPassword(), picPath, localFile);
        		if (success){
        			
        			File pic = new File(localFile);
                    if (!operatorService.validateUsedSpace(curOper, pic.length())){
            			result.failCountIncrement();
            			result.setDesc("spaceSizeLimit");
            			FileUtils.delFile(localFile);
            			continue;
                    }
                    
            		if (album.getPicSize() != null && pic.length() > (album.getPicSize() * 1024)){
            			result.failCountIncrement();
            			FileUtils.delFile(localFile);
            			continue;
            		}
            		
            		BufferedImage image = FileUtils.readPictureResolution(pic);
            		if (StringUtils.isNotEmpty(album.getPicHeight())
            				&& image.getHeight() > Integer.valueOf(album.getPicHeight())) {
            			result.failCountIncrement();
            			FileUtils.delFile(localFile);
            			continue;
            		}

            		if (StringUtils.isNotEmpty(album.getPicWidth())
            				&& image.getWidth() > Integer.valueOf(album.getPicWidth())) {
            			result.failCountIncrement();
            			FileUtils.delFile(localFile);
            			continue;
            		}
            		
            		MD5Engine engine = new MD5Engine(false);
            		String md5 = engine.calculateMD5(localFile);
            		Picture picture = new Picture();
            		picture.setShowOrder(100);
            		picture.setPicName(picName);
            		picture.setPicPath(dirPath + localFileName);
            		picture.setCheckCode(md5);
            		picture.setStatus(PictureStatus.DRAFT.getIndex());
            		picture.setVoteFlag(Integer.valueOf(voteFlag));
            		//picture.setVideoTime(videoTime);
            		picture.setAlbumNo(album.getAlbumNo());
            		picture.setCompanyNo(album.getCompanyNo());
            		picture.setOperatorNo(operatorNo);
            		picture.setCreateTime(DateUtil.getCurrentTime());
            		pictureService.addPicture(picture);
    				// 成功条数
    				result.successCountIncrement();
    				operatorService.calculateUsedSpace(curOper, pic, true);
        		}else{
        			result.failCountIncrement();
        		}

			}

			result.setResultCode(ResultCode.SUCCESS.getIndex());
		} catch (Exception e) {
			result.setResultCode(ResultCode.OTHER.getIndex());
			logger.error("Read excel file exception occurred, cause by:{}", e);
			throw e;
		} finally {
			FileUtils.closeReader(reader);
			FileUtils.delFile(file.getPath());
		}

		return result;
	}
	
	private boolean handleExcelHead(String[] cells) {
		if (cells.length != FileHead.PICTURE.length) {
			return false;
		}

		for (int i = 0; i < FileHead.PICTURE.length; i++) {

			String title = cells[i];
			if (!FileHead.PICTURE[i].equals(title)) {
				return false;
			}
		}
		return true;
	}

	private String[] readCells(Row row, int lastCellNum) {
		Cell cell = null;
		String value = null;
		List<String> cells = new ArrayList<String>();
		DataFormatter formatter = new DataFormatter();
		for (int i = 0; i < lastCellNum; i++) {
			cell = row.getCell(i);
			if (cell == null) {
				continue;
			}
			
			if (cell.getCellType() != Cell.CELL_TYPE_FORMULA) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue());
					    value = DateUtil.getDateStr(date);
					} else {
						String temp = formatter.formatCellValue(cell);
						value = temp == null ? "" : temp.trim();
					}
				} else {
					value = formatter.formatCellValue(cell);
				}
			} else {
				value = cell.getStringCellValue();
			}
			
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			cells.add(value);
		}
		return cells.toArray(new String[0]);
	}
}
