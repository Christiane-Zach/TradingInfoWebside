package com.fdmgroup.traderinfo.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.service.BitfinexService;
import com.fdmgroup.traderinfo.service.DatabaseService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BitfinexServiceTest {
	
	@InjectMocks
	BitfinexService bitfinexExService;
	
	@MockBean
	DatabaseService dbService;
	
	@Test
	public void testConnectionToExchange_callesExractPrice(){
		bitfinexExService.connectionToExchange("BTCUSDT", LocalDateTime.of(2021,8,8,0,0),
												LocalDateTime.of(2021,8,8,0,1));
		verify(dbService, times(1)).getPairId("BTCUSDT");
	}
	
	@Test
	public void testExtractPrice_createPriceEntry() throws Exception{
		URL url = new URL("https://api-pub.bitfinex.com/v2/candles/trade:1m:tBTCUSD/hist?limit=500"
							+ "&start=1628402400000&end=1628402460000&sort=-1");
		BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));
		PriceEntry price = new PriceEntry(10240, LocalDateTime.of(2021,8,8,8,0), 1, new BigDecimal("44713"), 0);
		bitfinexExService.extractPrice(inStream, 1);
		verify(dbService, times(1)).createPriceEntry(price);
	}
	
	@Test
	public void testConnectionToExchange_throwException() {
		LocalDateTime time = LocalDateTime.of(2021,8,8,0,0);
		Throwable exception = assertThrows(NoConnectionToTheExchangeException.class, 
				() -> bitfinexExService.connectionToExchange("abc", time, time));
		assertEquals("There is no connection to the exchange.", exception.getMessage());
	}
	
	@Test
	public void testExtractPrice_throwException() throws Exception{
		Throwable exception = assertThrows(NullPointerException.class, 
				() -> bitfinexExService.extractPrice(new BufferedReader(null), 1));
		assertEquals(null, exception.getMessage());
	}

}
