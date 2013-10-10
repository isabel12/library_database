import java.util.List;


public class Author {

	int AuthorId;
	String Name;
	String Surname;
	List<Book> booksAuthored;

	public String compactName(){
		return Name.charAt(0) + ". " + Surname;
	}
}
