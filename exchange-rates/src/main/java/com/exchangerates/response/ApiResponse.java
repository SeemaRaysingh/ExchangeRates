package com.exchangerates.response;

public class ApiResponse extends BaseResponse {

	Object data;
	
	/**
	 * 
	 * @param status
	 * @param message
	 * @param statusCode
	 * @param data
	 */
	public ApiResponse(boolean status, String message,int statusCode,Object data) {
		super(status, message, statusCode);
		this.data = data;
	}

	/**
	 * 
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 
	 * @param data 
	 * set the data
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
