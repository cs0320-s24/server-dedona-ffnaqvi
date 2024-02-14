package edu.brown.cs.student.main.Census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

// TODO: Model this class after the activity handler in the server gearup
public class CensusHandler implements Route {

  private String apiKey = "c4dae4f067d4a604595239338bf6e62c93bcdc34";
  private Map<String, String> stateCodes;

  public CensusHandler() {
    // Initialize the stateCodes map when the handler is created
    this.stateCodes = getStateCodes();
  }

  /**
   * This handle method needs to be filled by any class implementing Route. When the path set in
   * edu.brown.cs.examples.moshiExample.server.Server gets accessed, it will fire the handle method.
   *
   * <p>NOTE: beware this "return Object" and "throws Exception" idiom. We need to follow it because
   * the library uses it, but in general this lowers the protection of the type system.
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   */
  @Override
  public Object handle(Request request, Response response) throws IOException {

    String state = request.queryParams("state");
    System.out.println("State: "+state);

    String county = request.queryParams("county");
    System.out.println("County: "+county);

    String stateCode = this.stateCodes.get(state);
    String countyCode = getCountyCodes(stateCode,county);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String censusJson = this.sendRequest(stateCode,countyCode);
      // Deserializes JSON into an Activity
      Census census = CensusAPIUtilities.deserializeCensus(censusJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("census", census);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private String sendRequest(String stateCode, String countyCode)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?
    HttpRequest buildCensusApiRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"+ countyCode + "&in=state:" + stateCode+"&key="+this.apiKey))
            .GET()
            .build();


    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    System.out.println(sentCensusApiResponse);
    System.out.println(sentCensusApiResponse.body());
    System.out.println("before returning from sendRequest");

    return sentCensusApiResponse.body();
  }

  private Map<String, String> getStateCodes() {
    Map<String, String> codes = new HashMap<>();

    try {
      // Send a request to the API to get state names and codes
      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
          .GET()
          .build();

      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      // Parse the JSON response
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(
          Types.newParameterizedType(List.class, List.class, String.class)
      );
      List<List<String>> data = adapter.fromJson(response.body());

      // Populate the stateCodes map
      if (data != null && data.size() > 1) {
        List<String> header = data.get(0); // Assuming the first element is the header
        // Finds the index of the column named "state" in the header.
        // The indexOf method returns the index of the first occurrence of the specified element in the list.
        int stateIdIndex = header.indexOf("state");
        // Finds the index of the column named "NAME" in the header.
        // Similar to the previous line, it searches for the index of "NAME" in the header.
        int nameIndex = header.indexOf("NAME");

        for (int i = 1; i < data.size(); i++) {
          List<String> entry = data.get(i);
          codes.put(entry.get(nameIndex), entry.get(stateIdIndex));
        }
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      // Handle the exception as needed
    }

    return codes;
  }

  private String getCountyCodes(String stateCode, String targetCounty) throws IOException {
    try {
      // Send a request to the API to get county names and codes for a specific state
      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode))
          .GET()
          .build();

      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      // Parse the JSON response
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(
          Types.newParameterizedType(List.class, List.class, String.class)
      );
      List<List<String>> data = adapter.fromJson(response.body());

      // Assuming the first list element is the header
      List<String> header = data.get(0);
      int countyIndex = header.indexOf("county");
      int nameIndex = header.indexOf("NAME");

      // Find the county code based on the target county
      if (data != null) {
        for (int i = 1; i < data.size(); i++) { // Start from 1 to skip the header
          List<String> entry = data.get(i);

          // Extract the county name from the full string "<county>, <state>"
          String countyFullName = entry.get(nameIndex);

          // ,:    This part of the regular expression matches a literal comma.
          // \\s*: This part matches zero or more whitespace characters.
          String[] parts = countyFullName.split(",\\s*");
          String countyName = parts[0];

          // Compare the extracted county name with the target county
          if (countyName.equalsIgnoreCase(targetCounty)) {
            System.out.println("Found county!");
            return entry.get(countyIndex);
          }
        }
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      // Handle the exception as needed
    }

    // Return null if the county is not found
    throw new IOException("No found county");

}

}
