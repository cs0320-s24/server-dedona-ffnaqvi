package edu.brown.cs.student.main.CSVParser;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.Server;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCSVHandler implements Route {

  /**
   * Pick a convenient soup and make it. the most "convenient" soup is the first recipe we find in
   * the unordered set of recipe cards.
   *
   * <p>NOTE: beware this "return Object" and "throws Exception" idiom. We need to follow it because
   * the library uses it, but in general this lowers the protection of the type system.
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   * @throws Exception This is part of the interface; we don't have to throw anything.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    // Get Query parameters, can be used to make your search more specific
    String cityName = request.queryParams("city");
    String medianHouseIncome = request.queryParams("medianHouseIncome");
    String medianFamilyIncome = request.queryParams("medianFamilyIncome");
    String perCapitaIncome = request.queryParams("perCapitaIncome");

    if (Server.loadStatus == 200) {
      //TODO: handle searching and printing the CSV data
//      return new SearchDataSuccessResponse();
    }
    if (Server.loadStatus != 200) {
      return new SearchDataFailureResponse("The CSV has not been loaded yet").serialize();

    }
    return new SearchDataFailureResponse().serialize();
  }

  /*
   * Ultimately up to you how you want to structure your success and failure responses, but they
   * should be distinguishable in some form! We show one form here and another form in ActivityHandler
   * and you are also free to do your own way!
   */

  /** Response object to send, containing a soup with certain ingredients in it */
  public record SearchDataSuccessResponse(String response_type, Map<String, Object> responseMap) {
    public SearchDataSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // Initialize Moshi which takes in this class and returns it as JSON!
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SearchDataSuccessResponse> adapter = moshi.adapter(SearchDataSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        // For debugging purposes, show in the console _why_ this fails
        // Otherwise we'll just get an error 500 from the API in integration
        // testing.
        e.printStackTrace();
        throw e;
      }
    }
  }

  /** Response object to send if someone requested data from an invalid csv */
  public record SearchDataFailureResponse(String response_type) {
    public SearchDataFailureResponse() {
      this("error searching");
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
