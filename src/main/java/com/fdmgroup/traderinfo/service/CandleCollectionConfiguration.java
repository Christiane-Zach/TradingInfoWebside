package com.fdmgroup.traderinfo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public interface CandleCollectionConfiguration {
	
	@Autowired
	public void extractCandles(String pairName);
	
	public Map<String, Integer> intervals();
	
	public List<String> getPairNames();
}

