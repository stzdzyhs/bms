package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Wordstock;

public interface WordstockMapper {
	
	/**
	 * get next primary key
	 * @return
	 * @throws Exception
	 */
	Long getPrimaryKey() throws Exception;

	/**
	 * select by no(primary key)
	 * @param wordNo
	 * @return
	 * @throws Exception
	 */
	Wordstock selectByNo(@Param(value = "no") Long no) throws Exception;

	/**
	 * select by no.(pk) array
	 * @param nos
	 * @return
	 * @throws Exception
	 */
	List<Wordstock> selectByNos(@Param(value = "nos")Long[] nos) throws Exception;

	/**
	 * get all words count
	 * @return
	 */
	int getAllWordstockCount();
	
	/**
	 * select wordstocks 
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	List<Wordstock> selectWordstock(@Param(value="wordstock")Wordstock wordstock) throws Exception;
	Integer selectWordstockCount(@Param(value="wordstock")Wordstock wordstock) throws Exception;
	
	
	/**
	 * add a wordstock
	 * @param Wordstock
	 * @return
	 * @throws Exception
	 */
	Integer addWordstock(@Param(value="wordstock")Wordstock wordstock) throws Exception;

	/**
	 * delete Wordstock. need to set wordstock.wordNo, and wordstock.curOper
	 * @param col
	 * @return - the deleted row count
	 * @throws Exception
	 */
	Integer deleteWordstock(@Param(value="wordstock")Wordstock wordstock) throws Exception;

	/**
	 * update a wordstock
	 * @param Wordstock
	 * @return
	 * @throws Exception
	 */
	Integer updateWordstock(@Param(value="wordstock")Wordstock wordstock) throws Exception;
	
	Integer isWordstockExit(@Param(value="wordstock")Wordstock wordstock) throws Exception;

	Integer getWordstockCountByIdOrName(@Param(value="wordstock")Wordstock wordstock) throws Exception;
	
	/**
	 * get Wordstock refered count
	 * @param no -  the Wordstock no.
	 * @return -  refered count.
	 */
	Integer getWordstockRefCount(@Param(value = "no")Long no);

	/**
	 * 获取全部生僻字
	 * @return
	 */
	List<String> getWordstocks();
}
