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

// FileSystem.java
// File system class that access, read, and save file for input and output
// Used for file based storage instead of database file

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileSystem {

  public AddressBook readFile(File bookFile)
      throws IOException, ClassCastException, ClassNotFoundException {

    ObjectInputStream
        localObjectInputStream =
        new ObjectInputStream(new FileInputStream(bookFile));
    AddressBook localAddressBook = (AddressBook) localObjectInputStream.readObject();
    localAddressBook.setChangedSinceLastSave(false);
    localAddressBook.setFile(bookFile);
    return localAddressBook;

  }

  public void saveFile(AddressBook book, File bookFile)
      throws IOException {

    ObjectOutputStream
        localObjectOutputStream =
        new ObjectOutputStream(new FileOutputStream(bookFile));
    localObjectOutputStream.writeObject(book);
    book.setChangedSinceLastSave(false);
    book.setFile(bookFile);

  }

}

