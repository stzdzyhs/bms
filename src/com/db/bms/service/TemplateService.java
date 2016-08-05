
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Template;
import com.db.bms.sync.portal.protocal.GetTemplateREQT;
import com.db.bms.sync.portal.protocal.GetTemplateRESP;


public interface TemplateService {

	Template findTemplateById(Long templateId) throws Exception;
	
	List<Template> findTemplatesById(Long[] templateIds) throws Exception;
	
	List<Template> findTemplates(Template search) throws Exception;
	
	List<Template> findAllTemplates(Template search) throws Exception;
	
	boolean isTemplateRepeat(Template template) throws Exception;
	
	void addTemplate(Template template) throws Exception;
	
	void updateTemplate(Template template) throws Exception;
	
	void auditTemplate(Integer status, Long[] templateIds) throws Exception;
	
	void deleteTemplatesById(Long[] templateIds) throws Exception;
	
	GetTemplateRESP getTemplateList(GetTemplateREQT request) throws Exception;
	
	boolean isReferenced(Template template) throws Exception;
}
