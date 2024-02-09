package edu.brown.cs.student.main.Flower;

import edu.brown.cs.student.main.Utility.CreatorFromRow;
import edu.brown.cs.student.main.Utility.FactoryFailureException;
import java.util.ArrayList;
import java.util.List;

/** FlowerRowCreator class to determine how to store rows of Flowers */
public class FlowerRowCreator implements CreatorFromRow<List<Flower>> {

  /**
   * Altered this method to return a List string to more easily parse it
   *
   * @param row of parsed input
   * @return List<Flower>
   * @throws FactoryFailureException upon parsing error
   */
  @Override
  public List<Flower> create(List<String> row) {
    List<Flower> flowerList = new ArrayList<>();
    for (String s : row) {
      for (Flower f : Flower.values()) {
        if (f.equals(s)) {
          flowerList.add(f);
        }
      }
    }
    return flowerList;
  }
}
