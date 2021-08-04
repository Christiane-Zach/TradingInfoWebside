package com.fdmgroup.traderinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.traderinfo.model.CurrencyPair;
import com.fdmgroup.traderinfo.service.CandleCollectionConfiguration;
import com.fdmgroup.traderinfo.service.CandleCollectionService;
import com.fdmgroup.traderinfo.service.ICurrencyPairService;


@Controller
public class TraderInfoController {
	
	@Autowired
	CandleCollectionConfiguration configuration;
	ICurrencyPairService service;
	
	@GetMapping("/")
	public String goToIndexPage(ModelMap model) {
		model.addAttribute("candles", configuration.extractCandles());
		return "index";
	}
	
	@PostMapping("/")
	public String createNewCurrencyPair(ModelMap model, @ModelAttribute CurrencyPair currencyPair) {
		
		//service.createPlace(place);
		//model.addAttribute("places", service.findAllPlaces());
		return "index";
	}

}
