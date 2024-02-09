package edu.brown.cs.student;

import edu.brown.cs.student.main.Flower.Flower;
import edu.brown.cs.student.main.Flower.FlowerRowCreator;
import edu.brown.cs.student.main.Utility.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class TestCSVParser {

  private CSVParser<String> testParser;
  private CSVParser<String> testParser2;
  private CSVParser<String> testParser3;
  private CSVParser<String> testParser4;

  /**
   * Method to initialize testing data
   *
   * @throws FileNotFoundException
   * @throws FactoryFailureException
   */
  public TestCSVParser() throws FileNotFoundException, FactoryFailureException {
    CreatorFromRow row = new RowCreator();
    this.testParser = new CSVParser<>(new FileReader("data/brown/brown_dining.csv"), row);
    this.testParser2 = new CSVParser<>(new FileReader("data/stars/ten-star.csv"), row);
    this.testParser3 =
        new CSVParser<>(
            new StringReader(
                "Ratty,Andrews,VDub,Ivy Room\n"
                    + "Ratty,Andrews,Ivy Room,VDub\n"
                    + "Andrews,Ratty,Ivy Room,VDub\n"
                    + "Blue Room,Ratty,Ivy Room,VDub"),
            row);
    this.testParser4 =
        new CSVParser<>(new FileReader("data/census/dol_ri_earnings_disparity.csv"), row);
  }

  /**
   * Test to check CSVParser compatability with Flower Object type (aka any generic object parsing
   * capability)
   */
  @Test
  public void flowerTest() throws FactoryFailureException {
    CreatorFromRow flowerRow = new FlowerRowCreator();
    List<List<Flower>> flowerList = new ArrayList<>();
    ArrayList<Flower> flower1 = new ArrayList<>();
    flower1.add(Flower.TULIP);
    flower1.add(Flower.ROSE);
    flower1.add(Flower.DAISY);
    flower1.add(Flower.SPRIG);
    ArrayList<Flower> flower2 = new ArrayList<>();
    flower2.add(Flower.ROSE);
    flower2.add(Flower.TULIP);
    flower2.add(Flower.DAISY);
    flower2.add(Flower.SPRIG);
    flowerList.add(flower1);
    flowerList.add(flower2);

    // tests expected value of parsing flower object equals the actual parsing
    CSVParser<Flower> flowerParser =
        new CSVParser<>(
            new StringReader("TULIP,ROSE,DAISY,SPRIG\n" + "ROSE,TULIP,DAISY,SPRIG"), flowerRow);
    Assert.assertEquals(flowerList, flowerParser.getContent());
  }
  /** Tests CSV data with inconsistent column count; */
  @Test
  public void testMalformedData() {
    // Test CSV file with inconsistent column count throws error
    CreatorFromRow row = new RowCreator();

    Assert.assertThrows(
        FactoryFailureException.class,
        () -> {
          new CSVParser<>(new FileReader("data/malformed/malformed_signs.csv"), row);
        });
  }

  /** Tests for CSV data that lies outside the protected directory; */
  @Test
  public void testFileAccess() {
    CreatorFromRow row = new RowCreator();

    // tests that the path is invalid
    Assert.assertThrows(
        FileNotFoundException.class,
        () -> {
          new CSVParser<>(new FileReader("data/invalid_file.csv"), row);
        });
  }

  /** Tests for when searchString is and is not in the CSV file */
  @Test
  public void testBasicSearch() {
    // Searching for values that are in the CSV (positive case)
    List<String> searchResult1 = new Search(this.testParser4, "RI", "State", true).resultList;
    Assert.assertEquals(5, searchResult1.size());

    // Searching for another value that is in the CSV (positive case)
    List<String> searchResult2 =
        new Search(this.testParser4, "White", "Data Type", true).resultList;
    Assert.assertEquals(1, searchResult2.size());

    // Searching for a value that is not in the CSV (negative case)
    List<String> searchResult3 =
        new Search(this.testParser4, "0.45", "Earnings Disparity", true).resultList;
    Assert.assertEquals(0, searchResult3.size());

    // Searching for another value that is not in the CSV (negative case)
    List<String> searchResult4 =
        new Search(this.testParser4, "Hispanic", "Data Type", true).resultList;
    Assert.assertEquals(0, searchResult4.size());
  }

  /** Tests searching by column when there are headers, no headers, and the wrong col is entered */
  @Test
  public void testSearchByCol() {
    // searches a file with headers using the col index
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    ArrayList<String> rowData = new ArrayList<>();
    rowData.add("71454");
    rowData.add("Rigel Kentaurus B");
    rowData.add("-0.50359");
    rowData.add("-0.42128");
    rowData.add("-1.1767");
    dataList.add(rowData);

    // searching a file with headers and a number column identifier
    Search search1 = new Search(this.testParser2, "Rigel Kentaurus B", "1", true);
    Assert.assertEquals(dataList, search1.resultList);

    // searches a file with headers with a valid col index
    Search search2 = new Search(this.testParser2, "Rigel Kentaurus B", "ProperName", true);
    Assert.assertEquals(search1.resultList, search2.resultList);

    // Searching for values that are present, but are in the wrong column;
    Search search3 = new Search(this.testParser2, "Rigel Kentaurus B", "StarID", true);
    Assert.assertEquals(new ArrayList<ArrayList<String>>(), search3.resultList);
  }

  /** Tests searching by row when there is no column identifier specified */
  @Test
  public void testSearchByRow() {
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    ArrayList<String> rowData = new ArrayList<>();
    rowData.add("71454");
    rowData.add("Rigel Kentaurus B");
    rowData.add("-0.50359");
    rowData.add("-0.42128");
    rowData.add("-1.1767");
    dataList.add(rowData);

    // tests searching with no column identifiers (search by row) but headers
    Search search1 = new Search(this.testParser2, "Rigel Kentaurus B", "-1", true);
    Assert.assertEquals(dataList, search1.resultList);

    // tests searching with no column identifiers (search by row) and no headers
    Search search2 = new Search(this.testParser2, "Rigel Kentaurus B", "", false);
    Assert.assertEquals(dataList, search2.resultList);

    // tests searching with column identifiers (search by row) and no headers
    Search search3 = new Search(this.testParser2, "Rigel Kentaurus B", "ProperName", false);
    Assert.assertEquals(dataList, search3.resultList);
  }

  /** Tests the CSVParser and search on different reader types */
  @Test
  public void differentReaders() {
    // tests fileReader search
    Search search1 = new Search(this.testParser, "Andrews", "1", false);

    // tests stringReader search
    Search search2 = new Search(this.testParser3, "Andrews", "1", false);

    // tests handling of different readers by comparing the different readers' input
    Assert.assertEquals(search1.resultList, search2.resultList);
  }
  /** Testing on data without headers */
  @Test
  public void withoutHeaders() {
    // testing valid column identifier without headings
    ArrayList<ArrayList<String>> expectList = new ArrayList<ArrayList<String>>();
    ArrayList<String> row1 = new ArrayList<>();
    row1.add("Ratty");
    row1.add("Andrews");
    row1.add("VDub");
    row1.add("Ivy Room");
    expectList.add(row1);
    ArrayList<String> row2 = new ArrayList<>();
    row2.add("Ratty");
    row2.add("Andrews");
    row2.add("Ivy Room");
    row2.add("VDub");
    expectList.add(row2);

    Search search1 = new Search(this.testParser, "Andrews", "1", false);
    Assert.assertEquals(expectList, search1.resultList);

    // testing invalid column identifier result without headings
    Search search2 = new Search(this.testParser, "Andrews", "somegibberish", false);
    ArrayList<String> row3 = new ArrayList<>();
    row3.add("Andrews");
    row3.add("Ratty");
    row3.add("Ivy Room");
    row3.add("VDub");
    expectList.add(row3);

    // asserts that the expected list and the search results of data without headers is the same
    Assert.assertEquals(expectList, search2.resultList);
  }

  /** Testing on data with headers */
  @Test
  public void withHeaders() {
    ArrayList<ArrayList<String>> expectList = new ArrayList<>();

    // First ArrayList
    ArrayList<String> list1 = new ArrayList<>();
    list1.add("1");
    list1.add("");
    list1.add("282.43485");
    list1.add("0.00449");
    list1.add("5.36884");
    expectList.add(list1);

    // Second ArrayList
    ArrayList<String> list2 = new ArrayList<>();
    list2.add("2");
    list2.add("");
    list2.add("43.04329");
    list2.add("0.00285");
    list2.add("-15.24144");
    expectList.add(list2);

    // Third ArrayList
    ArrayList<String> list3 = new ArrayList<>();
    list3.add("3");
    list3.add("");
    list3.add("277.11358");
    list3.add("0.02422");
    list3.add("223.27753");
    expectList.add(list3);

    // Fourth ArrayList
    ArrayList<String> list4 = new ArrayList<>();
    list4.add("118721");
    list4.add("");
    list4.add("-2.28262");
    list4.add("0.64697");
    list4.add("0.29354");
    expectList.add(list4);

    // assert that the expected list and actual search results of searching by column and correct
    Search search1 = new Search(this.testParser2, "", "ProperName", true);
    Assert.assertEquals(expectList, search1.resultList);
  }
}
