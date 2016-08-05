package com.db.bms.controller.opermgmt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.db.bms.entity.Album;
import com.db.bms.entity.Article;
import com.db.bms.entity.Article.ArticleStatus;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.ButtonItemInfo;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.Company;
import com.db.bms.entity.CoverInfo;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.GTCInfo;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.PicListInfo;
import com.db.bms.entity.Picture;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.Template;
import com.db.bms.entity.Template.ComponentType;
import com.db.bms.entity.Template.TemplateStatus;
import com.db.bms.entity.Template.TemplateType;
import com.db.bms.entity.TextInfo;
import com.db.bms.entity.Topic;
import com.db.bms.service.AlbumService;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnArticleMapService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.PictureService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.TemplateService;
import com.db.bms.service.TopicService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.PageCreator;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.XmlUtils;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/template")
@Controller
public class TemplateController {

	private final static Logger logger = Logger.getLogger(TemplateController.class);

	@Autowired
	private LogService logService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private TopicService topicService;

	@Autowired
	private AlbumService albumService;

	@Autowired
	private ResourceAllocationService resourceAllocationService;

	@Autowired
	private PortalProcessor processor;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private ColumnService columnService;

	@Autowired
	private ColumnArticleMapService columnArticleMapService;

	@Autowired
	private ArticleService articleService;

	@Value("${portal.sysUrl}")
	private String portalUrl;

	@Value("${article.dir}")
	private String articleDir;

	@RequestMapping(value = "/templateList.action")
	public String templateList(HttpServletRequest request, ModelMap modelMap,
			Template search) throws Exception {

		Operator curOper = SessionUtil.getActiveOperator(request);

		List<Template> list = templateService.findTemplates(search);
		List<Operator> operatorList = operatorService.findAllOperators(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("templateTypeMap", ConstConfig.templateTypeMap);
		modelMap.put("templateStatusMap", ConstConfig.templateStatusMap);
		return "opermgmt/template/templateList";
	}

	@RequestMapping(value = "/templateEdit.action")
	public String templateEdit(HttpServletRequest request, ModelMap modelMap,
			Template search, Long templateId) throws Exception {

		Template template = new Template();
		if (templateId != null && templateId > 0) {
			template = templateService.findTemplateById(templateId);
		}
		modelMap.put("template", template);
		modelMap.put("search", search);
		modelMap.put("templateTypeMap", ConstConfig.templateTypeMap);
		return "opermgmt/template/templateEdit";
	}

	@RequestMapping(value = "/checkData.action")
	@ResponseBody
	public String checkData(Template template) throws Exception {
		boolean result = false;
		Template data = new Template();
		data.setId(template.getId());
		data.setTemplateName(template.getTemplateName());
		result = this.templateService.isTemplateRepeat(data);
		return "{result: '" + !result + "', desc : ''}";
	}

	@RequestMapping(value = "/saveOrUpdateTemplate.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateTemplate(HttpServletRequest request,
			HttpServletResponse response, Template template) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {

			int index = template.getFilePath().lastIndexOf(Delimiters.SLASH);
			String fileName = template.getFilePath().substring(index + 1,
					template.getFilePath().length());
			template.setFileName(fileName);
			if (template.getId() != null && template.getId() > 0) {
				logStr = "更新[" + template.getTemplateName() + "]模板";
				template.setStatus(AuditStatus.DRAFT);
				template.setUpdateTime(DateUtil.getCurrentTime());
				templateService.updateTemplate(template);
			} else {
				logStr = "添加[" + template.getTemplateName() + "]模板";
				template.setStatus(AuditStatus.DRAFT);
				template.setCreateTime(DateUtil.getCurrentTime());
				template.setOperatorNo(curOper.getOperatorNo());
				List<Company> companyList = companyService
						.findCompanyByParentId(-1L);
				if (companyList != null && companyList.size() > 0) {
					template.setCompanyNo(companyList.get(0).getCompanyNo());
				} else {
					template.setCompanyNo(curOper.getCompanyNo());
				}

				templateService.addTemplate(template);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (template.getId() == null) {
				logger.error(
						"Add template system exception occurred, cause by:{}",
						e);
			} else {
				logger.error(
						"Update template system exception occurred, cause by:{}",
						e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/templateDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String templateDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] templateIds) throws Exception {
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		boolean result = false;
		String str = "";
		try {

			if (templateIds != null && templateIds.length > 0) {

				List<Template> list = templateService
						.findTemplatesById(templateIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getTemplateName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getTemplateName();
				templateService.deleteTemplatesById(templateIds);

				for (Template template : list) {
					Operator curOper = SessionUtil.getActiveOperator(request);
				    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
				    File file = new File(rootPath + template.getFilePath());
				    operatorService.calculateUsedSpace(operator, file, false);
				    
					FileUtils.delFile(rootPath + template.getFilePath());
				}

			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]模板", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]模板", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error(
					"Delete template system exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/templateDetail.action")
	public String templateDetail(HttpServletRequest request, ModelMap modelMap,
			Long templateId) throws Exception {

		Template template = this.templateService.findTemplateById(templateId);
		modelMap.put("template", template);
		modelMap.put("templateStatusMap", ConstConfig.templateStatusMap);
		modelMap.put("templateTypeMap", ConstConfig.templateTypeMap);
		return "opermgmt/template/templateDetail";
	}

	@RequestMapping(value = "/templateAudit.action", method = RequestMethod.POST)
	@ResponseBody
	public String templateAudit(HttpServletRequest request, Integer status,
			Long[] templateIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			if (templateIds != null && templateIds.length > 0) {
				List<Template> list = templateService
						.findTemplatesById(templateIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getTemplateName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getTemplateName();
				
				for (Template template : list){
					switch (TemplateStatus.getStatus(status)) {
					case ENABLE:
						break;
					case DISABLE:
						boolean isReferenced = templateService.isReferenced(template);
						if (isReferenced){
							return "{result: '" + result + "', desc : 'isReferenced'}";
						}
						break;
					}
				}
				
				templateService.auditTemplate(status, templateIds);

				List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				for (Template template : list) {
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setResourceId(template.getId());
					notice.setResourceType(EntityType.TYPE_TEMPLATE);
					switch (TemplateStatus.getStatus(status)) {
					case ENABLE:
						notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
						break;
					case DISABLE:
						notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
						break;
					}
					noticeList.add(notice);
				}

				processor.putNoticeToQueue(noticeList);

			}
			result = true;
			switch (TemplateStatus.getStatus(status)) {
			case ENABLE:
				logService.logToDB(request, "模板[" + str + "]启用",
						LogUtil.LOG_INFO, true, true);
				break;
			case DISABLE:
				logService.logToDB(request, "模板[" + str + "]禁用",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (TemplateStatus.getStatus(status)) {
			case ENABLE:
				logService.logToDB(request, "模板[" + str + "]启用",
						LogUtil.LOG_INFO, false, true);
				logger.error("Enable template exception occurred, cause by:{}",
						e);
				break;
			case DISABLE:
				logService.logToDB(request, "模板[" + str + "]禁用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Disable template exception occurred, cause by:{}", e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/templateUploadFile.action")
	@ResponseBody
	public String templateUploadFile(HttpServletRequest request,
			@RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String Filename, Long operatorNo) throws Exception {
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		String fileName = FileUpload.geneFileName(Filename);
		String dirPath = ConstConfig.UPLOAD_PATH
				+ ConstConfig.TEMPLATE_FILE_PATH;
		String filePath = dirPath + fileName;
        Operator curOper = operatorService.findOperatorById(operatorNo);
        if (!operatorService.validateUsedSpace(curOper, file.getSize())){
        	return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:'spaceSizeLimit'}";
        }
        
		boolean isok = FileUpload.saveFile(file, rootPath + dirPath, fileName);
		if (isok){
			operatorService.calculateUsedSpace(curOper, new File(rootPath + dirPath + fileName), true);
		}
		return "{result:" + isok + ",filePath:'" + filePath + "',fileName:'"
				+ Filename + "',desc:''}";
	}

	@RequestMapping(value = "/templateDeleteFile.action")
	@ResponseBody
	public String templateDeleteFile(HttpServletRequest request, Long topicId,
			@RequestParam("filePath") String filePath) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");
		String str = "删除模板文件";
		FileUtils.delFile(rootPath + filePath);
	    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
	    File file = new File(rootPath + filePath);
	    operatorService.calculateUsedSpace(operator, file, false);
		logService.logToDB(request, str, LogUtil.LOG_INFO, true, true);
		return "";
	}

	@RequestMapping(value = "/preview.action")
	@ResponseBody
	public String preview(HttpServletRequest request, ModelMap modelMap,
			Long resourceId, Integer type) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext()
				.getRealPath("/");

		boolean result = false;
		String filePath = "";
		try {

			switch (TemplateType.getType(type)) {
			case TOPIC:
				filePath = previewTopic(curOper, rootPath, resourceId, type);
				break;
			case ALBUM:
				filePath = previewAlbum(curOper, rootPath, resourceId, type);
				break;
			case COLUMN:
				filePath = previewColumn(curOper, rootPath, resourceId, type);
				break;
			case ARTICLE:
				filePath = previewArticle(curOper, rootPath, resourceId, type);
				break;
			}

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "{result: '" + result + "', file : '" + filePath + "'}";
	}

	private String previewTopic(Operator curOper, String rootPath,
			Long resourceId, Integer type) throws Exception {
		String previewFile = "";
		String previewPath = "";
		Map<String, Object> htmlMap = new HashMap<String, Object>();

		Topic topic = topicService.findTopicById(resourceId);
		Template template = templateService.findTemplateById(topic
				.getTemplateId());
		previewPath = template.getFilePath().substring(0,
				template.getFilePath().lastIndexOf(Delimiters.DOT));
		String destPath = rootPath + previewPath;
		FileUtils.delFolder(destPath);
		FileUtils.unZipFiles(new File(rootPath + template.getFilePath()),
				destPath);
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				template.getFileName().substring(0,
						template.getFileName().indexOf(Delimiters.DOT)))
				.append(Delimiters.UNDERLINE).append(type)
				.append(Delimiters.UNDERLINE).append(topic.getId())
				.append(".html");
		previewFile = buffer.toString();
		htmlMap.put("pagetitle", topic.getTopicName());
		htmlMap.put("backUrl", "{href:\"\"}");

		File[] xmlFileList = FileUtils.getFileList(destPath, ".xml");
		Map<String, String> map = XmlUtils
				.readTemplateConfigFile(xmlFileList[0]);
		for (String name : map.keySet()) {

			switch (ComponentType.getType(name)) {
			case backGround:
				htmlMap.put(name, map.get(name));
				break;
			case pagetitle:
				break;
			case backUrl:
				break;
			case GTC:
				String value = map.get(name);
				if (StringUtils.isNotEmpty(value) && !"[]".equals(value)) {

					Album search = new Album();
					search.setCurOper(curOper);
					search.setStatus(AuditStatus.AUDIT_PASS);
					if (topic.getStatus() == AuditStatus.PUBLISH) {
						search.setStatus(AuditStatus.PUBLISH);
					}
					switch (OperatorType.getType(curOper.getType())) {
					case SUPER_ADMIN:
						break;
					case COMPANY_ADMIN:
						search.setGroupId(curOper.getOperatorNo());
						break;
					case ORDINARY_OPER:
						search.setOperatorNo(curOper.getOperatorNo());
						List<Long> allocResourceIds = resourceAllocationService
								.findAllocResourceIds(
										ResourceAllocation.ResourceType.ALBUM
												.getIndex(), curOper
												.getOperatorNo());
						search.setAllocResourceIds(allocResourceIds);
						break;
					}

					List<GTCInfo> dataList = new ArrayList<GTCInfo>();
					List<Album> albumList = null;
					if (topic.getStatus() == AuditStatus.PUBLISH) {
						albumList = albumService.findAllTopicPublishAlbums(
								search, EntityType.TYPE_TOPIC, topic.getId(),
								EntityType.TYPE_ALBUM);
					} else {
						albumList = albumService.findAllResourceAlbums(EntityType.TYPE_TOPIC, resourceId, search);
					}

					Iterator<Album> it = albumList.iterator();
					while (it.hasNext()) {
						Album album = it.next();
						GTCInfo gtcInfo = new GTCInfo();
						List<CoverInfo> coverList = new ArrayList<CoverInfo>();
						gtcInfo.setCover(coverList);
						gtcInfo.setTitle(album.getAlbumName());
						CoverInfo coverInfo = new CoverInfo();
						coverInfo.setImageUrl(portalUrl + "/"
								+ album.getAlbumCover());
						coverList.add(coverInfo);
						dataList.add(gtcInfo);
					}

					String json = JSON.toJSONString(dataList);
					htmlMap.put(name, json);
				} else {
					htmlMap.put(name, value);
				}
				break;
			case buttonItem:
				htmlMap.put(name, "[]");
				break;
			case PicList:
				htmlMap.put(name, "[]");
				break;
			case Text:
				htmlMap.put(name, "[]");
				break;
			}

		}

		PageCreator.getInstance().writePage(htmlMap,
				template.getTemplateName(), destPath + "/" + previewFile,
				destPath);
		return previewPath + "/" + previewFile;
	}

	private String previewAlbum(Operator curOper, String rootPath,
			Long resourceId, Integer type) throws Exception {
		String previewFile = "";
		Template template = null;
		String previewPath = "";
		Map<String, Object> htmlMap = new HashMap<String, Object>();

		Picture search = new Picture();
		search.setCurOper(curOper);
		search.setAlbumNo(resourceId);
		search.setStatus(PictureStatus.AUDIT_PASS.getIndex());
		Album album = albumService.findAlbumById(resourceId);
		if (album.getStatus() == AuditStatus.PUBLISH) {
			search.setStatus(PictureStatus.PUBLISH.getIndex());
		}
		template = templateService.findTemplateById(album.getTemplateId());

		previewPath = template.getFilePath().substring(0,
				template.getFilePath().lastIndexOf(Delimiters.DOT));
		String destPath = rootPath + previewPath;
		FileUtils.delFolder(destPath);
		FileUtils.unZipFiles(new File(rootPath + template.getFilePath()),
				destPath);
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				template.getFileName().substring(0,
						template.getFileName().indexOf(Delimiters.DOT)))
				.append(Delimiters.UNDERLINE).append(type)
				.append(Delimiters.UNDERLINE).append(album.getAlbumNo())
				.append(".html");
		previewFile = buffer.toString();
		htmlMap.put("pagetitle", album.getAlbumName());
		htmlMap.put("backUrl", "{href:\"\"}");

		File[] xmlFileList = FileUtils.getFileList(destPath, ".xml");
		Map<String, String> map = XmlUtils
				.readTemplateConfigFile(xmlFileList[0]);
		for (String name : map.keySet()) {

			switch (ComponentType.getType(name)) {
			case backGround:
				htmlMap.put(name, map.get(name));
				break;
			case pagetitle:
				break;
			case backUrl:
				break;
			case GTC:
				htmlMap.put(name, "[]");
				break;
			case buttonItem:
				htmlMap.put(name, "[]");
				break;
			case PicList:
				String value = map.get(name);
				if (StringUtils.isNotEmpty(value) && !"[]".equals(value)) {
					List<PicListInfo> dataList = new ArrayList<PicListInfo>();
					List<Picture> pictureList = pictureService
							.findAllPictures(search);
					Iterator<Picture> it = pictureList.iterator();
					while (it.hasNext()) {
						Picture picture = it.next();
						PicListInfo data = new PicListInfo();
						data.setImageUrl(portalUrl + "/" + picture.getPicPath());
						data.setImageTitle(picture.getPicName());
						data.setImageId(String.valueOf(picture.getId()));
						data.setImageDesc(picture.getPicDesc());
						data.setHref(Delimiters.WELLNO);
						dataList.add(data);
					}

					String json = JSON.toJSONString(dataList);
					htmlMap.put(name, json);
				}
				else {
					htmlMap.put(name, value);
				}
				break;
			case Text:
				htmlMap.put(name, "[]");
				break;
			}

		}

		PageCreator.getInstance().writePage(htmlMap,
				template.getTemplateName(), destPath + "/" + previewFile,
				destPath);
		return previewPath + "/" + previewFile;
	}

	private String previewColumn(Operator curOper, String rootPath,
			Long resourceId, Integer type) throws Exception {
		String previewFile = "";
		String previewPath = "";
		Map<String, Object> htmlMap = new HashMap<String, Object>();

		Column column = columnService.selectByNo(resourceId);
		Template template = templateService.findTemplateById(column
				.getTemplateId());
		previewPath = template.getFilePath().substring(0,
				template.getFilePath().lastIndexOf(Delimiters.DOT));
		String destPath = rootPath + previewPath;
		FileUtils.delFolder(destPath);
		FileUtils.unZipFiles(new File(rootPath + template.getFilePath()),
				destPath);
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				template.getFileName().substring(0,
						template.getFileName().indexOf(Delimiters.DOT)))
				.append(Delimiters.UNDERLINE).append(type)
				.append(Delimiters.UNDERLINE).append(column.getColumnNo())
				.append(".html");
		previewFile = buffer.toString();
		htmlMap.put("pagetitle", column.getColumnName());
		htmlMap.put("backUrl", "{href:\"\"}");

		File[] xmlFileList = FileUtils.getFileList(destPath, ".xml");
		Map<String, String> map = XmlUtils
				.readTemplateConfigFile(xmlFileList[0]);
		for (String name : map.keySet()) {
			switch (ComponentType.getType(name)) {
			case backGround:
				htmlMap.put(name, map.get(name));
				break;
			case pagetitle:
				break;
			case backUrl:
				break;
			case GTC:
				String value = map.get(name);
				if (StringUtils.isNotEmpty(value) && !"[]".equals(value)) {
					Article search = new Article();
					search.setCurOper(curOper);
					search.setColumnNo(column.getColumnNo());
					search.setStatus(ArticleStatus.AUDIT_PASS.getIndex());
					
					if(column.getStatus()==null) {
						throw new ResultCodeException(ResultCode.INVALID_PARAM, "column status null");
					}
					
					if (column.getStatus() == AuditStatus.PUBLISH) {
						search.setStatus(ArticleStatus.PUBLISH.getIndex());
					}
					switch (OperatorType.getType(curOper.getType())) {
					case SUPER_ADMIN:
						break;
					case COMPANY_ADMIN:
						search.setGroupId(curOper.getOperatorNo());
						break;
					case ORDINARY_OPER:
						search.setOperatorNo(curOper.getOperatorNo());
						List<Long> allocResourceIds = resourceAllocationService
								.findAllocResourceIds(
										ResourceAllocation.ResourceType.ALBUM
												.getIndex(), curOper
												.getOperatorNo());
						search.setAllocResourceIds(allocResourceIds);
						break;
					}

					List<GTCInfo> dataList = new ArrayList<GTCInfo>();
					if(column.getStatus()==null) {
						throw new ResultCodeException(ResultCode.INVALID_PARAM, "column status null");
					}
					
					if (column.getStatus() == AuditStatus.PUBLISH) {
						List<Article> articleList = articleService.findAllColumnPublishArticles(search,EntityType.TYPE_COLUMN, 
								column.getColumnNo(), EntityType.TYPE_ARTICLE);
						Iterator<Article> it = articleList.iterator();
						while (it.hasNext()) {
							Article article = it.next();
							GTCInfo gtcInfo = new GTCInfo();
							gtcInfo.setTitle(article.getTitle());
							gtcInfo.setSubTitle(article.getTitle2());
							gtcInfo.setDataId(String.valueOf(article.getArticleNo()));

							List<CoverInfo> coverList = new ArrayList<CoverInfo>();
							gtcInfo.setCover(coverList);
							Article tempArt = articleService.selectByNoWithPictures(article);
							List<Picture> pictureList = tempArt.getPictures();
							if (pictureList != null && pictureList.size() > 0) {
								for (Picture pic : pictureList) {
									CoverInfo coverInfo = new CoverInfo();
									coverInfo.setImageUrl(portalUrl
											+ "/textmgmt/article/view/"
											+ pic.getPicPath());
									coverList.add(coverInfo);
								}

							}
							dataList.add(gtcInfo);
						}
					} 
					else {
						List<ColumnArticleMap> columnArticleMapList = columnArticleMapService.selectColumnArticles(search);
						Iterator<ColumnArticleMap> it = columnArticleMapList.iterator();
						while (it.hasNext()) {
							Article article = it.next().getArticle();
							GTCInfo gtcInfo = new GTCInfo();
							gtcInfo.setTitle(article.getTitle());
							gtcInfo.setSubTitle(article.getTitle2());
							gtcInfo.setDataId(String.valueOf(article.getArticleNo()));

							List<CoverInfo> coverList = new ArrayList<CoverInfo>();
							gtcInfo.setCover(coverList);
							Article tempArt = articleService.selectByNoWithPictures(article);
							List<Picture> pictureList = tempArt.getPictures();
							if (pictureList != null && pictureList.size() > 0) {
								for (Picture pic : pictureList) {
									CoverInfo coverInfo = new CoverInfo();
									coverInfo.setImageUrl(portalUrl
											+ "/textmgmt/article/view/"
											+ pic.getPicPath());
									coverList.add(coverInfo);
								}

							}
							dataList.add(gtcInfo);
						}
					}

					String json = JSON.toJSONString(dataList);
					htmlMap.put(name, json);
				} else {
					htmlMap.put(name, value);
				}
				break;
			case buttonItem:
				value = map.get(name);
				if (StringUtils.isNotEmpty(value) && !"[]".equals(value)) {
					List<ButtonItemInfo> dataList = new ArrayList<ButtonItemInfo>();
					ButtonItemInfo data = new ButtonItemInfo();
					data.setDataId(String.valueOf(column.getColumnNo()));
					data.setContent(column.getColumnName());
					data.setHref(Delimiters.WELLNO);
					dataList.add(data);
					String json = JSON.toJSONString(dataList);
					htmlMap.put(name, json);
				} 
				else {
					htmlMap.put(name, value);
				}

				break;
			case PicList:
				htmlMap.put(name, "[]");
				break;
			case Text:
				htmlMap.put(name, "[]");
				break;
			}

		}

		PageCreator.getInstance().writePage(htmlMap,
				template.getTemplateName(), destPath + "/" + previewFile,
				destPath);
		return previewPath + "/" + previewFile;
	}

	private String previewArticle(Operator curOper, String rootPath,
			Long resourceId, Integer type) throws Exception {
		String previewFile = "";
		Template template = null;
		String previewPath = "";
		Map<String, Object> htmlMap = new HashMap<String, Object>();

		Article search = new Article();
		search.setArticleNo(resourceId);
		Article article = articleService.selectByNoWithPictures(search);
		template = templateService.findTemplateById(article.getTemplateId());

		previewPath = template.getFilePath().substring(0, template.getFilePath().lastIndexOf(Delimiters.DOT));
		String destPath = rootPath + previewPath;
		FileUtils.delFolder(destPath);
		FileUtils.unZipFiles(new File(rootPath + template.getFilePath()), destPath);
		StringBuffer buffer = new StringBuffer();
		buffer.append(template.getFileName().substring(0, template.getFileName().indexOf(Delimiters.DOT)))
			.append(Delimiters.UNDERLINE).append(type)
			.append(Delimiters.UNDERLINE).append(article.getArticleNo())
			.append(".html");
		previewFile = buffer.toString();
		htmlMap.put("pagetitle", article.getArticleName());
		htmlMap.put("backUrl", "{href:\"\"}");

		File[] xmlFileList = FileUtils.getFileList(destPath, ".xml");
		Map<String, String> map = XmlUtils.readTemplateConfigFile(xmlFileList[0]);
		for (String name : map.keySet()) {

			switch (ComponentType.getType(name)) {
			case backGround:
				htmlMap.put(name, map.get(name));
				break;
			case pagetitle:
				break;
			case backUrl:
				break;
			case GTC:
				htmlMap.put(name, "[]");
				break;
			case buttonItem:
				htmlMap.put(name, "[]");
				break;
			case PicList:
				String value = map.get(name);
				if (StringUtils.isNotEmpty(value) && !"[]".equals(value)) {
					if (article.getPictures() != null && article.getPictures().size() > 0){
						List<PicListInfo> dataList = new ArrayList<PicListInfo>();
						Iterator<Picture> it = article.getPictures().iterator();
						while (it.hasNext()) {
							Picture picture = it.next();
							PicListInfo data = new PicListInfo();
							data.setImageUrl(portalUrl + "/textmgmt/article/view/" + picture.getPicPath());
							data.setImageTitle(picture.getPicName());
							data.setImageId(String.valueOf(picture.getId()));
							data.setImageDesc(picture.getPicDesc());
							data.setHref(Delimiters.WELLNO);
							dataList.add(data);
						}

						String json = JSON.toJSONString(dataList);
						htmlMap.put(name, json);
					}
					else{
						htmlMap.put(name, value);
					}

				}
				else {
					htmlMap.put(name, value);
				}
				
				break;
			case Text:
			    value = map.get(name);
				if (StringUtils.isNotEmpty(value) && !"[]".equals(value)) {
					List<TextInfo> dataList = new ArrayList<TextInfo>();
					TextInfo data = new TextInfo();
					data.setTitle(article.getTitle());
					data.setSubTitle(article.getTitle2());
					String text = FileUtils.readFileContent(articleDir	+ article.getBody(), "UTF-8");
					data.setText(text);
					data.setHref(Delimiters.WELLNO);
					dataList.add(data);
					String json = JSON.toJSONString(dataList);
					htmlMap.put(name, json);
				}
				else {
					htmlMap.put(name, value);
				}
				break;
			}

		}

		PageCreator.getInstance().writePage(htmlMap,
				template.getTemplateName(), destPath + "/" + previewFile,
				destPath);
		return previewPath + "/" + previewFile;
	} 
	
}
