
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
import com.db.bms.dao.TopicMapper;
import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
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
import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.service.AlbumService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.AlbumInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetAlbumREQT;
import com.db.bms.sync.portal.protocal.GetAlbumRESP;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("albumService")
public class AlbumServiceImpl implements AlbumService {
	
	//private Logger log = Logger.getLogger(AlbumServiceImpl.class);

	@Autowired
	private AlbumMapper albumMapper;
	
	@Autowired
	private PictureMapper pictureMapper;
	
	@Autowired
	private TopicMapper topicMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private ResourceAlbumMapMapper resourceAlbumMapMapper;
	
	@Autowired
	private ResourceAllocationMapper resAllocMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private FeacodeGroupMapMapper feacodeGroupMapMapper;
	
	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Value("${portal.sysUrl}")
	private String portalUrl;
	
	@Autowired
	StrategyService strategyService;
	
	@Override
	public Album findAlbumById(Long albumNo) throws Exception {
		return albumMapper.findAlbumById(albumNo);
	}

	@Override
	public List<Album> findAlbumsById(Long[] albumNos) throws Exception {
		return albumMapper.findAlbumsById(albumNos);
	}

	@Override
	public List<Album> findAlbums(Album search) throws Exception {
		PageUtil page = search.getPageUtil();
		int count = albumMapper.findAlbumCount(search);
		page.setRowCount(count);
		return albumMapper.findAlbums(search);
	}
	
	/**
	 * query album and its strategy
	 */
//	@Override
//	public List<Album> findAlbumWithStrategy(Album search) throws Exception {
//		return null;
//		PageUtil pu = search.getPageUtil();
//		if(pu.getPaging()) {
//			int c =  topicMapper.findTopicWithStrategyCount(search);
//			pu.setRowCount(c);
//		}
//		else {
//			pu.setRowCount(-1);
//		}
//		
//		List<Topic> list = topicMapper.findAlbumWithStrategy(search);
//		
//		Map<Long, Strategy> stMap = new HashMap<Long, Strategy>();
//		
//		for(Album t:list) {
//			if(t.strategy!=null) {
//				Strategy s = stMap.get(t.strategy.id);
//				if(s == null) {
//					s = strategyService.getStrategyAllData(t.strategy.id);
//					stMap.put(s.id, s);
//				}
//				t.setStrategy(s);
//			}
//		}
//		return list;
//	}

	

	@Override
	public void addAlbum(Album album) throws Exception {
		String now = DateUtil.getCurrentTime();
		album.setCreateTime(now);
		album.setUpdateTime(now); // always set updateTime for sorting

		album.setAlbumNo(albumMapper.getPrimaryKey());
		albumMapper.addAlbum(album);
	}

	@Override
	public void updateAlbum(Album album) throws Exception {
		album.setUpdateTime(DateUtil.getCurrentTime());
		albumMapper.updateAlbum(album);
	}

	/**
	 * unpublish album 
	 * @param topicNo - topic no. NOT null
	 * @param albumNos - album nos. not null
	 * @param noticeList - must not null
	 * @return - 
	 */
	@Override
	public void unpublishAlbum(Long topicNo, Long[] albumNos, List<PortalPublishNotice> noticeList) throws Exception {
		String updateTime = DateUtil.getCurrentTime();

		for (Long albumNo : albumNos){
			//删除相册
			resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_TOPIC, topicNo, EntityType.TYPE_ALBUM, albumNo);
			//删除发布相册消息
			PortalPublishNotice notice = new PortalPublishNotice();
			notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
			notice.setParentType(EntityType.TYPE_TOPIC);
			notice.setParentId(topicNo);
			notice.setResourceType(EntityType.TYPE_ALBUM);
			notice.setResourceId(albumNo);
			noticeList.add(notice);
			
			List<ResourcePublishMap> otherpub = resourcePublishMapMapper.findResourcePublishMapByResId(
					null, null, EntityType.TYPE_ALBUM, albumNo);			
			// 如果相册没有其他发布，更新相册状态, 删除发布的图片
			if(ArrayUtils.getSize(otherpub)==0) {
				albumMapper.updateAlbumStatus(AuditStatus.AUDIT_PASS, new Long[]{albumNo}, updateTime);
				
				otherpub = resourcePublishMapMapper.findResourcePublishMapByResId(EntityType.TYPE_ALBUM, albumNo, EntityType.TYPE_PICTURE, null);
				if(ArrayUtils.getSize(otherpub)>0) {
					ArrayList<Long> pids = new ArrayList<Long>();
					for(ResourcePublishMap m2: otherpub) {
						pids.add(m2.resourceId);
						notice = new PortalPublishNotice();
						notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
						notice.setParentType(EntityType.TYPE_ALBUM);
						notice.setParentId(albumNo);
						notice.setResourceType(m2.type);
						notice.setResourceId(m2.resourceId);
						noticeList.add(notice);	
					}
					this.resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, albumNo,	EntityType.TYPE_PICTURE, null);
					
					Long[] picss = pids.toArray(new Long[0]);
					pictureMapper.updatePictureStatus(PictureStatus.AUDIT_PASS.getIndex(), picss, DateUtil.getCurrentTime());
				}
			}
		}
	}
	
	/**
	 * mapType: parentType !!!
	 */
	@Override
	public void auditAlbum(Integer status, Long[] albumNos, Long topicId, boolean pcascade, Integer mapType,ResourcePublishMap publish)
			throws Exception {
		
		String updateTime = DateUtil.getCurrentTime();
		if(status==null) {
			throw new ResultCodeException(ResultCode.INVALID_PARAM, "status null");
		}
		if(topicId == null){
			Topic search = new Topic();
			search.setCaptureFlag(1);
			List<Topic> topics = this.topicMapper.findTopics(search);
			if(topics != null){
				topicId = topics.iterator().next().getId();
			}
		}
		if(topicId==null) {
			throw new ResultCodeException(ResultCode.INVALID_PARAM, "发布专题错误:null");
		}
		
		ResourcePublishMap localPub = new ResourcePublishMap();
		
		if(publish.noticeList==null) {
			List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(noticeList);
		}
		
		switch (status) {
		case AuditStatus.AUDITING:
			List<Album> albumList = albumMapper.findAlbumsById(albumNos);
			Iterator<Album> it = albumList.iterator();
			while (it.hasNext()){
				Album album = it.next();
//				if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.YES){
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDITING.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.DRAFT.getIndex());
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDITING.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDIT_NO_PASS.getIndex());
//				}
			}
			break;
		case AuditStatus.AUDIT_PASS:
			albumList = albumMapper.findAlbumsById(albumNos);
			it = albumList.iterator();
			while (it.hasNext()){
				Album album = it.next();
				pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDITING.getIndex());
				pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDIT_NO_PASS.getIndex());
				if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.YES){
					//截图相册审核通过，即进行发布
					switch (mapType){
					case EntityType.TYPE_TOPIC:
						int count = resourceAlbumMapMapper.getResourceAlbumMapCount(EntityType.TYPE_TOPIC, topicId, album.getAlbumNo());
						if (count <= 0){
							ResourceAlbumMap map = new ResourceAlbumMap();
							map.setType(EntityType.TYPE_TOPIC);
							map.setResourceId(topicId);
							map.setAlbumId(album.getAlbumNo());
							map.setCreatedBy(publish.getCreatedBy());
							resourceAlbumMapMapper.addResourceAlbumMap(map);
						}
						break;
					}
					publish.setParentType(mapType);
					publish.setType(EntityType.TYPE_ALBUM);
					publish.setParentId(topicId);
					Long albumNo = album.getAlbumNo();
					//发布相册
					publish.setResourceId(albumNo);
					resourcePublishMapMapper.addResourcePublishMap(publish);
					//添加发布相册消息
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
					notice.setParentType(mapType);
					notice.setParentId(topicId);
					notice.setResourceType(EntityType.TYPE_ALBUM);
					notice.setResourceId(albumNo);
					publish.noticeList.add(notice);
					//发布图片
					if (pcascade){
						Picture picSearch = new Picture();
						picSearch.setAlbumNo(albumNo);
						picSearch.setStatus(PictureStatus.AUDIT_PASS.getIndex());
						List<Picture> pictureList= pictureMapper.findPictures(picSearch);
						for (Picture pic : pictureList){
							publish.setType(EntityType.TYPE_PICTURE);
							publish.setParentType(EntityType.TYPE_ALBUM);
							publish.setParentId(albumNo);
							publish.setResourceId(pic.getId());
							resourcePublishMapMapper.addResourcePublishMap(publish);
						}
						pictureMapper.updatePictureStatusByAlbumId(PictureStatus.PUBLISH.getIndex(), updateTime, albumNo, PictureStatus.AUDIT_PASS.getIndex());
					}
				}

			}
			break;
		case AuditStatus.AUDIT_NO_PASS:
			albumList = albumMapper.findAlbumsById(albumNos);
			it = albumList.iterator();
			while (it.hasNext()){
				Album album = it.next();
				if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.YES){
					//截图相册审核不通过时，取消发布
					//noticeList = new ArrayList<PortalPublishNotice>();
					//publish.setNoticeList(noticeList);
					status = AuditStatus.AUDIT_NO_PASS;
					long albumNo = album.getAlbumNo();
					//取消发布图片
					resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, albumNo, EntityType.TYPE_PICTURE, null);
					pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, albumNo, PictureStatus.PUBLISH.getIndex());
					//删除相册
					resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_TOPIC, null, EntityType.TYPE_ALBUM, albumNo);
					//添加发布相册消息
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
					notice.setResourceType(EntityType.TYPE_ALBUM);
					notice.setParentType(mapType);
					notice.setParentId(topicId);
					notice.setResourceId(albumNo);
					publish.noticeList.add(notice);
				}
				pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_NO_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDITING.getIndex());
				pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_NO_PASS.getIndex(), updateTime, album.getAlbumNo(), PictureStatus.AUDIT_PASS.getIndex());
			}
			break;
		case AuditStatus.PUBLISH:
			switch (mapType){
			case EntityType.TYPE_TOPIC:
				for (Long albumNo : albumNos){
					int count = resourceAlbumMapMapper.getResourceAlbumMapCount(EntityType.TYPE_TOPIC, topicId, albumNo);
					if (count <= 0){
						ResourceAlbumMap map = new ResourceAlbumMap();
						map.setType(EntityType.TYPE_TOPIC);
						map.setResourceId(topicId);
						map.setAlbumId(albumNo);
						map.setCreatedBy(publish.getCreatedBy());
						resourceAlbumMapMapper.addResourceAlbumMap(map);
					}
				}
				break;
			case EntityType.TYPE_MENU:
				for (Long albumNo : albumNos){
					int count = resourceAlbumMapMapper.getResourceAlbumMapCount(EntityType.TYPE_MENU, topicId, albumNo);
					if (count <= 0){
						ResourceAlbumMap map = new ResourceAlbumMap();
						map.setType(EntityType.TYPE_MENU);
						map.setResourceId(topicId);
						map.setAlbumId(albumNo);
						map.setCreatedBy(publish.getCreatedBy());
						resourceAlbumMapMapper.addResourceAlbumMap(map);
					}
				}
				break;
			}

			for (int i = 0; i < albumNos.length; i++){
				Long albumNo = albumNos[i];
				//发布相册
				localPub.setParentType(mapType);
				localPub.setParentId(topicId);
				localPub.setType(EntityType.TYPE_ALBUM);
				localPub.setResourceId(albumNo);
				localPub.setStrategyNo(publish.strategyNo);
				localPub.setCreatedBy(publish.getCreatedBy());
				resourcePublishMapMapper.addResourcePublishMap(localPub);
				
				//添加发布相册消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setParentType(mapType);
				notice.setParentId(topicId);
				notice.setResourceType(EntityType.TYPE_ALBUM);
				notice.setResourceId(albumNo);
				publish.noticeList.add(notice);
				
				//发布图片
				if (pcascade){
					Picture picSearch = new Picture();
					picSearch.setAlbumNo(albumNo);
					//picSearch.setStatus(PictureStatus.AUDIT_PASS.getIndex());
					//List<Picture> pictureList= pictureMapper.findPictures(picSearch);
					List<Picture> pictureList= pictureMapper.findAlbumCanPublishPicture(picSearch);
					if(ArrayUtils.getSize(pictureList)>0) {
						List<Long> pids = new ArrayList<Long>();
						for (Picture pic : pictureList){
							localPub.setParentType(EntityType.TYPE_ALBUM);
							localPub.setParentId(albumNo);
							localPub.setType(EntityType.TYPE_PICTURE);
							localPub.setResourceId(pic.getId());
							localPub.setStrategyNo(publish.strategyNo);
							resourcePublishMapMapper.addResourcePublishMap(localPub);
							
							pids.add(pic.id);
							
							//添加发布图片消息
							Album album = albumMapper.findAlbumById(albumNo);
							if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.NO){
							    notice = new PortalPublishNotice();
								notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
								notice.setParentType(EntityType.TYPE_ALBUM);
								notice.setParentId(albumNo);
								notice.setResourceType(EntityType.TYPE_PICTURE);
								notice.setResourceId(pic.getId());
								publish.noticeList.add(notice);
							}	
						}
						
						Long[] picss = pids.toArray(new Long[0]);
						pictureMapper.updatePictureStatus(PictureStatus.PUBLISH.getIndex(), picss, DateUtil.getCurrentTime());

						//pictureMapper.updatePictureStatusByAlbumId(PictureStatus.PUBLISH.getIndex(), updateTime, albumNo, PictureStatus.AUDIT_PASS.getIndex());
					}
				}

			}
			break;
		case AuditStatus.UNPUBLISH:
			unpublishAlbum(topicId, albumNos, publish.noticeList);
			
			/* code replaced.
		    noticeList = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(noticeList);
			status = AuditStatus.AUDIT_PASS;

			for (Long albumNo : albumNos){
				//删除相册
				resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_TOPIC, topicId, EntityType.TYPE_ALBUM, albumNo);
				//删除发布相册消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
				notice.setParentType(mapType);
				notice.setParentId(topicId);
				notice.setResourceType(EntityType.TYPE_ALBUM);
				notice.setResourceId(albumNo);
				noticeList.add(notice);
				
				List<ResourcePublishMap> otherpub = resourcePublishMapMapper.findResourcePublishMapByResId(
						null, null, EntityType.TYPE_ALBUM, albumNo);
				// 如果相册没有其他发布，删除发布的图片
				if(ArrayUtils.getSize(otherpub)==0) {
					otherpub = resourcePublishMapMapper.findResourcePublishMapByResId(EntityType.TYPE_ALBUM, albumNo, EntityType.TYPE_PICTURE, null);
					if(ArrayUtils.getSize(otherpub)>0) {
						ArrayList<Long> pids = new ArrayList<Long>();
						for(ResourcePublishMap m2: otherpub) {
							pids.add(m2.resourceId);
							notice = new PortalPublishNotice();
							notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
							notice.setParentType(EntityType.TYPE_ALBUM);
							notice.setParentId(albumNo);
							notice.setResourceType(m2.type);
							notice.setResourceId(m2.resourceId);
							noticeList.add(notice);	
						}
						this.resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, albumNo,	EntityType.TYPE_PICTURE, null);
						
						Long[] picss = pids.toArray(new Long[0]);
						pictureMapper.updatePictureStatus(PictureStatus.AUDIT_PASS.getIndex(), picss, DateUtil.getCurrentTime());
					}
				}

				//取消发布图片 
				//  to remove ...
				//resourcePublishMapMapper.deleteResourcePublishMaps(
				//		EntityType.TYPE_ALBUM, albumNo, EntityType.TYPE_PICTURE, null);
				//pictureMapper.updatePictureStatusByAlbumId(PictureStatus.AUDIT_PASS.getIndex(), updateTime, albumNo, PictureStatus.PUBLISH.getIndex());
				
			}
			*/

			break;
		}
		
		// status is already updated in UNPUBLISH
		if(status!=AuditStatus.UNPUBLISH) {
			albumMapper.updateAlbumStatus(status, albumNos, updateTime);
		}
	}

	@Override
	public void deleteAlbums(Long[] albumNos) throws Exception {
		resAllocMapper.deleteResourceAllocation(ResourceAllocation.ResourceType.ALBUM.getIndex(), null, albumNos, null);
		pictureMapper.deletePicturesByAlbumNo(albumNos);
		albumMapper.deleteAlbumsById(albumNos);
	}

	@Override
	public GetAlbumRESP getAlbumList(GetAlbumREQT request) throws Exception {
		GetAlbumRESP response = new GetAlbumRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setTopicNo(request.getTopicNo());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<AlbumInfo> albumInfoList = new ArrayList<AlbumInfo>();
		response.setAlbumList(albumInfoList);
		
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
	    
	    if (StringUtils.isNotEmpty(request.getAlbumNo())){
	    	Album album = albumMapper.findAlbumById(Long.valueOf(request.getAlbumNo()));
	    	if (album==null || album.getStatus() != AuditStatus.PUBLISH){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	    	
	    	AlbumInfo albumInfo = convertAlbum(Long.valueOf(request.getTopicNo()), album);
			albumInfoList.add(albumInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
		
		Album search = new Album();
		search.setStatus(AuditStatus.PUBLISH);
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());

		// 仅查询topic下的相册， 不处理menu下的相册 !!
		request.setType(EntityType.TYPE_TOPIC);
		int totalCount = albumMapper.findTopicPublishAlbumCount(search, request.getType(), Long.valueOf(request.getTopicNo()), EntityType.TYPE_ALBUM);
		page.setPaging(true);
		page.setRowCount(totalCount);
		List<Album> albumList = albumMapper.findTopicPublishAlbums(search, request.getType(), Long.valueOf(request.getTopicNo()), EntityType.TYPE_ALBUM,null);
		Iterator<Album> it = albumList.iterator();
		while (it.hasNext()){
			Album album = it.next();
	    	AlbumInfo albumInfo = convertAlbum(Long.valueOf(request.getTopicNo()), album);
			albumInfoList.add(albumInfo);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private AlbumInfo convertAlbum(Long topicId, Album album) throws Exception{
		AlbumInfo albumInfo = new AlbumInfo();
		albumInfo.setAlbumNo(album.albumNo);
		albumInfo.setAlbumId(album.albumId);
		albumInfo.setShowOrder(album.getShowOrder());
		albumInfo.setUpdateTime(album.updateTime);
		albumInfo.setAssetId(album.getAlbumId());
		albumInfo.setAlbumTitle(album.getAlbumName());
		albumInfo.setAlbumCover(StringUtils.isEmpty(album.getAlbumCover()) ? "" : portalUrl + "/" + album.getAlbumCover());
		albumInfo.setFileMd5(album.getCheckCode());
		albumInfo.setAlbumDesc(album.getAlbumDesc());
		albumInfo.setCaptureFlag(album.getCaptureFlag());
		albumInfo.setTemplateNo(String.valueOf(album.getTemplateId()));
		//处理相册关联的策略
		ResourcePublishMap publish = null;
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(EntityType.TYPE_TOPIC, topicId, 
				EntityType.TYPE_ALBUM,Long.valueOf(album.getAlbumNo()));
		if (list != null && list.size() > 0){
			publish = list.iterator().next();
		}
		/* 不处理策略继承,由portal处理
		//如果相册没有关联策略，就获取对应的专题策略
		if(publish == null){
			 
			log.debug("alnum["+album.getAlbumId()+"] NO bind any strategy,so get topic strtegy with topic id["+topicId.longValue()+"]");
			list = resourcePublishMapMapper.findResourcePublishMapByResId(null, null, EntityType.TYPE_TOPIC,topicId);
			if (list != null && list.size() > 0){
				publish = list.iterator().next();
			}
		}
		*/
		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){				
				albumInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}
		return albumInfo;
	}

	@Override
	public List<Album> findAllResourceAlbum(Integer type, Integer status,
			Long[] resourceIds) throws Exception {
		return albumMapper.findAllResourceAlbum(type, status, resourceIds);
	}

	@Override
	public void albumSinglePublish(Integer status, Long[] parentIds,
			ResourcePublishMap publish) throws Exception {
		for (Long parentId : parentIds){
			this.auditAlbum(status, new Long[]{publish.getResourceId()}, parentId, publish.getPcascade(), publish.getParentType(), publish);
		}
	}

	@Override
	public List<Album> findAllResourceAlbums(Integer type, Long resourceId,
			Album search) throws Exception {
		return albumMapper.findResourceAlbums(type, resourceId, search);
	}

	@Override
	public List<Album> findAllTopicPublishAlbums(Album search,
			Integer parentType, Long parentId, Integer type) throws Exception {
		return albumMapper.findTopicPublishAlbums(search, parentType, parentId, type, null);
	}

	@Override
	public Album findAlbumByAlbumId(String albumId) throws Exception {
		return albumMapper.findAlbumByAlbumId(albumId);
	}

	@Override
	public List<Album> findAlbumByNo(Operator operatorNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 相册策略
	 */
	@Override
	public List<Album> findAlbumWithStrategy(Album search) throws Exception {
		PageUtil pu = search.getPageUtil();
		if(pu.getPaging()) {
			int c =  albumMapper.findAlbumWithStrategyCount(search);
			pu.setRowCount(c);
		}
		else {
			pu.setRowCount(-1);
		}
		
		List<Album> list = albumMapper.findAlbumWithStrategy(search);
		
		Map<Long, Strategy> stMap = new HashMap<Long, Strategy>();
		
		for(Album t:list) {
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
	public Integer getAlbumCountByCompanyNo(Long companyNo) throws Exception {
		Album search = new Album();
		search.setCompanyNo(companyNo);
		return this.albumMapper.findAlbumCount(search);
	}


}
