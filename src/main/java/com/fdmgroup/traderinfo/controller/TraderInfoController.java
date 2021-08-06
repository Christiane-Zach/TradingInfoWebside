package com.fdmgroup.traderinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.service.CandleCollectionConfiguration;
import com.fdmgroup.traderinfo.service.ICurrencyPairService;


@Controller
public class TraderInfoController {
	
	@Autowired
	CandleCollectionConfiguration configuration;
	@Autowired
	ICurrencyPairService service;
	
	@GetMapping("/")
	public String goToIndexPage(ModelMap model) {
		model.addAttribute("pairNames", configuration.getPairNames());
		return "index";
	}
	
	@PostMapping("/{pairName}")
	public String goToCoinPage(ModelMap model, @RequestParam String pairName) {
		configuration.extractCandles(pairName);
		populateModel(model, pairName);
		model.addAttribute("oldInterval", "1minute");
		model.addAttribute("candles", service.filterByPairName(pairName));
		return "coin";
	}
	
	@PostMapping("/{pairName}/{interval}")
	public String goToIntervalsPage(ModelMap model, @RequestParam String interval, @RequestParam String pairName) {
		populateModel(model, pairName);
		model.addAttribute("oldInterval", interval);
		model.addAttribute("candles", service.findIntervalEntriesForPair(interval, pairName));
		return "coin";
	}
	
	@ExceptionHandler(NoConnectionToTheExchangeException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String noConnectionToTheExchange() {
		return "noConnectionToTheExchange";
	}
	
	private void populateModel(ModelMap model, String pairName) {
		model.addAttribute("oldPairName", pairName);
		model.addAttribute("pairNames", configuration.getPairNames());
		model.addAttribute("intervals", configuration.intervals());
	}

}
