package test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.db.bms.sync.portal.protocal.GetColumnREQT;
import com.db.bms.sync.portal.protocal.GetPageREQT;

public class TestPortalServer {

	public static void testGetPageList() throws Exception {
		HttpClient httpClient = new DefaultHttpClient(); 
		HttpPost postMethod = new HttpPost("http://10.10.1.76:8080/bms/GetPageList");
		
		GetPageREQT req = new GetPageREQT();
		req.setSerialNo("00001");
/*		req.setColumnId(560L);
		req.setCompanyId("gz");*/
		req.setSystemId("db");
		req.setStartPage(1);
		req.setPageSize(10);
		
		StringEntity se = new StringEntity(req.build());
		postMethod.setEntity(se);
		
		HttpResponse resp = httpClient.execute(postMethod);
		HttpEntity e = resp.getEntity();
		String t = EntityUtils.toString(e);
		System.out.println(t);
	}
	
	public static void testGetColumnList() throws Exception {
		HttpClient httpClient = new DefaultHttpClient(); 
		HttpPost postMethod = new HttpPost("http://127.0.0.1:8080/bms/GetColumnList");
		
		GetColumnREQT req = new GetColumnREQT();
		req.setSerialNo("00001");
		req.setSystemId("db");
		req.setStartPage(1);
		req.setPageSize(10);
		
		StringEntity se = new StringEntity(req.build());
		postMethod.setEntity(se);
		
		HttpResponse resp = httpClient.execute(postMethod);
		HttpEntity e = resp.getEntity();
		String t = EntityUtils.toString(e);
		System.out.println(t);
	}
	
	public static void main(String[] args) throws Exception {
		//testGetPageList();
		testGetColumnList();
	}

}
