package com.fdmgroup.traderinfo.service;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public abstract class ExchangeService{
	
	public abstract void connectionToExchange(String pairName, LocalDateTime startTime, LocalDateTime endTime);
	
	public abstract void extractPrice(BufferedReader inStream, Integer pairId);

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
	
	public LocalDateTime convertEpochTimeToDate(Long epochTime){
        return LocalDateTime.ofEpochSecond(epochTime/1000, (int)(epochTime%1000), ZoneOffset.UTC);
    }
	
	protected long convertDateToEpochTime(LocalDateTime localDateTime){
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
