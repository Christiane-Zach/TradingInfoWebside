package com.fdmgroup.traderinfo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fdmgroup.traderinfo.model.CurrencyPair;

@Service
public class CandleCollectionService implements CandleCollectionConfiguration{
	
	@Override
	public String extractCandles() {
	        String string1 = "Extract candles";
	        //RestTemplate rt = new RestTemplate();
	        try {
	        	 URL url = new URL("https://api.binance.com/api/v3/klines?symbol=BTCUSDT&interval=1m&limit=500");

	            // read text returned by server
	            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	             
	            String line;
	            while ((line = in.readLine()) != null) {
	                string1 = line;
	            }
	            in.close();
	             
	        }
	        catch (MalformedURLException e) {
	            string1 = "Malformed URL: " + e.getMessage();
	        }
	        catch (IOException e) {
	            string1 = "I/O Error: " + e.getMessage();
	        }
		return string1;
	}

}
