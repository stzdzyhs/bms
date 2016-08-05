package com.db.bms.dao2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.db.bms.dao.ColumnArticleMapMapper;
import com.db.bms.dao.ColumnMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.ResourcePublishMapMapper;
import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Column;
import com.db.bms.entity.EntityType;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.ColumnInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetColumnREQT;
import com.db.bms.sync.portal.protocal.GetColumnRESP;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;

@Component("columnDao")
public class ColumnDao {
	//private static Logger log = Logger.getLogger(ColumnDao.class);
	
	@Autowired
	private ColumnMapper columnMapper;
	
	@Autowired
	private ColumnArticleMapMapper colartMapper;
	
	@Autowired
	private ResourcePublishMapMapper resourcePublishMapMapper;
		
	@Autowired
	StrategyService strategyService;
	
	@Value("${portal.sysUrl}")
	private String portalUrl;
	
	public ColumnMapper getMapper() {
		return columnMapper;
	}

	
	public boolean queryOperatorAdminPermission(Column column) throws Exception {
		Integer c = columnMapper.queryOperatorAdminPermission(column);
		if(c>0) {
			return true;
		}
		return false;
	}

	public Column selectByNo(Long id) throws Exception {
		Column col = columnMapper.selectByNo(id);
		return col;
	}
	
	public Column selectByNoWithCompany(Long id) throws Exception {
		Column col = columnMapper.selectByNoWithCompany(id);
		return col;
	}
	

	public List<Column> selectByNos(Long[] ids) throws Exception {
		List<Column> ret = columnMapper.selectByNos(ids);
		return ret;
	}
	//------------------------------------------------------------------------
	
	public List<Column> selectAllColumns(Column search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(false);
		List<Column> ret = selectColumns(search);
		return ret;
	}

	public List<Column> selectColumns(Column search) throws Exception {
		PageUtil page = search.getPageUtil();
		if (page.getPaging()) {
			int count = columnMapper.selectColumnCount(search);
			page.setRowCount(count);
		}
		
		List<Column> ret = columnMapper.selectColumns(search);
		return ret;
	}

	/**
	 * select company columns
	 * must set search.curOper, optionally set search.columnNo,
	 * if set search.columnNo, it will exclude that column and its children columns.
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<Column> selectCompanyColumns(Column search) throws Exception {
		if(search.getCurOper()==null) {
			throw new Exception("查询错误");
		}
		
		PageUtil page = search.getPageUtil();
		List<Column> ret = null;
		if(search.getColumnNo()==null) {
			if (page.getPaging()) {
				int count = columnMapper.selectColumnCount(search);
				page.setRowCount(count);
			}
			ret = columnMapper.selectColumns(search);
		}
		else { // exclude columns and its children columns specified by search.columnNo
			if (page.getPaging()) {
				int count = columnMapper.selectColumnsAndExcludeCount(search);
				page.setRowCount(count);
			}
			ret = columnMapper.selectColumnsAndExclude(search);
		}
		return ret;
	}

	public List<Column> selectNewColumnForArticle(Column column) throws Exception {
		PageUtil page = column.getPageUtil();
		if (page.getPaging()) {
			int count = columnMapper.selectNewColumnForArticleCount(column);
			page.setRowCount(count);
		}
		
		List<Column> ret = columnMapper.selectNewColumnForArticle(column);
		return ret;
	}
	//------------------------------------------------------------------------
	
	public boolean isIdUnique(Column search) throws Exception {
		// no need to check permission
		int count = this.columnMapper.getSameIdCount(search);
		return count > 0 ? false: true;
	}
	
	public boolean isNameUnique(Column search) throws Exception {
		// no need to check permission
		int count = this.columnMapper.getSameNameCount(search);
		return count > 0 ? false: true;
	}
	//------------------------------------------------------------------------
	

    public int getColumnRefCount(Long no) {
		int count = this.columnMapper.getColumnRefCount(no);
		return count;
    }

    // check write permission
    public void checkWritePermission(Column col) throws Exception {
    	Operator op = col.getCurOper();
		if(op==null) {
			throw new Exception("非法的operator");
		}
		
		Integer type = op.getType();
		if(type==null) {
			throw new Exception("非法的operator");
		}
		else if (type.equals(Operator.TYPE_SUPER_ADMIN)) {
		}
		else if (type.equals(Operator.TYPE_COMPANY_ADMIN)) {
			Long opc1 = op.getCompanyNo();
			Long opc2 = col.getCompanyNo();
			if( !opc1.equals(opc2) ) {
				throw new Exception("板块权限错误");
			}
		}
		else if (type.equals(Operator.TYPE_ORDINARY_OPER)) {
			Long opc1 = op.getCompanyNo();
			Long opc2 = col.getCompanyNo();
			if( !opc1.equals(opc2) ) {
				throw new Exception("板块权限错误");
			}
		}
		else {
			throw new Exception("非法的operator");
		}		
    }
    
    /**
     * add column
     * @param col
     * @throws Exception
     */
    public void addColumn(Column col)  throws Exception {
    	Operator op = col.getCurOper();
    	if(op==null) {
    		throw new Exception("增加版块权限问题");
    	}
    	
    	if(op.isSuperAdmin()) {
    		if (col.getCompanyNo()==null) {
    			throw new Exception("admin必须设置companyNo");
    		}
    	}
    	else {
    		if(op.getCompanyNo()==null) {
    			throw new Exception("用户必须从属公司!");
    		}
    		col.setCompanyNo(op.getCompanyNo());
    	}
		col.setOperatorNo(op.getOperatorNo());
    	
		// check if set
		if(col.getOperatorNo()==null) {
			throw new Exception("非法的操作员");
		}
		if(col.getCompanyNo()==null) {
			throw new Exception("非法的运营商");
		}
    	
		// check permission
    	checkWritePermission(col);
		
		col.setColumnNo(this.columnMapper.getPrimaryKey());
		String now = DateUtil.getCurrentTime();
		col.setCreateTime(now);
		col.setUpdateTime(now);			
		this.columnMapper.addColumn(col);
    }
    
    public void updateColumn(Column col) throws Exception {
    	// since not allow to change column's company, so, not check here.
		Integer ret = this.columnMapper.updateColumn(col);
		if(ret==null || ret!=1) {
			throw new Exception("更新板块错误");
		}
    }
    
	private void rmColumn(Column col) throws Exception {
		this.colartMapper.deleteColumnMap(col.getColumnNo());
		this.columnMapper.deleteColumn(col);
		/*int cnt = this.columnMapper.getColumnRefCount(col.getColumnNo());
		if(cnt>0) {
			String err = String.format("此板块:%s 被其它板块引用", col.getColumnId());
			throw new Exception(err);
		}
		
		//删除中间表数据
		this.colartMapper.deleteColumnMap(col.getColumnNo());
		
		// columnMapper.deleteColumn() will check permission 
		cnt = this.columnMapper.deleteColumn(col);
		if(cnt!=1) {
			throw new Exception("板块数据错误");
		}*/
	}
	
    public void deleteColumn(Column col) throws Exception {
		rmColumn(col);
    }
    
	public void deleteColumns(List<Column> list) throws Exception {
		for (Column col : list) {
			rmColumn(col);
		}
	}
	public void deleteColumns(Long[] colNos) throws Exception {
		/*for (Column col : list) {
			rmColumn(col);
		}*/
		this.columnMapper.deleteColumns(colNos);
	}

	public boolean checkUserPermission(Column search) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * get all columns count  
	 * @return
	 */
	public int getAllColumnsCount() throws Exception {
		Integer ret = this.columnMapper.getAllColumnsCount();
		return ret;
	}

	/**
	 * audit column
	 * @param col - need to specify col.curOper.
	 * @param nos - pk list
	 * @param status - the new status
	 * @throws Exception
	 */
	public void audit(Column col, Long[] nos, Integer status) throws Exception {
		if(col.getCurOper()==null) {
			throw new IllegalArgumentException("需要设置操作员");
		}
		
		ConstConfig.checkStatusValid(status);

		if(status==ConstConfig.STATUS_UNPUBLISH) {
			status = ConstConfig.STATUS_PASS;
		}
		columnMapper.audit(col, nos, status);
	}

	@Autowired
	PortalMapper portalMapper;
	
	public GetColumnRESP getColumnList(HttpServletRequest req, GetColumnREQT request) throws Exception {
		GetColumnRESP response = new GetColumnRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<ColumnInfo> columnInfoList = new ArrayList<ColumnInfo>();
		response.setColumnList(columnInfoList);
		
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
	    
	    if (StringUtils.isNotEmpty(request.getColumnNo())){
	    	Column column = columnMapper.selectByNo(Long.valueOf(request.getColumnNo()));
	    	if (column==null || column.getStatus() != AuditStatus.PUBLISH){
				response.setTotalCount(0);
				response.setTotalPage(0);
				response.setCurrentPage(1);
				return response;
	    	}
	    	
	    	ColumnInfo columnInfo = convertColumn(req, column);
	    	columnInfoList.add(columnInfo);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
		
		Column search = new Column();
		search.setStatus(ConstConfig.STATUS_PUBLISH);
		PageUtil page = search.getPageUtil();
		page.setPageInfo(request.getPageSize(), request.getStartPage());
		page.setPaging(true);
		
		List<Column> columnList = this.selectColumns(search);
		Iterator<Column> it = columnList.iterator();
		while (it.hasNext()){
			Column c = it.next();
	    	ColumnInfo columnInfo = convertColumn(req, c);
	    	columnInfoList.add(columnInfo);
		}

		response.setTotalCount(page.getRowCount());
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}	
	
	private ColumnInfo convertColumn(HttpServletRequest req,Column column) throws Exception{
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setColumnNo(column.columnNo);
		columnInfo.setColumnId(column.columnId);
		columnInfo.setShowOrder(column.showOrder);
		columnInfo.setParentColumnNo(column.getParentNo());
		columnInfo.setColumnTitle(column.getColumnName());
		columnInfo.setColumnDesc(column.getColumnDesc());
		columnInfo.setColumnCover(StringUtils.isEmpty(column.getCover()) ? "" : portalUrl + "/" + column.getCover());
		columnInfo.setTemplateNo(column.getTemplateId());
		columnInfo.setUpdateTime(column.updateTime);
		ResourcePublishMap publish = null;
		//List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(
		//		column.getParentNo() == null ? null: EntityType.TYPE_COLUMN,column.getParentNo(),EntityType.TYPE_COLUMN, column.getColumnNo());
		
		// 保存发布信息是没有保存parentColumn,因此无需匹配parentColumn
		List<ResourcePublishMap> list = resourcePublishMapMapper.findResourcePublishMapByResId(
				null, null, EntityType.TYPE_COLUMN,column.getColumnNo());
		if(list != null && !list.isEmpty()){
			publish = list.iterator().next();
		}
		
		/* 不处理策略继承,由portal处理
		Column parentColumn = column;
		Long parnetId = column.getParentNo();
		//如果版块没有绑定策略，则循环查找其父级板块的策略
		while(publish == null){
			if(parnetId == null || parnetId.longValue() == -1){
				break;
			}
			log.debug("column["+parentColumn.getColumnId()+"] NO bind any strategy,so find it's parent strategy["+parnetId+"]");
			parentColumn = columnMapper.selectByNo(parnetId);			
			//list = resourcePublishMapMapper.findResourcePublishMapByResId(
			//		parentColumn.getParentNo() == null ? null: EntityType.TYPE_COLUMN,parentColumn.getParentNo(), EntityType.TYPE_COLUMN, parentColumn.getColumnNo());
			list = resourcePublishMapMapper.findResourcePublishMapByResId(
					null, null, EntityType.TYPE_COLUMN, parentColumn.getColumnNo());
			if(list != null && !list.isEmpty()){
				publish = list.iterator().next();
			}
			parnetId = parentColumn.getParentNo();			
		}
		*/
		if(publish != null){
			String strategyNoStr = publish.getStrategyNo();
			if (StringUtils.isNotEmpty(strategyNoStr)){
				columnInfo.getStrategyArray().addAll(this.strategyService.getPublishStrategyByNos(strategyNoStr));
			}
		}		
		return columnInfo;
	}
	
	public List<Column> selectChildColumn(Column column) throws Exception {
		PageUtil page = column.getPageUtil();
		if (page.getPaging()) {
			int count = columnMapper.selectChildColumnCount(column);
			page.setRowCount(count);
		}
		
		List<Column> ret = columnMapper.selectChildColumn(column);
		
		return ret;
	}
	
	public Integer findArticlePublishColumnCount(Integer type, Long resourceid, Column search) throws Exception{
		return columnMapper.findArticlePublishColumnCount(type, resourceid, search);
	}
	
	public List<Column> findArticlePublishColumns(Integer type, Long resourceid, Column search) throws Exception{
		return columnMapper.findArticlePublishColumns(type, resourceid, search);
	}
	
	public Integer findResourceColumnNoPublishCount(Long[] resourceIds, Column column) throws Exception{
		return columnMapper.findResourceColumnNoPublishCount(resourceIds, column);
	}
	
	public List<Column> findResourceColumnNoPublishs(Long[] resourceIds, Column column) throws Exception{
		return columnMapper.findResourceColumnNoPublishs(resourceIds, column);
	}
	
	/**
	 * 版块策略
	 */
	public List<Column> findColumnWithStrategy(Column search) throws Exception{
		return columnMapper.findColumnWithStrategy(search);
	}
	
	public Integer findColumnWithStrategyCount(Column search) throws Exception{
		return columnMapper.findColumnWithStrategyCount(search);
	}
	
	public Integer getColumnCount(Column search) throws Exception{
		return this.columnMapper.selectColumnCount(search);
	}
	
    /**
     * 根据版块no,查询此版块的顶级版块和level
     * @param columnNo
     * @return column.parentNo and column.level
     * @throws Exception
     */
    public Column selectColumnTopestParentAndLevel(Long columnNo) throws Exception {
    	Column ret = this.columnMapper.selectColumnTopestParentAndLevel(columnNo);
    	return ret;
    }

    /**
     * 根据parent和level查询版块列表
     * @param columnNo 
     * @param level 
     * @return
     * @throws Exception
     */
    public List<Column> selectColumnByParentAndLevel(Long columnNo,	Integer level) throws Exception {
    	List<Column> ret = this.columnMapper.selectColumnByParentAndLevel(columnNo, level);
    	return ret;   	
    }

    /**
     * 查询一个版块可选的parent column
     * @param columnNo - column no. 
     * @return
     * @throws Exception
     */
    public List<Column> selectAvailableParentColumn(Long columnNo)  throws Exception {
    	Column c = this.selectColumnTopestParentAndLevel(columnNo);
    	if(c!=null) {
        	List<Column> ret = this.columnMapper.selectColumnByParentAndLevel(c.parentNo, c.level);
        	return ret;
    	}
    	return null;
	}

}
