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
}
