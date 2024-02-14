//package edu.brown.cs.student.main.Census;
//
//import com.squareup.moshi.JsonAdapter;
//import com.squareup.moshi.Moshi;
//import com.squareup.moshi.Types;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//public class CachedCensusHandler implements Route {
//
//    private final CensusHandler wrappedCensusHandler;
//    private final LoadingCache<String, Object> cache;
//
//    public CachedCensusHandler(CensusHandler censusHandler) {
//        this.wrappedCensusHandler = censusHandler;
//        this.cache = CacheBuilder.newBuilder()
//                .maximumSize(100) // Adjust as needed
//                .expireAfterWrite(1, TimeUnit.HOURS) // Adjust as needed
//                .build(new CacheLoader<>() {
//                    //TODO: Figure out what to pass into handle
//                    @Override
//                    public Object load(String key) throws Exception {
//                        // If the data is not found in the cache, fetch it using the wrapped CensusHandler
//                        return wrappedCensusHandler.handle(request, response); // Pass request and response to the handle method
//                    }
//                });
//    }
//
//    @Override
//    public Object handle(Request request, Response response) throws IOException {
//        String cacheKey = generateCacheKey(request);
//
//        try {
//            // Attempt to retrieve data from the cache
//            return cache.get(cacheKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.status(500);
//            return "Error retrieving data from cache: " + e.getMessage();
//        }
//    }
//
//    private String generateCacheKey(Request request) {
//        // Generate a unique cache key based on request parameters, headers, or URL
//        // Example: Concatenate state and county parameters to form a cache key
//        String state = request.queryParams("state");
//        String county = request.queryParams("county");
//        return state + "_" + county;
//    }
//}
