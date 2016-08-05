package test;

import java.util.List;

import org.junit.Test;

import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Operator;

public class TestFeatureCodeService extends TestBase {
	
	static Operator SA = new Operator();
	static {
		SA.setType(Operator.TYPE_SUPER_ADMIN);
		SA.setOperatorNo(-1L);
		SA.setCompanyNo(3L);
	}
	
	public void testDeletes() throws Exception {
		// add a top column
	}
	
	
	void testGetFeatureCode() throws Exception {
		FeatureCode fc  = new FeatureCode();
		List<FeatureCode> regionList = null;//featureCodeService.getFeatureCodeList(fc);
		System.out.println("****** " + regionList);
	}
	
	@Test
	public void testRun() throws Exception {
		try {
			testGetFeatureCode();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
