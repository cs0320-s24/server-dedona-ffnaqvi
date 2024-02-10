package edu.brown.cs.student.main.Creators;

import java.util.List;

/**
 * The IntegerCreator class implements the CreatorFromRow interface to create an Integer object from
 * a row of CSV data.
 */
public class IntegerCreator implements CreatorFromRow<Integer> {

  /**
   * Creates an Integer object from the given row of CSV data.
   *
   * @param row The list of strings representing a row of CSV data.
   * @return An Integer object created from the CSV data.
   */
  @Override
  public Integer create(List<String> row) {
    return Integer.parseInt(row.get(0));
  }
}
