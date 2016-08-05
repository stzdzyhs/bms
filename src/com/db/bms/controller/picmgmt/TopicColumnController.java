
package com.db.bms.controller.picmgmt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.entity.Album;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Company;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.Topic;
import com.db.bms.entity.TopicColumn;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.TopicColumn.ColumnStatus;
import com.db.bms.service.AlbumService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.TopicColumnService;
import com.db.bms.service.TopicService;
import com.db.bms.sync.portal.engine.PortalProcessor;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;


@RequestMapping("picmgmt/column")
@Controller
public class TopicColumnController {

	private final static Logger logger = Logger
	.getLogger(TopicColumnController.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private PortalProcessor processor;
	
	@Autowired
	private TopicColumnService topicColumnService;
	
	@Autowired
	private ResourceAllocationService resourceAllocationService;
	
	@RequestMapping(value = "/columnList.action")
	public String columnList(HttpServletRequest request, ModelMap modelMap, TopicColumn search)
			throws Exception {
		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        PageUtil page = search.getPageUtil();
    	page.setPaging(true);
        List<TopicColumn> list = topicColumnService.findColumns(search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("columnStatusMap", ConstConfig.columnStatusMap);
		return "picmgmt/column/columnList";
	}
	
    @RequestMapping(value = "/columnEdit.action")
    public String columnEdit(HttpServletRequest request, ModelMap modelMap, TopicColumn search, Long columnId) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);
    	TopicColumn column = new TopicColumn();
    	if (columnId != null && columnId > 0){
    		column = topicColumnService.findColumnById(columnId);
    	}

    	Topic topic = topicService.findTopicById(search.getTopicId());
    	modelMap.put("topic", topic);
    	modelMap.put("column", column);
    	modelMap.put("search", search);
		modelMap.put("columnStatusMap", ConstConfig.columnStatusMap);
    	return "picmgmt/column/columnEdit";
    }

	@RequestMapping(value = "/saveOrUpdateColumn.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateTopic(HttpServletRequest request,
			HttpServletResponse response, TopicColumn column) throws Exception {
		Operator curOper = SessionUtil.getActiveOperator(request);
		boolean result = false;
		String logStr = "";
		try {
			if (column.getId() != null && column.getId() > 0) {
				logStr = "更新[" + column.getColumnName() + "]栏目";
				column.setStatus(ColumnStatus.DRAFT.getIndex());
				column.setUpdateTime(DateUtil.getCurrentTime());
				topicColumnService.updateColumn(column);
			} else {
				Topic topic = topicService.findTopicById(column.getTopicId());
				
				logStr = "添加[" + column.getColumnName() + "]栏目";
				column.setStatus(ColumnStatus.DRAFT.getIndex());
				column.setCreateTime(DateUtil.getCurrentTime());
				column.setOperatorNo(curOper.getOperatorNo());
				column.setCompanyNo(topic.getCompanyNo());
				column.setGroupId(curOper.getCreateBy());
				topicColumnService.addColumn(column);
			}
			result = true;
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			if (column.getId() == null) {
				logger.error("Add column exception occurred, cause by:{}", e);
			} else {
				logger.error("Update column exception occurred, cause by:{}", e);
			}
			logService.logToDB(request, logStr, LogUtil.LOG_INFO, false, true);
			result = false;
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/columnDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String columnDelete(HttpServletRequest request,
			HttpServletResponse response, Long[] columnIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			
			if (columnIds != null && columnIds.length > 0) {
				List<TopicColumn> list = topicColumnService.findColumnsById(columnIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getColumnName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getColumnName();
				
				topicColumnService.deleteColumns(columnIds);
			}
			result = true;
			logService.logToDB(request, "删除[" + str + "]栏目", LogUtil.LOG_INFO,
					true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除[" + str + "]栏目", LogUtil.LOG_ERROR,
					false, true);
			result = false;
			logger.error("Delete column exception occurred, cause by:{}", e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/columnDetail.action")
	public String columnDetail(HttpServletRequest request, ModelMap modelMap,
			Long columnId) throws Exception {

		TopicColumn column = this.topicColumnService.findColumnById(columnId);
		modelMap.put("column", column);
		modelMap.put("columnStatusMap", ConstConfig.columnStatusMap);
		return "picmgmt/column/columnDetail";
	}
	
	@RequestMapping(value = "/columnEnable.action", method = RequestMethod.POST)
	@ResponseBody
	public String columnEnable(HttpServletRequest request,
			Integer status, Long[] columnIds) throws Exception {
		boolean result = false;
		String str = "";
		try {
			if (columnIds != null && columnIds.length > 0) {
				List<TopicColumn> list = topicColumnService.findColumnsById(columnIds);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getColumnName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getColumnName();
				
				List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
				for (TopicColumn column : list){
					column.setStatus(status);
					column.setUpdateTime(DateUtil.getCurrentTime());
					topicColumnService.updateColumn(column);
					
					PortalPublishNotice notice = new PortalPublishNotice();
					notice.setParentId(column.getTopicId());
					notice.setResourceId(column.getId());
					notice.setResourceType(EntityType.TYPE_MENU);
					switch (ColumnStatus.getStatus(status)){
					case ENABLE:
						notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
						break;
					case DISABLE:
						notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
						break;
					}
					noticeList.add(notice);
				}

				processor.putNoticeToQueue(noticeList);

			}
			result = true;
			switch (ColumnStatus.getStatus(status)){
			case ENABLE:
				logService.logToDB(request, "栏目[" + str + "]启用",
						LogUtil.LOG_INFO, true, true);
				break;
			case DISABLE:
				logService.logToDB(request, "栏目[" + str + "]禁用",
						LogUtil.LOG_INFO, true, true);
				break;
			}

		} catch (Exception e) {
			result = false;
			switch (ColumnStatus.getStatus(status)){
			case ENABLE:
				logService.logToDB(request, "栏目[" + str + "]启用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Enable column exception occurred, cause by:{}",
						e);
				break;
			case DISABLE:
				logService.logToDB(request, "栏目[" + str + "]禁用",
						LogUtil.LOG_INFO, false, true);
				logger.error(
						"Disable column exception occurred, cause by:{}",
						e);
				break;
			}

		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/columnAlbumSelect.action")
	public String columnAlbumSelect(HttpServletRequest request,
			ModelMap modelMap, Long columnId, Album search) throws Exception {

		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
//        switch (OperatorType.getType(curOper.getType())){
//        case SUPER_ADMIN:
//        	break;
//        case COMPANY_ADMIN:
//        	search.setOperatorNo(curOper.getOperatorNo());
//        	break;
//        case ORDINARY_OPER:
//        	search.setOperatorNo(curOper.getOperatorNo());
//        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getOperatorNo());
//        	search.setAllocResourceIds(allocResourceIds);
//        	break;
//        }

        List<Album> list = topicService.findResourceAlbums(EntityType.TYPE_MENU, columnId, search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("columnId", columnId);
		modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
		return "picmgmt/album/columnAlbumSelect";
	}

	@RequestMapping(value = "/columnAlbumNoSelect.action")
	public String columnAlbumNoSelect(HttpServletRequest request,
			ModelMap modelMap, Long columnId, Album search) throws Exception {

		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
		Long topicId = -1l;
		TopicColumn topicColumn = this.topicColumnService.findColumnById(columnId);
		if(topicColumn != null){
			Topic topic = this.topicService.findTopicById(topicColumn.getTopicId());
			search.setCaptureFlag(topic.getCaptureFlag());
			topicId = topic.getId();
		}
        Operator curOper = SessionUtil.getActiveOperator(request);
        search.setCurOper(curOper);
        search.getPageUtil().setPaging(true);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(curOper.getOperatorNo());
        	search.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getOperatorNo()));
        	break;
        case ORDINARY_OPER:
        	search.setGroupId(curOper.getCreateBy());
        	List<Long> allocResourceIds = resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.ALBUM.getIndex(), curOper.getCreateBy());
        	search.setAllocResourceIds(allocResourceIds);
        	break;
        }
        
        List<Album> list = topicService.findColumnAlbumsNoSelect(EntityType.TYPE_MENU, columnId, search,topicId);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);

        List<Company> companyList = companyService.findAllCompanys(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("companyList", companyList);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("columnId", columnId);
		modelMap.put("albumStatusMap", ConstConfig.albumStatusMap);
		return "picmgmt/album/columnAlbumNoSelect";
	}

	@RequestMapping(value = "/saveColumnAlbum.action", method = RequestMethod.POST)
	@ResponseBody
	public String saveColumnAlbum(HttpServletRequest request,
			Long[] albumNos, Long columnId) throws Exception {
		boolean result = false;
		String str = "";
		TopicColumn column = null;
		Operator curOper = SessionUtil.getActiveOperator(request);
		OperatorType opType = OperatorType.getType(curOper.getType());
		long createdByNo = -1;
		switch (opType) {
		case SUPER_ADMIN:
			createdByNo = -1;
			break;
		case COMPANY_ADMIN:
			createdByNo = curOper.getOperatorNo();
			break;
		case ORDINARY_OPER:
			createdByNo = curOper.getCreateBy();
			break;
		default:
			createdByNo = -1;
			break;
		}
		try {
			if (albumNos != null && albumNos.length > 0) {
				List<Album> list = albumService.findAlbumsById(albumNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getAlbumName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getAlbumName();

				column = topicColumnService.findColumnById(columnId);
				topicService.addResourceAlbumMap(EntityType.TYPE_MENU,columnId, albumNos,createdByNo);
			}
			result = true;
			logService.logToDB(request, "栏目[" + column.getColumnName() + "]关联["
					+ str + "]相册", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "栏目[" + column.getColumnName() + "]关联["
					+ str + "]相册", LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error(
					"Column associated albums exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}

	@RequestMapping(value = "/columnAlbumDelete.action", method = RequestMethod.POST)
	@ResponseBody
	public String columnAlbumDelete(HttpServletRequest request,
			Long[] albumNos, Long columnId) throws Exception {
		boolean result = false;
		String str = "";
		TopicColumn column = null;
		try {
			if (albumNos != null && albumNos.length > 0) {
				List<Album> list = albumService.findAlbumsById(albumNos);
				for (int i = 0; i < list.size() - 1; i++) {
					str += list.get(i).getAlbumName() + Delimiters.COMMA;
					if (str.length() > 200) {
						str += "...";
						break;
					}
				}
				str += list.get(list.size() - 1).getAlbumName();
				column = topicColumnService.findColumnById(columnId);
				topicService.deleteResourceAlbumMapsByAlbumId(EntityType.TYPE_MENU,columnId, albumNos);
			}
			result = true;
			logService.logToDB(request, "删除栏目[" + column.getColumnName()
					+ "]相册[" + str + "]", LogUtil.LOG_INFO, true, true);
		} catch (Exception e) {
			logService.logToDB(request, "删除栏目[" + column.getColumnName()
					+ "]相册[" + str + "]", LogUtil.LOG_ERROR, false, true);
			result = false;
			logger.error(
					"Delete column associated albums exception occurred, cause by:{}",
					e);
		}
		return "{result: '" + result + "', desc : ''}";
	}
	
	@RequestMapping(value = "/resourceColumnNoPublishList.action")
	public String resourceColumnNoPublishList(HttpServletRequest request, ModelMap modelMap, Long resourceId, TopicColumn search)
			throws Exception {
		//先找出该操作员可以看到的专题，从而为找出该操作员可以看到的栏目提供查询条件
		Topic topicSearch = new Topic();
        Operator curOper = SessionUtil.getActiveOperator(request);
        topicSearch.setCurOper(curOper);
        switch (OperatorType.getType(curOper.getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	topicSearch.setGroupId(curOper.getOperatorNo());
        	topicSearch.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getOperatorNo()));
        	break;
        case ORDINARY_OPER:
        	topicSearch.setGroupId(curOper.getCreateBy());
        	topicSearch.setAllocResourceIds(resourceAllocationService.findAllocResourceIds(ResourceAllocation.ResourceType.TOPIC.getIndex(), curOper.getCreateBy()));
        	break;
        }

		search.setStatus(AuditStatus.PUBLISH);
        Long[] resourceIds = new Long[1];
        resourceIds[0] = resourceId;
		List<Topic> topicList = topicService.findAllResourceTopicNoPublishs(resourceIds, topicSearch);
		Long[] topicIds = new Long[topicList.size()];
		int idx = 0;
		for (Topic topic : topicList) {
			topicIds[idx++] = topic.getId();
		}
		if (StringUtils.isEmpty(search.getSortKey())){
			search.setSortKey("createTime");
			search.setSortType("desc");
		}
        search.setCurOper(curOper);;
        search.setStatus(ColumnStatus.ENABLE.getIndex());
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
        
        List<TopicColumn> list = topicColumnService.findAllResourceColumnNoPublishsWithTopicIds(topicIds, resourceIds, search);
        List<Operator> operatorList = operatorService.findAllOperators(curOper);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("operatorList", operatorList);
		modelMap.put("pageUtil", search.getPageUtil());
		modelMap.put("search", search);
		modelMap.put("resourceId", resourceId);
		modelMap.put("columnStatusMap", ConstConfig.columnStatusMap);
		return "picmgmt/column/resourceColumnNoPublishList";
	}
}
