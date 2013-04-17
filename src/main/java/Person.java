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

// Person.java
// Person class that represents an entry in an address book.

import java.io.Serializable;
import java.util.Comparator;

public class Person implements Serializable {

  private int personID;
  private String firstName;
  private String lastName;
  private String address;
  private String city;
  private String state;
  private String zip;
  private String email;
  private String phone;

  /**
   * Person constructor with no argument
   */
  public Person() {
    // empty constructor
  }

  /**
   * Person constructor with complete arguments
   */
  public Person(int id, String first, String last,
                String address, String city,
                String state, String zip,
                String phone, String email) {

    this.personID = id;
    this.firstName = first;
    this.lastName = last;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phone = phone;
    this.email = email;

  }

  /**
   * Various person attribute getter
   */

  public int getPersonID() {
    return this.personID;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getAddress() {
    return this.address;
  }

  public String getCity() {
    return this.city;
  }

  public String getState() {
    return this.state;
  }

  public String getZIP() {
    return this.zip;
  }

  public String getPhone() {
    return this.phone;
  }

  public String getEmail() {
    return this.email;
  }

  /**
   * Update for all person's information
   * excluding its name
   */
  public void update(String address, String city, String state,
                     String zip, String phone, String email) {

    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phone = phone;
    this.email = email;

  }

  /**
   * Comparator for comparing two persons by order of name
   */
  public static class CompareByName implements Comparator {

    /**
     * Compare two objects (which must both be Persons) by last name,
     * with ties broken by first name.
     *
     * @param person1 the first object
     * @param person2 the second object
     * @return a negative number if person1 belongs before person2 in alphabetical order of name;
     *         0 if they are equal; a positive number if person1 belongs after person2
     * @throws ClassCastException if either parameter is not a Person object
     */
    public int compare(Object person1, Object person2) {
      int compareByLast = ((Person) person1).getLastName().compareTo(
          ((Person) person2).getLastName());
      if (compareByLast != 0) {
        return compareByLast;
      } else {
        return ((Person) person1).getFirstName().compareTo(
            ((Person) person2).getFirstName());
      }
    }

    /**
     * Compare two objects (which must both be Persons) by name
     *
     * @param  person1 the first object
     * @param  person2 the second object
     * @return true if they have the same name, false if they do not
     * @throws ClassCastException if either parameter is not a Person object
     */
    public boolean equals(Object person1, Object person2) {
      return compare(person1, person2) == 0;
    }
  }

  /**
   * Comparator for comparing two persons by order of zip code
   */
  public static class CompareByZIP implements Comparator {

    public int compare(Object person1, Object person2) {
      int compareByZIP = ((Person) person1).getZIP().compareTo(
          ((Person) person2).getZIP());
      if (compareByZIP != 0) {
        return compareByZIP;
      } else {
        return new CompareByName().compare(person1, person2);
      }
    }

    /**
     * Compare two objects (which must both be Persons) by zip
     *
     * @param  person1 the first object
     * @param  person2 the second object
     * @return true if they have the same zip, false if they do not
     * @throws ClassCastException if either parameter is not a Person object
     */
    public boolean equals(Object person1, Object person2) {
      return compare(person1, person2) == 0;
    }

  }


}
