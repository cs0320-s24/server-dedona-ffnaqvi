package edu.brown.cs.student.CensusAPITests;

import edu.brown.cs.student.main.Census.CachedCensusHandler;
import edu.brown.cs.student.main.Spark.SparkUtilities;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import spark.Request;
import spark.Response;
//import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;

import edu.brown.cs.student.main.Spark.SparkTestUtilities;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.io.IOException;
import org.testng.Assert;

public class TestCachedCensus {

  private String testState = "Alabama";
  private String testCounty = "Lauderdale%20County";

  private CachedCensusHandler cachedCensusHandler;

    @Test
    public void testHandle() throws IOException {
      // Create a mock request
      Request mockRequest = SparkTestUtilities.mockRequest();

      // Set query parameters
      String state = mockRequest.queryParams("state");
      String county = mockRequest.queryParams("county");

      // Create a mock response
      Response mockResponse = SparkTestUtilities.mockResponse();


      // Invoke the handle method
      Object result = cachedCensusHandler.handle(mockRequest, mockResponse);

      // Assert the result
      Assert.assertNotNull(result);
      // Adjust the following assertion based on your implementation
      Assert.assertEquals("Expected result based on your implementation", result.toString());
    }

}
