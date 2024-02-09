package edu.brown.cs.student.main.Utility;

import java.util.List;

/** RowCreator is a class for type of List<String> that implements the CreatorFromRow Interface */
public class RowCreator implements CreatorFromRow<List<String>> {
  /**
   * Altered this method to return a List string to more easily parse it
   *
   * @param row
   * @return
   * @throws FactoryFailureException
   */
  @Override
  public List<String> create(List<String> row) {
    return row;
  }
}
