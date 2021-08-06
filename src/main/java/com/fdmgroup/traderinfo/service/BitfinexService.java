package com.fdmgroup.traderinfo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.traderinfo.exception.NoConnectionToTheExchangeException;
import com.fdmgroup.traderinfo.model.PriceEntry;

@Service
public class BitfinexService extends ExchangeService{
	
	@Autowired
	DatabaseService dbService;

	@Override
	public void connectionToExchange(String pairName, LocalDateTime startTime, LocalDateTime endTime) {
		try {
			URL url = new URL("https://api-pub.bitfinex.com/v2/candles/trade:1m:t" + pairName.substring(0,pairName.length() - 1) 
							+ "/hist?limit=500&start=" + convertDateToEpochTime(startTime) + "&end="
							+ convertDateToEpochTime(endTime) + "&sort=-1");
			BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));
			extractPrice(inStream, dbService.getPairId(pairName));
		} catch (IOException e) {
		       throw new NoConnectionToTheExchangeException();}
	}

	@Override
	public void extractPrice(BufferedReader inStream, Integer pairId) {
		try {
			String outputLine;
			Integer exchangeId = dbService.getExchangeId("Bitfinex");
	    	while ((outputLine = inStream.readLine()) != null) { 
	    		String[] partsOfOutputLine = outputLine.split("]"); 
	        	for(int i=1; i<partsOfOutputLine.length; i++) { 
	        		String cuttedPart = partsOfOutputLine[i].substring(1);
	        		String[] smallerPartsOfOutputLine = cuttedPart.split(",");
	        		Long epochTime = Long.parseLong(smallerPartsOfOutputLine[0].substring(1));
	        		Integer priceId = Integer.parseInt(Integer.toString(exchangeId) + Integer.toString(pairId) + smallerPartsOfOutputLine[0].substring(6, smallerPartsOfOutputLine[0].length()-4));
	        		LocalDateTime currentTime = convertEpochTimeToDate(epochTime);
	                BigDecimal currentPrice = new BigDecimal(smallerPartsOfOutputLine[1]);
	                PriceEntry priceEntry = new PriceEntry(priceId, currentTime, pairId, currentPrice, exchangeId);
	                dbService.createPriceEntry(priceEntry);
	        	}
	    	}
	    	inStream.close();
		} catch (IOException e) {
		       throw new NoConnectionToTheExchangeException();}
	}
		
}
