package com.fdmgroup.traderinfo.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Exchange {

	@Id
	private Integer exchangeId;
	private String exchangeName;
	
	public Exchange() {}
	
	public Exchange(Integer exchangeId, String exchangeName) {
		super();
		this.exchangeId = exchangeId;
		this.exchangeName = exchangeName;
	}
	
	public Integer getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Integer exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(exchangeId, exchangeName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exchange other = (Exchange) obj;
		return Objects.equals(exchangeId, other.exchangeId) && Objects.equals(exchangeName, other.exchangeName);
	}

	@Override
	public String toString() {
		return "Exchange [exchangeId=" + exchangeId + ", exchangeName=" + exchangeName + "]";
	}
	
}
