package com.fdmgroup.traderinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.traderinfo.model.CurrencyPair;

public interface TraderInfoRepository extends JpaRepository<CurrencyPair, Integer>{

}
