package com.db.bms.controller.opermgmt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.FeacodeGroupMap;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.FeatureCodeGroup;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.service.FeacodeGroupMapService;
import com.db.bms.service.FeatureCodeGroupService;
import com.db.bms.service.FeatureCodeService;
import com.db.bms.service.LogService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping(value="opermgmt/featureCodeGroup")
@Controller
public class FeatureCodeGroupController {

	private final static Logger logger = Logger.getLogger(PortalController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private FeatureCodeService featureCodeService;
	
	@Autowired
	private FeatureCodeGroupService  featureCodeGroupService;
	
	@Autowired
	private FeacodeGroupMapService feacodeGroupMapService;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	/**
	 * 查询所有特征码组
	 * @param request
	 * @param modelMap
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/featureCodeGroupList.action")
	public String FeatureCodeGroupList (HttpServletRequest request, ModelMap modelMap,
				FeatureCodeGroup search) throws Exception{
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		switch (curOper.getType()){
        case Operator.TYPE_SUPER_ADMIN:
        	break;
        case Operator.TYPE_COMPANY_ADMIN:
        	search.setGid(curOper.getOperatorNo());
        	break;
        case Operator.TYPE_ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(
        					ResourceAllocation.ResourceType.FEACODE_GROUP.getIndex(), curOper.getOperatorNo());
        	search.setAllocResourceIds(allocResourceIds);
			break;
		}	
		
		List<FeatureCodeGroup> list;
		try {
			list = this.featureCodeGroupService.findFeatureCodeGroups(search);
			
			modelMap.put("list", list);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("search", search);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occurred an exception when findFeatureCodeGroups");
		}
		
		return "opermgmt/feacodeGroup/feacodeGroupList";
	}

	/**
	 * 查询当前组号的所有特征码
	 * @param request
	 * @param modelMap
	 * @param search
	 * @param groupNo
	 * @return
	 */
	@RequestMapping(value="/showFeatureCodes.action")
	public String showFeatureCodes(HttpServletRequest request, ModelMap modelMap, 
				FeacodeGroupMap search ,Long groupNo) {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		List<FeacodeGroupMap> list = null;
		try {
			if(search.getGroupNo() != null) { 
				list = this.feacodeGroupMapService.findFeacodeGroupMaps(search);
			}
			else {
				list = new ArrayList<FeacodeGroupMap>();
			}
			
			modelMap.put("search", search);
			modelMap.put("list", list);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("groupNo", groupNo);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("occured an error when execute the function -->findFeacodeGroupMaps cause by {}", e);
		}

		return "opermgmt/feacodeGroup/featureCodeInGroup";
	}
	
	/**
	 * 查询不在当前组内的所有特征码
	 * @param request
	 * @param modelMap
	 * @param search
	 * @param groupNo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/showFeacodesOutGroup.action")
	public String showFeacodesOutGroup(HttpServletRequest request, ModelMap modelMap, 
						FeatureCode search, Long groupNo) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		switch (curOper.getType()){
        case Operator.TYPE_SUPER_ADMIN:
        	break;
        case Operator.TYPE_COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	break;
        case Operator.TYPE_ORDINARY_OPER:
        	search.setOperatorNo(curOper.getOperatorNo());
        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(
        					ResourceAllocation.ResourceType.FEATURE_CODE.getIndex(), curOper.getOperatorNo());
        	search.setAllocResourceIds(allocResourceIds);
			break; 
		}	
		List<FeatureCode> list = null;
		try { 
			if(search.getGroupNo() != null) {
				list = this.featureCodeService.getFeatureCodeList(search);
			}
			else {
				list = new ArrayList<FeatureCode>();
			}

			modelMap.put("search", search);
			modelMap.put("list", list);
			modelMap.put("pageUtil", search.getPageUtil());
			modelMap.put("groupNo", groupNo);
			
		} catch (Exception e) {
			logger.error("occured an exception when execute th function-->getFeatureCodeList,cuase by {}",e);
		}
		
		return "opermgmt/feacodeGroup/addFeacodeToGroup";
	}
	
	/**
	 * 将特征码加入分组
	 * @param request
	 * @param featureCodeNos
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveToGroup.ation")
	@ResponseBody
	public String saveToGroup (HttpServletRequest request,
				Long[] featureCodeNos ,Long groupNo) throws Exception{
		boolean result = false;
		String str = "";
		FeatureCodeGroup group = null;
		try {
			if(featureCodeNos != null && featureCodeNos.length > 0) {
				List<FeatureCode> list = this.featureCodeService.findFeatureCodesById(featureCodeNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getFeatureCodeVal() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getFeatureCodeVal();
				group = this.featureCodeGroupService.findFeaGroupByNo(groupNo);
				this.feacodeGroupMapService.add(groupNo,featureCodeNos);
			}
			result = true;
			logService.logToDB(request, group.getGroupName()+"组增加[ " + str + " ]特征码", LogUtil.LOG_INFO, result, true);
		} catch (Exception e) {
			result = false;
			logService.logToDB(request, group.getGroupName()+"组增加[ " + str + " ]特征码", LogUtil.LOG_ERROR, result, true);
			logger.error("saveFeatureCode to group exception occurred, cause by:{}" ,e);
		}
		
		return "{result: '" + result + "', desc : ''}";
	}
	
	/**
	 * 删除组内特征码
	 * @param request
	 * @param featureCodeNos
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/feacodeGroupDelete.action")
	@ResponseBody
	public String feacodeGroupDelete(HttpServletRequest request,
			Long[] featureCodeNos ,Long groupNo) throws Exception {
		boolean result = false;
		String str = "";
		FeatureCodeGroup group = null;
		try {
			if(featureCodeNos != null && featureCodeNos.length > 0){
				List<FeatureCode> list = this.featureCodeService.findFeatureCodesById(featureCodeNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getFeatureCodeVal() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getFeatureCodeVal();
				group = this.featureCodeGroupService.findFeaGroupByNo(groupNo);
				
				this.feacodeGroupMapService.deleteInGroupMap(groupNo ,featureCodeNos);
			}
			result = true;
			logService.logToDB(request, group.getGroupName()+"删除[ " + str + " ]特征码", LogUtil.LOG_INFO, result, true);
		} catch (Exception e) {
			result = false;
			logService.logToDB(request, group.getGroupName()+"删除[ " + str + " ]特征码", LogUtil.LOG_ERROR, result, true);
			logger.error("delete featureCode In Group occured exception, cause by:{}" ,e);
		}
		
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value="/feacodeGroupDetail.action")
	public String feacodeGroupDetail(HttpServletRequest request, ModelMap modelMap,
			Long groupNo) throws Exception {
		FeatureCodeGroup group = this.featureCodeGroupService.findFeaGroupDetail(groupNo);
		modelMap.put("group", group);
		
		return "opermgmt/feacodeGroup/groupDetail";
	}
	
	/**
	 * 进入新增编辑页面
	 * @param request
	 * @param modelMap
	 * @param search
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/featureCodeGroupEdit.action")
	public String featureCodeGroupEdit(HttpServletRequest request, ModelMap modelMap,
			FeatureCodeGroup search, Long groupNo) throws Exception {
		//Operator curOper = SessionUtil.getActiveOperator(request);
		FeatureCodeGroup group =new FeatureCodeGroup();
		if(groupNo != null && groupNo > 0) {
			group = this.featureCodeGroupService.findFeaGroupByNo(groupNo);
		}
		modelMap.put("group", group);
		modelMap.put("search", search);
		return "opermgmt/feacodeGroup/groupEdit";
	}
	
	@RequestMapping(value="/checkGroupName.action")
	@ResponseBody
	public String checkfeatureCodeVal(FeatureCodeGroup  group) throws Exception {
		boolean result = false;
		FeatureCodeGroup data = new FeatureCodeGroup();
		data.setGroupName(group.getGroupName());
        result = this.featureCodeGroupService.isRepeatGroupName(data);
        return "{result: '" + !result + "', desc : ''}";
	}
	
	
	@RequestMapping(value="/checkGroupId.action")
	@ResponseBody
	public String checkGroupId(FeatureCodeGroup  group) throws Exception {
		boolean result = false;
		FeatureCodeGroup data = new FeatureCodeGroup();
		data.setGroupId(group.getGroupId());
        result = this.featureCodeGroupService.isRepeatGroupId(data);
        return "{result: '" + !result + "', desc : ''}";
	}
	
	/**
	 *  新增、编辑保存
	 * @param request
	 * @param response
	 * @param featureCodeGroup
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveOrUpdate.action")
	@ResponseBody
	public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response, 
				FeatureCodeGroup featureCodeGroup) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {
			if(featureCodeGroup.getGroupNo() != null && featureCodeGroup.getGroupNo() > 0) { //update		
				logStr = "更新[ " + featureCodeGroup.getGroupName() + " ]特征码组";
				featureCodeGroup.setUpdateTime(DateUtil.getCurrentTime());
				this.featureCodeGroupService.updateGroup(featureCodeGroup);
			}
			else { //new 
				logStr = "新增[ " + featureCodeGroup.getGroupName() + " ]特征码组";
				featureCodeGroup.setOperatorNo(curOper.getOperatorNo());
				this.featureCodeGroupService.saveGroup(featureCodeGroup);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			result = false;
			logger.error(e);
			logService.logToDB(request, logStr, LogUtil.LOG_ERROR, true, true);
		}
		
		return "{result: '" + result + "', desc : ''}";
	}
	
	/**
	 * 删除特征码组
	 * @param request
	 * @param response
	 * @param groupNos
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/groupDelete.action")
	@ResponseBody
	public String groupDelete(HttpServletRequest request, HttpServletResponse response, 
			Long[] groupNos) throws Exception {		
		boolean result = false;
		String str = "";
		try {
			if(groupNos !=null && groupNos.length > 0) {
				List<FeatureCodeGroup> gList = this.featureCodeGroupService.findGroups(groupNos);
				for(int i=0; i<gList.size()-1; i++) {
					str += gList.get(i).getGroupName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += gList.get(gList.size() - 1).getGroupName();
				
				//删除code_group_map中的数据
				this.feacodeGroupMapService.deleteGroupNos(groupNos);
				this.featureCodeGroupService.deleteGroups(groupNos);
			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]特征码组", LogUtil.LOG_INFO, true, true);
		}
		catch (Exception e) {
			result = false;
			logService.logToDB(request, "删除[" + str + "]特征码", LogUtil.LOG_ERROR,false, true);
			logger.error("Delete featureCodeGroup occurred exception, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value="/groupSelect.action")
	public String groupSelect (HttpServletRequest request, ModelMap modelMap,
				FeatureCodeGroup search) throws Exception{
		Operator curOper = SessionUtil.getActiveOperator(request);
		search.setCurOper(curOper);
		List<FeatureCodeGroup> list = this.featureCodeGroupService.findFeatureCodeGroups(search);
		modelMap.put("list", list);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		return "opermgmt/featureCode/groupSelect";
	}
	
}
