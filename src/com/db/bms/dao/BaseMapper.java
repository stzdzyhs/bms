package com.db.bms.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/** 
 * <b>Application describing:</b>定义在这里是公用的 由Mapper配置文件实现 <br>
 * <b>Date:</b>2014-7-9<br>
 * @version $Revision$
 */
public interface BaseMapper<T> {
    /*****************CRUD操作********************/
    public Long getPrimaryKeyNo() throws Exception;

    public T selectByPrimaryKey(Object key) throws Exception;

    public List<T> get(Object[] key) throws Exception;

    public Integer updateByPrimaryKey(T t) throws Exception;

    public Integer deleteByPrimaryKey(Object key) throws Exception;

    public Integer insert(T t) throws Exception;

    public Integer insertBath(List<T> t) throws Exception;

    public Integer insertMtM(@Param(value = "otherKey")
    Object otherTableKey, @Param(value = "mainKey")
    Object primaryKey) throws SQLException;

    public Integer deleteByEntity(T entity) throws Exception;

    //public Integer deleteByMap()
    public List<T> selectBySql(@Param(value = "sql")
    String sql) throws Exception;

    public Integer updateBySql(@Param(value = "sql")
    String sql) throws Exception;

    public Integer deleteBySql(@Param(value = "sql")
    String sql) throws Exception;

    public Integer insertBySql(@Param(value = "sql")
    String sql) throws Exception;

    /***********************分页查询操作************************/
    public Integer selectByModelCount(T model) throws Exception;

    public List<T> selectByModel(T model) throws Exception;

    public Integer selectByMapPagingCount(Map<?, ?> map) throws java.sql.SQLException;

    public List<T> selectByMapPaging(Map<?, ?> map) throws java.sql.SQLException;

    /***********************查询不分页*************************/
    public List<T> selectByEntiry(T entity) throws Exception;

    public Integer selectByMapCount(Map<?, ?> map) throws Exception;

    public List<T> selectByMap(Map<?, ?> map) throws Exception;

    /************************** 图表 **************************/
    public List<Map<?, ?>> charts(Map<?, ?> map) throws SQLException;

    /**************************递归查询***********************/
    public List<T> selectByChild(T model) throws SQLException;
}
