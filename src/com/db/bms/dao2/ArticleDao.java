package com.db.bms.dao2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.db.bms.dao.ArticleMapper;
import com.db.bms.dao.ColumnArticleMapMapper;
import com.db.bms.dao.PictureMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.entity.Article;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.service.OperatorService;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.MD5Engine;
import com.db.bms.utils.core.PageUtil;

// This class is NOT used anymore !!!
@Deprecated
@Component("articleDao")
public class ArticleDao {
	@Autowired
	ArticleMapper articleMapper;
	
	@Autowired
	PictureMapper pictureMapper;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private ColumnArticleMapMapper columnArticleMapMapper;
	
	@Autowired
	ResourcePublishMapMapper resourcePublishMapMapper;
	
	/**
	 * the dir to save article body text, article images,
	 * and column cover picture. 
	 */
	@Value("${article.dir}")
	private String articleDir;

	
	static File articleDirFile = null;

	public File getArticleDirFile() throws Exception {
		if(articleDirFile==null) {
			articleDirFile = new File(articleDir);
			if(!articleDirFile.exists()) {
				articleDirFile.mkdirs();
			}
		}
		return articleDirFile;
	}
	
	public String getFilePath(String filename) {
		String ret= this.articleDir +"/"+filename;
		return ret;
	}
	
	
	public ArticleMapper getMapper() {
		return articleMapper;
	}

	/**
	 * check search.curOper, 是否有权限更改 search.ArticleNo 的文章
	 * @param search
	 * @return
	 */
	public int queryOperatorAdminPermission(Article search) {
		int ret = this.articleMapper.queryOperatorAdminPermission(search);
		return ret;
	}
	
	/**
	 * get next primary key
	 * @return
	 * @throws Exception
	 */
	public Long getPrimaryKey() throws Exception {
		Long x= this.articleMapper.getPrimaryKey();
		return x;
	}

	/**
	 * select by no(primary key)
	 * @param ArticleNo
	 * @return
	 * @throws Exception
	 */
	public Article selectByNo(@Param(value = "no") Long ArticleNo) throws Exception {
		Article a = this.articleMapper.selectByNo(ArticleNo);
		return a;
	}
	
	/**
	 * select by no(primary key), propergate porperty operator and columns
	 * @param articleNo - article pk.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Article selectByNoV2(@Param(value = "no") Long ArticleNo) throws Exception {
		Article a= this.articleMapper.selectByNoV2(ArticleNo);
		return a;
	}
	
	/**
	 * select by no.(pk) array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	public List<Article> selectByNos(@Param(value = "nos")Long[] nos) throws Exception {
		List<Article> ret = this.articleMapper.selectByNos(nos);
		return ret;
	}
	
	/**
	 * select by no(primary key), propergate porperty operator and pictures
	 * @param article = article pk and page info
	 * @return
	 * @throws Exception
	 */
	public Article selectByNoWithPictures(@Param(value = "article")Article article) throws Exception {
		if (article.getArticleNo()==null) {
			throw new IllegalArgumentException("need to set articleNo");
		}
		
		PageUtil page = article.getPageUtil();
		if (page.getPaging()) {
			int count = articleMapper.selectByNoWithPicturesCount(article);
			page.setRowCount(count);
		}
		
		Article art = this.articleMapper.selectByNoWithPictures(article);
		if(art==null) {
			return null;
		}
		
		// since left join query, it's possible that pic list is empty
		List<Picture> ps = art.getPictures();
		if(ps==null || ps.size()<1) {
			page.setRowCount(0);
		}
		return art;
	}
	public Integer selectByNoWithPicturesCount(@Param(value = "article")Article article) throws Exception {
		Integer x = this.articleMapper.selectByNoWithPicturesCount(article);
		return x;
	}
	
	/**
	 * get all Articles count  
	 * @return
	 */
	public int getAllArticlesCount() {
		int x = this.articleMapper.getAllArticlesCount();
		return x;
	}
	
	public List<Article> selectAllArticle(Article search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(false);
		List<Article> ret = selectArticles(search);
		return ret;
		
	}
	
	
	/**
	 * select Articles count. 需要设置 search.curOper 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public Integer selectArticleCount(@Param(value="article")Article search) throws Exception {
		Integer x = this.articleMapper.selectArticleCount(search);
		return x;
	}
	
	/**
	 * select Articles. 需要设置 search.curOper 
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	public List<Article> selectArticles(@Param(value="article")Article search) throws Exception {
		PageUtil page = search.getPageUtil();
		if (page.getPaging()) {
			int count = articleMapper.selectArticleCount(search);
			page.setRowCount(count);
		}
		
		List<Article> ret = articleMapper.selectArticles(search);
		return ret;
	}

	public Integer getArticleCountByIdOrName(@Param(value="article")Article search) throws Exception {
		Integer x = this.articleMapper.getArticleCountByIdOrName(search);
		return x;
	}
	
	/**
	 * get max res no. of article
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public Integer getArticleMaxResNo(@Param(value="article")Article search) throws Exception {
		Integer x = this.articleMapper.getArticleMaxResNo(search);
		if (x==null) {
			x=0;
		}
		return x;
	}
	
	
	public boolean isArticleRepeateIdOrName(Article search) throws Exception {
		// no need to check permission
		int count = this.articleMapper.getArticleCountByIdOrName(search);
		return count > 0 ? true : false;
	}	
	
	/**
	 * get Article refered count
	 * @param no -  the Article no.
	 * @return -  refered count.
	 */
	public Integer getArticleRefCount(@Param(value = "no")Long no) throws Exception {
		Integer x = this.articleMapper.getArticleRefCount(no);
		return x;
	}
	
	public void checkArticleWritePermission(Article article)  throws Exception {
		// check permission
		Operator op = article.getCurOper();
		if(op==null) {
			throw new Exception("非法的operator");
		}
		Integer type = op.getType();
		if(type==null) {
			throw new Exception("非法的operator");
		}
		else if (type.equals(Operator.TYPE_SUPER_ADMIN)) {
		}
		else if (type.equals(Operator.TYPE_COMPANY_ADMIN)) {
/*			if( op.getCompanyNo().equals(article.getCompanyNo())) {
				throw new Exception("文章权限错误");
			}*/
		}
		else if (type.equals(Operator.TYPE_ORDINARY_OPER)) {
/*			if( op.getCompanyNo().equals(article.getCompanyNo())) {
				throw new Exception("文章权限错误");
			}*/
		}
		else {
			throw new Exception("非法的operator");
		}
	}
	
	/**
	 * add a Article, araticle.templateNo must set
	 * @param Article
	 * @return
	 * @throws Exception
	 */
	public void addArticle(@Param(value="article")Article article) throws Exception {
		//checkArticleWritePermission(article);
		Operator curOper = article.getCurOper();
		if(curOper==null) {
			throw new Exception("增加文章权限错误!");
		}
		
    	if(curOper.isSuperAdmin()) {
    		if (article.getCompanyNo()==null) {
    			throw new Exception("admin必须设置文章的companyNo");
    		}
    	}
    	else {
    		if(curOper.getCompanyNo()==null) {
    			throw new Exception("用户必须从属公司!");
    		}
    		article.setCompanyNo(curOper.getCompanyNo());
    	}
    	article.setOperatorNo(curOper.getOperatorNo());
		
		article.setArticleNo(this.articleMapper.getPrimaryKey());
		String now = DateUtil.getCurrentTime();
		article.setCreateTime(now);
		article.setUpdateTime(now);
			
		// add article
		this.articleMapper.addArticle(article);
	}
	
	/**
	 * update a Article basic info
	 * @param Article
	 * @return
	 * @throws Exception
	 */
	public void updateArticle(@Param(value="article")Article article) throws Exception {
		checkArticleWritePermission(article);

		Integer x = this.articleMapper.updateArticle(article);
		if(x!=1) {
			throw new Exception("更新文章错误");
		}
	}
	
	/**
	 * update article body.
	 * @param Article
	 * @throws Exception
	 */
	public String updateArticleBody(@Param(value="article")Article article, String filename, MultipartFile file) throws Exception {
		String dir = getArticleDirFile().getCanonicalPath();
		String genFileName = FileUpload.geneFileName(filename);
		boolean isok = FileUpload.saveFile(file, dir, genFileName);
		if(file!=null && !isok) {
			String err = String.format("保存文件:%s失败", genFileName);
			throw new Exception(err);
		}
		
		File f = new File(dir, genFileName);
		String charset = FileUtils.get_charset(f);
		//txt文档，检查编码
		if(!"UTF-8".equals(charset)) {
			if(f.exists() && f.isFile()) {
				f.delete();
			}
			throw new Exception("编码不支持:" + charset);
		}
		article.setBody(genFileName);	
		this.articleMapper.updateArticleBody(article);
		
		return genFileName;
	}
	
	/**
	 * get article body.
	 * article.body must set
	 * @param article
	 * @return
	 */
	public String getArticleBody(Article article) throws Exception {
		String body = article.getBody();
		if(body==null) {
			return "";
		}
		body = getFilePath(body);
		String txt = FileUtils.readFileContent(body, ConstConfig.DEF_ENCODING);
		return txt;
	}
	
	public String uploadArticleImage(Article article, String filename, MultipartFile file) throws Exception {
		
		Picture p = new Picture();
		p.setCurOper(article.getCurOper());
		article = this.articleMapper.selectByNo(article.getArticleNo());
		
		String dir = getArticleDirFile().getCanonicalPath();
		String genFileName = FileUpload.geneFileName(filename);
		boolean isok = FileUpload.saveFile(file, dir, genFileName);
		if(file!=null && !isok) {
			String err = String.format("保存文件:%s失败", genFileName);
			throw new Exception(err);
		}

		p.setShowOrder(100);
		p.setPicName(filename);
		
		MD5Engine engine = new MD5Engine(false);
		String md5 = engine.calculateMD5(dir + "/" + genFileName);
		p.setCheckCode(md5);
		
		p.setPicPath(genFileName);
		//p.setCheckCode("");
		p.setArticleNo(article.getArticleNo());
		
		int max = this.getArticleMaxResNo(article);
		
		p.setResNo(max+1);
		
		p.setStatus(ConstConfig.STATUS_EDIT);
		
		p.setCompanyNo(article.getCompanyNo());		
		
		// TODO: add a DAO layer ???
		Operator curOper = p.getCurOper();
		
		if(curOper!=null) {
			if(p.getCompanyNo()==null) {
				p.setCompanyNo(curOper.getCompanyNo());
			}
			if(p.getOperatorNo()==null) {
				p.setOperatorNo(curOper.getOperatorNo());
			}
		}
		
		Long no = this.pictureMapper.getPrimaryKey();
		p.setId(no); 
		
		String t = DateUtil.getCurrentTime();
		p.setCreateTime(t);
		p.setUpdateTime(t);
		
		pictureMapper.addPicture(p);
		
		//--------------------------------------------------------------
		
		
		
		return genFileName;
	}
	
	
	private void rmArticle(Article art) throws Exception {
		//int cnt = this.articleMapper.getArticleRefCount(art.getArticleNo());

		//TODO: check permission
		
		// first, remove mapping
		columnArticleMapMapper.deleteArticleMap(art.getArticleNo());
		
		int cnt = this.articleMapper.deleteArticle(art);
		
		if(cnt!=1) {
			throw new Exception("文章数据错误");
		}
	}
	
	/**
	 * delete Article. need to set col.curOper
	 * @param col
	 * @return - the deleted row count
	 * @throws Exception
	 */
	public void deleteArticle(@Param(value="article")Article art) throws Exception {
		rmArticle(art);		
	}
	
	public void deleteArticles(List<Article> list) throws Exception {
		for (Article col : list) {
			rmArticle(col);
		}
	}	
	
	/**
	 * update article's status.
	 * @param article - operator, article.status, article.updateTime
	 * @param nos - article no list
	 * @param status - the new status.
	 * @throws Exception
	 */
	public void audit(Article article, @Param(value = "nos")Long[] nos) throws Exception {
		ConstConfig.checkStatusValid(article.status);
		
		if(article.status==ConstConfig.STATUS_PUBLISH) {
		}
		else if(article.status==ConstConfig.STATUS_UNPUBLISH) {
			article.status = ConstConfig.STATUS_PASS;
		}
		
		if(article.updateTime==null) {
			article.setUpdateTime(DateUtil.getCurrentTime());
		}
		
		this.articleMapper.audit(article, nos);		
	}
	
	public void updateStatus(Integer status, Long[] articleNos, String updateTime) throws Exception {
		if(updateTime==null) {
			updateTime = DateUtil.getCurrentTime();
		}
		this.articleMapper.updateStatus(status, articleNos, updateTime);
	}
	
	/**
	 * select articles that can be added into column, 
	 * NOTE: it will exclude articles that already in the column
	 * search.currOper and search.columnNo must set !
	 * @param search
	 * @return
	 * @param article
	 * @return list
	 * @throws Exception
	 */
	public List<Article> selectArticleForColumn(Article search) throws Exception {
		Operator op = search.getCurOper();
		if(op==null || op.getType()==null) {
			throw new IllegalArgumentException("需要设置operator");
		}
		if (search.getColumnNo()==null) {
			throw new IllegalArgumentException("需要设置版块No");
		}
		
		PageUtil page = search.getPageUtil();
		if (page.getPaging()) {
			int count = articleMapper.selectArticleForColumnCount(search);
			page.setRowCount(count);
		}
		List<Article> ret = this.articleMapper.selectArticleForColumn(search);
		return ret;
	}
	
	public Integer getColumnArticleCount(Long columnNo, Long articleNo) throws Exception{
		return columnArticleMapMapper.getColumnArticleCount(columnNo, articleNo);
	}
	
	public void addColumnArticleMap(ColumnArticleMap map) throws Exception{
		map.setColumnArticleNo(columnArticleMapMapper.getPrimaryKey());
		columnArticleMapMapper.addColumnArticleMap(map);
	}
	
	public Integer findColumnPublishArticleCount(Article search, Integer parentType,Long parentId, Integer type) throws Exception{
		return this.articleMapper.findColumnPublishArticleCount(search, parentType, parentId, type);
	}

	public List<Article> findColumnPublishArticles(Article search,Integer parentType,Long parentId, Integer type) throws Exception{
		return this.articleMapper.findColumnPublishArticles(search, parentType, parentId, type);
	}
	
	/**
	 * 文章策略
	 */
	public List<Article> findArticleWithStrategy(Article search) throws Exception{
		return articleMapper.findArticleWithStrategy(search);
	}
	
	public Integer findArticleWithStrategyCount(Article search) throws Exception{
		return articleMapper.findArticleWithStrategyCount(search);
	}
	
	/**
	 * unpublish article 
	 * @param columnNo - column no. NOT null
	 * @param articleNos - album nos. not null
	 * @param noticeList - must not null
	 * @return - 
	 */
	public void unpublishArticle(Long columnNo, Long[] articleNos, List<PortalPublishNotice> noticeList) throws Exception {
		String updateTime = DateUtil.getCurrentTime();

		for (Long articleNo : articleNos){
			//删除article
			resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_COLUMN, columnNo, EntityType.TYPE_ARTICLE, articleNo);
			//删除发布article消息
			PortalPublishNotice notice = new PortalPublishNotice();
			notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
			notice.setParentType(EntityType.TYPE_COLUMN);
			notice.setParentId(columnNo);
			notice.setResourceType(EntityType.TYPE_ARTICLE);
			notice.setResourceId(articleNo);
			noticeList.add(notice);
			
			List<ResourcePublishMap> otherpub = resourcePublishMapMapper.findResourcePublishMapByResId(
					null, null, EntityType.TYPE_ARTICLE, articleNo);
			// 如果article没有其他发布，删除发布的图片
			if(ArrayUtils.getSize(otherpub)==0) {
				this.updateStatus(AuditStatus.AUDIT_PASS, new Long[]{articleNo}, updateTime);
				
				otherpub = resourcePublishMapMapper.findResourcePublishMapByResId(EntityType.TYPE_ARTICLE, articleNo, EntityType.TYPE_PICTURE, null);
				if(ArrayUtils.getSize(otherpub)>0) {
					ArrayList<Long> pids = new ArrayList<Long>();
					for(ResourcePublishMap m2: otherpub) {
						pids.add(m2.resourceId);
						notice = new PortalPublishNotice();
						notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
						notice.setParentType(EntityType.TYPE_ARTICLE);
						notice.setParentId(articleNo);
						notice.setResourceType(m2.type);
						notice.setResourceId(m2.resourceId);
						noticeList.add(notice);	
					}
					this.resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_ARTICLE, articleNo,	EntityType.TYPE_PICTURE, null);
					
					Long[] picss = pids.toArray(new Long[0]);
					pictureMapper.updatePictureStatus(PictureStatus.AUDIT_PASS.getIndex(), picss, DateUtil.getCurrentTime());
				}
			}
		}
	
	}
	
}
