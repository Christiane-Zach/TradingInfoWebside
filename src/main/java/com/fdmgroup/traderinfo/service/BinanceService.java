package com.fdmgroup.traderinfo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.model.PriceEntry;

@Service
@Primary
public class BinanceService extends ExchangeService{
	
	@Autowired
	DatabaseService dbService;

	@Override
	public void connectionToExchange(String pairName, LocalDateTime startTime, LocalDateTime endTime) {
		try {
			URL url = new URL("https://api.binance.com/api/v3/klines?symbol=" 
								+ pairName + "&interval=1m&startTime=" 
								+ convertDateToEpochTime(startTime) + "&endTime=" 
								+ convertDateToEpochTime(endTime));
			BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));
			extractPrice(inStream, dbService.getPairId(pairName));
		} catch (IOException e) {
		       throw new NoConnectionToTheExchangeException();}
	}

	@Override 
	public void extractPrice(BufferedReader inStream, Integer pairId) {
		try {
			String outputLine;
	    	while ((outputLine = inStream.readLine()) != null) { 
	    		String[] partsOfOutputLine = outputLine.split("]"); 
	        	for(int i=1; i<partsOfOutputLine.length; i++) { 
	        		Integer exchangeId = dbService.getExchangeId("Binance");
	        		String[] smallerPartsOfOutputLine = partsOfOutputLine[i].split(",");
	        		Integer priceId;
	        		if (pairId == 1) {
	        			priceId = Integer.parseInt(Integer.toString(exchangeId) + Integer.toString(pairId) 
        				+ smallerPartsOfOutputLine[1].substring(5, smallerPartsOfOutputLine[2].length()-6));
	        		} else {
	        			priceId = Integer.parseInt(Integer.toString(exchangeId) + Integer.toString(pairId) 
	        				+ smallerPartsOfOutputLine[1].substring(5, smallerPartsOfOutputLine[2].length()-3));
	        		}
	        		LocalDateTime currentTime = convertEpochTimeToDate(Long.parseLong(
	        				smallerPartsOfOutputLine[1].substring(1)));
	                BigDecimal currentPrice = new BigDecimal(smallerPartsOfOutputLine[2].substring(1, 
	                		smallerPartsOfOutputLine[2].length()-1));
	                PriceEntry price = new PriceEntry(priceId, currentTime, pairId, currentPrice, exchangeId);
	                dbService.createPriceEntry(price);
	        	}
	    	}
	    	inStream.close();
		} catch (IOException e) {
		       throw new NoConnectionToTheExchangeException();}
	}
}