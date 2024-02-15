package edu.brown.cs.student.main.Spark;

import spark.Request;
import spark.Response;

public class SparkUtilities {

  public static Request mockRequestWithParams() {
    // Create a mock request and manually set query parameters
    return new Request() {
      @Override
      public String queryParams(String queryParam) {
        if ("state".equals(queryParam)) {
          return "Alabama";
        } else if ("county".equals(queryParam)) {
          return "Lauderdale County";
        }
        return null;
      }

      // Implement other methods if needed
    };
  }

  public static Request mockRequest() {
    return new MockRequest();
  }

  public static Response mockResponse() {
    return new MockResponse();
  }

  private static class MockRequest extends Request {
    // You can add any necessary implementation for your mock request
  }

  private static class MockResponse extends Response {
    // You can add any necessary implementation for your mock response
  }
}
