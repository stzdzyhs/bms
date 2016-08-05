package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.ClientMapper;
import com.db.bms.dao.OperatorMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Client;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.ClientService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.AreaCodeInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;

@Service("clientService")
public class ClientServiceImpl implements ClientService{

	@Autowired
	private ClientMapper mapper;

	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private OperatorMapper operatorMapper;
	
	@Autowired
	StrategyService strategyService;

	public StrategyService getStrategyService() {
		return strategyService;
	}

	public void setStrategyService(StrategyService strategyService) {
		this.strategyService = strategyService;
	}

	public ClientMapper getMapper() {
		return mapper;
	}

	@Override
	public Client findClientByNo(Long clientNo) throws Exception {
		return mapper.findClientByNo(clientNo);
	}

	@Override
	public List<Client> findClientsByNo(List<Long> clientNos)
			throws Exception {
		return mapper.findClientsByNo(clientNos);
	}

	@Override
	public Client findClientById(String clientId) throws Exception {
		return mapper.findClientById(clientId);
	}

	@Override
	public List<Client> findAllClients(List<Long> distClientList) throws Exception {
		return mapper.findAllClients(distClientList);
	}

	@Override
	public List<Client> findAllClients(Operator curOper) throws Exception {
		
		List<Long> distClientList = new ArrayList<Long>();
		switch (OperatorType.getType(curOper.getType())) {
		case ORDINARY_OPER:
			List<Client> clientList = mapper.findDistClientByOperNo(curOper.getOperatorNo());
			if (clientList != null && clientList.size() > 0){
				for (Client cmpy : clientList){
					distClientList.add(cmpy.getClientNo());
				}
			}
			break;
		}
		List<Client> belongList = mapper.findAllClients(distClientList);
		return belongList;
	}

	@Override
	public List<Client> findClients(Client entity,List<Long> distClientList) throws Exception {
		PageUtil page = entity.getPageUtil();
		int count = mapper.findClientCount(entity,distClientList);
		page.setRowCount(count);
		return mapper.findClients(entity,distClientList);
	}

	@Override
	public boolean isClientRepeateIdOrName(Client search) throws Exception {
		int count = this.mapper.getClientCountByIdOrName(search);
		return count > 0 ? true : false;
	}

	@Override
	public void saveOrUpdate(Client client) throws Exception { 
		if (client.getClientNo() == null) {
			
			String now = DateUtil.getCurrentTime();
			client.setCreateTime(now);  
			client.setUpdateTime(now);
			
			Long a = this.mapper.getPrimaryKey();
			client.setClientNo(a); 
			
			Long pid = client.getParentId(); 
			if (pid != null && pid >= 0) { 
				Client parent = this.mapper.findClientByNo(client.getParentId()); 
				String path = parent.getPath() + client.getClientNo() + Delimiters.DOT; 
				client.setPath(path); 
			} else {
				String path = client.getClientNo() + Delimiters.DOT;
				client.setPath(path);
			}

			this.mapper.addClient(client);
		} else {
			
			this.mapper.updateClient(client);
		}

	}

	@Override
	public List<Client> findClientsWithSubByNo(List<Long> clientNos)
			throws Exception {
		return mapper.findClientsWithSubByNo(clientNos);
	}

	@Override
	public void deleteClients(List<Client> list) throws Exception { 
		for (Client client : list) { 
			if (client.getClientNo() != null) { 
				Long clientNo = client.getClientNo();  
				mapper.deleteClient(clientNo); 
			}
		}
	}

	@Override
	public void addClientCardRegionMap(Long clientNo, Long[] regionIds)
			throws Exception {
		for (Long regionId : regionIds) {
			mapper.addClientCardRegionMap(clientNo, regionId);
		}
	}

	@Override
	public List<CardRegion> findClientCardRegions(Long clientNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findClientRegionCount(clientNo, region);
		page.setRowCount(count);
		return cardRegionMapper.findClientRegions(clientNo, region);
	}

	@Override
	public List<CardRegion> findClientCardRegionsNoSelect(Long clientNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findClientRegionNoSelectCount(clientNo,
				region);
		page.setRowCount(count);
		return cardRegionMapper.findClientRegionsNoSelect(clientNo, region);
	}

	@Override
	public void deleteClientCardRegionMaps(Long clientNo, Long[] regionIds)
			throws Exception {
		mapper.deleteClientCardRegionMaps(clientNo, regionIds);

	}

	@Override
	public boolean isClientReferenceCardRegion(Long[] regionIds)
			throws Exception {
		int count = mapper.findClientCardRegionMapCountByRegionId(regionIds);
		return count > 0 ? true : false;
	}

	@Override
	public Integer findClientCountById(List<String> clientIds)
			throws Exception {
		return mapper.findClientCountById(clientIds);
	}
	
	@Override
	public List<Client> findDistClientByOperNo(Long operatorNo)
			throws Exception {
		return mapper.findDistClientByOperNo(operatorNo);
	}


	@Override
	public List<Client> findOperatorClients(Client client) throws Exception {
		PageUtil page = client.getPageUtil();
		if (page.getPaging()) {
			int ret = this.mapper.findOperatorClientsCount(client);
			page.setRowCount(ret);
		}
		List<Client> ret = this.mapper.findOperatorClients(client);
		return ret;
	}

	@Override
	public GetAreaCodeRESP getAreaCodeList(GetAreaCodeREQT request)
			throws Exception {
		GetAreaCodeRESP response = new GetAreaCodeRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<AreaCodeInfo> areaCodeList = new ArrayList<AreaCodeInfo>();
		response.setAreaCodeList(areaCodeList);
		
		Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
	    if (portal == null){
			response.setResultCode(CommonResultCode.NOT_FOUND_SYSTEM.getResultCode());
			response.setResultDesc("Could not find the system.");
			return response;
	    }
	    
	    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
			response.setResultCode(CommonResultCode.NO_ACCESS_RIGHTS.getResultCode());
			response.setResultDesc("No access rights.");
			return response;
	    }
		
	    //查询指定运营商
	    if (StringUtils.isNotEmpty(request.getClientId())){
	    	Client client = mapper.findClientById(request.getClientId());
	        AreaCodeInfo areaCode = convertClient(client);
	        areaCodeList.add(areaCode);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
	    
	    Client search = new Client();
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = mapper.findClientCount(search, null);
		page.setRowCount(totalCount);
		List<Client> clientList = mapper.findClients(search, null);
		Iterator<Client> it = clientList.iterator();
		while (it.hasNext()){
			Client client = it.next();
			AreaCodeInfo areaCode = convertClient(client);
			areaCodeList.add(areaCode);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private AreaCodeInfo convertClient(Client client){
		AreaCodeInfo areaCode = new AreaCodeInfo();
		areaCode.setClientId(client.getClientId());
		if (client.getCardRegionList() != null && client.getCardRegionList().size() > 0){
			StringBuffer buffer = new StringBuffer();
			for (CardRegion region : client.getCardRegionList()){
				switch (RegionCodeType.getType(region.getCodeType())){
				case REGION:
					buffer.append(region.getRegionCode());
					break;
				case SECTION:
					buffer.append(region.getRegionSectionBegin()).append(Delimiters.WHIFFLETREE).append(region.getRegionSectionEnd());
					break;
				}
				buffer.append(Delimiters.COMMA);

			}
			
			areaCode.setAreaId(buffer.substring(0, buffer.length() - 1));
		}
		
		return areaCode;
	}

	@Override
	public List<Client> findClientByParentId(Long parentId) throws Exception {
		return mapper.findClientByParentId(parentId);
	}
	
}
