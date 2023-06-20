package com.revature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.model.Account;
import com.revature.model.Request;
import com.revature.service.AccountService;

import Exception.InsufficientFundsException;
import Exception.InvalidCredential;
import Exception.RequestNotFoundException;
import Exception.UserNotFoundException;

@CrossOrigin("*")
@RestController
public class Controller {
	 AccountService accountService;
	@Autowired
	public Controller(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> postUser(@RequestBody Account account){
		try {
			accountService.createAccount(account);
			return ResponseEntity.ok("User successfully registered");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Account account){
		try {
			Account login = accountService.login(account.getUsername(), account.getPassword());
			return ResponseEntity.ok(login);
		}catch(InvalidCredential e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/requests/{username}")
	public List<Request> getRequestsByUsername(@PathVariable String username){
		try {
			return accountService.getRequests(username);
		}catch(UserNotFoundException e){
			return null;
		}
	}
	
	@PostMapping("/request/send-money")
	public ResponseEntity<?> sendMoney(@RequestBody Request request){
		try {
			accountService.sendMoney(request.getDetUsername(), request.getReqUsername(), request.getAmount());
			return ResponseEntity.ok().body("Transfer successful");
		}catch(InsufficientFundsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}catch(UserNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/request/send-request")
	public ResponseEntity<?> sendRequest(@RequestBody Request request){
		try {
			accountService.requestMoney(request);
			return ResponseEntity.status(200).body("Request successfully sent");
		}catch(UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/request/accept")
	public ResponseEntity<?> acceptRequest(@RequestBody Request request){
		try {
			accountService.acceptRequest(request);
			return ResponseEntity.status(HttpStatus.OK).body("Request accepted");
		}catch(RequestNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/request/deny")
	public ResponseEntity<?> denyRequest(@RequestBody Request request){
		try {
			accountService.denyRequest(request);
			return ResponseEntity.status(HttpStatus.OK).body("Request denied");
		}catch(RequestNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
