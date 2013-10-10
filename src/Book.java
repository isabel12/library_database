import java.util.ArrayList;
import java.util.List;


public class Book {
	public int ISBN;
	public String Title;
	public int NumOfCop;
	public int NumLeft;
	public List<Author> authors;

	public Book(){
		this.authors = new ArrayList<Author>();
	}

	public String toFullString(){
		String result = "";
		result += String.format("\tISBN:\t%d\r\n", ISBN);
		result += String.format("\tTitle:\t%s\r\n", Title);
		result += String.format("\tNumOfCop:\t%d\r\n", NumOfCop);
		result += String.format("\tNumOfCop:\t%d\r\n", NumLeft);
		result += "\tAuthors:\t";
		if(authors.isEmpty()){
			result += "(None)";
		} else {
			for(Author auth: authors){
				result += auth.compactName() + ", ";
			}
			result = result.substring(0, result.length() - 3); // remove the last ', '
		}
		result += "\r\n";

		return result;
	}

}
