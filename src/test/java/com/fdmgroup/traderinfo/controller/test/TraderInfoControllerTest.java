package com.fdmgroup.traderinfo.controller.test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.traderinfo.controller.TraderInfoController;
import com.fdmgroup.traderinfo.service.CandleCollectionService;
import com.fdmgroup.traderinfo.service.CurrencyPairService;

@SpringBootTest(classes = {TraderInfoController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TraderInfoControllerTest {
	
	@MockBean
	private CandleCollectionService config;
	@MockBean
	private CurrencyPairService service;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void test_goToIndex_statusOk() throws Exception {
		List<String> expectedPairNames = new ArrayList<>();
		mockMvc.perform(get("/"))
			   .andExpect(status().isOk())
			   .andExpect(model().attribute("pairNames", expectedPairNames))
			   .andExpect(view().name("index"));
	}
	
	@Test
	public void test_goToCoinPage_statusOk() throws Exception {
		List<String> expectedPairNames = new ArrayList<>();
		mockMvc.perform(post("/BTCUSDT").param("pairName", "BTCUSDT"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("oldInterval", "1minute"))
				.andExpect(model().attribute("oldPairName", "BTCUSDT"))
				.andExpect(model().attribute("pairNames", expectedPairNames))
				.andExpect(view().name("coin"));
		
		verify(config, times(1)).intervals();
		verify(service, times(1)).filterByPairName("BTCUSDT");
	}
	
	@Test
	public void test_goToIntervalsPage_statusOk() throws Exception {
		List<String> expectedPairNames = new ArrayList<>();
		mockMvc.perform(post("/BTCUSDT/5minutes").param("pairName", "BTCUSDT")
												 .param("interval", "5minutes"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("oldInterval", "5minutes"))
				.andExpect(model().attribute("oldPairName", "BTCUSDT"))
				.andExpect(model().attribute("pairNames", expectedPairNames))
				.andExpect(view().name("coin"));
		
		verify(config, times(1)).intervals();
		verify(service, times(1)).findIntervalEntriesForPair("5minutes", "BTCUSDT");
	}
}
