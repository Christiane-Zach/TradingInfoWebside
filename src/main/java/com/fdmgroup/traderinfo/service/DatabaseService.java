package com.fdmgroup.traderinfo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
		return repo.filterByPairId(getPairId(pairName), getExchangeId(exchangeName));
	}

	public void createPriceEntry(PriceEntry price) {
		repo.save(price);
	}
	
	public LocalDateTime getMaxEpochTime(String pairName, String exchangeName) {
		return repo.getMaxEpochTime(getPairId(pairName), getExchangeId(exchangeName));
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

	public List<PriceEntry> findIntervalForPriceEntries(String interval, List<PriceEntry> priceEntries) {
		Map<String, Integer> mappedIntervals = exService.intervals();
		List<PriceEntry> intervalEntries = new ArrayList<>();
		int count = mappedIntervals.get(interval);
		for (int i=0; i<priceEntries.size()-count; i+=count) {
			PriceEntry firstEntry = priceEntries.get(i);
			PriceEntry currencyPairInterval = new PriceEntry(
					firstEntry.getPairId(), firstEntry.getTime(), i, firstEntry.getPrice(),firstEntry.getPairId());
			intervalEntries.add(currencyPairInterval);
		}
		return intervalEntries;
	}
	
	public List<PriceEntry> exchangePriceDifference(List<PriceEntry> priceEntriesFirstEx, 
													List<PriceEntry> priceEntriesSecondEx) {
		List<PriceEntry> differenceList = new ArrayList<>();
		for (PriceEntry priceEntryFirstEx: priceEntriesFirstEx) {
			for (PriceEntry priceEntrySecondEx: priceEntriesSecondEx) {
				if ((priceEntryFirstEx.getTime()).isEqual(priceEntrySecondEx.getTime())) {
					BigDecimal difference = (
							(priceEntryFirstEx.getPrice()).subtract(priceEntrySecondEx.getPrice())).abs();
					Integer priceId = priceEntryFirstEx.getPriceId() * priceEntrySecondEx.getPriceId();
					Integer exchangeId = Integer.parseInt(Integer.toString(priceEntryFirstEx.getExchangeId()) 
							+ Integer.toString(priceEntrySecondEx.getExchangeId()));
					PriceEntry price = new PriceEntry(priceId, priceEntryFirstEx.getTime(), 
							priceEntryFirstEx.getPairId(), difference, exchangeId);
					differenceList.add(price);
				}
			}
		}
		return differenceList;
	}

	public List<PriceEntry> findPriceEntriesInTimeInterval(List<PriceEntry> priceEntries, 
															LocalDateTime startDate, LocalDateTime endDate) {
		List<PriceEntry> zoomedPriceEntries = new ArrayList<>();
		for (PriceEntry priceEntry: priceEntries) {
			if ((priceEntry.getTime()).isAfter(startDate) && (priceEntry.getTime()).isBefore(endDate)) {
				zoomedPriceEntries.add(priceEntry);
			}
		}
		return zoomedPriceEntries;
	}

}

