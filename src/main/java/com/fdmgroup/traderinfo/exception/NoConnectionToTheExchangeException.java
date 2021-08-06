package com.fdmgroup.traderinfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoConnectionToTheExchangeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NoConnectionToTheExchangeException() {
		super("There is no connection to the exchange.");
	}

}
