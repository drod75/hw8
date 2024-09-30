/* TransactionTicket contains the account number, date of transaction, type of transaction, amount of transaction,
and the term of there new cd, the method also contains getters, and both no argument and parameterized constructors
*/
import java.util.Calendar;
public class TransactionTicket {
	private int accountNumber;
	private Calendar dateOfTransaction;
	private String typeOfTransaction;
	private double amountOfTransaction;
	private int termOfCD;
	
	//No-Arg Constructor
	public TransactionTicket() {
		accountNumber = 0;
		dateOfTransaction = Calendar.getInstance();
		dateOfTransaction.clear();
		typeOfTransaction = "";
		amountOfTransaction = 0;
		termOfCD = 0;
	}
	
	//Parameterized Constructor
	public TransactionTicket(int acctNum, Calendar dateTrans, String typeTrans, double amtTrans, int tCD) {
		accountNumber = acctNum;
		dateOfTransaction = dateTrans;
		typeOfTransaction = typeTrans;
		amountOfTransaction = amtTrans;
		termOfCD = tCD;
	}
	
	//Copy Constructor
	public TransactionTicket(TransactionTicket ticket) {
		accountNumber = ticket.accountNumber;
		dateOfTransaction = ticket.dateOfTransaction;
		typeOfTransaction = ticket.typeOfTransaction;
		amountOfTransaction = ticket.amountOfTransaction;
		termOfCD = ticket.termOfCD;
	}
	
	//getters
	public int getAcctNum () {
		return accountNumber;
	}
	public Calendar getDateOfTransaction () {
		return dateOfTransaction;
	}
	public String getTypeOfTransaction() {
		return typeOfTransaction;
	}
	public double getAmountOfTransaction() {
		return amountOfTransaction;
	}
	public int getTermOfCD() {
		return termOfCD;
	}
	
	//to String
	public String toString() {
		String toStr = "Transaction Requested: " + typeOfTransaction + "\nAccount Number: " + accountNumber;
		return toStr;
	}
	public String getData() {
		int month = dateOfTransaction.get(Calendar.MONTH) + 1;
		String stringMonth = "" + month;
		if (stringMonth.length() != 2) stringMonth = "0" + stringMonth;
			
		int day = dateOfTransaction.get(Calendar.DAY_OF_MONTH);
		String stringDay = "" + day;
		if (stringDay.length() != 2) stringDay = "0" + stringDay;

		int year = dateOfTransaction.get(Calendar.YEAR);
		String date = stringMonth + "/" + stringDay + "/" + year;
		
		String toData = String.format("%-7d%-11s%-15s%-10.2f%-4d", 
				accountNumber, date, typeOfTransaction, amountOfTransaction, termOfCD);
		return toData;
	}
}
