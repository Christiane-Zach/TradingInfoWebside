package com.fdmgroup.traderinfo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.model.CurrencyPairEntry;

@Service
public class CandleCollectionService implements CandleCollectionConfiguration{
	
	@Autowired
	ICurrencyPairService service;
	
	@Override
	public void extractCandles(String pairName) {
	        try {
	        	int index = getPairNames().indexOf(pairName);
	        	URL url = new URL("https://api.binance.com/api/v3/klines?symbol=" + pairName + "&interval=1m");
	            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	            String line;
	            while ((line = in.readLine()) != null) { 
	                String[] parts = line.split("]"); 
	                for(int i=1; i<=parts.length - 1;i++) { 
	                	String[] smallerParts = parts[i].split(",");
	                	Integer epochTime = Integer.parseInt(index + smallerParts[1].substring(1, smallerParts[1].length()-4));
	                	BigDecimal openPrice = new BigDecimal(smallerParts[2].substring(1, smallerParts[2].length()-1));
	                	BigDecimal highestPrice = new BigDecimal(smallerParts[3].substring(1, smallerParts[3].length()-1));
	                	BigDecimal lowestPrice = new BigDecimal(smallerParts[4].substring(1, smallerParts[4].length()-1));
	                	BigDecimal closePrice = new BigDecimal(smallerParts[5].substring(1, smallerParts[5].length()-1));
	                	CurrencyPairEntry currencyPair = new CurrencyPairEntry(epochTime, openPrice, highestPrice, lowestPrice, closePrice, pairName);
	                	service.createPairEntry(currencyPair);
	                }
	            }
	            in.close();
	             
	        }
	        catch (IOException e) {
	            throw new NoConnectionToTheExchangeException();
	        }
	}

	@Override
	public Map<String, Integer> intervals() {
		Map<String, Integer> intervals = new LinkedHashMap<String, Integer>();
		intervals.put("1minute", 1);
		intervals.put("3minutes", 3);
		intervals.put("5minutes", 5);
		intervals.put("15minutes", 15);
		intervals.put("30minutes", 30);
		intervals.put("1hour", 60);
		intervals.put("2hours", 120);
		intervals.put("4hours", 240);
		intervals.put("6hours", 360);
		intervals.put("8hours", 480);
		intervals.put("12hours", 720);
		intervals.put("1day", 1440);
		intervals.put("3days", 4320);
		intervals.put("1week", 10080);
		return intervals;
	}

	@Override
	public List<String> getPairNames() {
		List<String> pairNames = new ArrayList<>(Arrays.asList("BTCUSDT", "ETHBTC"));
		return pairNames;
	}

}
