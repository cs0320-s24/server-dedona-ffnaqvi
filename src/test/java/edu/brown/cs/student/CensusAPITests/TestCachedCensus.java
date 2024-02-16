package edu.brown.cs.student.CensusAPITests;

import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Census.CensusHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import spark.Spark;

/** Class to test the methods of caching the ACS API */
public class TestCachedCensus {
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
   * Helper to sendRequests and handle caching
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private String tryRequest(String apiCall) throws IOException {
    String cachedResponse = null; // initialize to null

    // Call sendRequest method of CachedCensusHandler
    String[] requests = apiCall.split("[?=&]");
    if (requests.length >= 3) {
      String state = requests[2];
      if (requests.length >= 5) {
        String county = requests[4];
        cachedResponse = this.datasource.sendRequest(state, county);
      } else {
        cachedResponse = this.datasource.sendRequest(state, "*");
      }
    }

    return cachedResponse;
  }

  @Test
  /** Tests to see if a data point has been cached */
  // tests to see if the size of the cache changes with two same requests to the API
  public void testCached() throws IOException {
    String connection1 = tryRequest("broadband?state=California");

    String connection2 = tryRequest("broadband?state=California");

    Assert.assertEquals(1, this.datasource.cache.size());
  }

  /**
   * Test to ensure the cache size doesn't pass the predetermined size
   *
   * @throws IOException
   */
  @Test
  public void testCachedSizeRestriction() throws IOException {
    String clientConnection = tryRequest("broadband?state=California");
    String clientConnection2 = tryRequest("broadband?state=Alabama");
    String clientConnection3 = tryRequest("broadband?state=Ohio");
    String clientConnection4 = tryRequest("broadband?state=Texas");

    Assert.assertEquals(3, this.datasource.cache.size());
  }

  /**
   * Tests to ensure that entries are removed after the specified amount of time
   *
   * @throws IOException
   * @throws InterruptedException
   */
  @Test
  // test to ensure that cache entries are removed after the specified time
  public void testExpiry() throws IOException, InterruptedException {
    String clientConnection = tryRequest("broadband?state=California");
    Assert.assertEquals(1, this.datasource.cache.size());

    Thread.sleep(65000);
    String clientConnection1 = tryRequest("broadband?state=Alabama");

    Assert.assertEquals(1, this.datasource.cache.size());
  }
}
