package test;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.db.bms.entity.Operator;
import com.db.bms.entity.Wordstock;
import com.db.bms.service.WordstockService;

public class TestWordstockSerivce {
	static ClassPathXmlApplicationContext ctx;
	static WordstockService wordstockService = null;
	
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
		wordstockService = ctx.getBean(WordstockService.class);
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		System.out.println("tearDown");
		ctx.close();
    }
	//-----------------------------------------------------------------
	
	public Wordstock testAdd() throws Exception {
		System.out.println("test Add");
		
		Wordstock col = new Wordstock();
		col.setWord("鱻");
		col.setCurOper(SA);
		
		// add
		wordstockService.addWordstock(col);
		return col;
	}

	public void testUpdate(Wordstock col) throws Exception {
		col.setWord("羴");
		col.setCurOper(SA);
		wordstockService.updateWordstock(col);
		
		System.out.println("test 222");
	}
	
	public void testDelete() throws Exception {
		// add a wordstock
		Wordstock rec = new Wordstock();
		rec.setWord("鱻");
		rec.setCurOper(SA);
		wordstockService.addWordstock(rec);
		
		wordstockService.deleteWordstock(rec);
	}
	
	@Test
	public void testRun() throws Exception {
		int c = wordstockService.getAllWordstocksCount();
		
		Wordstock col = testAdd();
		
		testUpdate(col);
		
		testDelete();
		
		
		col.getPageUtil().setPaging(true);
		
		Wordstock c2 = wordstockService.selectByNo(col.getWordNo());
		Assert.assertTrue("c!=c2",col.equals(c2));
		
		List<Wordstock> cols = wordstockService.selectByNos(new Long[]{col.getWordNo()});
		Assert.assertTrue("c!=c2", cols.size()==1 && cols.get(0).equals(col));
		
		col.getPageUtil().setPaging(false);
		col.setCurOper(SA);
		col.setWord("\\;delete");
		cols = wordstockService.selectWordstock(col);

		int d = wordstockService.getAllWordstocksCount();
		System.out.println("cnt: " + d+ c + "   " + d);
		
		boolean t = wordstockService.isWordstockRepeateIdOrName(col);
		System.out.println("t: " + t);
	}
}
