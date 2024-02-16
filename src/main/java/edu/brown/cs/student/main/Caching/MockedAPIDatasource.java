package edu.brown.cs.student.main.Caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.Census.Census;
import edu.brown.cs.student.main.Census.CensusHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Class that mocks the ACS Datasource */
public class MockedAPIDatasource implements ACSDatasource {

  private ACSDatasource constantData; //the census handler
  private Map<String, Object> constantMap;
  private LoadingCache<String, String> cache;

  /**
   * Constructor for MockedAPIDatasource
   * This initializes the constantMap used as well as the cache to be used within the mocked data
   *
   * @param map
   * @param cache
   */
  public MockedAPIDatasource(Map<String, Object> map, LoadingCache<String, String> cache) {
    this.constantMap = map;
    this.cache = cache;
    // Configure the cache for maximum size and expiration time
    if (cache == null) {
      this.cache = CacheBuilder.newBuilder()
          .maximumSize(3)
          .expireAfterWrite(1, TimeUnit.MINUTES)
          .build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
              // Implement the loading logic here (optional)
              return null;
            }
          });
    }
  }

  /**
   * Mocks an API call with caching.
   *
   * @param state
   * @param county
   * @return
   */
  public  Map<String, Object> mockAPICall(String state, String county){
    String cacheKey = state + ":" + county;

    // Check if data is already present in the cache
    String cachedResult = cache.getIfPresent(cacheKey);
    if (cachedResult != null) {
      System.out.println("Returning cached result for " + cacheKey);
      return this.constantMap; // Adjust the return type based on your requirements
    }

    // Store the result in the cache
    cache.put(cacheKey, "");
    return this.constantMap;
  }

  /**
   * Sends an API request based on state and county codes.
   *
   * @param stateCode
   * @param countyCode
   * @return
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
  @Override
  public String sendRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException {
    return this.constantMap.toString();
  }

  /**
   * Sets the datasource for the ACS Datasource.
   *
   * @param datasource The datasource to set.
   */
  @Override
  public void setDatasource(ACSDatasource datasource) {
    this.constantData = datasource;
  }
}


