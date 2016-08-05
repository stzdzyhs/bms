package com.db.bms.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.db.bms.entity.Article;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.sync.portal.protocal.GetArticleREQT;
import com.db.bms.sync.portal.protocal.GetArticleRESP;

/**
 * 板块 Article service
 */
public interface ArticleService {

	/**
	 * get file path
	 * @param filename
	 * @return
	 */
	String getFilePath(String filename); 
		
	
	/**	
	 * 根据 id 查找 Article, 不检查operator.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Article selectByNo(Long id) throws Exception;
	
	/**
	 * same with selectByNo, but join table bus_company, sys_operator.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Article selectByNoV2(Long id) throws Exception;
	
	/**
	 * select article and its pictures
	 * @param article - pk and page info
	 * @return
	 * @throws Exception
	 */
	Article selectByNoWithPictures(Article article) throws Exception;

	/**
	 * 根据 Article No列表  查找 Articles, 不检查operator.
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<Article> selectByNos(Long[] ids) throws Exception;

    
    /**
     * 根据search.curOper,返回operator可以使用/看到的Article
     * @param search
     * @return
     * @throws Exception
     */
    List<Article> selectAllArticles(Article search) throws Exception;
    
    /**
     * 根据search.operator,返回operator可以使用/看到的Article,
     * search.curOper 和分页信息需要设置
     * @param search -  
     * @return
     * @throws Exception
     */
	List<Article> selectArticles(Article search) throws Exception;
	
	/**
	 * 判断板块id/name是否重复
	 * @param search
	 * @return
	 * @throws Exception
	 */
    boolean isArticleRepeateIdOrName(Article search) throws Exception;
    
	/**
	 * get Article refered count
	 * @param no -  the Article no.
	 * @return -  refered count.
	 */
    int getArticleRefCount(Long no) throws Exception;
    

    /**
     * get article max res no of pictures.
     * @param article
     * @return
     * @throws Exception
     */
	Integer getArticleMaxResNo(Article article) throws Exception;

    /**
     * 检查 search.curOper 是否有权限更新或删除 search.ArticleNo 指定的Article
     * @param o
     * @param search
     * @return
     */
    //boolean checkUserPermission(Article search) throws Exception ;
    
    /**
     * 检查 Article.curOper权限，增加 Article
     * @param Article
     * @throws Exception
     */
	void addArticle(Article article) throws Exception;

    /**
     * 检查 Article.curOper权限，更新 Article
     * @param Article
     * @throws Exception
     */
	void updateArticle(Article article) throws Exception;
	

	/**
	 * update article body
	 * @param article
	 * @param filename
	 * @param file
	 * @param result: out param
	 * @return - the file path
	 * @throws Exception
	 */
	String uploadArticleBody(Article article, String filename, MultipartFile file) throws Exception;
	
	String getArticleBody(Article article) throws Exception;
	
	/**
	 * upload article image
	 * @param article
	 * @param filename
	 * @param file
	 * @return
	 * @throws Exception
	 */
	String uploadArticleImage(Article article, String filename, MultipartFile file) throws Exception;
	
    /**
     * delete a Article, art.curOper muset set
     * @throws Exception
     */
    void deleteArticle(Article art) throws Exception;
    
    /**
     * 删除 Article list. 如果出错, 抛出异常
     * @param list
     * @param list
     * @return
     * @throws Exception
     */
    void deleteArticles(List<Article> list) throws Exception;
    
	/**
	 * get all Articles count  
	 * @return
	 */
	int getAllArticlesCount() throws Exception;
	
	/**
	 * 操作员更新板块状态
	 * @param article - need to set: article.curOper, article.status, article.updateTime
	 * @param nos
	 * @param status
	 * @throws Exception
	 */
	void audit(Article article, Long[] nos, ResourcePublishMap publish) throws Exception;
	
	void articleSinglePublish(Article article, Long[] parentIds, Integer status, ResourcePublishMap publish) throws Exception;
	
	File getFileDir() throws Exception;
//
//    List<Article> findArticlesWithSubByNo(List<Long> ArticleNos) throws Exception;
//
//    
//	List<Article> findArticlesByParentId(Long parentId) throws Exception;
//	
//	void deleteArticleCardRegionMaps(Long ArticleNo, Long[] regionIds) throws Exception;
//	
//	void addArticleCardRegionMap(Long ArticleNo, Long[] regionIds) throws Exception;
//	
//	List<CardRegion> findArticleCardRegions(Long ArticleNo, CardRegion region) throws Exception;
//	
//	List<CardRegion> findArticleCardRegionsNoSelect(Long ArticleNo, CardRegion region) throws Exception;
//	
//	boolean isArticleReferenceCardRegion(Long[] regionIds) throws Exception;
//	
//	Integer findArticleCountById(List<String> ArticleIds) throws Exception;
//	
//	Article findArticleById(String ArticleId) throws Exception;
//	
//	List<Article> findSelectArticleList(Article search)throws Exception;
//	
//	List<Article> ArticleSelectList(Article search) throws Exception;
//
//	Integer ArticleSelectListCount(Article search) throws Exception;
	
	List<Article> findAllColumnPublishArticles(Article search,Integer parentType,Long parentId, Integer type) throws Exception;
	
	GetArticleRESP getArticleList(GetArticleREQT request) throws Exception;

	
	/**
	 * select articles that can be added into column, 
	 * NOTE: it will exclude articles that already in the column
	 * search.currOper and search.columnNo must set !
	 * @param search
	 * @return
	 */
	List<Article> selectArticleForColumn(Article article) throws Exception;
	
	/**
	 * 文章策略
	 */
	List<Article> findArticleWithStrategy(Article article) throws Exception;
	
	/**
	 * 查询运营商号被引用的数量，主要用于删除运营商时进行检测
	 * @param companyNo
	 * @return
	 * @throws Exception
	 */
	Integer getArticleCountByCompanyNo(Long companyNo)throws Exception;

	/**
	 * unpublish article 
	 * @param columnNo - column no. NOT null
	 * @param articleNos - album nos. not null
	 * @param noticeList - must not null
	 * @return - 
	 */
	public void unpublishArticle(Long columnNo, Long[] articleNos, List<PortalPublishNotice> noticeList) throws Exception;
	
	
	public List<Article> findColumnPublishArticles(Article search,Integer parentType,Long parentId, Integer type) throws Exception;
	
	
}
