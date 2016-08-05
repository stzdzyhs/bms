package test;

import java.util.List;

import org.junit.Test;

import com.db.bms.entity.EntityType;
import com.db.bms.entity.ResourcePublishMap;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.core.PageUtil;

public class TestResourcePublishMapService extends TestBase {
	
	public void testFindPublishedPicture() throws Exception {
		ResourcePublishMap rpm = new ResourcePublishMap();
		rpm.parentType = EntityType.TYPE_ALBUM;
		rpm.parentId = 1221L;
		PageUtil page = rpm.getPageUtil();
		page.setPageSize(2);
		page.setPageId(1);
		rpm.sortKey="createTime";
		rpm.sortType="desc";
		List<ResourcePublishMap> ret = resourcePublishMapService.findPublishedPicture(rpm);
		ArrayUtils.printList(ret,  "rpm");
	}

	
	@Test
	public void testRun() throws Exception {
		try {
			testFindPublishedPicture();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
