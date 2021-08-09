package com.fdmgroup.traderinfo.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.traderinfo.model.CoinPair;
import com.fdmgroup.traderinfo.model.Exchange;
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.repository.TraderInfoRepository;
import com.fdmgroup.traderinfo.service.BinanceService;
import com.fdmgroup.traderinfo.service.DatabaseService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DatabaseServiceTest {
	
	@MockBean
	TraderInfoRepository mockRepo;
	@MockBean
	PriceEntry mockCurrencyPairEntry;
	
	@InjectMocks
	DatabaseService dbService;
	@MockBean
	BinanceService binanceExService;
	
	@Test
	public void testDatabaseService_filterByPairName() {
		List<CoinPair> coinPairList = new ArrayList<>();
		List<Exchange> exchangeList = new ArrayList<> ();
		coinPairList.add(new CoinPair(1, "BTCUSDT"));
		exchangeList.add(new Exchange(1, "Binance"));
		Mockito.when(mockRepo.getPairByName("BTCUSDT")).thenReturn(coinPairList);
		Mockito.when(mockRepo.getExchangeByExchangeName("Binance")).thenReturn(exchangeList);
		dbService.filterByPairName("BTCUSDT","Binance");;
		verify(mockRepo, times(1)).filterByPairId(1,1);
	}
	
	@Test
	public void testDatabaseService_createPriceEntry() {
		dbService.createPriceEntry(mockCurrencyPairEntry);
		verify(mockRepo, times(1)).save(mockCurrencyPairEntry);
	}
	
	@Test
	public void testDatabaseService_getMaxEpochTime() {
		List<CoinPair> coinPairList = new ArrayList<>();
		List<Exchange> exchangeList = new ArrayList<> ();
		coinPairList.add(new CoinPair(1, "BTCUSDT"));
		exchangeList.add(new Exchange(1, "Binance"));
		Mockito.when(mockRepo.getPairByName("BTCUSDT")).thenReturn(coinPairList);
		Mockito.when(mockRepo.getExchangeByExchangeName("Binance")).thenReturn(exchangeList);
		dbService.getMaxEpochTime("BTCUSDT", "Binance");
		verify(mockRepo, times(1)).getMaxEpochTime(1,1);
	}
	
	@Test
	public void testDatabaseService_getExchangeId() {
		List<Exchange> exchangeList = new ArrayList<> ();
		exchangeList.add(new Exchange(1, "Binance"));
		Mockito.when(mockRepo.getExchangeByExchangeName("Binance")).thenReturn(exchangeList);
		dbService.getExchangeId("Binance");
		verify(mockRepo, times(1)).getExchangeByExchangeName("Binance");
	}
	
	@Test
	public void testDatabaseService_getPairNames() {
		List<CoinPair> coinPairList = new ArrayList<>();
		coinPairList.add(new CoinPair(1, "BTCUSDT"));
		Mockito.when(mockRepo.getPair()).thenReturn(coinPairList);
		List<String> pairNames = dbService.getPairNames();
		assertEquals("BTCUSDT", pairNames.get(0));
	}
	
	@Test
	public void testDatabaseService_getPairId() {
		List<CoinPair> coinPairList = new ArrayList<>();
		coinPairList.add(new CoinPair(2, "ETHBTC"));
		Mockito.when(mockRepo.getPairByName("ETHBTC")).thenReturn(coinPairList);
		dbService.getPairId("ETHBTC");
		verify(mockRepo, times(1)).getPairByName("ETHBTC");
	}
	
	@Test
	public void testDatabaseService_findIntervalForPriceEntries() {
		Map<String,Integer> intervals = new LinkedHashMap<>();
		intervals.put("3minutes", 3);
		List<PriceEntry> listOfPriceEntries = new ArrayList<>();
		for (int i=1;i<8;i++) {
			listOfPriceEntries.add(new PriceEntry(i, LocalDateTime.of(2021,8,8,0,0), 1, new BigDecimal(i), 1));
		}
		Mockito.when(binanceExService.intervals()).thenReturn(intervals);
		List<PriceEntry> filteredListPrice = dbService.findIntervalForPriceEntries("3minutes", listOfPriceEntries);
		assertEquals(2, filteredListPrice.size());
	}
	
	@Test
	public void testDatabaseService_exchangePriceDifference() {
		List<PriceEntry> listOfPriceEntries = new ArrayList<>();
		listOfPriceEntries.add(new PriceEntry(1, LocalDateTime.of(2021,8,8,0,0), 1, new BigDecimal(2), 1));
		List<PriceEntry> expectedListPrice = new ArrayList<>();
		expectedListPrice.add(new PriceEntry(1, LocalDateTime.of(2021,8,8,0,0), 1, new BigDecimal(0), 11));
		List<PriceEntry> filteredListPrice = dbService.exchangePriceDifference(listOfPriceEntries, 
																				listOfPriceEntries);
		assertEquals(expectedListPrice, filteredListPrice);
	}
	
	@Test
	public void testDatabaseService_findPriceEntriesInTimeInterval() {
		List<PriceEntry> listOfPriceEntries = new ArrayList<>();
		for (int i=1;i<8;i++) {
			listOfPriceEntries.add(new PriceEntry(i, LocalDateTime.of(2021,8,8,0,i), 1, new BigDecimal(i), 1));
		}
		List<PriceEntry> zoomedPriceEntries = dbService.findPriceEntriesInTimeInterval(listOfPriceEntries, 
									LocalDateTime.of(2021,8,8,0,3), LocalDateTime.of(2021,8,8,0,5));
		assertEquals(1, zoomedPriceEntries.size());
	}
}
