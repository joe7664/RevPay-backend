package com.revature.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.model.Account;
import com.revature.model.Request;
import com.revature.repo.AccountRepo;
import com.revature.repo.RequestRepo;

import Exception.InsufficientFundsException;
import Exception.InvalidCredential;
import Exception.RequestNotFoundException;
import Exception.UserNotFoundException;

@Service
public class AccountService {
	private AccountRepo accountRepo;
	private RequestRepo requestRepo;
	
	public AccountService(AccountRepo accountRepo, RequestRepo requestRepo) {
		this.accountRepo = accountRepo;
		this.requestRepo = requestRepo;
	}
	
	public Account createAccount(Account account) {
		Account existingAccount = accountRepo.findByUsernameIgnoreCase(account.getUsername());
		if(existingAccount != null) {
			throw new InvalidCredential("An account with that username already exists");
			
		}
		if(account.getPassword().length()<8) {
			throw new InvalidCredential("Password must be at least 8 characters long");//password too short
		}
		return accountRepo.save(account);
	}
	
	public Account login(String username, String password) {
		Account existingAccount = accountRepo.findByUsernameIgnoreCase(username);
		if(existingAccount != null) {
			if(existingAccount.getPassword().equals(password)) {
				return existingAccount;//login successful
			}else {
				throw new InvalidCredential("Invalid password, please verify your input and try again");
			}
		}
		else {
			throw new UserNotFoundException("Username does not exist");
		}
	}
	
	public boolean sendMoney(String senderUsername, String receiver, long amount) {
		Account sender = accountRepo.findByUsernameIgnoreCase(senderUsername);
		Account existingAccount = null;
		if(receiver.contains("@") && (receiver.contains(".com") || receiver.contains(".net") || receiver.contains(".org"))) {
			existingAccount = accountRepo.findByEmailIgnoreCase(receiver);
		}else if(receiver.matches("\\d+")) {
			existingAccount = accountRepo.findByPhoneNumberIgnoreCase(receiver);
		}else {
			existingAccount = accountRepo.findByUsernameIgnoreCase(receiver);
		}
		if(existingAccount == null) {
			throw new UserNotFoundException("This user does not exist, check your input and try again");
		}
		if(sender.getBalance()<amount) {
			throw new InsufficientFundsException("Insufficuent funds for transfer");
		}
		sender.setBalance(sender.getBalance()-amount);
		existingAccount.setBalance(existingAccount.getBalance()+amount);
		accountRepo.save(sender);
		accountRepo.save(existingAccount);
		return true;//successful transfer
	}
	
	public List<Request> getRequests(String username){
		Account existingAccount = accountRepo.findByUsernameIgnoreCase(username);
		if(existingAccount == null) {
			throw new UserNotFoundException("Could not find this user");
		}else {
			return existingAccount.getRequests();
		}
	}
	
	public boolean requestMoney(Request request) {
		Request newRequest = new Request(request.getReqUsername(), request.getDetUsername(), false, request.getAmount());
		String reqUsername = newRequest.getReqUsername();//requester username
		String detIdentifier = newRequest.getDetUsername();//determiner identifier
		
		//find accounts of requester and determiner
		Account requester = accountRepo.findByUsernameIgnoreCase(reqUsername);
		Account determiner = null;
		if(detIdentifier.contains("@") && (detIdentifier.contains(".com") || detIdentifier.contains(".net") || detIdentifier.contains(".org"))) {
			determiner = accountRepo.findByEmailIgnoreCase(detIdentifier);
		}else if(detIdentifier.matches("\\d+")) {
			determiner = accountRepo.findByPhoneNumberIgnoreCase(detIdentifier);
		}else {
			determiner = accountRepo.findByUsernameIgnoreCase(detIdentifier);
		}
		if(determiner == null) {
			throw new UserNotFoundException("We can't find an account with that identifier, please check your input and try again");
		}
		
		//get determiner username after finding account
		newRequest.setDetUsername(determiner.getUsername());
		Request savedRequest = requestRepo.save(newRequest);//BREAKS HERE WITH INDEX/PRIMARY KEY VIOLATION
		//add request to list of user sending request
		requester.getRequests().add(savedRequest);
		//add request to list of user receiving request
		determiner.getRequests().add(savedRequest);
		accountRepo.save(requester);
		accountRepo.save(determiner);
		return true;//successful request
	}
	
	public boolean acceptRequest(Request request) {
		String reqName = request.getReqUsername();
		String detName = request.getDetUsername();
		Account requester = accountRepo.findByUsernameIgnoreCase(reqName);
		Account determiner = accountRepo.findByUsernameIgnoreCase(detName);
		if(determiner.getRequests().contains(request)) {
			//get list of requests for determiner, get request that matches, resolve the request
			determiner.getRequests().get(determiner.getRequests().indexOf(request)).setResolved(true);
		}else {
			throw new RequestNotFoundException("Request not found for determiner");//error happened, request doesnt exist
		}
		if(requester.getRequests().contains(request)) {
			//get list of requests for requester, get request that matches, resolve the request
			requester.getRequests().get(requester.getRequests().indexOf(request)).setResolved(true);
		}else {
			throw new RequestNotFoundException("Request not found for requester");//error happened, request doesnt exist
		}
		accountRepo.save(requester);
		accountRepo.save(determiner);
		//send money after requests on both sides confirmed and resolved
		sendMoney(detName, reqName, request.getAmount());
		return true;//requests updated and money sent
	}
	
	public boolean denyRequest(Request request) {
		String reqName = request.getReqUsername();
		String detName = request.getDetUsername();
		Account requester = accountRepo.findByUsernameIgnoreCase(reqName);
		Account determiner = accountRepo.findByUsernameIgnoreCase(detName);
		if(determiner.getRequests().contains(request)) {
			//get list of requests for determiner, get request that matches, resolve the request
			determiner.getRequests().get(determiner.getRequests().indexOf(request)).setResolved(true);
		}else {
			throw new RequestNotFoundException("Request not found for determiner");//error happened, request doesnt exist
		}
		if(requester.getRequests().contains(request)) {
			//get list of requests for requester, get request that matches, resolve the request
			requester.getRequests().get(requester.getRequests().indexOf(request)).setResolved(true);
		}else {
			throw new RequestNotFoundException("Request not found for requester");//error happened, request doesnt exist
		}
		accountRepo.save(requester);
		accountRepo.save(determiner);
		return true;
	}
}
