package com.revature.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revature.model.Account;

public interface AccountRepo extends JpaRepository<Account, Integer> {
	Account findByUsernameIgnoreCase(String username);
	Account findByEmailIgnoreCase(String email);
	Account findById(int id);
	Account findByPhoneNumberIgnoreCase(String phoneNumber);
	List<Account> findAllByOrderByBalanceDesc();
}
