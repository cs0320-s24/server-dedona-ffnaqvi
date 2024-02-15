package edu.brown.cs.student.CensusAPITests;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.brown.cs.student.main.Census.Census;
import edu.brown.cs.student.main.Census.CensusAPIUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

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
        String chickenNoodle =
                "{\n"
                        + "    \"soupName\": \"chicken noodle\",\n"
                        + "    \"ingredients\": [\"chicken broth\", \"celery\", \"carrot\", \"onion\"],\n"
                        + "    \"isHot\": true\n"
                        + "  }";
        List<Census> censusList = CensusAPIUtilities.deserializeCensus(chickenNoodle);
        // This might throw an IOException, but if so JUnit will mark the test as failed.
        Assert.assertTrue(censusList.get(0).equals("hello")); //TODO: replace hello with valid string
    }

}
