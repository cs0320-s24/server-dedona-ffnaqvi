package edu.brown.cs.student.CSVHandlerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import edu.brown.cs.student.main.CSVParser.ViewCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class ViewCSVParserTests {
  @BeforeEach
  public void setup() {
    Spark.get("loadCSV", new LoadCSVHandler());
    Spark.get("viewCSV", new ViewCSVHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("loadCSV");
    Spark.unmap("viewCSV");
    Spark.awaitStop();
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }

  //TODO: revisit
//  @Test
//  public void testViewBeforeLoad() throws IOException {
//    HttpURLConnection clientConnection = tryRequest("viewCSV");
//    int responseCode = clientConnection.getResponseCode();
//
//    // Ensure that the response code is 404 (Not Found)
//    assertEquals(404, responseCode);
//  }


  /**
   * Test to see if viewing a CSV file results in a loadStatus of 200 (is valid)
   *
   * @throws IOException
   */
  @Test
  public void testViewBasic() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadCSV?fileName=stardata.csv");
    assertEquals(200, loadConnection.getResponseCode());

    HttpURLConnection clientConnection = tryRequest("viewCSV");
    assertEquals(200, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

}
