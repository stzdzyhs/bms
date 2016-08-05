package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Column;


/*
 * param alias: col, NOT column !!! that is a oracle KEY.
 */
public interface ColumnMapper {

	/**
	 * check search.curOper, 是否有权限更改 column.columnNo 的板块
	 * @param search
	 * @return
	 */
	int queryOperatorAdminPermission(@Param(value="col")Column column);
	
	/**
	 * get next primary key
	 * @return
	 * @throws Exception
	 */
	Long getPrimaryKey() throws Exception;

	/**
	 * select by no(primary key)
	 * @param columnNo
	 * @return
	 * @throws Exception
	 */
	Column selectByNo(@Param(value = "no") Long columnNo) throws Exception;
	/**
	 * select by no(pk) join with table company
	 * @param columnNo
	 * @return
	 * @throws Exception
	 */
	Column selectByNoWithCompany(@Param(value = "no") Long columnNo) throws Exception;
	
	/**
	 * select by no.(pk) array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	List<Column> selectByNos(@Param(value = "nos")Long[] nos) throws Exception;

	/**
	 * get all columns count  
	 * @return
	 */
	int getAllColumnsCount();
	
	/**
	 * select columns. 需要设置 search.curOper 
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	List<Column> selectColumns(@Param(value="col")Column column) throws Exception;
	Integer selectColumnCount(@Param(value="col")Column column) throws Exception;
	
	
	/**
	 * select columns and exclude column.columnNo and its children columns
	 * column.curOper and column.columnNo must set!
	 * @param column
	 * @return
	 * @throws Exception
	 */
	List<Column> selectColumnsAndExclude(@Param(value="col")Column column) throws Exception;
	Integer selectColumnsAndExcludeCount(@Param(value="col")Column column) throws Exception;
	
	/**
	 * select new columns for article
	 * need to set: search.articleNo, search.curOper 
	 * @param article
	 * @return
	 * @throws Exception
	 */
	List<Column> selectNewColumnForArticle(@Param(value="col")Column column) throws Exception;
	Integer selectNewColumnForArticleCount(@Param(value="col")Column column) throws Exception;
	
	/**
	 * delete column. need to set col.curOper
	 * @param col
	 * @return - the deleted row count
	 * @throws Exception
	 */
	Integer deleteColumn(@Param(value="col")Column column) throws Exception;
	
	void deleteColumns(Long[] colNos) throws Exception;

	//Integer getColumnCountByIdOrName(@Param(value="column")Column search) throws Exception;
	
	Integer getSameIdCount(@Param(value="col")Column search) throws Exception;
	Integer getSameNameCount(@Param(value="col")Column search) throws Exception;
	
	/**
	 * get column refered count
	 * @param no -  the column no.
	 * @return -  refered count.
	 */
	Integer getColumnRefCount(@Param(value = "no")Long no);
	
	/**
	 * add a column
	 * @param column
	 * @return
	 * @throws Exception
	 */
	Integer addColumn(@Param(value="col")Column column) throws Exception;

	/**
	 * update a column
	 * @param column
	 * @return
	 * @throws Exception
	 */
	Integer updateColumn(@Param(value="col")Column column) throws Exception;
	
	/**
	 * update column cover, only cover.
	 * @param column
	 * @throws Exception
	 */
	Integer updateColumnCover(@Param(value="col")Column column) throws Exception;
	
	/**
	 * audit status
	 * @param curOper
	 * @param nos - column pk array
	 * @param status
	 * @throws Exception
	 */
	void audit(@Param(value="col")Column column, @Param(value = "nos")Long[] nos, @Param(value = "status")Integer status) throws Exception;
	//-----------------------------------------------------------------------
	
	List<Column> findColumnsWithSubByNo(List<Long> columnNos) throws Exception;

	List<Column> findColumnsByParentId(@Param(value = "parentId")Long parentId) throws Exception;
	
	Integer findColumnCountById(List<String> columnIds) throws Exception;
	
	Column findColumnById(String columnId) throws Exception;
	
	List<Column> columnSelectList(Column search) throws Exception;

	Integer columnSelectListCount(Column search) throws Exception;
	
	
	/*************Childcolumn***************************/
	List<Column> selectChildColumn(@Param(value="col")Column column) throws Exception;
	Integer selectChildColumnCount(@Param(value="col")Column column) throws Exception;

	List<Long> findAllColumnIdByArticleId(@Param(value = "status")Integer status, @Param(value = "articleNo")Long articleNo) throws Exception;
	
	Integer findArticlePublishColumnCount(@Param(value = "type")Integer type, @Param(value = "resourceid")Long resourceid, @Param(value = "search")Column search) throws Exception;
	
	List<Column> findArticlePublishColumns(@Param(value = "type")Integer type, @Param(value = "resourceid")Long resourceid, @Param(value = "search")Column search) throws Exception;
	
	Integer findResourceColumnNoPublishCount(@Param(value="resourceIds")Long[] resourceIds, @Param(value="col")Column column) throws Exception;
	
	List<Column> findResourceColumnNoPublishs(@Param(value="resourceIds")Long[] resourceIds, @Param(value="col")Column column) throws Exception;
	
	
	Integer getColumnCountByTemplateId(Long templateId) throws Exception;
	
	/**
	 * 版块策略
	 */
    List<Column> findColumnWithStrategy(@Param(value = "col")Column search) throws Exception;
	
	Integer findColumnWithStrategyCount(@Param(value = "col")Column search) throws Exception;
	
    /**
     * 根据parent和level查询版块列表
     * @param columnNo 
     * @param level 
     * @return
     * @throws Exception
     */
    public List<Column> selectColumnByParentAndLevel(@Param(value = "columnNo")Long columnNo, 
    		@Param(value = "level")Integer level) throws Exception;

    
    /**
     * 根据版块no,查询此版块的顶级版块和level
     * @param columnNo
     * @return
     * @throws Exception
     */
    public Column selectColumnTopestParentAndLevel(@Param(value = "columnNo")Long columnNo) throws Exception;
    
}
