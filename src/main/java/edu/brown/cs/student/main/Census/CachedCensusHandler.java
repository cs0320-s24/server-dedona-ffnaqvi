package edu.brown.cs.student.main.Census;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Route;

public class CachedCensusHandler implements Route {

  private final CensusHandler wrappedCensusHandler;
  private final LoadingCache<String, Object> cache;

  public CachedCensusHandler(CensusHandler censusHandler, Request request, Response response) {
    System.out.println("in caching");
    this.wrappedCensusHandler = censusHandler;
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(100) // Adjust as needed
            .expireAfterWrite(1, TimeUnit.HOURS) // Adjust as needed
            .build(
                new CacheLoader<>() {
                  @Override
                  public Object load(String key) throws Exception {
                    System.out.println(key);
                    // If the data is not found in the cache, fetch it using the wrapped
                    // CensusHandler
                    return wrappedCensusHandler.handle(
                        request, response); // Pass request and response to the handle method
                  }
                });
  }

  @Override
  public Object handle(Request request, Response response) throws IOException {
    String cacheKey = generateCacheKey(request);
    System.out.println("cache key");

    try {
      // Attempt to retrieve data from the cache
      return cache.get(cacheKey);
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500);
      return "Error retrieving data from cache: " + e.getMessage();
    }
  }

  private String generateCacheKey(Request request) {
    // Generate a unique cache key based on request parameters, headers, or URL
    // Example: Concatenate state and county parameters to form a cache key
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    return state + "_" + county;
  }
}
