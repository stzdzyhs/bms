package com.db.bms.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.db.bms.dao.WordstockMapper;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Wordstock;
import com.db.bms.service.WordstockService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.core.PageUtil;

/**
 * <b>功能：</b>用于事物处理<br>
 */

@Service("WordstockService")
public class WordstockServiceImpl implements WordstockService {
	
	//上传生僻字时候的临时存放路径
	@Value("${wordstock.dir}")
	private String wordstockDir;

	static File wordstockDirFile = null;

	public File getWordstockDirFile() throws Exception {
		if(wordstockDirFile==null) {
			wordstockDirFile = new File(wordstockDir);
			if(!wordstockDirFile.exists()) {
				wordstockDirFile.mkdirs();
			}
		}
		return wordstockDirFile;
	}
	
	public String getFilePath(String filename) {
		String ret= this.wordstockDir + filename;
		return ret;
	}
	
	public static List<String> wordstocks = new ArrayList<String>();
	
	public void init() {
		wordstocks = wordstockMapper.getWordstocks();
	}

	@Autowired
	private WordstockMapper wordstockMapper;
	

	@Override
	public Wordstock selectByNo(Long id) throws Exception {
		return this.wordstockMapper.selectByNo(id);
	}

	@Override
	public List<Wordstock> selectByNos(Long[] ids) throws Exception {
 		return this.wordstockMapper.selectByNos(ids);

	}
	//------------------------------------------------------------------------
	
	@Override
	public List<Wordstock> selectWordstock(Wordstock wordstock) throws Exception {
		//更新内存中的生僻字
		this.init();
		PageUtil page = wordstock.getPageUtil();
		if (page.getPaging()) {
			int count = wordstockMapper.selectWordstockCount(wordstock);
			page.setRowCount(count);
		}
		
		List<Wordstock> ret = wordstockMapper.selectWordstock(wordstock);
		return ret;
	}
	//------------------------------------------------------------------------

	@Override
	public boolean isWordstockRepeateIdOrName(Wordstock wordstock) throws Exception {
		// no need to check permission   
		int count = this.wordstockMapper.getWordstockCountByIdOrName(wordstock);
		return count > 0 ? true : false;
	}

	@Override
    public int getWordstockRefCount(Long no) {
		int count = this.wordstockMapper.getWordstockRefCount(no);
		return count;
    }

	/**
     * 检查 wordstock.curOper 是否有权限更新或删除 wordstock.wordNo 指定的Wordstock
     * @param o
     * @param wordstock
     * @return
     */
	private void checkWritePermission(Wordstock wordstock) throws Exception {
		Operator op = wordstock.getCurOper();
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
			throw new Exception("生僻字权限错误");
		}
		else if (type.equals(Operator.TYPE_ORDINARY_OPER)) {
			throw new Exception("生僻字权限错误");
		}
		else {
			throw new Exception("非法的operator");
		}
	}
	
	@Override
	public void addWordstock(Wordstock wordstock) throws Exception {
		//checkWritePermission(wordstock);		
		wordstock.setWordNo(this.wordstockMapper.getPrimaryKey());
		wordstock.setOperatorNo(wordstock.getCurOper().getOperatorNo());
		wordstock.setCreateTime(DateUtil.getCurrentTime());
		
		int cnt = this.wordstockMapper.addWordstock(wordstock);
		if(cnt!=1) {
			throw new Exception("增加失败");
		}
	}

	@Override
	public void updateWordstock(Wordstock wordstock) throws Exception {
		//checkWritePermission(wordstock);
		
		int cnt = this.wordstockMapper.updateWordstock(wordstock);
		if(cnt!=1) {
			throw new Exception("更新失败");
		}
	}
	
	@Override
	public boolean checkForWrite(Wordstock wordstock) throws Exception {
		boolean result = false;
		Integer count = this.wordstockMapper.isWordstockExit(wordstock);
		if (count > 0) {  //有管理权限
			result = true;
		}
		return result;
	}
	
	
	private void rmWordstock(Wordstock wordstock) throws Exception {
	/*	//checkWritePermission(wordstock);
		
		int cnt = this.wordstockMapper.getWordstockRefCount(wordstock.getWordNo());
		if(cnt>0) {
			String err = String.format("该数据:%s 被引用", wordstock.getWord());
			throw new Exception(err);
		}
		
		// wordstockMapper.deketeWordstock() will check permission 
		cnt = this.wordstockMapper.deleteWordstock(wordstock);
		if(cnt!=1) {
			throw new Exception("板块数据错误");
		}*/
	}
	
	@Override
    public void deleteWordstock(Wordstock wordstock) throws Exception {
		this.wordstockMapper.deleteWordstock(wordstock);
    }
    
	@Override
	public void deleteWordstocks(List<Wordstock> list) throws Exception {
		for (Wordstock col : list) {
			rmWordstock(col);
		}
	}

	/**
	 * get all wordstock count  
	 * @return
	 */
	@Override
	public int getAllWordstocksCount() throws Exception {
		Integer ret = this.wordstockMapper.getAllWordstockCount();
		return ret;
	}


	//*-----------------------------------------------------------------------
	// getter and setter
	//*-----------------------------------------------------------------------
	
	public WordstockMapper getWordstockMapper() {
		return wordstockMapper;
	}

	public void setWordstockMapper(WordstockMapper wordstockMapper) {
		this.wordstockMapper = wordstockMapper;
	}

	@Override
	public Integer uploadWordstocks(File file, Wordstock wordstock) throws Exception {
		//RandomAccessFile raf = new RandomAccessFile(file, "r");
		InputStream is = new FileInputStream(file);
		BufferedReader raf = new BufferedReader(new InputStreamReader(is, ConstConfig.DEF_ENCODING)); 
		String line = raf.readLine();
		String rex = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$"; //仅中文
		Integer totalCount = 0;
		boolean flag = true;  //正则检测
		while (line != null && flag) {
			// remove BOM special char
			if (line.length()>0 && (line.charAt(0)=='\ufffe' ||line.charAt(0)=='\ufeff' )) {
				line = line.substring(1);
			}
			for(int i=0; i < line.length() && flag; i++) {  //正则检验
				char c = line.charAt(i);
				
				if(!(""+c).matches(rex)) { //生僻字不满足正则
					flag = false;
					totalCount = -1;
				} else if(Character.isWhitespace(c)) {
					continue;
				} else {
					wordstock.setWord(""+c);
					if(this.isWordstockRepeateIdOrName(wordstock)) {
						continue;
					}
					//插入
					this.addWordstock(wordstock);
					totalCount++;
				}
			}
			line = raf.readLine();
		}
		raf.close();		
		return totalCount; //增加成功
	}
	/**
	 * 获取全部生僻字
	 * @return
	 */
	public List<String> findWordstocks() throws Exception{
		return this.wordstockMapper.getWordstocks();
	}
	
}
