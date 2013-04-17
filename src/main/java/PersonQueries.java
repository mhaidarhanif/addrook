package main.java;

/**
 * SMTI06, 54411850, M Haidar Hanif
 * Task Six: Address Book
 * Addrook | Simple address book simulator with basic features
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
  private static final String url = "jdbc:sqlite:addressbook.db";

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
              "CREATE TABLE IF NOT EXISTS Addresses" +
              "( addressID integer," +
              "firstName text NOT NULL," +
              "lastName text," +
              "email text," +
              "phoneNumber text," +
              "PRIMARY KEY(addressID)" +
              ")");
      createNewTable.execute();
      // create query that selects all entries in the AddressBook
      selectAllPeople =
          connection.prepareStatement(
              "SELECT * FROM Addresses");
      // create query that selects entries with a specific last name
      selectPeopleByLastName =
          connection.prepareStatement(
              "SELECT * FROM Addresses WHERE LastName = ?");
      // create insert that adds a new entry into the database
      insertNewPerson =
          connection.prepareStatement(
              "INSERT INTO Addresses " +
              "( FirstName, LastName, Email, PhoneNumber ) " +
              "VALUES ( ?, ?, ?, ? )");
    } catch (SQLException sqle) {
      System.err.println("\nUnable to use SQL " + driver);
      sqle.printStackTrace();
      sqle.printStackTrace(System.err);
    } catch (ClassNotFoundException cnfe) {
      System.err.println("\nUnable to load the JDBC driver " + driver);
      System.err.println("Please check your CLASSPATH.");
      cnfe.printStackTrace(System.err);
    } catch (InstantiationException ie) {
      System.err.println("\nUnable to instantiate the JDBC driver " + driver);
      ie.printStackTrace(System.err);
    } catch (IllegalAccessException iae) {
      System.err.println("\nNot allowed to access the JDBC driver " + driver);
      iae.printStackTrace(System.err); 
    }
  }

  // select all of the addresses in the database
  public List<Person> getAllPeople() {
    List<Person> results = null;
    ResultSet resultSet = null;
    try {
      // executeQuery returns ResultSet containing matching entries
      resultSet = selectAllPeople.executeQuery();
      results = new ArrayList<Person>();
      while (resultSet.next()) {
        results.add(new Person(
            resultSet.getInt("addressID"),
            resultSet.getString("firstName"),
            resultSet.getString("lastName"),
            resultSet.getString("email"),
            resultSet.getString("phoneNumber")));
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
            resultSet.getInt("addressID"),
            resultSet.getString("firstName"),
            resultSet.getString("lastName"),
            resultSet.getString("email"),
            resultSet.getString("phoneNumber")));
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
  public int addPerson(String fname, String lname, String email, String num) {
    int result = 0;
    // set parameters, then execute insertNewPerson
    try {
      insertNewPerson.setString(1, fname);
      insertNewPerson.setString(2, lname);
      insertNewPerson.setString(3, email);
      insertNewPerson.setString(4, num);
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
