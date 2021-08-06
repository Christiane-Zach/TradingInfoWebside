package com.fdmgroup.traderinfo.service;

import java.util.List;

import com.fdmgroup.traderinfo.model.CurrencyPairEntry;
import com.fdmgroup.traderinfo.model.CurrencyPairIntervalEntry;

public interface ICurrencyPairService {
	
	//List<CurrencyPairEntry> findAllEntriesForPair();
	
	List<CurrencyPairEntry> filterByPairName(String pairName);
	
	void createPairEntry(CurrencyPairEntry currencyPairEntry);

	List<CurrencyPairIntervalEntry> findIntervalEntriesForPair(String interval, String pairName);

}
