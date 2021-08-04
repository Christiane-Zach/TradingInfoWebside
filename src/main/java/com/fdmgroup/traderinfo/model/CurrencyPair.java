package com.fdmgroup.traderinfo.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CurrencyPair {
	
	@Id
	@GeneratedValue
	private Integer epochTime;
	private Integer openPrice;
	private Integer closePrice;
	private String pairName;
	
	public CurrencyPair() {}

	public CurrencyPair(Integer epochTime, Integer openPrice, Integer closePrice, String name) {
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

	public Integer getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Integer openPrice) {
		this.openPrice = openPrice;
	}

	public Integer getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Integer closePrice) {
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
		CurrencyPair other = (CurrencyPair) obj;
		return Objects.equals(closePrice, other.closePrice) && Objects.equals(epochTime, other.epochTime)
				&& Objects.equals(pairName, other.pairName) && Objects.equals(openPrice, other.openPrice);
	}

	@Override
	public String toString() {
		return "CurrencyPair [epochTime=" + epochTime + ", openPrice=" + openPrice + ", closePrice=" + closePrice
				+ ", name=" + pairName + "]";
	}
	
	
	
}
