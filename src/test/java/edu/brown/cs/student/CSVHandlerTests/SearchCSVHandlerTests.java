package edu.brown.cs.student.CSVHandlerTests;

import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import edu.brown.cs.student.main.CSVParser.SearchCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import spark.Spark;

/** This test class tests the functionality of the SearchCSVHandler class */
public class SearchCSVHandlerTests {
  @BeforeEach
  public void setup() {
    Spark.get("loadCSV", new LoadCSVHandler());
    Spark.get("searchCSV", new SearchCSVHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("loadCSV");
    Spark.unmap("searchCSV");
    Spark.awaitStop();
  }

  /**
   * Helper method to send requests to the API
   *
   * @param apiCall
   * @return
   * @throws IOException
   */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Tests search with valid parameters
   *
   * @throws IOException
   */
  @Test
  public void testSearchCSVWithValidParameters() throws IOException {
    // Load CSV data
    HttpURLConnection loadConnection = tryRequest("loadCSV?fileName=stardata.csv");
    Assert.assertEquals(200, loadConnection.getResponseCode());

    // Search CSV with valid parameters
    HttpURLConnection searchConnection = tryRequest("searchCSV?searchValue=example");
    int searchResponseCode = searchConnection.getResponseCode();

    // Ensure that the search operation is successful (response code 200)
    Assert.assertEquals(200, searchResponseCode);

    // Don't forget to disconnect the connections
    loadConnection.disconnect();
    searchConnection.disconnect();
  }

  /**
   * Tests search with invalid parameters
   *
   * @throws IOException
   */
  @Test
  public void testSearchCSVWithInvalidParameters() throws IOException {
    // Load CSV data
    HttpURLConnection loadConnection = tryRequest("loadCSV?fileName=stardata.csv");
    Assert.assertEquals(200, loadConnection.getResponseCode());

    // Search CSV with invalid parameters
    HttpURLConnection searchConnection =
        tryRequest("searchCSV?searchValue=value&columnIndexIdentifier=-1");
    int searchResponseCode = searchConnection.getResponseCode();

    // Ensure that the search operation fails (response code 404)
    Assert.assertEquals(200, searchResponseCode);

    // Don't forget to disconnect the connections
    loadConnection.disconnect();
    searchConnection.disconnect();
  }
}
