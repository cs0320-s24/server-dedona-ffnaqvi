package edu.brown.cs.student.main.Creators;

import java.util.List;

/* The Creator class, this is a class that specifies how to create an object of type <List<String>>. */
public class ListStringCreator implements CreatorFromRow<List<String>> {
  /**
   * Creates a List[String] object from the given row of CSV data.
   *
   * @param row The list of strings representing a row of CSV data.
   * @return A List[String] object created from the CSV data.
   */
  @Override
  public List<String> create(List<String> row) {
    return row;
  }
}
