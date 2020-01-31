package com.mastercard.account.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.account.exception.AccountException;
import com.mastercard.account.model.Account;
import com.mastercard.account.model.FundTransferDetails;
import com.mastercard.account.service.AccountService;

/**
 * REST Controller for creating accounts, funding accounts and transferring amount 
 * between accounts.
 * @author Anuj Dalal 01/26/20
 */

@RestController
@RequestMapping("/account/")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	
	/**
	 * REST Endpoint to get a particular account details by account number
	 * @param accountId
	 * @return
	 * @throws AccountException 
	 */
	@GetMapping("{accountId}")
    @ResponseBody
    public Account getAccount(@PathVariable Integer accountId) throws AccountException {
        Account acc = accountService.getAccountDetails(accountId);
        return acc;
    }
	
	/**
	 * REST Endpoint to create new accounts
	 * @param accountRequest
	 * @return
	 * @throws AccountException 
	 */
	@PostMapping("create")
    @ResponseBody
    public Account createAccount(@RequestBody Account accountRequest) throws AccountException {
		accountService.createAccount(accountRequest);
        return accountService.getAccountDetails(accountRequest.getAccountNum());
    }
	
	/**
	 * REST Endpoint to fund an existing account
	 * @param accountNum
	 * @param amount
	 * @return
	 * @throws AccountException
	 */
	@PutMapping("{accountNum}/deposit/{amount}")
    @ResponseBody
    public Account deposit(@PathVariable Integer accountNum, @PathVariable BigDecimal amount) throws AccountException {
		System.out.println("Depositing " + amount + " in account num " + accountNum);
		accountService.depositAmount(accountNum,amount);
        return accountService.getAccountDetails(accountNum);
    }
	
	/**
	 * REST Endpoint to transfer amount between two accounts
	 * @param fundTransferDetails
	 * @return
	 * @throws AccountException
	 */
	@PutMapping("transferFunds")
    @ResponseBody
    @Transactional
    public Account transferFunds(@RequestBody FundTransferDetails fundTransferDetails) throws AccountException {
		System.out.println("Transfering amount " + fundTransferDetails.getAmount() + " from account " + fundTransferDetails.getFromAccountId() + " to account " + fundTransferDetails.getToAccountId());
		accountService.transferFunds(fundTransferDetails);
		return accountService.getAccountDetails(fundTransferDetails.getToAccountId());
	}

}
