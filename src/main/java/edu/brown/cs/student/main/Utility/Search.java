package edu.brown.cs.student.main.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the user to search the CSV file
 *
 * @param <T>
 */
public class Search<T> {

  private CSVParser<List<String>> parser;
  private String searchValue;
  private String colIdentifier;
  private int intCol;
  public List<List<String>> resultList;
  private List<List<String>> parsedContent;

  /**
   * Search constructor for user
   *
   * @param parsedObject CSVParser<T> that parsed the input
   * @param value String of the search value
   * @param columnIdentifier String of the specified column
   * @param headers boolean to determine if there are headers
   */
  public Search(
      CSVParser<List<String>> parsedObject,
      String value,
      String columnIdentifier,
      boolean headers) {

    this.parser = parsedObject;

    this.parsedContent = this.parser.getContent();

    this.searchValue = value;
    this.colIdentifier = columnIdentifier;
    try {
      this.intCol = Integer.parseInt(columnIdentifier);
      if (this.intCol == -1) {
        this.searchByRow();
      } else {
        this.searchByCol();
      }
    } catch (NumberFormatException e) {
      if (headers) {
        this.searchByCol();
      } else {
        this.searchByRow();
      }
    }
  }

  /**
   * Method to search all the rows in the inputted content
   *
   * @return List<List<String>> of data that contains the search value
   */
  private List<List<String>> searchByRow() {
    List<List<String>> returnList = new ArrayList<>();

    for (List<String> row : this.parsedContent) {
      for (String s : row) {
        if (s.equals(this.searchValue)) {
          System.out.println(row);
          returnList.add(row);
          break;
        }
      }
    }
    if (returnList.isEmpty()) {
      System.out.println(
          "Did not detect your specified String in this file. Please try another query!");
    }
    this.resultList = returnList;
    return returnList;
  }

  /**
   * Searches inputted data by specified column
   *
   * @return List<List<String>> of data that contains the search value
   */
  private List<List<String>> searchByCol() {
    int rows = this.parsedContent.size();
    int cols = this.parsedContent.get(0).size();
    List<List<String>> returnList = new ArrayList<>();

    int headerInd = this.intCol; // might be initialized to 0 forever if not properly used

    for (int i = 0; i < cols; i++) {
      if (this.parsedContent.get(0).get(i).equals(this.colIdentifier)) {
        headerInd = i;
      }
    }

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (c == headerInd && this.parsedContent.get(r).get(c).equals(searchValue)) {
          System.out.println(this.parsedContent.get(r));
          returnList.add(this.parsedContent.get(r));
          break;
        }
      }
    }
    if (returnList.isEmpty()) {
      System.out.println(
          "Did not detect your specified String in this file. Please try another query!");
    }
    this.resultList = returnList;
    return returnList;
  }
}
