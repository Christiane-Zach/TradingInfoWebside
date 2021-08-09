package com.fdmgroup.traderinfo.service.test;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.service.BinanceService;
import com.fdmgroup.traderinfo.service.DatabaseService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BinanceServiceTest {
	
	@InjectMocks
	BinanceService binanceExService;
	
	@MockBean
	DatabaseService dbService;
	
	@Test
	public void testIntervals_returnMapWith14Entrys() {
		Map<String, Integer> intervals = binanceExService.intervals();
		assertEquals(11, intervals.size());
	}
	
	@Test
	public void test_convertEpochTimeToDate_returnLocalDateTime() {
		LocalDateTime date = binanceExService.convertEpochTimeToDate(1628373600000L);
		assertEquals(LocalDateTime.of(2021,8,8,0,0), date);
	}
	
	@Test
	public void test_convertDateToEpochTime_returnEpochTime() {
		Long epochTime = binanceExService.convertDateToEpochTime(LocalDateTime.of(2021,8,8,0,0));
		assertEquals(1628373600000L, epochTime);
	}
	
	@Test
	public void testConnectionToExchange_callesExractPrice(){
		binanceExService.connectionToExchange("BTCUSDT", LocalDateTime.of(2021,8,8,0,0), 
				LocalDateTime.of(2021,8,8,0,0));
		verify(dbService, times(1)).getPairId("BTCUSDT");
	}
	
	@Test
	public void testExtractPrice_createPriceEntry() throws Exception{
		URL url = new URL("https://api.binance.com/api/v3/klines?symbol=BTCUSDT&interval=1m"
							+ "&startTime=1628402400000&endTime=1628402460000");
		BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));
		PriceEntry price = new PriceEntry(140246, LocalDateTime.of(2021,8,8,8,1), 1, 
											new BigDecimal("44906.00000000"), 0);
		binanceExService.extractPrice(inStream, 1);
		verify(dbService, times(1)).createPriceEntry(price);
	}
	
	@Test
	public void testConnectionToExchange_throwException() {
		LocalDateTime time = LocalDateTime.of(2021,8,8,0,0);
		Throwable exception = assertThrows(NoConnectionToTheExchangeException.class, 
				() -> binanceExService.connectionToExchange("abc", time, time));
		assertEquals("There is no connection to the exchange.", exception.getMessage());
	}
	
	@Test
	public void testExtractPrice_throwException() throws Exception{
		Throwable exception = assertThrows(NullPointerException.class, 
				() -> binanceExService.extractPrice(new BufferedReader(null), 1));
		assertEquals(null, exception.getMessage());
	}
}
