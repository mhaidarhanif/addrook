package main.gui;

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

// AddrookGUI.java
// Frame class for main GUI application.
// An object of this class allows interaction between the program and the human user.

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import main.java.AddressBook;
import main.java.AddressBookController;

public class AddrookGUI extends JFrame implements Observer {

  // GUI components and menu items
  private NameListModel nameListModel;
  private JList nameList;
  private JButton addButton, editButton, deleteButton, sortByNameButton, sortByZIPButton;
  private JMenuItem newItem, openItem, saveItem, saveAsItem, printItem, quitItem;
  // The controller that performs operations in response to user action gestures
  private AddressBookController controller;
  // The address book this GUI displays/operates on
  private AddressBook addressBook;

  /**
   * GUI Constructor
   *
   * @param controller  the controller which performs operations
   *                    in response to user gestures on this GUI
   * @param addressBook the AddressBook this GUI displays
   */

  public AddrookGUI(final AddressBookController controller,
                    AddressBook addressBook) {

    super("Addrook");
    this.controller = controller;

    // Create and add file menu
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    newItem = new JMenuItem("New", 'N');
    fileMenu.add(newItem);
    openItem = new JMenuItem("Open...", 'O');
    fileMenu.add(openItem);
    fileMenu.addSeparator();
    saveItem = new JMenuItem("Save", 'S');
    fileMenu.add(saveItem);
    saveAsItem = new JMenuItem("Save As...");
    fileMenu.add(saveAsItem);
    fileMenu.addSeparator();
    printItem = new JMenuItem("Print", 'P');
    fileMenu.add(printItem);
    fileMenu.addSeparator();
    quitItem = new JMenuItem("Quit", 'Q');
    fileMenu.add(quitItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);

    // The displayed list of names gets its information
    // from the address book
    nameListModel = new NameListModel();

    // The nameListModel and saveItem objects must exist before this is done;
    // but this must be done before the nameList is created
    setAddressBook(addressBook);

    // Create and add components for the main window

    JPanel buttonPanel = new JPanel();
    addButton = new JButton("    Add    ");
    buttonPanel.add(addButton);
    editButton = new JButton("    Edit    ");
    buttonPanel.add(editButton);
    deleteButton = new JButton("   Delete   ");
    buttonPanel.add(deleteButton);
    sortByNameButton = new JButton("Sort by name");
    buttonPanel.add(sortByNameButton);
    sortByZIPButton = new JButton("Sort by ZIP ");
    buttonPanel.add(sortByZIPButton);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
    getContentPane().add(buttonPanel, BorderLayout.NORTH);

    nameList = new JList(nameListModel);
    JScrollPane listPane = new JScrollPane(nameList);
    nameList.setVisibleRowCount(20);
    listPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(10, 10, 10, 10),
        BorderFactory.createLineBorder(Color.gray, 1)));
    getContentPane().add(listPane, BorderLayout.CENTER);

    // Add the action listeners for the buttons, menu items, close box,
    // and for double-clicking the list to edit information

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.doAdd(AddrookGUI.this);
        int index = getAddressBook().getNumberOfPersons() - 1;
        // This will ensure that the person just added is visible in list
        nameList.ensureIndexIsVisible(index);
      }
    });

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int index = nameList.getSelectedIndex();
        if (index < 0) {
          reportError("You must select a person before");
        } else {
          controller.doEdit(AddrookGUI.this, index);
        }
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int index = nameList.getSelectedIndex();
        if (index < 0) {
          reportError("You must select a person before");
        } else {
          controller.doDelete(AddrookGUI.this, index);
        }
      }
    });

    sortByNameButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.doSortByName(AddrookGUI.this);
      }
    });

    sortByZIPButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.doSortByZIP(AddrookGUI.this);
      }
    });

    newItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (getAddressBook().getChangedSinceLastSave()) {
            controller.doOfferSaveChanges(AddrookGUI.this);
          }
          controller.doNew(AddrookGUI.this);
        } catch (IOException exception) {
          reportError("Problem writing the file:\n" + exception);
        } catch (InterruptedException exception) {
          // Thrown if user cancels a save or a file dialog - can be ignored
        } catch (SecurityException exception) {
          // Thrown if security manager disallows the operation -
          // will always happen in an applet
          reportError("Operation disallowed:\n" + exception);
        }
      }
    });

    openItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (getAddressBook().getChangedSinceLastSave()) {
            controller.doOfferSaveChanges(AddrookGUI.this);
          }
          controller.doOpen(AddrookGUI.this);
        } catch (IOException exception) {
          reportError("Problem reading or writing the file:\n" + exception);
        } catch (InterruptedException exception) {
          // Thrown if user cancels a save or a file dialog - can be ignored
        } catch (SecurityException exception) {
          // Thrown if security manager disallows the operation -
          // will always happen in an applet
          reportError("Operation disallowed:\n" + exception);
        } catch (Exception exception) {
          // Any other case means the file did not contain an
          // address book
          reportError("This file did not contain an address book");
        }
      }
    });

    saveItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          controller.doSave(AddrookGUI.this);
        } catch (IOException exception) {
          reportError("Problem writing the file:\n" + exception);
        } catch (InterruptedException exception) {
          // Thrown if user cancels a file dialog - can be ignored
        } catch (SecurityException exception) {
          // Thrown if security manager disallows the operation -
          // will always happen in an applet
          reportError("Operation disallowed:\n" + exception);
        }
      }
    });

    saveAsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          controller.doSaveAs(AddrookGUI.this);
        } catch (IOException exception) {
          reportError("Problem writing the file:\n" + exception);
        } catch (InterruptedException exception) {
          // Thrown if user cancels a file dialog but can be ignored
        } catch (SecurityException exception) {
          // Thrown if security manager disallows the operation
          // It will always happen in an applet
          reportError("Operation disallowed:\n" + exception);
        }
      }
    });

    printItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.doPrint(AddrookGUI.this);
      }
    });

    quitItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        AddrookArrayApp.quitApplication();
      }
    });

    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {

        try {
          if (getAddressBook().getChangedSinceLastSave()) {
            controller.doOfferSaveChanges(AddrookGUI.this);
          }
          dispose();
          if (Frame.getFrames().length == 0) {
            AddrookArrayApp.quitApplication();
          }
        } catch (IOException exception) {
          reportError("Problem writing the file:\n" + exception);
        } catch (InterruptedException exception) {
          // Thrown if user cancels a file dialog but can be ignored
        } catch (SecurityException exception) {
          // Thrown if security manager disallows the operation
          // It will always happen in an applet
          reportError("Operation disallowed:\n" + exception);
        }

      }
    });

    /**
     * The following is adapted from an example in the documentation
     * for class JList. It invokes the controller's doEdit method
     * if the user double clicks a name.
     */
    nameList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          int index = nameList.locationToIndex(e.getPoint());
          controller.doEdit(AddrookGUI.this, index);
        }
      }
    });
    pack();
  }

  /**
   * Accessor for the address book this GUI displays
   *
   * @return the current address book for this GUI
   */
  public AddressBook getAddressBook() {
    return addressBook;
  }

  /**
   * Mutator to change the address book this GUI displays
   *
   * @param addressBook the new address book for this GUI
   */
  public void setAddressBook(AddressBook addressBook) {
    if (this.addressBook != null) {
      this.addressBook.deleteObserver(this);
    }
    this.addressBook = addressBook;
    addressBook.addObserver(this);
    update(addressBook, null);
  }

  /**
   * Report an error to the user
   *
   * @param message the message to display
   */
  public void reportError(String message) {
    JOptionPane.showMessageDialog(this, message, "Addrook | Error",
                                  JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Method required by the Observer interface
   * To update the display in response to any change in the address book
   */
  public void update(Observable o, Object arg) {
    if (o == addressBook) {
      setTitle("Addrook | " + addressBook.getTitle());
      saveItem.setEnabled(addressBook.getChangedSinceLastSave());
      nameListModel.contentsChanged();
    }
  }

  /**
   * Class used for the model for the list of persons in the address book
   */
  private class NameListModel extends AbstractListModel {

    // Report that the contents of the list have changed
    void contentsChanged() {
      super.fireContentsChanged(this, 0, 0);
    }

    // Implementation of abstract methods of the base class
    public Object getElementAt(int index) {
      return getAddressBook().getFullInfoOfPerson(index);
    }

    public int getSize() {
      return getAddressBook().getNumberOfPersons();
    }

  }

}
