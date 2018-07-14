package com.cg.mypaymentapp.repo;

import java.math.BigDecimal;


import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;

public interface WalletRepo 
{
	//public abstract Customer getStudentById(int id);

    public boolean save(Customer customer);
	
	public Customer findOne(String mobileNo);

	public Customer fundTransfer (String sourceMobileNo,String targetMobileNo, BigDecimal amount);
	
	public Customer depositAmount (String mobileNo,BigDecimal amount );
	
	public Customer withdrawAmount(String mobileNo, BigDecimal amount);

	public Transactions getTransactionDetails(String mobileNo);
	

	
}
