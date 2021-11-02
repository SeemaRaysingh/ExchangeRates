package com.exchangerates.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String currency;
	
	private double rate;
	
	private String docDate;
	
	private String base;

	public Currency() {
		super();
	}

	/**
	 * 
	 * @param currency
	 * @param rate
	 * @param docDate
	 * @param base
	 */
	public Currency(String currency, double rate, String docDate, String base) {
		super();
		this.currency = currency;
		this.rate = rate;
		this.docDate = docDate;
		this.base = base;
	}

	/**
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * set the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * 
	 * @param currency
	 *  set the currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * 
	 * @return rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * 
	 * @param rate
	 * set the rate
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * 
	 * @return docDate
	 */
	public String getDocDate() {
		return docDate;
	}

	/**
	 * 
	 * @param docDate
	 * set the docDate
	 */
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	/**
	 * 
	 * @return base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * 
	 * @param base
	 * set the base
	 */
	public void setBase(String base) {
		this.base = base;
	}

	@Override
	public String toString() {
		return "Currency [id=" + id + ", currency=" + currency + ", rate="
				+ rate + ", docDate=" + docDate + ", base=" + base + "]";
	}
	
	/**
	 * This method checks the equality of two currency objects based on the content
	 * @return 
	 */
	public boolean equals(Object object){
		
		Currency currency = (Currency) object;
		
		if(this.currency.equals(currency.getCurrency()) &&
				this.rate == currency.getRate() && this.docDate.equals(currency.getDocDate())
				&& this.base.equals(currency.getBase())){
			
			return true;
		}
		
		else {
			return false;
		}
	}
}
