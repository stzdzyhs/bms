
package com.db.bms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class HttpUtils {

	public static void downloadFile(String url, String localPath) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		FileOutputStream output = null;
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpclient.execute(get);
			
			String path = localPath.substring(0, localPath.lastIndexOf("/"));
			File dir = new File(path);
			if (!dir.exists()){
				dir.mkdirs();
			}
			File storeFile = new File(localPath);
		    output = new FileOutputStream(storeFile);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream input = entity.getContent();
				byte b[] = new byte[1024];
				int length = 0;
				while ((length = input.read(b)) != -1) {
					output.write(b, 0, length);
				}
				output.flush();
				output.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (output != null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
			httpclient.getConnectionManager().shutdown();
		}

	}
}
