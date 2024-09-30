
public class CDMaturityDateException extends Exception{
	public CDMaturityDateException(String mdate) {
		super("Maturity Date (" + mdate + ") not reached yet");
	}
}
