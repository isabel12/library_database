/*
 * LibraryModel.java
 * Author:
 * Created on:
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;


public class LibraryModel {

	private String url = "jdbc:postgresql://db.ecs.vuw.ac.nz/broomeisab_jdbc";
	private Connection conn;
	private ResultSet res;

	// For use in creating dialogs and making them modal
	private JFrame dialogParent;

	public LibraryModel(JFrame parent, String userid, String password){
		dialogParent = parent;

		// connect to the database
		try {
			this.conn = DriverManager.getConnection(url, userid, password);
			System.out.println("Connected to database " + url);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	//==========================================================================================
	// Formatting helper/wrapper methods
	//==========================================================================================
	/**
	 * Formats the title of the query.
	 * @param title
	 * @return
	 */
	private String formattedTitle(String title){
		int length = title.length();

		title += "\r\n";
		for (int i = 0; i < length; i++){
			title += "=";
		}
		title += "\r\n\r\n";
		return title;
	}

	private String newLine(){
		return "\r\n";
	}

	private String noResults(){
		return "(No results)\r\n\r\n";
	}

	//===========================================================================================

	public String bookLookup(int isbn) {

		try{
			String result = formattedTitle("Book Lookup");
			Statement stmt = conn.createStatement();
			Book book = new Book();

			// add info from Book
			String sql = String.format("SELECT * FROM Book WHERE ISBN = %d;", isbn);
			res = stmt.executeQuery(sql);
			// if no results
			if(!res.isBeforeFirst()){
				return result + noResults();
			}
			while(res.next()){
				book.ISBN = res.getInt("ISBN");
				book.Title = res.getString("Title");
				book.NumOfCop = res.getInt("NumOfCop");
				book.NumLeft = res.getInt("NumLeft");
			}

			// add info about Authors
			sql = String.format("SELECT AuthorSeqNo, Name, Surname FROM Author NATURAL JOIN (SELECT * FROM Book_Author WHERE ISBN = %d ORDER BY AuthorSeqNo) as BA;", isbn);
			res = stmt.executeQuery(sql);
			while(res.next()){
				Author author = new Author();
				author.Name = res.getString("Name").trim();
				author.Surname = res.getString("Surname").trim();
				book.authors.add(author);
			}

			// format string result
			result += book.toFullString();

			return result;

		} catch(SQLException e){
			return e.getMessage();
		}

	}

	public String showCatalogue() {
		try{

			Statement stmt = conn.createStatement();
			String result = formattedTitle("Show Catalogue");
			Map<Integer, Book> books = new HashMap<Integer, Book>();

			// get info from Book
			String sql = String.format("SELECT * FROM Book;");
			res = stmt.executeQuery(sql);
			// if no results
			if(!res.isBeforeFirst()){
				return result + noResults();
			}
			while(res.next()){
				Book book = new Book();
				book.ISBN = res.getInt("ISBN");
				book.Title = res.getString("Title");
				book.NumOfCop = res.getInt("NumOfCop");
				book.NumLeft = res.getInt("NumLeft");
				books.put(book.ISBN, book);
			}

			// get info from authors, zip together with books
			sql = "SELECT ISBN, AuthorSeqNo, Name, Surname FROM Author NATURAL JOIN Book_Author ORDER BY ISBN, AuthorSeqNo;";
			res = stmt.executeQuery(sql);
			while(res.next()){
				Author author = new Author();
				author.Name = res.getString("Name").trim();
				author.Surname = res.getString("Surname").trim();
				int ISBN = res.getInt("ISBN");
				books.get(ISBN).authors.add(author);
			}

			// print each book to result
			List<Integer> keys = new ArrayList<Integer>(books.keySet());
			Collections.sort(keys);
			for(int isbn: keys){
				result += books.get(isbn).toFullString() + newLine();
			}

			// print total number
			result += String.format("(%d results)", keys.size()) + newLine();

			return result;

		} catch(SQLException e){
			return e.getMessage();
		}
	}

	public String showLoanedBooks() {
		try {

			Statement stmt = conn.createStatement();
			String result = formattedTitle("Show Catalogue");
			Map<Integer, Book> books = new HashMap<Integer, Book>();

			// get info from Books
			String sql = String.format("SELECT * FROM Book WHERE ISBN IN (SELECT ISBN from Cust_Book);");
			res = stmt.executeQuery(sql);
			// if no results
			if(!res.isBeforeFirst()){
				return result + noResults();
			}
			while(res.next()){
				Book book = new Book();
				book.ISBN = res.getInt("ISBN");
				book.Title = res.getString("Title");
				book.NumOfCop = res.getInt("NumOfCop");
				book.NumLeft = res.getInt("NumLeft");
				books.put(book.ISBN, book);
			}

			// get info from authors, zip together with books
			sql = "SELECT ISBN, AuthorSeqNo, Name, Surname FROM Author NATURAL JOIN Book_Author WHERE ISBN IN (SELECT ISBN from Cust_Book) ORDER BY ISBN, AuthorSeqNo;";
			res = stmt.executeQuery(sql);
			while(res.next()){
				Author author = new Author();
				author.Name = res.getString("Name").trim();
				author.Surname = res.getString("Surname").trim();
				int ISBN = res.getInt("ISBN");
				books.get(ISBN).authors.add(author);
			}

			// get and add info about customers who are borrowing each book
			sql = "SELECT * FROM Customer NATURAL JOIN Cust_Book;";
			res = stmt.executeQuery(sql);
			while(res.next()){
				Customer c = new Customer();
				c.customerID = res.getInt("CustomerID");
				c.f_name = res.getString("F_Name").trim();
				c.l_name = res.getString("L_Name").trim();
				c.city = res.getString("City").trim();

				int ISBN = res.getInt("ISBN");
				books.get(ISBN).borrowedBy.add(c);
			}

			// print each book to result
			List<Integer> keys = new ArrayList<Integer>(books.keySet());
			Collections.sort(keys);
			for(int isbn: keys){
				result += books.get(isbn).toFullString() + newLine();
			}

			// print total number
			result += String.format("(%d results)", keys.size()) + newLine();

			return result;

		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String showAuthor(int authorID) {
		return "Show Author Stub";
	}

	public String showAllAuthors() {
		return "Show All Authors Stub";
	}

	public String showCustomer(int customerID) {
		return "Show Customer Stub";
	}

	public String showAllCustomers() {
		return "Show All Customers Stub";
	}

	public String borrowBook(int isbn, int customerID,
			int day, int month, int year) {
		return "Borrow Book Stub";
	}

	public String returnBook(int isbn, int customerid) {
		return "Return Book Stub";
	}

	public String deleteCus(int customerID){
		return "Delete Customer Stub";
	}

	public String deleteBook(int bookID){
		return "Delete Book Stub";
	}

	public String deleteAuthor(int authorID){
		return "Delete Author Stub";
	}

	public void closeDBConnection() {
	}
}
