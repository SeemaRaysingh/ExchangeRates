package com.exchangerates.response;

import com.fasterxml.jackson.databind.JsonNode;

public class Response {

	String statusCode;
	JsonNode response;
	
	public Response(String statusCode, JsonNode response) {
		super();
		this.statusCode = statusCode;
		this.response = response;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public JsonNode getResponse() {
		return response;
	}

	public void setResponse(JsonNode response) {
		this.response = response;
	}
	
	
	
}
