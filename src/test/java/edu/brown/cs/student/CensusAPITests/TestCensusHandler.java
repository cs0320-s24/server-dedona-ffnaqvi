package edu.brown.cs.student.CensusAPITests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Census.CensusHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;
import spark.Spark;

/** Class to test the CensusHandler capabilities */
public class TestCensusHandler {
  private CachedCensusHandler datasource;

  @BeforeClass
  public static void setup_before_everything() {
    // Set the Spark port number.
    Spark.port(0);

    // Remove the logging spam during tests
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    this.datasource = new CachedCensusHandler(3, 1);
    // Re-initialize state, etc. for _every_ test method run
    Spark.get("broadband", new CensusHandler(this.datasource));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Tests bad request exception through if a state doesn't exist
   *
   * @throws IOException
   */
  @Test
  public void testWrongState() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=hello");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(400, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

  /**
   * Tests success call with state
   *
   * @throws IOException
   */
  @Test
  public void testAPIJustState() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=California");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

  /**
   * Tests success call with state and county
   *
   * @throws IOException
   */
  @Test
  public void testAPIStateAndCounty() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Kings%20County");

    assertEquals(200, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

  /**
   * Tests error status when a json is not will formed (bad json)
   *
   * @throws IOException
   */
  @Test
  public void testBadJsonError() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?STATE=California&COUNTY=Kings County");

    assertEquals(400, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

  /**
   * Tests error status for when a field is missing (bad request)
   *
   * @throws IOException
   */
  @Test
  public void testBadRequestError() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=");

    assertEquals(400, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

  /**
   * Tests error status for when a file doesn't exist (datasource error)
   *
   * @throws IOException
   */
  @Test
  public void testDatasourceError() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband");

    assertEquals(404, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }
}
