import java.util.Scanner;
import java.util.Calendar;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
public class HW08 {

	public static void main(String[] args) throws IOException, InvalidMenuSelectionException{
		String a = "testCases.txt";
		File tc = new File(a);
		Scanner input = new Scanner(tc);
		
		File po = new File("Output.txt");
		PrintWriter receipt = new PrintWriter(po);
		
		Bank bank = new Bank();
		
		int numAccts = readAccts(bank);
		printAccounts(bank, receipt, numAccts);
	
		boolean loop = true;		
		do {
			
			try {
			
			menu();
			numAccts = bank.getNumberOfAccounts();
			
			char option = input.next().charAt(0);
			switch(option){
				case 'q':
				case 'Q':
					printAccounts(bank, receipt, numAccts);
					System.out.println("\tQuitting Bank Program: ");
					loop = false;
					break;
				case 'w':
				case 'W':
					withdrawal(bank, receipt, input);
					break;
				case 'd':
				case 'D':
					deposit(bank, receipt, input);
					break;
				case 'c':
				case 'C':
					clearCheck(bank, receipt, input);
					break;
				case 'n':
				case 'N':
					newAcct(bank, receipt, input, numAccts);
					break;
				case 'i':
				case 'I':
					accountInfo(bank, numAccts, receipt, input);
					break;
				case 'h':
				case 'H':
					accountInfoWithHistory(bank, numAccts, receipt, input);
					break;
				case 'S':
				case 's':
					closeAccount(bank, receipt, input);
					break;
				case 'R':
				case 'r':
					reOpenAccount(bank, receipt, input);
					break;
				case 'b':
				case 'B':
					balance(bank, receipt, input);
					break;
				case 'x':
				case 'X':
					deleteAcct(bank, receipt, input);
					break;
				default:
	                throw new InvalidMenuSelectionException(option);
	                
				}       
			}catch (InvalidMenuSelectionException ex) {
				receipt.println(ex.getMessage());
				receipt.println();
			} 		
		}while (loop);
		
		input.close();
		receipt.close();
	}
	
	/*
	 The Input is an object from Bank class which contains the array of accounts, 
	 	this also includes the number of accounts
	 The process is that the a scanner is used to take the account information from
	 	text file accounts.txt and put it into the corresponding account type, then the account subclass object is
	 	put into an array list of account objects, which works via polymorphism.
	 The output is the actual amount accounts in the banks account array, decided by 
	 	the array list of accounts size method.
	 */
	public static int readAccts(Bank bank) throws IOException {
		File file = new File("InititalAccounts.txt");
		Scanner readFrom = new Scanner(file);
		Account fillUp;
		
		while (readFrom.hasNext()) {
			
			String last = readFrom.next();
			String first = readFrom.next();
			String ssn = readFrom.next();
			int accountNumber = readFrom.nextInt();
			String type = readFrom.next();
			double balance = readFrom.nextDouble();
			
			String maturityDate;
			Calendar date = Calendar.getInstance();
			
			if (type.equals("CD")) {
				maturityDate = readFrom.next();
			}else maturityDate = "N/A";
			
			
			if (!maturityDate.equals("N/A")){
				date = Calendar.getInstance();
				date.clear();
				
				String[] a = maturityDate.split("/");
				int[] b = new int[3];
				
				b[0] = Integer.parseInt(a[0]);
				b[1] = Integer.parseInt(a[1]);
				b[2] = Integer.parseInt(a[2]);

				date.set(Calendar.MONTH, b[0] - 1);
				date.set(Calendar.DAY_OF_MONTH, b[1]);
				date.set(Calendar.YEAR, b[2]);
						
			}
			
			Name name = new Name(first, last);
			Depositor depositer = new Depositor(ssn, name);
			
			if (type.equals("Savings")){
				fillUp = new SavingsAccount(depositer, accountNumber, type, balance, true, 0);
			}else if (type.equals("Checking")){
				fillUp = new CheckingAccount(depositer, accountNumber, type, balance, true, 0);
			}else{
				fillUp = new CDAccount(depositer, accountNumber, type, balance, true, 0, date);
			}			
			
			Calendar present = Calendar.getInstance();
			TransactionTicket ticket = new TransactionTicket(accountNumber, present, "New Account", balance, 0);
			TransactionReceipt getBack = new TransactionReceipt(ticket, true, "N/A", type, balance, balance, maturityDate);
			
			fillUp.addTransaction(getBack);
			
			bank.addAccount(fillUp);
			
		}
		bank.checkAllAccts();
		
		readFrom.close();
		return bank.getNumberOfAccounts();
	}
	
	/*
	 The Input is an object from the Bank class which contains the array of accounts, 
	 	the number of accounts, and the PrintWriter that leads to the output file.
	 The process is that the information from each account in the account array is printed to the output file by 
	 	using the to string method
	 There is no other output.
	 */
	public static void printAccounts(Bank bank, PrintWriter receipt, int numAccts) throws IOException {
		String header = String.format("\t\tCurrent Bank Accounts");
		String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
		receipt.println(header);
		receipt.println(rowTitle);
		
		for (int x = 0; x < numAccts; x++) {
			Account account  = bank.getAccount(x);
			receipt.println(account);
		}
		receipt.printf("\tAmount in All Savings Accounts:  $%11.2f", bank.getAllSv());
		receipt.println();
		receipt.printf("\tAmount in All Checking Accounts: $%11.2f", bank.getAllCh());
		receipt.println();
		receipt.printf("\tAmount in All CD Accounts: \t\t $%11.2f", bank.getAllCD());
		receipt.println();
		receipt.printf("\tAmount in All Accounts: \t\t $%11.2f", bank.getAllAmount());
		receipt.println();
		
		receipt.println();

	}
	
	/*
	Input is nothing
	The method prints messages out to the console
	The output is a series of prompts that tells the user all the choices they can makes in the program
	*/
	public static void menu(){
		System.out.println();
	    System.out.println("Select one of the following transactions: ");
	    System.out.println("\t----------------------------");
	    System.out.println("\t    List of Choices         ");
	    System.out.println("\t----------------------------");
	    System.out.println("\t     W(w) -- Withdrawal");
	    System.out.println("\t     D(d) -- Deposit");
	    System.out.println("\t     C(c) -- Clear Check");
	    System.out.println("\t     N(n) -- New Account");
	    System.out.println("\t     B(b) -- Balance Inquiry");
	    System.out.println("\t     I(i) -- Account Info");
	    System.out.println("\t     H(h) -- Account Info with Transaction History");
	    System.out.println("\t     S(s) -- Close an account");
	    System.out.println("\t     R(r) -- Reopen Account");
	    System.out.println("\t     X(x) -- Delete Account");
	    System.out.println("\t     Q(q) -- Quit");
	    System.out.println();
	    System.out.println("\tEnter your selection: ");
	}

	/*
	 The input is a bank object, and PrintWriter and scanner objects
	 The process is that the test cases file is read for the account requested for the balance, then a transcation tikcet
	 	is made to send to the bank class to it's balance method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void balance(Bank bank, PrintWriter receipt, Scanner input) throws IOException {
		System.out.println("\tEnter the account number: ");
		int account = input.nextInt();
		Calendar present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, present, "Balance", 0.00, 0);
		TransactionReceipt getBack = bank.getBalance(ticket);
		
		receipt.println(getBack);
		receipt.println();
	}
	
	/*
	 The input is a bank object, and PrintWriter and scanner objects
	 The process is that the test cases file is read for the account requested for the withdraw, then a transcation tikcet
	 	is made to send to the bank class to it's withdraw method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void withdrawal(Bank bank, PrintWriter receipt, Scanner input) throws IOException {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		int term = input.nextInt();
		
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Withdrawal", amount, term);
		TransactionReceipt gotBack = bank.makeWithdraw(ticket);
		receipt.println(gotBack);
		receipt.println();
	}
	
	/*
	 The input is a bank object, and PrintWriter and scanner objects
	 The process is that the test cases file is read for the account requested for the deposit, then a transcation tikcet
	 	is made to send to the bank class to it's deposit method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void deposit(Bank bank, PrintWriter receipt, Scanner input) throws IOException {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		int term = input.nextInt();
		
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Deposit", amount, term);
		TransactionReceipt gotBack = bank.makeDeposit(ticket);
		
		receipt.println(gotBack);
		receipt.println();
	}
		
	/*
	 The input is a bank object, and PrintWriter and scanner objects
	 The process is that the test cases file is read for the account requested for the deletion, then a transcation tikcet
	 	is made to send to the bank class to it's deletion method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void deleteAcct(Bank bank, PrintWriter receipt, Scanner input) throws IOException{
		System.out.println("\tEnter an account to delete: ");
		int deleteAcct = input.nextInt();
		Calendar present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(deleteAcct, present, "Delete Account", 0.00, 0);
		TransactionReceipt getBack = bank.deleteAccount(ticket);
		
		receipt.println(getBack);
		receipt.println();
	
	}
		
	/*
	 The Input is an object from the bank class which contains the array of accounts and number of accounts, it also
	 	receives a PrintWriter and scanner object.
	 The process is that the test cases file is scanned for the ssn of a user, then it used to locate
	 	all accounts with the ssn and if and else's are used to display the proper messages
	 	to the output file.
	 The output is all info in the output file.
	 */
	public static void accountInfo(Bank bank, int numAccts,PrintWriter receipt, Scanner input) throws IOException {
		System.out.println("\tEnter your Social Security Number: ");
		String ssn = input.next();
		int yes = 0;
		
		Calendar present = Calendar.getInstance();
		receipt.println(present.getTime());
		
		for (int x = 0; x < numAccts; x++) {
			String check = bank.getAccount(x).getDepositer().getSSN();
			if (check.equals(ssn))yes++;
		}
		if (yes == 0) {
			receipt.println("Transaction Requested: Account Info");
			receipt.println("Error: SSN " + ssn +" does not exist in our database: ");
			receipt.println();
		}else {
			receipt.println("Transaction Requested: Account Info");
			String header = String.format("\t\tCurrent Accounts With Your Matching SSN: ");
			String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
			receipt.println(header);
			receipt.println(rowTitle);
			for (int x = 0; x < numAccts; x++) {
				Account account  = bank.getAccount(x);
				String check = account.getDepositer().getSSN();
				if (check.equals(ssn))	receipt.println(account);
				
			}
			receipt.println();
		}
	}
		
	/*
	 The input is a bank class object, a PrintWriter, and a scanner object.
	 The process is that the bank object to extract the information under the ssns accounts.
	 The output is to the output file and the accounts under that ssn and its accounts plus those accounts transactions. 
   		if the ssn does not exist in the database than that is accounted for.
	 */
	public static void accountInfoWithHistory(Bank bank, int numAccts,PrintWriter receipt, Scanner input) throws IOException {
		System.out.println("\tEnter your Social Security Number: ");
		String ssn = input.next();
		int yes = 0;
		
		Calendar present = Calendar.getInstance();
		receipt.println();
		receipt.println(present.getTime());
		
		for (int x = 0; x < numAccts; x++) {
			String check = bank.getAccount(x).getDepositer().getSSN();
			if (check.equals(ssn))yes++;
		}
		if (yes == 0) {
			receipt.println("Transaction Requested: Account Info With Transaction History");
			receipt.println("Error: SSN " + ssn +" does not exist in our database: ");
			receipt.println();
		}else {
			receipt.println("Transaction Requested: Account Info with Transaction History");
			String header = String.format("\t\tCurrent Accounts With Your Matching SSN and their History: ");
			String rowTitle = String.format("Last Name:\tFirst Name:\tSSN:\t\tAccount Number:\tAccount Type:\tBalance:\tMaturity Date:");
			receipt.println(header);
			
			for (int x = 0; x < numAccts; x++) {
				
				Account account  = bank.getAccount(x);
				String check = account.getDepositer().getSSN();
				if (check.equals(ssn)) {
					receipt.println(rowTitle);
					receipt.println(account);
					
					receipt.println("-----Account Transactions: ");
					
					String transactionHeader = String.format("Date:\t\t\tTransaction:\tAmount:\t\tStatus:\tBalance:\tReason For Failure:");
					receipt.println(transactionHeader);
					
					int numTransactions = account.getAmountOfTransactions();
					
					for (int z = 0; z < numTransactions; z++) {
						TransactionReceipt soloHistory = account.getTransactionHistory(z);
						
						Calendar transDate = soloHistory.getTINfo().getDateOfTransaction();
						int transMonth = transDate.get(Calendar.MONTH) + 1;
						String transMonthString = "" + transMonth;
						if (transMonthString.length() != 2) transMonthString = "0" + transMonthString;
						
						
						int transDay = transDate.get(Calendar.DAY_OF_MONTH);
						String transDayString = "" + transDay;
						if (transDayString.length() != 2) transDayString = "0" + transDayString;

						int transYear = transDate.get(Calendar.YEAR);
						String transYearString = transMonthString + "/" + transDayString + "/" + transYear;

						
						receipt.printf("%-6s\t\t", transYearString);
						receipt.printf("%-12s\t", soloHistory.getTINfo().getTypeOfTransaction());
						receipt.printf("$%-8.2f", soloHistory.getTINfo().getAmountOfTransaction());
						receipt.printf("\t%4b", soloHistory.getFlag());
						receipt.printf("\t%-7.2f ", soloHistory.getPostBalance());
						
						String reasonTransaction = soloHistory.getReason();
						receipt.printf("\t%-7s", reasonTransaction);
						receipt.println();
				
					}
					receipt.println();
					
				}
			}
			receipt.println();
		}
	}
	
	/*
	 The input is a bank class object, a PrintWriter, and a scanner object.
	 The process is that the a transactionticket ticket is made and sent to the bank object to continue the transaction,
	 	and get a transaction receipt object.
	 The output is to the output file and depends on the transaction receipt object.
	 */
	public static void closeAccount(Bank bank, PrintWriter receipt, Scanner input) throws IOException {
		int account = input.nextInt();
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "Close Account", 0.0, 0);
		TransactionReceipt closeReceipt = bank.closeAccount(ticket);
		
		boolean results = closeReceipt.getFlag();
		String reason = closeReceipt.getReason();
		
		if(results == false) {
			 if (reason.contains("does not exist")) { 
				 receipt.println(ticket);
			     receipt.println("Error: " + reason);
			 }
			 else {
				 receipt.println(ticket);
				 receipt.println("Error: " + reason);
			 }
		}else {
			receipt.println(ticket);
			receipt.println("Your account has been closed, transactions will not go through unless it is reopened");
		} 
		receipt.println();
	}
	
	/*
	 The input is a bank class object, a PrintWriter, and a scanner object.
	 The process is that the a transactionticket ticket is made and sent to the bank object to continue the transaction,
	 	and get a transaction receipt object.
	 The output is to the output file and depends on the transaction receipt object.
	  */
	public static void reOpenAccount(Bank bank, PrintWriter receipt, Scanner input) throws IOException {
		int account = input.nextInt();
		Calendar Present = Calendar.getInstance();
		TransactionTicket ticket = new TransactionTicket(account, Present, "ReOpen Account", 0.0, 0);
		TransactionReceipt reOpenReceipt = bank.reOpenAccount(ticket);
		
		boolean results = reOpenReceipt.getFlag();
		String reason = reOpenReceipt.getReason();
		
		if(results == false) {
			 if (reason.contains("does not exist")) { 
					receipt.println(ticket);			     
					receipt.println("Error: " + reason);
			 }else {
				 receipt.println(ticket);
			     receipt.println("Error: " + reason);
			 }
			 
		}else {
			receipt.println(ticket);
			receipt.println("Your account has been Reopened, transactions will now go through again");
		} 
		receipt.println();
	}
	
	/*
	 The input is a bank object, and PrintWriter and scanner objects
	 The process is that the test cases file is read for the account requested for the check, then a transcation tikcet
	 	is made to send to the bank class to it's check method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void clearCheck(Bank bank, PrintWriter receipt, Scanner input) throws IOException {
		System.out.println("\tEnter your account number: ");
		int account = input.nextInt();
		double amount = input.nextDouble();
		String date = input.next();
		
		Calendar present = Calendar.getInstance();
		Check check = new Check(account, amount, date);
		TransactionTicket ticket = new TransactionTicket(account, present, "Clear Check", amount, 0);
		TransactionReceipt returnedCheck = bank.clearCheck(check, ticket);
		
		receipt.println(returnedCheck);
		receipt.println();
	}
	
	/*
	The input is a bank object, and PrintWriter and scanner objects
	 The process is that the test cases file is read for the information for the new account, then a transcation tikcet
	 	is made to send to the bank class to it's new account method. A transaction receipt object is sent back and is used to
	 	display the correct message to the output file
	 The output is the message in the output file
	 */
	public static void newAcct(Bank bank, PrintWriter receipt, Scanner input, int numAccts) throws IOException {
		System.out.println("\tEnter your Last Name: ");
		String last = input.next();
		System.out.println("\tEnter your First Name: ");
		String first = input.next();
		System.out.println("\tEnter your SSN: ");
		String ssn = input.next();
		System.out.println("\tEnter your proposed account number: ");
		int account = input.nextInt();
		String type = input.next();
		double balance = 0.00;
		int term;
		
		if (type.equals("CD")) {
			System.out.println("\tEnter your intial Deposit: ");
			balance = input.nextDouble();	
			System.out.println("\tenter your wanted term length: ");
			term = input.nextInt();
		}else {
			term = 0;
		}
		
		Calendar present = Calendar.getInstance();
		Name name = new Name(first, last);
		Depositor depositor = new Depositor(ssn, name);
		TransactionTicket ticket = new TransactionTicket(account, present, "New Account", balance, term);
		TransactionReceipt getBack = bank.newAccount(ticket, depositor, type);
		
		boolean success = getBack.getFlag();
		String reason = getBack.getReason();	
		receipt.println(present.getTime());
		
		if (success == false) {
			if (reason.contains("already exists")) {
				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
		        receipt.println("Proposed Account Number: " + account);
		        receipt.println("Error: " + reason );
			}else if(reason.contains("opening balance")) {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				System.out.println("\tEnter your opening CD balance");
				
				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Opening balance: " + balance);
				receipt.println("Error: " + reason);
			}else if(reason.contains("CD accounts must a maturity date that is either")) {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				System.out.println("\tEnter your opening CD balance");
				System.out.println("\tEnter your opening CD term(6, 12, 8, or 24)");

				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Opening balance: " + balance);
				receipt.println("Error: " + reason);
			}else if(reason.contains("not 9 digits long")) {
				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
				receipt.println("Error: " + reason);
			}else {
				System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
				
				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Error: " + reason);
			}
		}else {
			System.out.println("\tEnter what type of account you want: (Checking, Savings, or CD)");
			if (type.equals("CD")) {
				System.out.println("\tEnter your opening CD balance");
				System.out.println("\tEnter your opening CD term(6, 12, 8, or 24)");

				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Opening balance: " + balance);
				
				Calendar date = getBack.getPostMDate();
				int month = date.get(Calendar.MONTH) + 1;
				int day = date.get(Calendar.DAY_OF_MONTH);
				int year = date.get(Calendar.YEAR);
				String writtenDate = month + "/" + day + "/" + year;
				
				receipt.println("Maturity Date: " + writtenDate);
				receipt.println("Congratulations! Your account " + account + " has been made, welcome to our Bank!");
			
				
			}else {
				receipt.println("Transaction Requested: New Account");
				receipt.println(name);
		        receipt.println(depositor);
				receipt.println("Proposed Account Number: " + account);
				receipt.println("Account type: " + type);
				receipt.println("Congratulations! Your account " + account + " has been made, welcome to our Bank!");
			}
			
		}
	
		receipt.println();
	}
	
}