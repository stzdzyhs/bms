package com.db.bms.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class MD5Engine {

	private static final Log logger = LogFactory.getLog(MD5Engine.class);

	private ReentrantLock lock = new ReentrantLock();

	private ReentrantLock errorLock = new ReentrantLock();

	private char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private MessageDigest messageDigest = null;

	private BlockingQueue<byte[]> dataQueue;

	private boolean isFinish = false;

	private String md5;

	private boolean isError = false;

	public MD5Engine(boolean flag) {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			if (flag){
				dataQueue = new ArrayBlockingQueue<byte[]>(100000);
				HandleDataThread handleDataThread = new HandleDataThread();
				handleDataThread.start();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void putDataToQueue(byte[] data) {
		try {
			if (this.dataQueue.remainingCapacity() == 0) {
				logger.error("{"
						+ Thread.currentThread().getName()
						+ "} : The data queue is full, current pool video size:"
						+ this.dataQueue.size() + ".");
			}
			this.dataQueue.put(data);
		} catch (InterruptedException e) {
			logger.error("put data to queue exception occurred, cause by:{}", e);
		}
	}

	private String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		try {
			lock.lock();
			this.isFinish = isFinish;
		} finally {
			lock.unlock();
		}
	}

	public String getMd5() {
		try{
			lock.lock();
			return md5;
		}finally{
			lock.unlock();
		}
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public boolean isError() {
		try {
			errorLock.lock();
			return isError;
		} finally {
			errorLock.unlock();
		}
	}

	public void setError(boolean isError) {
		try {
			errorLock.lock();
			this.isError = isError;
		} finally {
			errorLock.unlock();
		}
	}
	
	public String calculateMD5(String filePath){
		FileInputStream fis = null;
		String md5 = null;
		try {
		   File file = new File(filePath);
		   if (!file.exists()){
			   return null;
		   }
		   fis = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				messageDigest.update(buffer, 0, length);
			}
			md5 = bufferToHex(messageDigest.digest());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return md5;
	}

	public class HandleDataThread extends Thread {

		public void run() {
			while (true) {
				try {
					byte[] data = dataQueue.poll(100, TimeUnit.MILLISECONDS);
					if (data != null) {
						messageDigest.update(data, 0, data.length);
					}

					lock.lock();
					if (isFinish && dataQueue.size() <= 0) {
						md5 = bufferToHex(messageDigest.digest());
						lock.unlock();
						return;
					}
				} catch (Exception e) {
				    setError(true);
					logger.error("Handle data exception occurred, cause by:{}",
							e);
				} finally {
					if (lock.isLocked()) {
						lock.unlock();
					}
				}
			}
		}

	}

}
