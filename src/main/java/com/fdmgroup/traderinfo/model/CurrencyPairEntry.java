package com.fdmgroup.traderinfo.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CurrencyPairEntry {

	@Id
	private Integer epochTime;
	private String pairName;
	private BigDecimal openPrice, highestPrice, lowestPrice, closePrice;
	
	public CurrencyPairEntry() {}
	
	public CurrencyPairEntry(Integer epochTime, BigDecimal openPrice, BigDecimal highestPrice, BigDecimal lowestPrice, BigDecimal closePrice, String name) {
		this.epochTime = epochTime;
		this.highestPrice = highestPrice;
		this.lowestPrice = lowestPrice;
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
	public int hashCode() {
		return Objects.hash(closePrice, epochTime, pairName, openPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyPairEntry other = (CurrencyPairEntry) obj;
		return Objects.equals(closePrice, other.closePrice) && Objects.equals(epochTime, other.epochTime)
				&& Objects.equals(pairName, other.pairName) && Objects.equals(openPrice, other.openPrice);
	}

	@Override
	public String toString() {
		return "CurrencyPair [epochTime=" + epochTime + ", openPrice=" + openPrice + ", closePrice=" + closePrice
				+ ", name=" + pairName + "]";
	}

	public BigDecimal getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(BigDecimal highestPrice) {
		this.highestPrice = highestPrice;
	}

	public BigDecimal getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}	
	
}

