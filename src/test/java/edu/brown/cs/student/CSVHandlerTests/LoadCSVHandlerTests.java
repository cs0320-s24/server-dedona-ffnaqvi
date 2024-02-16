package edu.brown.cs.student.CSVHandlerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class LoadCSVHandlerTests {

  private static final String API_ENDPOINT = "loadCSV";
  private static final int SUCCESS_RESPONSE_CODE = 200;

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    Spark.get(API_ENDPOINT, new LoadCSVHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap(API_ENDPOINT);
    Spark.awaitStop();
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Test for checking if a user can connect to a regular file in the datasource file
   *
   * @throws IOException
   */
  @Test
  public void testAPIReadCSV() throws IOException {
    //______________________________________________
    // testing one file
    //______________________________________________
    String fileName1 = "ri_city_income.csv";
    HttpURLConnection clientConnection1 = tryRequest(API_ENDPOINT + "?fileName=" + fileName1);
    assertEquals(SUCCESS_RESPONSE_CODE, clientConnection1.getResponseCode());

    Moshi moshi1 = new Moshi.Builder().build();
    LoadCSVHandler.LoadNoDataFailureResponse response1 =
        moshi1
            .adapter(LoadCSVHandler.LoadNoDataFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));

    Logger.getLogger(LoadCSVHandlerTests.class.getName()).info(response1.toString());

    clientConnection1.disconnect();

    //______________________________________________
    // testing another file
    //______________________________________________
    String fileName2 = "ri_city_income.csv";
    HttpURLConnection clientConnection2 = tryRequest(API_ENDPOINT + "?fileName=" + fileName2);
    assertEquals(SUCCESS_RESPONSE_CODE, clientConnection2.getResponseCode());

    Moshi moshi2 = new Moshi.Builder().build();
    LoadCSVHandler.LoadNoDataFailureResponse response2 =
        moshi2
            .adapter(LoadCSVHandler.LoadNoDataFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));

    // Use logger for better logging and debugging
    Logger.getLogger(LoadCSVHandlerTests.class.getName()).info(response2.toString());

    clientConnection2.disconnect();

    //______________________________________________
    // yet another file...
    //______________________________________________
    String fileName3 = "ri_city_income.csv";
    HttpURLConnection clientConnection3 = tryRequest(API_ENDPOINT + "?fileName=" + fileName3);
    assertEquals(SUCCESS_RESPONSE_CODE, clientConnection3.getResponseCode());

    Moshi moshi3 = new Moshi.Builder().build();
    LoadCSVHandler.LoadNoDataFailureResponse response3 =
        moshi3
            .adapter(LoadCSVHandler.LoadNoDataFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));

    // Use logger for better logging and debugging
    Logger.getLogger(LoadCSVHandlerTests.class.getName()).info(response3.toString());

    clientConnection3.disconnect();
  }

  /**
   * Test for checking if an invalid file results in a loadStatus of 500
   *
   * @throws IOException
   */
  @Test
  public void testAPIReadCSVWithInvalidFilename() throws IOException {
    String invalidFileName = "nonexistent_file.csv";
    HttpURLConnection clientConnection = tryRequest(API_ENDPOINT + "?fileName=" + invalidFileName);
    assertEquals(500, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    LoadCSVHandler.LoadNoDataFailureResponse response =
        moshi
            .adapter(LoadCSVHandler.LoadNoDataFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    Logger.getLogger(LoadCSVHandlerTests.class.getName()).info(response.toString());

    clientConnection.disconnect();
  }

  /**
   * Test for checking if no file or parameter at all results in a loadStatus of 500
   *
   * @throws IOException
   */
  @Test
  public void testAPIReadCSVWithNoFileName() throws IOException {
    HttpURLConnection clientConnection = tryRequest(API_ENDPOINT);
    assertEquals(500, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    LoadCSVHandler.LoadNoDataFailureResponse response =
        moshi
            .adapter(LoadCSVHandler.LoadNoDataFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    Logger.getLogger(LoadCSVHandlerTests.class.getName()).info(response.toString());

    clientConnection.disconnect();
  }

}
