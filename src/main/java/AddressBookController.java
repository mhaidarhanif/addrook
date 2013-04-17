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

// AddressBookController.java
// Controller class to connect GUI, actual application, and file system

import java.io.File;
import java.io.IOException;
import javax.swing.*;

import main.gui.AddrookGUI;

public class AddressBookController {

  private FileSystem fileSystem;

  /**
   * Address book with file system constructor
   */
  public AddressBookController(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * Add command button from GUI
   */
  public void doAdd(AddrookGUI gui) {

    String[] person =
        {"ID", "First Name", "Last Name", "Address", "City", "State", "ZIP", "Phone", "Email"};

    String[] personData =
        MultiInputPane.showMultiInputDialog(gui, person, "Add Person Information");

    if (personData != null) {
      gui.getAddressBook()
          .addPerson(Integer.parseInt(personData[0]),
                     // first and last name
                     personData[1], personData[2],
                     // address, city, state, zip
                     personData[3], personData[4], personData[5], personData[6],
                     // phone and email
                     personData[7], personData[8]);
    }

  }

  /**
   * Edit command button from GUI
   * by edit with corresponding person's id
   */
  public void doEdit(AddrookGUI gui, int id) {

    String name = gui.getAddressBook().getFullNameOfPerson(id);
    String[] personInfo =
        gui.getAddressBook().getOtherPersonInformation(id);
    String[] editInfo =
        {"Address", "City", "State", "ZIP", "Phone", "Email"};
    String[] personData =
        MultiInputPane
            .showMultiInputDialog(gui, editInfo, personInfo, "Edit " + name + " Information");
    if (personData != null) {
      gui.getAddressBook()
          .updatePerson(id, personData[0], personData[1], personData[2],
                        personData[3], personData[4], personData[5]);

    }

  }

  /**
   * Delete command button from GUI
   */
  public void doDelete(AddrookGUI gui, int id) {
    String name = gui.getAddressBook().getFullNameOfPerson(id);
    if (JOptionPane
            .showConfirmDialog(gui,
                               "Are you sure you want to delete " + name + "?",
                               "Confirm delete", 0) == 0) {
      gui.getAddressBook().removePerson(id);
    }
  }

  /**
   * Sort by Name command button from GUI
   */
  public void doSortByName(AddrookGUI gui) {
    gui.getAddressBook().sortByName();
  }

  /**
   * Sort by ZIP command button from GUI
   */
  public void doSortByZIP(AddrookGUI gui) {
    gui.getAddressBook().sortByZIP();
  }

  /**
   * Print all person information in active address book
   * command button from GUI
   */
  public void doPrint(AddrookGUI gui) {
    // get all output then store to be printed
    gui.getAddressBook().getOutputAll();
    // print on both system and GUI
    gui.getAddressBook().printSystemAll();
    gui.getAddressBook().printFrameAll();
  }

  /**
   * Create new address book
   * menu button from GUI
   */
  public void doNew(AddrookGUI gui) {
    gui.setAddressBook(new AddressBook());
  }

  /**
   * Open existing address book
   * menu button from GUI
   */
  public void doOpen(AddrookGUI gui)
      throws IOException, ClassCastException, ClassNotFoundException,
             InterruptedException, SecurityException {

    JFileChooser localJFileChooser = new JFileChooser(System.getProperty("user.dir"));

    if (localJFileChooser.showOpenDialog(gui) == 0) {
      File localFile = localJFileChooser.getSelectedFile();
      gui.setAddressBook(this.fileSystem.readFile(localFile));
    } else {
      throw new InterruptedException("Open cancelled by user");
    }

  }

  /**
   * Save active address book including available person
   * menu button from GUI
   */
  public void doSave(AddrookGUI gui)
      throws IOException, InterruptedException, SecurityException {
    File localFile = gui.getAddressBook().getFile();
    if (localFile == null) {
      doSaveAs(gui);
    } else {
      this.fileSystem.saveFile(gui.getAddressBook(), localFile);
    }
  }

  /**
   * Save new active address book including available person
   * menu button from GUI
   */
  public void doSaveAs(AddrookGUI gui)
      throws IOException, InterruptedException, SecurityException {
    JFileChooser localJFileChooser = new JFileChooser(System.getProperty("user.dir"));
    if (localJFileChooser.showSaveDialog(gui) == 0) {
      File localFile = localJFileChooser.getSelectedFile();
      this.fileSystem.saveFile(gui.getAddressBook(), localFile);
    } else {
      throw new InterruptedException("Save as cancelled by user");
    }
  }

  /**
   * Offer to save new changed address book including edited person
   * menu button from GUI
   */
  public void doOfferSaveChanges(AddrookGUI gui)
      throws InterruptedException, IOException, SecurityException {
    int i =
        JOptionPane.showConfirmDialog(gui, "There are unsaved changes. Save them?",
                                      "Save changes", 1);
    // choice option condition
    switch (i) {
      case 1:
        break;
      case 0:
        doSave(gui);
        break;
      case 2:
      default:
        throw new InterruptedException("Save changes cancelled by user");
    }

  }

}

