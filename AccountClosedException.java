
public class AccountClosedException extends Exception{
	public AccountClosedException() {
		super("Error: Your account is Closed, reopen to be able to make a transaction");
	}
	public AccountClosedException(int accountNumber) {
		super("Error: Account " + accountNumber + " is Closed, reopen to be able to make a transaction");
	}
}
