import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class Account {
	private Depositor info;
	private int acctNumber;
	protected double balance;
	private String acctType;
	private boolean openStatus;
	private String thFile;
	private RandomAccessFile transactionHistory;
	private int numTransactions;
	
	//No Argument Constructor
	public Account () {
		info = new Depositor();
		acctNumber = 0;
		balance = 0.0;
		acctType = "";
		thFile = "";
		openStatus = false;
		numTransactions = 0;
	}
	//Parameterized Constructor
	public Account (Depositor d, int acctN, String acctT, double b, boolean status, int numTrans) throws FileNotFoundException{
		info = d;
		acctNumber = acctN;
		balance = b;
		acctType = acctT;
		openStatus = status;
		
		String filler = acctN + "";
		thFile = "x" + filler + ".dat";
		transactionHistory = new RandomAccessFile(thFile, "rw"); 
		numTransactions = numTrans;	
	}
	//copy Constructor
	public Account (Account account) throws FileNotFoundException{
		info = new Depositor(account.info);
		acctNumber = account.acctNumber;
		balance = account.balance;
		acctType = account.acctType;
		openStatus = account.openStatus;
		thFile = account.thFile;
		transactionHistory = new RandomAccessFile(thFile, "rw");
		numTransactions = account.numTransactions;
	}

	//Transaction Methods
	public void addTransaction(TransactionReceipt receipt) throws IOException {
		String transactionData = receipt.toFile();

		transactionHistory.seek(transactionHistory.length());
		transactionHistory.writeChars(transactionData);
		numTransactions++;
	}
	public TransactionReceipt getTransactionHistory(int index) throws IOException{
		long pointer = 370 * index;
		transactionHistory.seek(pointer);
		char[] arr = new char[185];
		for (int x = 0; x < 185; x++) {
			arr[x] = transactionHistory.readChar();
		}
		String filler = new String(arr).trim();
		
		int an = Integer.parseInt( filler.substring(0,7).trim() );
		
		String opDate = filler.substring(7,18).trim();
		
		Calendar date = Calendar.getInstance();
		date.clear();
		String[] a = opDate.split("/");
		int[] b = new int[3];		
		b[0] = Integer.parseInt(a[0]);
		b[1] = Integer.parseInt(a[1]);
		b[2] = Integer.parseInt(a[2]);
		date.set(Calendar.MONTH, b[0] - 1);
		date.set(Calendar.DAY_OF_MONTH, b[1]);
		date.set(Calendar.YEAR, b[2]);

		
		String operation = filler.substring(18, 33).trim();
		
		double opAmount = Double.parseDouble( filler.substring(33, 43) );
		
		int term = Integer.parseInt( filler.substring(43, 47).trim() );		
		
		boolean result = Boolean.parseBoolean( filler.substring(47, 53).trim() );
		
		String acctype = filler.substring(53, 63).trim();
		
		double pre = Double.parseDouble( filler.substring(63, 73).trim() );
		
		double post = Double.parseDouble( filler.substring(73, 83).trim() );
		
		String postDate = filler.substring(83, 94).trim();
		
		String reason = filler.substring(94);
		
		TransactionTicket r = new TransactionTicket(an, date, operation, opAmount, term);
		TransactionReceipt rr = new TransactionReceipt(r, result, reason, acctype, pre, post, postDate);
		
		return rr;
	}
	
	//getters
	public Depositor getDepositer() {
			return new Depositor(info);		
	}
	public int getAcctNumber() {
			return acctNumber;
	}
	public double getBalance() {
		return balance;
	}
	public String getType() {
			return acctType;
	}
	public boolean getStatus() {
			return openStatus;
	}
	public int getAmountOfTransactions() {
			return numTransactions;
	}

	/* The input is a Transaction Ticket object
	 The process is that the method makes the status boolean false and makes a transaction recsipt which is added
	 	to the Array List of receipts.
	 The output is the TransactionReceipt;
	*/
	public TransactionReceipt closeAcct(TransactionTicket ticket) throws IOException {
		if (openStatus == false) {
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, "Account is Closed", acctType, balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}else {
			openStatus = false;
			TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", acctType, balance, balance, "N/A");
			addTransaction(receipt);
			return new TransactionReceipt(receipt);
		}
	}
		
	/* The input is a Transaction Ticket object
	 The process is that the method makes the status boolean true and makes a transaction recsipt which is added
	 	to the Array List of receipts.
	 The output is the TransactionReceipt;
	 */
	public TransactionReceipt reOpenAcct(TransactionTicket ticket) throws IOException {
		openStatus = true;
		TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", acctType, balance, balance, "N/A");
		addTransaction(receipt);
		return new TransactionReceipt(receipt);
	}
	
	//make data file print
	public String getData() {
		String a = info.toFile();
		String b = String.format("%-30s%-7d%-10s%-5B%-8.2f", a, acctNumber, acctType, openStatus, balance, numTransactions);	
		return b;
	}
}