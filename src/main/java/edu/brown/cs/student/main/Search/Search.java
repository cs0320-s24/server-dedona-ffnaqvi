package edu.brown.cs.student.main.Search;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.SearchCSVHandler;
import edu.brown.cs.student.main.CSVParser.SearchCSVHandler.SearchDataFailureResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* The Search class, this class takes in a list of list of strings and prints out the data searched for. */
public class Search {

  private CSVParser<List<String>> parser;
  private String searchValue;
  private Map.Entry<String, Integer> columnIdentifier;
  private boolean hasHeader;
  private List<List<String>> resultList;

  public Search(
      CSVParser<List<String>> parser,
      String searchValue,
      Map.Entry<String, Integer> columnIdentifier,
      boolean hasHeader) {
    this.parser = parser;
    this.searchValue = searchValue;
    this.columnIdentifier = columnIdentifier;
    this.hasHeader = hasHeader;
    this.resultList = new ArrayList<>();

  }

  public Object search() {

    try {
      // initialize a List<List<String>> to hold the parsed data
      this.parser.parse();
      List<List<String>> parsedData = this.parser.getParsedData();
      int numCols = NumColumns(parsedData);

      String strColumnIdentifier = this.columnIdentifier.getKey();
      Integer intColumnIdentifier = this.columnIdentifier.getValue();

      if (strColumnIdentifier == null && intColumnIdentifier == null) {
        // print all rows that contain the "searchValue" no matter the column
        if (hasHeader) {
          parsedData.remove(0);
        } // remove the first row to not possibly print it out
        System.out.println("\nRows that match your search");
        printMatchingRows(parsedData, -1);

      } else if (strColumnIdentifier == null && intColumnIdentifier != null) {
        // print all rows that contain the "searchValue" in the column with the index
        // "intColumnIdentifier"
        if (hasHeader) {
          parsedData.remove(0);
        } // remove the first row to not possibly print it out
        if (intColumnIdentifier >= 0 && intColumnIdentifier < numCols) {
          System.out.println("Rows that match your search");
          printMatchingRows(parsedData, intColumnIdentifier);
        } else {
          return 1;//new Search.SearchDataFailureResponse("Invalid column index, max index number is"+(numCols-1));
        }
      } else {
        // print all rows that contain the "searchValue" in the column with the name
        // "strColumnIdentifier"
        int columnIndex = findColumnIndex(parsedData, strColumnIdentifier);
        if (columnIndex != -1) {
          parsedData.remove(0); // remove the first row to not possibly print it out
          System.out.println("Rows that match your search");
          printMatchingRows(parsedData, columnIndex);
        } else {
          return 2;//new Search.SearchDataFailureResponse("Invalid column name, please check your CSV headers");
        }
      }
      if (this.resultList.isEmpty()) {
        return 3;//new Search.SearchDataFailureResponse("Could not find any corresponding data");
      }

    } catch (IOException e) {
      // Handle the IOException according to your error handling strategy
      return 4; //new Search.SearchDataFailureResponse("Error in Searching");
    }
    return null;
  }

  /**
   * Gets the number of columns in the dataset assuming all rows have the same number of columns
   *
   * @param data The parsed dataset where each inner list represents a row
   * @return The number of columns in the dataset, returns 0 if the dataset is empty
   */
  private int NumColumns(List<List<String>> data) {
    if (data.isEmpty()) {
      return 0;
    }
    return data.get(0).size();
  }

  private void printMatchingRows(List<List<String>> data, int columnIndex) {
    for (List<String> row : data) {
      if (matchesSearchCriteria(row, columnIndex)) {
        this.resultList.add(row);
        System.out.println(row);
      }
    }
  }

  private boolean matchesSearchCriteria(List<String> row, int columnIndex) {
    if (columnIndex == -1) {
      // Match if the searchValue is found in any column
      return row.stream().anyMatch(cell -> cell.equals(searchValue));
    } else {
      // Match if the searchValue is found in the specified column
      return columnIndex >= 0
          && columnIndex < row.size()
          && row.get(columnIndex).equals(searchValue);
    }
  }

  public List<List<String>> getResultList() {
    return this.resultList;
  }

  private int findColumnIndex(List<List<String>> data, String columnName) {
    if (data.isEmpty() || data.get(0).isEmpty()) {
      return -1; // Return -1 if the data is empty or header row is missing
    }

    List<String> headerRow = data.get(0);
    for (int i = 0; i < headerRow.size(); i++) {
      if (headerRow.get(i).equalsIgnoreCase(columnName)) {
        return i;
      }
    }
    return -1; // Return -1 if the column name is not found
  }

  /** Response object to send if someone requested data from an invalid csv */
  public record SearchDataFailureResponse(String response_type, String reply) {
    public SearchDataFailureResponse(String reply) {
      this("error searching", reply);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(Search.SearchDataFailureResponse.class).toJson(this);
    }
  }
}
