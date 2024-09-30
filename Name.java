/*
 The Name class contains the users first and last name, as well as getters, no argument constructors, and parameterized
 constructors
 */
public class Name {
	private String first;
	private String last;
	
	//No-Arg Constructor
	public Name () {
		first = "";
		last = "";
	}
	
	//Parameterized Constructor
	public Name (String f, String l) {
		first = f;
		last = l;
	}
	
	//Copy Constructor
	public Name (Name name) {
		first = name.first;
		last = name.last;
	}
	
	//getters
	public String getLast() {
		return last;
	}		
	public String getFirst() {
		return first;
	}

	//equals
	public boolean equals(Name name) {
		if (first.equals(name.first) && last.equals(name.last) ) return true;
		else return false;
	}

	//to string
	public String toString() {
		String toStr = String.format("Your Last Name: " + last + "\nYour First Name: " + first);
		return toStr;
	}
}
