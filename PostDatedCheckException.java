
public class PostDatedCheckException extends Exception{
	public PostDatedCheckException(String checkDate) {
		super("Check date (" + checkDate + ") not reached yet");
	}
}
