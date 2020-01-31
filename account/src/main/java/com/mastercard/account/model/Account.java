package com.mastercard.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entity to persist and perform CRUD operations on a ACCOUNT
 *
 * @author Anuj Dalal 01/26/2020
 */

@Entity
@Table(name = "ACCOUNT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_num")
	private Integer accountNum;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "currency_code")
	private String currencyCode;

	@Column(name = "balance")
	private BigDecimal balance;

	@Column(name = "created_date_time")
	private LocalDateTime createdDateTime;

	@Column(name = "last_modified_date_time")
	private LocalDateTime lastModifiedDateTime;
	
	// default constructor
	public Account() {
		
	}
	
	// Using this for testing purposes only
	public Account(Integer accountNum, String firstName, String lastName, String currencyCode, BigDecimal balance, LocalDateTime createdDateTime,
			LocalDateTime lastModifiedDateTime) {
		this.accountNum = accountNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.currencyCode = currencyCode;
		this.balance = balance;
		this.createdDateTime = createdDateTime;
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	public Integer getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(Integer accountNum) {
		this.accountNum = accountNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public LocalDateTime getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}

	public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	@Override
	public String toString() {
		return "Account [accountNum=" + accountNum + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", currencyCode=" + currencyCode + ", balance=" + balance + ", createdDateTime=" + createdDateTime
				+ ", lastModifiedDateTime=" + lastModifiedDateTime + "]";
	}
}
