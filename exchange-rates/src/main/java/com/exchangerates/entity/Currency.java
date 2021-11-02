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

	public Currency(String currency, double rate, String docDate, String base) {
		super();
		this.currency = currency;
		this.rate = rate;
		this.docDate = docDate;
		this.base = base;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	@Override
	public String toString() {
		return "Currency [id=" + id + ", currency=" + currency + ", rate="
				+ rate + ", docDate=" + docDate + ", base=" + base + "]";
	}
	
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
