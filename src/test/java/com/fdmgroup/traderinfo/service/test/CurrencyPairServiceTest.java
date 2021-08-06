package com.fdmgroup.traderinfo.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
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

import com.fdmgroup.traderinfo.model.CurrencyPairEntry;
import com.fdmgroup.traderinfo.model.CurrencyPairIntervalEntry;
import com.fdmgroup.traderinfo.repository.TraderInfoRepository;
import com.fdmgroup.traderinfo.service.CandleCollectionService;
import com.fdmgroup.traderinfo.service.CurrencyPairService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CurrencyPairServiceTest {
	
	@MockBean
	TraderInfoRepository mockRepo;
	@MockBean
	CurrencyPairEntry mockCurrencyPairEntry;
	
	@InjectMocks
	CurrencyPairService service;
	@MockBean
	CandleCollectionService config;
	
	@Test
	public void testCurrencyPairService_filterByPairName() {
		List<CurrencyPairEntry> expectedList = new ArrayList<>();
		Mockito.when(mockRepo.filterByPairName("BTCUSDT")).thenReturn(expectedList);
		service.filterByPairName("BTCUSDT");
		verify(mockRepo).filterByPairName("BTCUSDT");
	}
	
	@Test
	public void testCurrencyPairService_createPairEntry() {
		service.createPairEntry(mockCurrencyPairEntry);
		verify(mockRepo).save(mockCurrencyPairEntry);
	}
	
	@Test
	public void testCurrencyPairService_findIntervalEntriesForPairs_returnListOfCurrencyIntervalEntriesWithReducedEntrysToListOfCurrencyPairEntry() {
		List<CurrencyPairEntry> expectedList = new ArrayList<>();
		for (int i=1; i<23;i++) {
			BigDecimal bD = new BigDecimal(i);
			CurrencyPairEntry currencyPairEntry = new CurrencyPairEntry(i,bD,bD,bD,bD,"BTCUSDT");
			expectedList.add(currencyPairEntry);
		}
		Map<String,Integer> map = new LinkedHashMap<String, Integer>();
		map.put("5minutes", 5);
		Mockito.when(config.intervals()).thenReturn(map);
		Mockito.when(service.filterByPairName("BTCUSDT")).thenReturn(expectedList);
		List<CurrencyPairIntervalEntry> readoutList = service.findIntervalEntriesForPair("5minutes", "BTCUSDT");
		assertEquals(22, expectedList.size());
		assertEquals(4, readoutList.size());
	}
}
