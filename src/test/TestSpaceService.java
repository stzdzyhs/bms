package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.db.bms.entity.Operator;
import com.db.bms.entity.Space;

public class TestSpaceService extends TestBase {

	public void testCheckPermission() throws Exception {
		Operator operator = new Operator();
		
		operator.type = Operator.TYPE_ORDINARY_OPER;
		operator.operatorNo = 782L;
		
		List<Space> list = new ArrayList<Space>();
		Space s = new Space();
		s.spaceNo = 1001L;
		s.createBy = 782L;
		list.add(s);
		
		spaceService.checkWritePermission(operator, list);
		
		operator.type = Operator.TYPE_COMPANY_ADMIN;
		operator.operatorNo = 781L;
		
		spaceService.checkWritePermission(operator, list);
	}
	
	@Test
	public void testRun() throws Exception {
		try {
			testCheckPermission();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
