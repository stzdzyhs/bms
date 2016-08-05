package com.db.bms.utils;



import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;



public class FtpProcessor {

	private static final Log logger = LogFactory.getLog(FtpProcessor.class);
	
	private FTPClient ftpClient;
	
	private String serverIp;
	
	private int serverPort;
	
	private String userName;
	
	private String password;
	
	public FtpProcessor(String serverIp, int serverPort, String userName, String password){
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.userName = userName;
		this.password = password;		
	}
	
	public boolean connectServer() {
		boolean flag = true;
		if (ftpClient == null) {
			int reply;
			try {
				ftpClient = new FTPClient();
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setDefaultPort(serverPort);
				//ftpClient.configure(getFtpConfig());
				ftpClient.connect(serverIp);
				ftpClient.login(userName, password);
				ftpClient.setDefaultPort(serverPort);
				reply = ftpClient.getReplyCode();
				//ftpClient.setDataTimeout(0);
				
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					logger.info("Couldn't login to FTP Server : "
							+ serverIp
							+ " on port : "
							+ serverPort
							+ " \nwith user name : "
							+ userName
							+ " and password : "
							+ password
							+ "\nPerhaps you have to check user name and password.");
					flag = false;
				}

			} catch (SocketException e) {
				flag = false;
				logger.error("Login FTP server [" + serverIp + "] failed, connection timeout!");
			} catch (IOException e) {
				flag = false;
				logger.error("Login FTP server [" + serverIp + "] failed,unable to open the FTP server!");
			} catch (Exception e){
				flag = false;
				logger.error("Login FTP server [" + serverIp + "] failed!");
			}
		}
		return flag;
	}
	
	public FTPFile[] listFileFilter(final String workDirectory, final String name, final Map<String, String> fileExtMap) {
		if (!connectServer()){
			return null;
		}
		FTPFile[] files = null;
		try {
			    ftpClient.changeWorkingDirectory(workDirectory);
			    files  = ftpClient.listFiles(null,new FTPFileFilter(){
				@Override
				public boolean accept(FTPFile file) {
					if (".".equals(file.getName()) || "..".equals(file.getName())){
						return false;
					}
					
					if (file.getType() == 0 && fileExtMap != null){
						String fileName = file.getName();
						String suffix = fileName.substring(fileName.lastIndexOf(Delimiters.DOT) + 1);
						if (!fileExtMap.containsKey(suffix)){
							return false;
						}
					}

					if (name != null && !name.isEmpty()){
						return file.getName().contains(name);
					}
					return true;
					
				}});
			
		} catch (Exception e) {
			logger.error("Failed to get the FTP server [" + serverIp + "] file list!");
		}
		
		return files;
	}
	
	public String getWorkingDirectory() throws IOException{
		if (ftpClient != null){
			return ftpClient.printWorkingDirectory();
		}
		return null;
	}
	
	private FTPClientConfig getFtpConfig() {
		FTPClientConfig ftpConfig = new FTPClientConfig(
				FTPClientConfig.SYST_UNIX);
		ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);
		return ftpConfig;
	}
	
	public static void main(String[] args){
		FtpProcessor ftp = new FtpProcessor("10.10.2.78", 21, "dflin", "123456");
		//ftp.listFileFilter(null, null);
		
	}
}
