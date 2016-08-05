package com.db.bms.service;

import java.io.File;
import java.util.List;

import com.db.bms.entity.Wordstock;

/**
 * wordstock service
 */
public interface WordstockService {
	
	/**
	 * 根据 id 查找 Wordstock, 不检查operator.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Wordstock selectByNo(Long id) throws Exception;
	
	/**
	 * 根据 Wordstock No列表  查找 Wordstocks, 不检查operator.
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<Wordstock> selectByNos(Long[] ids) throws Exception;

    
    
    /**
     * 根据wordstock,返回Wordstock,
     * 分页信息需要设置
     * @param wordstock -  
     * @return
     * @throws Exception
     */
	List<Wordstock> selectWordstock(Wordstock wordstock) throws Exception;

	
	/**
	 * 判断板块id/name是否重复
	 * @param wordstock
	 * @return
	 * @throws Exception
	 */
    boolean isWordstockRepeateIdOrName(Wordstock wordstock) throws Exception;
    
	/**
	 * get Wordstock refered count
	 * @param no -  the Wordstock no.
	 * @return -  refered count.
	 */
    int getWordstockRefCount(Long no);
    

    
    /**
     * 检查 wordstock.curOper权限，增加Wordstock
     * @param Wordstock
     * @throws Exception
     */
	public void addWordstock(Wordstock wordstock) throws Exception;
	
    /**
     * 检查 wordstock.curOper权限，增加Wordstock
     * @param Wordstock
     * @throws Exception
     */
	public void updateWordstock(Wordstock wordstock) throws Exception;
	
	public boolean checkForWrite(Wordstock wordstock) throws Exception;
	
    /**
     * delete a wordstock, wordstock.curOper and wordstock.wordNo muset set
     * @throws Exception
     */
    void deleteWordstock(Wordstock wordstock) throws Exception;
    
    /**
     * 删除 Wordstock list. 如果出错, 抛出异常
     * @param list
     * @param list
     * @return
     * @throws Exception
     */
	public void deleteWordstocks(List<Wordstock> list) throws Exception;
    
	/**
	 * get all Wordstocks count
	 * @return
	 */
	int getAllWordstocksCount() throws Exception;
	
	Integer uploadWordstocks(File file,Wordstock wordstock) throws Exception;

	List<String> findWordstocks () throws Exception;
	
	//上传文件保存路径
	String getFilePath(String filename) throws Exception;
	
	File getWordstockDirFile() throws Exception;
}
