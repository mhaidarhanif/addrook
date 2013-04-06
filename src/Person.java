/**
 * SMTI06, 54411850, M Haidar Hanif
 * Task Six: Address Book
 * Addrook | Simple address book simulator with basic features
 */

// Person.java
// Person class that represents an entry in an address book.

public class Person {

  private int addressID;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  // no-argument constructor
  public Person() {
    // empty constructor
  }

  // constructor
  public Person(int id, String first, String last,
                String emailAddress, String phone) {
    setAddressID(id);
    setFirstName(first);
    setLastName(last);
    setEmail(emailAddress);
    setPhoneNumber(phone);
  }

  // sets the addressID
  public void setAddressID(int id) {
    addressID = id;
  }

  // returns the addressID
  public int getAddressID() {
    return addressID;
  }

  // sets the firstName
  public void setFirstName(String first) {
    firstName = first;
  }

  // returns the first name
  public String getFirstName() {
    return firstName;
  }

  // sets the lastName
  public void setLastName(String last) {
    lastName = last;
  }

  // returns the first name
  public String getLastName() {
    return lastName;
  }

  // sets the email address
  public void setEmail(String emailAddress) {
    email = emailAddress;
  }

  // returns the email address
  public String getEmail() {
    return email;
  }

  // sets the phone number
  public void setPhoneNumber(String phone) {
    phoneNumber = phone;
  }

  // returns the email address
  public String getPhoneNumber() {
    return phoneNumber;
  }

}
