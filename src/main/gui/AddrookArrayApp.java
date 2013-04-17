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

// AddrookArrayApp.java
// Main class to launch GUI application and be used by user

import java.awt.*;
import java.awt.event.WindowEvent;

import main.java.AddressBook;
import main.java.AddressBookController;
import main.java.FileSystem;

public class AddrookArrayApp {

  /**
   * Main method for program
   * Initiate application GUI
   *
   * @param args
   */
  public static void main(String[] args) {
    FileSystem fileSystem = new FileSystem();
    AddressBookController controller = new AddressBookController(fileSystem);
    AddrookGUI gui = new AddrookGUI(controller, new AddressBook());
    gui.setVisible(true);
  }

  /**
   * Terminate the application
   * (unless cancelled by the user)
   */
  public static void quitApplication() {

    // When the user requests to quit the application, any open
    // windows must be closed
    Frame[] openWindows = Frame.getFrames();

    for (int i = 0; i < openWindows.length; i++) {

      // Attempt to close any window that belongs to this program
      if (openWindows[i] instanceof AddrookGUI) {
        openWindows[i].dispatchEvent(new WindowEvent(
            openWindows[i],
            WindowEvent.WINDOW_CLOSING));
        // If the window is still showing, this means that this attempt
        // to close the window was cancelled by the user - so abort the
        // quit operation
        if (openWindows[i].isShowing()) {
          return;
        }
      }

    }

    // If we get here, all open windows have been successfully closed
    // (i.e. the user has not cancelled an offer to save any of them).
    // Thus, the application can terminate.
    System.exit(0);

  }

}
