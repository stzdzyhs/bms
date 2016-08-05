
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Picture;


public interface PictureMapper {
	
	/**
	 * get primary key
	 * @return
	 * @throws Exception
	 */
	Long getPrimaryKey() throws Exception;

	Picture findPictureById(Long pictureId) throws Exception;
	
	List<Picture> findPicturesById(Long[] pictureIds) throws Exception;
	
	Integer findPictureCount(Picture search) throws Exception;
	
	List<Picture> findPictures(Picture search) throws Exception;
	
	/**
	 * find first picture of article
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	Picture findArticleFirstPicture(@Param(value = "articleNo")Long articleNo) throws Exception;
	
	/**
	 * 查找相册可以发布的图片 (排除已经发布的图片)
	 * @param picture - must set picture.albumNo
	 * @return
	 * @throws Exception
	 */
	List<Picture> findAlbumCanPublishPicture(@Param(value="picture")Picture picture) throws Exception;
	
	
	Integer addPicture(Picture pic) throws Exception;
	
	Integer updatePicture(Picture pic) throws Exception;
	
	/**
	 * update picture basic info(picName, picLabe, picDesc, picAuthor, picSource, voteFlag, resId)
	 * @param pic
	 * @return
	 * @throws Exception
	 */
	Integer updatePictureBasicInfo(@Param(value = "picture")Picture pic) throws Exception;

	Integer updatePictureStatus(@Param(value = "status")Integer status, @Param(value = "pictureIds")Long[] pictureIds, @Param(value = "updateTime")String updateTime) throws Exception;
	
	Integer deletePicturesById(Long[] pictureIds) throws Exception;
	
	Integer deletePicturesByAlbumNo(Long[] albumNos) throws Exception;
	
	Integer updatePictureStatusByAlbumId(@Param(value = "publishStatus")Integer publishStatus, 
			 @Param(value = "updateTime")String updateTime,
			 @Param(value = "albumNo")Long albumNo,
			 @Param(value = "auditPassStatus")Integer auditPassStatus) throws Exception;
	
	/**
	 * is res number unique
	 * @param search
	 * @return
	 * @throws Exception
	 */
	Integer isResNoUnique(@Param(value="picture")Picture pic) throws Exception;	
}
