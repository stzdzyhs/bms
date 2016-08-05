package com.db.bms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Article;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;

/**
 * column-article map service
 */
public interface ColumnArticleMapService {

	/**
	 * select rows by pk array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	public List<ColumnArticleMap> selectByNos(@Param(value = "nos")Long[] nos) throws Exception;
	
    /**
     * select article's column, need to set search.articleNo, and 分页信息需要设置
     * @param search -  
     * @return
     * @throws Exception
     */
	public List<ColumnArticleMap> selectArticleColumn(Column search) throws Exception;
	
	/**
	 * add column - article map,  need to set columnAritcleMap.curOper!
	 * @param article
	 */
	public void addColumnArticleMap(ColumnArticleMap columnAritcleMap) throws Exception;
	public void addColumnArticleMap(Long articleNo, Long[] columnNos,Long createdBy) throws Exception;
	
	/**
	 * delete a map, need to set columnAritcleMap.curOper!
	 * @param article
	 * @throws Exception
	 */
	public void deleteColumnArticleMap(ColumnArticleMap columnAritcleMap)  throws Exception;
	public void deleteColumnArticleMaps(List<ColumnArticleMap> list) throws Exception;

	
	/**
	 * delete article maps
	 * @param articleNo
	 * @throws Exception
	 */
	public void deleteArticleMap(Long articleNo) throws Exception; 
	
	/*//////////////////columnArticle--->article/////////////////////////*/
	 
	public List<ColumnArticleMap> selectColumnArticles(Article article) throws Exception;
	
	public void addArticlesToColumn(Long columnNo, Long[] articleNos,Long createdBy) throws Exception;
		
}
