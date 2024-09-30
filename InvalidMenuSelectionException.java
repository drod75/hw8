
public class InvalidMenuSelectionException extends Exception{
	public InvalidMenuSelectionException(char selection) {
		super("Error: The option " + selection + ", is not accepted, please try again");
	}
}
