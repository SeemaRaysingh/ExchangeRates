package com.exchangerates.response;

import com.exchangerates.constants.Constants;

public class BaseResponse {

	private long timestamp;
	private String status;
	private String message;
	private int statusCode;

	public BaseResponse(boolean status, String message, int statusCode) {

		this.timestamp = System.currentTimeMillis();
		this.status = status ? Constants.SUCCESS : Constants.FAILURE;
		this.message = message;
		this.statusCode = statusCode;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "Response [timestamp=" + timestamp + ", status=" + status
				+ ", message=" + message + ", statusCode=" + statusCode + "]";
	}

}
