package com.fdmgroup.traderinfo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.traderinfo.model.CoinPair;
import com.fdmgroup.traderinfo.model.Exchange;
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.repository.TraderInfoRepository;

@Service
public class DatabaseService{
	
	@Autowired
	private TraderInfoRepository repo;
	@Autowired
	private ExchangeService exService;
	
	public List<PriceEntry> filterByPairName(String pairName, String exchangeName) {
		Integer pairId = getPairId(pairName);
		Integer exchangeId = getExchangeId(exchangeName);
		System.out.println(exchangeId);
		return repo.filterByPairId(pairId, exchangeId);
	}

	public void createPriceEntry(PriceEntry price) {
		repo.save(price);
	}
	
	public Integer getExchangeId(String exchangeName) {
		Exchange exchange = repo.getExchangeByExchangeName(exchangeName).get(0);
		return exchange.getExchangeId();
	}
	
	public List<String> getPairNames(){
		List<CoinPair> coinPairList = repo.getPair();
		List<String> pairNameList = new ArrayList<>();
		for (CoinPair coinpair: coinPairList) {
			pairNameList.add(coinpair.getCoinPairName());
		}
		return pairNameList;
	}
	
	public Integer getPairId(String pairName) {
		CoinPair coinPair = repo.getPairByName(pairName).get(0);
		return coinPair.getCoinPairId();
	}

	public List<PriceEntry> findIntervalEntriesForPair(String interval, String pairName, String exchangeName) {
		List<PriceEntry> allEntries = filterByPairName(pairName, exchangeName);
		Map<String, Integer> mappedIntervals = exService.intervals();
		List<PriceEntry> intervalEntries = new ArrayList<>();
		int count = mappedIntervals.get(interval);
		for (int i=0; i<allEntries.size()-count-1; i+=count) {
			PriceEntry firstEntry = allEntries.get(i);
			PriceEntry currencyPairInterval = new PriceEntry(firstEntry.getPairId(), firstEntry.getEpochTime(), i, firstEntry.getPrice(),firstEntry.getPairId());
			intervalEntries.add(currencyPairInterval);
		}
		return intervalEntries;
	}
	
	public Map<LocalDateTime, BigDecimal> differenceBetweenBinanceAndBitfinex(String interval, String pairName) {
		List<PriceEntry> binancePriceEntries = findIntervalEntriesForPair(interval, pairName, "Binance");
		List<PriceEntry> bitfinexPriceEntries = findIntervalEntriesForPair(interval, pairName, "Bitfinex");
		Map<LocalDateTime, BigDecimal> differencMap = new LinkedHashMap<>();
		for (PriceEntry binancePriceEntry: binancePriceEntries) {
			for (PriceEntry bitfinexPriceEntry: bitfinexPriceEntries) {
				if ((binancePriceEntry.getEpochTime()).isEqual(bitfinexPriceEntry.getEpochTime())) {
					BigDecimal difference = ((binancePriceEntry.getPrice()).subtract(bitfinexPriceEntry.getPrice())).abs();
					differencMap.put(binancePriceEntry.getEpochTime(), difference);
				}
			}
		}
		return differencMap;
	}

	public List<PriceEntry> findIntervalEntriesForPairInTimeInterval(String interval, String pairName, String exchangeName, LocalDateTime startDate, LocalDateTime endDate) {
		List<PriceEntry> priceEntries = findIntervalEntriesForPair(interval, pairName, exchangeName);
		List<PriceEntry> zoomedPriceEntries = new ArrayList<>();
		for (PriceEntry priceEntry: priceEntries) {
			if ((priceEntry.getEpochTime()).isAfter(startDate) && (priceEntry.getEpochTime()).isBefore(endDate)) {
				zoomedPriceEntries.add(priceEntry);
			}
		}
		return zoomedPriceEntries;
	}

	public Map<LocalDateTime, BigDecimal> differenceBetweenBinanceAndBitfinexInTimeInterval(String interval, String pairName,
			LocalDateTime startDate, LocalDateTime endDate) {
			Map<LocalDateTime, BigDecimal> differenceMap = differenceBetweenBinanceAndBitfinex(interval, pairName);
			Map<LocalDateTime, BigDecimal> zoomedDifferenceMap = new LinkedHashMap<>();
			long i = 0;
			for (Map.Entry<LocalDateTime, BigDecimal> differencePerDate : differenceMap.entrySet()) {
				if((differencePerDate.getKey()).isAfter(startDate) && (differencePerDate.getKey()).isBefore(endDate)) {
					zoomedDifferenceMap.put(differencePerDate.getKey(), differencePerDate.getValue());
				}
			}
			return zoomedDifferenceMap;
	}

}

