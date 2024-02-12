package edu.brown.cs.student.main.CSVParser;

import edu.brown.cs.student.main.Creators.CreatorFromRow;
import edu.brown.cs.student.main.Exceptions.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Pattern;

public class CSVParser<T> {

  private Reader reader;
  private CreatorFromRow<T> creator;
  private List<T> parsedData;

  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  public CSVParser(Reader reader, CreatorFromRow<T> creator) {
    this.reader = reader;
    this.creator = creator;
    this.parsedData = new ArrayList<>();
  }

  /**
   * Parses the CSV data from the provided reader and creates objects of type T using the specified
   * CreatorFromRow. The parsed data is stored in the internal list for further access.
   *
   * <p>This method reads each line from the input reader, splits it into CSV rows, and creates
   * objects of type T using the provided CreatorFromRow. It ensures that all rows have consistent
   * column sizes. If an inconsistency is detected, an InvalidObjectException is thrown. Any
   * FactoryFailureException thrown during the creation of data objects is caught, and an error
   * message is printed.
   *
   * @throws IOException If an I/O error occurs while reading the CSV data.
   */
  public void parse() throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(this.reader)) {
      String line = bufferedReader.readLine();
      int initColSize = -1;

      while (line != null) {
        try {
          List<String> row = splitCSVRow(line);
          int curColSize = row.size();

          if (initColSize == -1) {
            initColSize = curColSize;
          }

          if (initColSize != curColSize) {
            throw new InvalidObjectException("Inconsistent Column Sizes Detected");
          }
          T dataObject = this.creator.create(row);
          this.parsedData.add(dataObject);
        } catch (FactoryFailureException e) {
          System.out.println("Error in CSVParser");
        }

        line = bufferedReader.readLine();
      }
    }
    catch (IOException e) {
      System.out.println("Error reading from the CSV file: " + e.getMessage());
    }
  }

  /**
   * Splits a CSV row string into a list of strings based on a provided regular expression.
   *
   * <p>This method takes a CSV row string and splits it into individual elements using the provided
   * regular expression. It then trims each element to remove leading and trailing whitespaces.
   *
   * @param line The CSV row string to be split.
   * @return A List of strings representing the elements of the CSV row.
   */
  private List<String> splitCSVRow(String line) {
    // Use the provided regular expression to split the CSV row
    String[] split = regexSplitCSVRow.split(line);
    // Trim each element to remove leading and trailing whitespaces
    for (int i = 0; i < split.length; i++) {
      split[i] = this.postprocess(split[i]).trim();
    }
    return List.of(split);
  }

  /**
   * Elimiate a single instance of leading or trailing double-quote, and replace pairs of double
   * quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   */
  public static String postprocess(String arg) {
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

  /**
   * Getter for data made by parse in order to visualize and test output
   *
   * @return the parsedData
   */
  public List<T> getParsedData() {
    return this.parsedData;
  }
}
