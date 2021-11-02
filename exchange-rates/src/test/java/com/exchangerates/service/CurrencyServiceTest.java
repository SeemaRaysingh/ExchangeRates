package com.exchangerates.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.entity.Currency;
import com.exchangerates.services.CurrencyService;
import com.exchangerates.services.CurrencyServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CurrencyServiceTest {

	@Mock
	private static CurrencyRepo currencyRepo;

	@Autowired
	private ApplicationContext context;
	
	@Mock
	private static RestTemplate restTemplate;
	
	@Autowired
	private CurrencyService currencyService ;
	
	public class TestConfig{

		@Bean
		public CurrencyService getCurencyService(){

			return new CurrencyServiceImpl(CurrencyServiceTest.restTemplate, CurrencyServiceTest.currencyRepo);
		}
	}
	
	@Test
	public void testFirstloadData() throws JsonMappingException, JsonProcessingException{
		
		List<String> list = new ArrayList<String>();
		list.add("XAU");
		
		String json = "{\"success\":\"true\",\"timestamp\":\"564687465\",\"historical\":\"true\",\"base\":\"EUR\",\"date\":\"2020-11-11\",\"rates\":{\"XAU\":0.853913}}";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode jsonNode = objectMapper.readTree(json);
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), eq(JsonNode.class))).thenReturn(jsonNode);
		
		currencyService = new TestConfig().getCurencyService();
		
		Currency currency = new Currency("XAU", 0.853913, "2020-11-11", "EUR");
		
		Mockito.when(currencyRepo.findByCurrencyAndDocDate(Mockito.anyString(), Mockito.anyString())).thenReturn(null);//savedObj
		 
		Mockito.when(currencyRepo.save(Mockito.any(Currency.class))).thenReturn(currency);
		
		currencyService.loadData("2020-11-11", list);
		
		Mockito.verify(currencyRepo, Mockito.atLeastOnce()).save(Mockito.any());
	}
	
	@Test
	public void testStoreDataForNewData() throws JsonMappingException, JsonProcessingException{
		
		List<String> list = new ArrayList<String>();
		list.add("XAU");
		
		Currency currency = new Currency("XAU", 0.853913, "2020-11-11", "EUR");
		
		String json = "{\"success\":\"true\",\"timestamp\":\"564687465\",\"historical\":\"true\",\"base\":\"EUR\",\"date\":\"2020-11-11\",\"rates\":{\"XAU\":0.853913}}";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode jsonNode = objectMapper.readTree(json);
		
		Mockito.when(currencyRepo.findByCurrencyAndDocDate(Mockito.anyString(), Mockito.anyString())).thenReturn(null);//savedObj
		 
		Mockito.when(currencyRepo.save(Mockito.any(Currency.class))).thenReturn(currency);

		CurrencyServiceImpl currencyServiceImpl= new CurrencyServiceImpl(currencyRepo);
		
		currencyServiceImpl.storeData("2020-11-11", jsonNode);
		
		Mockito.verify(currencyRepo,Mockito.atLeastOnce()).save(Mockito.any());
		
	}
	
	
	@Test
	public void testStoreDataForExistingData() throws JsonMappingException, JsonProcessingException{
		
		List<String> list = new ArrayList<String>();
		list.add("XAU");
		
		Currency currency = new Currency("XAU", 0.853913, "2020-11-11", "EUR");
		
		String json = "{\"success\":\"true\",\"timestamp\":\"564687465\",\"historical\":\"true\",\"base\":\"EUR\",\"date\":\"2020-11-11\",\"rates\":{\"XAU\":0.853913}}";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode jsonNode = objectMapper.readTree(json);
		
		Mockito.when(currencyRepo.findByCurrencyAndDocDate(Mockito.anyString(), Mockito.anyString())).thenReturn(currency);//savedObj
		 
		CurrencyServiceImpl currencyServiceImpl= new CurrencyServiceImpl(currencyRepo);
		
		currencyServiceImpl.storeData("2020-11-11", jsonNode);
		
		Mockito.verify(currencyRepo,Mockito.never()).save(Mockito.any());
		
	}
	
	@Test
	public void testFetchRate(){  
		
		Currency c1 = new Currency("XAU", 6.49E-4,"2021-02-02", "EUR");
		Currency c2 = new Currency("XAG", 6.49E-4,"2021-03-02", "EUR");
		Currency c3 = new Currency("GBP", 6.49E-4,"2021-04-02", "EUR");
		
		c1.setId(1);
		c2.setId(2);
		c3.setId(3);
		
		List<Currency> expected = new ArrayList<Currency>();
		
		expected.add(c1);
		expected.add(c2);
		expected.add(c3);
		
		Mockito.when(currencyRepo.findByDocDateBetween(Mockito.anyString(), Mockito.anyString())).thenReturn(expected);
		
		currencyService = new TestConfig().getCurencyService();

		assertEquals(expected.size(), currencyService.fetchRate("2021-02-02").size());
		
		assertFalse(3 != currencyService.fetchRate("2021-05-02").size());
		
	}
	
}
