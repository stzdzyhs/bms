package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Article;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;

/**
 * dao for column-article map
 */
public interface ColumnArticleMapMapper {

	Long getPrimaryKey() throws Exception;
	
	/**
	 * select by no.(pk) array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	List<ColumnArticleMap> selectByNos(@Param(value = "nos")Long[] nos) throws Exception;
	
	
	/**
     * select article's column, need to set column.articleNo, 分页信息需要设置
     * @param search -  
     * @return
     * @throws Exception
     */
	List<ColumnArticleMap> selectArticleColumn(@Param(value="column")Column column) throws Exception;
	Integer selectArticleColumnCount(@Param(value="column")Column column) throws Exception;

	/**
	 * add a map
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer addColumnArticleMap(@Param(value="columnArticleMap")ColumnArticleMap map) throws Exception;
	
	/**
	 * delete a map
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer deleteColumnArticleMap(@Param(value="columnArticleMap")ColumnArticleMap map) throws Exception;
	
	
	/**
	 * delete article map
	 * @param articleNo
	 * @return
	 */
	Integer deleteArticleMap(@Param(value="articleNo")Long articleNo);
	
	/**
	 * 
	 * @param columnNo
	 */
	void deleteColumnMap(@Param(value="columnNo")Long columnNo) throws Exception;
	
	/*///////////////////columnArticleMap--->article/////////////////////////*/
	List<ColumnArticleMap> selectColumnArticles(@Param(value="article")Article article) throws Exception;
	Integer selectColumnArticlesCount(@Param(value="article")Article  article) throws Exception;
	
	Integer getColumnArticleCount(@Param(value="columnNo")Long columnNo, @Param(value="articleNo")Long articleNo) throws Exception;
}
