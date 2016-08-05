package test;

import org.junit.Test;

import com.db.bms.entity.ResourceAllocation;

public class TestResourceAllocation extends TestBase {
	
	public void testFindResAllocByOperatorNoAndType() throws Exception {
		ResourceAllocation ra = new ResourceAllocation();
		int i=0;
		try {
			for(i=1;i<=5;i++) {
				//ra.type = i; //ResourceAllocation.TYPE_ALBUM;
				//ra.operatorNo = 110L;
				//List<ResourceAllocation> list = resourceAllocationService.findResAllocByOperatorNoAndType(ra);
				//ArrayUtils.printList(list, "ra:");
			}
		}
		catch(Exception e) {
			System.err.println("******* " + i);
			throw e;
		}
	}

	
	@Test
	public void testRun() throws Exception {
		try {
			testFindResAllocByOperatorNoAndType();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
