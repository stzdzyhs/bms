/*package test;

import java.util.List;

import org.junit.Test;

import com.db.bms.entity.Article;
import com.db.bms.entity.Page;
import com.db.bms.entity.Template;
import com.db.bms.sync.portal.protocal.GetPageREQT;
import com.db.bms.sync.portal.protocal.GetPageRESP;
import com.db.bms.utils.ConstConfig;

public class TestPageService extends TestBase {
	
	Template temp;
	public Page testAdd() throws Exception {
		System.out.println("test Add");
		
		temp = new Template();
		temp.setTemplateId("Template 1");
		temp.setTemplateName("col 1 name");
		temp.setTemplateDesc("temp1 desc");
		temp.setCurOper(SA);			
		templateService.addTemplate(temp);
		
		Article col = new Article();
		col.setTemplateNo(temp.getTemplateNo());
		col.setArticleId("Article 1");
		col.setArticleName("col 1 name");
		col.setTitle("title1");
		col.setTitle2("title2");
		col.setIntroduction("col 1 desc");
		col.setBody("testBodyName");
		col.setCurOper(SA);
		articleService.addArticle(col);
		
		// add
		Page p = new Page();
		p.setArticleNo(col.getArticleNo());
		p.setTemplateNo(temp.getTemplateNo());
		p.setPageId("just test");
		p.setUrl("just test");
		p.setCurOper(SA);
		
		pageService.addPage(p);
		
		return p;
	}

	public void testUpdate(Article col) throws Exception {
	}
	
	public void delete(Article c)  throws Exception {
	}
	
	public void testSelectByNo(Article c) throws Exception {
	}
	
	public void testDeletes() throws Exception {
		// add a article
		Page p  = testAdd();
		pageService.deletePage(p);
	}
	
	void testGetPageList() throws Exception {
		GetPageREQT req = new GetPageREQT();
		req.setStartPage(1);
		req.setPageSize(10);
		req.setCompanyId("gz");
		req.setColumnId(560L);
		req.setSystemId("db");
		
		GetPageRESP resp = pageService.getPageList(null, req);
		System.out.println(resp.toString());
	}
	
	void testGeneratePage() throws Exception {
		Article a = articleService.selectByNo(220L);
		Template t = templateService.selectByNo(101L);
		a.setCurOper(SA);
		Page p = pageService.generatePage(a,t);
		System.out.println(p);
	}
	
	@Test
	public void testRun() throws Exception {
		testDeletes();
		
		Page p = new Page();
		p.setColumnNo(560L);
		p.setCurOper(SA);
		p.setStatus(ConstConfig.STATUS_PUBLISH);
		List<Page> ps = pageService.selectPageInColumn(p);
		System.out.println("size:" + ps.size());
		
		//testGetPageList();
		
		//testGeneratePage();
		
		List<Page> list = pageService.selectPages(p);
		System.out.println("size!!!!:" + list.size());
	}
}
*/