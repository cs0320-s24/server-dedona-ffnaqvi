package edu.brown.cs.student.main.Caching;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ACSDatasource {

    //implements methods to getBroadabnd, which will actually send requests to the Census API

    //should be implemented in fake classes as well to check that server is implemeneted correctly
    public String sendRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException;
}
