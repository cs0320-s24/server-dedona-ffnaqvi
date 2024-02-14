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
//    try {
//      // Initializes Moshi
//      Moshi moshi = new Moshi.Builder().build();
//
//      // Initializes an adapter to an Census class then uses it to parse the JSON.
//      JsonAdapter<Census> adapter = moshi.adapter(Census.class);
//
//      Census census = adapter.fromJson(jsonCensus);
//
//      return census;
//    }
//    // Returns an empty activity... Probably not the best handling of this error case...
//    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
//    // the error instead of pushing it up.
//    catch (IOException e) {
//      e.printStackTrace();
//      return new Census();
//    }
    try {
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(List.class, Types.newParameterizedType(List.class, String.class));
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
      List<List<String>> listOfLists = adapter.fromJson(jsonCensus);

      // Extract values from listOfLists and populate a Census object
      Census census = new Census();
      if (!listOfLists.isEmpty()) {
        List<String> firstList = listOfLists.get(0);
        if (firstList.size() >= 3) {
          census.setState(firstList.get(0));
          census.setCounty(firstList.get(1));
          try {
            census.setPercentageOfAccess(Integer.parseInt(firstList.get(2)));
          } catch (NumberFormatException e) {
            // Handle parsing error if percentageOfAccess is not a valid integer
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
