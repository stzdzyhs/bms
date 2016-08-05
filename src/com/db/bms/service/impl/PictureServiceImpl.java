
package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.db.bms.dao.AlbumMapper;
import com.db.bms.dao.CompanyMapper;
import com.db.bms.dao.PictureMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.dao.VideoMapper;
import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.Portal;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Video;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.service.PictureService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetImageREQT;
import com.db.bms.sync.portal.protocal.GetImageRESP;
import com.db.bms.sync.portal.protocal.ImageInfo;
import com.db.bms.sync.portal.protocal.InjectVideoImageREQT;
import com.db.bms.sync.portal.protocal.InjectVideoImageRESP;
import com.db.bms.sync.portal.protocal.Poster;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("pictureService")
public class PictureServiceImpl implements PictureService {

	private final static Logger log = Logger.getLogger(PictureService.class);
	
	@Autowired
	private PictureMapper pictureMapper;
	
	@Autowired
	private AlbumMapper albumMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Value("${portal.sysUrl}")
	private String portalUrl;
	
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private VideoMapper videoMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	StrategyService strategyService;
	
	
	@Override
	public Long getPrimaryKey() throws Exception {
		Long no = this.pictureMapper.getPrimaryKey();
		return no;
	}
	
	@Override
	public Picture findPictureById(Long pictureId) throws Exception {
		return pictureMapper.findPictureById(pictureId);
	}

	@Override
	public List<Picture> findPicturesById(Long[] pictureIds) throws Exception {
		return pictureMapper.findPicturesById(pictureIds);
	}

	@Override
	public List<Picture> findPictures(Picture search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = pictureMapper.findPictureCount(search);
		page.setRowCount(count);
		return pictureMapper.findPictures(search);
	}
	
	@Override
	public List<Picture> findAllPictures(Picture search) throws Exception {
		return pictureMapper.findPictures(search);
	}
	
	
	@Override
	public Picture findArticleFirstPicture(Long articleNo) throws Exception {
		Picture p = this.pictureMapper.findArticleFirstPicture(articleNo);
		return p;
	}	

	/*
	 * some common used code, this function should be static.
	 * @param pictureMapper
	 * @param pic
	 * @throws Exception
	 */
	public static void addPicture(PictureMapper pictureMapper, Picture pic) throws Exception {
		Operator curOper = pic.getCurOper();
		
		if(curOper!=null) {
			if(pic.getCompanyNo()==null) {
				pic.setCompanyNo(curOper.getCompanyNo());
			}
			if(pic.getOperatorNo()==null) {
				pic.setOperatorNo(curOper.getOperatorNo());
			}
		}
		
		// set pictureId to PT.....
		if(pic.getPictureId()==null) {
			pic.setPictureId(StringUtils.generateFixPrefixId("PT"));
		}
		
		Long no = pictureMapper.getPrimaryKey();
		pic.setId(no);
		
		// for capture_flag=1 album, showOrder is fixed: 100
		// NOTE: showOrder is NOT nullable in table.
		if(pic.showOrder==null) {
			pic.showOrder = 100;
		}
		
		// always set updateTime for sorting.
		pic.updateTime = pic.createTime;
		
		pictureMapper.addPicture(pic);
	}
	
	@Override
	public void addPicture(Picture pic) throws Exception {
		addPicture(this.pictureMapper,pic);
	}
	
	@Override
	public void updatePicture(Picture pic) throws Exception {
		pictureMapper.updatePicture(pic);
	}
	
	@Override
	public void updatePictureBasicInfo(Picture pic) throws Exception {
		pic.setUpdateTime(DateUtil.getCurrentTime());
		int c = pictureMapper.updatePictureBasicInfo(pic);
		if(c!=1) {
			throw new Exception("图片更新错误");
		}
	}

	@Override
	public void auditPicture(Integer status, Long[] pictureIds,ResourcePublishMap publish)
			throws Exception {
		//String updateTime = DateUtil.getCurrentTime();
		switch (PictureStatus.getStatus(status)){
		case PUBLISH:
			List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(noticeList);
			
			List<Picture> pictureList= pictureMapper.findPicturesById(pictureIds);
			for (Picture pic : pictureList){
				publish.setType(EntityType.TYPE_PICTURE);
				publish.setParentType(EntityType.TYPE_ALBUM);
				publish.setParentId(pic.getAlbumNo());
				publish.setResourceId(pic.getId());
				resourcePublishMapMapper.addResourcePublishMap(publish);
				
				//添加发布图片消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_PICTURE);
				notice.setParentType(EntityType.TYPE_ALBUM);
				notice.setParentId(pic.getAlbumNo());
				notice.setResourceId(pic.getId());
				noticeList.add(notice);
			}
			break;
		case UNPUBLISH:
			noticeList = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(noticeList);
			status = PictureStatus.AUDIT_PASS.getIndex();
		    pictureList = pictureMapper.findPicturesById(pictureIds);
			for (Picture pic : pictureList){
				resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ALBUM, pic.getAlbumNo(), EntityType.TYPE_PICTURE, pic.getId());
				
				//添加发布图片消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
				notice.setResourceType(EntityType.TYPE_PICTURE);
				notice.setParentType(EntityType.TYPE_ALBUM);
				notice.setParentId(pic.getAlbumNo());
				notice.setResourceId(pic.getId());
				noticeList.add(notice);
			}
			
			break;
		default:
			break;
		}
		pictureMapper.updatePictureStatus(status, pictureIds, DateUtil.getCurrentTime());
	}

	@Override
	public void deletePicturesById(Long[] pictureIds) throws Exception {
		pictureMapper.deletePicturesById(pictureIds);
	}

	// the before code
	public GetImageRESP getImageList_0(GetImageREQT request) throws Exception {
		GetImageRESP response = new GetImageRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setAlbumNo(request.getAlbumNo());
		response.setTopicNo(request.getTopicNo());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
		//response.setImageList(imageInfoList);

		Long topicId = null;		
		
		try {
			topicId = Long.valueOf(request.getTopicNo());
		} catch (Exception e) {
			log.error("Ocurred exception when change topicId string into Lang:", e);
			e.printStackTrace();
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("topicId change into Lang error["+request.getTopicNo()+"].");
			return response;
		}
		
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
	    
		
		Album album = albumMapper.findAlbumById(Long.valueOf(request.getAlbumNo()));
		if (album == null){
			response.setTotalCount(0);
			response.setTotalPage(0);
			response.setCurrentPage(1);
			return response;
		}
		
		if (StringUtils.isNotEmpty(request.getImageNo())){
			Picture pic = pictureMapper.findPictureById(Long.valueOf(request.getImageNo()));
	    	if (pic==null || PictureStatus.getStatus(pic.getStatus()) != PictureStatus.PUBLISH){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	    	
			ImageInfo imageInfo = convertPicture_0(topicId,album.getAlbumNo(),pic);
			if (imageInfo == null){
				return response;
			}
			imageInfoList.add(imageInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
		}
		
		Picture search = new Picture();
		if (CaptureFlag.getFlag(album.getCaptureFlag()) == CaptureFlag.YES){
			search.setSortKey("frameNum");
			search.setSortType("asc");
		}else{
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
		
		search.setAlbumNo(album.getAlbumNo());
		search.setStatus(PictureStatus.PUBLISH.getIndex());
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		
		int totalCount = pictureMapper.findPictureCount(search);
		page.setRowCount(totalCount);
		List<Picture> picList = pictureMapper.findPictures(search);
		Iterator<Picture> it = picList.iterator();
		while (it.hasNext()){
			Picture pic = it.next();
			ImageInfo imageInfo = convertPicture_0(topicId, album.getAlbumNo(),pic);
			if (imageInfo != null){
				imageInfoList.add(imageInfo);
			}

		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private ImageInfo convertPicture_0(Long topicId,Long ablumId, Picture pic) throws Exception{
		ImageInfo imageInfo = new ImageInfo();
		imageInfo.setImageNo(pic.getId());
		imageInfo.setImageId(pic.pictureId);
		imageInfo.setImageTitle(pic.getPicName());
		imageInfo.setImageUrl(portalUrl + "/" + pic.getPicPath());
		imageInfo.setFileMd5(pic.getCheckCode());
		imageInfo.setImageDesc(pic.getPicDesc());
		imageInfo.setImageTime(StringUtils.isNotEmpty(pic.getVideoTime()) ? pic.getVideoTime() + ":" + pic.getFrameNum() : "");
		ResourcePublishMap publish = null;
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(
				EntityType.TYPE_ALBUM, ablumId, EntityType.TYPE_PICTURE, pic.getId());
		if (list != null && !list.isEmpty()){
			publish = list.iterator().next();
		}
		if(publish == null){
			log.debug("picture's alnum["+ablumId+"] NO bind any strategy,so get topic strtegy with topic id["+topicId.longValue()+"]");
			list = resourcePublishMapMapper.findResourcePublishMapByResId(null, null,EntityType.TYPE_TOPIC, topicId);
			if (list != null && list.size() > 0){
				publish = list.iterator().next();
			}
		}
		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){				
				imageInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}
		
		return imageInfo;
	}
	
	@Override
	public GetImageRESP getImageList(GetImageREQT request) throws Exception {
		GetImageRESP response = new GetImageRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setAlbumNo(request.getAlbumNo());
		//response.setTopicNo(request.getTopicNo());
		response.setTotalCount(0);
		response.setTotalPage(0);
		response.setCurrentPage(0);
		
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");

	    try {
			List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();

			Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
		    if (portal == null){
				throw new ResultCodeException(ResultCode.NOT_FOUND_SYSTEM);
		    }
		    
		    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
				throw new ResultCodeException(ResultCode.NO_ACCESS_RIGHTS);
		    }
		    
		    Long albumNo = Long.valueOf(request.getAlbumNo());
			if(albumNo==null) {
				throw new ResultCodeException(ResultCode.INVALID_PARAM, "albumNo null");
			}
			Album album = albumMapper.findAlbumById(albumNo);
			if (album == null){
				throw new ResultCodeException(ResultCode.DB_ERROR, "ablum error");
			}
			
			Long imageNo = null;
			if(request.getImageNo()!=null) {
				imageNo = Long.valueOf(request.getImageNo());
			}
			
			ResourcePublishMap search = new ResourcePublishMap();
			PageUtil page = search.getPageUtil();
			page.setPaging(true);
			page.setPageInfo(request.getPageSize(), request.getStartPage());
			
			if (album.captureAlbum()){
				search.setSortKey("frameNum");
				search.setSortType("asc");
			}
			else {
				search.setSortKey("createTime");
				search.setSortType("desc");
			}

			List<ResourcePublishMap> ret = null;
			int total = 0;
			if (imageNo==null) {
				search.parentType = EntityType.TYPE_ALBUM;
				search.parentId = albumNo;
				search.type = null;
				search.resourceId = null;
				
				total = this.resourcePublishMapMapper.findPublishedPictureCount(search);
				ret = this.resourcePublishMapMapper.findPublishedPicture(search);
			}
			else {
				search.parentType = EntityType.TYPE_ALBUM;
				search.parentId = albumNo;
				search.type = EntityType.TYPE_PICTURE;
				search.resourceId = imageNo;

				total = this.resourcePublishMapMapper.findPublishedPictureCount(search);
				ret = this.resourcePublishMapMapper.findPublishedPicture(search);
				
				if(ret.size()>1) {
					throw new ResultCodeException(ResultCode.DB_ERROR, "duplicate publish image");
				}
			}
			
			if(ArrayUtils.getSize(ret)<1) {
				return response;
			}
			
			for(ResourcePublishMap r: ret) {
				ImageInfo imageInfo = convertPicture(albumNo, r.picture, r);
				imageInfoList.add(imageInfo);
			}
			
			response.setImageList(imageInfoList, total, page.getPageSize());
			response.setCurrentPage(page.getPageId());
	    }
	    catch(ResultCodeException e) {
			response.setResultCode("" + e.result);
			response.setResultDesc(e.desc);
	    }
		return response;
	}
	
	private ImageInfo convertPicture(Long ablumId, Picture pic, ResourcePublishMap publish) throws Exception{
		ImageInfo imageInfo = new ImageInfo();
		imageInfo.setImageNo(pic.getId());
		imageInfo.setImageId(pic.pictureId);
		imageInfo.setImageTitle(pic.getPicName());
		imageInfo.setImageUrl(portalUrl + "/" + pic.getPicPath());
		imageInfo.setFileMd5(pic.getCheckCode());
		imageInfo.setImageDesc(pic.getPicDesc());
		imageInfo.setImageTime(StringUtils.isNotEmpty(pic.getVideoTime()) ? pic.getVideoTime() + ":" + pic.getFrameNum() : null);
		imageInfo.setShowOrder(pic.showOrder);
		imageInfo.setUpdateTime(pic.updateTime);		

		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){				
				imageInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}
		
		return imageInfo;
	}

	@Override
	public boolean isResNoUnique(Picture pic) throws Exception {
		int count = this.pictureMapper.isResNoUnique(pic);
		return count > 0 ? false: true;
	}

	@Override
	public InjectVideoImageRESP injectVideoImage(InjectVideoImageREQT request) throws Exception {
		InjectVideoImageRESP response = new InjectVideoImageRESP();
		response.setSerialNo(request.getSerialNo());
		response.setAssetId(request.getAssetId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		
		Album album = albumMapper.findAlbumByAlbumId(request.getAssetId());
		if (album == null){
			album = new Album();
			Long albumNo = albumMapper.getPrimaryKey();
			album.setShowOrder(100);
			album.setAlbumNo(albumNo);
			album.setAlbumId(request.getAssetId());
			album.setAlbumName(request.getAssetTitle());
			album.setStatus(AuditStatus.DRAFT);
			album.setCreateTime(DateUtil.getCurrentTime());
			album.setUpdateTime(DateUtil.getCurrentTime());
			album.setCaptureFlag(CaptureFlag.YES.getIndex());
			Video video = videoMapper.findVideoByAssetId(request.getAssetId());
			if (video == null){
				log.info("Discarding the video [asset=" + request.getAssetId() + "] inject message.");
				response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
				response.setResultDesc("The [assetId] parameter is invalid.");
				return response;
			}
			album.setCompanyNo(video.getCompanyNo());
			album.setOperatorNo(video.getOperatorNo());
			album.setGroupId(video.getGroupId());
			albumMapper.addAlbum(album);
		}
		
		if (request.getImageList() != null && request.getImageList().size() > 0){
			Iterator<ImageInfo> it = request.getImageList().iterator();
			while (it.hasNext()){
				ImageInfo image = it.next();
				if (!image.checkData()){
					log.info("Discarding the video [asset=" + request.getAssetId() + "] capture " + image.toString());
					continue;
				}
				
				Picture pic = new Picture();
				Long id = this.pictureMapper.getPrimaryKey();
				pic.setId(id);
				pic.setShowOrder(image.getFrameNum());
				pic.setPicName(image.getImageTitle());
				pic.setStatus(PictureStatus.DRAFT.getIndex());
				String Filename = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1, image.getImageUrl().length());
				String fileName = FileUpload.geneFileName(Filename);
				String posterPath = ConstConfig.UPLOAD_PATH + ConstConfig.PIC_FILE_PATH + fileName;
				pic.setPicPath(posterPath);
				pic.setCheckCode(image.getFileMd5());
				pic.setPicDesc(image.getImageDesc());
				pic.setVideoTime(image.getImageTime());
				pic.setFrameNum(image.getFrameNum());
				pic.setAlbumNo(album.getAlbumNo());
				pic.setCompanyNo(album.getCompanyNo());
				pic.setOperatorNo(album.getOperatorNo());
				pic.setCreateTime(DateUtil.getCurrentTime());
				
				addPicture(this.pictureMapper, pic);
				
				Poster poster = createPoster(image.getImageUrl(),posterPath,album.getOperatorNo());
				processor.putPosterToQueue(poster);
			}
		}
		return response;
	}

	private Poster createPoster(String posterUrl, String posterPath, Long operatorNo){
		Poster poster = new Poster();
		poster.setOperatorNo(operatorNo);
		poster.setPosterUrl(posterUrl);
		String classUrl = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1).replaceAll("%20", " ");
		classUrl = classUrl.substring(0,classUrl.indexOf("WEB-INF"));
		if (classUrl.indexOf(":")==-1){
			classUrl="/"+classUrl;
		}

		String localFile = classUrl + posterPath;
		poster.setLocalFile(localFile);
		return poster;
	}

	@Override
	public void deletePicturesByAlbumNo(Long[] albumNos) throws Exception {
		pictureMapper.deletePicturesByAlbumNo(albumNos);
	}
}
