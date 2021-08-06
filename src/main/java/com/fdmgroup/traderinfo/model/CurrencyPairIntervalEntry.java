package com.fdmgroup.traderinfo.model;

import java.math.BigDecimal;

public class CurrencyPairIntervalEntry {
	
	private Integer epochTime;
	private BigDecimal openPrice, closePrice;
	private String pairName;
	
	public CurrencyPairIntervalEntry(Integer epochTime, BigDecimal openPrice, BigDecimal closePrice, String name) {
		this.epochTime = epochTime;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.pairName = name;
	}

	public Integer getEpochTime() {
		return epochTime;
	}

	public void setEpochTime(Integer epochTime) {
		this.epochTime = epochTime;
	}

	public BigDecimal getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(BigDecimal openPrice) {
		this.openPrice = openPrice;
	}

	public BigDecimal getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(BigDecimal closePrice) {
		this.closePrice = closePrice;
	}

	public String getPairName() {
		return pairName;
	}

	public void setPairName(String name) {
		this.pairName = name;
	}

	@Override
	public String toString() {
		return "CurrencyPair [epochTime=" + epochTime + ", openPrice=" + openPrice + ", closePrice=" + closePrice
				+ ", name=" + pairName + "]";
	}

}
