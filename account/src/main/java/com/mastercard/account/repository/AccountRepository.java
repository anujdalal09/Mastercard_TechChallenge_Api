package com.mastercard.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.mastercard.account.model.Account;

/**
 * Repository for Account
 * 
 * @author Anuj Dalal 01/26/20
 */

@Component
public interface AccountRepository extends JpaRepository<Account, Integer>{
	@Query(value= "SELECT * FROM account where account_num = :account_num", nativeQuery = true)
	Account findAccountById(Integer account_num);
}
