package com.cg.mypaymentapp.beans;


import java.util.ArrayList;


public class Transactions 
{
    
     ArrayList<String> transactionDetails = new ArrayList<String>();


	public Transactions() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Transactions(ArrayList<String> transactionDetails) {
		super();
		this.transactionDetails = transactionDetails;
	}


	public ArrayList<String> getTransactionDetails() {
		return transactionDetails;
	}


	public void setTransactionDetails(ArrayList<String> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	

	
   
	
     
     
     
}
