package edu.brown.cs.student.CensusAPITests;

import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Caching.MockedAPIDatasource;
import edu.brown.cs.student.main.Census.Census;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import spark.Spark;

/** This test class uses mocking to test a variety of API interactions */
public class TestMockedDatasource {

  @BeforeAll
  public static void setupOnce() {
    // Pick an arbitrary free port
    Spark.port(0);
    // Eliminate logger spam in the console for the test suite
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
    // Start the Spark server
    Spark.init();
    Spark.awaitInitialization(); // wait until the server is listening
  }

  @AfterAll
  public static void teardownOnce() {
    // Stop the Spark server after all tests are done
    Spark.stop();
  }

  /**
   * Mocked test to make sure that calling the census API works as intended Also check to make sure
   * that data is saved into a cache
   */
  @Test
  public void testMockSimple() {
    CachedCensusHandler cacheForMock = new CachedCensusHandler(3, 1);
    Map<String, Object> responseMap = new HashMap<>();

    Census censusForMock = new Census();
    censusForMock.setState("Alabama");
    censusForMock.setCounty("Lauderdale County");
    censusForMock.setPercentageOfAccess(77.8);

    String state = "Alabama";
    String county = "Lauderdale County";

    responseMap.put("result", "success");
    responseMap.put("Current Date and Time", "current date and time");
    responseMap.put("State", state);
    responseMap.put("County", county);
    responseMap.put("Broadband Result", censusForMock);

    MockedAPIDatasource mockCall = new MockedAPIDatasource(responseMap, cacheForMock.cache);

    Map<String, Object> resultMap = mockCall.mockAPICall("Alabama", "Lauderdale County");

    Assert.assertEquals(resultMap.get("result").toString(), "success");
    Assert.assertEquals(resultMap.get("Current Date and Time").toString(), "current date and time");
    Assert.assertEquals(resultMap.get("State").toString(), state);
    Assert.assertEquals(resultMap.get("County").toString(), county);
    Assert.assertEquals(
        resultMap.get("Broadband Result").toString(),
        "[Lauderdale County, Alabama has the estimated percent broadband internet subscription of: 77.8%]\n");
    Assert.assertEquals(cacheForMock.cache.size(), 1);
  }

  /**
   * Mocked test to make sure that calling the census API works as intended Also check to make sure
   * that data is saved into a cache
   */
  @Test
  public void testMockCache() {
    CachedCensusHandler cacheForMock = new CachedCensusHandler(3, 1);
    Map<String, Object> responseMap = new HashMap<>();

    Census censusForMock = new Census();
    censusForMock.setState("Alabama");
    censusForMock.setCounty("Lauderdale County");
    censusForMock.setPercentageOfAccess(77.8);

    String state = "Alabama";
    String county = "Lauderdale County";

    responseMap.put("result", "success");
    responseMap.put("Current Date and Time", "current date and time");
    responseMap.put("State", state);
    responseMap.put("County", county);
    responseMap.put("Broadband Result", censusForMock);

    MockedAPIDatasource mockCall = new MockedAPIDatasource(responseMap, cacheForMock.cache);

    Map<String, Object> resultMap = mockCall.mockAPICall("Alabama", "Lauderdale County");

    Assert.assertEquals(resultMap.get("result").toString(), "success");
    Assert.assertEquals(resultMap.get("Current Date and Time").toString(), "current date and time");
    Assert.assertEquals(resultMap.get("State").toString(), state);
    Assert.assertEquals(resultMap.get("County").toString(), county);
    Assert.assertEquals(
        resultMap.get("Broadband Result").toString(),
        "[Lauderdale County, Alabama has the estimated percent broadband internet subscription of: 77.8%]\n");
    Assert.assertEquals(cacheForMock.cache.size(), 1);

    mockCall.mockAPICall("Alabama", "Lauderdale County");
    mockCall.mockAPICall("Alabama", "Lauderdale County");
    // Should not change in size as we are calling the same call that we have cached
    Assert.assertEquals(cacheForMock.cache.size(), 1);

    mockCall.mockAPICall("New State", "New County");
    // Should change in size as we are calling a new call
    Assert.assertEquals(cacheForMock.cache.size(), 2);

    mockCall.mockAPICall("New State 2", "New County 2");
    mockCall.mockAPICall("New State 3", "New County 3");
    mockCall.mockAPICall("New State 4", "New County 4");
    // Should stay at size 3 as we have defined this parameter above
    Assert.assertEquals(cacheForMock.cache.size(), 3);
  }
}
