package edu.brown.cs.student.main.CSVParser;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonClass;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.CSVParser.LoadCSVHandler.LoadDataSuccessResponse;
import edu.brown.cs.student.main.Creators.CreatorFromRow;
import edu.brown.cs.student.main.Creators.ListStringCreator;
import edu.brown.cs.student.main.Search.Search;
import edu.brown.cs.student.main.Server;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCSVHandler implements Route {

  private int status;
  private List<List<String>> csvData;
  private CSVParser<List<String>> csvParserData;

  public SearchCSVHandler(/*int loadStatus,*/ List<List<String>> pCsvData, CSVParser<List<String>> pCsvParserData) {
//    status = loadStatus;
    this.csvData = pCsvData;
    this.csvParserData = pCsvParserData;
  }

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

    /*
    example of how to run:
    http://localhost:3232/searchCSV?searchValue=Barrington&hasHeaders=True
    searchValue: Barrington
    hasHeaders: true
    columnNameIdentifier: null
    columnIndexIdentifier: null
     */

    if (Server.loadStatus == 200) {
      String searchValue = request.queryParams("searchValue");
      boolean hasHeaders = Boolean.parseBoolean(request.queryParams("hasHeaders"));
      String columnNameIdentifier;
      Integer columnIndexIdentifier;

      try {
        columnNameIdentifier = request.queryParams("columnNameIdentifier");
      }
      catch (NullPointerException e) {
        columnNameIdentifier = null;
      }

      try {
        columnIndexIdentifier = Integer.parseInt(request.queryParams("columnIndexIdentifier"));
      }
      catch (NullPointerException | NumberFormatException e) {
        columnIndexIdentifier = null;
      }

      try {
        //TODO: handle searching and printing the CSV data
        System.out.println("searchValue: " + searchValue);
        System.out.println("columnNameIdentifier: " + columnNameIdentifier);
        System.out.println("columnIndexIdentifier: " + columnIndexIdentifier);
        System.out.println("hasHeaders: " + hasHeaders);
        CSVParser<List<String>> parser = this.csvParserData;
        Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(
            columnNameIdentifier, columnIndexIdentifier);
        Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
        search.search();
        List<List<String>> searchResults = search.getResultList();
        return new SearchDataSuccessResponse(searchResults).serialize();
      }
      catch (Exception e) {
      // Handle any other unexpected exceptions
      e.printStackTrace();
      throw new RuntimeException("Unexpected error during processing: " + e.getMessage());
    }

//      return new SearchDataSuccessResponse();
    }
    if (this.status != 200) {
      return new SearchDataFailureResponse("The CSV has not been loaded yet").serialize();

    }
    return new SearchDataFailureResponse().serialize();


  }

  /** Response object to send, containing a soup with certain ingredients in it */
  @JsonClass(generateAdapter = true)
  /*
  used in conjunction with the Moshi library in Java to automatically generate a JSON adapter for a
  class. Moshi uses code generation to create a specialized adapter that knows how to serialize and
  deserialize instances of the annotated class to and from JSON.

  @JsonClass: This annotation indicates to Moshi that it should generate a JSON adapter for the
        annotated class.
  generateAdapter = true: This is a parameter of the @JsonClass annotation. When set to true, it
        instructs Moshi to generate an adapter for the annotated class during the compilation
        process. The generated adapter will be tailored to the structure of the annotated class,
        making the serialization and deserialization processes efficient and specific to that class.

  Using @JsonClass(generateAdapter = true) is a convenient way to avoid manually writing JSON
  adapters for your classes. Moshi takes care of the boilerplate code needed for serialization and
  deserialization based on the structure of your class.
   */
  public record SearchDataSuccessResponse(String response_type, List<List<String>> responseMap) {
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
        JsonAdapter<SearchDataSuccessResponse> adapter = moshi.adapter(SearchDataSuccessResponse.class);
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
