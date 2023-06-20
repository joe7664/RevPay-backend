package com.revature.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
public class Account {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	private String username;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private String phoneNumber;
	@Column(nullable=false)
	private long balance = 0;
//	@Column(name = "requests")
//	private List<Request> requests;
	
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	public String getPhoneNumber() {return phoneNumber;}
	public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
	public long getBalance() {return balance;}
	public void setBalance(long balance) {this.balance = balance;}
	public List<Request> getRequests(){return this.requests;}
	public void setRequests(List<Request> requests) {this.requests = requests;}
	
	public Account() {
		
	}
	
	public Account(String username, String password, String email, String phoneNumber, long balance, List<Request> requests) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.balance = balance;
		this.requests = requests;
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	List<Request> requests;
}
