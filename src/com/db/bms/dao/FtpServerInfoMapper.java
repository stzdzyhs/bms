
package com.db.bms.dao;

import java.util.List;

import com.db.bms.entity.FtpServerInfo;


public interface FtpServerInfoMapper {

	FtpServerInfo findFtpServerById(Long ftpServerId) throws Exception;
	
	List<FtpServerInfo> findFtpServersById(Long[] ftpServerIds) throws Exception;
	
	Integer findFtpServerCount(FtpServerInfo search) throws Exception;
	
	List<FtpServerInfo> findFtpServers(FtpServerInfo search) throws Exception;
	
	Integer addFtpServer(FtpServerInfo ftpServer) throws Exception;
	
	Integer updateFtpServer(FtpServerInfo ftpServer) throws Exception;
	
	Integer deleteFtpServersById(Long[] ftpServerIds) throws Exception;
}
