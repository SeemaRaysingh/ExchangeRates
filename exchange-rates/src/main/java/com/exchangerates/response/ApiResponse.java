package com.exchangerates.response;

public class ApiResponse extends BaseResponse {

	Object data;
	
	public ApiResponse(boolean status, String message,int statusCode,Object data) {
		super(status, message, statusCode);
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
