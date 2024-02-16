package edu.brown.cs.student.CensusAPITests;

import edu.brown.cs.student.main.Census.Census;
import edu.brown.cs.student.main.Census.CensusAPIUtilities;
import java.io.IOException;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Test deserializing Census data */
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
        "[Lauderdale County, Alabama has the estimated percent broadband internet subscription of: 77.8%]\n",
        censusList.get(0).toString());
  }
}
