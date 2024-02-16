package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import edu.brown.cs.student.main.CSVParser.SearchCSVHandler;
import edu.brown.cs.student.main.CSVParser.ViewCSVHandler;
import edu.brown.cs.student.main.Caching.CachedCensusHandler;
import edu.brown.cs.student.main.Census.*;
import java.io.IOException;
import java.util.List;
import spark.Spark;

/**
 * Server is a class that sets up a server at a localhost address and starts the paths for the
 * endpoints
 */
public class Server {

  public static int loadStatus;
  public static CSVParser<List<String>> parser;
  public static String fileName;

  public static void main(String[] args) throws IOException {

    int port = 3232;

    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    CachedCensusHandler caching = new CachedCensusHandler(3, 1);

    // Setting up the handler for the GET /census endpoints
    Spark.get("broadband", new CensusHandler(caching));

    // Setting up the handler for the CSV endpoints
    Spark.get("loadcsv", new LoadCSVHandler());
    Spark.get("viewcsv", new ViewCSVHandler());
    Spark.get("searchcsv", new SearchCSVHandler());

    Spark.init();
    Spark.awaitInitialization();

    // Instructions to the user
    System.out.println("Server started at http://localhost:" + port);
    System.out.println(
        "To load a CSV, go to the path /loadCSV and enter the fileName "
            + "as a parameter.\nTo view a CSV, go to the /viewCSV path. To search a CSV, "
            + "go to the /searchCSV path, followed by the parameters for searching \n(searchValue, "
            + "columnIdentifier or columnNumber, and a boolean for \nwhether or not the data"
            + "contains headers). To connect to the US Census API, go to the path /census.\n"
            + "inputting no county will result in a search for all counties in the specified state.\n" +
                "Data entries from the ACS API is cached automatically for 1 minute. To change the duration,\n" +
                "edit the Server class to fit your desired criteria. To remove caching entirely, set the time \n" +
                "duration to 0 minutes.");
  }
}
