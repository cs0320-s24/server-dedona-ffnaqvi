package edu.brown.cs.student.main.Census;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Route;

public class CachedCensusHandler implements Route {

  private final CensusHandler wrappedCensusHandler;
  private final LoadingCache<String, Object> cache;
  private String cacheKey;

  public CachedCensusHandler(CensusHandler censusHandler, Request request, Response response) {
    System.out.println("in caching");
    this.wrappedCensusHandler = censusHandler;
//    this.cacheKey = this.generateCacheKey();

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
  public Object handle(Request request, Response response) throws IOException, URISyntaxException, InterruptedException {
    String censusJson = this.wrappedCensusHandler.sendRequest(this.wrappedCensusHandler.stateCode, this.wrappedCensusHandler.countyCode);
    // Deserializes JSON into an Activity
    List<Census> census = CensusAPIUtilities.deserializeCensus(censusJson);

    this.cache.put(this.cacheKey, census);
    System.out.println("cache key");

    try {
      // Attempt to retrieve data from the cache
      return cache.getUnchecked(this.cacheKey);
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500);
      return "Error retrieving data from cache: " + e.getMessage();
    }
  }

}
