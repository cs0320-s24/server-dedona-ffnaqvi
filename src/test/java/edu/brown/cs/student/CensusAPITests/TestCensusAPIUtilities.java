package edu.brown.cs.student.CensusAPITests;

import edu.brown.cs.student.main.Census.Census;
import edu.brown.cs.student.main.Census.CensusAPIUtilities;
import java.io.IOException;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test deserializing soup recipes
 *
 * <p>Because we're using JUnit here, we needed to add JUnit to pom.xml.
 *
 * <p>In a real application, we'd want to test better---e.g., if it's part of our spec that
 * SoupHandler throws an IOException on invalid JSON, we'd want to test that.
 */
public class TestCensusAPIUtilities {

    @Test
    public void testCensusDeserialize() throws IOException {
      String lauderdaleCounty =
          "[[\"NAME\",\"S2802_C03_022E\",\"state\",\"county\"],\n"
              + "[\"Lauderdale County, Alabama\",\"77.8\",\"01\",\"077\"]]";
      List<Census> censusList = CensusAPIUtilities.deserializeCensus(lauderdaleCounty);
      // This might throw an IOException, but if so JUnit will mark the test as failed.
      System.out.println(censusList.get(0).toString());
      Assert.assertEquals(
          "[Lauderdale County, Alabama has the estimated percent broadband internet subscription of: 77.8%]\n", censusList.get(0).toString());
    }
}
