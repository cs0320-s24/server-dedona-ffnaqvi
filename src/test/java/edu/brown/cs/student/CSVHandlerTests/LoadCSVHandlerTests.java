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
  // TODO: add any static variables needed, etc: final List<Income> income = new ArrayList<>();

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run
    // TODO: add any static variables needed, etc: this.income.clear();

    // In fact, restart the entire Spark server for every test!
    Spark.get("loadCSV", new LoadCSVHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("loadCSV");
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
  public void testAPIReadCSV() throws IOException {
    String fileName = "ri_city_income.csv";
    HttpURLConnection clientConnection = tryRequest("loadCSV?fileName=datasource/" + fileName);
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode());

    // Now we need to see whether we've got the expected Json response.
    // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    LoadCSVHandler.LoadNoDataFailureResponse response =
        moshi
            .adapter(LoadCSVHandler.LoadNoDataFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    System.out.println(response);
    // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception, but
    // a real Json reply.

    clientConnection.disconnect();
  }
}
