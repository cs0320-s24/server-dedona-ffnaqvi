package edu.brown.cs.student.main.Census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CensusAPIUtilities {

  /**
   * Deserializes JSON from the CensusAPI into an Activity object.
   *
   * @param jsonCensus
   * @return
   */
  public static Census deserializeCensus(String jsonCensus) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(List.class, Types.newParameterizedType(List.class, String.class));
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
      List<List<String>> censusData = adapter.fromJson(jsonCensus);

      // Extract values from listOfLists and populate a Census object
      Census census = new Census();
      if (!censusData.isEmpty()) {
        List<String> firstList = censusData.get(1); //skip the headers
        if (firstList.size() >= 3) {
          String[] parts = firstList.get(0).split(",\\s*");
          census.setCounty(parts[0]);
          census.setState(parts[1]);
          try {
            census.setPercentageOfAccess(Double.parseDouble(firstList.get(1)));
          } catch (NumberFormatException e) {
            System.out.println("precentange of broadband access is incorrect");
          }
        }
      }

      return census;
    } catch (IOException e) {
      e.printStackTrace();
      return new Census(); // Return default Census if deserialization fails
    }

  }

}
