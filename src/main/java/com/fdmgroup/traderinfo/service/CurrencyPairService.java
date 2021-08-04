package com.fdmgroup.traderinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.traderinfo.repository.TraderInfoRepository;

@Service
public class CurrencyPairService implements ICurrencyPairService{
	
	@Autowired
	private TraderInfoRepository repo;
}
