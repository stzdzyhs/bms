package test;

import org.junit.Test;

import com.db.bms.entity.Article;
import com.db.bms.entity.Picture;
import com.db.bms.utils.ConstConfig;

public class TestPictureService extends TestBase {
	
	public Picture testAdd() throws Exception {
		
		System.out.println("test Add");
		
		Article art = new Article();
		art.setArticleId("Article 1");
		art.setArticleName("col 1 name");
		art.setTitle("title1");
		art.setTitle2("title2");
		art.setIntroduction("col 1 desc");
		art.setBody("testBodyName");		
		art.setTemplateId(101L);
		art.setCurOper(SA);
		art.setStatus(ConstConfig.STATUS_PUBLISH);
			
		articleService.addArticle(art);
		
		Picture p = new Picture();
		p.setPicName("test1.png");
		p.setPicPath("files/upload/picmgmt/picture/201507211634511.png");
		p.setCheckCode("test");
		p.setArticleNo(art.getArticleNo());
		p.setCreateTime("2015-07-21 16:34:51");
		p.setResNo(21);
		p.setStatus(ConstConfig.STATUS_PUBLISH);
		
		p.setCurOper(SA);
		
		pictureService.addPicture(p);
		
		return p;
	}

	public void testUpdate(Picture pic) throws Exception {
		pic.setResNo(23);
		pic.setCurOper(SA);
		pictureService.updatePictureBasicInfo(pic);
		
		System.out.println("test 222");
	}
	
	public void delete(Picture c)  throws Exception {
//		pictureService.deletePicture(c);
	}
	
	public void testSelectByNo(Picture c) throws Exception {
	}
	
	public void testFindPublishedPicture() throws Exception {

	}

	@Test
	public void testRun() throws Exception {
		
		Picture pic = testAdd();
		
		testUpdate(pic);
		
		pic.setResNo(1);
		pictureService.isResNoUnique(pic);
		
		pic = pictureService.findArticleFirstPicture(220L);
		if(pic!=null) {
			System.out.println(pic.getId());
		}
		else {
			System.out.println("null");
		}
		
	}
}
