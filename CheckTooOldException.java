
public class CheckTooOldException extends Exception{
	public CheckTooOldException(String checkDate, String sixMonthDate) {
		super("The check is dated for (" + checkDate + ") which is over six months old, it cannot be accepted, "
				+ "for reference, the check should have been cashed in by (" + sixMonthDate + ")");
	}
}
