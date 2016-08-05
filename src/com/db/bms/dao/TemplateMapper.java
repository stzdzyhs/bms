
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Template;


public interface TemplateMapper {

	Template findTemplateById(Long templateId) throws Exception;
	
	List<Template> findTemplatesById(Long[] templateIds) throws Exception;
	
	Integer findTemplateCount(Template search) throws Exception;
	
	List<Template> findTemplates(Template search) throws Exception;
	
	Integer getTemplateCountByName(Template template) throws Exception;
	
	Integer addTemplate(Template template) throws Exception;
	
	Integer updateTemplate(Template template) throws Exception;
	
	Integer updateTemplateStatus(@Param(value = "status")Integer status, @Param(value = "templateIds")Long[] templateIds,  @Param(value = "updateTime")String updateTime) throws Exception;
	
	Integer deleteTemplatesById(Long[] templateIds) throws Exception;
}
