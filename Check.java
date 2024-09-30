import java.util.Calendar;
/*
The Check class contains the users account number, the amount of the check they have, and the date of the check
from the calendar class, as well as getters, no argument constructors, and parameterized
constructors
*/
public class Check {
	private int accountNumber;
	private double checkAmount;
	private Calendar checkDate = Calendar.getInstance();
	
	//No-Arg Constructor
	public Check () {
		accountNumber = 0;
		checkAmount = 0.0;
		checkDate = Calendar.getInstance();
		checkDate.clear();
	}
	
	//Parameterized Constructor
	public Check (int acctN, double cAmount, String chd) {
			accountNumber = acctN;
			checkAmount = cAmount;
			String[] a = chd.split("/");
			int[] b = new int[3];
			checkDate.clear();
			
			b[0] = Integer.parseInt(a[0]);
			b[1] = Integer.parseInt(a[1]);
			b[2] = Integer.parseInt(a[2]);

			
			checkDate.set(Calendar.MONTH, b[0] - 1);
			checkDate.set(Calendar.DAY_OF_MONTH, b[1]);
			checkDate.set(Calendar.YEAR, b[2]);

		}

	//Copy Constructor
	public Check(Check check) {
		accountNumber = check.accountNumber;
		checkAmount = check.checkAmount;
		checkDate = check.checkDate;
	}
	
	//getters
	public int getAcctNum () {
		return accountNumber;
	}
	public double getCheckAmount () {
		return checkAmount;
	}
	public Calendar getCheckDate () {
		return checkDate;
	}


	//to String
	public String toString() {
		String toStr = String.format("Transaction Requested: Clear Check\n" + 
									 "Account number: " + accountNumber 
								   + "\nAccount type: ");
		return toStr;
	}
}