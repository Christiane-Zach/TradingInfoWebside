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
import com.fdmgroup.traderinfo.service.BinanceService;
import com.fdmgroup.traderinfo.service.BitfinexService;
import com.fdmgroup.traderinfo.service.DatabaseService;
import com.fdmgroup.traderinfo.service.ExchangeService;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class TraderInfoController {
	
	@Autowired
	ExchangeService exService;
	@Autowired
	BinanceService binanceExService;
	@Autowired
	BitfinexService bitfinexExService;
	@Autowired
	DatabaseService dbService;
	
	@GetMapping("/")
	public String goToIndexPage(ModelMap model) {
		model.addAttribute("pairNames", dbService.getPairNames());
		return "index";
	}
	
	@PostMapping("/{pairName}")
	public String goToCoinPage(ModelMap model, @RequestParam String pairName) {
		//Timer timer = new Timer();
		//timer.schedule(new TimerTask() {
		//    @Override
		//    public void run() { // Function runs every MINUTES minutes
		    	//binanceExService.connectionToExchange(pairName, 
				//		LocalDateTime.of(2021, 8, 6, 14, 33), 
				//		LocalDateTime.of(2021, 8, 6, 16, 33));
				//bitfinexExService.connectionToExchange(pairName, 
				//		LocalDateTime.of(2021, 8, 6, 14, 33), 
				//		LocalDateTime.of(2021, 8, 6, 16, 33));
		 //   }
		//}, 0, 1000 * 60);
		populateModel(model, pairName);
		model.addAttribute("oldInterval", "1minute");
		model.addAttribute("binancePrices", dbService.filterByPairName(pairName, "Binance"));
		model.addAttribute("bitfinexPrices", dbService.filterByPairName(pairName, "Bitfinex"));
		model.addAttribute("bbDifferences", dbService.differenceBetweenBinanceAndBitfinex("1minute", pairName));
		return "coin";
	}
	
	@PostMapping("/{pairName}/{interval}")
	public String goToIntervalsPage(ModelMap model, @RequestParam String interval, @RequestParam String pairName) {
		populateModel(model, pairName);
		model.addAttribute("oldInterval", interval);
		model.addAttribute("binancePrices", dbService.findIntervalEntriesForPair(interval, pairName, "Binance"));
		model.addAttribute("bitfinexPrices", dbService.findIntervalEntriesForPair(interval, pairName, "Bitfinex"));
		model.addAttribute("bbDifferences", dbService.differenceBetweenBinanceAndBitfinex(interval, pairName));
		return "coin";
	}
	
	@PostMapping("/{pairName}/{interval}/zoomedtime")
	public String zoomTime(ModelMap model, @RequestParam String interval, @RequestParam String pairName, @RequestParam Integer startMonth,
							@RequestParam Integer startHour, @RequestParam Integer startMinute, @RequestParam Integer startDay, 
							@RequestParam Integer startYear, @RequestParam Integer endHour, @RequestParam Integer endMinute,
							@RequestParam Integer endDay, @RequestParam Integer endYear, @RequestParam Integer endMonth) {
		populateModel(model, pairName);
		LocalDateTime startDate = LocalDateTime.of(startYear, startDay, startMonth, startHour, startMinute);
		LocalDateTime endDate = LocalDateTime.of(endYear, endDay, endMonth, endHour, endMinute);
		model.addAttribute("oldInterval", interval);
		model.addAttribute("binancePrices", dbService.findIntervalEntriesForPairInTimeInterval(interval, pairName, "Binance", startDate, endDate));
		model.addAttribute("bitfinexPrices", dbService.findIntervalEntriesForPairInTimeInterval(interval, pairName, "Bitfinex", startDate, endDate));
		model.addAttribute("bbDifferences", dbService.differenceBetweenBinanceAndBitfinexInTimeInterval(interval, pairName, startDate, endDate));
		return "coin";
	}
	
	@ExceptionHandler(NoConnectionToTheExchangeException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String noConnectionToTheExchange() {
		return "noConnectionToTheExchange";
	}
	
	private void populateModel(ModelMap model, String pairName) {
		model.addAttribute("oldPairName", pairName);
		model.addAttribute("pairNames", dbService.getPairNames());
		model.addAttribute("intervals", exService.intervals());
	}

}
