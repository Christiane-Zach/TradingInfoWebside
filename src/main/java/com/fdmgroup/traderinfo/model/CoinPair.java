package com.fdmgroup.traderinfo.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CoinPair {
	
	@Id
	private Integer coinPairId;
	private String coinPairName;
	
	public CoinPair() {}
	
	public CoinPair(Integer coinPairId, String coinPairName) {
		super();
		this.coinPairId = coinPairId;
		this.coinPairName = coinPairName;
	}

	public Integer getCoinPairId() {
		return coinPairId;
	}

	public void setCoinPairId(Integer coinPairId) {
		this.coinPairId = coinPairId;
	}

	public String getCoinPairName() {
		return coinPairName;
	}

	public void setCoinPairName(String coinPairName) {
		this.coinPairName = coinPairName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coinPairId, coinPairName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoinPair other = (CoinPair) obj;
		return Objects.equals(coinPairId, other.coinPairId) && Objects.equals(coinPairName, other.coinPairName);
	}

	@Override
	public String toString() {
		return "CoinPair [coinPairId=" + coinPairId + ", coinPairName=" + coinPairName + "]";
	}
	
	
}
