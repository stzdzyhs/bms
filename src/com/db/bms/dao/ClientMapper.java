package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Client;

public interface ClientMapper {
	
    Long getPrimaryKey() throws Exception; 
	
    Client findClientByNo(Long clientNo) throws Exception; 
	
    List<Client> findClientsByNo(List<Long> clientNos) throws Exception;
	
    Client findClientById(String clientId) throws Exception;
	
	List<Client> findAllClients(@Param(value = "distClientList")List<Long> distClientList) throws Exception;

	Integer findClientCount(@Param(value = "client")Client entity,@Param(value = "distClientList")List<Long> distClientList) throws Exception;

	/**
	 * 
	 * @param entity
	 * @param distClientList - 仅限与这些client, 可以为空
	 * @return
	 * @throws Exception
	 */
	List<Client> findClients(@Param(value = "client")Client entity, @Param(value = "distClientList")List<Long> distClientList) throws Exception;
	
	Integer getClientCountByIdOrName(Client entity) throws Exception;
	
	Integer addClient(@Param(value = "client")Client client) throws Exception;

	Integer updateClient(@Param(value = "client")Client client) throws Exception;

	List<Client> findClientsWithSubByNo(List<Long> clientNos)	throws Exception;
	
	Integer deleteClient(@Param(value="clientNo") Long clientNo) throws Exception;

	Integer deleteClientByNos(@Param("nos") Long[] nos) throws Exception;
	
	Integer deleteClientCardRegionMapByClientNo(Long clientNo) throws Exception;
	
	Integer deleteClientCardRegionMaps(@Param(value = "clientNo")Long clientNo, @Param(value = "regionIds")Long[] regionIds) throws Exception;
	
	Integer addClientCardRegionMap(@Param(value = "clientNo")Long clientNo, @Param(value = "regionId")Long regionId) throws Exception;
	
	Integer findClientCardRegionMapCountByRegionId(Long[] regionIds) throws Exception;
	
	Integer findClientCountById(List<String> clientIds) throws Exception;
	
	List<Client> findDistClientByOperNo(Long operatorNo) throws Exception;
	
	/**
	 * 查找operator关联client, 不包括子client
	 */
	List<Client> findOperatorClients(Client entity) throws Exception;
	// count
	Integer findOperatorClientsCount(Client entity) throws Exception;
	
	List<Client> findClientByIds(String[] clientIds) throws Exception;
	
	List<Client> findClientByParentId(Long parentId) throws Exception;
	
	/**
	 * CA码策略
	 */
	/*List<Client> findClientWithStrategy(@Param(value = "client")Client search) throws Exception;
	Integer findClientWithStrategyCount(@Param(value = "client")Client search) throws Exception;*/
	
	
	Integer findStrategyClientNoSelectCount(@Param(value = "strategyNo")Long strategyNo,
			@Param(value = "companyNo")Long companyNo,
			@Param(value = "client")Client client, 
			@Param("excludeIds") Long[] excludeIds) throws Exception;
	List<Client> findStrategyClientNoSelect(@Param(value = "strategyNo")Long strategyNo,
			@Param(value = "companyNo")Long companyNo,
			@Param(value = "client")Client client, 
			@Param("excludeIds") Long[] excludeIds) throws Exception;
}
