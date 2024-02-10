package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

//TODO: model this class after the OrderHandler in the gearup
public class IncomeHandler implements Route {

    /**
     * <p>NOTE: beware this "return Object" and "throws Exception" idiom. We need to follow it because
     * the library uses it, but in general this lowers the protection of the type system.
     *
     * @param request the request to handle
     * @param response use to modify properties of the response
     * @return response content
     * @throws Exception This is part of the interface; we don't have to throw anything.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        // Get Query parameters, can be used to make your search more specific
        String soupname = request.queryParams("soupName");
        // Initialize a map for our informative response.
        Map<String, Object> responseMap = new HashMap<>();
        // Iterate through the soups in the menu and return the first one
        //TODO: a condition for returning success
        return new SoupNoRecipesFailureResponse().serialize();
    }

    /*
     * Ultimately up to you how you want to structure your success and failure responses, but they
     * should be distinguishable in some form! We show one form here and another form in ActivityHandler
     * and you are also free to do your own way!
     */

    /** Response object to send, containing a soup with certain ingredients in it */
    public record SoupSuccessResponse(String response_type, Map<String, Object> responseMap) {
        public SoupSuccessResponse(Map<String, Object> responseMap) {
            this("success", responseMap);
        }
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            try {
                // Initialize Moshi which takes in this class and returns it as JSON!
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<SoupSuccessResponse> adapter = moshi.adapter(SoupSuccessResponse.class);
                return adapter.toJson(this);
            } catch (Exception e) {
                // For debugging purposes, show in the console _why_ this fails
                // Otherwise we'll just get an error 500 from the API in integration
                // testing.
                e.printStackTrace();
                throw e;
            }
        }
    }
    //TODO: change to income success and failure
    /** Response object to send if someone requested soup from an empty Menu */
    public record SoupNoRecipesFailureResponse(String response_type) {
        public SoupNoRecipesFailureResponse() {
            this("error");
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(SoupNoRecipesFailureResponse.class).toJson(this);
        }
    }
}

