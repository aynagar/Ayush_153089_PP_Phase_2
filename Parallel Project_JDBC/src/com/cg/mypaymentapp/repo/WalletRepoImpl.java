package com.cg.mypaymentapp.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;









import com.cg.mypaymentapp.DBUtil.WalletDBUtil;
import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InvalidInputException;

public class WalletRepoImpl implements WalletRepo 
{
	
	private Map<String, Customer> data; 
   
    private String details = null;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Customer customer;
    
	public WalletRepoImpl()
	{
		data = new HashMap<String, Customer>();
	    customer = new Customer();
	}

	public WalletRepoImpl(Map<String, Customer> data) 
	{
		super();
		this.data = data;
	}
	
	

	
	
	@Override
	public boolean save(Customer customer) 
	{
		try( Connection con = WalletDBUtil.getConnection()) 
		{
			try
			{
		     
		    con.setAutoCommit(false);
			PreparedStatement pstm = con.prepareStatement("insert into WalletCustomer values(?,?,?)");
			pstm.setString(1, customer.getName());
			pstm.setString(2, customer.getMobileNo());
			pstm.setBigDecimal(3, customer.getWallet().getBalance());
			
			pstm.executeQuery();
			
			PreparedStatement pstm1 = con.prepareStatement("insert into Transactions values(?,?)");
			pstm1.setString(1, customer.getMobileNo());
			Date dateOfTransaction = new Date();
			details = "Wallet with mobile number: "+customer.getMobileNo()+" created with on "+dateFormat.format(dateOfTransaction)+" with Wallet balance: "+customer.getWallet().getBalance();
			pstm1.setString(2, details);
			pstm1.execute();
			
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Customer findOne(String mobileNo) 
	{
		customer = null;
		try( Connection con = WalletDBUtil.getConnection()) 
		{
			try
			{
		     
		    con.setAutoCommit(false);
			PreparedStatement pstm = con.prepareStatement("select * from WalletCustomer where c_mobile_no=?");
			pstm.setString(1, mobileNo);
			ResultSet res = pstm.executeQuery();
			if(res.next()==false)
				throw new InvalidInputException("No customer with mobile number "+ mobileNo);
			customer = new Customer();
			customer.setName(res.getString(1));
			customer.setMobileNo(res.getString(2));
			Wallet wallet = new Wallet(res.getBigDecimal(3));
			customer.setWallet(wallet);
			
			
			}catch(Exception e) {
				con.rollback();
				e.printStackTrace();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return customer;
	}

	
	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		customer = null;
		try( Connection con = WalletDBUtil.getConnection()) 
		{
			try
			{

				con.setAutoCommit(false);
				
		        PreparedStatement pstm = con.prepareStatement("select * from WalletCustomer where c_mobile_no=?");
			    pstm.setString(1, mobileNo);
			    ResultSet res = pstm.executeQuery();
			
			    if(res.next()==false)
				throw new InvalidInputException("No customer with mobile number "+ mobileNo);
			    customer = new Customer();
			    customer.setName(res.getString(1));
				customer.setMobileNo(res.getString(2));
				 BigDecimal bal = res.getBigDecimal(3).add(amount);
				Wallet wallet = new Wallet(bal);
				customer.setWallet(wallet);
			   
			   
			
			
			
			    PreparedStatement pstm1 = con.prepareStatement("update WalletCustomer set c_wallet_bal=? where c_mobile_no=?");
			    pstm1.setBigDecimal(1, bal );
			    pstm1.setString(2, mobileNo);
			    pstm1.execute();
			
			    
			    PreparedStatement pstm2 = con.prepareStatement("insert into Transactions values(?,?)");
				pstm2.setString(1, customer.getMobileNo());
				Date dateOfTransaction = new Date();
				details = "Wallet with mobile number: "+customer.getMobileNo()+" credited with "+ amount+" on "+dateFormat.format(dateOfTransaction)+" with Updated balance: "+customer.getWallet().getBalance();
				pstm2.setString(2, details);
				pstm2.execute();
			
			
			}catch(Exception e) {
				con.rollback();
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
			
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		customer = null;
		try( Connection con = WalletDBUtil.getConnection()) 
		{
			try
			{
		     
				
                con.setAutoCommit(false);
				
		        PreparedStatement pstm = con.prepareStatement("select * from WalletCustomer where c_mobile_no=?");
			    pstm.setString(1, mobileNo);
			    ResultSet res = pstm.executeQuery();
			
			    if(res.next()==false)
				throw new InvalidInputException("No customer with mobile number "+ mobileNo);
			    customer = new Customer();
			    customer.setName(res.getString(1));
				customer.setMobileNo(res.getString(2));
				BigDecimal bal = res.getBigDecimal(3).subtract(amount);
				Wallet wallet = new Wallet(bal);
				customer.setWallet(wallet);
			   
			    PreparedStatement pstm1 = con.prepareStatement("update WalletCustomer set c_wallet_bal=? where c_mobile_no=?");
			    pstm1.setBigDecimal(1, bal );
			    pstm1.setString(2, mobileNo);
			    pstm1.execute();
			
			    
			    PreparedStatement pstm2 = con.prepareStatement("insert into Transactions values(?,?)");
				pstm2.setString(1, customer.getMobileNo());
				Date dateOfTransaction = new Date();
				details = "Wallet with mobile number: "+customer.getMobileNo()+" debited with "+ amount+" on "+dateFormat.format(dateOfTransaction)+" with Updated balance: "+customer.getWallet().getBalance();
				pstm2.setString(2, details);
				pstm2.execute();
			
			
			}catch(Exception e) {
				con.rollback();
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}


	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo,
			BigDecimal amount) {
		customer = null;
		try( Connection con = WalletDBUtil.getConnection()) 
		{
			try
			{

				con.setAutoCommit(false);
				PreparedStatement pstm3 = con.prepareStatement("select * from WalletCustomer where c_mobile_no=?");
			    pstm3.setString(1, sourceMobileNo);
			    ResultSet res1 = pstm3.executeQuery();
			
			    if(res1.next()==false)
				throw new InvalidInputException("No customer with mobile number "+ sourceMobileNo);
			    customer = new Customer();
			    customer.setName(res1.getString(1));
				customer.setMobileNo(res1.getString(2));
				BigDecimal bal1 = res1.getBigDecimal(3).subtract(amount);
				Wallet wallet = new Wallet(bal1);
				customer.setWallet(wallet);
			   
			    PreparedStatement pstm4 = con.prepareStatement("update WalletCustomer set c_wallet_bal=? where c_mobile_no=?");
			    pstm4.setBigDecimal(1, customer.getWallet().getBalance() );
			    pstm4.setString(2, sourceMobileNo);
			    pstm4.execute();
			
			    
			    PreparedStatement pstm5 = con.prepareStatement("insert into Transactions values(?,?)");
				pstm5.setString(1, customer.getMobileNo());
				Date dateOfTransaction = new Date();
				details = "Wallet with mobile number: "+customer.getMobileNo()+" has Fund transfer of "+ amount+" to mobile number "+targetMobileNo+" on "+dateFormat.format(dateOfTransaction)+" with Updated balance: "+bal1;
				pstm5.setString(2, details);
				pstm5.execute();
			
			    customer = new Customer();
			    customer = findOne(sourceMobileNo);
				
		       
				details = null;
				
				 PreparedStatement pstm = con.prepareStatement("select * from WalletCustomer where c_mobile_no=?");
				    pstm.setString(1, targetMobileNo);
				    ResultSet res = pstm.executeQuery();
				
				    if(res.next()==false)
					throw new InvalidInputException("No customer with mobile number "+ targetMobileNo);
				    customer = new Customer();
				    customer.setName(res.getString(1));
					customer.setMobileNo(res.getString(2));
					BigDecimal bal = res.getBigDecimal(3).add(amount);
					wallet = new Wallet(bal);
					customer.setWallet(wallet);
				   
				   
				    PreparedStatement pstm1 = con.prepareStatement("update WalletCustomer set c_wallet_bal=? where c_mobile_no=?");
				    pstm1.setBigDecimal(1, bal );
				    pstm1.setString(2, targetMobileNo);
				    pstm1.execute();
				
				    
				    PreparedStatement pstm2 = con.prepareStatement("insert into Transactions values(?,?)");
					pstm2.setString(1, customer.getMobileNo());
					dateOfTransaction = new Date();
					details = "Wallet with mobile number: "+customer.getMobileNo()+" has Fund transfer of "+ amount+" from mobile number "+sourceMobileNo+" on "+dateFormat.format(dateOfTransaction)+" with Updated balance: "+bal;
					pstm2.setString(2, details);
					pstm2.execute();
				
				customer = new Customer();
				customer.setName(res1.getString(1));
				customer.setMobileNo(res1.getString(2));
				wallet = new Wallet(bal1);
				customer.setWallet(wallet);
			    
			}catch(Exception e) {
				con.rollback();
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
		
	}

	@Override
	public Transactions getTransactionDetails(String mobileNo) 
	{
		Transactions transactions = new Transactions();
		try( Connection con = WalletDBUtil.getConnection()) 
		{
			try
			{

				con.setAutoCommit(false);
				
		        PreparedStatement pstm = con.prepareStatement("select transaction_details from Transactions where mobile_no=?");
			    pstm.setString(1, mobileNo);
			    ResultSet res = pstm.executeQuery();
			    
			    if(res.next()==false)
			    	throw new InvalidInputException("No transactions is available for the number "+mobileNo);
			    	
			    transactions.getTransactionDetails().add(res.getString(1));
			    while(res.next())
			   {
			    	transactions.getTransactionDetails().add(res.getString(1));   
			   }
			    		
			}catch(Exception e) {
				con.rollback();
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return transactions;
	}
	

	
	

	

}
