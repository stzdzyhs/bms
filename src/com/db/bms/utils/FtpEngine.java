package com.db.bms.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FtpEngine {

	private static final Log logger = LogFactory.getLog(FtpEngine.class);
	
	private FTPClient ftpClient;

	private String serverIp;

	private int serverPort;

	private String userName;

	private String password;

	private String fileName;

	public FtpEngine(String serverIp, int serverPort, String userName,
			String password, String fileName) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.userName = userName;
		this.password = password;
		this.fileName = fileName;
	}

	public boolean connectServer(){
		
		boolean result = false;
		if (ftpClient == null) {
			int reply;
			try {
				ftpClient = new FTPClient();
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.connect(serverIp, serverPort);
				ftpClient.login(userName, password);
				ftpClient.setBufferSize(10240);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				reply = ftpClient.getReplyCode();

				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					logger.error("Couldn't login to FTP Server : "
							+ serverIp
							+ " on port : "
							+ serverPort
							+ " \nwith user name : "
							+ userName
							+ " and password : "
							+ password
							+ "\nPerhaps you have to check user name and password.");
				}else{
					result = true;
				}
				

			} catch (SocketException e) {
				logger.error("Login FTP server [" + serverIp
						+ "] failed, connection timeout!",e);
			} catch (IOException e) {
				logger.error("Login FTP server [" + serverIp
						+ "] failed,unable to open the FTP server!",e);
			} catch (Exception e) {
				logger.error("Login FTP server [" + serverIp + "] failed!",e);
			}
		}
		
		return result;
	}
	
	public void logout(){
		try {
			ftpClient.logout();
		} catch (IOException e) {
			logger.error("Logout FTP server [" + serverIp + "] failed!",e);
		}
	}
	
	public void disconnect(){
		try {
			ftpClient.disconnect();
		} catch (IOException e) {
			logger.error("Disconnect FTP server [" + serverIp + "] failed!",e);
		}finally{
			if (ftpClient != null && ftpClient.isConnected()){
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					logger.error("Disconnect FTP server [" + serverIp + "] failed!",e);
				}
			}
		}
	}
	
	public boolean isConnected(){
		return ftpClient.isConnected();
	}
	
	public void storeFile(InputStream input) throws IOException{
		ftpClient.storeFile(fileName, input);
		input.close();
	}
	
	public void storeFile(String fileName) throws IOException{
		InputStream input = new FileInputStream(fileName);
		ftpClient.storeFile(fileName, input);
		input.close();
	}
	
/*	public boolean rename(String oldName, String newName){
		boolean result = false;
		try {
			ftpClient.rename(oldName + ConstConfig.TS_FILE_SUFFIX, newName + ConstConfig.TS_FILE_SUFFIX);
			result = true;
		} catch (IOException e) {
			logger.error("Rename file name failed,oldName=" + oldName + ", newName=" + newName,e);
		}
		return result;
	}
	
	public boolean delete(String pathName){
		boolean result = false;
		try {
			result = ftpClient.deleteFile(pathName + ConstConfig.TS_FILE_SUFFIX);
		} catch (IOException e) {
			logger.error("Delete file failed,fileName=" + pathName,e);
		}
		
		return result;
	}*/
	
/*	public void storeFile(byte[] data) throws IOException{
		InputStream input = new ByteArrayInputStream(data);
		ftpClient.storeFile(fileName, input);
		input.close();
	}
	
	public void appendFile(byte[] data) throws IOException{
		InputStream input = new ByteArrayInputStream(data);
		ftpClient.appendFile(fileName, input);
		input.close();
	}*/

}
