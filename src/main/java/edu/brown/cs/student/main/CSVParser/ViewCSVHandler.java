package edu.brown.cs.student.main.CSVParser;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.Server;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * ViewCSVHandler is a class that serves as an endpoint
 * for the proxy server by displaying the results of a CSV
 */
public class ViewCSVHandler implements Route {
  private List<List<String>> csvData;

  /**
   * function to display a CSV if the CVS has been loaded
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   * @throws Exception This is part of the interface; we don't have to throw anything.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    if (Server.loadStatus == 200) {
      this.csvData = Server.parser.getParsedData();
      String[][] parsedData = new String[this.csvData.size()][this.csvData.get(0).size()];
      for (int r = 0; r < parsedData.length; r++) {
        for (int c = 0; c < parsedData[0].length; c++) {
          parsedData[r][c] = this.csvData.get(r).get(c);
        }
      }
      return new ViewDataSuccessResponse(parsedData).serialize();
    } else {
      return new ViewDataFailureResponse("The CSV data hasn't been loaded yet").serialize();
    }
  }

    /** Response object to send, displaying the constants of a CSV */
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
