
package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.db.bms.dao.AlbumMapper;
import com.db.bms.dao.ArticleMapper;
import com.db.bms.dao.ColumnMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.TemplateMapper;
import com.db.bms.dao.TopicMapper;
import com.db.bms.entity.Portal;
import com.db.bms.entity.Template;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.entity.Template.TemplateStatus;
import com.db.bms.entity.Template.TemplateType;
import com.db.bms.service.TemplateService;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetTemplateREQT;
import com.db.bms.sync.portal.protocal.GetTemplateRESP;
import com.db.bms.sync.portal.protocal.TemplateInfo;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TopicMapper topicMapper;
	
	@Autowired
	private AlbumMapper albumMapper;
	
	@Autowired
	private ColumnMapper columnMapper;
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private TemplateMapper templateMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Value("${portal.sysUrl}")
	private String portalUrl;
	
	@Override
	public Template findTemplateById(Long templateId) throws Exception {
		return templateMapper.findTemplateById(templateId);
	}

	@Override
	public List<Template> findTemplatesById(Long[] templateIds)
			throws Exception {
		return templateMapper.findTemplatesById(templateIds);
	}

	@Override
	public List<Template> findTemplates(Template search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = templateMapper.findTemplateCount(search);
		page.setRowCount(count);
		return templateMapper.findTemplates(search);
	}

	@Override
	public List<Template> findAllTemplates(Template search) throws Exception {
		return templateMapper.findTemplates(search);
	}

	@Override
	public boolean isTemplateRepeat(Template template) throws Exception {
		int count = templateMapper.getTemplateCountByName(template);
		return count > 0 ? true : false;
	}

	@Override
	public void addTemplate(Template template) throws Exception {
		templateMapper.addTemplate(template);
	}

	@Override
	public void updateTemplate(Template template) throws Exception {
		templateMapper.updateTemplate(template);
	}

	@Override
	public void auditTemplate(Integer status, Long[] templateIds) throws Exception {
		templateMapper.updateTemplateStatus(status, templateIds, DateUtil.getCurrentTime());
	}

	@Override
	public void deleteTemplatesById(Long[] templateIds) throws Exception {
		templateMapper.deleteTemplatesById(templateIds);
	}

	@Override
	public GetTemplateRESP getTemplateList(GetTemplateREQT request)
			throws Exception {
		GetTemplateRESP response = new GetTemplateRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<TemplateInfo> templateList = new ArrayList<TemplateInfo>();
		response.setTemplateList(templateList);
		
		Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
	    if (portal == null) {
			response.setResultCode(CommonResultCode.NOT_FOUND_SYSTEM.getResultCode());
			response.setResultDesc("Could not find the system.");
			return response;
	    }
	    
	    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
			response.setResultCode(CommonResultCode.NO_ACCESS_RIGHTS.getResultCode());
			response.setResultDesc("No access rights.");
			return response;
	    }
		
	    //查询指定模板
	    if (StringUtils.isNotEmpty(request.getTemplateNo())){
	    	Template template = templateMapper.findTemplateById(Long.valueOf(request.getTemplateNo()));
	    	if (TemplateStatus.getStatus(template.getStatus()) != TemplateStatus.ENABLE){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	        TemplateInfo templateInfo = convertTemplate(template);
	        templateList.add(templateInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
	    
		Template search = new Template();
		search.setStatus(TemplateStatus.ENABLE.getIndex());
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = templateMapper.findTemplateCount(search);
		page.setRowCount(totalCount);
		List<Template> list = templateMapper.findTemplates(search);
		Iterator<Template> it = list.iterator();
		while (it.hasNext()){
			Template template = it.next();
			TemplateInfo templateInfo = convertTemplate(template);
			templateList.add(templateInfo);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private TemplateInfo convertTemplate(Template template){
		TemplateInfo templateInfo = new TemplateInfo();
		templateInfo.setTemplateNo(String.valueOf(template.getId()));
		templateInfo.setTemplateName(template.getTemplateName());
		templateInfo.setType(template.getType());
		templateInfo.setFilePath(portalUrl + "/" + template.getFilePath());
		return templateInfo;
	}

	@Override
	public boolean isReferenced(Template template) throws Exception {
		
		switch (TemplateType.getType(template.getType())){
		case TOPIC:
			int count = topicMapper.getTopicCountByTemplateId(template.getId());
			if (count > 0){
				return true;
			}
			break;
		case ALBUM:
			count = albumMapper.getAlbumCountByTemplateId(template.getId());
			if (count > 0){
				return true;
			}
			break;
		case COLUMN:
			count = columnMapper.getColumnCountByTemplateId(template.getId());
			if (count > 0){
				return true;
			}
			break;
		case ARTICLE:
			count = articleMapper.getArticleCountByTemplateId(template.getId());
			if (count > 0){
				return true;
			}
			break;
		}

		return false;
	}

}
