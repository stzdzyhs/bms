package test;

import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.service.PortalService;


public class TestPortalSerivce {
/*	static ClassPathXmlApplicationContext ctx;
	static PortalService portalService = null;
	
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
		portalService = ctx.getBean(PortalService.class);
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		System.out.println("tearDown");
		ctx.close();
    }
	//-----------------------------------------------------------------
	
	public Portal testAdd() throws Exception {
		System.out.println("test Add");
		
		Portal portal = new Portal();
		Date t = new Date();
		portal.setPortalId("portal" + t.getTime());
		portal.setPortalName("portal" + t.toString() );
		
		portal.setCurOper(SA);

		// add
		portalService.addPortal(portal);
		return portal;
	}

	public void testUpdate(Portal portal) throws Exception {
		portal.setPortalName(portal.getPortalName() + "   UUUUUUUUUUUU");

		portal.setCurOper(SA);
		portalService.updatePortal(portal);
	}
	
	public void testDelete() throws Exception {
		// add a new
		Portal portal = new Portal();
		Date t = new Date();
		portal.setPortalId("portal" + t.getTime());
		portal.setPortalName("portal" + t.toString() );
		
		portal.setCurOper(SA);
		
		portalService.addPortal(portal);
		
		portalService.deletePortal(portal);
	}
	
	@Test
	public void testRun() throws Exception {
		int c = portalService.getAllPortalCount();
		
		Portal col = testAdd();
		
		testUpdate(col);
		
		testDelete();
		
		
		col.getPageUtil().setPaging(true);
		
		Portal c2 = portalService.selectByNo(col.getPortalNo());
		Assert.assertTrue("c!=c2",col.equals(c2));
		
		List<Portal> cols = portalService.selectByNos(new Long[]{col.getPortalNo()});
		Assert.assertTrue("c!=c2", cols.size()==1 && cols.get(0).equals(col));
		
		col.getPageUtil().setPaging(false);
		col.setCurOper(SA);
		col.setPortalName("\\;'\"delete");
		cols = portalService.selectPortal(col);

		int d = portalService.getAllPortalCount();
		System.out.println("cnt: " + d+ c + "   " + d);
		
		boolean t = portalService.isPortalRepeateIdOrName(col);
		System.out.println("t: " + t);
	}
	*/
}
