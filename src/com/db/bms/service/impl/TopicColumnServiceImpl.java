
package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.FeacodeGroupMapMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.ResourceAlbumMapMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.dao.TopicColumnMapper;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.TopicColumn;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.entity.TopicColumn.ColumnStatus;
import com.db.bms.service.StrategyService;
import com.db.bms.service.TopicColumnService;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetMenuREQT;
import com.db.bms.sync.portal.protocal.GetMenuRESP;
import com.db.bms.sync.portal.protocal.MenuInfo;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;


@Service("topicColumnService")
public class TopicColumnServiceImpl implements TopicColumnService {

	@Autowired
	private TopicColumnMapper topicColumnMapper;
	
	@Autowired
	private ResourceAlbumMapMapper resourceAlbumMapMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private FeacodeGroupMapMapper feacodeGroupMapMapper;
	
	@Autowired
	StrategyService strategyService;
	
	@Override
	public TopicColumn findColumnById(Long columnId) throws Exception {
		return topicColumnMapper.findColumnById(columnId);
	}

	@Override
	public List<TopicColumn> findColumnsById(Long[] columnIds) throws Exception {
		return topicColumnMapper.findColumnsById(columnIds);
	}

	@Override
	public List<TopicColumn> findColumns(TopicColumn search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = topicColumnMapper.findColumnCount(search);
		page.setRowCount(count);
		return topicColumnMapper.findColumns(search);
	}

	@Override
	public void addColumn(TopicColumn column) throws Exception {
		topicColumnMapper.addColumn(column);
	}
	
	@Override
	public void updateColumn(TopicColumn column) throws Exception {
		topicColumnMapper.updateColumn(column);
	}

	@Override
	public void deleteColumns(Long[] columnIds) throws Exception {
		resourceAlbumMapMapper.deleteResourceAlbumMaps(EntityType.TYPE_MENU, columnIds);
		topicColumnMapper.deleteColumnsById(columnIds);
	}
	
	@Override
	public List<TopicColumn> findAllColumns(TopicColumn search)
			throws Exception {
		return topicColumnMapper.findColumns(search);
	}

	@Override
	public GetMenuRESP getMenuList(GetMenuREQT request) throws Exception {
		GetMenuRESP response = new GetMenuRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setTopicId(request.getTopicId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<MenuInfo> menuInfoList = new ArrayList<MenuInfo>();
		response.setMenuList(menuInfoList);
		
		Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
	    if (portal == null){
			response.setResultCode(CommonResultCode.NOT_FOUND_SYSTEM.getResultCode());
			response.setResultDesc("Could not find the system.");
			return response;
	    }
	    
	    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
			response.setResultCode(CommonResultCode.NO_ACCESS_RIGHTS.getResultCode());
			response.setResultDesc("No access rights.");
			return response;
	    }
	    
	    if (StringUtils.isNotEmpty(request.getMenuId())){
	    	TopicColumn column = topicColumnMapper.findColumnById(Long.valueOf(request.getMenuId()));
	    	if (TopicColumn.ColumnStatus.getStatus(column.getStatus()) != TopicColumn.ColumnStatus.ENABLE){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	    	
	    	MenuInfo menuInfo = convertMenu(Long.valueOf(request.getTopicId()),column);
	    	menuInfoList.add(menuInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
		
		TopicColumn search = new TopicColumn();
		search.setTopicId(Long.valueOf(request.getTopicId()));
		search.setStatus(ColumnStatus.ENABLE.getIndex());
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		
		int totalCount = topicColumnMapper.findColumnCount(search);
		page.setRowCount(totalCount);
		List<TopicColumn> columnList = topicColumnMapper.findColumns(search);
		Iterator<TopicColumn> it = columnList.iterator();
		while (it.hasNext()){
			TopicColumn column = it.next();
			MenuInfo menuInfo = convertMenu(Long.valueOf(request.getTopicId()),column);
			menuInfoList.add(menuInfo);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private MenuInfo convertMenu(Long topicId, TopicColumn column) throws Exception{
		MenuInfo menuInfo = new MenuInfo();
		menuInfo.setMenuNo(column.getId());
		menuInfo.setMenuId(String.valueOf(column.getId()));
		menuInfo.setShowOrder(column.getShowOrder());
		menuInfo.setMenuTitle(column.getColumnName());
		ResourcePublishMap publish = null;
		//获取栏目所属的专题的发布策略
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(null, null, EntityType.TYPE_TOPIC, topicId);
		if(list != null && !list.isEmpty()){
			publish = list.iterator().next();			
		}
		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){				
				menuInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}		
		return menuInfo;
	}

	@Override
	public List<TopicColumn> findResourceColumnNoPublishs(Long resourceId,
			TopicColumn search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = topicColumnMapper.findResourceColumnNoPublishCount(new Long[]{resourceId}, search);
		page.setRowCount(count);
		return topicColumnMapper.findResourceColumnNoPublishs(new Long[]{resourceId}, search);
	}

	@Override
	public List<TopicColumn> findAllResourceColumnNoPublishs(
			Long[] resourceIds, TopicColumn search) throws Exception {
		Operator curOper = search.getCurOper();
		if (curOper != null){
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
		}

		return topicColumnMapper.findResourceColumnNoPublishs(resourceIds, search);
	}

	@Override
	public List<TopicColumn> findAllResourceColumnNoPublishsWithTopicIds(
			Long[] topicIds, Long[] resourceIds, TopicColumn search)
			throws Exception {
		Operator curOper = search.getCurOper();
		if (curOper != null){
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
		}
		return topicColumnMapper.findResourceColumnNoPublishsWithTopicIds(topicIds, resourceIds, search);
	}

}
