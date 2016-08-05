
package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.FtpServerInfoMapper;
import com.db.bms.entity.FtpServerInfo;
import com.db.bms.service.FtpServerInfoService;
import com.db.bms.utils.core.PageUtil;


@Service("ftpServerInfoService")
public class FtpServerInfoServiceImpl implements FtpServerInfoService {

	@Autowired
	private FtpServerInfoMapper ftpServerInfoMapper;
	
	@Override
	public FtpServerInfo findFtpServerById(Long ftpServerId) throws Exception {
		return ftpServerInfoMapper.findFtpServerById(ftpServerId);
	}

	@Override
	public List<FtpServerInfo> findFtpServersById(Long[] ftpServerIds)
			throws Exception {
		return ftpServerInfoMapper.findFtpServersById(ftpServerIds);
	}
	
	@Override
	public List<FtpServerInfo> findFtpServers(FtpServerInfo search)
			throws Exception {
		PageUtil page = search.getPageUtil();
		int count = ftpServerInfoMapper.findFtpServerCount(search);
		page.setRowCount(count);
		return ftpServerInfoMapper.findFtpServers(search);
	}

	@Override
	public void addFtpServer(FtpServerInfo ftpServer) throws Exception {
		ftpServerInfoMapper.addFtpServer(ftpServer);
	}

	@Override
	public void updateFtpServer(FtpServerInfo ftpServer) throws Exception {
		ftpServerInfoMapper.updateFtpServer(ftpServer);
	}

	@Override
	public void deleteFtpServersById(Long[] ftpServerIds) throws Exception {
		ftpServerInfoMapper.deleteFtpServersById(ftpServerIds);
	}

}
