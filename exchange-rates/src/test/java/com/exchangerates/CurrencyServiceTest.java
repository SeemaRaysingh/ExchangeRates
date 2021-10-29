package com.exchangerates;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.services.CurrencyServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
public class CurrencyServiceTest {

	@MockBean
	private RestTemplate restTemplate;
	
	@MockBean
	private CurrencyRepo repo;
	
	@InjectMocks
	private CurrencyServiceImpl currencyServiceImpl;
	
	@Test
	void testGetForObject() throws JsonMappingException, JsonProcessingException{
		
		List<String> list = new ArrayList();
		list.add("XAU");
		
//		String json = "{ \"XAU\" : \"v1\" } ";
		
		String json = "[{\"success\":\"true\",\"timestamp\":\"564687465\",\"historical\":\"true\",\"base\":\"EUR\",\"date\":\"2021-10-01\",\"rates\":{\"GBP\":0.853913}}]";
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(json);
		
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), eq(JsonNode.class))).thenReturn(jsonNode);
//		Mockito.when(currencyServiceImpl.storeData(Mockito.anyString(),any(JsonNode.class))).thenReturn(true);
		
//		MyBean myBean = mock(JsonNode.class);
		
		Mockito.when(currencyServiceImpl.storeData(Mockito.anyString(), jsonNode)).thenReturn(true);
		
		currencyServiceImpl.loadData("2020-11-11", list);
		
	}
	
	@Test
	void testStoreData(){
		
		
		
	}
}
