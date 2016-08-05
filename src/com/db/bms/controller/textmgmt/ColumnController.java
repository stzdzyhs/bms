package com.db.bms.controller.textmgmt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.entity.Article;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.Company;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Template;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Template.TemplateStatus;
import com.db.bms.entity.Template.TemplateType;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnArticleMapService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.service.TemplateService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FileUpload;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.Result;
import com.db.bms.utils.Result2;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("textmgmt/column")
@Controller
public class ColumnController {

	private final static Logger logger = Logger.getLogger(ColumnController.class);
	
	@Autowired
	private ColumnArticleMapService columnArticleMapService;
	
	@Autowired
	private LogService logService;
	
    @Autowired
    private ColumnService columnService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    CompanyService companySerivce;
    
	@Autowired
	private PortalProcessor processor;    
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	@Autowired
	private ResourcePublishMapService resourcePublishMapService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;

	@Autowired
	private PortalProcessor portalProcessor;
	

	@RequestMapping(value = "/columnList.action")
    public String columnList(HttpServletRequest request, ModelMap modelMap, Column search) throws Exception {
    	boolean queryFlag = false;
    	if((search.getColumnId() != null&&!search.getColumnId().isEmpty()) || (search.getColumnName() != null&&!search.getColumnName().isEmpty()) || search.getStatus() != null || search.getCompanyNo() != null || search.getOperatorNo() != null){
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
        	if((queryFlag && search.getOperatorNo() == null)|| !queryFlag){
	        	search.setGroupId(curOper.getOperatorNo());
	        	search.setOperatorNo(curOper.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), curOper.getOperatorNo()));
        	}
        	else{
        		search.setGroupId(search.getOperatorNo());
	        	search.setOperatorNo(search.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), search.getOperatorNo()));
        	}
        	break;
        case ORDINARY_OPER:
        	if((queryFlag && search.getOperatorNo() == null)|| !queryFlag){
	        	search.setGroupId(curOper.getCreateBy());
//	        	search.setOperatorNo(curOper.getCreateBy());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), curOper.getCreateBy()));
        	}
        	else{
        		search.setGroupId(search.getOperatorNo());
//	        	search.setOperatorNo(search.getOperatorNo());
	        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), search.getOperatorNo()));
        	}
        	break;
        }
    	List<Column> list = this.columnService.selectColumns(search);
    	if(list != null){
	    	Long opNo = curOper.getOperatorNo();
	    	if(curOper.getType().intValue() == 2){
	    		opNo = curOper.getCreateBy();
	    	}
	    	List<ResourceAllocation> resAolList = null;
	    	List<Long> resAllocIdList = null; resAllocIdList = search.getAllocResourceIds();
	        if(resAllocIdList != null && !resAllocIdList.isEmpty()){
	        	resAolList = this.resourceAllocationService.findAllocResourceByIds((Long[])resAllocIdList.toArray(new Long[resAllocIdList.size()]));
	        }
	    	Long createdBy = null;
	    	for (Column column : list) {
	    		column.setCmds(null);
	    		createdBy = column.getOperatorNo();
	    		if(column.getOperator().getType().intValue() == 2){
	    			createdBy = column.getOperator().getCreateBy();
	    		}
	    		if(curOper.isSuperAdmin()||opNo.longValue() == createdBy.longValue()){
	    			column.setAllocRes(null);
	    		}
	    		else{
	    			column.setAllocRes(1l);
	    		}
	    		if(resAolList != null && !resAolList.isEmpty()){
	    			for (ResourceAllocation resAlloc : resAolList) {
						if(opNo.longValue() == resAlloc.getOperatorNo().longValue() && resAlloc.getResourceId().longValue() == column.getColumnNo().longValue()){
							if(resAlloc.getCmdStr() == null || resAlloc.getCmdStr().isEmpty()){
								column.setCmds("");
							}
							else{
								column.setCmds(resAlloc.getCmdStr());
							}
						}
					}
	    		}
			}
    	}
    	List<Company> companyList = companySerivce.findAllCompanys(curOper);
       	List<Operator> operatorList = operatorService.findAllOperators(curOper);
        modelMap.put("list", list);
        modelMap.put("companyList", companyList);
        modelMap.put("operatorList", operatorList);
        modelMap.put("articleStatusMap", ConstConfig.articleStatusMap);
        modelMap.put("pageUtil", page);
        if(!queryFlag){
        	search.setOperatorNo(null);
        }
        modelMap.put("search", search);
        return "textmgmt/column/columnList";
    }
  
    @RequestMapping(value = "/columnDetail.action")
    public String columnDetail(HttpServletRequest request, ModelMap modelMap, Long columnNo) throws Exception {
    	Column col = null;
        try {
        	col = columnService.selectByNoWithCompany(columnNo);
        	if (col.getTemplateId() != null){
        		Template template = templateService.findTemplateById(col.getTemplateId());
        		col.setTemplate(template);
        	}
    		ResourcePublishMap publish = null;
    		//List<ResourcePublishMap> list = resourcePublishMapService.findResourcePublishMapById( 
    		//		col.getParentNo() == null ?null : EntityType.TYPE_COLUMN,null, EntityType.TYPE_COLUMN, columnNo);
    		List<ResourcePublishMap> list = resourcePublishMapService.findResourcePublishMapById( 
    				null, null, EntityType.TYPE_COLUMN, columnNo);
    		
    		if (list != null && list.size() > 0){
    			publish = list.get(0);
    		}

    		modelMap.put("publish", publish);
        }
        catch (Exception e) {
        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
        }
        modelMap.put("column", col);
        return "textmgmt/column/columnDetail";    
    }

    @RequestMapping(value = "/columnNew.action")
    public String columnNew(HttpServletRequest request, ModelMap modelMap, Column search) throws Exception {
        try {
            Operator curOper = SessionUtil.getActiveOperator(request);
            search.setCurOper(curOper);
            List<Column> belongList = columnService.selectAllColumns(search);
            Column column = null;
            
            Company company = new Company();
            company.setCurOper(curOper);
            company.getPageUtil().setPaging(false); // return all company that this operator can select
            List<Company> companyList = companySerivce.findOperatorCompanys(company);
            modelMap.put("companyList", companyList);
            
            //modelMap.put("systemConfigList", systemConfigList);
            modelMap.put("search", search);
            
        	Template tsearch = new Template();
        	tsearch.setType(TemplateType.COLUMN.getIndex());
        	tsearch.setStatus(TemplateStatus.ENABLE.getIndex());
        	List<Template> templateList = templateService.findAllTemplates(tsearch);
        	modelMap.addAttribute("templateList", templateList);
            modelMap.put("column", column);
            modelMap.put("belongList", belongList);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "textmgmt/column/columnEdit";
    }
    
    @RequestMapping(value = "/columnEdit.action")
    public String columnEdit(HttpServletRequest request, Long id, ModelMap modelMap, Column search) throws Exception {
        try {
            Operator curOper = SessionUtil.getActiveOperator(request);
            search.setCurOper(curOper);
            List<Column> belongList = columnService.selectAllColumns(search);
//            List<Column> belongList = columnService.selectAvailableParentColumn(id);
            Column column = null;
            if(id!=null) {
            	column = this.columnService.selectByNoWithCompany(id);
            }

            // not need company list since not allow to change companyNo after created.
            /*
            Company company = new Company();
            search.setCurOper(curOper);
            List<Company> companyList = companySerivce.findAllCompanys(company);
            modelMap.put("companyList", companyList);
            */
        	Template tsearch = new Template();
        	tsearch.setType(TemplateType.COLUMN.getIndex());
        	tsearch.setStatus(TemplateStatus.ENABLE.getIndex());
        	List<Template> templateList = templateService.findAllTemplates(tsearch);
        	modelMap.addAttribute("templateList", templateList);
            //modelMap.put("systemConfigList", systemConfigList);
            modelMap.put("search", search);
            modelMap.put("column", column);
            modelMap.put("belongList", belongList);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "textmgmt/column/columnEdit";
    }
    
    @RequestMapping(value = "/isIdUnique.action",produces = ConstConfig.APP_JSON )
    @ResponseBody
    public String isIdUnique(Column search) throws Exception {
    	Result2<Object> result2 = new Result2<Object>();
    	try {
    		boolean t = this.columnService.isIdUnique(search);
    		if(!t) {
    			result2.result = ResultCode.DUP_ERROR;
    		}
    	}
    	catch(Exception e) {
    		result2 = ResultCode.convertException(e);
    	}
    	String ret = result2.toString();
    	return ret;
    }
    
    @RequestMapping(value = "/isNameUnique.action",produces = ConstConfig.APP_JSON )
    @ResponseBody
    public String isNameUnique(Column search) throws Exception {
    	Result2<Object> result2 = new Result2<Object>();
    	try {
    		boolean t = this.columnService.isNameUnique(search);
    		if(!t) {
    			result2.result = ResultCode.DUP_ERROR;
    		}
    	}
    	catch(Exception e) {
    		result2 = ResultCode.convertException(e);
    	}
    	String ret = result2.toString();
    	return ret;
    }
    
    @RequestMapping(value = "/columnSave.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String columnSaveOrUpdate(HttpServletRequest request, Column column) throws Exception {
    	Result result = new Result();
        String logStr = "";
        File f = null;
        //String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        Operator curOper = SessionUtil.getActiveOperator(request);
        try {
            if (column != null) {
                column.setCurOper(curOper);
            	
                if (column.getColumnNo() != null) {
                	column.setStatus(AuditStatus.DRAFT);
                	column.setUpdateTime(DateUtil.getCurrentTime());
                    this.columnService.updateColumn(column);
                    logStr = "更新[" + column.getColumnName() + "]版块详情";
                }
                else {
                	column.setColumnId(StringUtils.generateFixPrefixId("CL"));
                	column.setGroupId(curOper.getCreateBy());
                    this.columnService.addColumn(column);
                    logStr = "添加[" + column.getColumnName() + "]版块详情";
                }
                result.setResult(true);
                logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, false);
            }
            if (StringUtils.isNotEmpty(column.getCover())){
			    Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
			    f = new File(this.articleService.getFileDir() + "/" + column.getCover());
			    operatorService.calculateUsedSpace(operator, f, true);
			}
        }
        catch (Exception e) {
        	logger.error("Save or update column exception occurred, cause by:{}", e);
        	//logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, false);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    }
    
    @RequestMapping(value = "/uploadColumnCover.action", method = {RequestMethod.POST}, 
    		produces = {ConstConfig.APP_JSON, ConstConfig.TEXT_PLAIN})
    @ResponseBody
    public String uploadColumnCover(HttpServletRequest request, @RequestParam("Filedata") MultipartFile file,
			@RequestParam("Filename") String Filename ,Long operatorNo ,Long columnNo) throws Exception {
    	String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        String fileName = FileUpload.geneFileName(Filename);
        String dirPath =  ConstConfig.UPLOAD_PATH + ConstConfig.COLUMN_FILE_PATH;
        String filePath = dirPath + fileName;
        Operator curOper = operatorService.findOperatorById(operatorNo);
        if (!operatorService.validateUsedSpace(curOper, file.getSize())){
        	return "{result:" + false + ",filePath:'" + filePath + "',fileName:'" + Filename + "',desc:'spaceSizeLimit'}";
        }
        
        boolean isok = FileUpload.saveFile(file, rootPath + dirPath, fileName);
        if (columnNo != null) { //编辑时上传
        	Column column = columnService.selectByNo(columnNo);
        	column.setUpdateTime(DateUtil.getCurrentTime());
        	column.setCover(filePath);
			columnService.updateColumn(column);
        }
        if (isok){
			operatorService.calculateUsedSpace(curOper, new File(rootPath + dirPath + fileName), true);
		}
        return "{result:" + isok + ",filePath:'" + filePath + "',fileName:'" + fileName + "'}";
        /*UploadResult result = new UploadResult();
    	
       try {        	
        	// curOper == NULL since this action is skip SecurityCheck !
        	File dirFile = this.articleService.getFileDir();
    		String genFileName = FileUpload.geneFileName(filename);
    		boolean isok = FileUpload.saveFile(file, dirFile.toString(), genFileName);
        	//String filePath = this.columnService.uploadColumnCover(col, filename, file);
    		File f = new File(dirFile + "/" + genFileName);
    		if (col.getColumnNo() != null) { //编辑时上传
    			Column column = this.columnService.selectByNo(col.getColumnNo());
    			column.setCover(f.toString());
    			columnService.updateColumn(column);
    		}
    		if (isok && f.exists() && f.isFile()) {
    			result.setFilePath(f.getName());
            	result.setResult(true);
    		}
        	
        }
        catch (Exception e) {
        	result.setDesc(e.getMessage());
        	logger.error("uploadColumnCover exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;*/
    }
    /**
     * 删除版块封面
     * @param request 
     * @param columnNo
     * @param filePath
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/columnDeleteCover.action")
	@ResponseBody
	public String columnDeleteCover(HttpServletRequest request,Long columnNo,@RequestParam("filePath")String filePath) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		String rootPath = request.getSession(true).getServletContext().getRealPath("/");
		FileUtils.delFile(rootPath + filePath);
		String str = "删除版块封面";
		if (columnNo != null){
			Column column = columnService.selectByNo(columnNo);
			column.setCover(null);
			columnService.updateColumn(column);
			str = "删除版块["+ column.getColumnName() +"]封面";
			
			Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
			File file = new File(rootPath + filePath);
			operatorService.calculateUsedSpace(operator, file, false);
		}
		logService.logToDB(request, str, LogUtil.LOG_INFO, true, true);
		return "";
    }

    
    
	@RequestMapping(value = "/getCompanyColumns.action", produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String getCompanyColumns(HttpServletRequest request, Column search) throws Exception {
		Result result = new Result();
		try {
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
            	break;
            }
            
			result.data = this.columnService.selectCompanyColumns(search);
			result.setResult(true);
		}
		catch (Exception e) {
			logger.error("getCompanyColumns exception occurred, cause by:{}", e);
			result.setDesc(e.getMessage());
		}

		String ret = JSON.toJSONString(result);
		System.out.println("ret:" + ret);
		return ret;
	}    

    @RequestMapping(value = "/columnDelete.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String columnDelete(HttpServletRequest request, Long[] rtId) throws Exception {
    	Result2<Object> result = new Result2<Object>();
        String delName = ""; 
        String logStr = "";
        Operator curOper = SessionUtil.getActiveOperator(request);//当前登录人
        try {
        	if (rtId != null && rtId.length > 0) {
        		List<Column> colList = this.columnService.selectByNos(rtId);
        		//遍历，是否包含子版块
        		boolean flag = true;
        		for (int i=0; i<colList.size(); i++) {
        			Column c = colList.get(i);
        			flag = this.columnService.isCotainChildCol(c.getColumnNo());
        			if(i!=0) { //组装日志
        				delName +=",";
        			}
        			delName += colList.get(i).getColumnName();
        			if (flag) {
            			throw new ResultCodeException(ResultCode.REF_ERROR, "["+c.columnName + "]包括子版块");
        			}
        			 //释放管理员存储空间
        			if (StringUtils.isNotEmpty(colList.get(i).getCover())){
        				Operator operator = operatorService.findOperatorById(curOper.getOperatorNo());
        				File f = new File(this.articleService.getFileDir() + "/" + colList.get(i).getCover());
        				operatorService.calculateUsedSpace(operator, f, false);
        			}
        			
        		}
       			//result.setDesc("删除["+ delName+"]版块");
       			logStr = "删除["+ delName+"]版块";      			 
       			this.columnService.deleteColumns(rtId);
        	}
		} 
        catch (Exception e) {
			logger.error("columnDelete exception occurred, cause by:{}", e);
			ResultCodeException.convertException(result, e);
		}
        
        /*try {
        	
            if (rtId != null && rtId.length > 0) {
            	List<Column> cs = null;
            	try {
            		cs = columnService.selectByNos(rtId);
            	}
            	catch(Exception e) {
            		throw new Exception("查询板块数据错误");
            	}
            	
            	for(int i=0;i<cs.size();i++) {
            		Column c = cs.get(i);
           			if(i!=0) {
           				delName +=",";
           			}
           			delName+=c.getColumnName();
           			c.setCurOper(curOper);
            	}
 	
            	if(cs!=null && cs.size()>0) {
            		columnService.deleteColumns(cs);
            	}

            	result.setDesc("板块["+delName +"]删除");
            	result.setResult(true);
            }
        }
        catch (Exception e) {
        	logger.error("columnDelete exception occurred, cause by:{}", e);
            result.setDesc(e.getMessage());
        }*/

        logService.logToDB(request,logStr, LogUtil.LOG_INFO, result.getResult()==0, true);
        String ret = JSON.toJSONString(result);
        return ret;
    }
    
	@RequestMapping(value = "/columnAudit.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
	@ResponseBody
	public String columnAudit(HttpServletRequest request, Integer status, Long[] rtId) throws Exception {
		Result result = new Result();
		String str = "";
		try {
			ResourcePublishMap publish = new ResourcePublishMap();
	        Operator curOper = SessionUtil.getActiveOperator(request);//当前登录人
	        Long createdBy = curOper.getOperatorNo();
			if(curOper.getOperator().getType().intValue() == 2){
				createdBy = curOper.getOperator().getCreateBy();
			}
			publish.setCreatedBy(createdBy);
	        if (rtId != null && rtId.length > 0) {
				List<Column> list = columnService.selectByNos(rtId);
				
				Column c = new Column();
				c.setCurOper(curOper);
				columnService.audit(c, rtId, status, publish);

				// notify portal
				if(status==ConstConfig.STATUS_PUBLISH || status==ConstConfig.STATUS_UNPUBLISH) {
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setResourceType(EntityType.TYPE_COLUMN);
					if(status==ConstConfig.STATUS_PUBLISH) {
						notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
					}
					else {
						notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
					}
					processor.putNoticeToQueue(publish.getNoticeList());
				}
				
				str = StringUtils.concatListProperty(list,  Column.class,  "columnName");
				result.setDesc(str);				
				result.setResult(true);
				
				logService.logToDB(request, "板块[" + str + "] " + Column.getStatusName(status), LogUtil.LOG_INFO, true, true);
			}
		}
		catch (Exception e) {
			logger.error("Column on submit audit exception occurred, cause by:{}", e);
		}
		
		String ret = JSON.toJSONString(result);
		return ret;
	}
	
	/**
	 * 板块所包含文章
	 * @param request
	 * @param modelMap
	 * @param columnNo 版块主键
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/columnArticleList.action")
	public String columnArticleSelect(HttpServletRequest request, ModelMap modelMap, Long columnNo, Article search,String cmdStr) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		//排序
		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
		PageUtil page = search.getPageUtil();
    	page.setPaging(true);
		List<ColumnArticleMap> list = null;
		if (search.getColumnNo() != null) {
			list =this.columnArticleMapService.selectColumnArticles(search);
		}
		else {
			list = new ArrayList<ColumnArticleMap>();
		}
		Column column = this.columnService.selectByNo(columnNo);
		Long columnCreatedBy = column.getOperatorNo();
		if(column.getOperator() == null){
			System.out.println("invalid op for column:"+column.getOperatorNo()+",column no:"+columnNo);
		}
		if(column.getOperator() != null && column.getOperator().getType().intValue() == 2){
			columnCreatedBy = column.getOperator().getCreateBy();
		}
		modelMap.put("columnCreatedBy", columnCreatedBy);
		modelMap.put("columnNo", columnNo);
		modelMap.put("list", list);
		modelMap.put("pageUtil", page);
		modelMap.put("search", search);
		if(cmdStr != null){
			SessionUtil.setAttr(request, "cmdStr", cmdStr);
		}
		return "textmgmt/column/columnArticleList";
	}
	
	/**
	 * 新增板块包含文章
	 * @param request
	 * @param modelMap
	 * @param columnNo
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/columnAddNewArticle.action")
	public String columnAddNewArticle(HttpServletRequest request, ModelMap modelMap, Long columnNo, Article article) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		article.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	article.setGroupId(curOper.getOperatorNo());
        	article.setOperatorNo(curOper.getOperatorNo());
        	article.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ARTICLE.getIndex(), curOper.getOperatorNo()));
        	break;
        case ORDINARY_OPER:
        	article.setGroupId(curOper.getCreateBy());
        	article.setOperatorNo(curOper.getCreateBy());
        	article.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ARTICLE.getIndex(), curOper.getCreateBy()));
        	break;
        }
    	PageUtil page = article.getPageUtil();
    	page.setPaging(true);
    	article.setArticleNo(columnNo);
    	article.setStatus(AuditStatus.AUDIT_PASS);
    	article.setTemplateId(-1l);
    	List<Article> list = null;
    	if(columnNo !=null) {
    		list = this.articleService.selectArticleForColumn(article);    		
    	}
    	else {
    		list = new ArrayList<Article>();
    	}
        modelMap.put("columnNo", columnNo);
        modelMap.put("list", list);
        modelMap.put("pageUtil", page);
        modelMap.put("article", article);
		return "textmgmt/column/columnAddNewArticle";
	}

	@RequestMapping(value=("/addArticleToColumn.action"))
	@ResponseBody
	public String addArticleToColumn(HttpServletRequest request, Long[] articleNo, Long columnNo) {
		Result result = new Result();
		try {
        	if(articleNo==null || columnNo==null || articleNo.length<1) {
        		throw new Exception("增加板块数据错误");
           	}
        	Operator curOper = SessionUtil.getActiveOperator(request);
            Long createdBy = curOper.getOperatorNo();
            if(curOper.getType().intValue() == 2){
            	createdBy = curOper.getCreateBy();
            }
        	columnArticleMapService.addArticlesToColumn(columnNo, articleNo,createdBy);
            result.setResult(true);
        }
        catch (Exception e) {
        	logger.error("addColumnArticleMap exception occurred, cause by:{}", e);
            result.setDesc(e.getMessage());
        }
		
		String ret = JSON.toJSONString(result);
		return ret;
	}
	
	@RequestMapping(value="/deleteArticleInColumn.action")
	@ResponseBody
	public String deleteArticleInColumn(HttpServletRequest request, Long[] rtId) throws Exception {
		Result2<Object> result = new Result2<Object>();
        String delName="";
        Operator curOper = SessionUtil.getActiveOperator(request);
        try {
            if (rtId != null && rtId.length > 0) {
            	List<ColumnArticleMap> cs = null;
            	try {
            		cs = this.columnArticleMapService.selectByNos(rtId);
            	}
            	catch(Exception e) {
            		throw new Exception("查询版块数据错误");
            	}

            	if (cs==null || cs.size()<1) {
            		throw new ResultCodeException(ResultCode.DB_ERROR, "column-article list null/empty");
            	}
            	
            	List<Long> articleNoList = new ArrayList<Long>();
            	ColumnArticleMap c = cs.get(0);
            	Long columnNo = c.getColumnNo();
            	delName = "" + c.getColumnArticleNo();
       			c.setCurOper(curOper);
       			articleNoList.add(c.articleNo);
       			for(int i=1;i<cs.size();i++) {
            		c = cs.get(i);
            		if(!columnNo.equals(c.getColumnNo())) {
            			throw new ResultCodeException(ResultCode.DB_ERROR, "delete article in diff column:" + columnNo + ":" + c.getColumnNo());
            		}
           			delName+="," + c.getColumnArticleNo();
           			c.setCurOper(curOper);
           			articleNoList.add(c.articleNo);	
            	}
            	
            	if(cs!=null && cs.size()>0) {
            		this.columnArticleMapService.deleteColumnArticleMaps(cs);
            	}
            	
            	List<PortalPublishNotice> notices = new ArrayList<PortalPublishNotice>();
            	
            	Long[] articleNos = articleNoList.toArray(new Long[0]);
            	this.articleService.unpublishArticle(columnNo, articleNos, notices);
            	
            	/* To remove, code replaced by articleService.unpublishArticle()
            	//hzm modify 20160616            	
            	List<Long> tmp = new ArrayList<Long>();
				for (ColumnArticleMap m: cs){
					List<ResourcePublishMap> albumPublishList = resourcePublishMapMapper.findResourcePublishMapByResId(
							EntityType.TYPE_COLUMN, columnNo, EntityType.TYPE_ARTICLE, m.getArticleNo());
					if (albumPublishList != null && albumPublishList.size() > 0){
						tmp.add(albumPublishList.get(0).getId());
					}
					
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
					notice.setParentType(EntityType.TYPE_COLUMN);
					notice.setParentId(columnNo);
					notice.setResourceType(EntityType.TYPE_ARTICLE);
					notice.setResourceId(m.getArticleNo());
					notices.add(notice);
				}
				
				if (tmp.size() > 0){
					Long[] publishIds = tmp.toArray(new Long[0]);
					resourcePublishMapService.deleteResourcePublishMapsById(SessionUtil.getActiveOperator(request), publishIds);
					
					for (ColumnArticleMap m: cs) {
						List<ResourcePublishMap> otherpub = resourcePublishMapService.findResourcePublishMapById(
								null, null, EntityType.TYPE_ARTICLE, m.articleNo);
						// 如果没有其他发布，删除发布的图片
						if(ArrayUtils.getSize(otherpub)==0) {
							otherpub = resourcePublishMapService.findResourcePublishMapById(EntityType.TYPE_ARTICLE, m.articleNo,
									EntityType.TYPE_PICTURE, null);
							if(otherpub!=null) {
								for(ResourcePublishMap m2: otherpub) {
									PortalPublishNotice notice = new PortalPublishNotice();
									notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
									notice.setParentType(EntityType.TYPE_ARTICLE);
									notice.setParentId(m.articleNo);
									notice.setResourceType(m2.type);
									notice.setResourceId(m2.resourceId);
									notices.add(notice);	
								}
							}
							this.resourcePublishMapService.deleteResourcePublishMaps(EntityType.TYPE_ARTICLE, m.articleNo,
									EntityType.TYPE_PICTURE, null);
						}
					}
				}
				*/
				
				if(notices.size()>0) {
					portalProcessor.putNoticeToQueue(notices);
				}

            	result.setDesc("版块[ "+delName +" ]删除");
            }
        }
        catch (Exception ex) {
        	ResultCodeException.convertException(result, ex);
        	logger.error("deleteArticle exception occurred, cause by:{}", ex);
            //result.setDesc(ex.getMessage());
        }
        
        logService.logToDB(request,result.getDesc(), LogUtil.LOG_INFO, result.getResult()==0, true);		
		String ret = JSON.toJSONString(result);
		return ret;
	}
	
	@RequestMapping(value="/showChildColumnList.action")
	public String showChildColumnList(HttpServletRequest request, ModelMap modelMap, Long columnNo, Column search) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		//排序
		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
		
		PageUtil page = search.getPageUtil();
    	page.setPaging(true);
		
		List<Column> list = null;
		if (search.getColumnNo() != null) {
			list =this.columnService.selectChildColumns(search);
		}
		else {
			list = new ArrayList<Column>();
		}
		if(list != null){
			Long createdBy = null;
			Long opNo = curOper.getOperatorNo();
			if(curOper.getType().intValue() == 2){
				opNo = curOper.getCreateBy();
			}
			for (Column column : list) {
	    		column.setCmds(null);
	    		createdBy = column.getOperatorNo();
	    		if(column.getOperator().getType().intValue() == 2){
	    			createdBy = column.getOperator().getCreateBy();
	    		}
	    		if(curOper.isSuperAdmin()||opNo.longValue() == createdBy.longValue()){
	    			column.setAllocRes(null);
	    		}
	    		else{
	    			column.setAllocRes(1l);
	    		}
			}
		}
		modelMap.put("columnNo", columnNo);
		modelMap.put("list", list);
		modelMap.put("pageUtil", page);
		modelMap.put("search", search);
		
		return "textmgmt/column/childColumnList";
	}
	
	@RequestMapping(value = "/toColumnPublish.action")
	public String toColumnPublish(HttpServletRequest request,
			ModelMap modelMap, String columnNos) throws Exception {
		modelMap.put("columnNos", columnNos);
		return "textmgmt/column/columnPublish";
	}

	@RequestMapping(value = "/columnPublish.action")
	@ResponseBody
	public String columnPublish(HttpServletRequest request,
			ModelMap modelMap, String columnNos,ResourcePublishMap publish) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		Long createdBy = curOper.getOperatorNo();
		if(curOper.getOperator().getType().intValue() == 2){
			createdBy = curOper.getOperator().getCreateBy();
		}
		publish.setCreatedBy(createdBy);
		boolean result = false;
		String str = "";
		try {
			
			String[] tempIdArr = columnNos.split(Delimiters.COMMA);
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
				
				Column c = new Column();
				c.setCurOper(curOper);
				columnService.audit(c, ids, AuditStatus.PUBLISH, publish);
			}
			
			processor.putNoticeToQueue(publish.getNoticeList());
			result = true;
			logService.logToDB(request, "发布版块[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "发布版块[" + str + "]", LogUtil.LOG_INFO, false, true);
			result = false;
			logger.error(
					"Publish columns exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
    @RequestMapping(value = "/resourceColumnNoPublishList.action")
    public String resourceColumnNoPublishList(HttpServletRequest request, ModelMap modelMap, Long resourceId, Column search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	search.setOperatorNo(curOper.getOperatorNo());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), curOper.getOperatorNo()));
        	break;
        case ORDINARY_OPER:
        	search.setGroupId(curOper.getCreateBy());
        	search.setOperatorNo(curOper.getCreateBy());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), curOper.getCreateBy()));
        	break;
        }
        List<Column> belongList = null;
        List<Column> list = null;
    	PageUtil page = search.getPageUtil();
    	//只查询顶级版块
    	//search.setParentNo(-1l);
    	search.setStatus(AuditStatus.PUBLISH);
    	page.setPaging(true);
       	list = this.columnService.findResourceColumnNoPublishs(resourceId, search);
       	//belongList =  columnService.selectAllColumns(search);
        modelMap.put("belongList", belongList);
        modelMap.put("list", list);
        modelMap.put("pageUtil", page);
        modelMap.put("resourceId", resourceId);
        modelMap.put("search", search);
        return "textmgmt/column/resourceColumnNoPublishList";
    }

//    /*
//     * 运营商选择
//     */
//    @RequestMapping(value = "/columnSelectList.action")
//    public String columnSelectList(HttpServletRequest request, ModelMap modelMap, Column search) throws Exception {
//        Operator curOper = SessionUtil.getActiveOperator(request);
//        search.setCurOper(curOper);
//        List<Column> belongList = null;
//        List<Column> distColumnList = null;
//        List<Column> list = null;
//        List<Long> columnNoList = new ArrayList<Long>();
//    	Map<Long, Column> distColumnMap = new HashMap<Long, Column>();
//        switch (OperatorType.getType(curOper.getType())){
//        case SUPER_ADMIN:
//        	search.setColumnName(search.getKeyWord());
//        	belongList =  columnService.findAllColumns(null, null);
//        	list = this.columnService.findColumns(search, null, null);
//        	break;
//        case Column_ADMIN:
//        	Column cmpy = columnService.findColumnByNo(curOper.getColumnNo());
//        	if (search == null || StringUtils.isEmpty(search.getSearchPath())){
//            	search.setPath(cmpy.getPath());
//        	}
//
//            belongList =  columnService.findAllColumns(cmpy, null);
//        	search.setColumnName(search.getKeyWord());
//        	list = this.columnService.findColumns(search, null, null);
//        	break;
//        case ORDINARY_OPER:
//            distColumnList = columnService.findDistColumnsByOperNo(curOper.getOperatorNo());
//        	List<Long> createColumnNoList = columnService.findCreateColumnNosByOperNo(curOper.getOperatorNo());
//        	
//        	if (distColumnList != null && distColumnList.size() > 0){
//        		for (Column temp: distColumnList){
//        			columnNoList.add(temp.getColumnNo());
//        			distColumnMap.put(temp.getColumnNo(), temp);
//        		}
//        	}
//        	
//        	if (createColumnNoList != null && createColumnNoList.size() > 0){
//        		columnNoList.addAll(createColumnNoList);
//        	}
//        	
//        	if (createColumnNoList == null || createColumnNoList.size() <= 0){
//        		createColumnNoList.add(Long.valueOf(-1));
//        	}
//        	
//        	search.setColumnName(search.getKeyWord());
//        	list = this.columnService.findColumns(search, columnNoList, createColumnNoList);
//        	columnNoList.add(curOper.getColumnNo());
//            belongList =  columnService.findAllColumns(null, columnNoList);
//        	if (distColumnList != null && distColumnList.size() > 0){
//            	Iterator<Column> it = list.iterator();
//            	while (it.hasNext()){
//            		Column cpy = it.next();
//            		if (distColumnMap.containsKey(cpy.getColumnNo())){
//            			Column distCpy = distColumnMap.get(cpy.getColumnNo());
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
//    	return "textmgmt/column/columnSelectList";
//    }
//    
//    /*
//     * 运营商选择
//     */
//    @RequestMapping(value = "/findColumnSelectList.action")
//    public String findColumnSelectList(HttpServletRequest request, ModelMap modelMap, Column search) throws Exception {
//    	PageUtil page = search.getPageUtil();
//    	search.getPageUtil().setPaging(true);
//    	Operator curOper = SessionUtil.getActiveOperator(request);
//    	List<Column> list=null;
//    	switch (OperatorType.getType(curOper.getType())){
//        case SUPER_ADMIN:
//        	search.setColumnName(search.getKeyWord());        	
//        	list = this.columnService.findColumns(search, null, null);
//        	break;
//        default:
//    	if(search.getColumnNo()!=null&&!search.getColumnNo().equals("")){
//    		
//    	}else{
//    		search.setColumnNo(curOper.getColumnNo());
//    	}   	
//		int count = columnService.columnSelectListCount(search);
//		page.setRowCount(count);
//		list=columnService.columnSelectList(search);
//    	}
//		modelMap.put("list", list);
//	    modelMap.put("pageUtil", search.getPageUtil());
//	    modelMap.put("search", search);	  
//    	
//		return "textmgmt/column/columnSelectList";
//    } 
    
    /** 
	 * 查询有相册权限的用户
	 */ 
	@RequestMapping(value = "/findColumnOperator.action")
	public String findAlbumOperator(HttpServletRequest request,ModelMap modelMap,Long columnNo, Operator search) throws Exception {  
		    List<String> operList =null; 
		    List<Operator> list = new ArrayList<Operator>();  
		    String type = "3"; 
		    Long resourceId = columnNo;
			if(columnNo != null){
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
