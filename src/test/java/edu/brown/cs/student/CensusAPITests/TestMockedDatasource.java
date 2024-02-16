package edu.brown.cs.student.CensusAPITests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Caching.ACSDatasource;
import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Caching.MockedAPIDatasource;
import edu.brown.cs.student.main.Census.Census;
import edu.brown.cs.student.main.Census.CensusHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import spark.Spark;

public class TestMockedDatasource {

  @BeforeAll
  public static void setupOnce() {
    // Pick an arbitrary free port
    Spark.port(0);
    // Eliminate logger spam in console for test suite
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
  }

  @Test
  public void testMock() {
    CachedCensusHandler cacheForMock = new CachedCensusHandler(3, 2);
    CensusHandler censusHandlerForMock = new CensusHandler(cacheForMock);
    Map<String, Object> mockmap = new HashMap<>();
    Census censusForMock = new Census();
    censusForMock.setState("Alabama");
    censusForMock.setCounty("Lauderdale County");
    censusForMock.setPercentageOfAccess(77.8);

    String state = "Alabama";
    String county = "Lauderdale County";

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("result", "success");
    responseMap.put("Current Date and Time", "current date and time");
    responseMap.put("State", state);
    responseMap.put("County", county);
    responseMap.put("Broadband Result", censusForMock);

    MockedAPIDatasource mockCall = new MockedAPIDatasource(mockmap);

    Map<String, Object> resultMap = mockCall.mockAPICall("Alabama", "Lauderdale County");


    //Assert.assertEquals(resultMap.get("Broadband Result").toString(),"[Lauderdale County, Alabama has the estimated percent broadband internet subscription of: 77.8%]\n");


  }


}
