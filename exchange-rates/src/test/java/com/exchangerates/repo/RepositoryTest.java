package com.exchangerates.repo;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.entity.Currency;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

	@Autowired
	CurrencyRepo currencyRepo;
	
	@Test
	public void testFindByCurrNameAndDate(){
		
		Currency expected = null;
		
		Currency actualCurrencyObj = currencyRepo.findByCurrencyAndDocDate("XAU", "2021-02-02");
		
		assertTrue(actualCurrencyObj == expected);
		
	}
	
	
	@Test
	public void testFindByCurrNameAndDate1(){

		Currency currency = new Currency("XAU", 6.49E-4, "2021-02-02", "EUR");

		currencyRepo.save(currency);

		boolean expected = true;

		Currency actualCurrencyObj = currencyRepo.findByCurrencyAndDocDate("XAU", "2021-02-02");

		boolean actual = (actualCurrencyObj.getCurrency().equals(currency.getCurrency()) &&
				actualCurrencyObj.getDocDate().equals(currency.getDocDate())) ? true :false;

		assertEquals(expected,actual);

	}
	
	@Test
	public void testFindByDateBetween(){
		
		Currency c1 = new Currency("XAU", 6.49E-4,"2021-02-02", "EUR");
		Currency c2 = new Currency("XAG", 6.49E-4,"2021-03-02", "EUR");
		Currency c3 = new Currency("GBP", 6.49E-4,"2021-04-02", "EUR");
		
		c1.setId(1);
		c2.setId(2);
		c3.setId(3);
		
		List<Currency> data = new ArrayList<Currency>();
		
		data.add(c1);
		data.add(c2);
		data.add(c3);
		
		List<Currency> expected = currencyRepo.saveAll(data);
		
		List<Currency> actual = currencyRepo.findByDocDateBetween("2021-02-02", "2021-10-30");
		
		assertEquals(expected.size(), actual.size());
		
	}

}
