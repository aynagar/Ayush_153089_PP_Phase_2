package com.cg.mypaymentapp.beans;






public class Customer 
{
	
	private String name;
	
	
	private String mobileNo;
	
	private Wallet wallet;
	
	private Transactions transactions;
	
	
	
	public Customer() 
	{
		
	}
	
	public Customer(String name, String mobileNo, Wallet wallet, Transactions transactions) {
		this.name=name;
		this.mobileNo=mobileNo;
		this.wallet=wallet;
		this.transactions = transactions;
}
	public Transactions getTransactions() {
		return transactions;
	}

	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	@Override
	public String toString() {
		return "Customer name=" + name + ", mobileNo=" + mobileNo
				 + wallet;
	}
	
	
}
