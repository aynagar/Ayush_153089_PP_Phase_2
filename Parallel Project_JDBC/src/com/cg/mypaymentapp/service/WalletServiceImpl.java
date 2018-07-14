package com.cg.mypaymentapp.service;


import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService 
{

	private WalletRepo repo;
	private Customer customer1;
	private Customer customer2;
	private Wallet wallet;
	private Map<String,Customer> data1;
	private BigDecimal updatedBalance1;
	private BigDecimal updatedBalance2;
	
	
	public WalletServiceImpl() 
	{
		repo = new WalletRepoImpl();
		
		data1 = new HashMap<String, Customer>();
	}
	public WalletServiceImpl(Map<String, Customer> data)
	{
		repo= new WalletRepoImpl(data);
	}
	public WalletServiceImpl(WalletRepo repo) 
	{
		super();
		this.repo = repo;
	}

	

	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) 
	{
		boolean check = false;
		wallet = new Wallet(amount);
	
		customer1 = new Customer(name, mobileNo, wallet, new Transactions() );
		boolean ch = isValid(customer1);
		check = repo.save(customer1);
		if (check && ch) 
		{
			
			return customer1;
		} else 
		{
			System.out.println("Data not saved.");
			return null;  
		}
		
	}

	@Override
	public Customer showBalance(String mobileNo) 
	{
		
		customer1 = repo.findOne(mobileNo);
		if(customer1 != null)
		{
			
			return customer1;
		}
		else
			throw new InvalidInputException("No such mobile number exists.");
	}
	
	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) 
	{
		if(mobileNo==null||amount.compareTo(BigDecimal.ZERO)<=0||mobileNo.trim().isEmpty())
			throw new InvalidInputException("Inputs cannot be empty.");
		
		
		customer1=new Customer() ;
		customer1 = repo.depositAmount(mobileNo, amount);
		if(customer1 != null)
		{
			
			return customer1;
		}
		else
			throw new InvalidInputException("No such mobile number exists.");
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) 
	{
		if(mobileNo==null||amount.compareTo(BigDecimal.ZERO)<=0||mobileNo.trim().isEmpty())
			throw new InvalidInputException("Inputs cannot be empty.");
		
		customer1=new Customer() ;
		customer1 = repo.findOne(mobileNo);
		if(customer1 != null)
		{
			updatedBalance1 = customer1.getWallet().getBalance();
			int comp = updatedBalance1.intValue()-amount.intValue();
	        
			if(comp>0)
			{
				customer1 = repo.withdrawAmount(mobileNo, amount);
		
			return customer1;
			}
			else
				throw new InsufficientBalanceException("Amount to be withdrawn is greater than the balance.");
			
		}
		else
			throw new InvalidInputException("No such mobile number exists.");
	}
	
	
	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) 
	{
		if(sourceMobileNo==null||amount.compareTo(BigDecimal.ZERO)<=0||sourceMobileNo.trim().isEmpty()||targetMobileNo.trim().isEmpty()||targetMobileNo==null)
			throw new InvalidInputException("Inputs cannot be empty.");
		
		customer1=new Customer() ;
		customer2=new Customer() ;
		
		customer1 = repo.findOne(sourceMobileNo);
		customer2 = repo.findOne(targetMobileNo);
		
		if(customer1 != null && customer2 != null)
		{
			updatedBalance1 = customer1.getWallet().getBalance();
			updatedBalance2 = customer2.getWallet().getBalance();
			int comp = updatedBalance1.intValue()-amount.intValue();
	        
			if(comp>0)
			{
				customer1 =new Customer();
				customer1 = repo.fundTransfer(sourceMobileNo, targetMobileNo, amount);
				 System.out.println(customer1.getWallet().getBalance());
			
			return customer1;
			}
			else
				throw new InsufficientBalanceException("Amount to be withdrawn is greater than the balance.");
			
		}
		else
			throw new InvalidInputException("No such mobile number exists.");
		
	}

	
	@Override
	public Transactions getTransaction(String mobileNo) 
	{
		Transactions transactions = new Transactions();
		transactions = repo.getTransactionDetails(mobileNo);
		if(transactions!=null)	
		return transactions;
		else
		    throw new InvalidInputException("No such mobile number exists.");
	}
	
	
	@Override
	public boolean isValid(Customer customer) throws InvalidInputException, InsufficientBalanceException
	{
	if(customer.getName() == null || customer.getName() == "")
	{
		throw new InvalidInputException("User Name cannot be null or empty.");
		
	}
	if(customer.getMobileNo() == null || customer.getMobileNo() == "")
		throw new InvalidInputException("User Mobile Number cannot be null or empty.");
	
	BigDecimal value = BigDecimal.ZERO;
	
	if(customer.getWallet().getBalance() == null ||customer.getWallet().getBalance().compareTo(value)==-1)
		throw new InvalidInputException("Wallet Balance cannot be Null.");
	
	if(!(customer.getName().matches("^([A-Z]{1}\\w+)$")))
	{
		throw new InvalidInputException("Invalid Name");
	}
	if(!(customer.getMobileNo().length()==10))
		throw new InvalidInputException("Mobile Number is not 10 digit.");
	
	if(!(customer.getMobileNo().matches("^[7-9]{1}[0-9]{9}$")))
	{
		throw new InvalidInputException("Invalid Number");
	}
	
		return true;
		
	}

}
