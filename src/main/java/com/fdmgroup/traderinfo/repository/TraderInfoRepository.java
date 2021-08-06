package com.fdmgroup.traderinfo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.traderinfo.model.CurrencyPairEntry;

@Repository
public interface TraderInfoRepository extends JpaRepository<CurrencyPairEntry, Integer>{

	@Query("SELECT p FROM CurrencyPairEntry p where p.pairName LIKE %?1%")
	List<CurrencyPairEntry> filterByPairName(String pairName);
}
