package edu.brown.cs.student.main.CSVParser;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.Creators.ListStringCreator;
import edu.brown.cs.student.main.Search.Search;
import edu.brown.cs.student.main.server.Server;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * SearchCSVHandler is a class that serves as an endpoint for the proxy server by searching a CSV
 * and displaying the results
 */
public class SearchCSVHandler implements Route {
  private List<List<String>> csvData;

  /**
   * Function that checks if a CSV has been loaded and performs search if true
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   * @throws Exception This is part of the interface; we don't have to throw anything.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    // Get Query parameters, can be used to make your search more specific
    if (Server.loadStatus == 200) {

      String searchValue = request.queryParams("searchValue");
      if (searchValue == null) {
        return new SearchDataFailureResponse(
            "no search value found, please enter a value to search for by using \"http://localhost:3232/searchcsv?searchValue=&lt;value&gt;.\""
                + " Other parameters include \"hasHeaders=&lt;boolean&gt;\", \"columnNameIdentifier=&lt;string&gt;\", and  \"columnIndexIdentifier=&lt;int&gt;\" ");
      }

      boolean hasHeaders = Boolean.parseBoolean(request.queryParams("hasHeaders"));

      String columnNameIdentifier = null;
      Integer columnIndexIdentifier = null;

      try {
        columnNameIdentifier = request.queryParams("columnNameIdentifier");
      } catch (NullPointerException e) {
        // no error handling needed
      }

      try {
        columnIndexIdentifier = Integer.parseInt(request.queryParams("columnIndexIdentifier"));
      } catch (NullPointerException | NumberFormatException e) {
        // no error handling needed
      }

      try {
        Map.Entry<String, Integer> columnIdentifier =
            new AbstractMap.SimpleEntry<>(columnNameIdentifier, columnIndexIdentifier);
        CSVParser<List<String>> parser =
            new CSVParser<>(
                new BufferedReader(new FileReader("datasource/" + Server.fileName)),
                new ListStringCreator());
        Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
        int result = search.search();
        if (result == 1) {
          return new SearchCSVHandler.SearchDataFailureResponse(
              "Invalid column index, please check your CSV file");
        } else if (result ==2) {
          return new SearchCSVHandler.SearchDataFailureResponse(
              "Invalid column name, please check your CSV headers");
        } else if (result == 3) {
          return new SearchCSVHandler.SearchDataFailureResponse(
              "Could not find any corresponding data");
        } else if (result == 4) {
          return new SearchCSVHandler.SearchDataFailureResponse("Error in Searching");
        }

        search.search();
        this.csvData = search.getResultList();

        return new SearchDataSuccessResponse(this.csvData).serialize();
      } catch (Exception e) {
        // Handle any other unexpected exceptions
        e.printStackTrace();
        throw new RuntimeException("Unexpected error during processing: " + e.getMessage());
      }
    }
    if (Server.loadStatus != 200) {
      return new SearchDataFailureResponse("The CSV has not been loaded yet").serialize();
    }
    return new SearchDataFailureResponse("").serialize();
  }

  /** Response object to send, containing the requested data */
  public record SearchDataSuccessResponse(String response_type, List<List<String>> responseData) {
    public SearchDataSuccessResponse(List<List<String>> responseData) {
      this("success", responseData);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // Initialize Moshi which takes in this class and returns it as JSON!
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SearchDataSuccessResponse> adapter =
            moshi.adapter(SearchDataSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        // For debugging purposes, show in the console _why_ this fails
        // Otherwise we'll just get an error 500 from the API in integration
        // testing.
        e.printStackTrace();
        throw new RuntimeException("Error during Moshi serialization: " + e.getMessage());
      }
    }
  }

  /** Response object to send if someone requested data from an invalid csv */
  public record SearchDataFailureResponse(String response_type, String reply) {
    public SearchDataFailureResponse(String reply) {
      this("error searching", reply);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchDataFailureResponse.class).toJson(this);
    }
  }
}
