package test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.db.bms.entity.Operator;
import com.db.bms.service.ArticleService;
import com.db.bms.service.ColumnArticleMapService;
import com.db.bms.service.CompanyService;
import com.db.bms.service.PictureService;
import com.db.bms.service.ResourceAllocationService;
import com.db.bms.service.ResourcePublishMapService;
import com.db.bms.service.SpaceService;
import com.db.bms.service.StrategyService;
import com.db.bms.service.TemplateService;
import com.db.bms.service.TopicService;

public class TestBase {
	
	static ClassPathXmlApplicationContext ctx;
	static PictureService pictureService;
	static ArticleService articleService;
	static ColumnArticleMapService columnArticleMapService;
	static TemplateService templateService;
	//static PageService pageService;
	static CompanyService companyService;
	static TopicService topicService;
	static StrategyService strategyService;
	static ResourceAllocationService resourceAllocationService;
	static ResourcePublishMapService resourcePublishMapService;
	static SpaceService spaceService;
	
	static Operator SA = new Operator();
	static {
		SA.setType(Operator.TYPE_SUPER_ADMIN);
		SA.setOperatorNo(-1L);
		SA.setCompanyNo(3L); // gz
	}
	
	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setup");
		String[] cp = new String[]{"spring-mvc.xml", "spring-mybatis.xml", "spring-portal.xml"};
		ctx = new ClassPathXmlApplicationContext(cp);
		pictureService = ctx.getBean(PictureService.class);
		articleService = ctx.getBean(ArticleService.class);
		templateService = ctx.getBean(TemplateService.class);
		//pageService = ctx.getBean(PageService.class);
		companyService = ctx.getBean(CompanyService.class);
		topicService = ctx.getBean(TopicService.class);
		strategyService = ctx.getBean(StrategyService.class);
		resourceAllocationService = ctx.getBean(ResourceAllocationService.class);
		resourcePublishMapService = ctx.getBean(ResourcePublishMapService.class);
		columnArticleMapService = ctx.getBean(ColumnArticleMapService.class);
		spaceService = ctx.getBean(SpaceService.class);		
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		System.out.println("tearDown");
		ctx.close();
    }

}
