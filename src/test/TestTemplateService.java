/*package test;

import org.junit.Test;

import com.db.bms.entity.Template;

public class TestTemplateService extends TestBase {
	
	public Template testAdd() throws Exception {
		
		System.out.println("test Add");
		
		Template temp = new Template();
		temp.setTemplateId("Template 1");
		temp.setTemplateName("col 1 name");
		temp.setTemplateDesc("temp1 desc");
		temp.setCurOper(SA);
			
		templateService.addTemplate(temp);
		
		return temp;
	}

	public void testUpdate(Template pic) throws Exception {
		pic.setTemplateDesc("new desc");
		pic.setCurOper(SA);
		templateService.updateTemplateBasicInfo(pic);
		
		System.out.println("test 222");
	}
	
	public void delete(Template c)  throws Exception {
		templateService.deleteTemplate(c);
	}
	
	public void testSelectByNo(Template c) throws Exception {
		templateService.selectByNo(1L);
	}
	
	public void testDeletes() throws Exception {
	}

	@Test
	public void testRun() throws Exception {
		Template temp = testAdd();
		System.out.println("temp id:" + temp.getTemplateNo());

		testSelectByNo(temp);
		templateService.selectByNos(new Long[]{1L,2L});
		
		testUpdate(temp);
		
		delete(temp);		
	}
}
*/