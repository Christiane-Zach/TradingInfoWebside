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
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.service.BinanceService;
import com.fdmgroup.traderinfo.service.BitfinexService;
import com.fdmgroup.traderinfo.service.DatabaseService;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class TraderInfoController {
	
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
	
	@GetMapping("/booking")
	public String goToBooking(ModelMap model) {
		List<Date> dates = new ArrayList<>();
		java.util.Date myDate = new java.util.Date("18/08/2020");
		Date sqlDate = new Date(myDate.getTime());
		dates.add(sqlDate);
		model.addAttribute("dates", dates);
		return "booking";
	}
	
	@PostMapping("/search")
	public String searchForAnItem(ModelMap model, @RequestParam String searchedFor) {
		model.addAttribute("category", searchedFor);
		//model.addAttribute("Items",model.getAttribute("category").getItems());
		return "categories";
	}
	
	@PostMapping("/{pairName}")
	public String goToCoinPage(ModelMap model, @RequestParam String pairName) {
		populateModel(model, pairName);
		List<PriceEntry> binancePrices = dbService.filterByPairName(pairName, "Binance");
		List<PriceEntry> bitfinexPrices = dbService.filterByPairName(pairName, "Bitfinex");
		model.addAttribute("oldInterval", "1minute");
		model.addAttribute("binancePrices", binancePrices);
		model.addAttribute("bitfinexPrices", bitfinexPrices);
		model.addAttribute("bbDifferences", dbService.exchangePriceDifference(binancePrices, bitfinexPrices));
		return "coin";
	}
	
	@PostMapping("/{pairName}/{interval}")
	public String goToIntervalsPage(ModelMap model, @RequestParam String interval, @RequestParam String pairName) {
		List<PriceEntry> binancePrices = dbService.filterByPairName(pairName, "Binance");
		List<PriceEntry> bitfinexPrices = dbService.filterByPairName(pairName, "Bitfinex");
		populateModel(model, pairName);
		model.addAttribute("oldInterval", interval);
		model.addAttribute("binancePrices", dbService.findIntervalForPriceEntries(interval, binancePrices));
		model.addAttribute("bitfinexPrices", dbService.findIntervalForPriceEntries(interval, bitfinexPrices));
		model.addAttribute("bbDifferences", dbService.findIntervalForPriceEntries(interval, 
				dbService.exchangePriceDifference(binancePrices, bitfinexPrices)));
		return "coin";
	}
	
	@PostMapping("/{pairName}/{interval}/zoomedtime")
	public String zoomTime(ModelMap model, @RequestParam String interval, @RequestParam String pairName, 
							@RequestParam Integer startMonth, @RequestParam Integer startHour, 
							@RequestParam Integer startMinute, @RequestParam Integer startDay, 
							@RequestParam Integer startYear, @RequestParam Integer endHour, 
							@RequestParam Integer endMinute, @RequestParam Integer endDay, 
							@RequestParam Integer endYear, @RequestParam Integer endMonth) {
		populateModel(model, pairName);
		LocalDateTime startDate = LocalDateTime.of(startYear, startDay, startMonth, startHour, startMinute);
		LocalDateTime endDate = LocalDateTime.of(endYear, endDay, endMonth, endHour, endMinute);
		List<PriceEntry> binanceTimePrices = dbService.findPriceEntriesInTimeInterval(
				dbService.filterByPairName(pairName, "Binance"), startDate, endDate);
		List<PriceEntry> bitfinexTimePrices = dbService.findPriceEntriesInTimeInterval(
				dbService.filterByPairName(pairName, "Bitfinex"), startDate, endDate);
		model.addAttribute("oldInterval", interval);
		model.addAttribute("binancePrices", dbService.findIntervalForPriceEntries(interval, binanceTimePrices));
		model.addAttribute("bitfinexPrices", dbService.findIntervalForPriceEntries(interval, bitfinexTimePrices));
		model.addAttribute("bbDifferences", dbService.findIntervalForPriceEntries(interval, 
				dbService.exchangePriceDifference(binanceTimePrices, bitfinexTimePrices)));
		return "coin";
	}
	
	@ExceptionHandler(NoConnectionToTheExchangeException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String noConnectionToTheExchange() {
		return "noConnectionToTheExchange";
	}
	
	private void populateModel(ModelMap model, String pairName) {
		populateDatabase(pairName);
		model.addAttribute("oldPairName", pairName);
		model.addAttribute("pairNames", dbService.getPairNames());
		model.addAttribute("intervals", binanceExService.intervals());
	}
	
	private void populateDatabase(String pairName) { 
		LocalDateTime maxTimeBit = dbService.getMaxEpochTime(pairName, "Bitfinex");
		if (maxTimeBit == null) {
			maxTimeBit = LocalDateTime.of(2021, 8, 8, 2, 38);
		}
		while(! maxTimeBit.isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(1))) {
			bitfinexExService.connectionToExchange(pairName, maxTimeBit, maxTimeBit.plusMinutes(200));
			maxTimeBit = dbService.getMaxEpochTime(pairName, "Bitfinex");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				throw new NoConnectionToTheExchangeException();
			}
		}
		LocalDateTime maxTimeBin = dbService.getMaxEpochTime(pairName, "Binance");
		if (maxTimeBin == null) {
			maxTimeBin = LocalDateTime.of(2021, 8, 8, 2, 38);
		}
		while(! maxTimeBin.isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
			binanceExService.connectionToExchange(pairName, maxTimeBin, maxTimeBin.plusMinutes(200));
			maxTimeBin = dbService.getMaxEpochTime(pairName, "Binance");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				throw new NoConnectionToTheExchangeException();
			}
		}
	}

}
