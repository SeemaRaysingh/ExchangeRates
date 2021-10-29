package com.exchangerates.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.model.Currency;

@DataJpaTest
public class RepositoryTest {

	@Autowired
	CurrencyRepo currencyRepo;
	
	@Test
	public void testFindByCurrNameAndDate(){
		
		Currency expected = new Currency("XAU", 6.49E-4,"2021-02-02", "EUR");
		
		currencyRepo.save(expected);
		
		Currency actualResult = currencyRepo.findByCurrNameAndDate("XAU", "2021-02-02");
		
		assertEquals(expected,actualResult);
		
//		assertThat(actualResult).isEqualTo(expected);
		
	}
	
	@Test
	public void testFindByDateBetween(String startDate, String endDate){
		
		Currency c1 = new Currency("XAU", 6.49E-4,"2021-02-02", "EUR");
		Currency c2 = new Currency("XAG", 6.49E-4,"2021-03-02", "EUR");
		Currency c3 = new Currency("GBP", 6.49E-4,"2021-04-02", "EUR");
		
		List<Currency> expected = new ArrayList<Currency>();
		
		expected.add(c1);
		expected.add(c2);
		expected.add(c3);
		
		currencyRepo.saveAll(expected);
		
		List<Currency> actual = currencyRepo.findByDateBetween("2021-02-02", "2021-10-29");
		
		assertEquals(expected, actual);
		
	}


}
