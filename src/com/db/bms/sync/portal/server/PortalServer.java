
package com.db.bms.sync.portal.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.db.bms.service.AlbumService;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.FeatureCodeService;
import com.db.bms.service.PictureService;
import com.db.bms.service.TemplateService;
import com.db.bms.service.TopicColumnService;
import com.db.bms.service.TopicService;
import com.db.bms.service.VideoService;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetAlbumREQT;
import com.db.bms.sync.portal.protocal.GetAlbumRESP;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;
import com.db.bms.sync.portal.protocal.GetArticleREQT;
import com.db.bms.sync.portal.protocal.GetArticleRESP;
import com.db.bms.sync.portal.protocal.GetColumnREQT;
import com.db.bms.sync.portal.protocal.GetColumnRESP;
import com.db.bms.sync.portal.protocal.GetFeatureCodeREQT;
import com.db.bms.sync.portal.protocal.GetFeatureCodeRESP;
import com.db.bms.sync.portal.protocal.GetImageREQT;
import com.db.bms.sync.portal.protocal.GetImageRESP;
import com.db.bms.sync.portal.protocal.GetMenuREQT;
import com.db.bms.sync.portal.protocal.GetMenuRESP;
import com.db.bms.sync.portal.protocal.GetTemplateREQT;
import com.db.bms.sync.portal.protocal.GetTemplateRESP;
import com.db.bms.sync.portal.protocal.GetTopicREQT;
import com.db.bms.sync.portal.protocal.GetTopicRESP;
import com.db.bms.sync.portal.protocal.InjectVideoImageREQT;
import com.db.bms.sync.portal.protocal.InjectVideoImageRESP;
import com.db.bms.sync.portal.protocal.VideoCutReportREQT;
import com.db.bms.sync.portal.protocal.VideoCutReportRESP;
import com.db.bms.utils.ResultCodeException;


@RequestMapping("")
@Controller
public class PortalServer {

	private final static Logger logger = Logger.getLogger(PortalServer.class);
	
	private static String ENCODING = "UTF-8";
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private PictureService pictureService;

	@Autowired
	private ColumnService columnSerivce;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private TopicColumnService topicColumnService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private FeatureCodeService featureCodeService;
	
	@RequestMapping(value = "/GetTopicList")
	public void getTopicList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetTopicRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[GetTopicList] operation request:" + content);
			GetTopicREQT getTopicREQT = JSON.parseObject(content, GetTopicREQT.class);
			result = getTopicREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = topicService.getTopicList(getTopicREQT);
			}
		}
		catch(ResultCodeException e) {
			if (result == null){
				result = new GetTopicRESP();
			}
			result.setResultCode("" + e.result);
			result.setResultDesc(e.desc);
		}
		catch (Exception e){
			if (result == null){
				result = new GetTopicRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get topic list message exception occurred, cause by:{}", e);
		}
		finally{
			writeResponse(response, result.build(), "GetTopicList");
		}
	}
	
	@RequestMapping(value = "/GetMenuList")
	public void GetMenuList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetMenuRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[GetMenuList] operation request:" + content);
			GetMenuREQT getMenuREQT = JSON.parseObject(content, GetMenuREQT.class);
			result = getMenuREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = topicColumnService.getMenuList(getMenuREQT);
			}			
		}
		catch (Exception e){
			if (result == null){
				result = new GetMenuRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get menu list message exception occurred, cause by:{}", e);
		}
		finally{
			writeResponse(response, result.build(), "GetMenuList");
		}
	}
	
	@RequestMapping(value = "/GetAlbumList")
	public void getAlbumList(HttpServletRequest request,HttpServletResponse response) throws Exception {						
		GetAlbumRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[GetAlbumList] operation request:" + content);
			GetAlbumREQT getAlbumREQT = JSON.parseObject(content, GetAlbumREQT.class);
			result = getAlbumREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = albumService.getAlbumList(getAlbumREQT);
			}
		}
		catch (Exception e){
			if (result == null){
				result = new GetAlbumRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get album list message exception occurred, cause by:{}", e);
		}
		finally{
			writeResponse(response, result.build(), "GetAlbumList");
		}
	}
	
	@RequestMapping(value = "/GetImageList")
	public void getImageList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetImageRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[GetImageList] operation request:" + content);
			GetImageREQT getImageREQT = JSON.parseObject(content, GetImageREQT.class);
			result = getImageREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = pictureService.getImageList(getImageREQT);
			}
			
		}
		catch (Exception e){
			if (result == null){
				result = new GetImageRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get image list message exception occurred, cause by:{}", e);
		}
		finally{
			writeResponse(response, result.build(), "GetImageList");
		}
	}	
	//------------------------------------------------------------------------
	
	@RequestMapping(value = "/GetColumnList")
	public void getColumnList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetColumnRESP result = new GetColumnRESP();
		try{
			String content = readContent(request);
			logger.info("[GetColumnList] operation request:" + content);
			GetColumnREQT req = GetColumnREQT.fromJsonString(content);
			req.checkData(result);
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = this.columnSerivce.getColumnList(request, req);
			}
		}
		catch(ResultCodeException e) {
			if (result == null){
				result = new GetColumnRESP();
			}
			result.setResultCode("" + e.result);
			result.setResultDesc(e.desc);
		}
		catch (Exception e){
			if (result == null){
				result = new GetColumnRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get column list message exception occurred, cause by:{}",e);
		}
		finally {
			String s= result.build();
			writeResponse(response, s, "GetColumnList");
		}
	}
	
	@RequestMapping(value = "/GetArticleList")
	public void GetArticleList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetArticleRESP result = null;
		try {
			String content = readContent(request);
			logger.info("[GetArticleList] operation request:" + content);
			GetArticleREQT getArticleREQT = JSON.parseObject(content, GetArticleREQT.class);
			result = getArticleREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = articleService.getArticleList(getArticleREQT);
			}
			
		}
		catch (Exception e){
			if (result == null){
				result = new GetArticleRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get article list message exception occurred, cause by:{}", e);
		}
		finally{
			writeResponse(response, result.build(), "GetArticleList");
		}
	}
	
	@RequestMapping(value = "/VideoCutReport")
	public void videoCutReport(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		VideoCutReportRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[VideoCutReport] operation request:" + content);
			VideoCutReportREQT videoCutReportReqt = JSON.parseObject(content, VideoCutReportREQT.class);
			result = videoCutReportReqt.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = videoService.videoCutReport(videoCutReportReqt);
			}
			
		}
		catch (Exception e){
			if (result == null){
				result = new VideoCutReportRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle video cut report message exception occurred, cause by:{}",	e);
		}
		finally{
			writeResponse(response, result.build(), "VideoCutReport");
		}
	}
	
	@RequestMapping(value = "/InjectVideoImage")
	public void injectVideoImage(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		InjectVideoImageRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[InjectVideoImage] operation request:" + content);
			InjectVideoImageREQT injectVideoImageReqt = JSON.parseObject(content, InjectVideoImageREQT.class);
			result = injectVideoImageReqt.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = pictureService.injectVideoImage(injectVideoImageReqt);
			}
			
		}
		catch (Exception e){
			if (result == null){
				result = new InjectVideoImageRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle inject video image message exception occurred, cause by:{}",e);
		}
		finally{
			writeResponse(response, result.build(), "InjectVideoImage");
		}
	}	
	
	@RequestMapping(value = "/GetAreaCodeList")
	public void getAreaCodeList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetAreaCodeRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[GetTopicList] operation request:" + content);
			GetAreaCodeREQT getAreaCodeREQT = JSON.parseObject(content, GetAreaCodeREQT.class);
			result = getAreaCodeREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = companyService.getAreaCodeList(getAreaCodeREQT);
			}
			
		}
		catch (Exception e) {
			if (result == null){
				result = new GetAreaCodeRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get area code list message exception occurred, cause by:{}", e);
		} 
		finally {
			writeResponse(response, result.build(), "GetAreaCodeList");
		}
	}
	
	@RequestMapping(value = "/GetTemplateList")
	public void getTemplateList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetTemplateRESP result = null;
		try {
			String content = readContent(request);
			logger.info("[GetTemplateList] operation request:" + content);
			
			GetTemplateREQT getTemplateREQT = JSON.parseObject(content, GetTemplateREQT.class);

			result = getTemplateREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = templateService.getTemplateList(getTemplateREQT);
			}
			
		} 
		catch (Exception e) {
			if (result == null){
				result = new GetTemplateRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get template list message exception occurred, cause by:{}", e);
		}
		finally {
			writeResponse(response, result.build(), "GetTemplateList");
		}
	}
	
	@RequestMapping(value = "/GetFeatureCodeList")
	public void getFeatureCodeList(HttpServletRequest request,HttpServletResponse response) throws Exception{						
		GetFeatureCodeRESP result = null;
		try{
			String content = readContent(request);
			logger.info("[GetFeatureCodeList] operation request:" + content);
			GetFeatureCodeREQT getFeatureCodeREQT = JSON.parseObject(content, GetFeatureCodeREQT.class);
			result = getFeatureCodeREQT.checkData();
			if (CommonResultCode.getResultCode(result.getResultCode()) == CommonResultCode.SUCCESS){
				result = featureCodeService.getFeatureCodeList(getFeatureCodeREQT);
			}
			
		}
		catch (Exception e){
			if (result == null){
				result = new GetFeatureCodeRESP();
			}
			result.setResultCode(CommonResultCode.OTHER.getResultCode());
			result.setResultDesc("An unknown error.");
			logger.error("Handle get feature code list message exception occurred, cause by:{}",e);
		}
		finally{
			writeResponse(response, result.build(), "GetFeatureCodeList");
		}
	}
	
	
	private String readContent(HttpServletRequest request) throws Exception{
		ServletInputStream sis = null;
		try {
			sis = request.getInputStream();
			int size = request.getContentLength();  
			if(size <= 0){
				size = 409600;
			}
            byte[] buffer = new byte[size];  
            byte[] xmldataByte = new byte[size];  
            int count = 0;  
            int rbyte = 0;  
            while (count < size) {   
                rbyte = sis.read(buffer);  
                if(rbyte <= 0)
                	break;
                for(int i=0;i<rbyte;i++) {  
                    xmldataByte[count + i] = buffer[i];  
                }  
                count += rbyte;  
            }                           
            String reponse = new String(xmldataByte,0,count, "UTF-8");
			return reponse;
		} 
		catch (Exception e) {
			logger.error("Ocurred exception when read request content:", e);
			throw e;
		}
		finally{
			if (sis != null){
				sis.close();
			}
		}

	}
	
	private void writeResponse(HttpServletResponse response, String responseText, String operator) {
		PrintWriter out = null;
		try {
			byte[] rt = responseText.getBytes(ENCODING);
			response.setCharacterEncoding(ENCODING);
			response.setContentLength(rt.length);
			response.setHeader("Content-Type", "text/json");
			out = response.getWriter();
			out.print(new String(rt, ENCODING));
			out.flush();
			logger.info("[" + operator + "] operation response:" + responseText);
		} catch (Exception e) {
			logger.error("Response to the [" + operator + "] operation exception occurred, cause by:{}", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	
	static void getFileContent(HttpServletRequest request, HttpServletResponse response, String filename) throws Exception {
    	InputStream is = null;
    	try {
	    	ServletContext context = request.getSession().getServletContext();
	    	String mimeType = context.getMimeType(filename);
	        if (mimeType == null) {
	            // set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	    	
	        response.setContentType(mimeType);
	        
	        File f = new File(filename);
	        if(!f.exists()) {
	        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
	        	return;
	        }
	        
	    	is = new FileInputStream(f);
    		IOUtils.copy(is, response.getOutputStream());
    	}
    	catch(Exception e) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND);
    	}
    	finally {
    		IOUtils.closeQuietly(is);
    	}    	
	}
	
	
    /**
     * get generated page res, in default, the generated page is in c:/upload/pageId/filename.html
     * @param request
     * @param search
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{dir:\\d+}/{filename:.*}") 
    public void getArticleImage(HttpServletRequest request, HttpServletResponse response, 
    		@PathVariable("dir") String dir, @PathVariable("filename") String filename) throws Exception {

/*    	String filePath = this.pageService.getDirPath(dir, filename);
    	if(filename.endsWith(".html")) {
    		// update page hit count
    		Page p = new Page();
    		p.setPageId(dir);
    		p.setUrl(filename);
    		pageService.updatePageHitByDirAndFileName(p);
    	}
    	getFileContent(request, response, filePath);*/
    }
    
    /**
     * get article file content. in default, the generated page is in c:/upload/article/filename
     * @param request
     * @param search
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/art/{filename:.*}") 
    public void getArticleImage(HttpServletRequest request, HttpServletResponse response,
    		@PathVariable("filename") String filename) throws Exception {

    	String filePath = this.articleService.getFilePath(filename);
    	getFileContent(request, response, filePath);
    }
    
    
	static String ver = null;
	
	public static String getVer() {
		try {
			if(ver==null) {
				Properties p = new Properties();
				InputStream is = PortalServer.class.getClassLoader().getResourceAsStream("ver.properties");
				if(is==null) {
					ver = "r123";
				}
				else {
					p.load(is);
					ver = p.getProperty("Revision");
					if(ver==null) {
						ver = "r123";
					}
					else {
						ver = "r" + ver;
					}
					is.close();
				}
			}
		}
		catch(Exception e) {
			logger.error("get version error", e);
			ver = "r555";					
		}
		return ver;
	}
	
	@RequestMapping(value = "/ver.action", produces="text/text; charset=utf-8")
	@ResponseBody
	public String ver (HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*
		 * run command to create a ver.properties in src dir.
		 * svn info |grep Revision: |sed  's/:/=/' > src/ver.properties
		 */
		return getVer();
	}
	
}
