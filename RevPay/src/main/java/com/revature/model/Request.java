package com.revature.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
public class Request {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "requester", nullable=false)
	private String reqUsername;
	@Column(name = "determiner", nullable=false)
	private String detUsername;
	@Column(nullable=false)
	private boolean resolved;
	@Column(nullable=false)
	private long amount;
	
	public int getRequestId() {return this.id;}
	public void setRequestId(int id) {this.id = id;}
	public String getReqUsername() {return this.reqUsername;}
	public void setReqUsername(String reqUsername) {this.reqUsername = reqUsername;}
	public String getDetUsername() {return this.detUsername;}
	public void setDetUsername(String detUsername) {this.detUsername = detUsername;}
	public boolean getResolved() {return this.resolved;}
	public void setResolved(boolean resolved) {this.resolved = resolved;}
	public long getAmount() {return this.amount;}
	public void setAmount(long amount) {this.amount = amount;}
	
	public Request() {
		
	}
	
	//used to create new request
	public Request(String reqUsername, String detUsername, boolean resolved, long amount) {
		this.reqUsername = reqUsername;
		this.detUsername = detUsername;
		this.resolved = resolved;
		this.amount = amount;		
	}
	
	//used to find existing request
	public Request(int id, String reqUsername, String detUsername, boolean resolved, long amount) {
		this.id = id;
		this.reqUsername = reqUsername;
		this.detUsername = detUsername;
		this.resolved = resolved;
		this.amount = amount;		
	}
}
