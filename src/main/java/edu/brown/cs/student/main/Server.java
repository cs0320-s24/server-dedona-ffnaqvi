package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Utility.*;
import java.io.*;

/** The Main class of our project. This is where execution begins. */
public final class Server {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws IOException {
    new Server(args).run();
  }

  private Server(String[] args) {}

  /**
   * Method to run the program
   *
   * @throws IOException
   */
  private void run() throws IOException {
    this.REPL();
  }

  /**
   * Method to create a REPL that interacts with the user through the terminal. Collects the data
   * needed to pass into CSVParser and Search classes.
   *
   * @throws IOException
   */
  private void REPL() throws IOException {
    boolean headers = false;
    System.out.println(
        "Welcome to Search! This is a REPL for user interaction that will allow you to search a "
            + "specified file for a specified input.\nThis search tool is cAsE SeNsiTivE. To exit,"
            + " type exit and press enter at any time.");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    // implement a REPL
    while (true) {
      // collects the file name from the user and throws exception if file is not found
      FileReader file = null;
      System.out.println(
          "\nPlease enter a valid file name. All files must be within the data folder.");

      String line = input.readLine();
      this.exit(line);
      String fileName = "data/" + line;

      try {
        file = new FileReader(fileName);
      } catch (FileNotFoundException e) {
        System.err.println("Your file name was invalid. Restarting the REPL!");
        continue;
      }

      // collects the search string from the user
      System.out.println("Please input the String you are searching for.");
      line = input.readLine();
      String value = line;
      this.exit(line);
      if (value.equals("")) {
        System.out.println("Please enter a valid search value. Restarting the REPL!");
        continue;
      }

      // add a y/n for boolean of col headers
      System.out.println("Does your file have headers? Enter y for yes, n for no");
      line = input.readLine();
      this.exit(line);
      if (line.equals("y")) {
        headers = true;
      }

      // check for specified column
      System.out.println(
          "if you know the column identifier of the item you are searching for, please input it now. Otherwise, press enter.");
      line = input.readLine();
      this.exit(line);
      if (line.equals("")) {
        line = "-1";
      }

      // creates a parser from the collected data
      CreatorFromRow row = new RowCreator();
      try {
        CSVParser<String> parser = new CSVParser<String>(file, row);
        new Search(parser, value, line, headers);
      } catch (FactoryFailureException e) {
        System.err.println(
            "There was a factory failure exception based on the csv file. Restarting the REPL!");
      }
    }
  }

  /**
   * Method to exit upon exit input
   *
   * @param line is terminal input
   */
  private void exit(String line) {
    if (line.equals("exit")) {
      System.exit(0);
    }
  }
}
