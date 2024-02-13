package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import edu.brown.cs.student.main.CSVParser.SearchCSVHandler;
import edu.brown.cs.student.main.CSVParser.ViewCSVHandler;
import edu.brown.cs.student.main.Census.*;
import java.io.IOException;
import java.util.List;
import spark.Spark;

public class Server {
  /*
  Server -
  Server is kind of like the main for the server part of the spring (Main/server is separate)
  Parameters
  - FilePath: "\nEnter a file path that follows the root path \"data/...\" for the csv file to be parsed:"
  - endpoint
        `loadcsv`
        `viewcsv`
        `searchcsv`
  Once you have server, should instantiate all of the endpoints to be using; each endpoint (aka handler, should have its own class that should implement spark route)
   */
  public static int loadStatus;
  public static CSVParser<List<String>> parser;
  public static String fileName;

  public static void main(String[] args) throws IOException {

    int port = 3232;

    Spark.port(port);

    after(
            (request, response) -> {
              // TODO: By setting the Access-Control-Allow-Origin header to "*", we allow requests from
              // any origin.
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("census", new CensusHandler());

    Spark.get("loadCSV", new LoadCSVHandler());

    Spark.get("viewCSV", new ViewCSVHandler());
    Spark.get("searchCSV", new SearchCSVHandler());

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
    System.out.println(
            "To load a CSV, go to the path /loadCSV and enter the fileName "
                    + "as a parameter.\nTo view a CSV, go to the /viewCSV path. To search a CSV, "
                    + "go to the /searchCSV path, followed by the parameters for searching \n(searchValue, "
                    + "columnIdentifier or columnNumber, and a boolean for \nwhether or not the data"
                    + "contains headers). To connect to the US Census API, go to the path /census.");
  }
}