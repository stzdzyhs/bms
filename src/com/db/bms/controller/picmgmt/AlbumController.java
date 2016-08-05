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

import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Company;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Template;
import com.db.bms.entity.Topic;
import com.db.bms.entity.TopicColumn;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Template.TemplateStatus;
import com.db.bms.entity.Template.TemplateType;
import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.entity.TopicColumn.ColumnStatus;
import com.db.bms.service.AlbumService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
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
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("picmgmt/album")
@Controller
public class AlbumController {

	private final static Logger logger = Logger.getLogger(AlbumController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	@Autowired
	private TopicColumnService topicColumnService;
	
	@Autowired
	private ResourcePublishMapService resourcePublishMapService;
	
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(value = "/albumList.action")
	public String albumList(HttpServletRequest request, ModelMap modelMap, Album search)
			throws Exception {
		boolean queryFlag = false;
		if((search.getAlbumName() != null && !search.getAlbumName().isEmpty()) || search.getStatus() != null || search.getCaptureFlag() != null || search.getCompanyNo() != null || search.getOperatorNo() != null){
			queryFlag = true;
		}
		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
		//获取登录人
        Operator curOper = SessionUtil.getActiveOperator(request);    
        search.setCurOper(curOper); 
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
    	Long createdBy = curOper.getOperatorNo();
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	if((queryFlag && search.getOperatorNo() == null) || !queryFlag){
	        	search.setGroupId(curOper.getOperatorNo());
	        	search.setOperatorNo(curOper.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getOperatorNo()));
        	}
        	else{
        		search.setGroupId(search.getOperatorNo());
	        	search.setOperatorNo(search.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), search.getOperatorNo()));
        	}
        	break;
        case ORDINARY_OPER:
        	createdBy = curOper.getCreateBy();
        	if((queryFlag && search.getOperatorNo() == null) || !queryFlag){
	        	search.setGroupId(curOper.getCreateBy());
//	        	search.setOperatorNo(curOper.getCreateBy());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getCreateBy()));
        	}
        	else{
        		search.setGroupId(search.getOperatorNo());
//	        	search.setOperatorNo(search.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), search.getOperatorNo()));
        	}
        	break;
        }
        List<Album> list = albumService.findAlbums(search);
        if(list != null){
        	//根据当前登录操作员与资源创建者的对比，设置是否是分配资源，除admin外，都无权操作分配的资源
        	Long albumOp = null;
        	for (Album album : list) {
        		album.setCmds(null);
				if(album.getOperator() != null && album.getOperator().getType().intValue() == 2){
					albumOp = album.getOperator().getCreateBy();
				}
				else{
					albumOp = album.getOperatorNo();
				}
				if(curOper.isSuperAdmin() || albumOp.longValue() == createdBy.longValue()){
					album.setAllocRes(null);
				}
				else{
					album.setAllocRes(1l);
				}
			}
        	//获取被分配资源的具体权限
        	List<Long> resAllocIdList = null;
        	resAllocIdList = search.getAllocResourceIds();
        	if(resAllocIdList != null && !resAllocIdList.isEmpty()){
            	List<ResourceAllocation> resAolList = this.resourceAllocationService.findAllocResourceByIds((Long[])resAllocIdList.toArray(new Long[resAllocIdList.size()]));
            	if(resAolList != null && !resAolList.isEmpty()){
            		for (ResourceAllocation resourceAllocation : resAolList) {
						for (Album album : list) {
							if(createdBy.longValue() ==resourceAllocation.getOperatorNo().longValue() && resourceAllocation.getResourceId().longValue() == album.getAlbumNo().longValue()){
								if(resourceAllocation.getCmdStr() == null || resourceAllocation.getCmdStr().isEmpty()){
									album.setCmds("");
			        			}
								else{
									album.setCmds(resourceAllocation.getCmdStr());
								}
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
		//modelMap.put("topicTypeMap", ConstConfig.topicTypeMap);
		modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
		modelMap.put("captureFlagMap", ConstConfig.captureFlagMap);
		return "picmgmt/album/albumList";
	}
	
    @RequestMapping(value = "/albumEdit.action")
    public String albumEdit(HttpServletRequest request, ModelMap modelMap, Album search, Long albumNo) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	Album album = new Album();
    	if (albumNo != null && albumNo > 0){
    		album = albumService.findAlbumById(albumNo);
    	}

        List<Company> companyList = companyService.findAllCompanys(curOper);
    	Template tsearch = new Template();
    	tsearch.setType(TemplateType.ALBUM.getIndex());
    	tsearch.setStatus(TemplateStatus.ENABLE.getIndex());
    	List<Template> templateList = templateService.findAllTemplates(tsearch);
    	
    	modelMap.put("album", album);
    	modelMap.put("search", search);
    	modelMap.addAttribute("companyList", companyList);
    	modelMap.addAttribute("templateList", templateList);
    	modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
    	modelMap.put("pictureFormatMap", ConstConfig.pictureFormatMap);
    	return "picmgmt/album/albumEdit";
    }

	@RequestMapping(value = "/saveOrUpdateAlbum.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateAlbum(HttpServletRequest request,
			HttpServletResponse response, Album album) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		boolean result = false;
		String logStr = "";
		try {
			
			if (album.getCompanyNo() == null){
				album.setCompanyNo(curOper.getCompanyNo());
			}
			
			if (StringUtils.isNotEmpty(album.getPicHeight()) && "高".equals(album.getPicHeight())){
				album.setPicHeight(null);
			}
			
			if (StringUtils.isNotEmpty(album.getPicWidth() ) && "宽".equals(album.getPicWidth())){
				album.setPicWidth(null);
			}
			
			if (StringUtils.isNotEmpty(album.getAlbumCover())){
				//String rootPath = request.getSession(true).getServletContext().getRealPath("/");
				MD5Engine engine = new MD5Engine(false);
				String md5 = engine.calculateMD5(rootPath + album.getAlbumCover());
				album.setCheckCode(md5);
			}
			
			if (album.getAlbumNo() != null && album.getAlbumNo() > 0) {
				logStr = "更新[" + album.getAlbumName() + "]相册";
				album.setStatus(AuditStatus.DRAFT);
				albumService.updateAlbum(album);
			} else {
				logStr = "添加[" + album.getAlbumName() + "]相册";
				album.setAlbumId(StringUtils.generateAssetId());
				album.setStatus(AuditStatus.DRAFT);
				album.setCaptureFlag(CaptureFlag.NO.getIndex());
				album.setOperatorNo(curOper.getOperatorNo());
				album.setGroupId(curOper.getCreateBy());
				albumService.addAlbum(album);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (album.getAlbumNo() == null) {
				logger.error("Add album exception occurred, cause by:{}", e);
			} else {
				logger.error("Update album exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/albumDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String albumDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] albumNos) throws Exception {
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		boolean result = false;
		String str = "";
		try {
			
			if (albumNos != null && albumNos.length > 0) {

				if (topicService.isReferenceResource(albumNos)){
					return "{result: '" + result + "', desc : 'topic'}";
				}
			
				List<Album> list = albumService.findAlbumsById(albumNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getAlbumName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getAlbumName();
				albumService.deleteAlbums(albumNos);
				
				for (Album album : list){
					if (StringUtils.isNotEmpty(album.getAlbumCover())){
						
						if (StringUtils.isNotEmpty(album.getAlbumCover())){
							Operator curOper = SessionUtil.getActiveOperator(request);
						    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
						    File file = new File(rootPath + album.getAlbumCover());
						    operatorService.calculateUsedSpace(operator, file, false);

							FileUtils.delFile(rootPath + album.getAlbumCover());
						}

					} 
				}

			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]相册", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]相册", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete album exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/albumDetail.action")
	public String albumDetail(HttpServletRequest request, ModelMap modelMap,
			Long albumNo,Long topicId) throws Exception {

		Album album = this.albumService.findAlbumById(albumNo);

		List<ResourcePublishMap> publishList = resourcePublishMapService.findResourcePublishMapById(
				EntityType.TYPE_TOPIC, topicId, EntityType.TYPE_ALBUM, albumNo);
		if(ArrayUtils.isEmpty(publishList)) {
			publishList=null;
		}
		modelMap.put("publish", publishList);
		modelMap.put("album", album);
		modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
		modelMap.put("captureFlagMap", ConstConfig.captureFlagMap);
		return "picmgmt/album/albumDetail";
	}
	
	@RequestMapping(value = "/albumAudit.action", method = RequestMethod.POST)
	@ResponseBody
	public String albumAudit(HttpServletRequest request, Integer status, Long[] albumNos) throws Exception {
		boolean result = false;
		String str = "";
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		try {			
			ResourcePublishMap publish = new ResourcePublishMap();
			publish.setCreatedBy(createdBy);
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
				albumService.auditAlbum(status, albumNos,null,false,EntityType.TYPE_TOPIC,publish);
			}
			result = true;
			
			if(status==null) {
				throw new ResultCodeException(ResultCode.INVALID_PARAM, "status null");
			}
			switch (status) {
			case AuditStatus.AUDITING:
				logService.logToDB(request, "相册[" + str + "]提交审核", LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.AUDIT_PASS:
				logService.logToDB(request, "相册[" + str + "]审核通过", LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.AUDIT_NO_PASS:
				logService.logToDB(request, "相册[" + str + "]审核不通过",	LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.PUBLISH:
				logService.logToDB(request, "相册[" + str + "]发布",	LogUtil.LOG_INFO, true, true);
				break;
			case AuditStatus.UNPUBLISH:
				processor.putNoticeToQueue(publish.getNoticeList());				
				logService.logToDB(request, "相册[" + str + "]取消发布",	LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;

			switch (status) {
			case AuditStatus.AUDITING:
				logService.logToDB(request, "相册[" + str + "]提交审核",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Album on submit audit exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.AUDIT_PASS:
				logService.logToDB(request, "相册[" + str + "]审核通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Album on audit pass exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.AUDIT_NO_PASS:
				logService.logToDB(request, "相册[" + str + "]审核不通过",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Album on audit no pass exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.PUBLISH:
				logService.logToDB(request, "发布相册[" + str + "]",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Album on publish exception occurred, cause by:{}",
						e);
				break;
			case AuditStatus.UNPUBLISH:
				logService.logToDB(request, "取消发布相册[" + str + "]",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Album on unpublish exception occurred, cause by:{}",
						e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/albumUploadCover.action")
	@ResponseBody
	public String albumUploadCover(HttpServletRequest request, @RequestParam("Filedata")MultipartFile file, @RequestParam("Filename")String Filename
			,Long operatorNo, Long albumNo) throws Exception{
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        String fileName = FileUpload.geneFileName(Filename);
        String dirPath =  ConstConfig.UPLOAD_PATH + ConstConfig.ALBUM_FILE_PATH;
        String filePath = dirPath + fileName;
        Operator curOper = operatorService.findOperatorById(operatorNo);
        if (!operatorService.validateUsedSpace(curOper, file.getSize())){
        	return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:'spaceSizeLimit'}";
        }
        
        boolean isok = FileUpload.saveFile(file, rootPath + dirPath, fileName);
		if (albumNo != null){
			MD5Engine engine = new MD5Engine(false);
			String md5 = engine.calculateMD5(rootPath + dirPath + fileName);
			Album album = albumService.findAlbumById(albumNo);
			album.setCheckCode(md5);
			album.setAlbumCover(filePath);
			albumService.updateAlbum(album);
		}
		
		//上传成功计算使用空间
		if (isok){
			operatorService.calculateUsedSpace(curOper, new File(rootPath + dirPath + fileName), true);
		}
		return "{result:" + isok + ",filePath:'" + filePath + "',fileName:'" + Filename + "'}";
	}
	
	@RequestMapping(value = "/albumDeleteCover.action")
	@ResponseBody
	public String albumDeleteCover(HttpServletRequest request,Long albumNo,@RequestParam("filePath")String filePath) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        FileUtils.delFile(rootPath + filePath);
        String str = "删除相册封面";
        if (albumNo != null){
			Album album = albumService.findAlbumById(albumNo);
			album.setCheckCode(null);
			album.setAlbumCover(null);
			albumService.updateAlbum(album);
			str = "删除相册【" + album.getAlbumName() + "】封面";
			
		    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
		    File file = new File(rootPath + filePath);
		    operatorService.calculateUsedSpace(operator, file, false);
        }
        logService.logToDB(request, str, LogUtil.LOG_INFO, true, true);
        return "";
	}
	
	@RequestMapping(value = "/toAlbumPublish.action")
	public String toTopicPublish(HttpServletRequest request,
			ModelMap modelMap, String albumNos) throws Exception {

		Topic search = new Topic();
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getOperatorNo()));
        	break;
        case ORDINARY_OPER:
        	search.setGroupId(curOper.getCreateBy());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getCreateBy()));
        	break;
        }

		search.setStatus(AuditStatus.PUBLISH);
        String[] ids = albumNos.split(Delimiters.COMMA);
        Long[] resourceIds = new Long[ids.length];
        for (int i = 0; i < ids.length; i++){
        	resourceIds[i] = Long.valueOf(ids[i]);
        }
		List<Topic> topicList = topicService.findAllResourceTopicNoPublishs(resourceIds, search);
		Long[] topicIds = new Long[topicList.size()];
		int idx = 0;
		for (Topic topic : topicList) {
			topicIds[idx++] = topic.getId();
		}
		
		TopicColumn columnSearch = new TopicColumn();
		if (StringUtils.isEmpty(columnSearch.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
        columnSearch.setCurOper(curOper);
        columnSearch.setStatus(ColumnStatus.ENABLE.getIndex());
        List<TopicColumn> columnList = topicColumnService.findAllResourceColumnNoPublishsWithTopicIds(topicIds,resourceIds, columnSearch);
        
		modelMap.put("albumNos", albumNos);
		modelMap.put("topicList", topicList);
		modelMap.put("columnList", columnList);
		return "picmgmt/album/albumPublish";
	}
	
	@RequestMapping(value = "/albumPublish.action")
	@ResponseBody
	public String albumPublish(HttpServletRequest request,
			ModelMap modelMap, String albumNos,Long topicId, Long columnId,Boolean pcascade,Integer parentType,ResourcePublishMap publish) throws Exception {

		boolean result = false;
		String str = "";
		String logStr = "";
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		publish.setCreatedBy(createdBy);
		try {
			
			String[] tempIdArr = albumNos.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			if (ids != null && ids.length > 0) {
				List<Album> list = albumService.findAlbumsById(ids);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getAlbumName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getAlbumName();
				
				//截图专题
				if (topicId == null && columnId == null){
					Topic search = new Topic();
					search.setCaptureFlag(CaptureFlag.YES.getIndex());
					List<Topic> topicList = topicService.findAllTopics(search);
					if (topicList == null || topicList.size() <= 0){
						return "{result: '" + result + "', desc : 'noTopic'}";
					}
					topicId = topicList.get(0).getId();
					pcascade = true;
					parentType = 0;
				}
				
				switch (parentType){
				case EntityType.TYPE_TOPIC:
					albumService.auditAlbum(AuditStatus.PUBLISH, ids, topicId, pcascade, EntityType.TYPE_TOPIC,publish);
					Topic topic = topicService.findTopicById(topicId);
					logStr = "发布相册[" + str + "]到专题[" + topic.getTopicName() + "]";
					break;
				case EntityType.TYPE_MENU:
					albumService.auditAlbum(AuditStatus.PUBLISH, ids, columnId, pcascade,EntityType.TYPE_MENU,publish);
					TopicColumn column = topicColumnService.findColumnById(columnId);
					logStr = "发布相册[" + str + "]到栏目[" + column.getColumnName() + "]";
					break;
				}

			}
			processor.putNoticeToQueue(publish.getNoticeList());
			
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error("Publish albums exception occurred, cause by:{}",e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/toAlbumSinglePublish.action")
	public String toAlbumSinglePublish(HttpServletRequest request,
			ModelMap modelMap, Integer parentType, String parentIds, Long resourceId) throws Exception {
        
		modelMap.put("parentType", parentType);
		modelMap.put("parentIds", parentIds);
		modelMap.put("resourceId", resourceId);
		return "picmgmt/album/albumSinglePublish";
	}
	
	@RequestMapping(value = "/albumSinglePublish.action")
	@ResponseBody
	public String albumSinglePublish(HttpServletRequest request,
			ModelMap modelMap, String parentIds, ResourcePublishMap publish) throws Exception {

		boolean result = false;
		String str = "";
		String logStr = "";
		Album album = null;
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		publish.setCreatedBy(createdBy);
		try {
			
			album = albumService.findAlbumById(publish.getResourceId());
			String[] tempIdArr = parentIds.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			
			if (ids != null && ids.length > 0) {
				
				switch (publish.getParentType()){
				case EntityType.TYPE_TOPIC:
					List<Topic> topicList = topicService.findTopicsById(ids);
					for (int i = 0; i < topicList.size() - 1; i++) {
						str += topicList.get(i).getTopicName() + Delimiters.COMMA;
						if (str.length() > 200) {
							str += "...";
							break;
						}
					}
					str += topicList.get(topicList.size() - 1).getTopicName();
					
					logStr = "发布相册[" + album.getAlbumName() + "]到专题[" + str + "]";
					break;
				case EntityType.TYPE_MENU:
					List<TopicColumn> columnList = topicColumnService.findColumnsById(ids);
					for (int i = 0; i < columnList.size() - 1; i++) {
						str += columnList.get(i).getColumnName() + Delimiters.COMMA;
						if (str.length() > 200) {
							str += "...";
							break;
						}
					}
					str += columnList.get(columnList.size() - 1).getColumnName();
					logStr = "发布相册[" + album.getAlbumName() + "]到栏目[" + str + "]";
					break;
				}
				
				albumService.albumSinglePublish(AuditStatus.PUBLISH, ids ,publish);

			}
			processor.putNoticeToQueue(publish.getNoticeList());
			
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error("Publish albums exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	} 
	
	/** 
	 * 查询有相册权限的用户
	 */ 
	@RequestMapping(value = "/findAlbumOperator.action")
	public String findAlbumOperator(HttpServletRequest request,ModelMap modelMap,Long albumNo, Operator search) throws Exception {  
		    List<String> operList =null; 
		    List<Operator> list = new ArrayList<Operator>();  
		    String type = "2"; 
		    Long resourceId = albumNo;
			if(albumNo != null){
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
	
	/** 
	 * 查询用户可用相册
	 */ 
	/*@RequestMapping(value = "/findOperatorAlbum.action")
	@ResponseBody 
	private String findOperatorAlbum(HttpServletRequest request) throws Exception{  
		//获取登录用户ID
		String id = String.valueOf(SessionUtil.getActiveOperator(request));
		try{  
			Operator operatorNo = new Operator();  
			//String类型转换成Long类型
			Long operNo = Long.parseLong(id); 
			operatorNo.setOperatorNo(operNo); 
			List<Operator> list = operatorService.findOperatorAlbum(operatorNo); 
			for(int i=0;i<list.size();i++){ 
				albumService.findAlbumByNo(list.get(i));
			}
			
		}catch(Exception e){ 
			
		}
		return null; 
	}*/
}
