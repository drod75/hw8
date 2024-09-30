import java.util.Calendar;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Bank {
	private static double totalAmountInSavingsAccts;
	private static double totalAmountInCheckingAccts;
	private static double totalAmountInCDAccts;
	private static double totalAmountInALLAccts;
	private RandomAccessFile file;
	private String fileName = "BankAccounts.dat";
	private int numAccts;
	
	//No-Arg Constructor
	public Bank () throws FileNotFoundException {
		numAccts = 0;
		totalAmountInSavingsAccts = 0;
		totalAmountInCheckingAccts = 0;
		totalAmountInCDAccts = 0;
		totalAmountInALLAccts = 0;
		file = new RandomAccessFile(fileName, "rw");
		numAccts = 0;
		
	}
	
	//add account
	public void addAccount(Account account) throws IOException {
		String addIn = account.getData();
		String qe = String.format("%-4d", account.getAmountOfTransactions());
		
		file.writeChars(qe + addIn);
		numAccts++;
	}
	
	//delete account file style
	public void fileDelete(int index) throws IOException {
		if (index == (numAccts - 1)) {
			long newLength = 148 * (numAccts - 1);
			file.setLength(newLength);
		}else {
			long pointer  = index * 148;
		
			int lastPlace = numAccts - 1;
			Account last = getAccount(lastPlace);
		
			String ab = String.format("%-4d", last.getAmountOfTransactions());
			String writeIn = ab + last.getData();
			
			file.seek(pointer);
			file.writeChars(writeIn);
		
			int ad = numAccts - 1;
			long newLength = 148 * ad;
			file.setLength(newLength);
		}
	}
	
	//getter
	public Account getAccount (int index) throws IOException {
		final long length = 148;
		long pointer = index * length;
		file.seek(pointer);
		char[] arr = new char[74];
		
		for (int x = 0; x < 74; x++) {
			arr[x] = file.readChar();
		}
		
		String filler = new String(arr);
		filler.trim();
		
		String wer = filler.substring(0, 4).trim();
		int amountTrans = Integer.parseInt(wer);
		
		String first = filler.substring(4, 14).trim();
		String last = filler.substring(14, 24).trim();
		String ssn = filler.substring(24, 34).trim();
		
		String f = filler.substring(34, 41).trim();
		int acctNumber = Integer.parseInt(f);
		
		String acctType = filler.substring(41, 51).trim();
		
		String fillera = filler.substring(51, 56).trim();
		boolean status = Boolean.parseBoolean(fillera);
		
		String filler1 = filler.substring(56, 64).trim();
		double balance = Double.parseDouble(filler1);
		
		String MDate = filler.substring(64, 74).trim();

		Name name = new Name (first, last);
		Depositor d = new Depositor(ssn, name);
		
		if (acctType.equals("CD")) {
			Calendar date = Calendar.getInstance();
			date.clear();
			String[] a = MDate.split("/");
			int[] b = new int[3];		
			b[0] = Integer.parseInt(a[0]);
			b[1] = Integer.parseInt(a[1]);
			b[2] = Integer.parseInt(a[2]);
			date.set(Calendar.MONTH, b[0] - 1);
			date.set(Calendar.DAY_OF_MONTH, b[1]);
			date.set(Calendar.YEAR, b[2]);
			
			CDAccount account = new CDAccount(d, acctNumber, acctType, balance, status, amountTrans, date);
			return account;
			
		}else if(acctType.equals("Checking")) {
			CheckingAccount account = new CheckingAccount(d, acctNumber, acctType, balance, status, amountTrans);
			return account;
		}else {
			SavingsAccount account = new SavingsAccount(d, acctNumber, acctType, balance, status, amountTrans);
			return account;
		}
		
	}
	public int getNumberOfAccounts() {
		return numAccts;
	}
	public double getAllCh() {
		return totalAmountInCheckingAccts;
	}
	public double getAllSv() {
		return totalAmountInSavingsAccts;
	}
	public double getAllCD() {
		return totalAmountInCDAccts;
	}
	public double getAllAmount() {
		return totalAmountInALLAccts;
	}
		
	//Total in All Accounts Checker
	public void checkAllInS() throws IOException {
		for (int x = 0; x < getNumberOfAccounts(); x++) {
			if (getAccount(x).getType().equals("Savings") ) totalAmountInSavingsAccts += getAccount(x).getBalance();
		}
	}
	public void checkAllInCh() throws IOException {
		for (int x = 0; x < getNumberOfAccounts(); x++) {
			if (getAccount(x).getType().equals("Checking") ) totalAmountInCheckingAccts += getAccount(x).getBalance();
		}
	}
	public void checkAllInCD() throws IOException {
		for (int x = 0; x < getNumberOfAccounts(); x++) {
			if (getAccount(x).getType().equals("CD") ) totalAmountInCDAccts += getAccount(x).getBalance();
		}
	}
	public void checkAllAccts() throws IOException {
		checkAllInS();
		checkAllInCh();
		checkAllInCD();	
		totalAmountInALLAccts = totalAmountInSavingsAccts + totalAmountInCheckingAccts + totalAmountInCDAccts;
	}
		
	//This method has an input of an account number and locates it the array of accounts, if found it outputs the account
 	private int findAcct(int requestedAccount) throws IOException {
 		for (int x  = 0; x < numAccts; x++) {
 			Account filler = getAccount(x);
 			int acctN = filler.getAcctNumber();
 			if (acctN == requestedAccount) return x;
 		}
		return -1;
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionReceipt getBalance(TransactionTicket ticket) throws IOException {
		int account = ticket.getAcctNum();
		int index = findAcct(account);
		try {
			
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			String type = getAccount(index).getType();
			if (type.equals("CD")) {
				CDAccount filler = (CDAccount)getAccount(index);
				TransactionReceipt receipt = filler.getCurrentBalance(ticket);
				
				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}else if (type.equals("Checking")) {
				CheckingAccount filler = (CheckingAccount)getAccount(index);
				TransactionReceipt receipt = filler.getCurrentBalance(ticket);
				
				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}else{
				SavingsAccount filler = (SavingsAccount)getAccount(index);
				TransactionReceipt receipt = filler.getCurrentBalance(ticket);
				
				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}
		}
		}catch (InvalidAccountException ex) {
			String reason = ex.getMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionReceipt makeWithdraw(TransactionTicket ticket) throws IOException {
		int account = ticket.getAcctNum();
		
		try {
		int index = findAcct(account);
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			String type = getAccount(index).getType();
			if (type.equals("CD")) {
				CDAccount filler = (CDAccount)getAccount(index);
				TransactionReceipt receipt = filler.makeWithdrawal(ticket);

				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}else if (type.equals("Checking")) {
				CheckingAccount filler = (CheckingAccount)getAccount(index);
				TransactionReceipt receipt = filler.makeWithdrawal(ticket);

				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}else{
				SavingsAccount filler = (SavingsAccount)getAccount(index);
				TransactionReceipt receipt = filler.makeWithdrawal(ticket);

				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}
		}
		}catch (InvalidAccountException ex) {
			String reason = ex.getLocalizedMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			
			return receipt;
		}
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */	
	public TransactionReceipt makeDeposit(TransactionTicket ticket) throws IOException {
		int account = ticket.getAcctNum();
		try {
		int index = findAcct(account);
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			String type = getAccount(index).getType();
			if (type.equals("CD")) {
				CDAccount filler = (CDAccount)getAccount(index);
				TransactionReceipt receipt = filler.makeDeposit(ticket);

				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}else if (type.equals("Checking")) {
				CheckingAccount filler = (CheckingAccount)getAccount(index);
				TransactionReceipt receipt = filler.makeDeposit(ticket);

				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
						
				return receipt;
			}else{
				SavingsAccount filler = (SavingsAccount)getAccount(index);
				TransactionReceipt receipt = filler.makeDeposit(ticket);

				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}
		}
		}catch (InvalidAccountException ex) {
			String reason = ex.getLocalizedMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			return receipt;
		}
	}

	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does sends the ticket to that account 
		and method for the transaction.
	The output is a receipt which is obtained from the accounts method for the transaction.
	 */
	public TransactionReceipt clearCheck(Check check, TransactionTicket ticket) throws IOException {
		int account = check.getAcctNum();
		
		try {
		int index = findAcct(account);
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			CheckingAccount filler = (CheckingAccount)getAccount(index);
			TransactionReceipt receipt = filler.clearCheck(check, ticket);


			int amount = filler.getAmountOfTransactions();
			String fill = String.format("%-4d", amount);		
			long pointer  = index * 148;
			file.seek(pointer);
			file.writeChars(fill + filler.getData());
					
			checkAllAccts();
			return receipt;
		}
		}catch (InvalidAccountException ex) {
			String reason = ex.getLocalizedMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			
			return receipt;
		}
	}
		
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if it does deletes the account if it has a balance of zero,
		if not the receipt is put false.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionReceipt deleteAccount(TransactionTicket ticket) throws IOException {
		int account = ticket.getAcctNum();
		try {
		int index = findAcct(account);
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			Account filler = getAccount(index);
			String type = filler.getType();
			double balance = filler.getBalance();
			if (balance > 0) {
				String reason = "Balance does not equal zero";
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, type, balance, balance, "N/A");
				
				int amount = filler.getAmountOfTransactions();
				String fill = String.format("%-4d", amount);		
				long pointer  = index * 148;
				file.seek(pointer);
				file.writeChars(fill + filler.getData());
				
				return receipt;
			}else {
				fileDelete(index);
				numAccts--;
				TransactionReceipt receipt = new TransactionReceipt(ticket, true, "", type, balance, balance, "N/A");
				checkAllAccts();
				return receipt;
			}
		
		}
		}catch (InvalidAccountException ex) {
			String reason = ex.getLocalizedMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			
			return receipt;
		}
	}
	
	/*The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the receipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionReceipt newAccount(TransactionTicket ticket, Depositor info, String type) throws IOException {
		int account = ticket.getAcctNum();
		String ssn = info.getSSN();
		int termNext = ticket.getTermOfCD();
		double cdBalance = ticket.getAmountOfTransaction();
		int index = findAcct(account);
		
		if (ssn.length() == 9) {
			if (index != -1) {
				String reason = "Account number " + account + " already exists";
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
				return receipt;
			}else {
				if (type.equals("Savings")) {
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", type, 0.00, 0.00, "N/A");
					Account newAccount = new SavingsAccount(info, account, type, 0, true, 0);
					addAccount(newAccount);
					checkAllAccts();
					return receipt;
				}else if(type.equals("Checking")){
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", type, 0.00, 0.00, "N/A");
					Account newAccount = new CheckingAccount(info, account, type, 0, true, 0);
					addAccount(newAccount);
					checkAllAccts();
					return receipt;
				}else if (type.equals("CD")) {
					if ( !(cdBalance >= 0) ) {
						String reason = "CD accounts must have an opening balance of at least 0";
						TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, type, 0.00, 0.00, "N/A");
						return receipt;
					}else {
						if ( termNext == 6 || termNext == 12 || termNext == 18 || termNext == 24 ) {
							Calendar date = Calendar.getInstance();
							Calendar present = Calendar.getInstance();
							date.clear();
							date.set(Calendar.MONTH, present.MONTH);
							date.set(Calendar.DAY_OF_MONTH, present.DAY_OF_MONTH);
							date.set(Calendar.YEAR, 2023);
							date.add(Calendar.MONTH, termNext);
							
							int month = date.get(Calendar.MONTH) + 1;
							int day = date.get(Calendar.DAY_OF_MONTH);
							int year = date.get(Calendar.YEAR);
							String writtenDate = month + "/" + day + "/" + year;
						
							TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", type, cdBalance, cdBalance, writtenDate);
							Account newAccount = new CDAccount(info, account, type, cdBalance, true, 0, date);
							addAccount(newAccount);
							checkAllAccts();
							
							return receipt;
							
							
						}else {
							String reason = "You chose a term of " + termNext + ", CD accounts must a maturity date that is either,"
									+ " 6, 12, 18, or 24 months away";
							TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, type, cdBalance, cdBalance, "N/A");
							return receipt;
						}
					}
				}else {
					String reason = "Account type " + type + " cannot be accepted, choose from the choices available "
							+ "(Checking, Savings, CD)";
					TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
					return receipt;
				}
			}
		}else {
			String reason = "SSN " + ssn + " cannot be accpeted as it is not 9 digits long";
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "N/A", 0.00, 0.00, "N/A");
			return receipt;
		}
		
	}
	
	/*
	The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the receipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	 */
	public TransactionReceipt closeAccount(TransactionTicket ticket) throws IOException {
		int account = ticket.getAcctNum();
		try {
		int index = findAcct(account);
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			Account filler = getAccount(index);
			TransactionReceipt receipt = filler.closeAcct(ticket);

			int amount = filler.getAmountOfTransactions();
			String fill = String.format("%-4d", amount);		
			long pointer  = index * 148;
			file.seek(pointer);
			file.writeChars(fill + filler.getData());
			
			return receipt;
		}
		}catch (InvalidAccountException ex) {
			String reason = ex.getLocalizedMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
			
			return receipt;
		}
	}
	
	/* 
	The input is an object from the transaction ticket class.
	The process is that if locates the account and if does not, the information is sent through and all of it is checked.
		If anything is wrong then the receipt if put false and the reason why.
	The output is a receipt which is made depending on if the transaction went through.
	  */
	public TransactionReceipt reOpenAccount(TransactionTicket ticket) throws IOException {
		int account = ticket.getAcctNum();
		try {
		int index = findAcct(account);
		if (index == -1) {
			throw new InvalidAccountException(account);
		}else {
			Account filler = getAccount(index);
			TransactionReceipt receipt = filler.reOpenAcct(ticket);


			int amount = filler.getAmountOfTransactions();
			String fill = String.format("%-4d", amount);		
			long pointer  = index * 148;
			file.seek(pointer);
			file.writeChars(fill + filler.getData());
			
			return receipt;
		}
	}catch (InvalidAccountException ex) {
		String reason = ex.getLocalizedMessage();
		TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, "", 0.00, 0.00, "N/A");
		
		return receipt;
	}
}
}
