package edu.brown.cs.student.ParserTests;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.Creators.CreatorFromRow;
import edu.brown.cs.student.main.Creators.IntegerCreator;
import edu.brown.cs.student.main.Creators.ListStringCreator;
import edu.brown.cs.student.main.Creators.StringCreator;
import edu.brown.cs.student.main.Exceptions.FactoryFailureException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CSVParserTestSuite {

  /** Test for CSVParser */

  /*
   * normal testing for input and output
   * */
  @Test
  void testStringParsing() throws IOException, FactoryFailureException {
    String filePath = "census/dol_ri_earnings_disparity.csv";
    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    parser.parse();

    List<List<String>> parsedData = parser.getParsedData();

    // row 1 of dol_ri_earnings_disparity
    List<String> dol_ri_earnings_disparity1 = new ArrayList<>();
    dol_ri_earnings_disparity1.add("RI");
    dol_ri_earnings_disparity1.add("White");
    dol_ri_earnings_disparity1.add("$1,058.47");
    dol_ri_earnings_disparity1.add("395773.6521");
    dol_ri_earnings_disparity1.add("$1.00");
    dol_ri_earnings_disparity1.add("75%");

    // row 3 of dol_ri_earnings_disparity
    List<String> dol_ri_earnings_disparity3 = new ArrayList<>();
    dol_ri_earnings_disparity3.add("RI");
    dol_ri_earnings_disparity3.add("Native American/American Indian");
    dol_ri_earnings_disparity3.add("$471.07");
    dol_ri_earnings_disparity3.add("2315.505646");
    dol_ri_earnings_disparity3.add("$0.45");
    dol_ri_earnings_disparity3.add("0%");

    // row 6 of dol_ri_earnings_disparity
    List<String> dol_ri_earnings_disparity6 = new ArrayList<>();
    dol_ri_earnings_disparity6.add("RI");
    dol_ri_earnings_disparity6.add("Multiracial");
    dol_ri_earnings_disparity6.add("$971.89");
    dol_ri_earnings_disparity6.add("8883.049171");
    dol_ri_earnings_disparity6.add("$0.92");
    dol_ri_earnings_disparity6.add("2%");

    Assert.assertEquals(parsedData.size(), 7);
    Assert.assertEquals(parsedData.get(1), dol_ri_earnings_disparity1);
    Assert.assertEquals(parsedData.get(3), dol_ri_earnings_disparity3);
    Assert.assertEquals(parsedData.get(6), dol_ri_earnings_disparity6);
  }

  /**
   * test_differentClassImplementationInteger
   *
   * <p>Test for using multiple CreatorFromRow classes to extract CSV data in different formats 1
   */
  @Test
  public void test_differentClassImplementationInteger() throws IOException {
    String filePath = "newclasses/data_with_integers.csv";
    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<Integer> creator = new IntegerCreator();
    CSVParser<Integer> parser = new CSVParser<>(reader, creator);
    parser.parse();

    List<Integer> parsedData = parser.getParsedData();

    // Assert specific values based on the test data
    Assert.assertEquals(parsedData.size(), 3);
    Assert.assertEquals(parsedData.get(0), Integer.valueOf(42));
    Assert.assertEquals(parsedData.get(1), Integer.valueOf(-7));
    Assert.assertEquals(parsedData.get(2), Integer.valueOf(100));
  }

  /**
   * test_differentClassImplementationString
   *
   * <p>Test for using multiple CreatorFromRow classes to extract CSV data in different formats 2
   */
  @Test
  public void test_differentClassImplementationString() throws IOException {

    String filePath = "newclasses/data_with_strings.csv";
    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<String> creator = new StringCreator();
    CSVParser<String> parser = new CSVParser<>(reader, creator);
    parser.parse();

    List<String> parsedData = parser.getParsedData();

    // Assert specific values based on the test data
    Assert.assertEquals(parsedData.size(), 3);
    Assert.assertEquals(parsedData.get(0), "Apple");
    Assert.assertEquals(parsedData.get(1), "Banana");
    Assert.assertEquals(parsedData.get(2), "Cherry");
  }

  /**
   * test_emptyCSV
   *
   * <p>Test for parsing an empty csv
   */
  @Test
  public void test_emptyCSV() throws IOException {

    String filePath = "newclasses/empty.csv";
    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<String> creator = new StringCreator();
    CSVParser<String> parser = new CSVParser<>(reader, creator);
    parser.parse();

    List<String> parsedData = parser.getParsedData();

    Assert.assertTrue(parsedData.isEmpty());
  }

  /**
   * test_Readers
   *
   * <p>Test for using different readers
   */
  @Test
  public void test_Readers() throws IOException {
    String randomCSVString =
        "Item,Color,Weight\n" + "1,Blue,80\n" + "2,Purple,0.2\n" + "3,Turquoise,4\n" + "4,Red,105";
    StringReader readReader = new StringReader(randomCSVString);
    CreatorFromRow creator = new ListStringCreator();
    CSVParser<List<String>> randomParse = new CSVParser<>(readReader, creator);
    randomParse.parse();

    List<List<String>> myList = new ArrayList<>();

    ArrayList<String> start = new ArrayList<>();
    start.add("Item");
    start.add("Color");
    start.add("Weight");
    myList.add(start);

    ArrayList<String> one = new ArrayList<>();
    one.add("1");
    one.add("Blue");
    one.add("80");
    myList.add(one);

    ArrayList<String> two = new ArrayList<>();
    two.add("2");
    two.add("Purple");
    two.add("0.2");
    myList.add(two);

    ArrayList<String> three = new ArrayList<>();
    three.add("3");
    three.add("Turquoise");
    three.add("4");
    myList.add(three);

    ArrayList<String> four = new ArrayList<>();
    four.add("4");
    four.add("Red");
    four.add("105");
    myList.add(four);

    Assert.assertEquals(myList, randomParse.getParsedData());
  }

  /**
   * testMultipleCreators
   *
   * <p>Test suite for evaluating the functionality of the CSVParser class in the CSVParser project.
   *
   * <p>Tests include assessing the parser's ability to work with multiple CreatorFromRow classes
   * that operate on differently shaped CSV data. The goal is to ensure that the parser can accept
   * an arbitrary CreatorFromRow and invoke it on each row of the data without the need to be
   * concerned about the actual type the creator strategy returns.
   */
  @Test
  public void testMultipleCreators() throws IOException, FactoryFailureException {
    // Test data containing both integer and string values
    String csvData = "1,John,25\n2,Jane,30\n3,Bob,22";

    // Creating a CSVParser with IntegerCreator
    Reader reader = new StringReader(csvData);
    CreatorFromRow<Integer> integerCreator = new IntegerCreator();
    CSVParser<Integer> integerParser = new CSVParser<>(reader, integerCreator);

    integerParser.parse();

    // Creating a CSVParser with ListStringCreator
    reader = new StringReader(csvData);
    CreatorFromRow<List<String>> listStringCreator = new ListStringCreator();
    CSVParser<List<String>> listStringParser = new CSVParser<>(reader, listStringCreator);

    listStringParser.parse();

    // Validate the parsed data with IntegerCreator
    List<Integer> parsedIntegers = Arrays.asList(1, 2, 3);
    Assert.assertEquals(integerParser.getParsedData(), parsedIntegers);

    // Validate the parsed data with ListStringCreator
    List<List<String>> parsedListStrings = new ArrayList<>();
    parsedListStrings.add(Arrays.asList("1", "John", "25"));
    parsedListStrings.add(Arrays.asList("2", "Jane", "30"));
    parsedListStrings.add(Arrays.asList("3", "Bob", "22"));
    Assert.assertEquals(listStringParser.getParsedData(), parsedListStrings);
  }

  /** Most other tests can be found within SearchTestSuite */
}
