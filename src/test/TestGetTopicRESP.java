package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.db.bms.entity.CardRegion;
import com.db.bms.sync.portal.protocal.GetTopicRESP;
import com.db.bms.sync.portal.protocal.PublishStrategy;
import com.db.bms.sync.portal.protocal.TopicInfo;

public class TestGetTopicRESP {

	
	@Test
	public void testS1() {
		GetTopicRESP resp = new GetTopicRESP();
		resp.resultCode="000";
		resp.resultDesc="good";

		List<TopicInfo> topicInfoList = new ArrayList<TopicInfo>();
		
		CardRegion cr = new CardRegion();
		cr.id = 1L;
		cr.regionName = "1";
		cr.regionSectionBegin = "1";
		cr.regionSectionEnd = "1000";

		PublishStrategy s = new PublishStrategy();
		s.regionCode = new ArrayList<CardRegion>();
		s.regionCode.add(cr);
		
		
		TopicInfo ti = new TopicInfo();
		ti.strategyArray = new ArrayList<PublishStrategy>();
		ti.strategyArray.add(s);
		
		topicInfoList.add(ti);
		
		resp.setTopicList(topicInfoList);
		
		String str = resp.build();
		System.out.println(str);
	}
	
	
}
