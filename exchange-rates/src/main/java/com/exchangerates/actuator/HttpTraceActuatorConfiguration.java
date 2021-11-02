package com.exchangerates.actuator;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 

/**
 * 
 * This class enables the trace in actuator
 *
 */
public class HttpTraceActuatorConfiguration { 
	
	@Bean 
	public HttpTraceRepository httpTraceRepository()
	{ 
		return new InMemoryHttpTraceRepository(); 
		}
	
}