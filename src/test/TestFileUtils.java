package test;



import org.junit.Assert;
import org.junit.Test;

import com.db.bms.utils.FileUtils;


public class TestFileUtils {

	@Test
	public void test1() throws Exception {
		String s = FileUtils.readFileContent("C:/upload/article/201507271913251.txt");
		System.out.println("s:" + s);
		
	}
	
	@Test
	public void testReplaceExtName() {
		String c = FileUtils.replaceExtName("a.html",  "zip");
		Assert.assertTrue("testReplaceExtName error", c.equals("a.zip"));
	}
	
}
