package edu.brown.cs.student.main.Caching;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Class that mocks the ACS Datasource
 */
public class MockedAPIDatasource implements ACSDatasource{

    @Override
    public String sendRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException {
        return null;
    }

    @Override
    public void setDatasource(ACSDatasource datasource) {

    }
}
