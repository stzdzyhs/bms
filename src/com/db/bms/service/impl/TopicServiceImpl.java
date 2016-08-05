
package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.db.bms.dao.AlbumMapper;
import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.FeacodeGroupMapMapper;
import com.db.bms.dao.PictureMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.ResourceAlbumMapMapper;
import com.db.bms.dao.ResourceAllocationMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.dao.TopicColumnMapper;
import com.db.bms.dao.TopicMapper;
import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Picture;
import com.db.bms.entity.Portal;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourceAlbumMap;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.Topic;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.StrategyService;
import com.db.bms.service.TopicService;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetTopicREQT;
import com.db.bms.sync.portal.protocal.GetTopicRESP;
import com.db.bms.sync.portal.protocal.TopicInfo;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("topicService")
public class TopicServiceImpl implements TopicService {
	
	//private static Logger log = Logger.getLogger(TopicServiceImpl.class);

	@Autowired
	private TopicMapper topicMapper;
	
	@Autowired
	private AlbumMapper albumMapper;
	
	@Autowired
	private PictureMapper pictureMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private ResourceAlbumMapMapper resourceAlbumMapMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private TopicColumnMapper topicColumnMapper;
	
	@Autowired
	private ResourceAllocationMapper resAllocMapper;
	
	@Autowired
	private FeacodeGroupMapMapper feacodeGroupMapMapper;
	
	@Autowired
	private CardRegionMapper cardRegionMapper;

	@Autowired
	StrategyService strategyService;
	
	
	@Value("${portal.sysUrl}")
	private String portalUrl;
	
	@Override
	public Topic findTopicById(Long topicId) throws Exception {
		return topicMapper.findTopicById(topicId);
	}
	
	@Override
	public List<Topic> findTopicsById(Long[] topicIds) throws Exception {
		return topicMapper.findTopicsById(topicIds);
	}

	@Override
	public List<Topic> findTopics(Topic search) throws Exception {
		PageUtil page = search.getPageUtil();
		int count = topicMapper.findTopicCount(search);
		page.setRowCount(count);
		return topicMapper.findTopics(search);
	}
	
	/**
	 * query topic and its strategy
	 */
	@Override
	public List<Topic> findTopicWithStrategy(Topic search) throws Exception {
		PageUtil pu = search.getPageUtil();
		if(pu.getPaging()) {
			int c =  topicMapper.findTopicWithStrategyCount(search);
			pu.setRowCount(c);
		}
		else {
			pu.setRowCount(-1);
		}
		
		List<Topic> list = topicMapper.findTopicWithStrategy(search);
		
		Map<Long, Strategy> stMap = new HashMap<Long, Strategy>();
		
		for(Topic t:list) {
			if(t.strategy!=null) {
				Strategy s = stMap.get(t.strategy.strategyNo);
				if(s == null) {
					s = strategyService.getStrategyAllData(t.strategy.strategyNo);
					stMap.put(s.strategyNo, s);
				}
				t.setStrategy(s);
			}
		}
		return list;
	}

	
	@Override
	public void addTopic(Topic topic) throws Exception {
		String now = DateUtil.getCurrentTime();
		topic.setCreateTime(now);
		topic.setUpdateTime(now);// always set updateTime for sorting.
		topicMapper.addTopic(topic);
	}

	@Override
	public void updateTopic(Topic topic) throws Exception {
		topic.setUpdateTime(DateUtil.getCurrentTime());
		topicMapper.updateTopic(topic);
	}

	@Override
	public void auditTopic(Integer status, Long[] topicIds,boolean acascade, boolean pcascade,ResourcePublishMap publishMap)
			throws Exception {
		
		String updateTime = DateUtil.getCurrentTime();
		
		if(status==null) {
			throw new ResultCodeException(ResultCode.INVALID_PARAM,"status null");
		}
		
		switch (status){
		case AuditStatus.PUBLISH:
			List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
			publishMap.setNoticeList(noticeList);
			for (Long topicId : topicIds){
				publishMap.setParentType(null);
				publishMap.setParentId(null);
				publishMap.setType(EntityType.TYPE_TOPIC);
				publishMap.setResourceId(topicId);
				resourcePublishMapMapper.addResourcePublishMap(publishMap);
				
				//添加发布专题消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_TOPIC);
				notice.setParentType(null);
				notice.setParentId(null);
				notice.setResourceId(topicId);
				noticeList.add(notice);
				
				if (acascade){
					List<Album> albumList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.AUDIT_PASS, new Long[]{topicId});
					List<Album> albumPublishedList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.PUBLISH, new Long[]{topicId});
					if(albumList == null){
						albumList = albumPublishedList;
					}
					else{
						albumList.addAll(albumPublishedList);
					}
					for (Album album : albumList){
						albumMapper.updateAlbumStatus(AuditStatus.PUBLISH, new Long[]{album.getAlbumNo()}, updateTime);
						
						publishMap.setParentType(EntityType.TYPE_TOPIC);
						publishMap.setParentId(topicId);
						publishMap.setType(EntityType.TYPE_ALBUM);
						publishMap.setResourceId(album.getAlbumNo());
						resourcePublishMapMapper.addResourcePublishMap(publishMap);
						
						//添加发布相册消息
					    notice = new PortalPublishNotice();
						notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
						notice.setParentType(EntityType.TYPE_TOPIC);						
						notice.setParentId(topicId);
						notice.setResourceType(EntityType.TYPE_ALBUM);
						notice.setResourceId(album.getAlbumNo());
						noticeList.add(notice);
						if (pcascade){
							Picture picSearch = new Picture();
							picSearch.setAlbumNo(album.getAlbumNo());
							picSearch.setStatus(PictureStatus.AUDIT_PASS.getIndex());
							List<Picture> pictureList= pictureMapper.findPictures(picSearch);
							for (Picture pic : pictureList){
								publishMap.setParentType(EntityType.TYPE_ALBUM);
								publishMap.setParentId(album.getAlbumNo());
								publishMap.setType(EntityType.TYPE_PICTURE);
								publishMap.setResourceId(pic.getId());
								resourcePublishMapMapper.addResourcePublishMap(publishMap);
								
								//添加发布图片消息
							    notice = new PortalPublishNotice();
								notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
								notice.setParentType(EntityType.TYPE_ALBUM);
								notice.setParentId(album.getAlbumNo());
								notice.setResourceType(EntityType.TYPE_PICTURE);
								notice.setResourceId(pic.getId());
								noticeList.add(notice);
							}
							pictureMapper.updatePictureStatusByAlbumId(PictureStatus.PUBLISH.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDIT_PASS.getIndex());
						}
					}
				}
			}
			break;
			
		case AuditStatus.UNPUBLISH:
			status = AuditStatus.AUDIT_PASS;
		    noticeList = new ArrayList<PortalPublishNotice>();
			publishMap.setNoticeList(noticeList);
			for (Long topicId : topicIds){
				resourcePublishMapMapper.deleteResourcePublishMaps(null,null, EntityType.TYPE_TOPIC, topicId);
				
				//添加取消发布专题消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
				notice.setParentType(null);
				notice.setParentId(null);
				notice.setResourceType(EntityType.TYPE_TOPIC);
				notice.setResourceId(topicId);
				noticeList.add(notice);
				if (acascade){
					Album search = new Album();
					PageUtil page = search.getPageUtil();
					page.setPaging(true);
					//List<Album> albumList = albumMapper.findTopicPublishAlbums(search, EntityType.TYPE_TOPIC, null, EntityType.TYPE_ALBUM, topicIds);
					List<Album> albumList = albumMapper.findTopicPublishAlbums(search, EntityType.TYPE_TOPIC,
							topicId, EntityType.TYPE_ALBUM, null);
					
					for (Album album : albumList) {
						//删除发布相册
						resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_TOPIC,topicId, EntityType.TYPE_ALBUM,album.getAlbumNo());
						
						List<ResourcePublishMap> albumPublishList = resourcePublishMapMapper.findResourcePublishMapByResId(
								null, null, EntityType.TYPE_ALBUM, album.getAlbumNo());
						// 如果所有的发布都取消，更新album状态
						if (ArrayUtils.getSize(albumPublishList)==0){
							albumMapper.updateAlbumStatus(AuditStatus.AUDIT_PASS, new Long[]{album.getAlbumNo()}, updateTime);
						}
						
						//添加取消发布相册消息
					    notice = new PortalPublishNotice();
						notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
						notice.setParentType(EntityType.TYPE_TOPIC);
						notice.setParentId(topicId);
						notice.setResourceType(EntityType.TYPE_ALBUM);
						notice.setResourceId(album.getAlbumNo());
						noticeList.add(notice);
						
						// 获取相册所有发布
						albumPublishList = resourcePublishMapMapper.findResourcePublishMapByResId(
								EntityType.TYPE_TOPIC, null, EntityType.TYPE_ALBUM, album.getAlbumNo());
						// 如果没有发布到其它专题
						if(ArrayUtils.getSize(albumPublishList)<1) {
							//取消发布图片
							Picture picSearch = new Picture();
							picSearch.setAlbumNo(album.getAlbumNo());
							picSearch.setStatus(PictureStatus.PUBLISH.getIndex());
							List<Picture> pictureList= pictureMapper.findPictures(picSearch);
							for (Picture pic : pictureList){
								resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, album.getAlbumNo(), EntityType.TYPE_PICTURE, pic.getId());
							
								//添加取消发布图片消息
							    notice = new PortalPublishNotice();
								notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
								notice.setParentType(EntityType.TYPE_ALBUM);
								notice.setParentId(album.getAlbumNo());
								notice.setResourceType(EntityType.TYPE_PICTURE);
								notice.setResourceId(pic.getId());
								noticeList.add(notice);
							}
							pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.PUBLISH.getIndex());
						}
					}
				}
			}

			break;
		}
		topicMapper.updateTopicStatus(status, topicIds, updateTime);
	}
	
	private boolean compareTopic(Long[] topicIds, List<ResourcePublishMap> albumPublishList){
		boolean flag = true;
		Map<Long, Long> topicIdMap = new HashMap<Long, Long>();
		for (Long topicId : topicIds){
			topicIdMap.put(topicId, topicId);
		}
		
		for (ResourcePublishMap map : albumPublishList){
			if (!topicIdMap.containsKey(map.getParentId())){
				flag = false;
				return false;
			}
			
			/* should be TOPIC ?
			if (map.getParentType() == EntityType.TYPE_COLUMN){
				flag = false;
				return false;
			}
			*/
		}
		return flag;
	}

	@Override
	public void deleteTopics(Long[] topicIds) throws Exception {
		resAllocMapper.deleteResourceAllocation(ResourceAllocation.ResourceType.TOPIC.getIndex(), null, topicIds, null);
		resourceAlbumMapMapper.deleteResourceAlbumMaps(EntityType.TYPE_TOPIC,topicIds);
		topicColumnMapper.deleteColumnByTopicIds(topicIds);
		topicMapper.deleteTopicsById(topicIds);
	}

	@Override
	public List<Album> findResourceAlbums(Integer type,Long resourceId, Album search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = albumMapper.findResourceAlbumCount(type, resourceId, search);
		page.setRowCount(count);
		return albumMapper.findResourceAlbums(type, resourceId, search);
	}

	@Override
	public List<Album> findResourceAlbumsNoSelect(Integer type,Long resourceId, Album search)
			throws Exception {
		PageUtil page = search.getPageUtil();
		int count = albumMapper.findResourceAlbumCountNoSelect(type, resourceId, search);
		page.setRowCount(count);
		return albumMapper.findResourceAlbumsNoSelect(type, resourceId, search);
	}

	@Override
	public List<Topic> findAllTopics(Topic search) throws Exception {
		return topicMapper.findAllTopics(search);
	}

	@Override
	public GetTopicRESP getTopicList(GetTopicREQT request) throws Exception {
		GetTopicRESP response = new GetTopicRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<TopicInfo> topicInfoList = new ArrayList<TopicInfo>();
		response.setTopicList(topicInfoList);
		
		Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
	    if (portal == null){
			response.setResultCode(CommonResultCode.NOT_FOUND_SYSTEM.getResultCode());
			response.setResultDesc("Could not find the system.");
			return response;
	    }
	    
	    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
			response.setResultCode(CommonResultCode.NO_ACCESS_RIGHTS.getResultCode());
			response.setResultDesc("No access rights.");
			return response;
	    }
		
	    //查询指定专题
	    if (StringUtils.isNotEmpty(request.getTopicNo())) {
	    	Topic topic = topicMapper.findTopicById(Long.valueOf(request.getTopicNo()));
	    	if (topic==null || topic.getStatus() != AuditStatus.PUBLISH){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	        TopicInfo topicInfo = convertTopic(topic);
			topicInfoList.add(topicInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
	    
		Topic search = new Topic();
		search.setStatus(AuditStatus.PUBLISH);
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = topicMapper.findTopicCount(search);
		page.setRowCount(totalCount);
		List<Topic> topicList = topicMapper.findTopics(search);
		Iterator<Topic> it = topicList.iterator();
		while (it.hasNext()){
			Topic topic = it.next();
			TopicInfo topicInfo = convertTopic(topic);
			topicInfoList.add(topicInfo);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	public TopicInfo convertTopic(Topic topic) throws Exception {
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setTopicNo(topic.id);
		topicInfo.setTopicId(topic.getTopicId());
		topicInfo.setTopicTitle(topic.getTopicName());
		topicInfo.setTopicDesc(topic.getTopicDesc());
		topicInfo.setTopicType(topic.getType());
		topicInfo.setTopicCover(StringUtils.isEmpty(topic.getTopicCover()) ? "" : portalUrl + "/" + topic.getTopicCover());
		topicInfo.setFileMd5(topic.getCheckCode());
		topicInfo.setCaptureFlag(topic.getCaptureFlag());
		topicInfo.setTemplateNo(String.valueOf(topic.getTemplateId()));
		topicInfo.setUpdateTime(topic.getUpdateTime());
		//获取专题的发布信息，从而获取对应的发布策略（目前的设计，一个专题同一时刻只能有一个发布记录）
		ResourcePublishMap publish = null;
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(
				null, null, EntityType.TYPE_TOPIC, topic.getId());
		if (list != null && list.size() > 0){
			publish = list.iterator().next();
		}
		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){				
				topicInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}
		return topicInfo;		
	}

	@Override
	public void addResourceAlbumMap(Integer type, Long resourceId, Long[] albumNos,Long createdBy) throws Exception {
		ResourceAlbumMap map = new ResourceAlbumMap();
		map.setType(type);
		map.setResourceId(resourceId);
		map.setCreatedBy(createdBy);
		for (Long albumNo : albumNos){
			map.setAlbumId(albumNo);
			resourceAlbumMapMapper.addResourceAlbumMap(map);
		}
	}

	@Override
	public void deleteResourceAlbumMapsByAlbumId(Integer type,Long resourceId, Long[] albumNos)
			throws Exception {
		resourceAlbumMapMapper.deleteResourceAlbumMapsByAlbumId(type,resourceId, albumNos);
	}
	
	@Override
	public boolean isReferenceResource(Long[] albumNos) throws Exception {
		int count = resourceAlbumMapMapper.getResourceAlbumMapCountByAlbumId(albumNos);
		return count > 0 ? true : false;
	}

	@Override
	public void publishUpdate(ResourcePublishMap publish) throws Exception {
		
		ResourcePublishMap oldPublish = resourcePublishMapMapper.findResourcePublishMapByResId(
				null,null, EntityType.TYPE_TOPIC, publish.getResourceId()).get(0);
		oldPublish.setCompanyId(publish.getCompanyId());
		oldPublish.setRegionCode(publish.getRegionCode());
		oldPublish.setFeatureGroupId(publish.getFeatureGroupId());
		oldPublish.setFeatureId(publish.getFeatureId());
		resourcePublishMapMapper.updateResourcePublishMap(oldPublish);
		
		String updateTime = DateUtil.getCurrentTime();
		if (publish.getAcascade()){
			
			//更新已经发布的相册
			ResourcePublishMap oldAlbumPublish = new ResourcePublishMap();
			oldAlbumPublish.setType(EntityType.TYPE_ALBUM);
			oldAlbumPublish.setParentId(publish.getResourceId());
			oldAlbumPublish.setCompanyId(publish.getCompanyId());
			oldAlbumPublish.setRegionCode(publish.getRegionCode());
			oldAlbumPublish.setFeatureGroupId(publish.getFeatureGroupId());
			oldAlbumPublish.setFeatureId(publish.getFeatureId());
			resourcePublishMapMapper.updateResourcePublishMapByParentId(oldAlbumPublish);
			
			ResourcePublishMap newPublish = new ResourcePublishMap();
			newPublish.setCompanyId(publish.getCompanyId());
			newPublish.setRegionCode(publish.getRegionCode());
			newPublish.setFeatureGroupId(publish.getFeatureGroupId());
			newPublish.setFeatureId(publish.getFeatureId());
			
	
			if (publish.getPcascade()){
				//更新已经发布的图片
				List<Album> albumList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.PUBLISH, new Long[]{publish.getResourceId()});
				for (Album album : albumList){
					ResourcePublishMap oldPicPublish = new ResourcePublishMap();
					oldPicPublish.setType(EntityType.TYPE_PICTURE);
					oldPicPublish.setParentId(album.getAlbumNo());
					oldPicPublish.setCompanyId(publish.getCompanyId());
					oldPicPublish.setRegionCode(publish.getRegionCode());
					oldPicPublish.setFeatureGroupId(publish.getFeatureGroupId());
					oldPicPublish.setFeatureId(publish.getFeatureId());
					resourcePublishMapMapper.updateResourcePublishMapByParentId(oldPicPublish);
				}
				
				//发布已经发布的相册的审核通过的图片
				List<Album> publishAlbumList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.PUBLISH, new Long[]{publish.getResourceId()});
				for (Album album : publishAlbumList){
					Picture picSearch = new Picture();
					picSearch.setAlbumNo(album.getAlbumNo());
					picSearch.setStatus(PictureStatus.AUDIT_PASS.getIndex());
					List<Picture> pictureList= pictureMapper.findPictures(picSearch);
					for (Picture pic : pictureList){
						newPublish.setType(EntityType.TYPE_PICTURE);
						newPublish.setParentType(EntityType.TYPE_ALBUM);
						newPublish.setParentId(album.getAlbumNo());
						newPublish.setResourceId(pic.getId());
						resourcePublishMapMapper.addResourcePublishMap(newPublish);
					}
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.PUBLISH.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDIT_PASS.getIndex());
				}
				
				//发布审核通过的相册的审核通过的图片
				List<Album> auditPassAlbumList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.AUDIT_PASS, new Long[]{publish.getResourceId()});
				for (Album album : auditPassAlbumList){
					albumMapper.updateAlbumStatus(AuditStatus.PUBLISH, new Long[]{album.getAlbumNo()}, updateTime);
					
					newPublish.setType(EntityType.TYPE_ALBUM);
					newPublish.setParentType(EntityType.TYPE_TOPIC);
					newPublish.setParentId(publish.getResourceId());
					newPublish.setResourceId(album.getAlbumNo());
					resourcePublishMapMapper.addResourcePublishMap(newPublish);

					
					Picture picSearch = new Picture();
					picSearch.setAlbumNo(album.getAlbumNo());
					picSearch.setStatus(PictureStatus.AUDIT_PASS.getIndex());
					List<Picture> pictureList= pictureMapper.findPictures(picSearch);
					for (Picture pic : pictureList){
						newPublish.setType(EntityType.TYPE_PICTURE);
						newPublish.setParentType(EntityType.TYPE_ALBUM);
						newPublish.setParentId(album.getAlbumNo());
						newPublish.setResourceId(pic.getId());
						resourcePublishMapMapper.addResourcePublishMap(newPublish);
					}
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.PUBLISH.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDIT_PASS.getIndex());
				}

			}else{
				//取消已经发布的相册的图片
				List<Album> publishAlbumList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.PUBLISH, new Long[]{publish.getResourceId()});
				for (Album album : publishAlbumList){
					resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, album.getAlbumNo(), EntityType.TYPE_PICTURE, null);
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.PUBLISH.getIndex());
				}
			}
			
		}else{
			List<Album> albumList = albumMapper.findAllResourceAlbum(EntityType.TYPE_TOPIC,AuditStatus.PUBLISH, new Long[]{publish.getResourceId()});
			for (Album album : albumList){
				// TODO: null parent ????
				List<ResourcePublishMap> albumPublishList = resourcePublishMapMapper.findResourcePublishMapByResId(
						null, null, EntityType.TYPE_ALBUM, album.getAlbumNo());
				if (compareTopic(new Long[]{publish.getResourceId()}, albumPublishList)){
					//取消发布相册
					albumMapper.updateAlbumStatus(AuditStatus.AUDIT_PASS, new Long[]{album.getAlbumNo()}, updateTime);
					
					//取消发布图片
					resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, album.getAlbumNo(),EntityType.TYPE_PICTURE, null);
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.PUBLISH.getIndex());
				}

			}
			//删除相册
			resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_TOPIC,publish.getResourceId(), EntityType.TYPE_ALBUM, null);
		}
	}

	@Override
	public List<Topic> findResourceTopicNoPublishs(Long resourceId, Topic topic) throws Exception {
		PageUtil page = topic.getPageUtil();
		page.setPaging(true);
		int count = topicMapper.findResourceTopicNoPublishCount(new Long[]{resourceId}, topic);
		page.setRowCount(count);
		return topicMapper.findResourceTopicNoPublishs(new Long[]{resourceId}, topic);
	}

	@Override
	public List<Topic> findAllResourceTopicNoPublishs(Long[] resourceIds,
			Topic topic) throws Exception {
		return topicMapper.findResourceTopicNoPublishs(resourceIds, topic);
	}

	@Override
	public Integer getTopicCountByCompanyNo(Long companyNo) throws Exception {
		Topic search = new Topic();
		search.setCompanyNo(companyNo);
		return this.topicMapper.findTopicCount(search);
	}

	@Override
	public List<Album> findColumnAlbumsNoSelect(Integer type, Long resourceId,Album search, Long topicId) throws Exception {
		PageUtil page = search.getPageUtil();
		int count = albumMapper.findColumnAlbumCountNoSelect(type, resourceId, search,topicId);
		page.setRowCount(count);
		return albumMapper.findColumnAlbumsNoSelect(type, resourceId, search,topicId);
	}
	
}
