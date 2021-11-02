package com.exchangerates.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.exchangerates.constants.Constants;

@RunWith(SpringRunner.class)
public class UtilityTest{
	
	
	@Test
	public void validateDateWithProperFormat(){
		
		String userDate = "2021-02-02";
	
		 LocalDate validateDateFormat = Utility.validateDateFormat(userDate);
		
		assertTrue(validateDateFormat != null);
		
	}
	
	
	@Test
	public void validateDateWithInProperFormat(){
		
		String userDate = "2021/02/02";
		
		Optional<LocalDate> validate = Optional.ofNullable(Utility.validateDateFormat(userDate));
		
		assertFalse(validate.isPresent());
		
	}
	
	
	@Test
	public void validateDateWithImProperMonthRange(){
		
		String userDate = "2020-02-02";
		
		String resultData =  Utility.validateDate(userDate);
			
		assertEquals(Constants.OUT_OF_RANGE, resultData);
		
	}
	
}
