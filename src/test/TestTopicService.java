package test;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Topic;
import com.db.bms.sync.portal.protocal.GetTopicREQT;
import com.db.bms.sync.portal.protocal.GetTopicRESP;
import com.db.bms.utils.ArrayUtils;

public class TestTopicService extends TestBase {
	
	static Operator SA = new Operator();
	static {
		SA.setType(Operator.TYPE_SUPER_ADMIN);
		SA.setOperatorNo(-1L);
		SA.setCompanyNo(3L);
	}
	
	public void testSelectByNo() throws Exception {
		GetTopicREQT req = new GetTopicREQT();
		req.setSystemId("db");
		req.setTopicNo("861");
		GetTopicRESP resp = topicService.getTopicList(req);		
		System.out.println(JSON.toJSONString(resp));
	}
	
	public void testDeletes() throws Exception {
		// add a top column
	}
	
	void testQueryOperatorAdminPermission()  throws Exception {
	}
	
	void testFindTopicWithStrategy() throws Exception {
		Topic t = new Topic();
		t.getPageUtil().setPageSize(10000);
		t.setId(541L);
		List<Topic> list = topicService.findTopicWithStrategy(t);
		ArrayUtils.printList(list, "*** list");
	}
	
	@Test
	public void testRun() throws Exception {
//		testFindTopicWithStrategy();
		testSelectByNo();
	}
}
