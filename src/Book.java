import java.util.ArrayList;
import java.util.List;


public class Book {
	public int ISBN;
	public String Title;
	public int NumOfCop;
	public int NumLeft;
	public List<Author> authors;
	public List<Customer> borrowedBy;

	public Book(){
		this.authors = new ArrayList<Author>();
		this.borrowedBy = new ArrayList<Customer>();
	}

	public String toFullString(){
		String result = "";
		result += String.format("\tISBN:\t%d\r\n", ISBN);
		result += String.format("\tTitle:\t%s\r\n", Title);
		result += String.format("\tNumOfCop:\t%d\r\n", NumOfCop);
		result += String.format("\tNumOfCop:\t%d\r\n", NumLeft);
		// print authors
		result += "\tAuthors:\t";
		if(authors.isEmpty()){
			result += "(None)";
		} else {
			for(Author auth: authors){
				result += auth.compactName() + ", ";
			}
			result = result.substring(0, result.length() - 3) + "\r\n"; // remove the last ', '
		}
		// print borrowed by if applicable
		if(!borrowedBy.isEmpty()){
			result += "\tBorrowed By:\r\n";
			for(Customer c: borrowedBy){
				result += "\t" + c.toShortString() + "\r\n";
			}
		}

		return result + "\r\n";
	}

	public String toShortString(){
		return String.format("\t%d - %s\r\n", ISBN, Title);
	}

}
