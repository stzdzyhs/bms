package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Client;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Space;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.utils.ArrayUtils;

public class TestStrategyService extends TestBase {
	
	static Operator SA = new Operator();
	static {
		SA.setType(Operator.TYPE_SUPER_ADMIN);
		SA.setOperatorNo(-1L);
		SA.setCompanyNo(3L);
	}
	
	public void testDeletes() throws Exception {
		// add a top column
	}
	
	void testQueryOperatorAdminPermission()  throws Exception {
	}
	
	void testFindStrategyByIdWithStrategy() throws Exception {
		Strategy st = strategyService.findStrategyByNoWithCondition(2L);
		System.out.println("The Strategy: " + st);
		ArrayUtils.printList(st.conditionList,  "resource list:");
	}

	void testGetAllData() throws Exception {
		Strategy st = strategyService.getStrategyAllData(458L);
		System.out.println("****** " + st);
	}
	
	void testFindCardRegionNoSelect() throws Exception {
		CardRegion cr = new CardRegion();
		List<CardRegion> regionList = strategyService.findStrategyCardRegionNoSelect(2L, null, cr, null);
		regionList = strategyService.findStrategyCardRegionNoSelect(null, 161L, cr, null);
		System.out.println("****** " + regionList);
	}
	
	void testFindSpaceNoSelect() throws Exception {
		Space cr = new Space();
		List<Space> regionList = strategyService.findStrategySpaceNoSelect(2L, null, cr, null);
		regionList = strategyService.findStrategySpaceNoSelect(null, 161L, cr, null);
		System.out.println("****** " + regionList);
	}

	void testFindFeatureCodeNoSelect() throws Exception {
		FeatureCode cr = new FeatureCode();
		List<FeatureCode> regionList = strategyService.findStrategyFeatureCodeNoSelect(2L, null, cr, null);
		regionList = strategyService.findStrategyFeatureCodeNoSelect(null, 161L, cr, null);
		System.out.println("****** " + regionList);
	}
	
	void testFindClientNoSelect() throws Exception {
		Client cr = new Client();
		List<Client> regionList = strategyService.findStrategyClientNoSelect(2L, null, cr, null);
		regionList = strategyService.findStrategyClientNoSelect(null, 161L, cr, null);
		System.out.println("****** " + regionList);
	}
	
	void testSaveStrategy() throws Exception {
		Strategy s = new Strategy();
		s.strategyName = "aaaa";
		s.strategyId = "aaa";
	//	s.strategyForm = 0;
		s.setCurOper(SA);
		
		s.cardRegionList = new ArrayList<StrategyCondition>();
		StrategyCondition sc = new StrategyCondition();
		sc.conditionType = StrategyCondition.TYPE_CARD_REGION;
		sc.conditionId = 0L;
		s.cardRegionList.add(sc);
		strategyService.saveStrategy(s);
	}
	
	void testFindStrategys() throws Exception {
		Strategy s= new Strategy();
		List<Strategy> list = strategyService.findStrategys(s);
		ArrayUtils.printList(list, "st list");
	}

	@Test
	public void test1() throws Exception {
		try {
			testGetAllData();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
