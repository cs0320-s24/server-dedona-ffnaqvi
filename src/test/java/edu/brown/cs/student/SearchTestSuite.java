package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.brown.cs.student.main.Creators.CreatorFromRow;
import edu.brown.cs.student.main.Creators.ListStringCreator;
import edu.brown.cs.student.main.Parse.CSVParser;
import edu.brown.cs.student.main.Search.Search;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTestSuite {

  /** Test for CSVParser */

  /**
   * testRepl_FileNotFoundException
   *
   * <p>Test to make sure that files that are either nonexistent or that are restricted are not
   * opened
   */
  @Test
  public void testRepl_FileNotFoundException() {

    assertThrows(
        FileNotFoundException.class,
        () -> {
          String searchValue = "irrelevant";
          boolean hasHeaders = false;
          String filePathNonexistent = "nonexistent.csv";
          String filePathRestricted = "Restricted.csv";

          Reader reader = new BufferedReader(new FileReader("data/" + filePathNonexistent));
          CreatorFromRow<List<String>> creator = new ListStringCreator();
          CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
          Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(null, null);
          Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
          search.search();

          Reader reader2 = new BufferedReader(new FileReader("data/" + filePathRestricted));
          CreatorFromRow<List<String>> creator2 = new ListStringCreator();
          CSVParser<List<String>> parser2 = new CSVParser<>(reader, creator);
          Map.Entry<String, Integer> columnIdentifier2 = new AbstractMap.SimpleEntry<>(null, null);
          Search search2 = new Search(parser, searchValue, columnIdentifier, hasHeaders);
          search.search();
        });
  }

  /**
   * testRepl_FileFound
   *
   * <p>Test to make sure that files that are existent can be opened
   */
  @Test
  public void testRepl_FileFound() throws FileNotFoundException {

    String searchValue = "RI";
    boolean hasHeaders = true;
    String filePathExistent = "census/dol_ri_earnings_disparity.csv";

    Reader reader = new BufferedReader(new FileReader("data/" + filePathExistent));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(null, null);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();
    List<List<String>> searchReturn = search.getResultList();

    Assert.assertEquals(searchReturn.size(), 6);
  }

  /**
   * testRepl_NoHeaders
   *
   * <p>Test to make sure that files that do not have headers are still valid
   */
  @Test
  public void testRepl_NoHeaders() throws FileNotFoundException {

    String searchValue1 = "c";
    String searchValue2 = "9";
    boolean hasHeaders = false;
    String filePathExistent = "noheaders.csv";

    Reader reader = new BufferedReader(new FileReader("data/" + filePathExistent));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(null, null);
    Search search1 = new Search(parser, searchValue1, columnIdentifier, hasHeaders);
    search1.search();
    List<List<String>> searchReturn1 = search1.getResultList();

    Reader reader2 = new BufferedReader(new FileReader("data/" + filePathExistent));
    CreatorFromRow<List<String>> creator2 = new ListStringCreator();
    CSVParser<List<String>> parser2 = new CSVParser<>(reader2, creator2);
    Map.Entry<String, Integer> columnIdentifier2 = new AbstractMap.SimpleEntry<>(null, null);
    Search search2 = new Search(parser2, searchValue2, columnIdentifier, hasHeaders);
    search2.search();
    List<List<String>> searchReturn2 = search2.getResultList();

    Assert.assertEquals(searchReturn1.size(), 2);
    Assert.assertEquals(searchReturn2.size(), 10);
  }

  /**
   * testRepl_InconsistentRows
   *
   * <p>Test to ensure that inconsistent rows result in an exception
   */
  @Test
  public void testRepl_InconsistentRows() throws FileNotFoundException {
    String searchValue = "irrelevant";
    boolean hasHeaders = true;
    String filePathInconsistent = "malformed/malformed_signs.csv";

    Reader reader = new BufferedReader(new FileReader("data/" + filePathInconsistent));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(null, null);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    List<List<String>> searchReturn = search.getResultList();

    Assert.assertEquals(searchReturn.size(), 0);
  }

  @Test
  public void test() throws FileNotFoundException {
    String searchValue = "irrelevant";
    boolean hasHeaders = true;
    String filePathInconsistent = "malformed/malformed_signs.csv";

    Reader reader = new BufferedReader(new FileReader("data/" + filePathInconsistent));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(null, null);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    List<List<String>> searchReturn = search.getResultList();
  }

  /**
   * testRepl_InconsistentIndex
   *
   * <p>Test to ensure that inconsistent indices result in an exception
   */
  @Test
  public void testRepl_InconsistentIndex() {
    assertThrows(
        IndexOutOfBoundsException.class,
        () -> {
          String searchValue = "irrelevant";
          boolean hasHeaders = true;
          String filePath = "stars/stardata.csv";

          Integer inconsistentIndex = 20;

          Reader reader = new BufferedReader(new FileReader("data/" + filePath));
          CreatorFromRow<List<String>> creator = new ListStringCreator();
          CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
          Map.Entry<String, Integer> columnIdentifier =
              new AbstractMap.SimpleEntry<>(null, inconsistentIndex);
          Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
          search.search();
        });
  }

  /**
   * testRepl_ConsistentIndex
   *
   * <p>Test to ensure that consistent indices work
   */
  @Test
  public void testRepl_ConsistentIndex() throws FileNotFoundException {
    String searchValue = "8";
    boolean hasHeaders = true;
    String filePath = "stars/stardata.csv";

    Integer consistentIndex = 0;

    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier =
        new AbstractMap.SimpleEntry<>(null, consistentIndex);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    List<List<String>> searchReturn = search.getResultList();

    Assert.assertEquals(
        searchReturn.get(0), List.of("8", "Eura", "174.01562", "0.08288", "84.44669"));
  }

  /**
   * testRepl_ValuesDoNotExist
   *
   * <p>Test to make sure that search values matter for searching output
   */
  @Test
  public void testRepl_ValuesDoNotExist() throws FileNotFoundException {

    String searchValue1 = "nonexistent";
    boolean hasHeaders = false;
    String filePathExistent = "noheaders.csv";

    Reader reader = new BufferedReader(new FileReader("data/" + filePathExistent));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier = new AbstractMap.SimpleEntry<>(null, null);
    Search search1 = new Search(parser, searchValue1, columnIdentifier, hasHeaders);
    search1.search();
    List<List<String>> searchReturn1 = search1.getResultList();

    Assert.assertEquals(searchReturn1.size(), 0);
  }

  /**
   * testRepl_ValuesDoExistBut
   *
   * <p>Test for searching for values that are present, but are in the wrong column;
   */
  @Test
  public void testRepl_ValuesDoExistBut() throws FileNotFoundException {
    String searchValue = "8";
    boolean hasHeaders = true;
    String filePath = "stars/stardata.csv";
    String strColIdentifier = "ProperName";

    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier =
        new AbstractMap.SimpleEntry<>(strColIdentifier, null);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    List<List<String>> searchReturn = search.getResultList();

    Assert.assertEquals(searchReturn.size(), 0);
  }

  /**
   * testRepl_SearchByName
   *
   * <p>Test for searching for values given a column name identifier
   */
  @Test
  public void testRepl_SearchByName() throws FileNotFoundException {
    String searchValue = "Andreas";
    boolean hasHeaders = true;
    String filePath = "stars/stardata.csv";
    String strColIdentifier = "ProperName";

    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier =
        new AbstractMap.SimpleEntry<>(strColIdentifier, null);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    List<List<String>> searchReturn = search.getResultList();

    Assert.assertEquals(searchReturn.size(), 1);
  }

  /**
   * testRepl_SearchByIndex
   *
   * <p>Test for searching for values given column index;
   */
  @Test
  public void testRepl_SearchByIndex() throws FileNotFoundException {
    String searchValue = "Andreas";
    boolean hasHeaders = true;
    String filePath = "stars/stardata.csv";
    Integer intColIdentifier = 1;

    Reader reader = new BufferedReader(new FileReader("data/" + filePath));
    CreatorFromRow<List<String>> creator = new ListStringCreator();
    CSVParser<List<String>> parser = new CSVParser<>(reader, creator);
    Map.Entry<String, Integer> columnIdentifier =
        new AbstractMap.SimpleEntry<>(null, intColIdentifier);
    Search search = new Search(parser, searchValue, columnIdentifier, hasHeaders);
    search.search();

    List<List<String>> searchReturn = search.getResultList();

    Assert.assertEquals(searchReturn.size(), 1);
  }
}
