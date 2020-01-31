package com.mastercard.account.model;

import java.math.BigDecimal;

/**
 * FundTransferDetails class to store details of a funds transfer transaction
 *
 * @author Anuj Dalal 01/26/2020
 */

public class FundTransferDetails {

	private BigDecimal amount;

	private Integer fromAccountId;

	private Integer toAccountId;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(Integer fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public Integer getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(Integer toAccountId) {
		this.toAccountId = toAccountId;
	}
	
	// default constructor
	public FundTransferDetails() {
		
	}
	
	// Using this for testing purposes only
	public FundTransferDetails(BigDecimal amount, Integer fromAccountId, Integer toAccountId) {
		this.amount = amount;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
	}

	@Override
	public String toString() {
		return "FundTransferDetails [amount=" + amount + ", fromAccountId=" + fromAccountId + ", toAccountId="
				+ toAccountId + "]";
	}
}
