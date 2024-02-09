package edu.brown.cs.student.main.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class to parse a given input
 *
 * @param <T> specifies the type of object the user is parsing for
 */
public class CSVParser<T> {

  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  private List<T> parsedContent;

  /**
   * Generic constructor for the CSVParser class
   *
   * <p>Uses the parse method and throws a FactoryFailureException upon invalid data entry
   *
   * @param reader a generic input
   * @param row a generic CreatorFromRow object
   */
  public CSVParser(Reader reader, CreatorFromRow<T> row) throws FactoryFailureException {
    this.parsedContent = new ArrayList<>();

    BufferedReader stream = new BufferedReader(reader);
    this.parse(stream, row);
  }

  /**
   * Method to parse the inputted reader file and sort into a specific type
   *
   * @param reader Buffered reader input
   * @param row generic type CreatorFromRow<T>
   * @throws FactoryFailureException upon inconsistent columns
   */
  private void parse(BufferedReader reader, CreatorFromRow<T> row) throws FactoryFailureException {
    try {
      String line = reader.readLine();
      int expectedColumnCount = -1; // Initialize with an invalid value
      while (line != null) {
        ArrayList<String> lineContent = new ArrayList<String>();
        String[] result = regexSplitCSVRow.split(line);

        // Check if expectedColumnCount is initialized
        if (expectedColumnCount == -1) {
          expectedColumnCount = result.length;
        } else {
          // Compare the current row's column count with the expected count
          if (result.length != expectedColumnCount) {
            System.err.println("Inconsistent columns while parsing.");
            throw new FactoryFailureException("Inconsistent columns while parsing.", lineContent);
          }
        }

        for (String str : result) {
          str = this.postprocess(str);
          lineContent.add(str);
        }

        T genericLine = row.create(lineContent);
        this.parsedContent.add(genericLine);

        line = reader.readLine();
      }
    } catch (IOException e) {
      System.err.println("I/O exception");
    }
  }

  /**
   * Getter method to get the contents of a parsed stream
   *
   * @return List<T> of stream content
   */
  public List<T> getContent() {
    return this.parsedContent;
  }

  /**
   * Eliminate a single instance of leading or trailing double-quote, and replace pairs of double
   * quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   */
  private static String postprocess(String arg) {
    return arg
        // Remove extra spaces at beginning and end of the line
        .trim()
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"");
  }
}
