/* TransactionRecipt contains an object from the transaction ticket class, a boolean to indicate success, 
the reason if the transaction fails, the type of account used, the balances before and after, as well as the post 
maturity date if applicable, the method also contains getters, and both no argument and parameterized constructors
*/
import java.util.Calendar;
public class TransactionReceipt {
	private TransactionTicket ticketInfo;
	private boolean successFlag;
	private String failureReason;
	private String accountType;
	private double preBalance;
	private double postBalance; 
	private Calendar postMaturityDate = Calendar.getInstance();

	//No-Arg Constructor
	public TransactionReceipt() {
		ticketInfo = new TransactionTicket();
		successFlag = true;
		failureReason = "";
		accountType = "";
		preBalance = 0.0;
		postBalance = 0.0;
		postMaturityDate = Calendar.getInstance();
		postMaturityDate.clear();
	}
	
	//Parameterized Constructor
	public TransactionReceipt (TransactionTicket info, boolean a, String b, String c, double d, double e, String postDate) {
		ticketInfo = info;
		successFlag = a;
		failureReason = b;
		accountType = c;
		preBalance = d;
		postBalance = e;
		if (postDate.equals("N/A") ) {
			postMaturityDate = Calendar.getInstance();
			postMaturityDate.clear();
		}else {
			postMaturityDate.clear();
			
			String[] mdArray = postDate.split("/");
			
			int[] b1 = new int[3];
			
			b1[0] = Integer.parseInt(mdArray[0]);
			b1[1] = Integer.parseInt(mdArray[1]);
			b1[2] = Integer.parseInt(mdArray[2]);

			postMaturityDate.set(Calendar.MONTH, b1[0] - 1);
			postMaturityDate.set(Calendar.DAY_OF_MONTH, b1[1]);
			postMaturityDate.set(Calendar.YEAR, b1[2]);	
		}
	}
	
	//copy constructor
	public TransactionReceipt (TransactionReceipt receipt) {
		ticketInfo  = new TransactionTicket(receipt.ticketInfo);
		successFlag = receipt.successFlag;
		failureReason = receipt.failureReason;
		accountType = receipt.accountType;
		preBalance = receipt.preBalance;
		postBalance = receipt.postBalance;
		postMaturityDate = receipt.postMaturityDate;
	}
	
	//getters
	public TransactionTicket getTINfo () {
		return new TransactionTicket(ticketInfo);
	}
	public boolean getFlag() {
		return successFlag;
	}
	public String getType () {
		return accountType;
	}
	public String getReason() {
		return failureReason;
	}
	public double getPreBalance() {
		return preBalance;
	}
	public double getPostBalance() {
		return postBalance;
	}
	public Calendar getPostMDate() {
		return postMaturityDate;
	}

	//to String
	public String toString() {
		String transactionType = ticketInfo.getTypeOfTransaction();
		
		if(transactionType.equals("Balance") ) {
			if (successFlag == true) {
				String out = ticketInfo.getDateOfTransaction().getTime()  + "\nTransaction Requested: Balance\nAccount Number: " + ticketInfo.getAcctNum()
				+ "\nAccount Type: " + accountType;
				
				String out1 = String.format("\nCurrent Balance: $%.2f", postBalance);
				
				if (accountType.equals("CD")) {
		    		int month = postMaturityDate.get(Calendar.MONTH) + 1;
		    		String stringMonth = "" + month;
		    		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
		    		
		    		
		    		int day = postMaturityDate.get(Calendar.DAY_OF_MONTH);
		    		String stringDay = "" + day;
		    		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		    		int year = postMaturityDate.get(Calendar.YEAR);
		    		String date = stringMonth + "/" + stringDay + "/" + year;
					
					out1 = out1 + "\nMaturity Date: " +date;
				}
				return out + out1;
				
			}else if (successFlag == false) {
				if (failureReason.contains("is Closed")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\nError: " + failureReason;
					return a + b + c;
				}else {
					String out = ticketInfo.getDateOfTransaction().getTime()  + "\nTransaction Requested: Balance\nAccount Number: " + ticketInfo.getAcctNum() 
					+ "\n" +failureReason;
				
					return out;
				}
			}
			
		}else if(transactionType.equals("Withdrawal")) {
			if (successFlag == false) {
				if (failureReason.contains("does not exist")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\n" + failureReason;
					return a + b + c;
				}else if(failureReason.contains("is Closed")){
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\nError: " + failureReason;		
					return a + b + c;
				}
				
				if (accountType.equals("CD")) {
					if (failureReason.contains("not reached") ) {
						String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
						String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
						String c = String.format("\nOld Balance: $%.2f\nAmount to Withdraw: $%.2f\nError: " + failureReason
								, preBalance, ticketInfo.getAmountOfTransaction());	
						
						return a + b + c;
					}else {
						String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
						String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
						String c = String.format("\nOld Balance: $%.2f\nAmount to Withdraw: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
						String d = String.format("\nError: $%.2f " + failureReason, ticketInfo.getAmountOfTransaction());
						
						return a + b + c + d;
					}
				}else {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount to Withdraw: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nError: $%.2f " + failureReason, ticketInfo.getAmountOfTransaction());
					
					return a + b + c + d;
				}
				
			}else {
				if (accountType.equals("CD")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount to Withdraw: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nNew Balance $%.2f", postBalance);
					
					int month = postMaturityDate.get(Calendar.MONTH) + 1;
		    		String stringMonth = "" + month;
		    		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
		    		
		    		
		    		int day = postMaturityDate.get(Calendar.DAY_OF_MONTH);
		    		String stringDay = "" + day;
		    		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		    		int year = postMaturityDate.get(Calendar.YEAR);
		    		String date = stringMonth + "/" + stringDay + "/" + year;
		    		String e = "\nNew Maturity Date: " + date;
		    		
		    		return a + b + c + d + e;
				}else {
					if (failureReason.contains("Successful Transaction") ) {
						String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
						String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
						String c = String.format("\nOld Balance: $%.2f\nAmount of Check: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
						String d = String.format("\nNew Balance $%.2f", postBalance);
						String e = String.format("\nNotice: " + failureReason);
						
						return a + b +c + d + e;
					}else {					
						String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
						String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
						String c = String.format("\nOld Balance: $%.2f\nAmount to Withdraw: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
						String d = String.format("\nNew Balance $%.2f", postBalance);
						return a + b + c + d;
					}
				}
			}
		}else if(transactionType.equals("Deposit")) {
			if(successFlag == false) {
				if (failureReason.contains("does not exist")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\n" + failureReason;
					
					return a + b + c;
					
				}else if(failureReason.contains("is Closed")){
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\nError: " + failureReason;
					
					return a + b + c;
				}
				
				if (accountType.equals("CD")) {
					if (failureReason.contains("not reached") ) {
						String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
						String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
						String c = String.format("\nOld Balance: $%.2f\nAmount to Deposit: $%.2f\nError: " + failureReason
								, preBalance, ticketInfo.getAmountOfTransaction());	
						
						return a + b + c;
					}else {
						String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
						String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
						String c = String.format("\nOld Balance: $%.2f\nAmount to Deposit: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
						String d = String.format("\nError: $%.2f " + failureReason, ticketInfo.getAmountOfTransaction());
						
						return a + b + c + d;
					}
				}else {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount to Deposit: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nError: $%.2f " + failureReason, ticketInfo.getAmountOfTransaction());
					
					return a + b + c + d;
				}
				
			}else {
				if (accountType.equals("CD")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount to Deposit: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nNew Balance $%.2f", postBalance);
					
					int month = postMaturityDate.get(Calendar.MONTH) + 1;
		    		String stringMonth = "" + month;
		    		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
		    		
		    		
		    		int day = postMaturityDate.get(Calendar.DAY_OF_MONTH);
		    		String stringDay = "" + day;
		    		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		    		int year = postMaturityDate.get(Calendar.YEAR);
		    		String date = stringMonth + "/" + stringDay + "/" + year;
		    		String e = "\nNew Maturity Date: " + date;
		    		
		    		return a + b + c + d + e;
				}else {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount to Deposit: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nNew Balance $%.2f", postBalance);
		    		return a + b + c + d;
				}
			}
		}else if(transactionType.equals("Clear Check")) {
			if (successFlag == false) {
				if (failureReason.contains("does not exist")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\n" + failureReason;
					return a + b + c;
				}else if(failureReason.contains("cannot be accpeted to clear a check")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType + "\nError: Account Type cannot be accepted to clear a check";
					return a + b;
				}else if(failureReason.contains("Insufficient funds available")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount of Check: $%.2f\nError: Insufficient Funds Available - Bounce Fee ($2.50) Charged", preBalance, ticketInfo.getAmountOfTransaction());
					return a + b + c;
				}else if(failureReason.contains("not reached yet")) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount of Check: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nNew Balance $%.2f", postBalance);
					String e = "\nError: " + failureReason;
					return a + b + c + d + e;
				}else if(failureReason.contains("is Closed")){
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum();
					String c = "\nError: " + failureReason;		
					return a + b + c;
				}else {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount of Check: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = "\nError: " + failureReason;;
					
					return a + b +c + d;
				}
			}else {
				if (failureReason.contains("Successful Transaction") ) {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount of Check: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nNew Balance $%.2f", postBalance);
					String e = String.format("\nNotice: " + failureReason);
					
					return a + b +c + d + e;
				}else {
					String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
					String b = "\nAccount Number " + ticketInfo.getAcctNum() + "\nAccount Type: " + accountType;
					String c = String.format("\nOld Balance: $%.2f\nAmount of Check: $%.2f", preBalance, ticketInfo.getAmountOfTransaction());	
					String d = String.format("\nNew Balance $%.2f", postBalance);
				
					return a + b +c + d;
				}
			}
		}else if(transactionType.equals("Delete Account")) {
			if (successFlag == false && failureReason.contains("does not exist") ) {
				String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Request: " + ticketInfo.getTypeOfTransaction();
				String b = "\nAccount Number " + ticketInfo.getAcctNum();
				String c = "\n" + failureReason;
				return a + b + c;
			}
			else {
				if (successFlag == false && failureReason.contains("Balance does not equal zero") ) {
					String out = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Requested: Delete Account\nAccount Number: " + ticketInfo.getAcctNum()
					+ "\nAccount Type: " + accountType;
					
					String out1 = String.format("\nAccount Balance: $%.2f\n"
							+ "Error: Account " + ticketInfo.getAcctNum() + " does not have a balance of zero", postBalance);
					return out + out1;
				}else {
					String out = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Requested: Delete Account\nAccount Number: " + ticketInfo.getAcctNum()
					+ "\nAccount Type: " + accountType;
					String b = "\nYour Account has been deleted, sorry to see you go!";
					return out + b;
				}
			}
		}else if(transactionType.equals("Close Account")) {
			if(successFlag == false) {
				String a = ticketInfo.getDateOfTransaction().getTime() + 
						"\nTransaction Requested: Close Account\nAcccount Number: " + ticketInfo.getAcctNum();
				String b = "\nError: " + failureReason;
				
				return a + b;
			}else {
				String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Requested: Close Account";
				String b = "\nAccount Number: " + ticketInfo.getAcctNum();
				String c = "\nYour account has been closed, transactions will not go through unless it is reopened";
				return a + b + c;
			} 
			
		}else if(transactionType.equals("ReOpen Account")) {
			if(successFlag == false) {
				String a = ticketInfo.getDateOfTransaction().getTime() + 
						"\nTransaction Requested: ReOpen Account\nAcccount Number: " + ticketInfo.getAcctNum();
				String b = "\nError: " + failureReason;
				
				return a + b;
			}else {
				String a = ticketInfo.getDateOfTransaction().getTime() + "\nTransaction Requested: ReOpen Account";
				String b = "\nAccount Number: " + ticketInfo.getAcctNum();
				String c = "\nYour account has been Reopened, transactions will now go through again";
				return a + b + c;
			} 
		}
		return null;
		
	}
	
	//to File output
	public String toFile() {
		int montha = postMaturityDate.get(Calendar.MONTH) + 1;
		String stringMontha = "" + montha;
		if (stringMontha.length() != 2) stringMontha = "0" + stringMontha;
			
		int daya = postMaturityDate.get(Calendar.DAY_OF_MONTH);
		String stringDaya = "" + daya;
		if (stringDaya.length() != 2) stringDaya = "0" + stringDaya;

		int yeara = postMaturityDate.get(Calendar.YEAR);
		String protoDatea = stringMontha + "/" + stringDaya + "/" + yeara;
		if (protoDatea.equals("01/01/1970")) protoDatea = "N/A";
		//
		
		String toData = String.format("%-47s%-6B%-10s%-10.2f%-10.2f%-11s%-91s", 
				ticketInfo.getData(), successFlag, accountType, preBalance, postBalance, protoDatea, failureReason);
				
		return toData;
	}
	
}
