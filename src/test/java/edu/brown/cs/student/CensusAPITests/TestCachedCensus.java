package edu.brown.cs.student.CensusAPITests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Caching.MockedAPIDatasource;
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
        MockedAPIDatasource mockedSource = new MockedAPIDatasource();

        // Re-initialize state, etc. for _every_ test method run
        Spark.get("broadband", new CensusHandler(mockedSource));

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

    @Test
    public void testNoState() throws IOException {
        HttpURLConnection clientConnection = tryRequest("broadband");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(404, clientConnection.getResponseCode());

        // check response?

        clientConnection.disconnect();
    }

    @Test
    public void testWrongState() throws IOException {
        HttpURLConnection clientConnection = tryRequest("broadband?state=hello");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(404, clientConnection.getResponseCode());
        clientConnection.disconnect();
    }

    @Test
    // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
    // checker
    public void testAPIJustState() throws IOException {
        HttpURLConnection clientConnection = tryRequest("broadband?state=California");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(200, clientConnection.getResponseCode());

        clientConnection.disconnect();
    }

    @Test
    // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
    // checker
    public void testAPIStateAndCounty() throws IOException {
        HttpURLConnection clientConnection =
                tryRequest("broadband?state=California&county=Kings%20County");

        assertEquals(200, clientConnection.getResponseCode());

        clientConnection.disconnect();
    }
}
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import edu.brown.cs.student.main.Caching.CachedCensusHandler;
//import edu.brown.cs.student.main.Caching.MockedAPIDatasource;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import spark.Spark;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//public class TestCachedCensus {
//    private CachedCensusHandler datasource;
//    private MockedAPIDatasource mockedSource;
//
//    @BeforeEach
//    public void setup() {
//        mockedSource = new MockedAPIDatasource();
//        datasource = new CachedCensusHandler(mockedSource);
//
//        Spark.port(0);
//        Spark.get("broadband", new CensusHandler(datasource));
//
//        Spark.init();
//        Spark.awaitInitialization();
//    }
//
//    @AfterEach
//    public void teardown() {
//        Spark.unmap("broadband");
//        Spark.awaitStop();
//    }
//
//    @Test
//    public void testCacheHit() throws IOException {
//        // Make the first request
//        HttpURLConnection firstConnection = tryRequest("broadband?state=California");
//        assertEquals(200, firstConnection.getResponseCode());
//        firstConnection.disconnect();
//
//        // Make the second request
//        HttpURLConnection secondConnection = tryRequest("broadband?state=California");
//        assertEquals(200, secondConnection.getResponseCode());
//        secondConnection.disconnect();
//
//        // Check that the second response came from the cache
//        assertEquals(1, mockedSource.getRequestCount("California"));
//    }
//
//    @Test
//    public void testCacheMiss() throws IOException {
//        // Make the first request
//        HttpURLConnection firstConnection = tryRequest("broadband?state=California");
//        assertEquals(200, firstConnection.getResponseCode());
//        firstConnection.disconnect();
//
//        // Make the second request with a different state
//        HttpURLConnection secondConnection = tryRequest("broadband?state=New%20York");
//        assertEquals(200, secondConnection.getResponseCode());
//        secondConnection.disconnect();
//
//        // Check that the second response triggered a new API call
//        assertEquals(1, mockedSource.getRequestCount("California"));
//        assertEquals(1, mockedSource.getRequestCount("New York"));
//    }
//
//    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
//        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
//        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//        clientConnection.setRequestMethod("GET");
//        clientConnection.connect();
//        return clientConnection;
//    }
//}
//
//class MockedAPIDatasource implements CensusDatasource {
//    private final Map<String, Integer> requestCounts;
//
//    public MockedAPIDatasource() {
//        this.requestCounts = new HashMap<>();
//    }
//
//    @Override
//    public String getData(String state, String county) {
//        requestCounts.put(state, requestCounts.getOrDefault(state, 0) + 1);
//        // Simulate API response, returning dummy data for testing
//        return "Dummy data for " + state + (county != null ? " - " + county : "");
//    }
//
//    public int getRequestCount(String state) {
//        return requestCounts.getOrDefault(state, 0);
//    }
//}
//
//interface CensusDatasource {
//    String getData(String state, String county);
//}
//
//class CachedCensusHandler implements CensusDatasource {
//    private final CensusDatasource datasource;
//    private final Map<String, String> cache;
//
//    public CachedCensusHandler(CensusDatasource datasource) {
//        this.datasource = datasource;
//        this.cache = new HashMap<>();
//    }
//
//    @Override
//    public String getData(String state, String county) {
//        String key = state + (county != null ? "_" + county : "");
//        if (cache.containsKey(key)) {
//            System.out.println("Cache hit for " + key);
//            return cache.get(key);
//        } else {
//            System.out.println("Cache miss for " + key);
//            String data = datasource.getData(state, county);
//            cache.put(key, data);
//            return data;
//        }
//    }
//}
//
//class CensusHandler implements spark.Route {
//    private final CensusDatasource datasource;
//
//    public CensusHandler(CensusDatasource datasource) {
//        this.datasource = datasource;
//    }
//
//    @Override
//    public Object handle(spark.Request request, spark.Response response) throws Exception {
//        String state = request.queryParams("state");
//        String county = request.queryParams("county");
//        return datasource.getData(state, county);
//    }
//}
