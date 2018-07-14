package com.cg.mypaymentapp.pl;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import java.util.Scanner;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class Client
{
	private Scanner console = new Scanner(System.in);
	private Customer customer;
	
	//private Map<String, Customer> data;
	
	private WalletService serviceObject;
	private String customerName = null;
	private String customerMobileNo1 = null;
	private String customerMobileNo2 = null;
	private BigDecimal amount1 = null;
	
	
	
	
	public Client() 
	{
		customer = new Customer();
		serviceObject = new WalletServiceImpl();
	
		
	}
	public void menu()
	{
		int choice = 0;
		
		System.out.println("-------Welcome to Wallet-------");
		System.out.println();
		System.out.println("1) Create Account.");
		System.out.println("2) Show Balance.");
		System.out.println("3) Deposit.");
		System.out.println("4) Withdraw.");
		System.out.println("5) Fund Transfer.");
		System.out.println("6) Show Transaction.");
		System.out.println();
		System.out.print("Enter your choice: ");
		
		choice = console.nextInt();
		
		switch (choice) {
		case 1:
			//data = new HashMap<String, Customer>();
			
			System.out.println("Enter the Details: ");
			System.out.println();
			System.out.print("Enter your Name: ");
			customerName = console.next();
			System.out.print("Enter your Mobile Number: ");
			customerMobileNo1 = console.next();
			System.out.print("Add balance: ");
			amount1 = console.nextBigDecimal();
			customer = serviceObject.createAccount(customerName, customerMobileNo1, amount1);
			if(customer!=null)
			{
			System.out.println("Your account is created.");	
			System.out.println("With Account Details: ");
			
			
			System.out.println(customer);
			
			}else
				System.out.println("Your account is not created.");
			break;
		
		case 2:
			customerMobileNo1 = null;
			System.out.println();
			System.out.print("Enter your registered mobile Number: ");
			customerMobileNo1 = console.next();
			customer = serviceObject.showBalance(customerMobileNo1);
			System.out.println("Name: "+ customer.getName());
			System.out.println("Your account balance: "+ customer.getWallet().getBalance());
			break;
		
		case 3:
			customerMobileNo1 = null;
			System.out.print("Enter your registered mobile Number: ");
			customerMobileNo1 = console.next();
			System.out.print("Enter the Amount to Deposit: ");
			amount1 = console.nextBigDecimal();
			customer = serviceObject.depositAmount(customerMobileNo1, amount1);
			
			
			
			System.out.println("Amount " + amount1 + " deposited in the Account with customer name : " + customer.getName());
			System.out.println("Updated balance is " + customer.getWallet().getBalance());
			
			break;
		
		case 4:
			customerMobileNo1 = null;
			System.out.print("Enter your registered mobile Number: ");
			customerMobileNo1 = console.next();
			System.out.print("Enter the Amount to Withdraw: ");
			amount1 = console.nextBigDecimal();
			customer = serviceObject.withdrawAmount(customerMobileNo1, amount1);
			
			
			
			System.out.println("Amount " + amount1 + " withdrawn from the Account with customer name : " + customer.getName());
			System.out.println("Updated balance is " + customer.getWallet().getBalance());
			break;
		
		case 5:
			System.out.print("Enter the mobile number from which fund need to be transfer: ");
			customerMobileNo1 = console.next();
			System.out.print("Enter the mobile number to which fund need to be transfer: ");
			customerMobileNo2 = console.next();
			System.out.print("Enter the Amount to Transfer: ");
			amount1 = console.nextBigDecimal();
			customer = new Customer();
			customer = serviceObject.fundTransfer(customerMobileNo1, customerMobileNo2, amount1);
			
			
			
			System.out.println("Amount " + amount1 + " Transferred from the Account with customer name : " + customer.getName());
			System.out.println("Updated balance is " + customer.getWallet().getBalance());
			break;
		case 6:
			Transactions transactions = new Transactions();
			List<String> transactionDetails1 = new ArrayList<String>();
			System.out.print("Enter the Mobile number: ");
			customerMobileNo1 = console.next();
			
			transactions = serviceObject.getTransaction(customerMobileNo1);
			transactionDetails1 = transactions.getTransactionDetails();
			if(transactionDetails1!=null)
			{
			Iterator<String> it = transactionDetails1.iterator();
			while(it.hasNext())
				System.out.println(it.next());
			}else
				System.out.println("Unable to find any transactions.");
			break;
		default:
		       System.out.println("Please, enter the right choice!!!");
			break;
		}
		
	}
    public static void main(String[] args)
    {
    	Scanner console = new Scanner(System.in);
		Client client = new Client();
		
		String choice = "yes";
		do
		{
			if(choice.equalsIgnoreCase("yes"))
			{
			client.menu();
			System.out.print("Do you want to continue(yes/no): ");
			choice = console.next();
			}
				
			
		}while(choice.equalsIgnoreCase("yes"));
		System.out.println("Thank you for using it.");
		System.exit(0);
		
		console.close();
	}
}
