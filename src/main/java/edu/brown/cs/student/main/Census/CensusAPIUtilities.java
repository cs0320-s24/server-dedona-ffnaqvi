package edu.brown.cs.student.main.Census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/** Class to handle the API utilities specific to */
public class CensusAPIUtilities {

  /**
   * Deserializes JSON from the CensusAPI into an Activity object.
   *
   * @param jsonCensus
   * @return
   */
  public static List<Census> deserializeCensus(String jsonCensus) {
    List<Census> censusList = new ArrayList<>();
    try {
      Moshi moshi = new Moshi.Builder().build();
      Type type =
          Types.newParameterizedType(
              List.class, Types.newParameterizedType(List.class, String.class));
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
      List<List<String>> censusData = adapter.fromJson(jsonCensus);

      // Extract values from listOfLists and populate a Census object
      if (!censusData.isEmpty()) {
        for (int i = 1; i < censusData.size(); i++) {
          Census census = new Census();

          if (censusData.get(i).size() >= 3) {
            String[] parts =
                censusData.get(i).get(0).split(",\\s*"); // splits the name of the state/county
            census.setCounty(parts[0]);
            census.setState(parts[1]);
            try {
              census.setPercentageOfAccess(Double.parseDouble(censusData.get(i).get(1)));
            } catch (NumberFormatException e) {
              System.out.println("percentange of broadband access is incorrect");
            }
            censusList.add(census);
          }
        }
      }

      return censusList;
    } catch (IOException e) {
      e.printStackTrace();
      return censusList; // Returns empty list
    }
  }
}
