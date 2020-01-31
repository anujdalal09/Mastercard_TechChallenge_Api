package com.mastercard.account.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mastercard.account.api.CurrencyConversionApiService;
import com.mastercard.account.exception.AccountException;
import com.mastercard.account.model.Account;
import com.mastercard.account.model.FundTransferDetails;
import com.mastercard.account.repository.AccountRepository;
import com.mastercard.api.core.exception.ApiException;

/**
 * AccountService class to implement the methods that serve the REST end points
 * 
 * @author Anuj Dalal 01/26/20
 */

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRep;

	@Autowired
	CurrencyConversionApiService currConvApiService;

	/**
	 * Method to GET account details by a specific account id
	 * 
	 * @param accountNum, account number for which the details are requested
	 * @return Account Object with all the details of that particular account
	 * @throws AccountException 
	 */
	public Account getAccountDetails(Integer accountNum) throws AccountException {
		if(accountRep.findAccountById(accountNum) == null) {
			throw new AccountException("Account with that id does not exist");
		}
		return accountRep.findAccountById(accountNum);
	}


	/**
	 * Method to CREATE an account
	 * 
	 * @param acc, Account Object which maps fields for account table entry for each account
	 * @throws AccountException 
	 */
	public void createAccount(Account acc) throws AccountException {
		// Setting account creation & modification time to current time
		acc.setCreatedDateTime(LocalDateTime.now());
		acc.setLastModifiedDateTime(LocalDateTime.now());
		// Commit the account details to the database
		if(acc.getBalance().compareTo(BigDecimal.ZERO) < 0) {
			throw new AccountException("Cannot open an account with negative balance");
		}
		accountRep.save(acc);
	}

	/**
	 * Method to FUND an account i.e. deposit money in an account
	 * 
	 * @param accountNum, account number of the account which is being funded
	 * @param amount, amount to be funded in the account
	 * @throws AccountException, when an invalid amount such as 0 or less than 0 is supplied
	 */
	public void depositAmount(Integer accountNum, BigDecimal amount) throws AccountException {
		if(accountRep.findAccountById(accountNum) == null) {
			throw new AccountException("Account with that id does not exist");
		}
		Account acc = accountRep.findAccountById(accountNum);
		// Checking if amount to be funded is a valid amount
		if(amount.compareTo(BigDecimal.ZERO) > 0) {
			// Adding amount to balance
			acc.setBalance(acc.getBalance().add(amount));
			// Setting the LastModifiedTime as the current time
			acc.setLastModifiedDateTime(LocalDateTime.now());
			accountRep.save(acc);
		}
		else {
			throw new AccountException("Deposit Amount cannot be equal or less than 0. Please add a valid deposit amount");
		}

	}

	/**
	 * Method to TRANSFER funds from source account to destination account. This method also 
	 * uses master card currency-conversion api to convert transfer amount from source account currency
	 * to destination account currency if they are different.
	 * 
	 * @param fundTransferDetails, object which has details needed for transferring funds
	 * @throws AccountException, for insufficent funds or for invalid transfer amount
	 */
	@Transactional
	public void transferFunds(FundTransferDetails fundTransferDetails) throws AccountException {
		Account fromAcc = accountRep.findAccountById(fundTransferDetails.getFromAccountId());
		Account toAcc = accountRep.findAccountById(fundTransferDetails.getToAccountId());
		if(fromAcc == null || toAcc == null) {
			throw new AccountException("Source or Destination account id does not exist");
		}
		String fromCurrency = fromAcc.getCurrencyCode();
		String toCurrency = toAcc.getCurrencyCode();
		BigDecimal amount = fundTransferDetails.getAmount();
		// If transfer amount is not greater than 0, throw an exception
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new AccountException("Transfer amount cannot be less than or equal to 0. Please give a valid transfer amount");
		}
		BigDecimal amountInConvertedCurrency = BigDecimal.ZERO;
		// Printing for getting accurate checkpoints on console
		System.out.println("Converting "+ amount + " from currency "+ fromCurrency + " to currency " + toCurrency);
		try {
			// If source account has sufficient balance, then only proceed with transfer, else throw an exception
			if(fromAcc.getBalance().compareTo(amount) >= 0) {
				// If source and destination account have same currency, no need for currency-conversion
				if(!fromCurrency.equals(toCurrency)) {
					// Using mastercard currency-conversion api to convert the amount
					amountInConvertedCurrency = currConvApiService.getConvertedAmount(fromCurrency, toCurrency, amount);
					System.out.println("Converted Amount in " + toCurrency + " is "+ amountInConvertedCurrency);
				}
				else {
					amountInConvertedCurrency = amount;
				}
				// Modifying balances of source and destination account after transfer
				fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
				toAcc.setBalance(toAcc.getBalance().add(amountInConvertedCurrency));
				accountRep.save(fromAcc);
				accountRep.save(toAcc);
			}else {
				throw new AccountException("Insufficient funds in the source account to execute transfer");
			}
		}catch(ApiException ae) {
			ae.getError();
		}catch(FileNotFoundException f) {
			f.printStackTrace();
		}catch(ParseException pe) {
			pe.printStackTrace();
		}	
	}
}
