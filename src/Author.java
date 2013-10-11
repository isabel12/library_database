import java.util.ArrayList;
import java.util.List;


public class Author {

	int AuthorId;
	String Name;
	String Surname;
	List<Book> booksAuthored;

	public Author(){
		this.booksAuthored = new ArrayList<Book>();
	}

	public String compactName(){
		return Name.charAt(0) + ". " + Surname; // note. no carriage return here
	}

	public String toFullString(){
		String result = toShortString();
		if(booksAuthored != null && !booksAuthored.isEmpty()){
			result += "\tBooks Authored:\r\n";
			for(Book b: booksAuthored){
				result += "\t" + b.toShortString();
			}
		}
		return result;
	}

	public String toShortString(){
		return String.format("\t%d - %s, %s\r\n", AuthorId, Surname, Name);
	}

}
