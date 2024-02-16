package edu.brown.cs.student.main.Caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.Census.Census;
import edu.brown.cs.student.main.Census.CensusAPIUtilities;
import edu.brown.cs.student.main.Census.CensusHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Route;

public class CachedCensusHandler implements ACSDatasource {
  //works as a proxy, call methods on it that you would've called on the original thing

  //when you call getBroadband on it, checks getBroadband on this first and
  //if this doesn't have the item in the class it'll call getBroadband on the
  // actual censusHandler

  //initialize the datasource that gets pulled

  // a reference to an instance of the CensusHandler class.
  private ACSDatasource original;

  // a generic interface provided by the Guava library that represents a cache that loads its values
  // on demand.
  private final LoadingCache<String, String> cache;

  private String cacheKey;

  public CachedCensusHandler(ACSDatasource datasource, int size, int timeMinutes) {
    //give it size, minutes
    //return og.getBroadband
    System.out.println("in caching");
    this.original = datasource;
    //    this.cacheKey = this.generateCacheKey();

    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(size) // Adjust as needed
            .expireAfterWrite(timeMinutes, TimeUnit.MINUTES) // Adjust as needed
            .build(
                new CacheLoader<>() {
                  @Override
                  public String load(String key) throws Exception {
                    cacheKey = key;
                    System.out.println(key);
                    String[] codes = key.split(",");
                    String stateCode = codes[0];
                    String countyCode = codes[1];
                    // If the data is not found in the cache, fetch it using the wrapped
                    // CensusHandler
                    //returns String of body of request
                    return original.sendRequest(stateCode, countyCode); // Pass request and response to the handle method
                  }
                });
  }

  @Override
  public String sendRequest(String stateCode, String countyCode) {
    // "get" is designed for concurrent situations; for today, use getUnchecked:

    String result = cache.getUnchecked(this.cacheKey);
    // For debugging and demo (would remove in a "real" version):
    System.out.println(cache.stats());
    return result;
  }

}
