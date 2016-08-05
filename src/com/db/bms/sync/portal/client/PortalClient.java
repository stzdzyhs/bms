
package com.db.bms.sync.portal.client;

import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


public class PortalClient {

	public String sendMsg(String url, String content) throws Exception{
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(content, "UTF-8");
			post.setEntity(entity);
			
			post.setHeader("Content-Type", "text/json;charset=UTF-8");
			HttpResponse response = httpClient.execute(post);
			HttpEntity responseEnity = response.getEntity();
            InputStreamReader reader = new InputStreamReader(responseEnity.getContent(), "UTF-8"); 
            StringBuffer responseBuffer = new StringBuffer();
            char[] buff = new char[1024]; 
            int length = 0; 
            while ((length = reader.read(buff)) != -1) { 
            	responseBuffer.append(new String(buff, 0, length));
				httpClient.getConnectionManager().shutdown();
            } 
            return responseBuffer.toString();
		} catch (Exception e) {
			throw e;
		}
	}
}
