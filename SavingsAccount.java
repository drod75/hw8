import java.io.FileNotFoundException;
import java.io.IOException;

public class SavingsAccount extends Account {
	
	//Constructors
	public SavingsAccount() {
		super();
	}
	public SavingsAccount(Depositor d, int acctN, String acctT, double b, boolean status, int numTrans) throws FileNotFoundException{
		super(d, acctN, acctT, b, status, numTrans);
	}
	public SavingsAccount(SavingsAccount account) throws FileNotFoundException{
		super(account);
	}

	/*The input is a transaction ticket object.
	 The process is that it creates a  a transaction receipt object with the current balance and account type.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt getCurrentBalance(TransactionTicket ticket) throws IOException {
		try {
			if (getStatus() == false) throw new AccountClosedException(getAcctNumber());
			
			TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, balance, "N/A");
			return new TransactionReceipt(receipt);
		}catch(AccountClosedException ex) {
			String reason = ex.getMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			return new TransactionReceipt(receipt);
		}
	}
	
	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object that is true if the amount is valid, 
	 	if anything is wrong the receipt is given false and the reason why.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt makeDeposit(TransactionTicket ticket) throws IOException {
		try {
			if(getStatus() == false) throw new AccountClosedException(getAcctNumber());
			try {
				Double depositAmount = ticket.getAmountOfTransaction();
				if(depositAmount <= 0.00) throw new InvalidAmountException();
				
				double postBal = balance + depositAmount;
				TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, postBal, "N/A");
				balance += depositAmount;
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}catch(InvalidAmountException ex) {
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, ex.getMessage(), 
						getType(), balance, balance, "N/A");
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
			}
		}catch (AccountClosedException ex) {
			String reason = ex.getMessage();
			TransactionReceipt receipt = new TransactionReceipt(ticket, false, reason, getType(), balance, balance, "N/A");
			return new TransactionReceipt(receipt);
			
		}
	}

	/*The input is a transaction ticket object.
	 The process is that it creates a transaction receipt object if the withdraw amount is valid.If anything is
	 	wrong the receipt is given false and the reason why it is false.
	 The output is the transaction receipt object.
	*/
	public TransactionReceipt makeWithdrawal(TransactionTicket ticket) throws IOException {
		try {
			if (getStatus() == false) throw new AccountClosedException(getAcctNumber());
			
			try {
				double withdrawAmount = ticket.getAmountOfTransaction();
				if(withdrawAmount <= 0.00) throw new InvalidAmountException();
				else if (withdrawAmount > balance) throw new InsufficientFundsException();
					
					
				double postBal = balance - withdrawAmount;
				TransactionReceipt receipt = new TransactionReceipt(ticket, true, "N/A", getType(), balance, postBal, "N/A");
				balance = balance - withdrawAmount;
				addTransaction(receipt);
				return new TransactionReceipt(receipt);
				
			}catch(InsufficientFundsException | InvalidAmountException ex) {
				TransactionReceipt receipt = new TransactionReceipt(ticket, false, ex.getMessage(), 
						getType(), balance, balance, "N/A");
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
	
	//get data
	public String getData() {
		
		if (getType().equals("CD")) {
			String a = super.getData();
			return a;
		}else {
			String a = super.getData() + "          ";
			return a;
		}
	}	
	
	// to String
	public String toString() {
		String toStr = String.format("%-12s%-12s%-9s %8d\t\t\t%-9s\t\t$%8.2f\tN/A", getDepositer().getName().getLast(), getDepositer().getName().getFirst(),
				getDepositer().getSSN(), getAcctNumber(), getType(), balance);
		return toStr;
	}
	
}
