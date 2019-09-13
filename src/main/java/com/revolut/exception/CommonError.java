package com.revolut.exception;

import java.io.Serializable;

class CommonError implements Serializable {

	private String error;

	CommonError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
