package com.db.bms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.db.bms.entity.Column;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.sync.portal.protocal.GetColumnREQT;
import com.db.bms.sync.portal.protocal.GetColumnRESP;

/**
 * 板块 column service
 */
public interface ColumnService {
	
	boolean queryOperatorAdminPermission(Column column) throws Exception;

	/**
	 * 根据 id 查找 column, 不检查operator.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Column selectByNo(Long id) throws Exception;

	/**
	 * select column with its company info
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Column selectByNoWithCompany(Long id) throws Exception;

	/**
	 * select company columns.
	 * must set search.curOper, optionally set search.columnNo,
	 * if set search.columnNo, it will exclude that column and its children columns.
	 * @param search
	 * @return
	 * @throws Exception
	 */
	List<Column> selectCompanyColumns(Column search) throws Exception;
	
	/**
	 * 根据 column No列表  查找 columns, 不检查operator.
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<Column> selectByNos(Long[] ids) throws Exception;

    
    /**
     * 根据search.curOper,返回operator可以使用/看到的column
     * @param search
     * @return
     * @throws Exception
     */
    List<Column> selectAllColumns(Column search) throws Exception;
    
    /**
     * 根据search.operator,返回operator可以使用/看到的column,
     * search.curOper 和分页信息需要设置
     * @param search -  
     * @return
     * @throws Exception
     */
	List<Column> selectColumns(Column search) throws Exception;

	/**
	 * select new columns for article
	 * need to set: column.articleNo, column.curOper 
	 * @param article
	 * @return
	 * @throws Exception
	 */
	List<Column> selectNewColumnForArticle(Column search) throws Exception;
	
	/**
	 * 判断板块id/name是否重复
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public boolean isIdUnique(Column search) throws Exception;
	public boolean isNameUnique(Column search) throws Exception;

    
	/**
	 * get column refered count
	 * @param no -  the column no.
	 * @return -  refered count.
	 */
    int getColumnRefCount(Long no);
    

    /**
     * 检查 search.curOper 是否有权限更新或删除 search.columnNo 指定的column
     * @param o
     * @param search
     * @return
     */
    boolean checkUserPermission(Column search);
    
    /**
     * 检查 column.curOper权限，增加 column
     * @param Column
     * @throws Exception
     */
	public void addColumn(Column col) throws Exception;


    /**
     * 更新column
     * @param Column
     * @throws Exception
     */
	public void updateColumn(Column col) throws Exception;

    /**
     * delete a column, col.curOper muset set
     * @throws Exception
     */
    void deleteColumn(Column col) throws Exception;
    
    /**
     * 删除 column list. 如果出错, 抛出异常
     * @param list
     * @param list
     * @return
     * @throws Exception
     */
    void deleteColumns(List<Column> list) throws Exception;
    void deleteColumns(Long[] colNos) throws Exception;
	/**
	 * get all columns count  
	 * @return
	 */
	int getAllColumnsCount() throws Exception;

	/**
	 * 操作员更新板块状态
	 * @param op
	 * @param nos - column pk array
	 * @param status
	 * @throws Exception
	 */
	void audit(Column column, Long[] nos, Integer status, ResourcePublishMap publish) throws Exception;
	
	
	public GetColumnRESP getColumnList(HttpServletRequest req, GetColumnREQT request) throws Exception;
	
	
	public List<Column> selectChildColumns(Column column) throws Exception;
	
    List<Column> findArticlePublishColumns(Integer type, Long resourceid, Column search) throws Exception;

	List<Column> findResourceColumnNoPublishs(Long resourceId, Column column) throws Exception;
	
	List<Column> findAllResourceColumnNoPublishs(Long[] resourceIds, Column column) throws Exception;
	
	boolean isCotainChildCol(Long columnNo) throws Exception;
	
	/**
	 * 版块策略
	 */
	List<Column> findColumnWithStrategy(Column column) throws Exception;
	
	Integer getColumnCountByCompanyNo(Long companyNo)throws Exception;
	
    /**
     * 根据版块no,查询此版块的顶级版块和level
     * @param columnNo
     * @return column.parentNo and column.level
     * @throws Exception
     */
    public Column selectColumnTopestParentAndLevel(Long columnNo) throws Exception;
    
    /**
     * 根据parent和level查询版块列表
     * @param columnNo 
     * @param level 
     * @return
     * @throws Exception
     */
    public List<Column> selectColumnByParentAndLevel(Long columnNo,	Integer level) throws Exception;
    
    /**
     * 查询一个版块可选的parent column
     * @param columnNo - column no. 
     * @return
     * @throws Exception
     */
    public List<Column> selectAvailableParentColumn(Long columnNo)  throws Exception;

}
