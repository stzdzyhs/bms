package com.db.bms.controller.picmgmt;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.service.AlbumService;
import com.db.bms.service.ArticleService;
import com.db.bms.service.LogService;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.Result2;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;
@RequestMapping("picmgmt/publish")
@Controller
public class ResourcePublishController {

	private final static Logger logger = Logger.getLogger(ResourcePublishController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private ResourcePublishMapService resourcePublishMapService;
	
	@Autowired
	private PortalProcessor processor;
	
	@RequestMapping(value = "/pubMgmt.action")
	public String pubMgmt(HttpServletRequest request,
			ModelMap modelMap, ResourcePublishMap search) throws Exception {
		PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        List<ResourcePublishMap> list = resourcePublishMapService.findResourcePublishMaps(search);
        if(list != null){
        	Operator curOper = SessionUtil.getActiveOperator(request);
        	Long opNo = curOper.getOperatorNo();
        	if(curOper.getType().intValue() == 2){
        		opNo = curOper.getCreateBy();
        	}
        	for (ResourcePublishMap resourcePublishMap : list) {
				if(resourcePublishMap.getCreatedBy() != null && resourcePublishMap.getCreatedBy().longValue() == opNo.longValue()){
					resourcePublishMap.setAllocRes(null);
				}
				else{
					resourcePublishMap.setAllocRes(1l);
				}
			}
        }
		modelMap.addAttribute("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("resourceTypeMap", ConstConfig.resourceTypeMap);
		modelMap.put("parentTypeMap", ConstConfig.resourceTypeMap);
		return "picmgmt/publish/pubMgmt";
	}

	@Autowired
	AlbumService albumService;
	@Autowired
	ArticleService articleService;
	
	@RequestMapping(value = "/resourcePublishDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String resourcePublishDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] publishIds) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		//String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		Result2<Object> result = new Result2<Object>();
		List<ResourcePublishMap> list = null;
		try {
			if (publishIds != null && publishIds.length > 0) {
				List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				list = resourcePublishMapService.findResourcePublishMapById(publishIds);
				if(list==null) {
					throw new ResultCodeException(ResultCode.DB_ERROR,"publish list null");
				}
				
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);

				for (ResourcePublishMap map : list){
					// must check type before parentType which might be null.
					if(map.type==EntityType.TYPE_ALBUM && map.parentType== EntityType.TYPE_TOPIC) {
						albumService.unpublishAlbum(map.parentId, new Long[]{map.resourceId}, noticeList);
					}
					else if(map.type==EntityType.TYPE_ARTICLE && map.parentType== EntityType.TYPE_COLUMN) {
						articleService.unpublishArticle(map.parentId, new Long[]{map.resourceId}, noticeList);
					}
					else {
						notice.setParentId(map.getParentId());
						notice.setParentType(map.getParentType());
						notice.setResourceType(map.type);
						notice.setResourceId(map.getResourceId());
						noticeList.add(notice);
					}

					logService.logToDB(request, "取消资源[" + map.getResourceName() + "]发布[" + map.getParentName() + "]", LogUtil.LOG_INFO,
							true, true);
					
					/*
					switch (map.getType()){
					case EntityType.TYPE_ALBUM:
						notice.setResourceType(EntityType.TYPE_ALBUM);
						break;
					case EntityType.TYPE_ARTICLE:
						notice.setResourceType(EntityType.TYPE_ARTICLE);
						break;
					}
					notice.setParentId(map.getParentId());
					notice.setParentType(map.getParentType());
					notice.setResourceId(map.getResourceId());
					noticeList.add(notice);
					*/
				}
				
				// it should ok if some id are already deleted.
				resourcePublishMapService.deleteResourcePublishMapsById(curOper, publishIds);
				
				/*
				for (ResourcePublishMap map : list) {
					if(map.type==EntityType.TYPE_ALBUM || map.type==EntityType.TYPE_ARTICLE) {
						List<ResourcePublishMap> otherpub = resourcePublishMapService.findResourcePublishMapById(
								null, null, map.type, map.resourceId);
						// 如果相册/文章没有其他发布，删除发布的图片
						if(ArrayUtils.getSize(otherpub)==0) {
							otherpub = resourcePublishMapService.findResourcePublishMapById(map.type, map.resourceId, EntityType.TYPE_PICTURE, null);
							if(otherpub!=null) {
								for(ResourcePublishMap m2: otherpub) {
									PortalPublishNotice notice = new PortalPublishNotice();
									notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
									notice.parentType = map.type;
									notice.parentId = map.resourceId;
									notice.resourceType = m2.type;
									notice.resourceId = m2.resourceId;
									noticeList.add(notice);	
								}
							}
							this.resourcePublishMapService.deleteResourcePublishMaps(map.type, map.resourceId, EntityType.TYPE_PICTURE, null);
						}
								
					}
				}
				
				for (ResourcePublishMap map : list) {
					logService.logToDB(request, "取消资源[" + map.getResourceName() + "]发布[" + map.getParentName() + "]", LogUtil.LOG_INFO,
							true, true);
				}
				*/
				
				if(noticeList.size()>0) {
					processor.putNoticeToQueue(noticeList);
				}
			}
		} 
		catch (Exception e) {
			ResultCodeException.convertException(result, e);
			if (list != null){
				for (ResourcePublishMap map : list){
					logService.logToDB(request, "取消资源[" + map.getResourceName() + "]发布[" + map.getParentName() + "]", LogUtil.LOG_INFO,
							false, true);
				}
			}
			logger.error("Cancel the resources publish exception occurred, cause by:{}", e);
		}
		String s = result.toString();
		return s;
	}
	
	@RequestMapping(value = "/resourcePublishDetail.action")
	public String resourcePublishDetail(HttpServletRequest request, ModelMap modelMap,
			Long publishId) throws Exception {

		ResourcePublishMap publish = this.resourcePublishMapService.findResourcePublishMapsById(publishId);
		modelMap.put("publish", publish);
		modelMap.put("resourceTypeMap", ConstConfig.resourceTypeMap);
		modelMap.put("parentTypeMap", ConstConfig.resourceTypeMap);
		return "picmgmt/publish/resourcePublishDetail";
	}
	
	@RequestMapping(value = "/resMgmt.action")
	public String resMgmt(HttpServletRequest request,
			ModelMap modelMap, ResourcePublishMap search) throws Exception {
		
		modelMap.put("search", search);
		return "picmgmt/publish/resMgmt";
	}

}
