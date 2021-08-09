package com.fdmgroup.traderinfo.controller.test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.traderinfo.controller.TraderInfoController;
import com.fdmgroup.traderinfo.model.PriceEntry;
import com.fdmgroup.traderinfo.service.BinanceService;
import com.fdmgroup.traderinfo.service.BitfinexService;
import com.fdmgroup.traderinfo.service.DatabaseService;

@SpringBootTest(classes = {TraderInfoController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TraderInfoControllerTest {
	
	@MockBean
	BinanceService binanceExService;
	@MockBean
	BitfinexService bitfinexExService;
	@MockBean
	private DatabaseService dbService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void test_goToIndex_statusOk() throws Exception{
		List<String> expectedPairNames = new ArrayList<>();
		mockMvc.perform(get("/"))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("pairNames", expectedPairNames))
			   .andExpect(view().name("index"));
	}
	
	@Test
	public void test_goToCoinPage_statusOk() throws Exception{
		List<String> expectedPairNames = new ArrayList<>();
		Mockito.when(dbService.getMaxEpochTime("BTCUSDT", "Bitfinex")).thenReturn(
				LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(1));
		Mockito.when(dbService.getMaxEpochTime("BTCUSDT", "Binance")).thenReturn(
				LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		mockMvc.perform(post("/BTCUSDT").param("pairName", "BTCUSDT"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("oldInterval", "1minute"))
				.andExpect(model().attribute("oldPairName", "BTCUSDT"))
				.andExpect(model().attribute("pairNames", expectedPairNames))
				.andExpect(view().name("coin"));
		
		verify(binanceExService, times(1)).intervals();
		verify(dbService, times(1)).filterByPairName("BTCUSDT", "Binance");
		verify(dbService, times(1)).filterByPairName("BTCUSDT", "Bitfinex");
	}
	

	@Test
	public void test_goToIntervalsPage_statusOk() throws Exception{
		List<String> expectedPairNames = new ArrayList<>();
		List<PriceEntry> expectedPriceEntries = new ArrayList<>();
		Mockito.when(dbService.getMaxEpochTime("BTCUSDT", "Bitfinex")).thenReturn(
				LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(1));
		Mockito.when(dbService.getMaxEpochTime("BTCUSDT", "Binance")).thenReturn(
				LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		mockMvc.perform(post("/BTCUSDT/5minutes").param("pairName", "BTCUSDT")
												 .param("interval", "5minutes"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("oldInterval", "5minutes"))
				.andExpect(model().attribute("oldPairName", "BTCUSDT"))
				.andExpect(model().attribute("pairNames", expectedPairNames))
				.andExpect(view().name("coin"));
		verify(binanceExService, times(1)).intervals();
		verify(dbService, times(1)).filterByPairName("BTCUSDT", "Binance");
		verify(dbService, times(1)).filterByPairName("BTCUSDT", "Bitfinex");
		verify(dbService, times(3)).findIntervalForPriceEntries("5minutes", expectedPriceEntries);
		verify(dbService, times(1)).exchangePriceDifference(expectedPriceEntries, expectedPriceEntries);
	}
	
	@Test 
	public void test_zoomTime_statusOk() throws Exception{
		List<String> expectedPairNames = new ArrayList<>();
		List<PriceEntry> expectedPriceEntries = new ArrayList<>();
		Mockito.when(dbService.getMaxEpochTime("BTCUSDT", "Bitfinex")).thenReturn(
				LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(1));
		Mockito.when(dbService.getMaxEpochTime("BTCUSDT", "Binance")).thenReturn(
				LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		mockMvc.perform(post("/BTCUSDT/5minutes/zoomedtime").param("pairName", "BTCUSDT")
												.param("interval", "5minutes")
												.param("startMonth", "8")
												.param("startHour", "0")
												.param("startMinute", "0")
												.param("startDay", "8")
												.param("startYear", "2021")
												.param("endHour", "0")
												.param("endMinute", "0")
												.param("endDay", "8")
												.param("endYear", "2021")
												.param("endMonth", "8"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("oldInterval", "5minutes"))
				.andExpect(model().attribute("oldPairName", "BTCUSDT"))
				.andExpect(model().attribute("pairNames", expectedPairNames))
				.andExpect(view().name("coin"));
		verify(binanceExService, times(1)).intervals();
		verify(dbService, times(1)).filterByPairName("BTCUSDT", "Binance");
		verify(dbService, times(1)).filterByPairName("BTCUSDT", "Bitfinex");
		verify(dbService, times(3)).findIntervalForPriceEntries("5minutes", expectedPriceEntries);
		verify(dbService, times(2)).findPriceEntriesInTimeInterval(expectedPriceEntries, 
				LocalDateTime.of(2021, 8, 8, 0, 0), LocalDateTime.of(2021, 8, 8, 0, 0));
		verify(dbService, times(1)).exchangePriceDifference(expectedPriceEntries, expectedPriceEntries);
		
	}
}
