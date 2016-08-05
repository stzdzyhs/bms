package com.db.bms.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.db.bms.dao.ArticleMapper;
import com.db.bms.dao.ColumnArticleMapMapper;
import com.db.bms.dao.ColumnMapper;
import com.db.bms.dao.PictureMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.entity.Article;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.Portal;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.Article.ArticleStatus;
import com.db.bms.entity.Picture.PictureStatus;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.ArticleService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.ArticleInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetArticleREQT;
import com.db.bms.sync.portal.protocal.GetArticleRESP;
import com.db.bms.sync.portal.protocal.ImageInfo;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.MD5Engine;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;

/**
 * <b>功能：</b>用于事物处理<br>
 */

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	//private static Logger log = Logger.getLogger(ArticleServiceImpl.class);

	@Autowired
	public ArticleMapper articleMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
		
	@Autowired
	private ColumnMapper columnMapper;
	
	@Autowired
	private PictureMapper pictureMapper;

	@Value("${portal.sysUrl}")
	private String portalUrl;

	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private PortalMapper portalMapper;
			
	@Autowired
	StrategyService strategyService;
	
	@Autowired
	ColumnArticleMapMapper columnArticleMapMapper;
	

	/**
	 * the dir to save article body text, article images,
	 * and column cover picture. 
	 */
	@Value("${article.dir}")
	private String articleDir;
	
	
	/**
	 * select by no(primary key)
	 * @param ArticleNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public Article selectByNo(Long id) throws Exception {
		Article a = this.articleMapper.selectByNo(id);
		return a;
	}

	/**
	 * select by no(primary key), propergate porperty operator and pictures
	 * @param article = article pk and page info
	 * @return
	 * @throws Exception
	 */
	@Override
	public Article selectByNoWithPictures(Article article) throws Exception {
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
	
	/**
	 * select by no(primary key), propergate porperty operator and columns
	 * @param articleNo - article pk.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Article selectByNoV2(Long id) throws Exception {
		Article a= this.articleMapper.selectByNoV2(id);
		return a;
	}

	/**
	 * select by no.(pk) array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Article> selectByNos(Long[] ids) throws Exception {
		List<Article> ret = this.articleMapper.selectByNos(ids);
		return ret;
	}
	//------------------------------------------------------------------------
	
	@Override
	public List<Article> selectAllArticles(Article search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(false);
		List<Article> ret = selectArticles(search);
		return ret;
	}

	
	/**
	 * select Articles. 需要设置 search.curOper 
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	@Override
	public List<Article> selectArticles(Article search) throws Exception {
		PageUtil page = search.getPageUtil();
		if (page.getPaging()) {
			int count = articleMapper.selectArticleCount(search);
			page.setRowCount(count);
		}
		
		List<Article> ret = articleMapper.selectArticles(search);
		return ret;
	}
	//------------------------------------------------------------------------

	@Override
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
	@Override
    public int getArticleRefCount(Long no) throws Exception {
		Integer x = this.articleMapper.getArticleRefCount(no);
		return x;
    }
	
	/**
	 * add a Article
	 * @param Article
	 * @return
	 * @throws Exception
	 */
	@Override
	public void addArticle(Article article) throws Exception {
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
	
	public static void checkArticleWritePermission(Article article)  throws Exception {
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
	 * update a Article basic info
	 * @param Article
	 * @return
	 * @throws Exception
	 */	
	@Override
	public void updateArticle(Article article) throws Exception {
		checkArticleWritePermission(article);

		Integer x = this.articleMapper.updateArticle(article);
		if(x!=1) {
			throw new Exception("更新文章错误");
		}
	}
	
	@Override
	public String getFilePath(String filename) {
		String ret= this.articleDir +"/"+filename;
		return ret;
	}

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

	@Override
	public String uploadArticleBody(Article article, String filename, MultipartFile file) throws Exception {
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
	

	@Override
	public Integer getArticleMaxResNo(Article article) throws Exception {
		Integer x = this.articleMapper.getArticleMaxResNo(article);
		if (x==null) {
			x=0;
		}
		return x;
	}
		
	
	private static void rmArticle(ColumnArticleMapMapper columnArticleMapMapper, ArticleMapper articleMapper, Article art) throws Exception {
		//int cnt = this.articleMapper.getArticleRefCount(art.getArticleNo());
		// first, remove mapping
		columnArticleMapMapper.deleteArticleMap(art.getArticleNo());
		
		int cnt = articleMapper.deleteArticle(art);
		if(cnt!=1) {
			throw new Exception("文章数据错误");
		}
	}

	@Override
    public void deleteArticle(Article art) throws Exception {
		rmArticle(columnArticleMapMapper, articleMapper, art);		
    }
	
	@Override
	public void deleteArticles(List<Article> list) throws Exception {
		//this.articleDao.deleteArticles(list);
		File f = null;
		for (Article art : list) {
			if (art.getBody() != null && art.getBody().length() > 0) {
				//释放使用空间
				f = new File(this.getFileDir().toString() + "/" + art.getBody());
				operatorService.calculateUsedSpace(operatorService.findOperatorById(art.getCurOper().getOperatorNo()), f, false);
			}
			//删除文章
			rmArticle(columnArticleMapMapper, articleMapper, art);
		}
	}

	/**
	 * get all Articles count  
	 * @return
	 */
	@Override
	public int getAllArticlesCount() throws Exception {
		int x = this.articleMapper.getAllArticlesCount();
		return x;
	}

	public static void updateStatus(ArticleMapper articleMapper, Integer status, Long[] articleNos) throws Exception {
		if(status==ConstConfig.STATUS_UNPUBLISH) {
			status = ConstConfig.STATUS_PASS;
		}		
		String updateTime = DateUtil.getCurrentTime();
		articleMapper.updateStatus(status, articleNos, updateTime);
	}
	
	public void updateStatus(Integer status, Long[] articleNos) throws Exception {
		updateStatus(articleMapper, status, articleNos);
	}	
		
	@Override
	public void unpublishArticle(Long columnNo, Long[] articleNos, List<PortalPublishNotice> noticeList) throws Exception {
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
				this.updateStatus(AuditStatus.AUDIT_PASS, new Long[]{articleNo});
				
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

	/**
	 * 操作员更新板块状态
	 * @param article - need to set: article.curOper, article.status, article.updateTime
	 * @param nos
	 * @param publish - NOT null
	 * @throws Exception
	 */
	@Override
	public void audit(Article article, Long[] nos, ResourcePublishMap publish) throws Exception {
		if(publish.noticeList==null) {
			List<PortalPublishNotice> list = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(list);
		}
		
		if(article.updateTime==null) {
			article.setUpdateTime(DateUtil.getCurrentTime());
		}
		ResourcePublishMap localPub = new ResourcePublishMap();
		
		switch (ArticleStatus.getStatus(article.status)){
		case PUBLISH:
			Long createdBy = article.getCurOper().getOperatorNo();
			if(article.getCurOper() != null && article.getCurOper().getType().intValue() == 2){
				createdBy = article.getCurOper().getCreateBy();
			}
			for (Long articleNo : nos){
				
				int count = columnArticleMapMapper.getColumnArticleCount(publish.getParentId(), articleNo);
				if (count <= 0){
					ColumnArticleMap map = new ColumnArticleMap();
					map.setColumnNo(publish.getParentId());
					map.setArticleNo(articleNo);
					map.setCreatedBy(createdBy);
					map.setColumnArticleNo(columnArticleMapMapper.getPrimaryKey());
					columnArticleMapMapper.addColumnArticleMap(map);
				}
				
				localPub.setParentType(EntityType.TYPE_COLUMN);
				localPub.setParentId(publish.parentId);
				localPub.setType(EntityType.TYPE_ARTICLE);
				localPub.setResourceId(articleNo);
				localPub.setStrategyNo(publish.strategyNo);
				resourcePublishMapMapper.addResourcePublishMap(localPub);
				
				//添加发布文章消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_ARTICLE);
				notice.setParentType(EntityType.TYPE_COLUMN);
				notice.setParentId(publish.getParentId());
				notice.setResourceId(articleNo);
				publish.noticeList.add(notice);
			}			
			break;
			
		case UNPUBLISH:
			// TODO: replace following code by unpublishArticle()
			for (Long articleNo : nos){
				resourcePublishMapMapper.deleteResourcePublishMaps(
						EntityType.TYPE_COLUMN, null, EntityType.TYPE_ARTICLE, articleNo);
				//添加取消发布文章消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
				notice.setResourceType(EntityType.TYPE_ARTICLE);
				notice.setParentId(null); // TODO: Parent NULL ????
				notice.setResourceId(articleNo);
				publish.noticeList.add(notice);
			}
			break;
			
		default:
			break;
		}
		updateStatus(articleMapper, article.status, nos);
		//articleDao.audit(article, nos);
	}

	/*
	@Override
	public boolean checkUserPermission(Article search) throws Exception {
		boolean t = this.articleDao.checkArticleWritePermission(search);
		return t;
	}
	*/

	@Override
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

	/**
	 * select articles that can be added into column, 
	 * NOTE: it will exclude articles that already in the column
	 * search.currOper and search.columnNo must set !
	 * @param search
	 * @return
	 */
	@Override	
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

	@Override
	public void articleSinglePublish(Article article, Long[] parentIds,
			Integer status, ResourcePublishMap publish) throws Exception {		
		article.setStatus(status);
		for (Long parentId : parentIds){
			publish.setParentId(parentId);
			this.audit(article, new Long[]{publish.getResourceId()}, publish);
		}
	}

	@Override
	public File getFileDir() throws Exception {		
		if(articleDirFile==null) {
			articleDirFile = new File(articleDir);
			if(!articleDirFile.exists()) {
				articleDirFile.mkdirs();
			}
		}
		return articleDirFile;
	}

	@Override
	public List<Article> findAllColumnPublishArticles(Article search,
			Integer parentType, Long parentId, Integer type) throws Exception {
		return this.articleMapper.findColumnPublishArticles(search, parentType, parentId, type);
	}

	@Override
	public GetArticleRESP getArticleList(GetArticleREQT request) throws Exception {
		GetArticleRESP response = new GetArticleRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setColumnNo(request.getColumnNo());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();
		response.setArticleList(articleInfoList);
		
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
	    
		Article search = new Article();
	    if (StringUtils.isNotEmpty(request.getArticleNo())){
	    	search.setArticleNo(Long.valueOf(request.getArticleNo()));
	    	Article article = selectByNoWithPictures(search);
	    	if (ArticleStatus.getStatus(article.getStatus()) != ArticleStatus.PUBLISH){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	    	
	    	ArticleInfo articleInfo = convertArticle(Long.valueOf(request.getColumnNo()), article);
	    	articleInfoList.add(articleInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
		
		search.setStatus(AuditStatus.PUBLISH);
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		
		int totalCount = articleMapper.findColumnPublishArticleCount(search, EntityType.TYPE_COLUMN, Long.valueOf(request.getColumnNo()), 
				EntityType.TYPE_ARTICLE);
		page.setPaging(true);
		page.setRowCount(totalCount);
		List<Article> articleList = articleMapper.findColumnPublishArticles(search, EntityType.TYPE_COLUMN, Long.valueOf(request.getColumnNo()), 
				EntityType.TYPE_ARTICLE);
		Iterator<Article> it = articleList.iterator();
		while (it.hasNext()){
			Article article = it.next();
			ArticleInfo articleInfo = convertArticle(Long.valueOf(request.getColumnNo()), article);
			articleInfoList.add(articleInfo);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}

	private ArticleInfo convertArticle(Long columnId, Article article) throws Exception {
		ArticleInfo articleInfo = new ArticleInfo();
		articleInfo.setArticleNo(article.articleNo);
		articleInfo.setArticleId(article.articleId);
		articleInfo.setShowOrder(article.getShowOrder());
		articleInfo.setTitle(article.getTitle());
		articleInfo.setSubTitle(article.getTitle2());
		articleInfo.setIntroduction(article.getIntroduction());
		articleInfo.setBodyPath(portalUrl + "/art/" + article.getBody());
		articleInfo.setTemplateNo(article.getTemplateId());
		articleInfo.setUpdateTime(article.updateTime);
		
		List<ImageInfo> imageList = new ArrayList<ImageInfo>();
		articleInfo.setImageList(imageList);
		Article articleWithPic = selectByNoWithPictures(article);
		List<Picture> pictureList = articleWithPic.getPictures();
		if (pictureList != null && pictureList.size() > 0){
			for (Picture pic : pictureList){
				if (StringUtils.isNotEmpty(pic.getPicPath())){
					ImageInfo imageInfo = new ImageInfo();
					imageInfo.setImageNo(pic.id);
					imageInfo.setImageId(pic.pictureId);
					imageInfo.setImageUrl(portalUrl + "/art/" + pic.getPicPath());
					imageInfo.setFileMd5(pic.getCheckCode());
					imageList.add(imageInfo);
				}
			}
		}
		
		ResourcePublishMap publish = null;
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId( 
				EntityType.TYPE_COLUMN, columnId, EntityType.TYPE_ARTICLE, Long.valueOf(article.getArticleNo()));
		if (list != null && !list.isEmpty()){
			publish = list.iterator().next();
		}
		
		/* 不处理策略继承,由portal处理
		Column parentColumn = this.columnMapper.selectByNo(columnId);
		Long parnetId = parentColumn.getParentNo();
		//如果版块没有绑定策略，则循环查找其父级板块的策略
		while(publish == null){
			if(parnetId == null || parnetId.longValue() == -1){
				break;
			}
			log.debug("article column["+parentColumn.getColumnId()+"] NO bind any strategy,so find it's parent strategy["+parnetId+"]");
			parentColumn = columnMapper.selectByNo(parnetId);			
			list = resourcePublishMapMapper.findResourcePublishMapByResId( 
					parentColumn.getParentNo() == null ? null: EntityType.TYPE_COLUMN,parentColumn.getParentNo(), 
					EntityType.TYPE_COLUMN, parentColumn.getColumnNo());
			if(list != null && !list.isEmpty()){
				publish = list.iterator().next();
			}
			parnetId = parentColumn.getParentNo();			
		}
		*/
		
		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){				
				articleInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}		
		return articleInfo;
	}
    
	public Integer findArticleWithStrategyCount(Article search) throws Exception{
		return articleMapper.findArticleWithStrategyCount(search);
	}
	
	/**
	 * 文章策略
	 */
	@Override
	public List<Article> findArticleWithStrategy(Article search) throws Exception { 
		PageUtil pu = search.getPageUtil();
		if(pu.getPaging()) {
			int c =  articleMapper.findArticleWithStrategyCount(search);
			pu.setRowCount(c);
		}
		else {
			pu.setRowCount(-1);
		}
		
		List<Article> list = articleMapper.findArticleWithStrategy(search);
		
		Map<Long, Strategy> stMap = new HashMap<Long, Strategy>();
		
		for(Article t:list) {
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
	public Integer getArticleCountByCompanyNo(Long companyNo) throws Exception {
		Article search = new Article();
		search.setCompanyNo(companyNo);
		Integer ret = this.articleMapper.selectArticleCount(search); 
		return ret;
	}

	@Override
	public List<Article> findColumnPublishArticles(Article search, Integer parentType, Long parentId, Integer type) throws Exception {
		List<Article> list = this.articleMapper.findColumnPublishArticles(search, parentType, parentId, type);
		return list;
	}
}
