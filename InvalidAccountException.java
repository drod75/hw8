
public class InvalidAccountException extends Exception{
	public InvalidAccountException(){
		super("Error: That account does not exist");
	}
	public InvalidAccountException(int accountNumber){
		super("Error: The account number (" + accountNumber + ") does not exist");
	}
}
