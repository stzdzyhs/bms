
package com.db.bms.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.AlbumMapper;
import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.CompanyMapper;
import com.db.bms.dao.FeatureCodeGroupMapper;
import com.db.bms.dao.FeatureCodeMapper;
import com.db.bms.dao.PictureMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.dao.TopicColumnMapper;
import com.db.bms.dao.TopicMapper;
import com.db.bms.dao2.ArticleDao;
import com.db.bms.dao2.ColumnDao;
import com.db.bms.entity.Album;
import com.db.bms.entity.Article;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Column;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Topic;
import com.db.bms.entity.TopicColumn;
import com.db.bms.entity.Article.ArticleStatus;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.PublishStrategy;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("resourcePublishMapService")
public class ResourcePublishMapServiceImpl implements ResourcePublishMapService {

	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Autowired
	private FeatureCodeGroupMapper featureCodeGroupMapper;
	
	@Autowired
	private FeatureCodeMapper  featureCodeMapper;
	
	@Autowired
	private TopicMapper topicMapper;
	
	@Autowired
	private TopicColumnMapper topicColumnMapper;
	
	@Autowired
	private AlbumMapper albumMapper;
	
	@Autowired
	private PictureMapper pictureMapper;
	
	@Autowired
	private ColumnDao columnDao;
	
	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private StrategyService strategyService;
	
	@Override
	public ResourcePublishMap findResourcePublishMapsById(Long id) throws Exception {
		ResourcePublishMap publish = resourcePublishMapMapper.findResourcePublishMapsById(id);
		fillResourcePublish(publish);
		return publish;
	}

	@Override
	public List<ResourcePublishMap> findResourcePublishMapById(Long[] ids)
			throws Exception {
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapById(ids);
		for (ResourcePublishMap publish : list){
			fillResourcePublish(publish);
		}
		return list;
	}
	
	@Override
	public List<ResourcePublishMap> findResourcePublishMapById(Integer parentType,
			Long parentId, Integer type, Long resourceId) throws Exception {
		
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(parentType,parentId, type, resourceId);
		for (ResourcePublishMap publish : list){
			fillResourcePublish(publish);
		}

		return list;
	}
	
	/**
	 * find resource-publish-map data by rpm.[parentType, parentId, type, resourceId], 支持分页
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ResourcePublishMap> findResourcePublishMapByResId2(ResourcePublishMap rpm) throws Exception {
		PageUtil pu = rpm.getPageUtil();
		Integer cnt = this.resourcePublishMapMapper.findResourcePublishMapByResId2Count(rpm);
		pu.setPageCount(cnt);
		List<ResourcePublishMap> ret= this.resourcePublishMapMapper.findResourcePublishMapByResId2(rpm);
		
		return ret;
	}
	
	/**
	 * find published picture. picture data is set in ResourcePublishMap.
	 * rpm.sortKey can be createTime, frameNum, rpm.sortType asc/desc
	 * @param rpm
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ResourcePublishMap> findPublishedPicture(ResourcePublishMap rpm) throws Exception {
		PageUtil page = rpm.getPageUtil();
		page.setPageCount(this.resourcePublishMapMapper.findPublishedPictureCount(rpm));
		List<ResourcePublishMap> ret= this.resourcePublishMapMapper.findPublishedPicture(rpm);
		return ret;	
	}

	@Override
	public void addResourcePublishMap(ResourcePublishMap map) throws Exception {
		resourcePublishMapMapper.addResourcePublishMap(map);
	}

	@Override
	public void deleteResourcePublishMaps(Integer parentType,Long parentId, Integer type,Long resourceId) throws Exception {
		resourcePublishMapMapper.deleteResourcePublishMaps(parentType,parentId, type, resourceId);
	}

	@Override
	public List<ResourcePublishMap> findResourcePublishMaps(
			ResourcePublishMap search) throws Exception {
		PageUtil page = search.getPageUtil();
		int count = resourcePublishMapMapper.findResourcePublishMapCount(search);
		page.setRowCount(count);
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMaps(search);
		for (ResourcePublishMap publish : list){
			fillResourcePublish(publish);
		}
		
		return list;
	}

	@Override
	public void deleteResourcePublishMapsById(Operator curOper,Long[] ids) throws Exception {
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapById(ids);
	    resourcePublishMapMapper.deleteResourcePublishMapsById(ids);
	    if(ArrayUtils.getSize(list)<1) {
	    	return;
	    }
	    Long resourceId = list.get(0).getResourceId();
	    switch (list.get(0).getType()){
		case EntityType.TYPE_ALBUM:
			Album album = albumMapper.findAlbumById(resourceId);
			List<ResourcePublishMap> publishList = resourcePublishMapMapper.findResourcePublishMapByResId(
					null, null, EntityType.TYPE_ALBUM, resourceId);
			if (publishList == null || publishList.size() <= 0){
				album.setStatus(AuditStatus.AUDIT_PASS);
				album.setUpdateTime(DateUtil.getCurrentTime());
				albumMapper.updateAlbum(album);
			}

			if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.YES){
				resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, album.getAlbumNo(),EntityType.TYPE_PICTURE, null);
				Picture search = new Picture();
				search.setAlbumNo(album.getAlbumNo());
				search.setStatus(PictureStatus.PUBLISH.getIndex());
				List<Picture> picList = pictureMapper.findPictures(search);
				Iterator<Picture> it = picList.iterator();
				while (it.hasNext()){
					Picture pic = it.next();
					pic.setStatus(PictureStatus.AUDIT_PASS.getIndex());
					pictureMapper.updatePicture(pic);
				}
			}

			break;
		case EntityType.TYPE_ARTICLE:
			publishList = resourcePublishMapMapper.findResourcePublishMapByResId(
					null, null, EntityType.TYPE_ARTICLE, resourceId);
			if (publishList == null || publishList.size() <= 0){
				Article search = new Article();
				search.setCurOper(curOper);
				search.setStatus(ArticleStatus.AUDIT_PASS.getIndex());
				articleDao.audit(search, new Long[]{resourceId});
			}
			break;
	    }
	}
	
	private void fillResourcePublish(ResourcePublishMap publish) throws Exception{
		if (publish != null) {
			if(publish.parentType!=null) { 
				switch (publish.parentType){
				case EntityType.TYPE_TOPIC:
					Topic topic = topicMapper.findTopicById(publish.getParentId());
					if (topic != null){
						publish.setParentName(topic.getTopicName());
						publish.setTopic(topic);
					}
	
					break;
				case EntityType.TYPE_MENU:
					TopicColumn topicColumn = topicColumnMapper.findColumnById(publish.getParentId());
					if (topicColumn != null){
						publish.setParentName(topicColumn.getColumnName());
					}
	
					break;
				case EntityType.TYPE_ALBUM:
					Album album = albumMapper.findAlbumById(publish.getParentId());
					if (album != null){
						publish.setParentName(album.getAlbumName());
						publish.setAlbum(album);
					}
	
					break;
				case EntityType.TYPE_COLUMN:
					Column column = columnDao.selectByNo(publish.getParentId());
					if (column != null){
						publish.setParentName(column.getColumnName());
						publish.setColumn(column);
					}
	
					break;
				}
				
				switch (publish.getType()){
				case EntityType.TYPE_TOPIC:
					Topic topic = topicMapper.findTopicById(publish.getResourceId());
					publish.setResourceName(topic.getTopicName());
					publish.setTopic(topic);
					break;
				case EntityType.TYPE_MENU:
					TopicColumn topicColumn = topicColumnMapper.findColumnById(publish.getResourceId());
					publish.setResourceName(topicColumn.getColumnName());
					break;
				case EntityType.TYPE_ALBUM:
					Album album = albumMapper.findAlbumById(publish.getResourceId());
					publish.setResourceName(album.getAlbumName());
					publish.setAlbum(album);
					break;
				case EntityType.TYPE_PICTURE:
					Picture pic = pictureMapper.findPictureById(publish.getResourceId());
					publish.setResourceName(pic.getPicName());
					publish.setPicture(pic);
					break;
				case EntityType.TYPE_COLUMN:
					Column column = columnDao.selectByNo(publish.getResourceId());
					publish.setResourceName(column.getColumnName());
					publish.setColumn(column);
					break;
				case EntityType.TYPE_ARTICLE:
					Article art = articleDao.selectByNo(publish.getResourceId());
					publish.setResourceName(art.getArticleName());
					publish.setArticle(art);
					break;
				default:
					throw new RuntimeException("unknown type");
				}
			}
			if(StringUtils.isNotEmpty(publish.getStrategyNo())){
				List<PublishStrategy> publishStrategy = this.strategyService.getPublishStrategyByNos(publish.getStrategyNo());
				if(!publishStrategy.isEmpty()){
					publish.setPublishStrategy(publishStrategy);
				}
			}
		}

	}
}
