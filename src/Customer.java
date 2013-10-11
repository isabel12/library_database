import java.util.ArrayList;
import java.util.List;


public class Customer {
	int customerID;
	String f_name;
	String l_name;
	String city;
	List<Book> borrowing;

	public Customer(){
		borrowing = new ArrayList<Book>();
	}

	public String toFullString(){
		String result = toShortString();
		result += "\tBorrowed:\r\n";
		for(Book b: borrowing){
			result += "\t" + b.toShortString() + "\r\n";
		}
		return result;
	}

	public String toShortString(){
		return String.format("\t%d - %s, %s - %s\r\n", customerID, l_name, f_name, city);
	}
}