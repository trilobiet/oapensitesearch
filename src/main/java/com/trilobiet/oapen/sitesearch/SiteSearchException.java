package com.trilobiet.oapen.sitesearch;

import lombok.Getter;

@Getter
public class SiteSearchException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private final String message;
	
	public SiteSearchException(String msg) {
		this.message = msg;
	}

}
