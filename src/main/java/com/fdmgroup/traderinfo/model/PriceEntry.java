package com.fdmgroup.traderinfo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PriceEntry {
	
	@Id
	
	private Integer priceId;
	
	private LocalDateTime time;
	private Integer pairId;
	private BigDecimal price;
	private Integer exchangeId;
	
	public PriceEntry() {};
	
	
	public PriceEntry(Integer priceId, LocalDateTime time, Integer pairId, BigDecimal price, Integer exchangeId) {
		super();
		this.priceId = priceId;
		this.time = time;
		this.pairId = pairId;
		this.price = price;
		this.exchangeId = exchangeId;
	}
	
	public Integer getPriceId() {
		return priceId;
	}


	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}


	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public Integer getPairId() {
		return pairId;
	}
	public void setPairId(Integer pairId) {
		this.pairId = pairId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Integer exchangeId) {
		this.exchangeId = exchangeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(price, time, exchangeId, pairId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceEntry other = (PriceEntry) obj;
		return Objects.equals(price, other.price) && Objects.equals(time, other.time)
				&& Objects.equals(exchangeId, other.exchangeId) && Objects.equals(pairId, other.pairId);
	}

	@Override
	public String toString() {
		return "Price [priceId=" + priceId + ", time=" + time + ", pairId=" + pairId + ", Price=" + price
				+ ", exchangeId=" + exchangeId + "]";
	}
	
}

