package edu.brown.cs.student.CensusAPITests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Caching.MockedAPIDatasource;
import edu.brown.cs.student.main.Census.CensusHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("result", "success");
        responseMap.put("Current Date and Time", "2024-02-16 11:48:58");
        responseMap.put("State", "California");
        responseMap.put("County", "Kings County");
        responseMap.put("Broadband Result", "[Kings County, California has the estimated percent broadband internet subscription of: 83.5%]");
        MockedAPIDatasource mockedSource = new MockedAPIDatasource(responseMap);

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