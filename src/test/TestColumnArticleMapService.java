package test;

import java.util.List;

import org.junit.Test;

import com.db.bms.entity.Article;
import com.db.bms.entity.ColumnArticleMap;
import com.db.bms.entity.Article.ArticleStatus;
import com.db.bms.utils.ArrayUtils;

public class TestColumnArticleMapService extends TestBase {
	
	public void testSelectColumnArticles() throws Exception {
		Article a = new Article();
		a.getPageUtil().setPaging(false);
		a.setColumnNo(2421L);
		a.setStatus(ArticleStatus.PUBLISH.getIndex());
		List<ColumnArticleMap> list = columnArticleMapService.selectColumnArticles(a);
		ArrayUtils.printList(list, "list");
	}
	
	@Test
	public void testRun() throws Exception {
		testSelectColumnArticles();
	}
}
