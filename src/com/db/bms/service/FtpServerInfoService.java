
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.FtpServerInfo;


public interface FtpServerInfoService {

	FtpServerInfo findFtpServerById(Long ftpServerId) throws Exception;
	
	List<FtpServerInfo> findFtpServersById(Long[] ftpServerIds) throws Exception;
	
	List<FtpServerInfo> findFtpServers(FtpServerInfo search) throws Exception;
	
	void addFtpServer(FtpServerInfo ftpServer) throws Exception;
	
	void updateFtpServer(FtpServerInfo ftpServer) throws Exception;
	
	void deleteFtpServersById(Long[] ftpServerIds) throws Exception;
}
