package com.exchangerates.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		
		assertEquals(expected.getDate(),actualResult.getDate());
		
//		assertThat(actualResult).isEqualTo(expected);
		
	}


}
