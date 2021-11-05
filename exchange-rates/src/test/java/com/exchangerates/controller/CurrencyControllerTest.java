package com.exchangerates.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.entity.Currency;
import com.exchangerates.services.CurrencyService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class CurrencyControllerTest {
	
	@MockBean
	CurrencyService currencyService;

	@MockBean
	CurrencyRepo currencyRepo;
	
	@Autowired
	private MockMvc mockMvc;
	
	@org.junit.Test
	public void canLoadExchangeRates() throws Exception{

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/Rate")
				.param("date", "2021-11-11")
				.param("currency", "XAU")
				.param("base", "EUR"))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);

	}
	
	@org.junit.Test
	public void canNotLoadExchangeRates() throws Exception{

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/Rate")
				.param("date", "2021/11/11")
				.param("currency", "XAU")
				.param("base", "EUR"))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(400, status);

	}
	
	@Test
	public void canLoadRatesForYear() throws Exception{
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/Rates_For_Year/{currency}", "XAU"))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(200, status);
		
	}
	
	
	@org.junit.Test
	public void canGetRateByDateAndCurrency() throws Exception{

		Currency currency = new Currency("XAU", 0.853913, "2020-11-11", "EUR");
		
		Mockito.when(currencyRepo.findByCurrencyAndDocDate(Mockito.anyString(), Mockito.anyString())).thenReturn(currency);
		
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/Rate_By_Date_And_Currency")
				.param("date", "2021-11-11")
				.param("currency", "XAU").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);

	}
	
	@org.junit.Test
	public void canNotGetRateByDateAndCurrency() throws Exception{

		Mockito.when(currencyRepo.findByCurrencyAndDocDate(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/Rate_By_Date_And_Currency")
				.param("date", "2021-11-11")
				.param("currency", "XAU").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(404, status);

	}
	
	
}
