package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Creators.CreatorFromRow;
import edu.brown.cs.student.main.Creators.ListStringCreator;
import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.Search.Search;
import java.io.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/** The Main class of our project. This is where execution begins. */
public final class Main {

  private CSVParser<String> csvParser;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws IOException {
    try {
      new Main().run();
    } catch (IOException e) {
      System.exit(1); // Terminate the program on error
    }
  }

  /**
   * void function: repl
   *
   * <p>The purpose of repl is to get information from the user that is later necessary for
   * searching Specifically we are looking for: the file path (String filePath) the search value
   * (String searchValue) if the file has headers (boolean hasHeaders)
   *
   * <p>Reads from a file using a BufferedReader and writes the contents to a second file using a
   * BufferedWriter.
   */
  public void repl() throws IOException {

    // initialize BufferedReader to read from the terminal
    BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));

    // initialize variables for user input
    String filePath = "";
    String searchValue = "";
    boolean hasHeaders = false;

    // initialize variables used for loops
    String input = "";

    // initialize a Reader object in order to create a CSVParser object
    Reader reader = null;

    System.out.println("Welcome to the CSV parser! Type \"quit\" in order to stop the program.");
    while (!input.equals("y")) {
      try {
        System.out.println(
            "\nEnter a file path that follows the root path \"data/...\" for the csv file to be parsed:");
        filePath = terminalInput.readLine();
        if (filePath.equals("quit")) {
          System.exit(1);
        }

        // These line are necessary in order to comply with the project document stating:
        // "If the file cannot be found or opened, or an error is encountered when reading it,
        // your
        // program should print an appropriate error message via System.err.println rather than
        // crashing with an exception. After printing the error message, the program should
        // terminate."
        reader = new BufferedReader(new FileReader("data/" + filePath));

        while (!input.equals("y")) {
          System.out.println(
              "\nYou have entered the file path: data/" + filePath + "\nIs this correct? (y/n)");
          input = terminalInput.readLine();
          if (input.equals("quit")) {
            System.exit(1);
          }
          if (input.equals("n")) {
            break;
          }
        }
      } catch (FileNotFoundException e) {
        System.out.println("\nCould not find file " + filePath);
        System.exit(1); // Terminate the program on error
      }
    }

    // Set input to a NULL like value in order to reuse the variable for the next loop
    input = "";

    while (!input.equals("y")) {
      System.out.println("\nEnter a value to search for in data/" + filePath + ":");
      searchValue = terminalInput.readLine();
      if (searchValue.equals("quit")) {
        System.exit(1);
      }
      if (searchValue.equals("")) {
      } else {
        input = "";
        while (!input.equals("y")) {
          System.out.println(
              "\nYou have entered the value: " + searchValue + "\nIs this correct? (y/n)");
          input = terminalInput.readLine();
          if (input.equals("quit")) {
            System.exit(1);
          }
          if (input.equals("n")) {
            break;
          }
        }
      }
    }

    input = "";

    while (!input.equals("y")) {
      System.out.println("\nDoes your file contain file headers? (y/n)");
      input = terminalInput.readLine();
      if (input.equals("quit")) {
        System.exit(1);
      }
      if (input.equals("y")) {
        hasHeaders = true;
      }
      if (input.equals("n")) {
        break;
      }
    }

    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier = narrowData(hasHeaders);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    try {
      if (reader != null) {
        reader.close();
      }
    } catch (IOException e) {
      System.out.println(e.toString());
      System.out.println("\nCould not close file " + filePath);
      System.exit(1); // Terminate the program on error
    }
  }

  /**
   * Narrows the search based on user input for column identification.
   *
   * <p>This method prompts the user to specify whether they want to narrow their search via a
   * column identifier (either a column index, beginning with 0 for the left-most column, or a
   * column name). If the CSV data contains headers (hasHeaders is true), the user is given the
   * option to specify the identifier type and value interactively. If there are no headers, the
   * user is prompted to enter a column index directly.
   *
   * @param hasHeaders A boolean indicating whether the CSV data has headers.
   * @return A Map.Entry<String, Integer> containing the selected column identifier. The key is the
   *     column name (for identifier type "n"), and the value is the column index (for identifier
   *     type "i").
   * @throws IOException If an I/O error occurs while reading user input.
   */
  private Map.Entry<String, Integer> narrowData(boolean hasHeaders) throws IOException {

    BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));

    // initialize variables for user input
    String input = "";
    String indexOrName = "";
    String strColumnIdentifier = null;
    Integer intColumnIdentifier = null;

    if (hasHeaders) {
      while (!input.equals("y")) {
        System.out.println(
            "\nWould you like to narrow your search via column identifier (either a column index, "
                + "beginning with 0 for the left-most column, or a column name)? (y/n)");
        input = terminalInput.readLine();
        if (input.equals("quit")) {
          System.exit(1);
        } else if (input.equals("n")) {
          break;
        }
      }

      if (input.equals("y")) {
        input = "";
        while (!input.equals("y")) {
          while (!indexOrName.equals("i") && !indexOrName.equals("n")) {
            System.out.println(
                "\nSearch via column index or name? (\"i\" for index or \"n\" for name):");
            indexOrName = terminalInput.readLine();
            if (input.equals("quit")) {
              System.exit(1);
            }
          }

          if (indexOrName.equals("i")) {
            System.out.println(
                "\nYou have entered the following identifier: index\nIs this correct? (y/n)");
          } else if (indexOrName.equals("n")) {
            System.out.println(
                "\nYou have entered the following identifier: name\nIs this correct? (y/n)");
          }
          input = terminalInput.readLine();
          if (input.equals("quit")) {
            System.exit(1);
          }

          if (input.equals("n")) {
            break;
          }
        }

        // Set input to a NULL like value in order to reuse the variable for the next loop
        input = "";

        while (!input.equals("y")) {
          if (indexOrName.equals("i")) {
            System.out.println("\nEnter a column index:");
            try {
              intColumnIdentifier = Integer.parseInt(terminalInput.readLine());
            } catch (IOException e) {
            }
            while (!input.equals("y")) {
              System.out.println(
                  "\nYou have entered the following column index: "
                      + intColumnIdentifier
                      + "\nIs this correct? (y/n)");
              input = terminalInput.readLine();
              if (input.equals("quit")) {
                System.exit(1);
              }
              if (input.equals("n")) {
                break;
              }
            }
          } else if (indexOrName.equals("n")) {

            System.out.println("\nEnter a column name:");
            strColumnIdentifier = terminalInput.readLine();
            if (strColumnIdentifier.equals("quit")) {
              System.exit(1);
            }
            input = "";
            while (!input.equals("y")) {
              System.out.println(
                  "\nYou have entered the following column name: "
                      + strColumnIdentifier
                      + "\nIs this correct? (y/n)");
              input = terminalInput.readLine();
              if (input.equals("quit")) {
                System.exit(1);
              }
              if (input.equals("n")) {
                break;
              }
            }
          }
        }
      }
    } else {
      while (!input.equals("y")) {
        System.out.println(
            "\nWould you like to narrow your search via column identifier (a column index)? (y/n)");
        input = terminalInput.readLine();
        if (input.equals("quit")) {
          System.exit(1);
        }
        if (input.equals("n")) {
          break;
        }
      }

      if (input.equals("y")) {
        input = "";
        while (!input.equals("y")) {
          System.out.println("\nEnter a column index:");
          try {
            intColumnIdentifier = Integer.parseInt(terminalInput.readLine());
          } catch (IOException e) {
          }
          while (!input.equals("y")) {
            System.out.println(
                "\nYou have entered the following column index: "
                    + intColumnIdentifier
                    + "\nIs this correct? (y/n)");
            input = terminalInput.readLine();
            if (input.equals("quit")) {
              System.exit(1);
            }
            if (input.equals("n")) {
              break;
            }
          }
        }
      }
    }
    return new AbstractMap.SimpleEntry<>(strColumnIdentifier, intColumnIdentifier);
  }

  private void run() throws IOException {
    repl();
  }
}
