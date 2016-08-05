
package com.db.bms.controller.picmgmt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Company;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Template;
import com.db.bms.entity.Topic;
import com.db.bms.entity.TopicColumn;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.Template.TemplateStatus;
import com.db.bms.entity.Template.TemplateType;
import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.service.AlbumService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.PictureService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.service.TemplateService;
import com.db.bms.service.TopicColumnService;
import com.db.bms.service.TopicService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.MD5Engine;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("picmgmt/topic")
@Controller
public class TopicController {

	private final static Logger logger = Logger.getLogger(TopicController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private PictureService pictureService;
	
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	@Autowired
	private ResourcePublishMapService resourcePublishMapService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private TopicColumnService topicColumnService;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private PortalProcessor portalProcessor;  
	
	@RequestMapping(value = "/topicList.action")
	public String topicList(HttpServletRequest request, ModelMap modelMap, Topic search)
			throws Exception {  
		boolean queryFlag = false;
		if(search.getOperatorNo() != null || (search.getTopicName() != null && !search.getTopicName().isEmpty()) || search.getType() != null || search.getCaptureFlag() != null || search.getStatus() != null){
			queryFlag = true;
		}
		//获取登录人
        Operator curOper = SessionUtil.getActiveOperator(request);   
        Long opNo = curOper.getOperatorNo();
        if(curOper.getType().intValue() == 2){
        	opNo = curOper.getOperator().getCreateBy();
        }
        List<Topic> list = new ArrayList<Topic>();
        List<Long> resAllocIdList = null;
        search.setCurOper(curOper); 
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:   
        	break;
        case COMPANY_ADMIN:
        	if((queryFlag && search.getOperatorNo() == null) || !queryFlag){
	        	search.setGroupId(curOper.getOperatorNo());
	        	search.setOperatorNo(curOper.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getOperatorNo()));
        	}
        	else{
        		search.setOperatorNo(search.getOperatorNo());
        	}
        	break;
        case ORDINARY_OPER:
        	if((queryFlag && search.getOperatorNo() == null)||!queryFlag){
	        	search.setGroupId(curOper.getCreateBy());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getCreateBy()));
        	}
        	else{
        		search.setOperatorNo(search.getOperatorNo());
        	}
        	break;
        } 
        list = topicService.findTopics(search);
        //获取被分配专题对应当前操作员的权限，并设置到topic的cmds字段中，返回到页面中进行呈现时的权限控制
        resAllocIdList = search.getAllocResourceIds();
        if(resAllocIdList != null && !resAllocIdList.isEmpty()){
        	List<ResourceAllocation> resAolList = this.resourceAllocationService.findAllocResourceByIds((Long[])resAllocIdList.toArray(new Long[resAllocIdList.size()]));
        	if(resAolList != null && !resAolList.isEmpty() && list != null && !list.isEmpty()){
        		for (Topic topic : list) {
        			topic.setCmds(null);
        		}
        		for (Topic topic : list) {
        			for (ResourceAllocation resourceAllocation : resAolList) {
						if(opNo.longValue() == resourceAllocation.getOperatorNo().longValue() && resourceAllocation.getResourceId().longValue() == topic.getId().longValue()){
							if(resourceAllocation.getCmdStr() == null || resourceAllocation.getCmdStr().isEmpty()){
								topic.setCmds("");
		        			}
							else{
								topic.setCmds(resourceAllocation.getCmdStr());
							}
						}
        			}
				}
        	}
        }
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
        modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		if(!queryFlag){
			search.setOperatorNo(null);
		}
		modelMap.put("search", search);
		modelMap.put("topicTypeMap", ConstConfig.topicTypeMap);
		modelMap.put("topicStatusMap", ConstConfig.topicStatusMap);
		modelMap.put("captureFlagMap", ConstConfig.captureFlagMap);
		return "picmgmt/topic/topicList";
	}
	
    @RequestMapping(value = "/topicEdit.action")
    public String topicEdit(HttpServletRequest request, ModelMap modelMap, Topic search, Long topicId) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);  
    	Topic topic = new Topic();
    	if (topicId != null && topicId > 0){
    		topic = topicService.findTopicById(topicId);
    	}
    	List<Company> companyList = companyService.findAllCompanys(curOper);
    	
    	Template tsearch = new Template();
    	tsearch.setType(TemplateType.TOPIC.getIndex());
    	tsearch.setStatus(TemplateStatus.ENABLE.getIndex());
    	List<Template> templateList = templateService.findAllTemplates(tsearch); 
    	modelMap.put("topic", topic);
    	modelMap.put("search", search);
    	modelMap.addAttribute("companyList", companyList);
    	modelMap.addAttribute("templateList", templateList);
		modelMap.put("topicTypeMap", ConstConfig.topicTypeMap);
		modelMap.put("topicStatusMap", ConstConfig.topicStatusMap);
		modelMap.put("captureFlagMap", ConstConfig.captureFlagMap);
    	return "picmgmt/topic/topicEdit";
    }

	@RequestMapping(value = "/saveOrUpdateTopic.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateTopic(HttpServletRequest request,
			HttpServletResponse response, Topic topic) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		boolean result = false;
		String logStr = "";
		try {
			
			if (CaptureFlag.getFlag(topic.getCaptureFlag()) == CaptureFlag.YES){
			    Topic search = new Topic();
		     	search.setCaptureFlag(CaptureFlag.YES.getIndex());
			    List<Topic> tempList = topicService.findAllTopics(search);
			    if (tempList != null && tempList.size() > 0){
			    	if (topic.getId() == null || topic.getId().doubleValue() != tempList.get(0).getId().doubleValue()){
				    	return "{result: '" + result + "', desc : 'capture'}";
			    	}
			    }
			}
			
			if (topic.getCompanyNo() == null){
				topic.setCompanyNo(curOper.getCompanyNo());
			}
			
			if (StringUtils.isNotEmpty(topic.getTopicCover())){
				//String rootPath = request.getSession(true).getServletContext().getRealPath("/");
				MD5Engine engine = new MD5Engine(false);
				String md5 = engine.calculateMD5(rootPath + topic.getTopicCover());
				topic.setCheckCode(md5);
			}
			
			if (topic.getId() != null && topic.getId() > 0) {
				logStr = "更新[" + topic.getTopicName() + "]专题";
				topic.setStatus(AuditStatus.DRAFT);
				topicService.updateTopic(topic);
			} else {
				logStr = "添加[" + topic.getTopicName() + "]专题";
				topic.setTopicId(StringUtils.generateFixPrefixId("TP"));
				topic.setStatus(AuditStatus.DRAFT);
				topic.setOperatorNo(curOper.getOperatorNo());
				topic.setGroupId(curOper.getCreateBy());
				topicService.addTopic(topic);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (topic.getId() == null) {
				logger.error("Add topic exception occurred, cause by:{}", e);
			} else {
				logger.error("Update topic exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/topicDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String ftpServerDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] topicIds) throws Exception {
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		boolean result = false;
		String str = "";
		try {
			
			if (topicIds != null && topicIds.length > 0) {

				List<Topic> list = topicService.findTopicsById(topicIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getTopicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getTopicName();
				topicService.deleteTopics(topicIds);
				
				for (Topic topic : list){
					if (StringUtils.isNotEmpty(topic.getTopicCover())){
						Operator curOper = SessionUtil.getActiveOperator(request);
					    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
					    File file = new File(rootPath + topic.getTopicCover());
					    operatorService.calculateUsedSpace(operator, file, false);
					    
						FileUtils.delFile(rootPath + topic.getTopicCover());
					}
				}

			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]专题", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]专题", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete topic exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/topicDetail.action")
	public String topicDetail(HttpServletRequest request, ModelMap modelMap,
			Long topicId) throws Exception {

		Topic topic = this.topicService.findTopicById(topicId);
		
		ResourcePublishMap publish = null;
		List<ResourcePublishMap> list = resourcePublishMapService.findResourcePublishMapById(null, null, EntityType.TYPE_TOPIC, topicId);
		if (list != null && list.size() > 0){
			publish = list.get(0);
		}

		modelMap.put("publish", publish);
		modelMap.put("topic", topic);
		modelMap.put("topicTypeMap", ConstConfig.topicTypeMap);
		modelMap.put("topicStatusMap", ConstConfig.topicStatusMap);
		modelMap.put("captureFlagMap", ConstConfig.captureFlagMap);
		return "picmgmt/topic/topicDetail";
	}
	
	@RequestMapping(value = "/topicAudit.action", method = RequestMethod.POST)
	@ResponseBody
	public String topicAudit(HttpServletRequest request,
			Integer status, Long[] topicIds) throws Exception {
		boolean result = false;
		String str = "";
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		ResourcePublishMap publish = new ResourcePublishMap();
		publish.setCreatedBy(createdBy);
		try {
			if (topicIds != null && topicIds.length > 0) {
				List<Topic> list = topicService.findTopicsById(topicIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getTopicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getTopicName();
				topicService.auditTopic(status, topicIds, false, false,publish);

			}
			result = true;
			switch (status) {
			case AuditStatus.AUDITING:
				logService.logToDB(request, "专题[" + str + "]提交审核",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.AUDIT_PASS:
				logService.logToDB(request, "专题[" + str + "]审核通过",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.AUDIT_NO_PASS:
				logService.logToDB(request, "专题[" + str + "]审核不通过",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.PUBLISH:
				logService.logToDB(request, "专题[" + str + "]发布",
						LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.UNPUBLISH:
				logService.logToDB(request, "专题[" + str + "]取消发布",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (status) {
			case AuditStatus.AUDITING:
				logService.logToDB(request, "专题[" + str + "]提交审核",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Topic on submit audit exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.AUDIT_PASS:
				logService.logToDB(request, "专题[" + str + "]审核通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Topic on audit pass exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.AUDIT_NO_PASS:
				logService.logToDB(request, "专题[" + str + "]审核不通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Topic on audit no pass exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.PUBLISH:
				logService.logToDB(request, "发布专题[" + str + "]",LogUtil.LOG_INFO, false, true);
				logger.error("Topic on publish exception occurred, cause by:{}", e);
				break;
			case AuditStatus.UNPUBLISH:
				logService.logToDB(request, "取消发布专题[" + str + "]", LogUtil.LOG_INFO, false, true);
				logger.error("Topic on unpublish exception occurred, cause by:{}", e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/topicUploadCover.action")
	@ResponseBody
	public String topicUploadCover(HttpServletRequest request, @RequestParam("Filedata")MultipartFile file, @RequestParam("Filename")String Filename
			, Long operatorNo,Long topicId) throws Exception{
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        String fileName = FileUpload.geneFileName(Filename);
        String dirPath =  ConstConfig.UPLOAD_PATH + ConstConfig.TOPIC_FILE_PATH;
        String filePath = dirPath + fileName;
        Operator curOper = operatorService.findOperatorById(operatorNo);
        if (!operatorService.validateUsedSpace(curOper, file.getSize())){
        	return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:'spaceSizeLimit'}";
        }
        
        boolean isok = FileUpload.saveFile(file, rootPath + dirPath, fileName);
		if (topicId != null){
			MD5Engine engine = new MD5Engine(false);
			String md5 = engine.calculateMD5(rootPath + dirPath + fileName);
			Topic topic = topicService.findTopicById(topicId);
			topic.setCheckCode(md5);
			topic.setTopicCover(filePath);
			topicService.updateTopic(topic);
		}
		
		//上传成功计算使用空间
		if (isok){
			operatorService.calculateUsedSpace(curOper, new File(rootPath + dirPath + fileName), true);
		}

		return "{result:" + isok + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:''}";
	}
	
	@RequestMapping(value = "/topicDeleteCover.action")
	@ResponseBody
	public String topicDeleteCover(HttpServletRequest request,Long topicId,@RequestParam("filePath")String filePath) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        String str = "删除专题封面";
        if (topicId != null){
			Topic topic = topicService.findTopicById(topicId);
			topic.setCheckCode(null);
			topic.setTopicCover(null);
			topicService.updateTopic(topic);
			str = "删除专题【" + topic.getTopicName() + "】封面";
			
		    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
		    File file = new File(rootPath + filePath);
		    operatorService.calculateUsedSpace(operator, file, false);
        }
        FileUtils.delFile(rootPath + filePath);
        logService.logToDB(request, str, LogUtil.LOG_INFO, true, true);
        return "";
	}

	@RequestMapping(value = "/topicAlbumSelect.action")
	public String topicAlbumSelect(HttpServletRequest request,
			ModelMap modelMap, Long topicId, Album search,String cmdStr) throws Exception {

		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        search.getPageUtil().setPaging(true);
        Topic topic = this.topicService.findTopicById(topicId);
        List<Album> list = topicService.findResourceAlbums(EntityType.TYPE_TOPIC, topicId, search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("topicId", topicId);
		Long createBy = null;
		Operator op = topic.getOperator();
		createBy = op.getOperatorNo();
		if(op.getType().intValue() == 2){
			createBy = op.getCreateBy();
		}
		modelMap.put("topicCreatedBy", createBy);
		modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
		if(cmdStr != null){
			SessionUtil.setAttr(request, "cmdStr", cmdStr);
		}
		return "picmgmt/album/topicAlbumSelect";
	}

	@RequestMapping(value = "/topicAlbumNoSelect.action")
	public String topicAlbumNoSelect(HttpServletRequest request,
			ModelMap modelMap, Long topicId, Album search) throws Exception {

		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	search.setOperatorNo(curOper.getOperatorNo());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getOperatorNo()));
        	break;
        case ORDINARY_OPER:
        	search.setGroupId(curOper.getCreateBy());
        	search.setOperatorNo(curOper.getCreateBy());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getCreateBy()));
        	break;
        }
        Topic topic = this.topicService.findTopicById(topicId);
        search.setCaptureFlag(topic.getCaptureFlag());
        search.setStatus(2);
        search.setTemplateId(-1l);
        List<Album> list = topicService.findResourceAlbumsNoSelect(EntityType.TYPE_TOPIC, topicId, search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("topicId", topicId);
		modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
		return "picmgmt/album/topicAlbumNoSelect";
	}
	
	

	@RequestMapping(value = "/saveTopicAlbum.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveTopicAlbum(HttpServletRequest request,
			Long[] albumNos, Long topicId) throws Exception {
		boolean result = false;
		String str = "";
		Topic topic = null;
		Operator curOper = SessionUtil.getActiveOperator(request);
		OperatorType opType = OperatorType.getType(curOper.getType());
		long createdByNo = -1;
		switch (opType) {
		case SUPER_ADMIN:
			createdByNo = -1;
			break;
		case COMPANY_ADMIN:
			createdByNo = curOper.getOperatorNo();
			break;
		case ORDINARY_OPER:
			createdByNo = curOper.getCreateBy();
			break;
		default:
			createdByNo = -1;
			break;
		}
		try {
			if (albumNos != null && albumNos.length > 0) {
				List<Album> list = albumService.findAlbumsById(albumNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getAlbumName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getAlbumName();

				topic = topicService.findTopicById(topicId);
				topicService.addResourceAlbumMap(EntityType.TYPE_TOPIC,topicId, albumNos,createdByNo);
			}
			result = true;
			logService.logToDB(request, "专题[" + topic.getTopicName() + "]关联["
					+ str + "]相册", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "专题[" + topic.getTopicName() + "]关联["
					+ str + "]相册", LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error(
					"Topic associated albums exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/topicAlbumDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String topicAlbumDelete(HttpServletRequest request,
			Long[] albumNos, Long topicId) throws Exception {
		boolean result = false;
		String str = "";
		Topic topic = null;
		try {
			if (albumNos != null && albumNos.length > 0) {
				List<Album> list = albumService.findAlbumsById(albumNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getAlbumName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getAlbumName();
				topic = topicService.findTopicById(topicId);
				topicService.deleteResourceAlbumMapsByAlbumId(EntityType.TYPE_TOPIC,topicId, albumNos);
				
            	List<PortalPublishNotice> notices = new ArrayList<PortalPublishNotice>();
            	
            	albumService.unpublishAlbum(topicId, albumNos, notices);
            	
            	/* to remove, following code is replaced by albumService.unpublishAlbum() 
            	//hzm modify 20160616
				List<Long> tmp = new ArrayList<Long>();
				int ii;
				for (ii = 0; ii < albumNos.length; ii++) {
					List<ResourcePublishMap> albumPublishList = resourcePublishMapMapper.findResourcePublishMapByResId(
							EntityType.TYPE_TOPIC, topicId, EntityType.TYPE_ALBUM, albumNos[ii]);
					if (albumPublishList != null && albumPublishList.size() > 0){
						tmp.add(albumPublishList.get(0).getId());
					}
					
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setParentType(EntityType.TYPE_TOPIC);
					notice.setParentId(topicId);
					notice.setResourceType(EntityType.TYPE_ALBUM);
					notice.setResourceId(albumNos[ii]);
					notices.add(notice);					
				}
				
				if (tmp.size()> 0){
					Long[] ids = tmp.toArray(new Long[0]);
					resourcePublishMapService.deleteResourcePublishMapsById(SessionUtil.getActiveOperator(request), ids);
					
					for (ii = 0; ii < albumNos.length; ii++) {
						List<ResourcePublishMap> otherpub = resourcePublishMapService.findResourcePublishMapById(
								null, null, EntityType.TYPE_ALBUM, albumNos[ii]);
						// 如果相册没有其他发布，删除发布的图片
						if(ArrayUtils.getSize(otherpub)==0) {
							otherpub = resourcePublishMapService.findResourcePublishMapById(EntityType.TYPE_ALBUM, albumNos[ii],
									EntityType.TYPE_PICTURE, null);
							if(otherpub!=null) {
								for(ResourcePublishMap m2: otherpub) {
									PortalPublishNotice notice = new PortalPublishNotice();
									notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
									notice.setParentType(EntityType.TYPE_ALBUM);
									notice.setParentId(albumNos[ii]);
									notice.setResourceType(m2.type);
									notice.setResourceId(m2.resourceId);
									notices.add(notice);	
								}
							}
							this.resourcePublishMapService.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, albumNos[ii],
									EntityType.TYPE_PICTURE, null);
						}
					}
				}
				*/
				
				if(notices.size()>0) {
					portalProcessor.putNoticeToQueue(notices);
				}
				
				TopicColumn topicColumn = new TopicColumn();
				topicColumn.setTopicId(topicId);
				List<TopicColumn> topicColumns = this.topicColumnService.findColumns(topicColumn);
				if(topicColumns != null && !topicColumns.isEmpty()){
					for (TopicColumn tc : topicColumns) {
						topicService.deleteResourceAlbumMapsByAlbumId(EntityType.TYPE_MENU,tc.getId(), albumNos);
					}
				}
			}
			result = true;
			logService.logToDB(request, "删除专题[" + topic.getTopicName() + "]相册[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除专题[" + topic.getTopicName() + "]相册[" + str + "]", LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error("Delete topic associated albums exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/toTopicPublish.action")
	public String toTopicPublish(HttpServletRequest request,
			ModelMap modelMap, String topicIds) throws Exception {
		modelMap.put("topicIds", topicIds);
		return "picmgmt/topic/topicPublish";
	}
	
	@RequestMapping(value = "/topicPublish.action")
	@ResponseBody
	public String topicPublish(HttpServletRequest request,
			ModelMap modelMap, String topicIds,Boolean acascade, Boolean pcascade,ResourcePublishMap publish) throws Exception {

		boolean result = false;
		String str = "";
		try {
			Operator curOper = SessionUtil.getActiveOperator(request);
			Long createdBy = curOper.getOperatorNo();
			if(curOper.getOperator().getType().intValue() == 2){
				createdBy = curOper.getOperator().getCreateBy();
			}
			publish.setCreatedBy(createdBy);
			String[] tempIdArr = topicIds.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			if (ids != null && ids.length > 0) {
				List<Topic> list = topicService.findTopicsById(ids);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getTopicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getTopicName();
				topicService.auditTopic(AuditStatus.PUBLISH, ids, acascade, pcascade,publish);
			}
			
			processor.putNoticeToQueue(publish.getNoticeList());

			result = true;
			logService.logToDB(request, "发布专题[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "发布专题[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error(
					"Publish topics exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}

	
	@RequestMapping(value = "/toTopicUnPublish.action")
	public String toTopicUnPublish(HttpServletRequest request,
			ModelMap modelMap, String topicIds) throws Exception {

		modelMap.put("topicIds", topicIds);
		return "picmgmt/topic/topicUnPublish";
	}
	
	@RequestMapping(value = "/topicUnPublish.action")
	@ResponseBody
	public String topicUnPublish(HttpServletRequest request,
			ModelMap modelMap, String topicIds,Boolean acascade) throws Exception {

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
			String[] tempIdArr = topicIds.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			if (ids != null && ids.length > 0) {
				List<Topic> list = topicService.findTopicsById(ids);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getTopicName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getTopicName();
				topicService.auditTopic(AuditStatus.UNPUBLISH, ids, acascade, false,publish);
			}
			
			processor.putNoticeToQueue(publish.getNoticeList());

/*			if (pcascade){
				notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
				notice.setResourceType(PublishNoticeREQT.ResouceType.Image.getType());
				processor.putNoticeToQueue(notice);
			}*/
			
			result = true;
			logService.logToDB(request, "取消发布专题[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "取消发布专题[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error("Cancel publish topics exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/publishMgmt.action")
	public String publishMgmt(HttpServletRequest request,
			ModelMap modelMap, Long topicId) throws Exception {
		modelMap.put("topicId", topicId);
		
		ResourcePublishMap publish = null;
		List<ResourcePublishMap> list = resourcePublishMapService.findResourcePublishMapById(null,null, EntityType.TYPE_TOPIC, topicId);
		if (list != null && list.size() > 0){
			publish = list.get(0);
		}

		List<Album> albumList = albumService.findAllResourceAlbum(EntityType.TYPE_TOPIC, 
				AuditStatus.PUBLISH, new Long[]{topicId});
		if (albumList != null && albumList.size() > 0){
			publish.setAcascade(true);
			
			for (Album album : albumList){
				Picture picSearch = new Picture();
				picSearch.setAlbumNo(album.getAlbumNo());
				picSearch.setStatus(PictureStatus.PUBLISH.getIndex());
				List<Picture> pictureList= pictureService.findPictures(picSearch);
				if (pictureList != null && pictureList.size() > 0){
					publish.setPcascade(true);
				}
			}
		}
		
		modelMap.put("publish", publish);
		return "picmgmt/topic/publishMgmt";
	}
	
	@RequestMapping(value = "/publishUpdate.action")
	@ResponseBody
	public String publishUpdate(HttpServletRequest request,
			ModelMap modelMap, ResourcePublishMap publish) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		publish.setCreatedBy(createdBy);
		boolean result = false;
		String str = "";
		try {
			Topic topic = topicService.findTopicById(publish.getResourceId());
			str = topic.getTopicName();
			
			topicService.publishUpdate(publish);
/*			PortalPublishNotice notice = new PortalPublishNotice();
			notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
			notice.setResourceType(PublishNoticeREQT.ResouceType.Topic.getType());
			processor.putNoticeToQueue(notice);
			
			notice = new PortalPublishNotice();
			notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
			notice.setResourceType(PublishNoticeREQT.ResouceType.Album.getType());
			processor.putNoticeToQueue(notice);
			
			notice = new PortalPublishNotice();
			notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
			notice.setResourceType(PublishNoticeREQT.ResouceType.Image.getType());
			processor.putNoticeToQueue(notice);*/
			result = true;
			logService.logToDB(request, "重新发布专题[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "重新发布专题[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error(
					"Republish topic exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/resourceTopicNoPublishList.action")
	public String resourceTopicNoPublishList(HttpServletRequest request, ModelMap modelMap, Long resourceId, Topic search)
			throws Exception {

        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIdsFilterCmd(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getOperatorNo(),"2"));
        	break;
        case ORDINARY_OPER:
        	search.setGroupId(curOper.getCreateBy());
        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIdsFilterCmd(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getCreateBy(),"2");
        	search.setAllocResourceIds(allocResourceIds);
        	break;
        }
        Album album = this.albumService.findAlbumById(resourceId);
        if(album != null){
        	search.setCaptureFlag(album.getCaptureFlag());
        }
        search.setStatus(AuditStatus.PUBLISH);
        List<Topic> list = topicService.findResourceTopicNoPublishs(resourceId, search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("resourceId", resourceId);
		modelMap.put("topicTypeMap", ConstConfig.topicTypeMap);
		modelMap.put("topicStatusMap", ConstConfig.topicStatusMap);
		modelMap.put("captureFlagMap", ConstConfig.captureFlagMap);
		return "picmgmt/topic/resourceTopicNoPublishList";
	}
	
	/** 
	 * 查询有专题权限的用户
	 */ 
	@RequestMapping(value = "/findTopicOperator.action")
	public String findTopicOperator(HttpServletRequest request,ModelMap modelMap,Long topicId, Operator search) throws Exception {  
		    List<String> operList = null; 
		    List<Operator> list = new ArrayList<Operator>();  
		    String type = "1"; 
		    Long resourceId = topicId;
			if(topicId != null){
				operList = operatorService.findResourceOperator(resourceId,type); 
			  for(int i=0;i<operList.size();i++){ 
				Long operNo = Long.parseLong(operList.get(i)); 
				list.add(operatorService.findOperatorById(operNo));
			  }
			}
		    modelMap.put("createOperNo", null);
		    modelMap.put("type", null);
		    modelMap.put("resourceId", null);
		    modelMap.put("list", list);  
		    modelMap.put("pageUtil", search.getPageUtil());
            modelMap.put("belongList", null);
            modelMap.put("search", search);
            modelMap.put("userTypeMap", ConstConfig.userTypeMap);
		    return "sysmgmt/user/resourceUserAllocList";
	}  
	
}
