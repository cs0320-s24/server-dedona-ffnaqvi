package edu.brown.cs.student.main.Creators;

import java.util.List;

/**
 * Implementation of the CreatorFromRow interface for creating String objects from a row of CSV
 * data.
 */
public class StringCreator implements CreatorFromRow<String> {
  /**
   * Creates a String object from the given row of CSV data.
   *
   * @param row The list of strings representing a row of CSV data.
   * @return A String object created from the CSV data.
   */
  @Override
  public String create(List<String> row) {
    // Assuming that the first element of the row contains the String data
    return row.get(0);
  }
}
