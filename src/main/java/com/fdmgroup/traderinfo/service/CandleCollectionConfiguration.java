package com.fdmgroup.traderinfo.service;

import org.springframework.beans.factory.annotation.Autowired;

public interface CandleCollectionConfiguration {
	
	@Autowired
	public String extractCandles();

}

