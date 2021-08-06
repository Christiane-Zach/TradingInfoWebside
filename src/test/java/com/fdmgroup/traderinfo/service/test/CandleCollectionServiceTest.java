package com.fdmgroup.traderinfo.service.test;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.repository.TraderInfoRepository;
import com.fdmgroup.traderinfo.service.ExchangeService;
import com.fdmgroup.traderinfo.service.DatabaseService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CandleCollectionServiceTest {
	
	@MockBean
	TraderInfoRepository mockRepo;
	@MockBean
	PriceEntry mockCurrencyPairEntry;
	
	@InjectMocks
	ExchangeService config;
	@MockBean
	DatabaseService service;
	
	//@Test
	//public void testExtractCandels_create500PairEntrys() {
		//config.extractCandles("BTCUSDT");
		//verify(config, Mockito.times(1)).extractCandles("BTCUSDT");
	//}
	
	
	@Test
	public void testExtractCandels_throwException() {
		Throwable exception = assertThrows(NoConnectionToTheExchangeException.class, () -> config.extractCandles("abc"));
		assertEquals("There is no connection to the exchange.", exception.getMessage());
	}
	
	@Test
	public void testIntervals_returnMapWith14Entrys() {
		Map<String, Integer> intervals = config.intervals();
		assertEquals(14, intervals.size());
	}
}
