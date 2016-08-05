package com.db.bms.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.ArticleMapper;
import com.db.bms.dao.ColumnArticleMapMapper;
import com.db.bms.dao.ResourceAllocationMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.dao2.ColumnDao;
import com.db.bms.entity.Article;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.PortalPublishNotice;
import com.db.bms.entity.ResourceAllocation;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.Article.ArticleStatus;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnService;
import com.db.bms.service.OperatorService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.GetColumnREQT;
import com.db.bms.sync.portal.protocal.GetColumnRESP;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;

/**
 * <b>功能：</b>用于事物处理<br>
 */

@Service("columnService")
public class ColumnServiceImpl implements ColumnService {

	@Autowired
	private ColumnDao columnDao;
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
	
	@Autowired
	private ColumnArticleMapMapper columnArticleMapMapper;
	
	@Autowired
	private ResourceAllocationMapper resourceAllocationMapper;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
    private ArticleService articleService;
	
	@Autowired
	StrategyService strategyService;
	
	public ColumnDao getDao() {
		return columnDao;
	}

	
	@Override
	public boolean queryOperatorAdminPermission(Column column) throws Exception {
		boolean t = columnDao.queryOperatorAdminPermission(column);
		return t;
	}

	@Override
	public Column selectByNo(Long id) throws Exception {
		Column col = columnDao.selectByNo(id);
		return col;
	}

	@Override
	public Column selectByNoWithCompany(Long id) throws Exception {
		Column col = columnDao.selectByNoWithCompany(id);
		return col;
	}


	@Override
	public List<Column> selectByNos(Long[] ids) throws Exception {
		List<Column> ret = columnDao.selectByNos(ids);
		return ret;
	}
	//------------------------------------------------------------------------
	
	@Override
	public List<Column> selectAllColumns(Column search) throws Exception {
        switch (OperatorType.getType(search.getCurOper().getType())){
        case SUPER_ADMIN:
        	break;
        case COMPANY_ADMIN:
        	search.setGroupId(search.getCurOper().getOperatorNo());
        	search.setAllocResourceIds(resourceAllocationMapper.findAllocResourceIdsFilterCmd(ResourceAllocation.ResourceType.COLUMN.getIndex(), search.getCurOper().getOperatorNo(),"6"));
        	break;
        case ORDINARY_OPER:
//        	search.setOperatorNo(search.getCurOper().getOperatorNo());
//        	List<Long> allocResourceIds = resourceAllocationMapper.findAllocResourceIds(ResourceAllocation.ResourceType.COLUMN.getIndex(), search.getCurOper().getOperatorNo());
        	search.setGroupId(search.getCurOper().getCreateBy());
        	search.setAllocResourceIds(resourceAllocationMapper.findAllocResourceIdsFilterCmd(ResourceAllocation.ResourceType.COLUMN.getIndex(), search.getCurOper().getCreateBy(),"6"));
        	break;
        }
		List<Column> ret = columnDao.selectColumns(search);
		return ret;
	}
	
	
    /**
     * 根据版块no,查询此版块的顶级版块和level
     * @param columnNo
     * @return
     * @throws Exception
     */
    public Column selectColumnTopestParentAndLevel(Long columnNo) throws Exception {
    	Column ret = this.columnDao.selectColumnTopestParentAndLevel(columnNo);
    	return ret;
    }

    /**
     * 根据parent和level查询版块列表
     * @param columnNo 
     * @param level 
     * @return
     * @throws Exception
     */
	@Override
	public List<Column> selectColumnByParentAndLevel(Long columnNo, Integer level) throws Exception {
		List<Column> list = this.columnDao.selectColumnByParentAndLevel(columnNo, level);
		return list;
	}


    /**
     * 查询一个版块可选的parent column
     * @param columnNo - column no. 
     * @return
     * @throws Exception
     */
	@Override
    public List<Column> selectAvailableParentColumn(Long columnNo)  throws Exception {
		List<Column> ret = columnDao.selectAvailableParentColumn(columnNo);
		return ret;
    }


	@Override
	public List<Column> selectColumns(Column search) throws Exception {
		List<Column> ret = columnDao.selectColumns(search);
		return ret;
	}
	
	@Override
	public List<Column> selectCompanyColumns(Column search) throws Exception {
		List<Column> ret = columnDao.selectCompanyColumns(search);
		return ret;
	}
	
	@Override
	public List<Column> selectNewColumnForArticle(Column column) throws Exception {
		List<Column> ret = columnDao.selectNewColumnForArticle(column);
		return ret;
	}
	//------------------------------------------------------------------------

	@Override
	public boolean isIdUnique(Column search) throws Exception {
		boolean t = this.columnDao.isIdUnique(search);
		return t;
	}
	
	@Override
	public boolean isNameUnique(Column search) throws Exception {
		boolean t = this.columnDao.isNameUnique(search);
		return t;
	}

	@Override
    public int getColumnRefCount(Long no) {
		int count = this.columnDao.getColumnRefCount(no);
		return count;
    }
	
	@Override
	public void addColumn(Column col) throws Exception {
		this.columnDao.addColumn(col);
	}
	
	@Override
	public void updateColumn(Column col) throws Exception {
		this.columnDao.updateColumn(col);
	}

	@Override
    public void deleteColumn(Column col) throws Exception {
		this.columnDao.deleteColumn(col);
    }
    
	@Override
	public void deleteColumns(List<Column> list) throws Exception {
		for (Column column : list) {
			if (StringUtils.isNotEmpty(column.getCover())){
			    Operator operator = operatorService.findOperatorById(column.getOperatorNo());
			    File f = new File(this.articleService.getFileDir() + "/" + column.getCover());
			    operatorService.calculateUsedSpace(operator, f, false);
			}
			this.columnDao.deleteColumn(column);
		}
		//this.columnDao.deleteColumns(list);
	}
	@Override
	public void deleteColumns(Long[] colNos) throws Exception {
		//删除中间表数据
		for (int i=0; i<colNos.length; i++) {
			this.columnArticleMapMapper.deleteColumnMap(colNos[i]);
		}
		this.columnDao.deleteColumns(colNos);
	}

	@Override
	public boolean checkUserPermission(Column search) {
		return false;
	}

	/**
	 * get all columns count  
	 * @return
	 */
	@Override
	public int getAllColumnsCount() throws Exception {
		Integer ret = this.columnDao.getAllColumnsCount();
		return ret;
	}

	@Override
	public void audit(Column column, Long[] nos, Integer status, ResourcePublishMap publish) throws Exception {
		//String updateTime = DateUtil.getCurrentTime();
		switch (status){
		case AuditStatus.PUBLISH:
			List<PortalPublishNotice> noticeList = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(noticeList);
			for (Long columnNo : nos){
				Column tempColumn = columnDao.selectByNo(columnNo);
				/* 发布时无需记录父级版块
				publish.setParentType(tempColumn.parentNo == null ? null: EntityType.TYPE_COLUMN);
				publish.setParentId(tempColumn.parentNo);
				*/
				publish.setParentType(null);
				publish.setParentId(null);
				publish.setType(EntityType.TYPE_COLUMN);
				publish.setResourceId(columnNo);
				resourcePublishMapMapper.addResourcePublishMap(publish);
				
				//添加发布版块消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
				notice.setResourceType(EntityType.TYPE_COLUMN);
				notice.setParentType(tempColumn.getParentNo() == null ? null : EntityType.TYPE_COLUMN);
				notice.setParentId(tempColumn.getParentNo());
				notice.setResourceId(columnNo);
				noticeList.add(notice);
				
				if (publish.getPcascade()){
					
					Article asearch = new Article();
					asearch.setColumnNo(columnNo);
					asearch.setStatus(ArticleStatus.AUDIT_PASS.getIndex());
					List<ColumnArticleMap> articleList = columnArticleMapMapper.selectColumnArticles(asearch);
					//查询出已经被别的版块发布了的也被本板块关联的文章
					asearch.setStatus(ArticleStatus.PUBLISH.getIndex());
					List<ColumnArticleMap> articlePublishedList = columnArticleMapMapper.selectColumnArticles(asearch);
					if(articleList == null){
						articleList = articlePublishedList;
					}
					else{
						articleList.addAll(articlePublishedList);
					}
					for (ColumnArticleMap map : articleList){
						map.getArticle().setCurOper(column.getCurOper());
						Article a = map.getArticle();
						a.setStatus(ArticleStatus.PUBLISH.getIndex());
						ArticleServiceImpl.updateStatus(articleMapper, ArticleStatus.PUBLISH.getIndex(), new Long[]{map.getArticleNo()});
						//articleDao.audit(a, new Long[]{map.getArticleNo()});
						publish.setType(EntityType.TYPE_ARTICLE);
						publish.setParentType(EntityType.TYPE_COLUMN);
						publish.setParentId(columnNo);
						publish.setResourceId(map.getArticleNo());
						resourcePublishMapMapper.addResourcePublishMap(publish);
						
						//添加发布文章消息
					    notice = new PortalPublishNotice();
						notice.setActionType(PortalPublishNotice.ACT_PUBLISH);
						notice.setResourceType(EntityType.TYPE_ARTICLE);
						notice.setParentType(EntityType.TYPE_COLUMN);
						notice.setParentId(columnNo);
						notice.setResourceId(map.getArticleNo());
						noticeList.add(notice);

					}
				}
			}
			break;
			
		case AuditStatus.UNPUBLISH:
			noticeList = new ArrayList<PortalPublishNotice>();
			publish.setNoticeList(noticeList);
			status = AuditStatus.AUDIT_PASS;
			for (Long columnNo : nos){
				Column tempColumn = columnDao.selectByNo(columnNo);
				/*
				resourcePublishMapMapper.deleteResourcePublishMaps(
						tempColumn.getParentNo() == null ? null : EntityType.TYPE_COLUMN,tempColumn.getParentNo() , EntityType.TYPE_COLUMN, columnNo);
				*/
				
				resourcePublishMapMapper.deleteResourcePublishMaps(null, null, EntityType.TYPE_COLUMN, columnNo);
				
				Article asearch = new Article();
				asearch.getPageUtil().setPaging(false);
				asearch.setColumnNo(columnNo);
				asearch.setStatus(ArticleStatus.PUBLISH.getIndex());
				
				List<ColumnArticleMap> articleList = columnArticleMapMapper.selectColumnArticles(asearch);
				
				//添加取消发布版块消息
				PortalPublishNotice notice = new PortalPublishNotice();
				notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
				notice.setResourceType(EntityType.TYPE_COLUMN);
				notice.setParentType(tempColumn.getParentNo() == null ? null : EntityType.TYPE_COLUMN);
				notice.setParentId(tempColumn.getParentNo());
				notice.setResourceId(columnNo);
				noticeList.add(notice);

				for (ColumnArticleMap map : articleList){
					resourcePublishMapMapper.deleteResourcePublishMaps(EntityType.TYPE_COLUMN,columnNo, EntityType.TYPE_ARTICLE, map.getArticleNo());
					
					// 如果所有的发布都取消，更新article状态
					List<ResourcePublishMap> albumPublishList = resourcePublishMapMapper.findResourcePublishMapByResId(null, null, EntityType.TYPE_ARTICLE, map.getArticleNo());
					if (ArrayUtils.getSize(albumPublishList)==0){
						map.getArticle().setCurOper(column.getCurOper());
						Article a = map.getArticle();
						a.setStatus(ArticleStatus.UNPUBLISH.getIndex());
						//articleDao.audit(a, new Long[]{map.getArticleNo()});
						ArticleServiceImpl.updateStatus(articleMapper, ArticleStatus.UNPUBLISH.getIndex(), new Long[]{map.getArticleNo()});
					}
					
					//添加取消发布文章消息
				    notice = new PortalPublishNotice();
					notice.setActionType(PortalPublishNotice.ACT_UNPUBLISH);
					notice.setResourceType(EntityType.TYPE_ARTICLE);
					notice.setParentType(EntityType.TYPE_COLUMN);
					notice.setParentId(columnNo);
					notice.setResourceId(map.getArticleNo());
					noticeList.add(notice);
				}
			}
			break;
		}
		
		this.columnDao.audit(column, nos, status);
	}

	@Override
	public GetColumnRESP getColumnList(HttpServletRequest req, GetColumnREQT request) throws Exception {
		GetColumnRESP ret = this.columnDao.getColumnList(req, request);
		return ret;
	}


	@Override
	public List<Column> selectChildColumns(Column column) throws Exception {
		List<Column> ret = columnDao.selectChildColumn(column);
		return ret;
	}

	/*
	private boolean compareColumn(Long[] columnNos, List<ResourcePublishMap> articlePublishList){
		boolean flag = true;
		Map<Long, Long> columnNoMap = new HashMap<Long, Long>();
		for (Long columnNo : columnNos){
			columnNoMap.put(columnNo, columnNo);
		}
		
		for (ResourcePublishMap publish : articlePublishList){
			if (!columnNoMap.containsKey(publish.getParentId())){
				flag = false;
				return false;
			}
		}
		return flag;
	}
	*/

	@Override
	public List<Column> findArticlePublishColumns(Integer type,
			Long resourceid, Column search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = columnDao.findArticlePublishColumnCount(type, resourceid, search);
		page.setRowCount(count);
		return columnDao.findArticlePublishColumns(type, resourceid, search);
	}


	@Override
	public List<Column> findResourceColumnNoPublishs(Long resourceId,
			Column column) throws Exception {
		PageUtil page = column.getPageUtil();
		page.setPaging(true);
		int count = columnDao.findResourceColumnNoPublishCount(new Long[]{resourceId}, column);
		page.setRowCount(count);
		return columnDao.findResourceColumnNoPublishs(new Long[]{resourceId}, column);
	}


	@Override
	public List<Column> findAllResourceColumnNoPublishs(Long[] resourceIds,
			Column column) throws Exception {
		return columnDao.findResourceColumnNoPublishs(resourceIds, column);
	}


	@Override
	public boolean isCotainChildCol(Long columnNo) throws Exception {
		Integer count = this.columnDao.getColumnRefCount(columnNo);
		return count > 0? true:false;
	}

    /**
     * 版块策略
     */
	@Override
	public List<Column> findColumnWithStrategy(Column search) throws Exception {
		PageUtil pu = search.getPageUtil();
		if(pu.getPaging()) {
			int c =  columnDao.findColumnWithStrategyCount(search);
			pu.setRowCount(c);
		}
		else {
			pu.setRowCount(-1);
		}
		
		List<Column> list = columnDao.findColumnWithStrategy(search);
		
		Map<Long, Strategy> stMap = new HashMap<Long, Strategy>();
		
		for(Column t:list) {
			if(t.strategy!=null) {
				Strategy s = stMap.get(t.strategy.strategyNo);
				if(s == null) {
					s = strategyService.getStrategyAllData(t.strategy.strategyNo);
					stMap.put(s.strategyNo, s);
				}
				t.setStrategy(s);
			}
		}
		return list;
	}


	@Override
	public Integer getColumnCountByCompanyNo(Long companyNo) throws Exception {		
		Column search = new Column();
		search.setCompanyNo(companyNo);
		return this.columnDao.getColumnCount(search);
	}

}
