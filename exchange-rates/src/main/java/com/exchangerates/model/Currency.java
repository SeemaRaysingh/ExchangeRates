package com.exchangerates.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String currName;
	
	private double rate;
	
	private String date;
	
	private String baseCurr;
	
	
	public Currency() {
		super();
	}


	public Currency(String currName, double rate, String date,
			String baseCurr) {
		super();
		this.currName = currName;
		this.rate = rate;
		this.date = date;
		this.baseCurr = baseCurr;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCurrName() {
		return currName;
	}


	public void setCurrName(String currName) {
		this.currName = currName;
	}


	public double getRate() {
		return rate;
	}


	public void setRate(double rate) {
		this.rate = rate;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getBaseCurr() {
		return baseCurr;
	}


	public void setBaseCurr(String baseCurr) {
		this.baseCurr = baseCurr;
	}


	@Override
	public String toString() {
		return "Currency [id=" + id + ", currName=" + currName + ", rate="
				+ rate + ", date=" + date + ", baseCurr=" + baseCurr + "]";
	}

	
}
