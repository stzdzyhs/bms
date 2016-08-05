package com.db.bms.controller.textmgmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.db.bms.common.file.UploadResult;
import com.db.bms.entity.Article;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.Company;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Picture;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Template;
import com.db.bms.entity.Article.ArticleStatus;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Template.TemplateStatus;
import com.db.bms.entity.Template.TemplateType;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnArticleMapService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.PictureService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.service.TemplateService;
import com.db.bms.service.impl.WordstockServiceImpl;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.MD5Engine;
import com.db.bms.utils.Result;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("textmgmt/article")
@Controller
public class ArticleController {

	private final static Logger logger = Logger.getLogger(ArticleController.class);
	
	@Autowired
	private LogService logService;
	
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private ColumnService columnService;
    
    @Autowired
    private ColumnArticleMapService columnArticleMapService;
    
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	@Autowired
	private ResourcePublishMapService resourcePublishMapService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private PictureService pictureService;
	
	@Autowired
	private CompanyService companyService;
    
    @RequestMapping(value = "/articleList.action")
    public String articleList(HttpServletRequest request, ModelMap modelMap, Article search) throws Exception {
    	boolean queryFlag = false;
    	if((search.getArticleId() != null&&!search.getArticleId().isEmpty()) || (search.getArticleName() != null&&!search.getArticleName().isEmpty()) || (search.getTitle() != null&&!search.getTitle().isEmpty()) || search.getStatus() != null || search.getCompanyNo() != null || search.getOperatorNo() != null){
    		queryFlag = true;
    	}
    	//获取登录人
        Operator curOper = SessionUtil.getActiveOperator(request);    
        search.setCurOper(curOper); 
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	if((queryFlag && search.getOperatorNo() == null ) || !queryFlag){
	        	search.setGroupId(curOper.getOperatorNo());
	        	search.setOperatorNo(curOper.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ARTICLE.getIndex(), curOper.getOperatorNo()));
        	}
        	else{
        		search.setGroupId(search.getOperatorNo());
	        	search.setOperatorNo(search.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ARTICLE.getIndex(), search.getOperatorNo()));
        	}
        	break;
        case ORDINARY_OPER:
        	if((queryFlag && search.getOperatorNo() == null ) || !queryFlag){
	        	search.setGroupId(curOper.getCreateBy());
//	        	search.setOperatorNo(curOper.getCreateBy());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ARTICLE.getIndex(), curOper.getCreateBy()));
        	}
        	else{
        		search.setGroupId(search.getOperatorNo());
//	        	search.setOperatorNo(search.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ARTICLE.getIndex(), search.getOperatorNo()));
        	}
        	break;
        }
        List<Article> list = null;
       	list = this.articleService.selectArticles(search);
       	if(list != null){
       		Long opNo = curOper.getOperatorNo();
       		if(curOper.getType().intValue() == 2){
       			opNo = curOper.getCreateBy();
       		}
       		for (Article article : list) {
				Long createdBy = article.getOperatorNo();
				if(article.getOperator().getType().intValue() == 2){
					createdBy = article.getOperator().getCreateBy();
				}
				if(curOper.isSuperAdmin()||opNo.longValue() == createdBy.longValue()){
					article.setAllocRes(null);
				}
				else{
					article.setAllocRes(1l);
				}
			}
       	}
       	List<Company> companyList = companyService.findAllCompanys(curOper);
       	List<Operator> operatorList = operatorService.findAllOperators(curOper);
        modelMap.put("list", list);
        modelMap.put("pageUtil", page);
        if(!queryFlag){
        	search.setOperatorNo(null);
        }
        modelMap.put("search", search);
        modelMap.put("companyList", companyList);
        modelMap.put("operatorList", operatorList);
        modelMap.put("articleStatusMap", ConstConfig.articleStatusMap);
        return "textmgmt/article/articleList";
    }
  
    @RequestMapping(value = "/articleDetail.action")
    public String articleDetail(HttpServletRequest request, ModelMap modelMap, Long articleNo,Long columnNo) throws Exception {
    	Article rec = null;
        try {
        	rec = articleService.selectByNoV2(articleNo);
        	if (rec.getTemplateId() != null){
        		Template template = templateService.findTemplateById(rec.getTemplateId());
        		rec.setTemplate(template);
        	}
        	
    		List<ResourcePublishMap> publishList = resourcePublishMapService.findResourcePublishMapById(
    				EntityType.TYPE_COLUMN, columnNo, EntityType.TYPE_ARTICLE, articleNo);
    		if(ArrayUtils.isEmpty(publishList)) {
    			publishList=null;
    		}
    		modelMap.put("publish", publishList);
        }
        catch (Exception e) {
        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
        }
        modelMap.put("article", rec);
        return "textmgmt/article/articleDetail";
    }    

    @RequestMapping(value = "/articleNew.action")
    public String articleNew(HttpServletRequest request, ModelMap modelMap, Article search) throws Exception {
        try {
            Operator curOper = SessionUtil.getActiveOperator(request);
            logger.debug(curOper.getOperatorName() + "enter articleNew");
            Company company = new Company();
            company.setCurOper(curOper);
            company.getPageUtil().setPaging(false); // return all company that this operator can select
            List<Company> companyList = companyService.findOperatorCompanys(company);
            
            Column col = new Column();
            col.setCurOper(curOper);
            List<Column> cols = columnService.selectAllColumns(col);
            
        	Template tsearch = new Template();
        	tsearch.setType(TemplateType.ARTICLE.getIndex());
        	tsearch.setStatus(TemplateStatus.ENABLE.getIndex());
        	List<Template> templateList = templateService.findAllTemplates(tsearch);
        	modelMap.addAttribute("templateList", templateList);
            //modelMap.put("systemConfigList", systemConfigList);

            modelMap.put("columns", cols);
            modelMap.put("search", search);
            modelMap.put("companyList", companyList);
        }
        catch (Exception e) {
        	logger.error("articleNew exception occurred, cause by:{}", e);
        }
        
        return "textmgmt/article/articleNew";
    }
    
//    @RequestMapping(value = "/view/{filename:.*}") 
//    public FileSystemResource getArticleImage(HttpServletRequest request, @PathVariable("filename") String filename) throws Exception {
//    	String filePath = this.articleService.getFilePath(filename);
//    	
//    	FileSystemResource ret = new FileSystemResource(filePath);
//    	return ret;
//    }
    
    /**
     * get article image/resource
     * @param request
     * @param search
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/view/{filename:.*}") 
    public void getArticleImage(HttpServletRequest request, HttpServletResponse response, @PathVariable("filename") String filename) throws Exception {
    	String filePath = this.articleService.getFilePath(filename);
    	
    	ServletContext context = request.getSession().getServletContext();
    	String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
    	
        response.setContentType(mimeType);        
        
        File f = new File(filePath);
        if(!f.exists()) {
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        	return;
        }
        
    	InputStream is = new FileInputStream(f);
    	try {
    		IOUtils.copy(is, response.getOutputStream());
    	}
    	finally {
    		IOUtils.closeQuietly(is);
    	}
    	
    }
    
    @RequestMapping(value = "/articleEdit.action")
    public String articleEdit(HttpServletRequest request, Article search, ModelMap modelMap) throws Exception {
        try {
        	logger.debug("enter articleEdit, articleNo: " + search.getArticleNo());
            Operator curOper = SessionUtil.getActiveOperator(request);
            search.setCurOper(curOper);
            Long no = search.getArticleNo();
            PageUtil page = search.getPageUtil();
            page.setPaging(true);
            if(no != null) {
            	Article art = this.articleService.selectByNoWithPictures(search);
                modelMap.put("article", art);
            	if (art.getTemplateId() != null){
            		Template template = templateService.findTemplateById(art.getTemplateId());
            		art.setTemplate(template);
            	}
            }
            
        	Template tsearch = new Template();
        	tsearch.setType(TemplateType.ARTICLE.getIndex());
        	tsearch.setStatus(TemplateStatus.ENABLE.getIndex());
        	List<Template> templateList = templateService.findAllTemplates(tsearch);
        	modelMap.addAttribute("templateList", templateList);
        	
        	modelMap.put("operator", curOper);
            //modelMap.put("systemConfigList", systemConfigList);
            modelMap.put("search", search);
            modelMap.put("pageUtil", page);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "textmgmt/article/articleEdit";
    }

    /**
     * select column for article
     * @param request
     * @param modelMap
     * @param search
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/articleColumnSelect.action")
    public String articleColumnSelect(HttpServletRequest request, ModelMap modelMap, Long articleNo, Column search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        List<ColumnArticleMap> list = null;

    	PageUtil page = search.getPageUtil();
    	page.setPaging(true);
    	if (search.getArticleNo()!=null) {
    		list = this.columnArticleMapService.selectArticleColumn(search);
    	}
    	else {
    		list = new ArrayList<ColumnArticleMap>();
    	}
           	
        modelMap.put("articleNo", articleNo);
        modelMap.put("list", list);
        modelMap.put("pageUtil", page);
        modelMap.put("search", search);
        return "textmgmt/article/articleColumnSelect";
    }
    
    /**
     * view, add new column for article
     * @param request
     * @param modelMap
     * @param articleNo
     * @param search
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/articleAddNewColumn.action")
    public String articleAddNewColumn(HttpServletRequest request, ModelMap modelMap, Long articleNo, Column search) throws Exception {
        logger.debug("enter articleAddNewColumn..");
    	Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	break;
        case ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), curOper.getOperatorNo());
        	search.setAllocResourceIds(allocResourceIds);
        	break;
        }
        
    	PageUtil page = search.getPageUtil();
    	page.setPaging(true);
    	search.setArticleNo(articleNo);
    	List<Column> list;
    	if(articleNo!=null) {
    		list = this.columnService.selectNewColumnForArticle(search);    		
    	}
    	else {
    		list = new ArrayList<Column>();
    	}

        modelMap.put("articleNo", articleNo);
        modelMap.put("list", list);
        modelMap.put("pageUtil", page);
        modelMap.put("search", search);
        return "textmgmt/article/articleAddNewColumn";
    }
    
    //-------------------------------------------------------------------------
    // json actions
    //-------------------------------------------------------------------------
    
    @RequestMapping(value = "/checkArticle.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String checkArticle(Article search) throws Exception {
        boolean result = false;
        
        Integer s = search.getStatus();
        if(s==null || s==0) { // check Id, skip articleName
        	search.setArticleName("");
        }
        else if(s==1) { // // check name, skip articleId
        	search.setArticleId("");
        }
        else {
        	result = true;
        }
        result = this.articleService.isArticleRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }
    
    
    @RequestMapping(value = "/addArticle.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String addArticle(HttpServletRequest request, Article article) throws Exception {
    	logger.debug("enter addArticle...");
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	Result result = new Result();
        String logStr = "";
        try {
            if (article != null) {
                article.setCurOper(curOper);
                article.setGroupId(curOper.getCreateBy());
                article.setArticleId(StringUtils.generateFixPrefixId("AC"));
                //保存文章
                this.articleService.addArticle(article);
                if(article.getBody() != null && article.getBody().length() > 0) { //上传正文
                	//计算用户使用空间
                	curOper = operatorService.findOperatorById(curOper.getOperatorNo());
                	File f = new File (this.articleService.getFileDir() ,article.getBody());
                	operatorService.calculateUsedSpace(curOper, f, true);
                }
                
                logStr = "添加[" + article.getArticleName() + "]文章详情";
                result.setResult(true);
                logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
            }
        }
        catch (Exception e) {
        	result.setDesc("");
        	logger.error("addArticle exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    }
    
    @RequestMapping(value = "/updateArticle.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String updateArticle(HttpServletRequest request ,HttpServletResponse response,Article article) throws Exception {   	
    	logger.debug("enter updateArticle...");
        Operator curOper = SessionUtil.getActiveOperator(request);
        article.setCurOper(curOper);
    	Result result = new Result();
        String logStr = "";
        try {
            if (article != null) {
            	if(article.getArticleNo()==null) {
            		throw new Exception("updateArticle no null.");
            	}         	
            	File fileDir = this.articleService.getFileDir();
            	File f = null;
            	
            	//已上传文章正文，只改变文章正文内容
            	if(article.getBody() != null && article.getBody().length() > 0){
            		f = new File(fileDir.toString() + "/" + article.getBody());
            		FileUtils.delFile(fileDir.toString()+article.getBody()); //删除原有文件
                    operatorService.calculateUsedSpace(curOper, f, false);
            		//重新生成文章正文
            		FileUtils.newFile(fileDir.toString(), article.getBody(), article.getEditArticleBody());
            		 //计算管理员存储空间
                    operatorService.calculateUsedSpace(curOper, f, true);
            	}
            	else { //添加文章正文
            		String fileName = FileUpload.geneFileName("aa.txt");
            		article.setBody(fileName);
            		FileUtils.newFile(fileDir.toString(), fileName, article.getEditArticleBody());
            		f = new File(fileDir.toString() + "/" + article.getBody());
            		operatorService.calculateUsedSpace(curOper, f, true);
            	}
            	//更新
            	article.setStatus(ConstConfig.STATUS_EDIT);
                this.articleService.updateArticle(article);
                logger.debug("update article successful...");
                logStr = "更新[" + article.getArticleName() + "]文章详情";
                result.setData(article.getBody());
                result.setResult(true);
                logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
            }
        }
        catch (Exception e) {
        	result.setDesc(e.getMessage());
        	logger.error("Save or update article exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    }
    
    /**
     * 上传文章正文
     * @param request
     * @param file
     * @param filename
     * @param article
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadArticleBody.action", method = {RequestMethod.POST}, produces = {ConstConfig.APP_JSON,ConstConfig.TEXT_PLAIN})
    @ResponseBody
    public String uploadArticleBody(HttpServletRequest request, @RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String filename, Article article) throws Exception {
    	logger.debug("enter uploadArticleBody...");
    	UploadResult result = new UploadResult();
        try { 
        	File fileDir = this.articleService.getFileDir();  //文章正文路径
        	String genFileName = FileUpload.geneFileName(filename);
        	boolean isok = FileUpload.saveFile(file, fileDir.toString(), genFileName);
        	logger.debug("uploadArticleBody" + filename + ",isSuccessful:" + isok);
        	if(file != null && !isok) {
    			String err = String.format("上传文件:%s失败", genFileName);
    			throw new Exception(err);
    		}
        	File f = new File(fileDir, genFileName);
        	//检查编码
        	String charset = FileUtils.get_charset(f);
    		if(!"UTF-8".equals(charset)) {
    			result.setResult(false);
    			if(f.exists() && f.isFile()) {
    				f.delete();
    			}
    			result.setDesc("文章正文只支持UTF-8编码");
    			logger.debug(filename + "is deleted,bucause the coding is not UTF-8");
    		} else  { 
    			if(f.exists() && f.isFile()){
    				InputStream is = new FileInputStream(f);
    				BufferedReader raf = new BufferedReader(new InputStreamReader(is, ConstConfig.DEF_ENCODING)); 
    				List<String> words = WordstockServiceImpl.wordstocks;
    				String line = raf.readLine();
    				boolean flag = true;
    				while (line != null && line.length() > 0 && flag) {
    					for (String wordstock : words) {
    						if(!line.contains(wordstock)) { //不包含生僻字
    							continue;
    						}else {
    							flag = false;
    							break;
    						}
    					}
    					line = raf.readLine();
    				}
    				is.close();
            		raf.close();
    				if(flag) {
    					result.setFilePath(f.getName());
    					result.setDesc("");
                		result.setResult(true);
    				} else {
						result.setDesc("文章包含生僻字，请确认");
						f.delete();
						logger.debug(filename + "is deleted,bucause of containing wordstocks");
					}
    			}
        	}	
        }
        catch (Exception e) {
        	result.setDesc("");
        	logger.error("upload  ArticleBody exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    }
  
    @ResponseBody
    @RequestMapping(value = "/uploadArticleImage.action", method = {RequestMethod.POST}, 
    		produces = {ConstConfig.APP_JSON, ConstConfig.TEXT_PLAIN} )
    public String uploadArticleImage(HttpServletRequest request, @RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String filename, Article article) throws Exception {
    	logger.debug("enter uploadArticleImage...");
    	UploadResult result = new UploadResult();
        try {        	
        	Operator curOper = SessionUtil.getActiveOperator(request);
        	if(curOper==null) { // firefox in some version can not get curOper !
            	Long operatorNo = article.getOperatorNo();
            	Operator op = operatorService.findOperatorById(operatorNo);
            	if (op==null) {
            		throw new Exception("用户错误, operatorNo:" + operatorNo);
            	}
            	article.setCurOper(op);
        	}
        	else {
            	article.setCurOper(curOper);
        	}
        	String dir = this.articleService.getFileDir().getCanonicalPath();
    		String genFileName = FileUpload.geneFileName(filename);
    		String filePath = dir + "/" +genFileName;
    		if (!operatorService.validateUsedSpace(curOper, file.getSize())){
    			logger.debug(curOper.getOperatorName() + "usedSpace is full...");
    			return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + genFileName + "',desc:'spaceSizeLimit'}";
    	    }
    		boolean isok = FileUpload.saveFile(file, dir, genFileName);
    		logger.debug(curOper.getOperatorName() + "uploadArticleImage..." + filename +"result:"+isok);
    		//保存图片
    		Picture p = new Picture();
    		p.setCurOper(article.getCurOper());
    		article = this.articleService.selectByNo(article.getArticleNo());
    		p.setShowOrder(100);
    		if(filename.length() > 30){
    			filename = filename.substring(0,30);
    		}
    		p.setPicName(filename);
    		MD5Engine engine = new MD5Engine(false);
    		String md5 = engine.calculateMD5(dir + "/" + genFileName);
    		p.setCheckCode(md5);
    		p.setPicPath(genFileName);
    		//p.setCheckCode("");
    		p.setArticleNo(article.getArticleNo());
    		
    		int max = this.articleService.getArticleMaxResNo(article);
    		p.setResNo(max+1);	
    		p.setStatus(ConstConfig.STATUS_EDIT);
    		p.setCompanyNo(article.getCompanyNo());		
  
    		//Operator curOper = p.getCurOper();
    		if(curOper!=null) {
    			if(p.getCompanyNo()==null) {
    				p.setCompanyNo(curOper.getCompanyNo());
    			}
    			if(p.getOperatorNo()==null) {
    				p.setOperatorNo(curOper.getOperatorNo());
    			}
    		}
    		
    		p.setId(this.pictureService.getPrimaryKey()); 
    		p.setCreateTime(DateUtil.getCurrentTime());
    		p.setUpdateTime(DateUtil.getCurrentTime());
    		this.pictureService.addPicture(p);
    		//计算管理员空间
    		if (isok){
    			operatorService.calculateUsedSpace(curOper, new File(filePath), true);
    		}
    		
        	result.setFilePath(filePath);
        	result.setResult(true);
        }
        catch (Exception e) {
        	result.setDesc(e.getMessage());
        	logger.error("articleUpdateBody exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    }

    @RequestMapping(value = "/deleteArticle.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String deleteArticle(HttpServletRequest request, Long[] rtId) throws Exception {
    	logger.debug("enter deleteArticle...");
    	Result result = new Result();
        String delName="";
        Operator curOper = SessionUtil.getActiveOperator(request); //当前登录人
        try {
        	
            if (rtId != null && rtId.length > 0) {
            	List<Article> cs = null;
            	try {
            		cs = articleService.selectByNos(rtId);
            	}
            	catch(Exception e) {
            		throw new Exception("查询板块数据错误");
            	}
            	
            	for(int i=0;i<cs.size();i++) {
            		Article c = cs.get(i);
           			if(i!=0) {
           				delName +=",";
           			}
           			delName+=c.getArticleName();
           			c.setCurOper(curOper);
            	}
 	
            	if(cs!=null && cs.size()>0) {
            		articleService.deleteArticles(cs);
            	}

            	result.setDesc("文章[ "+delName +" ]删除");
            	result.setResult(true);
            }
        }
        catch (Exception e) {
        	logger.error("deleteArticle exception occurred, cause by:{}", e);
            result.setDesc(e.getMessage());
        }

        logService.logToDB(request,result.getDesc(), LogUtil.LOG_INFO, result.getResult(), true);
        String ret = JSON.toJSONString(result);
        return ret;
    }
    
	@RequestMapping(value = "/auditArticle.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String auditArticle(HttpServletRequest request, Integer status, Long[] rtId) throws Exception {
		Result result = new Result();
		String str = "";
		try {
	        Operator curOper = SessionUtil.getActiveOperator(request);//当前登录人
	        Long createdBy = curOper.getOperatorNo();
			if(curOper.getOperator().getType().intValue() == 2){
				createdBy = curOper.getOperator().getCreateBy();
			}
	        ResourcePublishMap pulibsh = new ResourcePublishMap();
	        pulibsh.setCreatedBy(createdBy);
	        if (rtId != null && rtId.length > 0) {
				List<Article> list = articleService.selectByNos(rtId);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getArticleName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getArticleName();
				Article art = new Article();
				art.setCurOper(curOper);
				art.setStatus(status);
				articleService.audit(art, rtId, pulibsh);
				
				// notify portal
				if(status==ConstConfig.STATUS_PUBLISH || status==ConstConfig.STATUS_UNPUBLISH) {
					processor.putNoticeToQueue(pulibsh.getNoticeList());
				}
				
				result.setDesc(str);
				result.setResult(true);
				logService.logToDB(request, "文章[" + str + "] " + Article.getStatusName(status), LogUtil.LOG_INFO, true, true);
			}
		}
		catch (Exception e) {
			logger.error("Topic on submit audit exception occurred, cause by:{}", e);
			String err = e.getMessage();
			result.setDesc(err);
		}
		
		String ret = JSON.toJSONString(result);
		return ret;
	}
	
	
	// for column-article map
    @RequestMapping(value = "/addColumnArticleMap.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String addColumnArticleMap(HttpServletRequest request, Long articleNo, Long[] columnNo) throws Exception {
    	Result result = new Result();
        Operator curOper = SessionUtil.getActiveOperator(request);
        Long createdBy = curOper.getOperatorNo();
        if(curOper.getType().intValue() == 2){
        	createdBy = curOper.getCreateBy();
        }
        try {
        	// TODO: check permission        	
        	if(articleNo==null || columnNo==null || columnNo.length<1) {
        		throw new Exception("增加板块数据错误");
           	}
        	
        	columnArticleMapService.addColumnArticleMap(articleNo, columnNo,createdBy);
            result.setResult(true);
        }
        catch (Exception e) {
        	logger.error("addColumnArticleMap exception occurred, cause by:{}", e);
            result.setDesc(e.getMessage());
        }

        String ret = JSON.toJSONString(result);
        return ret;
    }

    @RequestMapping(value = "/deleteColumnArticleMap.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String deleteColumnArticleMap(HttpServletRequest request, Long[] rtId) throws Exception {
    	Result result = new Result();
        String delName="";
        
        //Operator curOper = SessionUtil.getActiveOperator(request);//当前登录人
        try {
        	// TODO: check permission
            if (rtId != null && rtId.length > 0) {
            	List<ColumnArticleMap> maps = null;
            	try {
            		maps = columnArticleMapService.selectByNos(rtId);
            	}
            	catch(Exception e) {
            		throw new Exception("查询板块数据错误");
            	}
            	
            	columnArticleMapService.deleteColumnArticleMaps(maps);

            	result.setDesc("板块["+delName +"]删除");
            	result.setResult(true);
            }
        }
        catch (Exception e) {
        	logger.error("deleteArticle exception occurred, cause by:{}", e);
            result.setDesc(e.getMessage());
        }

        String ret = JSON.toJSONString(result);
        return ret;
    }
    
    @RequestMapping(value="/articleBodyEdit.action" ,produces = {ConstConfig.APP_JSON ,ConstConfig.TEXT_PLAIN})
    public String articleBodyEdit(Long articleNo ,String txtFileName ,ModelMap modelMap){
    	try {
    		logger.debug("enter articleBodyEdit, the input param articleNo:" + articleNo + "and txtFileName:" + txtFileName);	    	
	    	Article article = this.articleService.selectByNo(articleNo);
	    	if(txtFileName != null && txtFileName.length() > 0) {  //用户已上传正文
	    		String bodyPath = this.articleService.getFilePath(txtFileName); 
				String body = FileUtils.readFileContent(bodyPath, ConstConfig.DEF_ENCODING);
				article.setEditArticleBody(body);
	    	}
	    	else {  //用户直接编辑正文
	    	}
	    	modelMap.put("article", article);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "textmgmt/article/articleBodyEdit";
    }

	@RequestMapping(value = "/toArticlePublish.action")
	public String toArticlePublis(HttpServletRequest request, ModelMap modelMap, String articleNos) throws Exception {
	
		Column search = new Column();
		search.setStatus(AuditStatus.PUBLISH);

		Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        		search.setGroupId(curOper.getOperatorNo());
	        break;
        case ORDINARY_OPER:
        		search.setGroupId(curOper.getCreateBy());
	        break;
        }
        Long[] resourceIds = ArrayUtils.getIdArray(articleNos);
		List<Column> columnList = columnService.findAllResourceColumnNoPublishs(resourceIds, search);
        
		modelMap.put("articleNos", articleNos);
		modelMap.put("columnList", columnList);
		
		return "textmgmt/article/articlePublish";
	}

	@RequestMapping(value = "/articlePublish.action")
	@ResponseBody
	public String articlePublish(HttpServletRequest request,
			ModelMap modelMap, String articleNos,ResourcePublishMap publish) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		publish.setCreatedBy(createdBy);
		boolean result = false;
		String str = "";
		try {
			
			String[] tempIdArr = articleNos.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			if (ids != null && ids.length > 0) {
				List<Article> list = articleService.selectByNos(ids);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getArticleName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getArticleName();
				
				Article art = new Article();
				art.setCurOper(curOper);
				art.setStatus(ArticleStatus.PUBLISH.getIndex());
				articleService.audit(art, ids, publish);
			}
			
			processor.putNoticeToQueue(publish.getNoticeList());

			result = true;
			logService.logToDB(request, "发布文章[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "发布文章[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error(
					"Publish articles exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	
	@RequestMapping(value = "/toArticleSinglePublish.action")
	public String toAlbumSinglePublish(HttpServletRequest request,
			ModelMap modelMap, String parentIds, Long resourceId) throws Exception {
        
		modelMap.put("parentIds", parentIds);
		modelMap.put("resourceId", resourceId);
		return "textmgmt/article/articleSinglePublish";
	}
	
	@RequestMapping(value = "/articleSinglePublish.action")
	@ResponseBody
	public String articleSinglePublish(HttpServletRequest request,
			ModelMap modelMap, String parentIds, ResourcePublishMap publish) throws Exception {

		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		publish.setCreatedBy(createdBy);
		boolean result = false;
		String str = "";
		Article article = null;
		try {
			
			article = articleService.selectByNo(publish.getResourceId());
			String[] tempIdArr = parentIds.split(Delimiters.COMMA);
			Long[] ids = new Long[tempIdArr.length];
			for (int i=0; i < tempIdArr.length; i++){
				ids[i] = Long.valueOf(tempIdArr[i]);
			}
			
			if (ids != null && ids.length > 0) {
				List<Column> list = columnService.selectByNos(ids);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getColumnName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getColumnName();
				
				Article art = new Article();
				art.setCurOper(curOper);
				articleService.articleSinglePublish(art, ids, ArticleStatus.PUBLISH.getIndex(), publish);

			}
			processor.putNoticeToQueue(publish.getNoticeList());
			
			result = true;
			logService.logToDB(request, "发布文章[" + article.getArticleName() + "]到[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "发布文章[" + article.getArticleName() + "]到[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error(
					"Publish articles exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}


//    /*
//     * article选择
//     */
//    @RequestMapping(value = "/articleSelectList.action")
//    public String articleSelectList(HttpServletRequest request, ModelMap modelMap, article search) throws Exception {
//        Operator curOper = SessionUtil.getActiveOperator(request);
//        search.setCurOper(curOper);
//        List<article> belongList = null;
//        List<article> distarticleList = null;
//        List<article> list = null;
//        List<Long> articleNoList = new ArrayList<Long>();
//    	Map<Long, article> distarticleMap = new HashMap<Long, article>();
//        switch (OperatorType.getType(curOper.getType())){
//        case SUPER_ADMIN:
//        	search.setarticleName(search.getKeyWord());
//        	belongList =  articleService.findAllarticles(null, null);
//        	list = this.articleService.findarticles(search, null, null);
//        	break;
//        case article_ADMIN:
//        	article cmpy = articleService.findarticleByNo(curOper.getarticleNo());
//        	if (search == null || StringUtils.isEmpty(search.getSearchPath())){
//            	search.setPath(cmpy.getPath());
//        	}
//
//            belongList =  articleService.findAllarticles(cmpy, null);
//        	search.setarticleName(search.getKeyWord());
//        	list = this.articleService.findarticles(search, null, null);
//        	break;
//        case ORDINARY_OPER:
//            distarticleList = articleService.findDistarticlesByOperNo(curOper.getOperatorNo());
//        	List<Long> createarticleNoList = articleService.findCreatearticleNosByOperNo(curOper.getOperatorNo());
//        	
//        	if (distarticleList != null && distarticleList.size() > 0){
//        		for (article temp: distarticleList){
//        			articleNoList.add(temp.getarticleNo());
//        			distarticleMap.put(temp.getarticleNo(), temp);
//        		}
//        	}
//        	
//        	if (createarticleNoList != null && createarticleNoList.size() > 0){
//        		articleNoList.addAll(createarticleNoList);
//        	}
//        	
//        	if (createarticleNoList == null || createarticleNoList.size() <= 0){
//        		createarticleNoList.add(Long.valueOf(-1));
//        	}
//        	
//        	search.setarticleName(search.getKeyWord());
//        	list = this.articleService.findarticles(search, articleNoList, createarticleNoList);
//        	articleNoList.add(curOper.getarticleNo());
//            belongList =  articleService.findAllarticles(null, articleNoList);
//        	if (distarticleList != null && distarticleList.size() > 0){
//            	Iterator<article> it = list.iterator();
//            	while (it.hasNext()){
//            		article cpy = it.next();
//            		if (distarticleMap.containsKey(cpy.getarticleNo())){
//            			article distCpy = distarticleMap.get(cpy.getarticleNo());
//            			cpy.setHasDistribution(distCpy.getHasDistribution());
//            			cpy.setDistributeBy(distCpy.getDistributeBy());
//            			cpy.setDistributeTime(distCpy.getDistributeTime());
//            		}
//            	}
//        	}
//        	break;
//        }
//        
//        modelMap.put("belongList", belongList);
//        modelMap.put("list", list);
//        modelMap.put("pageUtil", search.getPageUtil());
//        modelMap.put("search", search);
//    	return "textmgmt/article/articleSelectList";
//    }
//    
//    /*
//     * article选择
//     */
//    @RequestMapping(value = "/findarticleSelectList.action")
//    public String findarticleSelectList(HttpServletRequest request, ModelMap modelMap, article search) throws Exception {
//    	PageUtil page = search.getPageUtil();
//    	search.getPageUtil().setPaging(true);
//    	Operator curOper = SessionUtil.getActiveOperator(request);
//    	List<article> list=null;
//    	switch (OperatorType.getType(curOper.getType())){
//        case SUPER_ADMIN:
//        	search.setarticleName(search.getKeyWord());        	
//        	list = this.articleService.findarticles(search, null, null);
//        	break;
//        default:
//    	if(search.getarticleNo()!=null&&!search.getarticleNo().equals("")){
//    		
//    	}else{
//    		search.setarticleNo(curOper.getarticleNo());
//    	}   	
//		int count = articleService.articleSelectListCount(search);
//		page.setRowCount(count);
//		list=articleService.articleSelectList(search);
//    	}
//		modelMap.put("list", list);
//	    modelMap.put("pageUtil", search.getPageUtil());
//	    modelMap.put("search", search);	  
//    	
//		return "textmgmt/article/articleSelectList";
//    }
	
	/** 
	 * 查询有文章权限的用户
	 */ 
	@RequestMapping(value = "/findArticleOperator.action")
	public String findArticleOperator(HttpServletRequest request,ModelMap modelMap,Long articleNo, Operator search) throws Exception {  
		    List<String> operList = null;  
		    List<Operator> list = new ArrayList<Operator>();   
		    String type = "4"; 
		    Long resourceId = articleNo;
			if(articleNo != null){ 
				operList = operatorService.findResourceOperator(resourceId,type);  
			  for(int i=0;i<operList.size();i++){  
				Long operNo = Long.parseLong(operList.get(i)); 
				list.add(operatorService.findOperatorById(operNo)); 
			  }
			}
		    modelMap.put("createOperNo", null);
		    modelMap.put("type", null);
		    modelMap.put("resourceId", null);
		    modelMap.put("list", list);  
		    modelMap.put("pageUtil", search.getPageUtil());
            modelMap.put("belongList", null);
            modelMap.put("search", search);
            modelMap.put("userTypeMap", ConstConfig.userTypeMap); 
		    return "sysmgmt/user/resourceUserAllocList";
	} 
	
}
