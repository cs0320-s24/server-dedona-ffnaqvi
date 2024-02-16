package edu.brown.cs.student.main.Caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

/**
 * Proxy class for caching data in which we check if something is already in the cache and send a
 * request to the ACS API if not
 */
public class CachedCensusHandler implements ACSDatasource {

  private ACSDatasource wrappedCensusHandler;
  private final LoadingCache<String, String> cache;
  private String cacheKey;

  /**
   * Constructor to build a cache with the specified size and duration
   *
   * @param size
   * @param timeMinutes
   */
  public CachedCensusHandler(int size, int timeMinutes) {

    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(size) // Adjust as needed
            .expireAfterWrite(timeMinutes, TimeUnit.MINUTES) // Adjust as needed
            .build(
                new CacheLoader<>() {
                  /**
                   * Method to send a request with the given key to the ACS API, called upon failure
                   * to find information in the cache
                   *
                   * @param key
                   * @return
                   * @throws Exception
                   */
                  @Override
                  public String load(String key) throws Exception {
                    cacheKey = key;
                    String[] codes = key.split(",");
                    String stateCode = codes[0];
                    String countyCode = codes[1];

                    return wrappedCensusHandler.sendRequest(
                        stateCode, countyCode); // Pass request and response to the handle method
                  }
                });
  }

  /**
   * Function to send a request to the cache for the given key, sends a request to the ACS API if
   * not found in the cache
   *
   * @param stateCode
   * @param countyCode
   * @return
   */
  @Override
  public String sendRequest(String stateCode, String countyCode) {
    // "get" is designed for concurrent situations; for today, use getUnchecked:
    this.cacheKey = stateCode + "," + countyCode;

    String result = this.cache.getUnchecked(this.cacheKey);
    // For debugging and demo (would remove in a "real" version):
    System.out.println(this.cache.stats());
    System.out.println("the cache size is:" + this.cache.size());
    return result;
  }

  /**
   * Function to set the datasource to the passed in datasource
   *
   * @param datasource
   */
  @Override
  public void setDatasource(ACSDatasource datasource) {
    this.wrappedCensusHandler = datasource;
  }
}
