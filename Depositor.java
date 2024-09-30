/*
 The Name class contains the users ssn and full name from the name class, as well as getters, no argument constructors, and parameterized
 constructors
 */
public class Depositor {
	private String ssn;
	private Name name;
	
	//No-Arg Constructor
	public Depositor (){
		ssn = "";
		name = new Name();
	}
	
	//Parameterized Constructor
	public Depositor (String SSN, Name name) {
		ssn = SSN;
		this.name = name;
	}
	
	//Copy Constructor
	public Depositor (Depositor info) {
		ssn = info.ssn;
		name = new Name(info.name);
	}
	
	//Getters
	public String getSSN() {
		return ssn;
	}		
	public Name getName() {
		return new Name(name);
	}

	//equals
	public boolean equals(Depositor info) {
		if (name.equals(info.name) && ssn.equals(info.ssn)) return true;
		else return false;
	}

	//to String
	public String toString() {
		String toStr = String.format("Your SSN: " + ssn);
		return toStr;
	}
	//format for file
	public String toFile() {
		String readIn = String.format("%-10s%-10s%-10s", name.getFirst(), name.getLast(), ssn);
		return readIn;
	}
}
