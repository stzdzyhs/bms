package com.db.bms.controller.opermgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.controller.picmgmt.TopicController;
import com.db.bms.entity.Operator;
import com.db.bms.service.OperatorService;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("opermgmt/operator")
@Controller
public class OperatorController {
   
	private final static Logger log = Logger.getLogger(TopicController.class);
	
	@Autowired
	private OperatorService operatorService;
	
	/**
	 * 查出管理员
	 * @param request
	 * @param modelMap
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findAllOperators.action")
	public String findAllOperators(HttpServletRequest request, ModelMap modelMap,Long topicId,Long albumNo,Long columnNo,Long articleNo)throws Exception {  
		//设置查出管理员
//		Operator operator = new Operator(); 
//		operator.setType(1);		
		//查询所有管理员
		//根据当前登录操作员来进行查询
		Operator curOper = SessionUtil.getActiveOperator(request);
		PageUtil page = curOper.getPageUtil();
    	page.setPaging(true);
		List<Operator> list = operatorService.findAllOperators(curOper);
		modelMap.put("pageUtil", curOper.getPageUtil());
		modelMap.put("search", curOper);
		modelMap.put("list",list); 
		modelMap.put("topicId", topicId); 
		modelMap.put("albumNo", albumNo); 
		modelMap.put("columnNo", columnNo); 
		modelMap.put("articleNo", articleNo);
		return "sysmgmt/user/resourceUserAllocList";  
    }  
	
	/**
	 * 添加用户专题资源
	 * @param request
	 * @param modelMap
	 * @param operatorNos
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addOperatorResource.action")
	@ResponseBody
	public String addOperatorResource(HttpServletRequest request, ModelMap modelMap,Long[]operatorNos,Long topicId,Long albumNo,Long columnNo,Long articleNo)throws Exception {  
		boolean result = true;  
		Operator operator = new Operator();  
		//专题
		try {
			if(topicId != null && operatorNos.length>0){ 
				String type = "1"; 
				for(int i=0;i<operatorNos.length;i++){   
					operator = operatorService.findOperatorById(operatorNos[i]);  
					String operatorId = operator.getOperatorId(); 
					operatorService.addOperatorResource(operatorId,topicId,type);  
				}
			}
			//相册
			if(albumNo != null && operatorNos.length>0){ 
				String type = "2"; 
				for(int i=0;i<operatorNos.length;i++){   
					operator = operatorService.findOperatorById(operatorNos[i]);  
					String operatorId = operator.getOperatorId(); 
					operatorService.addOperatorResource(operatorId,albumNo,type);  
				}
			}
			//版块
			if(columnNo != null && operatorNos.length>0){ 
				String type = "3"; 
				for(int i=0;i<operatorNos.length;i++){   
					operator = operatorService.findOperatorById(operatorNos[i]);  
					String operatorId = operator.getOperatorId(); 
					operatorService.addOperatorResource(operatorId,columnNo,type);  
				}
			}
			//文章
			if(articleNo != null && operatorNos.length>0){ 
				String type = "4"; 
				for(int i=0;i<operatorNos.length;i++){   
					operator = operatorService.findOperatorById(operatorNos[i]);  
					String operatorId = operator.getOperatorId(); 
					operatorService.addOperatorResource(operatorId,articleNo,type);  
				}
			}
		} catch (Exception e) {
			result = false;
			log.error("Ocurred exception when save operator resource:", e);
			e.printStackTrace();
		}
		return "{result: '" + result + "', desc : ''}";
    }  
	
}