package com.db.bms.controller.textmgmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.db.bms.common.file.UploadResult;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Wordstock;
import com.db.bms.service.LogService;
import com.db.bms.service.WordstockService;
import com.db.bms.utils.ConstConfig;
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

@RequestMapping("textmgmt/wordstock")
@Controller
public class WordstockController {

	private final static Logger logger = Logger.getLogger(WordstockController.class);
	
	@Autowired
	private LogService logService;
	
    @Autowired
    private WordstockService wordstockService;
    
    @RequestMapping(value = "/wordstockList.action")
    public String wordstockList(HttpServletRequest request, ModelMap modelMap, Wordstock search) throws Exception {
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        search.setOperator(curOper);    	
        PageUtil page = search.getPageUtil();
        page.setPaging(true);
        List<Wordstock> list = null;
    	search.setWord(StringUtils.removeSpecialChars(search.getWord()));
    	try {
    		list = this.wordstockService.selectWordstock(search);
		} catch (Exception e) {
			logger.error("selectWordstock occured an exception caused by{}",e);
		}
           	
        modelMap.put("list", list);
        modelMap.put("pageUtil", page);
        modelMap.put("search", search);
        return "textmgmt/wordstock/wordstockList";
    }
  
    @RequestMapping(value = "/wordstockDetail.action")
    public String companyDetail(HttpServletRequest request, ModelMap modelMap, Long wordstockNo) throws Exception {
    	Wordstock col = null;
        try {
        	col = wordstockService.selectByNo(wordstockNo);
        }
        catch (Exception e) {
        	logger.error("Forward to operators detail page exception occurred, cause by:{}", e);
        }
        modelMap.put("wordstock", col);
        return "textmgmt/wordstock/wordstockDetail";
    }    
    
    @RequestMapping(value = "/wordstockEdit.action")
    public String wordstockEdit(HttpServletRequest request, Long id, ModelMap modelMap, Wordstock search) throws Exception {
        try {
            Operator curOper = SessionUtil.getActiveOperator(request);
            search.setCurOper(curOper);
            Wordstock wordstock = null;
            if(id!=null) {
            	wordstock = this.wordstockService.selectByNo(id);
            }
            
            modelMap.put("search", search);
            modelMap.put("wordstock", wordstock);
        }
        catch (Exception e) {
        	logger.error("Forward to operators edit page exception occurred, cause by:{}", e);
        }
        return "textmgmt/wordstock/wordstockEdit";
    }
    
    @RequestMapping(value = "/wordstockCheck.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String wordstockCheck(Wordstock search) throws Exception {
        boolean result = false;
        result = this.wordstockService.isWordstockRepeateIdOrName(search);
        return "{result: '" + !result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/wordstockSave.action", method = RequestMethod.POST,produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String wordstockSaveOrUpdate(HttpServletRequest request, Wordstock wordstock) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
        wordstock.setCurOper(curOper);
    	Result result = new Result();
        String logStr = "";
        //String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        try {
            if (wordstock != null) {
                if (wordstock.getWordNo() != null && wordstock.getWordNo() > 0) { // update
                	//权限校验
                	boolean isPermit = this.wordstockService.checkForWrite(wordstock);
                	if (isPermit) { //有更新权限
                		result.setResult(true);
                		logStr = "更新生僻字[" + wordstock.getWord() + "]" ;
                    	this.wordstockService.updateWordstock(wordstock);
                	} else {
                		result.setResult(false);
                		result.setDesc("您没有操作权限");
					}
                	
                }
                else {
                	logStr = "新增生僻字[" + wordstock.getWord() + "]";
                	this.wordstockService.addWordstock(wordstock);
                	result.setResult(true);
                }
                
                logService.logToDB(request, logStr, LogUtil.LOG_INFO, result.isResult(), true);
            }
        }
        catch (Exception e) {
        	result.setDesc(e.getMessage());
        	logger.error("Save or update wordstock exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    }

    @RequestMapping(value = "/wordstockDelete.action",produces = ConstConfig.APP_JSON)
    @ResponseBody
    public String wordstockDelete(HttpServletRequest request, Long[] rtId) throws Exception {
    	Result2<Object> result = new Result2<Object>();
        String delName="";        
        Operator curOper = SessionUtil.getActiveOperator(request);//当前登录人
        try {
            if (rtId != null && rtId.length > 0) {
            	List<Wordstock> cs = null;
            	try {
            		cs = wordstockService.selectByNos(rtId);
            	}
            	catch(Exception e) {
            		throw new Exception("查询数据错误");
            	}
            	boolean isPermit = false;
            	for(int i=0; i<cs.size(); i++) {
            		Wordstock wordstock = cs.get(i);
            		wordstock.setCurOper(curOper);
            		if (i == cs.size()-1) { 
        				delName += cs.get(i).getWord();
        			} 
            		else {
        				delName += cs.get(i).getWord() + Delimiters.COMMA;
        			}
            		//权限检查
            		isPermit = this.wordstockService.checkForWrite(wordstock);
            		if (isPermit) {  //有删除权限
            			this.wordstockService.deleteWordstock(wordstock);
            			isPermit = false;
            			
            		} 
            		else {
            			result.setResult(ResultCode.NO_ACCESS_RIGHTS);
            			result.setDesc("您没有操作权限");
            			break;
            		}
            	}
            	/*if(cs!=null && cs.size()>0) {
            		wordstockService.deleteWordstocks(cs);
            	}*/
            	logService.logToDB(curOper, "生僻字[" + delName + "]删除", LogUtil.LOG_INFO, result.result==0, true);
            }
        }
        catch (Exception e) {
        	logger.error("wordstockDelete exception occurred, cause by:{}", e);
        	ResultCodeException.convertException(result, e);
        }
        String ret = JSON.toJSONString(result);
        return ret;
    }
    
    /**
     * 生僻字上传
     * @param request
     * @param file
     * @param filename
     * @param wordstock
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadWordstocks.action", method = {RequestMethod.POST}, produces = {ConstConfig.APP_JSON,ConstConfig.TEXT_PLAIN})
    @ResponseBody             
    public String uploadWordstocks(HttpServletRequest request, @RequestParam("Filedata") MultipartFile file,
    		@RequestParam("Filename") String filename ,Wordstock wordstock) throws Exception {
    	UploadResult result = new UploadResult();
        try {        	
        	Operator curOper = SessionUtil.getActiveOperator(request);
        	// curOper == NULL since this action is skip SecurityCheck !
        	curOper = new Operator();
        	curOper.setType(Operator.TYPE_SUPER_ADMIN);
        	curOper.setOperatorNo(-1l);
        	wordstock.setCurOper(curOper);
        	//上传生僻字文件，保存到本地目录
        	File dirFile = this.wordstockService.getWordstockDirFile();
    		String genFileName = FileUpload.geneFileName(filename);
    		boolean isok = FileUpload.saveFile(file, dirFile.toString(), genFileName);
    		File f = null;
    		if(isok) { //上传成功
    			f = new File(dirFile, genFileName);
    			String charset = FileUtils.get_charset(f);
    			//txt文档，检查编码
    			if(!"UTF-8".equals(charset)) {
    				if(f.exists() && f.isFile()) {
    					f.delete();
    				}
    				throw new Exception("编码不支持:" + charset);
    			}
    			//解析文档
    			//count = this.wordstockService.uploadWordstocks(f, wordstock);
    			else  {
        			if(f.exists() && f.isFile()){
        				result.setResult(true);    				
                		result.setFilePath(f.getName());
        			}

            	}
    		}	
        }
        catch (Exception e) {
        	result.setDesc(e.getMessage());
        	logger.error("uploadWordstocks exception occurred, cause by:{}", e);
        }
        finally{
        }
        
        String ret = JSON.toJSONString(result);
        return ret;
    	  
    }
    
    /**
     * 生僻字入库
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    @RequestMapping(value="/saveToDb.action" ,method = {RequestMethod.POST}, produces = {ConstConfig.APP_JSON,ConstConfig.TEXT_PLAIN})
    @ResponseBody
    public String saveToDb (HttpServletRequest request ,HttpServletResponse response ,String fileName) {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	Result result = new Result();
    	Wordstock wordstock = new Wordstock();
    	wordstock.setCurOper(curOper);
    	Integer count = 0;
    	String logStr = "";
    	try {
    		//获取生僻字文件
			String dirFile = this.wordstockService.getWordstockDirFile().toString();
			File f = new File(dirFile, fileName);
			//解析生僻字文件
			count = this.wordstockService.uploadWordstocks(f, wordstock);
			
			if(count > 0) {
    			logStr = "新增[" + count + "]条生僻字记录";
    			result.setResult(true);
    			logService.logToDB(request,logStr, LogUtil.LOG_INFO, true, true);  
    		} else if (count == 0) {
    			logStr = "上传生僻字文件重复";
    			logService.logToDB(request,logStr, LogUtil.LOG_INFO, true, false);
    		} else {
    			logStr = "生僻字只支持中文";
    			logService.logToDB(request,logStr, LogUtil.LOG_INFO, true, false);
    		}
			result.setDesc(logStr);
    		//删除上传的文件
    		if(f.exists() && f.isFile()) {
				f.delete();
    		}
			
		} catch (Exception e) {
			logger.error("occured an exception when save wordstock to DB caused by", e);
		}
    	
    	return JSON.toJSONString(result);
    }
    
    //downloadwordstockTemplate.action
    @RequestMapping(value = "/downloadwordstockTemplate.action")
	@ResponseBody
	public String downloadwordstockTemplate(HttpServletRequest request,
			HttpServletResponse response, String fileName) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String ctxPath = request.getSession().getServletContext().getRealPath("/");
		String downloadPath = ctxPath + ConstConfig.DOWNLOAD_PATH +	ConstConfig.WORDSTOCK_PATH + fileName;
		System.out.println("downloadPath-->" + downloadPath);
		
		try {
			long length = new File(downloadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ new String(fileName.getBytes("UTF-8"), "ISO8859-1")
					+ "\"");
			response.setHeader("Content-Length", String.valueOf(length));
			bis = new BufferedInputStream(new FileInputStream(downloadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int len;
			while (-1 != (len = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, len);
			}
			logService.logToDB(request, "下载生僻字模板【" + fileName + "】", LogUtil.LOG_INFO, true, true);
		} 
		catch (Exception e) {
			logService.logToDB(request, "下载生僻字模板【" + fileName + "】", LogUtil.LOG_ERROR, false, true);
			logger.error("Download wordstock template file exception occurred, cause by:{}",e);
		} 
		finally {
			if (bis != null) {
				bis.close();
			}

			if (bos != null) {
				bos.close();
			}
		}
		return null;
	}
    
}
