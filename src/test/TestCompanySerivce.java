package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.db.bms.entity.Company;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.core.PageUtil;

public class TestCompanySerivce extends TestBase {
	
	public Company testAdd() throws Exception {
		System.out.println("test Add");
		
		Company company1 = new Company();
		company1.setCompanyId("comp1");
		company1.setCompanyName("company1 name");
		company1.setCompanyDescribe("company1 desc");
		company1.setCreateBy(-1L);
		company1.setParentId(-1L);

		company1.setCurOper(SA);
		// add
		companyService.saveOrUpdate(company1);
		System.out.println("Company no: " + company1.getCompanyNo());
		Assert.assertTrue("t1", company1.getCompanyNo()!=null);
		return company1;
	}

	public void testUpdate(Company col) throws Exception {
		col.setCompanyId("Company 1");
		companyService.saveOrUpdate(col);
		
		System.out.println("test 222");
	}
	
	public void delete(Company c)  throws Exception {
	}
	
	public void testFindCompanyAndChildrenByNo() throws Exception {
		Company company = new Company();
		PageUtil pu = company.getPageUtil();
		pu.setPaging(true);
		pu.setPageSize(2);

		List<Company> list = companyService.findCompanyAndChildrenByNo(3L, company, null);
		ArrayUtils.printList(list,  "companys");
	}
	
	
	@Test
	public void testRun() throws Exception {
		testFindCompanyAndChildrenByNo();
		
//		Company col = testAdd();
//
//		col.getPageUtil().setPaging(true);
//		
//		List<Company> ret = companyService.findOperatorCompanys(col);
//		System.out.println(ret.size());
	}
	
}
