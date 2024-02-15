package edu.brown.cs.student.CSVHandlerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import edu.brown.cs.student.main.CSVParser.ViewCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.brown.cs.student.main.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class ViewCSVParserTests {

    @BeforeAll
    public static void setup_before_everything() {
      // Set the Spark port number. This can only be done once, and has to
      // Setting port 0 will cause Spark to use an arbitrary available port.
      Spark.port(0);

      // Changing the JDK *ROOT* logger's level (not global) will block messages
      //   (assuming using JDK, not Log4J)
      Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

  /**
   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
   * need to replace the reference itself. We clear this state out after every test runs.
   */
  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run

    // In fact, restart the entire Spark server for every test!
    Spark.get("loadCSV", new LoadCSVHandler());
    Spark.get("ViewCSV", new ViewCSVHandler());

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("loadCSV");
    Spark.unmap("viewCSV");

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
    // If we were using "POST", we'd need to say so.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
  // checker
  public void testViewBeforeLoad() throws IOException {
    HttpURLConnection clientConnection = tryRequest("viewCSV");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(404, clientConnection.getResponseCode());
  }

    @Test

    public void testViewBasic() throws IOException {
      HttpURLConnection loadConnection = tryRequest("loadCSV?fileName=datasource\\stardata.csv");
      assertEquals(200, loadConnection.getResponseCode());


      HttpURLConnection clientConnection = tryRequest("viewCSV");
      assertEquals(200, Server.loadStatus);

      clientConnection.disconnect();
    }
}
