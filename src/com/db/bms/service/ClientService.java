package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Client;
import com.db.bms.entity.Operator;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;

public interface ClientService {
	
	Client findClientByNo(Long clientNo) throws Exception;
	
	List<Client> findClientsByNo(List<Long> clientNos) throws Exception;

	Client findClientById(String clientId) throws Exception;
    
    List<Client> findAllClients(List<Long> distClientList) throws Exception;
    
    List<Client> findAllClients(Operator curOper) throws Exception;
    
	public List<Client> findClients(Client entity,List<Long> distClientList) throws Exception;
	
    boolean isClientRepeateIdOrName(Client search) throws Exception;

    void saveOrUpdate(Client client) throws Exception;

    List<Client> findClientsWithSubByNo(List<Long> clientNos) throws Exception;

    void deleteClients(List<Client> list) throws Exception;
	
	void deleteClientCardRegionMaps(Long clientNo, Long[] regionIds) throws Exception;
	
	void addClientCardRegionMap(Long clientNo, Long[] regionIds) throws Exception;
	
	List<CardRegion> findClientCardRegions(Long clientNo, CardRegion region) throws Exception;
	
	List<CardRegion> findClientCardRegionsNoSelect(Long clientNo, CardRegion region) throws Exception;
	
	boolean isClientReferenceCardRegion(Long[] regionIds) throws Exception;
	
	Integer findClientCountById(List<String> clientIds) throws Exception;
	
	List<Client> findDistClientByOperNo(Long operatorNo) throws Exception;
	

	/**
	 * 查找operator关联client,如果admin, 返回全部
	 * 和findAllClients()区别：返回结果不包括子client
	 */
	List<Client> findOperatorClients(Client entity) throws Exception;
	
	GetAreaCodeRESP getAreaCodeList(GetAreaCodeREQT request) throws Exception;
	
	List<Client> findClientByParentId(Long parentId) throws Exception;
	
}
