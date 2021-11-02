package com.exchangerates.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangerates.entity.Currency;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, String> {

	/**
	 * 
	 * This method returns the currency object for given currency and date
	 * @param currName
	 * @param date
	 * @return
	 */
	public Currency findByCurrencyAndDocDate(String currName, String date);
	
	
	/**
	 * This method selects the data from the startDate to endDate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Currency> findByDocDateBetween(String startDate, String endDate);
	
}
