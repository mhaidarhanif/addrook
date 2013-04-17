package main.java;

/**
 * Part of Addrook
 * Simple address book simulator with basic features
 *
 * Improvement:
 * M Haidar Hanif
 *
 * Origin:
 * Paul Deitel & Harvey Deitel (c) 2006
 *   Java How to Program, 7th Edition
 * Russell C. Bjork (c) 2004-2005
 *   CPS211: Object-Oriented Software Development
 */

// PersonQueries.java
// PreparedStatements used by the Address Book application.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonQueries {

  public static final String driver = "org.sqlite.JDBC";
  private static final String url = "jdbc:sqlite:addrookpeople.db";

  private Connection connection = null; // manages connection
  private PreparedStatement createNewTable = null;
  private PreparedStatement selectAllPeople = null;
  private PreparedStatement selectPeopleByLastName = null;
  private PreparedStatement insertNewPerson = null;

  // constructor
  public PersonQueries() {
    try {
      Class.forName(driver).newInstance();
      connection = DriverManager.getConnection(url);
      // create query that create a new table for AddressBook
      createNewTable =
          connection.prepareStatement(
              "CREATE TABLE IF NOT EXISTS People" +
              "( PersonID integer," +
              "FirstName text NOT NULL," +
              "LastName text," +
              "Address text," +
              "City text," +
              "State text," +
              "ZIP text," +
              "Phone text," +
              "Email text," +
              "PRIMARY KEY(PersonID)" +
              ")");
      createNewTable.execute();
      // create query that selects all entries in the AddressBook
      selectAllPeople =
          connection.prepareStatement(
              "SELECT * FROM People");
      // create query that selects entries with a specific last name
      selectPeopleByLastName =
          connection.prepareStatement(
              "SELECT * FROM People WHERE LastName = ?");
      // create insert that adds a new entry into the database
      insertNewPerson =
          connection.prepareStatement(
              "INSERT INTO People " +
              "( FirstName, LastName, Address, City, State, ZIP, Phone, Email ) " +
              "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )");
    } catch (SQLException sqle) {
      System.err.println("\nUnable to use SQL\n" + driver);
      sqle.printStackTrace();
      sqle.printStackTrace(System.err);
    } catch (ClassNotFoundException cnfe) {
      System.err.println("\nUnable to load the JDBC driver\n" + driver);
      System.err.println("\nPlease check your CLASSPATH.");
      cnfe.printStackTrace(System.err);
    } catch (InstantiationException ie) {
      System.err.println("\nUnable to instantiate the JDBC driver\n" + driver);
      ie.printStackTrace(System.err);
    } catch (IllegalAccessException iae) {
      System.err.println("\nNot allowed to access the JDBC driver\n" + driver);
      iae.printStackTrace(System.err); 
    }
  }

  // select all of the person information in the database
  public List<Person> getAllPeople() {
    List<Person> results = null;
    ResultSet resultSet = null;
    try {
      // executeQuery returns ResultSet containing matching entries
      resultSet = selectAllPeople.executeQuery();
      results = new ArrayList<Person>();
      while (resultSet.next()) {
        results.add(new Person(
            resultSet.getInt("PersonID"),
            resultSet.getString("FirstName"),
            resultSet.getString("LastName"),
            resultSet.getString("Address"),
            resultSet.getString("City"),
            resultSet.getString("State"),
            resultSet.getString("ZIP"),
            resultSet.getString("Phone"),
            resultSet.getString("Email")
            ));
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    } finally {
      try {
        resultSet.close();
      } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        close();
      }
    }
    return results;
  }

  // select person by last name
  public List<Person> getPeopleByLastName(String name) {
    List<Person> results = null;
    ResultSet resultSet = null;
    try {
      selectPeopleByLastName.setString(1, name); // specify last name
      // executeQuery returns ResultSet containing matching entries
      resultSet = selectPeopleByLastName.executeQuery();
      results = new ArrayList<Person>();
      while (resultSet.next()) {
        results.add(new Person(
            resultSet.getInt("PersonID"),
            resultSet.getString("FirstName"),
            resultSet.getString("LastName"),
            resultSet.getString("Address"),
            resultSet.getString("City"),
            resultSet.getString("State"),
            resultSet.getString("ZIP"),
            resultSet.getString("Phone"),
            resultSet.getString("Email")));
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    } finally {
      try {
        resultSet.close();
      } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        close();
      }
    }
    return results;
  }

  // add an entry
  public int addPerson(String first, String last, String phone, String email) {
    int result = 0;
    // set parameters, then execute insertNewPerson
    try {
      insertNewPerson.setString(1, first);
      insertNewPerson.setString(2, last);
      insertNewPerson.setString(3, "address");
      insertNewPerson.setString(4, "city");
      insertNewPerson.setString(5, "state");
      insertNewPerson.setString(6, "zip");
      insertNewPerson.setString(7, phone);
      insertNewPerson.setString(8, email);
      // insert the new entry; returns # of rows updated
      result = insertNewPerson.executeUpdate();
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      close();
    }
    return result;
  }

  // close the database connection
  public void close() {
    try {
      connection.close();
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

}
