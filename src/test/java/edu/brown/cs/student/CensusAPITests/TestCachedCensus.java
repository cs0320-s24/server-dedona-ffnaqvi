 package edu.brown.cs.student.CensusAPITests;
 import edu.brown.cs.student.main.Caching.CachedCensusHandler;
 import edu.brown.cs.student.main.Spark.SparkTestUtilities;
 import java.io.IOException;
 import java.net.URISyntaxException;
 import org.junit.jupiter.api.Test;
 import org.testng.Assert;
 import spark.Request;
 import spark.Response;

 public class TestCachedCensus {

  private String testState = "Alabama";
  private String testCounty = "Lauderdale%20County";

  private CachedCensusHandler cachedCensusHandler;

  @Test
  public void testHandle() throws IOException, URISyntaxException, InterruptedException {
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



