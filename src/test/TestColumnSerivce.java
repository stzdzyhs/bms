package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.db.bms.entity.AuditStatus;
import com.db.bms.entity.Column;
import com.db.bms.entity.Operator;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.service.ColumnService;
import com.db.bms.sync.portal.protocal.GetColumnREQT;
import com.db.bms.sync.portal.protocal.GetColumnRESP;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.ConstConfig;

public class TestColumnSerivce {
	static ClassPathXmlApplicationContext ctx;
	static ColumnService columnService = null;
	
	static Operator SA = new Operator();
	static {
		SA.setType(Operator.TYPE_SUPER_ADMIN);
		SA.setOperatorNo(-1L);
		SA.setCompanyNo(3L);
	}
	
	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setup");
		String[] cp = new String[]{"spring-mvc.xml", "spring-mybatis.xml", "spring-portal.xml"};
		ctx = new ClassPathXmlApplicationContext(cp);
		columnService = ctx.getBean(ColumnService.class);
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		System.out.println("tearDown");
		ctx.close();
    }
	//-----------------------------------------------------------------
	
	public Column testAdd() throws Exception {
		System.out.println("test Add");
		
		Column col = new Column();
		col.setParentNo(null);
		col.setColumnId("column 1");
		col.setColumnName("col 1 name");
		col.setColumnDesc("col 1 desc");
		col.setCurOper(SA);
		//col.setCreateTime(DateUtil.getCurrentTime());
		
		// add
		columnService.addColumn(col);
		
		//System.out.println("column no: " + col.getColumnNo());
		return col;
	}

	public void testUpdate(Column col) throws Exception {
		col.setColumnId("column 1");
		col.setColumnName("col 1 name UUUUUUUUUUUUU");
		col.setColumnDesc("col 1 desc UUUUUUUUUUUUU");
		col.setCurOper(SA);
		columnService.updateColumn(col);
		
		System.out.println("test 222");
	}
	
	public void delete(Column c)  throws Exception {
		columnService.deleteColumn(c);
	}
	
	public void testSelectByNo(Column c) throws Exception {
	}
	
	public void testDeletes() throws Exception {
		// add a top column
		Column col = new Column();
		col.setParentNo(null);
		col.setColumnId("p0 ");
		col.setColumnName("p0 name");
		col.setColumnDesc("p0 desc");
		col.setCurOper(SA);
		columnService.addColumn(col);
		
		// add a child
		col.setParentNo(col.getColumnNo());
		col.setColumnNo(null);
		col.setColumnId("p1 ");
		col.setColumnName("p1 name");
		col.setColumnDesc("p1 desc");
		columnService.addColumn(col);
		
		
		int cnt1 = columnService.getAllColumnsCount();
		
		Column c2 = new Column();
		c2.setCurOper(SA);
		c2.setColumnNo(col.getParentNo());

		boolean error = false;
		try {
			columnService.deleteColumn(c2);
			error = true;
		}
		catch(Exception e) {
			// expected
		}
		
		List<Column> list = new ArrayList<Column>();
		list.add(c2);
		
		error = false;
		try {
			columnService.deleteColumns(list);
			error = true;
		}
		catch(Exception e) {
			// expected
		}
		
		int cnt2 = columnService.getAllColumnsCount();
		
		Assert.assertFalse("delete ???", error);
		
		Assert.assertTrue("c!=c2", cnt1==cnt2);
	}
	
	void testQueryOperatorAdminPermission()  throws Exception {
		Column c= new Column();
		c.setCurOper(SA);
		c.setColumnNo(1L);
		columnService.queryOperatorAdminPermission(c);
	}
	
	void testGetColumnList() throws Exception {
		GetColumnREQT req = new GetColumnREQT();
		req.setStartPage(1);
		req.setPageSize(1);
		req.setSystemId("db");
		
		req.setStartPage(13);
		GetColumnRESP resp = columnService.getColumnList(null, req);
		System.out.println("-------- " + resp.toString());
		
	}
	
	void testFindAllResourceColumnNoPublishs() throws Exception {
		Column c= new Column();
		c.columnId="c1";
		c.columnName="cc";
		List<Column> list = columnService.findAllResourceColumnNoPublishs(null, c);
		ArrayUtils.printList(list,"column list");
	}
	
	void testSelectColumnTopestParentAndLevel() throws Exception {
		Column ret = columnService.selectColumnTopestParentAndLevel(2333L);
		System.out.println(ret.parentNo);
		System.out.println(ret.level);
	}
	
	void testSelectAvailableParentColumn() throws Exception {
		List<Column> list = columnService.selectAvailableParentColumn(2322L);
		ArrayUtils.printList(list, "parent list");
	}
	
	void testAudit()throws Exception {
		ResourcePublishMap publish = new ResourcePublishMap();
		Long[] ids = new Long[]{2421L, 2420L, 2422L};
		
		List<Column> list = columnService.selectByNos(ids);
		Column c = list.get(0);
		c.setCurOper(SA);
		//c.setCurOper(curOper);
		columnService.audit(c, ids, AuditStatus.UNPUBLISH, publish);
	}
	
	@Test
	public void testRun() throws Exception {
		try {
			testAudit();
			//testAdd();
			//testSelectAvailableParentColumn();
			//testSelectColumnTopestParentAndLevel();
			//testFindAllResourceColumnNoPublishs();
			if("".length()<1) {
				return;
			}
			
			int c = columnService.getAllColumnsCount();
			
			Column col = testAdd();
			
			col.getPageUtil().setPaging(true);
			Column c2 = columnService.selectByNo(col.getColumnNo());
			boolean t = col.equals(c2);
			Assert.assertTrue("c!=c2", t);
			
			List<Column> cols = columnService.selectByNos(new Long[]{col.getColumnNo()});
			Assert.assertTrue("c!=c2", cols.size()==1 && cols.get(0).equals(col));
			
			col.setCurOper(SA);
			col.setColumnName("aaaa");
			cols = columnService.selectColumns(col);
			
			testUpdate(col);
			delete(col);
			
			int d = columnService.getAllColumnsCount();
			Assert.assertTrue("c==d", c==d);
			
			String s = "{\"columnDesc\":\"CCCCCCCCC\",\"columnId\":\"aaaaa\",\"columnName\":\"aaaaa\",\"columnNo\":173,\"companyNo\":-1,\"createTime\":\"2015-07-14 10:57:38\",\"curOper\":{\"companyNo\":-1,\"createBy\":-1,\"createTime\":\"2015-06-30 11:50:59\",\"isSelf\":1,\"operator\":{\"companyNo\":-1,\"createBy\":-1,\"createTime\":\"2015-06-30 11:50:59\",\"isSelf\":1,\"operator\":{\"$ref\":\"@\"},\"operatorDescribe\":\"系统超级管理员\",\"operatorEmail\":\"admin@db.cn\",\"operatorId\":\"admin\",\"operatorName\":\"超级管理员\",\"operatorNo\":-1,\"operatorPwd\":\"OolhXxd7LPY=\",\"operatorTel\":\"88888888\",\"pageUtil\":{\"allConditionAndLimit\":\" limit 0,10\",\"andCondition\":\"\",\"hasNextPage\":false,\"hasPreviousPage\":false,\"lastRowCount\":10,\"like\":false,\"limit\":\" limit 0,10\",\"orderByCondition\":\"\",\"pageCount\":0,\"pageId\":1,\"pageList\":[],\"pageOffset\":0,\"pageSize\":10,\"pageTail\":0,\"paging\":false,\"rowCount\":0},\"roles\":[],\"status\":0,\"type\":0},\"operatorDescribe\":\"系统超级管理员\",\"operatorEmail\":\"admin@db.cn\",\"operatorId\":\"admin\",\"operatorName\":\"超级管理员\",\"operatorNo\":-1,\"operatorPwd\":\"OolhXxd7LPY=\",\"operatorTel\":\"88888888\",\"pageUtil\":{\"allConditionAndLimit\":\" limit 0,10\",\"andCondition\":\"\",\"hasNextPage\":false,\"hasPreviousPage\":false,\"lastRowCount\":10,\"like\":false,\"limit\":\" limit 0,10\",\"orderByCondition\":\"\",\"pageCount\":0,\"pageId\":1,\"pageList\":[],\"pageOffset\":0,\"pageSize\":10,\"pageTail\":0,\"paging\":false,\"rowCount\":0},\"roles\":[],\"status\":0,\"type\":0},\"isSelf\":1,\"operatorNo\":-1,\"pageUtil\":{\"allConditionAndLimit\":\" limit 0,10\",\"andCondition\":\"\",\"hasNextPage\":false,\"hasPreviousPage\":false,\"lastRowCount\":10,\"like\":false,\"limit\":\" limit 0,10\",\"orderByCondition\":\"\",\"pageCount\":0,\"pageId\":1,\"pageList\":[],\"pageOffset\":0,\"pageSize\":10,\"pageTail\":0,\"paging\":false,\"rowCount\":0},\"parentColumnName\":\"顶级板块\"}";
			col = Column.fromString(s);
			col.setColumnNo(null);
			col.setCurOper(SA);
			columnService.addColumn(col);
			
			testDeletes();
			
			testQueryOperatorAdminPermission();
			
			col.setArticleNo(300L);
			col.getPageUtil().setPaging(false);
			columnService.selectNewColumnForArticle(col);
			
			columnService.selectByNoWithCompany(1L);
			
			columnService.audit(col,  new Long[]{333L}, ConstConfig.STATUS_EDIT, null);

			testGetColumnList();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
}
