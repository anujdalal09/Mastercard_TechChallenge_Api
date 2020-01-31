package com.mastercard.account.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

import com.mastercard.account.exception.AccountException;
import com.mastercard.account.model.Account;
import com.mastercard.account.model.FundTransferDetails;
import com.mastercard.account.repository.AccountRepository;

/**
 * AccountServiceTest class for unit testing
 * 
 * @author Anuj Dalal 01/28/20
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accRep;
    
    @InjectMocks
    private AccountService accountService;
    
    @Before
    public void init() {
    	MockitoAnnotations.initMocks(this);
    }
    
    Account acc1,accdep1,acc2,acc3,acc4,acc5,acctrfto1,acctrffrom1,acctrfto3,acctrffrom3;
    FundTransferDetails ftd1,ftd2, ftd3, ftd4, ftd5;
    
    @Before
    public void setup() {
    	acc1 = new Account(1234, "FirstName1", "LastName1", "USD", new BigDecimal(5000.00), LocalDateTime.now(), LocalDateTime.now());
    	accdep1 = new Account(1234, "FirstName1", "LastName1", "USD", new BigDecimal(7000.00), LocalDateTime.now(), LocalDateTime.now());
    	acc2 = new Account(1235, "FirstName2", "LastName2", "INR", new BigDecimal(500000.00), LocalDateTime.now(), LocalDateTime.now());
    	acc3 = new Account(123456, "FirstName3", "LastName3", "EUR", new BigDecimal(10000.00), LocalDateTime.now(), LocalDateTime.now());
    	acc4 = new Account(123457, "FirstName4", "LastName4", "INI", new BigDecimal(50000.00), LocalDateTime.now(), LocalDateTime.now());
    	acc5 = new Account(7777, "FirstName5", "LastName5", "USD", new BigDecimal(50000.00), LocalDateTime.now(), LocalDateTime.now());
    	ftd1 = new FundTransferDetails(new BigDecimal(5000.00), 7777, 1234);
    	acctrffrom1 = new Account(7777, "FirstName5", "LastName5", "USD", new BigDecimal(45000.00), LocalDateTime.now(), LocalDateTime.now());
    	acctrfto1 = new Account(1234, "FirstName1", "LastName1", "USD", new BigDecimal(10000.00), LocalDateTime.now(), LocalDateTime.now());
    	ftd2 = new FundTransferDetails(new BigDecimal(-100.00), 7777, 1234);
    	ftd3 = new FundTransferDetails(new BigDecimal(2000.00), 1234, 1235);
    	acctrffrom3 = new Account(1234, "FirstName1", "LastName1", "USD", new BigDecimal(3000.00), LocalDateTime.now(), LocalDateTime.now());
    	acctrfto3 = new Account(1235, "FirstName2", "LastName2", "INR", new BigDecimal(642190.002441), LocalDateTime.now(), LocalDateTime.now());
    	ftd4 = new FundTransferDetails(new BigDecimal(5000.00), 1234, 1235);
    	ftd5 = new FundTransferDetails(new BigDecimal(5000.00), 123456, 123457);
    }
    
    @Test
    public void verifyInitializations() {
    	assertTrue(accRep != null);
    	assertTrue(accountService != null);
    }
    
    @Test
    public void getAccountDetailsTest() throws AccountException {
    	when(accRep.findAccountById(1234)).thenReturn(acc1);
    	Account result = accountService.getAccountDetails(1234);
    	assertTrue(result.getCurrencyCode() == "USD");
    	assertTrue(result.getBalance().compareTo(new BigDecimal(5000.00)) == 0);
    	assertTrue(result.getFirstName().equals("FirstName1"));
    	assertTrue(result.getLastName().equals("LastName1"));
    }
    
    @Test
    public void getInvalidAccountTest() throws AccountException {
    	when(accRep.findAccountById(8736)).thenReturn(null);
    	Account result = accRep.findAccountById(8736);
    	AccountException ae = assertThrows(AccountException.class, () -> accountService.getAccountDetails(8736));
    	assertTrue(result == null && accRep.findAccountById(8736) == null);
    	assertEquals("Account with that id does not exist", ae.getMessage());
    }

	@Test
    public void createValidAccountTest() throws AccountException {
		when(accRep.findAccountById(1235)).thenReturn(acc2);
    	Account result = accountService.getAccountDetails(1235);
    	assertTrue(result.getCurrencyCode() == "INR");
    	assertTrue(result.getBalance().compareTo(new BigDecimal(500000.00)) == 0);
    	assertTrue(result.getFirstName().equals("FirstName2"));
    	assertTrue(result.getLastName().equals("LastName2"));
    	assertEquals(result, accountService.getAccountDetails((acc2.getAccountNum())));
    }
	
	@Test
    public void depositValidAmountTest() throws AccountException {
		
		when(accRep.findAccountById(1234)).thenReturn(accdep1);
    	Account result = accountService.getAccountDetails(1234);
    	assertTrue(result.getCurrencyCode() == "USD");
    	assertTrue(result.getBalance().compareTo(new BigDecimal(7000.00)) == 0);
    }
	
	@Test
    public void depositInvalidAmountTest() throws AccountException {
		when(accRep.findAccountById(1234)).thenReturn(acc1);
    	AccountException ae = assertThrows(AccountException.class, () -> accountService.depositAmount(1234, new BigDecimal(-10.00)));
    	assertEquals("Deposit Amount cannot be equal or less than 0. Please add a valid deposit amount", ae.getMessage());
    }
	
	@Test
    public void TransferValidAmountSameCurrencyTest() throws AccountException {
		when(accRep.findAccountById(7777)).thenReturn(acctrffrom1);
		when(accRep.findAccountById(1234)).thenReturn(acctrfto1);
		assertEquals("USD",accountService.getAccountDetails(7777).getCurrencyCode());
		assertEquals("USD",accountService.getAccountDetails(1234).getCurrencyCode());
		assertTrue(accountService.getAccountDetails(7777).getBalance().compareTo(new BigDecimal(45000.00)) == 0);
		assertTrue(accountService.getAccountDetails(1234).getBalance().compareTo(new BigDecimal(10000.00)) == 0);
    }
	
	@Test
    public void TransferValidAmountDifferentCurrencyTest() throws AccountException {
		when(accRep.findAccountById(1234)).thenReturn(acctrffrom3);
		when(accRep.findAccountById(1235)).thenReturn(acctrfto3);
		assertEquals("USD",accountService.getAccountDetails(1234).getCurrencyCode());
		assertEquals("INR",accountService.getAccountDetails(1235).getCurrencyCode());
		assertTrue(accountService.getAccountDetails(1234).getBalance().compareTo(new BigDecimal(3000.00)) == 0);
		assertTrue(accountService.getAccountDetails(1235).getBalance().compareTo(new BigDecimal(642190.002441)) == 0);
    }
	
	@Test
    public void TransferInvalidAmountTest() throws AccountException {
		when(accRep.findAccountById(7777)).thenReturn(acctrffrom1);
		when(accRep.findAccountById(1234)).thenReturn(acctrfto1);
		AccountException ae = assertThrows(AccountException.class, () -> accountService.transferFunds(ftd2));
    	assertEquals("Transfer amount cannot be less than or equal to 0. Please give a valid transfer amount", ae.getMessage());
    }
	
	@Test
    public void TransferValidAmountInsufficientFundsTest() throws AccountException {
		when(accRep.findAccountById(1234)).thenReturn(acctrffrom3);
		when(accRep.findAccountById(1235)).thenReturn(acctrfto3);
		assertEquals("USD",accountService.getAccountDetails(1234).getCurrencyCode());
		assertEquals("INR",accountService.getAccountDetails(1235).getCurrencyCode());
		AccountException ae = assertThrows(AccountException.class, () -> accountService.transferFunds(ftd4));
    	assertEquals("Insufficient funds in the source account to execute transfer", ae.getMessage());
    }
}
