package com.db.bms.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.ColumnArticleMapMapper;
import com.db.bms.entity.Article;
import com.db.bms.entity.Column;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.service.ColumnArticleMapService;
import com.db.bms.utils.core.PageUtil;

/**
 * column-article map service
 */
@Service("columnArticleMapService")
public class ColumnArticleMapServiceImpl implements ColumnArticleMapService {
	
	@Autowired
	ColumnArticleMapMapper columnArticleMapMapper;
	
	/*
	 * ensure permission
	 * @param article
	private void checkPermission(Article article) throws Exception {
		Operator op = article.getCurOper();
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
			if( op.getCompanyNo().equals(article.getCompanyNo())) {
				throw new Exception("权限错误");
			}
		}
		else if (type.equals(Operator.TYPE_ORDINARY_OPER)) {
			throw new Exception("权限错误");
		}
		else {
			throw new Exception("非法的operator");
		}
	}
	 */

	
	@Override
	public List<ColumnArticleMap> selectByNos(@Param(value = "nos")Long[] nos) throws Exception {
		List<ColumnArticleMap> ret = columnArticleMapMapper.selectByNos(nos);
		return ret;
	}

	@Override
	public List<ColumnArticleMap> selectArticleColumn(Column search) throws Exception {
		PageUtil page = search.getPageUtil();
		if (page.getPaging()) {
			int count = columnArticleMapMapper.selectArticleColumnCount(search);
			page.setRowCount(count);
		}
		List<ColumnArticleMap> ret = columnArticleMapMapper.selectArticleColumn(search);
		return ret;
	}

	@Override
	public void addColumnArticleMap(ColumnArticleMap map) throws Exception {
		// TODO: check permission
		
		map.setColumnArticleNo(columnArticleMapMapper.getPrimaryKey());
		int rc = columnArticleMapMapper.addColumnArticleMap(map);
		if(rc!=1) {
			throw new Exception("增加错误");
		}
	}
	@Override
	public void addColumnArticleMap(Long articleNo, Long[] columnNos,Long createdBy) throws Exception {
		// TODO: check permission
		ColumnArticleMap map = new ColumnArticleMap();
		for(int i=0;i<columnNos.length;i++) {
			map.setColumnArticleNo(columnArticleMapMapper.getPrimaryKey());
			map.setArticleNo(articleNo);
			map.setColumnNo(columnNos[i]);
			map.setCreatedBy(createdBy);
			int rc = columnArticleMapMapper.addColumnArticleMap(map);
			if(rc!=1) {
				throw new Exception("增加错误");
			}
		}		
	}
	

	private void rmColumnArticleMap(ColumnArticleMap m) throws Exception {
		// TODO: check permission
		Integer rc = columnArticleMapMapper.deleteColumnArticleMap(m);
		if(rc!=1) {
			throw new Exception("删除错误");
		}
	}
	
	@Override
	public void deleteColumnArticleMap(ColumnArticleMap columnArticleMap) throws Exception {
		rmColumnArticleMap(columnArticleMap);
	}
	
	@Override
	public void deleteColumnArticleMaps(List<ColumnArticleMap> list) throws Exception {
		for (ColumnArticleMap m: list) {
			rmColumnArticleMap(m);
		}
	}
	
	@Override
	public void deleteArticleMap(Long articleNo) throws Exception {
		this.columnArticleMapMapper.deleteArticleMap(articleNo);
	}

	@Override
	public List<ColumnArticleMap> selectColumnArticles(Article article) throws Exception {
		
		PageUtil page = article.getPageUtil();
		if (page.getPaging()) {
			int count = columnArticleMapMapper.selectColumnArticlesCount(article);
			page.setRowCount(count);
		}
		
		List<ColumnArticleMap> ret = columnArticleMapMapper.selectColumnArticles(article);
		
		return ret;
		
	}

	@Override
	public void addArticlesToColumn(Long columnNo, Long[] articleNos,Long createdBy)
			throws Exception {
		//TODO:check permission
		ColumnArticleMap map = new ColumnArticleMap();
		for(int i=0;i<articleNos.length;i++) {
			map.setColumnArticleNo(columnArticleMapMapper.getPrimaryKey());
			map.setArticleNo(articleNos[i]);
			map.setColumnNo(columnNo);
			map.setCreatedBy(createdBy);
			int rc = columnArticleMapMapper.addColumnArticleMap(map);
			if(rc!=1) {
				throw new Exception("增加错误");
			}
		}		
		
	}
	
}
