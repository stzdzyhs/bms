package com.db.bms.service;

import com.db.bms.dao.BaseMapper;

/** 
 * <b>Application describing:</b>定义在这里由 BaseServiceImp来实现通用的 <br>
 * <b>Date:</b>2014-7-9<br>
 * @version $Revision$
 */
public interface BaseService<T> extends BaseMapper<T> {
    /**
     * {删除多条记录}
     * 
     * @param keys
     * @return
     * @throws Exception
     * @author: wanghaotao
     */
    public Integer deleteByPrimaryKeys(Object... keys) throws Exception;

}
