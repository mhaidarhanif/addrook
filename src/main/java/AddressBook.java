package main.java;

/**
 * Part of Addrook
 * Simple address book simulator with basic features
 *
 * Improvement:
 * M Haidar Hanif
 *
 * Origin:
 * Russell C. Bjork (c) 2004-2005
 *   CPS211: Object-Oriented Software Development
 */

// AddressBook.java
// Observable class to add, modify, and delete person array

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Observable;
import java.util.Vector;

import javax.swing.*;

public class AddressBook extends Observable implements Serializable {

  private Vector collection;
  private transient File file;
  private transient boolean changedSinceLastSave;
  private String outputAll;

  public AddressBook() {
    this.collection = new Vector();
    this.file = null;
    this.changedSinceLastSave = false;
    this.outputAll = null;
  }

  public int getNumberOfPersons() {
    return this.collection.size();
  }

  public void addPerson(int id, String first, String last,
                        String address, String city, String state, String zip,
                        String phone, String email) {
    this.collection.addElement(
        new Person(id, first, last, address,
                   city, state, zip,
                   phone, email));
    setChangedSinceLastSave(true);
  }

  public String getFullNameOfPerson(int id) {
    Person localPerson = (Person) this.collection.elementAt(id);
    return localPerson.getFirstName() + " " + localPerson.getLastName();
  }

  public String getFullInfoOfPerson(int id) {
    Person localPerson = (Person) this.collection.elementAt(id);
    return localPerson.getPersonID() + ": " +
           localPerson.getFirstName() + " " + localPerson.getLastName() +
           " at " + localPerson.getAddress() + ", " + localPerson.getCity() +
           ", " + localPerson.getState() + " " + localPerson.getZIP() +
           " (" + localPerson.getPhone() + ")";
  }

  public String[] getOtherPersonInformation(int id) {
    Person localPerson = (Person) this.collection.elementAt(id);
    String[]
        arrayOfString =
        {localPerson.getAddress(), localPerson.getCity(), localPerson.getState(),
         localPerson.getZIP(), localPerson.getPhone(), localPerson.getEmail()};
    return arrayOfString;
  }

  public void updatePerson(int id, String address, String city, String state,
                           String zip, String phone, String email) {
    ((Person) this.collection.elementAt(id))
        .update(address, city, state, zip, phone, email);
    setChangedSinceLastSave(true);
  }

  public void removePerson(int id) {
    this.collection.removeElementAt(id);
    setChangedSinceLastSave(true);
  }

  public void sortByName() {
    Collections.sort(this.collection, new Person.CompareByName());
    setChangedSinceLastSave(true);
  }

  public void sortByZIP() {
    Collections.sort(this.collection, new Person.CompareByZIP());
    setChangedSinceLastSave(true);
  }

  /**
   * Get all output in mail address format
   * and store it in outputAll
   */
  public void getOutputAll() {
    this.outputAll = "Book: " + this.getTitle() + "\n"; // reset output storage first
    for (int i = 0; i < this.collection.size(); i++) {
      Person localPerson = (Person) this.collection.elementAt(i);
      this.outputAll += "\n" + localPerson.getFirstName() + " " + localPerson.getLastName();
      this.outputAll += "\n" + localPerson.getAddress();
      this.outputAll += "\n" + localPerson.getCity() + ", " + localPerson.getState() + ", " + localPerson.getZIP();
      this.outputAll += "\n" + localPerson.getPhone() + " <" + localPerson.getEmail() + ">" + "\n";
    }
  }

  /**
   * Print all output in mail address format
   * on system terminal/console which only
   * visible when launched via command line
   */
  public void printSystemAll() {
      System.out.println(outputAll);
  }

  /**
   * Print all output in mail address format
   * on pop-up GUI frame
   */
  public void printFrameAll() {
    JOptionPane.showMessageDialog(null, outputAll);
  }

  public File getFile() {
    return this.file;
  }

  public String getTitle() {
    // when the address book is new or none
    if (this.file == null) {
      return "Untitled Book";
    }
    // when the address book is exists
    return this.file.getName();
  }

  public void setFile(File bookFile) {
    this.file = bookFile;
    setChanged();
    notifyObservers();
  }

  public boolean getChangedSinceLastSave() {
    return this.changedSinceLastSave;
  }

  public void setChangedSinceLastSave(boolean changed) {
    this.changedSinceLastSave = changed;
    setChanged();
    notifyObservers();
  }

  // dummy data for validation test checking
  void setupTest() {
    addPerson(1, "Steve", "Rogers", "Address C", "City A", "CA", "11111", "000-0000", "captain@america.gov");
    addPerson(2, "Tony", "Stark", "Address I", "City M", "IM", "00000", "000-0000", "tony@stark.co");
    addPerson(3, "Nick", "Fury", "Address N", "City F", "NF", "99999", "000-0000", "fury@shield.org");
    addPerson(4, "Bruce", "Banner", "Address T", "City H", "TH", "22222", "000-0000", "bruce@ilab.org");
  }

}

