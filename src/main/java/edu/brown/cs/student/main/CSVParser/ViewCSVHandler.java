package edu.brown.cs.student.main.CSVParser;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.Server;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ViewCSVHandler implements Route {

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

    if (Server.loadStatus == 200) {
      String[][] parsedData = new String[Server.csvData.size()][Server.csvData.get(0).size()];
      for (int r = 0; r < parsedData.length; r ++) {
        for (int c = 0; c < parsedData[0].length; c++) {
          parsedData[r][c] = Server.csvData.get(r).get(c);
        }
      }
      return new ViewDataSuccessResponse(parsedData).serialize();
    }

    else {
      return new ViewDataFailureResponse("The CSV data hasn't been loaded yet").serialize();
    }

  }

  /*
   * Ultimately up to you how you want to structure your success and failure responses, but they
   * should be distinguishable in some form! We show one form here and another form in ActivityHandler
   * and you are also free to do your own way!
   */

  /** Response object to send, containing a soup with certain ingredients in it */
  public record ViewDataSuccessResponse(String response_type, String[][] data) {
    public ViewDataSuccessResponse(String[][] data) {
      this("success", data);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // Initialize Moshi which takes in this class and returns it as JSON!
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ViewDataSuccessResponse> adapter = moshi.adapter(ViewDataSuccessResponse.class);
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
  public record ViewDataFailureResponse(String response_type) {
    public ViewDataFailureResponse() {
      this("error viewing");
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(ViewDataFailureResponse.class).toJson(this);
    }
  }
}
