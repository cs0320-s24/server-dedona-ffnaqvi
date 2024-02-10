package edu.brown.cs.student.main;

import static spark.Spark.after;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.LoadCSVHandler;
import edu.brown.cs.student.main.CSVParser.SearchCSVHandler;
import edu.brown.cs.student.main.CSVParser.ViewCSVHandler;
import edu.brown.cs.student.main.Census.*;
import edu.brown.cs.student.main.Creators.CreatorFromRow;
import edu.brown.cs.student.main.Creators.ListStringCreator;
import edu.brown.cs.student.main.Income.IncomeHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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

    // TODO: this is where we need to parse the FilePath using CSVParser
    // Sets up data needed for the OrderHandler. You will likely not read from local
    // JSON in this sprint.
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    Reader reader =
        new BufferedReader(
            new FileReader("edu/brown/cs/student/main/Datasource/RI City & Town Income.csv"));
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    parser.parse();
    List<List<String>> incomeData = parser.getParsedData();

    String filePath = null; // SoupAPIUtilities.readInJson("data/menu.json");
    //    List<Soup> menu = new ArrayList<>();
    //    try {
    //      menu = SoupAPIUtilities.deserializeMenu(menuAsJson);
    //    } catch (Exception e) {
    //      // See note in ActivityHandler about this broad Exception catch... Unsatisfactory, but
    // gets
    //      // the job done in the gearup where it is not the focus.
    //      e.printStackTrace();
    //      System.err.println("Errored while deserializing the menu");
    //    }

    //    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("income", new IncomeHandler());
    Spark.get("census", new CensusHandler());
    Spark.get("loadCSV", new LoadCSVHandler());
    Spark.get("viewCSV", new ViewCSVHandler());
    Spark.get("searchCSV", new SearchCSVHandler());

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
