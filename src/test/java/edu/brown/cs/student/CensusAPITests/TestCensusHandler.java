package edu.brown.cs.student.CensusAPITests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.Census.CensusHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestCensusHandler {

  @BeforeAll
  public static void setup_before_everything() {
    // Set the Spark port number.
    Spark.port(0);

    // Remove the logging spam during tests
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run
    Spark.get("census", new CensusHandler());

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("census");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
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

  //  @Test
  //  // TODO fix that invalid input works
  //  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the
  // type
  //  // checker
  //  public void testNoState() throws IOException {
  //    HttpURLConnection clientConnection = tryRequest("census");
  //    // Get an OK response (the *connection* worked, the *API* provides an error response)
  //    assertEquals(404, clientConnection.getResponseCode());
  //
  //    // check response?
  //
  //    clientConnection.disconnect();
  //  }

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
  // checker
  public void testAPIJustState() throws IOException {
    HttpURLConnection clientConnection = tryRequest("census?state=California");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());

    // check response?

    clientConnection.disconnect();
  }

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
  // checker
  public void testAPIStateAndCounty() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("census?state=California&county=Colusa%20County");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());

    // check response?

    clientConnection.disconnect();
  }
}
