package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.db.bms.entity.Article;
import com.db.bms.entity.Template;
import com.db.bms.utils.ConstConfig;

public class TestArticleService extends TestBase {
	
	Template temp;
	public Article testAdd() throws Exception {
		System.out.println("test Add");
		
		temp = new Template();
		//temp.setTemplateId("Template 1");
		temp.setTemplateName("col 1 name");
		temp.setTemplateDesc("temp1 desc");
		temp.setCurOper(SA);			
		templateService.addTemplate(temp);
		
		Article col = new Article();
		//col.setTemplateNo(temp.getTemplateNo());
		col.setArticleId("Article 1");
		col.setArticleName("col 1 name");
		col.setTitle("title1");
		col.setTitle2("title2");
		col.setIntroduction("col 1 desc");
		col.setBody("testBodyName");
		col.setCurOper(SA);
		
		// add
		articleService.addArticle(col);
		
		//System.out.println("Article no: " + col.getArticleNo());
		return col;
	}

	public void testUpdate(Article col) throws Exception {
		col.setArticleId("Article1UUUUUUUU");
		col.setArticleName("article name UUUUUUUY");
		col.setTitle("title1 UUUUUUUUUUU");
		col.setTitle2("title2 UUUUUUUUU");
		col.setIntroduction("article 1 introductionUUUUUUU");
		col.setBody("testBodyNameUUUUUUUUU");
		col.setCurOper(SA);
		articleService.updateArticle(col);
		
		System.out.println("test 222");
	}
	
	public void delete(Article c)  throws Exception {
		articleService.deleteArticle(c);
	}
	
	public void testSelectByNo(Article c) throws Exception {
	}
	
	public void testDeletes() throws Exception {
		// add a article
		Article art = new Article();
		art.setArticleNo(null);
		//art.setTemplateNo(temp.getTemplateNo());
		art.setArticleId("article1");
		art.setArticleName("article1");
		art.setIntroduction("p0 desc");
		art.setTitle("ttttt");
		art.setTitle2("ttttt2222");
		art.setBody("body");
		art.setCurOper(SA);
		articleService.addArticle(art);

		List<Article> list = new ArrayList<Article>();
		list.add(art);
		
		articleService.deleteArticles(list);
	}

	@Test
	public void t1() throws Exception {
		Article col = new Article();
		col.setArticleNo(1422L);

		// Integer parentType,Long parentId, Integer type
		//articleService.findColumnPublishArticles(col, 1, 1L, 1);
		
		
		/*
		Article art = ad.selectByNoWithPictures(col);
		List<Picture> ps = art.getPictures();
		if(ps==null) {
			System.out.println("NULL");
			return;
		}
		System.out.println("SSS: " + art.getPictures().size());
		if (art.getPictures().size()<1) {
			throw new Exception("????????SSSSSSSS: ");
		}
		*/
	}
	
	//@Test
	public void testRun() throws Exception {
		Article art = new Article();
		art.setArticleNo(220L);
		art.getPageUtil().setPaging(false);
		
		art = articleService.selectByNoWithPictures(art);
		System.out.println("SSS: " + art.getPictures().size());
		if (art.getPictures().size()<1) {
			throw new Exception("????????SSSSSSSS: ");
		}
		
		art = testAdd();
		
		art.setCurOper(SA);
		
		
		articleService.selectArticles(art);
		
		testUpdate(art);

		testDeletes();
		
		articleService.selectByNo(1L);
		articleService.selectByNos(new Long[]{1L,1L});
		
		Article new1 = articleService.selectByNoV2(220L);
		System.out.println("article oper: " + new1.getOperator().getOperatorName());
		
		art.setBody("just-test");
		articleService.uploadArticleBody(art, "filename", null);
		
		articleService.getArticleMaxResNo(art);
		
		art.setStatus( ConstConfig.STATUS_EDIT);
		articleService.audit(art, new Long[]{1L}, null);
	}
}
