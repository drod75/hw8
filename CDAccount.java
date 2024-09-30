import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class CDAccount extends SavingsAccount{
	private Calendar maturityDate = Calendar.getInstance();

	//Constructors
	public CDAccount() {
		super();
		maturityDate.clear();
	}
	public CDAccount(Depositor d, int acctN, String acctT, double b, boolean status, int numTrans, Calendar MDate) throws FileNotFoundException{
		super(d, acctN, acctT, b, status, numTrans);
		maturityDate = MDate;
	}
	public CDAccount(CDAccount account) throws FileNotFoundException{
		super(account);
		maturityDate = account.maturityDate;
	}

	/*The input is a transaction ticket object.
	 The process is that it creates a  a transaction receipt object with the current balance and account type.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt getCurrentBalance(TransactionTicket ticket) throws IOException {
		try {
			if(getStatus() == false) throw new AccountClosedException(getAcctNumber());
			
			int month = maturityDate.get(Calendar.MONTH) + 1;
			String stringMonth = "" + month;
			if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
			
			int day = maturityDate.get(Calendar.DAY_OF_MONTH);
			String stringDay = "" + day;
			if (stringDay.length() != 2) stringDay = "0" + stringDay;
			
			int year = maturityDate.get(Calendar.YEAR);
			String date = stringMonth + "/" + stringDay + "/" + year;
			if (date.equals("01/01/1970")) date = "N/A";
		
			TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, balance, date);
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}catch (AccountClosedException ex) {
			String reason = ex.getMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object that is true if, the cd account's maturity has been reached
	 the amount is valid, if anything is wrong the receipt is given false and the reason why.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt makeDeposit(TransactionTicket ticket) throws IOException {
		try {
			if(getStatus() == false) throw new AccountClosedException(getAcctNumber());
			
		try {
			double depositAmount = ticket.getAmountOfTransaction();
			if(depositAmount <= 0.00) throw new InvalidAmountException();
			
			Calendar present = Calendar.getInstance();
			int month = maturityDate.get(Calendar.MONTH) + 1;
			int day = maturityDate.get(Calendar.DAY_OF_MONTH);
			int year = maturityDate.get(Calendar.YEAR);
			String date = month + "/" + day + "/" + year;

			try {
				if (present.after(maturityDate) == false) throw new CDMaturityDateException(date);
				
				int newTerm = ticket.getTermOfCD();
				double postBal = balance + depositAmount;
					
				maturityDate.add(Calendar.MONTH, newTerm);
				int montha = maturityDate.get(Calendar.MONTH) + 1;
				int daya = maturityDate.get(Calendar.DAY_OF_MONTH);
				int yeara = maturityDate.get(Calendar.YEAR);
				String datea = montha + "/" + daya + "/" + yeara;
				TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", "CD", balance, postBal, datea);
				balance += depositAmount;
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			
			}catch(CDMaturityDateException ex){
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, ex.getMessage(), 
						"CD", 0.00, 0.00, date);
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}
		}catch(InvalidAmountException ex) {
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, ex.getMessage(), 
					"CD", balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
		}catch(AccountClosedException ex) {
			String reason = ex.getMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
		
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object if the withdraw amount is valid, or if the account
	 is a cd and the maturity date has been reached, if it has a int is used to extend the date by that term. If anything is
	 wrong the receipt is given false and the reason why it is false.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt makeWithdrawal(TransactionTicket ticket) throws IOException {
		try {
			if(getStatus() == false) throw new AccountClosedException(getAcctNumber());
			Calendar present = Calendar.getInstance();
			int month = maturityDate.get(Calendar.MONTH) + 1;
			int day = maturityDate.get(Calendar.DAY_OF_MONTH);
			int year = maturityDate.get(Calendar.YEAR);
			String date = month + "/" + day + "/" + year;
			
			try {
				double withdrawAmount = ticket.getAmountOfTransaction();
				if(withdrawAmount <= 0.00) throw new InvalidAmountException();
				else if(withdrawAmount > balance) throw new InsufficientFundsException();
			
				try {
					if (present.after(maturityDate) == false) throw new CDMaturityDateException(date);
					
					int newTerm = ticket.getTermOfCD();
					double postBal = balance - withdrawAmount;
					maturityDate.add(Calendar.MONTH, newTerm);
					int montha = maturityDate.get(Calendar.MONTH) + 1;
					int daya = maturityDate.get(Calendar.DAY_OF_MONTH);
					int yeara = maturityDate.get(Calendar.YEAR);
					String datea = montha + "/" + daya + "/" + yeara;
					TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", "CD", balance, postBal, datea);
						balance = balance - withdrawAmount;
					addTransaction(receipt);
					return new TransactionReceipt(receipt);
					
				}catch(CDMaturityDateException ex) {
					TransactionReceipt receipt = new TransactionReceipt(ticket, false, ex.getMessage(), 
						"CD", 0.00, 0.00, date);
					addTransaction(receipt);
					return new TransactionReceipt(receipt);
				}
			}catch(InsufficientFundsException | InvalidAmountException ex) {
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, ex.getMessage(), 
						"CD", balance, balance, "N/A");
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}
		}catch(AccountClosedException ex) {
			String reason = ex.getMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
	}
	
	//to M date
	public String toMDate() {
		int month = maturityDate.get(Calendar.MONTH) + 1;
		String stringMonth = "" + month;
		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
			
		int day = maturityDate.get(Calendar.DAY_OF_MONTH);
		String stringDay = "" + day;
		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		int year = maturityDate.get(Calendar.YEAR);
		String date = stringMonth + "/" + stringDay + "/" + year;
		return date;
	}

	//get data
	public String getData() {
		String a = super.getData();
		String b = String.format("%-10s", toMDate());
		
		return a + b;
	}
	// to String
	public String toString() {
		int month = maturityDate.get(Calendar.MONTH) + 1;
		String stringMonth = "" + month;
		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
			
		int day = maturityDate.get(Calendar.DAY_OF_MONTH);
		String stringDay = "" + day;
		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		int year = maturityDate.get(Calendar.YEAR);
		String date = stringMonth + "/" + stringDay + "/" + year;
		
		String toStr = String.format("%-12s%-12s%-9s %8d\t\t\t%-9s\t\t$%8.2f\t" + date, getDepositer().getName().getLast(), getDepositer().getName().getFirst(),
				getDepositer().getSSN(), getAcctNumber(), getType(), balance);
		return toStr;
	}	
}