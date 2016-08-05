package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Article;

public interface ArticleMapper {

	/**
	 * check search.curOper, 是否有权限更改 search.ArticleNo 的文章
	 * @param search
	 * @return
	 */
	int queryOperatorAdminPermission(Article search);
	
	/**
	 * get next primary key
	 * @return
	 * @throws Exception
	 */
	Long getPrimaryKey() throws Exception;

	/**
	 * select by no(primary key)
	 * @param ArticleNo
	 * @return
	 * @throws Exception
	 */
	Article selectByNo(@Param(value = "no") Long ArticleNo) throws Exception;
	
	
	/**
	 * select by no(primary key), join table company and operator
	 * @param no - article pk.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Article selectByNoV2(@Param(value = "no") Long ArticleNo) throws Exception;
	
	/**
	 * select by no.(pk) array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	List<Article> selectByNos(@Param(value = "nos")Long[] nos) throws Exception;
	
	/**
	 * select by no(primary key), propergate porperty operator and pictures
	 * @param article = article pk and page info
	 * @return
	 * @throws Exception
	 */
	Article selectByNoWithPictures(@Param(value = "article")Article article) throws Exception;
	Integer selectByNoWithPicturesCount(@Param(value = "article")Article article) throws Exception;

	
	/**
	 * get all Articles count  
	 * @return
	 */
	int getAllArticlesCount();
	
	
	/**
	 * select Articles count. 需要设置 search.curOper 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	Integer selectArticleCount(@Param(value="article")Article search) throws Exception;
	/**
	 * select Articles. 需要设置 search.curOper 
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	List<Article> selectArticles(@Param(value="article")Article search) throws Exception;

	/**
	 * delete Article. need to set col.curOper
	 * @param col
	 * @return - the deleted row count
	 * @throws Exception
	 */
	Integer deleteArticle(@Param(value="article")Article col) throws Exception;

	Integer getArticleCountByIdOrName(@Param(value="article")Article search) throws Exception;
	
	/**
	 * get max res no. of article
	 * @param search
	 * @return
	 * @throws Exception
	 */
	Integer getArticleMaxResNo(@Param(value="article")Article search) throws Exception;
	
	/**
	 * get Article refered count
	 * @param no -  the Article no.
	 * @return -  refered count.
	 */
	Integer getArticleRefCount(@Param(value = "no")Long no) throws Exception;
	
	/**
	 * add a Article
	 * @param Article
	 * @return
	 * @throws Exception
	 */
	Integer addArticle(@Param(value="article")Article Article) throws Exception;

	/**
	 * update a Article
	 * @param Article
	 * @return
	 * @throws Exception
	 */
	Integer updateArticle(@Param(value="article")Article Article) throws Exception;
	
	/**
	 * update article body.
	 * @param Article
	 * @throws Exception
	 */
	Integer updateArticleBody(@Param(value="article")Article Article) throws Exception;

	/**
	 * update article status.
	 * @param status
	 * @param articleNos
	 * @param updateTime
	 * @return
	 * @throws Exception
	 */
	Integer updateStatus(@Param(value = "status")Integer status, @Param(value = "articleNos")Long[] articleNos, @Param(value = "updateTime")String updateTime) throws Exception;

	/**
	 * audit status
	 * @param article - check curOper, article.status, article.updateTime need to set.
	 * @param nos
	 * @param status
	 * @throws Exception
	 */
	void audit(@Param(value="article")Article article, @Param(value = "nos")Long[] nos) throws Exception;

	/**
	 * find articles that can be added into column, 
	 * NOTE: it will exclude articles that already in the column
	 * search.currOper and search.columnNo must set !
	 * @param search
	 * @return
	 */
	List<Article> selectArticleForColumn(@Param(value="article")Article article) throws Exception;
	Integer selectArticleForColumnCount(@Param(value="article")Article article) throws Exception;
	
	Integer findColumnPublishArticleCount(@Param(value="article")Article search, @Param(value="parentType")Integer parentType,
			@Param(value="parentId")Long parentId, @Param(value="type")Integer type) throws Exception;

	List<Article> findColumnPublishArticles(@Param(value="article")Article search,@Param(value="parentType")Integer parentType,
			@Param(value="parentId")Long parentId, @Param(value="type")Integer type) throws Exception;
	
	Integer getArticleCountByTemplateId(Long templateId) throws Exception;
	
	/**
	 * 文章策略
	 */
    List<Article> findArticleWithStrategy(@Param(value = "article")Article search) throws Exception;
	
	Integer findArticleWithStrategyCount(@Param(value = "article")Article search) throws Exception;

}
