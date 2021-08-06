package com.fdmgroup.traderinfo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.traderinfo.model.CurrencyPairEntry;
import com.fdmgroup.traderinfo.model.CurrencyPairIntervalEntry;
import com.fdmgroup.traderinfo.repository.TraderInfoRepository;

@Service
public class CurrencyPairService implements ICurrencyPairService{
	
	@Autowired
	private TraderInfoRepository repo;
	@Autowired
	CandleCollectionConfiguration config;
	
	@Override
	public List<CurrencyPairEntry> filterByPairName(String pairName) {
		return repo.filterByPairName(pairName);
	}

	@Override
	public void createPairEntry(CurrencyPairEntry currencyPairEntry) {
		repo.save(currencyPairEntry);
	}

	@Override
	public List<CurrencyPairIntervalEntry> findIntervalEntriesForPair(String interval, String pairName) {
		List<CurrencyPairEntry> allEntries = filterByPairName(pairName);
		Map<String, Integer> mappedIntervals = config.intervals();
		List<CurrencyPairIntervalEntry> intervalEntries = new ArrayList<>();
		
		int count = mappedIntervals.get(interval);
		System.out.println(allEntries);
		for (int i=0; i<allEntries.size()-count-1; i+=count) {
			CurrencyPairEntry firstEntry = allEntries.get(i);
			CurrencyPairEntry lastEntry = allEntries.get(i+count-1);
			CurrencyPairIntervalEntry currencyPairInterval = new CurrencyPairIntervalEntry(firstEntry.getEpochTime(), firstEntry.getOpenPrice(),lastEntry.getClosePrice(),firstEntry.getPairName());
			intervalEntries.add(currencyPairInterval);
		}
		return intervalEntries;
	}

}

