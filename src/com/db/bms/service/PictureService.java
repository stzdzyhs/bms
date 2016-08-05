
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Picture;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.sync.portal.protocal.GetImageREQT;
import com.db.bms.sync.portal.protocal.GetImageRESP;
import com.db.bms.sync.portal.protocal.InjectVideoImageREQT;
import com.db.bms.sync.portal.protocal.InjectVideoImageRESP;


public interface PictureService {

	Long getPrimaryKey() throws Exception;
	
	Picture findPictureById(Long pictureId) throws Exception;
	
	List<Picture> findPicturesById(Long[] pictureIds) throws Exception;
	
	List<Picture> findPictures(Picture search) throws Exception;
	
	List<Picture> findAllPictures(Picture search) throws Exception;
	
	/**
	 * find first picture of article. return null if article has no picture.
	 * @param articleNo
	 * @return
	 */
	Picture findArticleFirstPicture(Long articleNo) throws Exception;
	
	
	void addPicture(Picture pic) throws Exception;
	
	void updatePicture(Picture pic) throws Exception;
	
	/**
	 * update picture basic info(picName, picLabe, picDesc, picAuthor, picSource, voteFlag)
	 */	
	void updatePictureBasicInfo(Picture pic) throws Exception;
	
	void auditPicture(Integer status, Long[] pictureIds,ResourcePublishMap publish) throws Exception;
	
	void deletePicturesById(Long[] pictureIds) throws Exception;
	
	GetImageRESP getImageList(GetImageREQT request) throws Exception;
	
    /**
     * test if reqId number unique or not
     * @param article
     * @return
     * @throws Exception
     */
	boolean isResNoUnique(Picture pic) throws Exception;
	
	InjectVideoImageRESP injectVideoImage(InjectVideoImageREQT request) throws Exception;
	
	void deletePicturesByAlbumNo(Long[] albumNos) throws Exception;
    
}
