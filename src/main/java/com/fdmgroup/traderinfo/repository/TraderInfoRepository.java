package com.fdmgroup.traderinfo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.traderinfo.model.CoinPair;
import com.fdmgroup.traderinfo.model.Exchange;
import com.fdmgroup.traderinfo.model.PriceEntry;

@Repository
public interface TraderInfoRepository extends JpaRepository<PriceEntry, Integer>{

	@Query("SELECT p FROM PriceEntry p where p.pairId LIKE ?1 and p.exchangeId LIKE ?2 ORDER BY p.time")
	List<PriceEntry> filterByPairId(Integer pairId, Integer exchangeId);
	
	@Query("SELECT e FROM Exchange e where e.exchangeName LIKE %?1%")
	List<Exchange> getExchangeByExchangeName(String exchangeName);
	
	@Query("SELECT c FROM CoinPair c where c.coinPairName LIKE %?1%")
	List<CoinPair> getPairByName(String pairName);
	
	@Query("SELECT d FROM CoinPair d")
	List<CoinPair> getPair();
	
	@Query("SELECT MAX(p.time) FROM PriceEntry p where p.pairId LIKE ?1 and p.exchangeId LIKE ?2")
	LocalDateTime getMaxEpochTime(Integer pairId, Integer exchangeId);
}
