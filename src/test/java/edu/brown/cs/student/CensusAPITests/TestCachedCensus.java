//package edu.brown.cs.student.CensusAPITests;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import edu.brown.cs.student.main.Caching.CachedCensusHandler;
//import edu.brown.cs.student.main.Caching.MockedAPIDatasource;
//import edu.brown.cs.student.main.Census.CensusHandler;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.testng.annotations.BeforeClass;
//import spark.Spark;
//
//public class TestCachedCensus {
//
//    @BeforeClass
//    public static void setup_before_everything() {
//        // Set the Spark port number.
//        Spark.port(0);
//
//        // Remove the logging spam during tests
//        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
//    }
//
//    @BeforeEach
//    public void setup() {
//        CachedCensusHandler caching = new CachedCensusHandler(3, 2);
//
////        Map<String, Object> responseMap = new HashMap<>();
////
////        responseMap.put("result", "success");
////        responseMap.put("Current Date and Time", "2024-02-16 11:48:58");
////        responseMap.put("State", "California");
////        responseMap.put("County", "Kings County");
////        responseMap.put("Broadband Result", "[Kings County, California has the estimated percent broadband internet subscription of: 83.5%]");
////        MockedAPIDatasource mockedSource = new MockedAPIDatasource(responseMap);
//
//        // Re-initialize state, etc. for _every_ test method run
//        Spark.get("broadband", new CensusHandler(caching));
//
//        Spark.init();
//        Spark.awaitInitialization(); // don't continue until the server is listening
//    }
//
//    @AfterEach
//    public void teardown() {
//        // Gracefully stop Spark listening on both endpoints after each test
//        Spark.unmap("broadband");
//        Spark.awaitStop(); // don't proceed until the server is stopped
//    }
//
//    /**
//     * Helper to start a connection to a specific API endpoint/params
//     *
//     * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
//     *     structure!)
//     * @return the connection for the given URL, just after connecting
//     * @throws IOException if the connection fails for some reason
//     */
//    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
////        // Configure the connection (but don't actually send the request yet)
////        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
////        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
////
////        // The default method is "GET", which is what we're using here.
////        clientConnection.setRequestMethod("GET");
////
////        clientConnection.connect();
////        return clientConnection;
//        // Configure the connection (but don't actually send a request yet)
//        URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
//        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//        // The request body contains a Json object
//        clientConnection.setRequestProperty("Content-Type", "application/json");
//        // We're expecting a Json object in the response body
//        clientConnection.setRequestProperty("Accept", "application/json");
//
//        clientConnection.connect();
//        return clientConnection;
//    }
//    @Test
//
//    public void testAPIStateAndCounty() throws IOException {
//        HttpURLConnection clientConnection =
//                tryRequest("broadband?state=California&county=Kings%20County");
//
//        assertEquals(200, clientConnection.getResponseCode());
//
//        clientConnection.disconnect();
//    }
//}
package edu.brown.cs.student.CensusAPITests;

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
import org.testng.Assert;
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
     * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
     *     structure!)
     * @return the connection for the given URL, just after connecting
     * @throws IOException if the connection fails for some reason
     */
    private String tryRequest(String apiCall) throws IOException {
//        // Configure the connection (but don't actually send the request yet)
//        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
//        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//
//        // The default method is "GET", which is what we're using here.
//        clientConnection.setRequestMethod("GET");
//
//        clientConnection.connect();
//        return clientConnection;
//        CachedCensusHandler caching = new CachedCensusHandler(3, 1);
        String cachedResponse = null; //initialize to null

        // Call sendRequest method of CachedCensusHandler
        String[] requests = apiCall.split("[?=&]");
        if (requests.length >= 2) {
            String state = requests[1];
            if (requests.length >= 4) {
                String county = requests[3];
                cachedResponse = this.datasource.sendRequest(state, county);
            }
            else {
                cachedResponse = this.datasource.sendRequest(state, "*");
            }
        }

        return cachedResponse;
    }

    @Test
    // tests to see if the size of the cache changes with two same requests to the API
    public void testCached() throws IOException {
        String connection1 = tryRequest("broadband?state=California");

        String connection2 = tryRequest("broadband?state=California");

        // Get an OK response (the *connection* worked, the *API* provides an error response)
        Assert.assertEquals(1, this.datasource.cache.size());
    }
    @Test
    // tests to see if the size of the cache changes with two same requests to the API
    public void testCachedSizeRestriction() throws IOException {
        String clientConnection = tryRequest("broadband?state=California");

        String clientConnection2 = tryRequest("broadband?state=Alabama");

        String clientConnection3 = tryRequest("broadband?state=Ohio");
        String clientConnection4 = tryRequest("broadband?state=Rhode Island");

        // Get an OK response (the *connection* worked, the *API* provides an error response)
        Assert.assertEquals(3, this.datasource.cache.size());
//        clientConnection.disconnect();
    }

    @Test
    // tests to see if the size of the cache changes with two same requests to the API
    public void testExpiry() throws IOException, InterruptedException {
        String clientConnection = tryRequest("broadband?state=California");
        Assert.assertEquals(1, this.datasource.cache.size());

        wait(60000);
        Assert.assertEquals(0, this.datasource.cache.size());

//        clientConnection.disconnect();
    }

}
