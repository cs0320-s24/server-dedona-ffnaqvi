package edu.brown.cs.student.main.Caching;

import edu.brown.cs.student.main.Census.CensusHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/** Class that mocks the ACS Datasource */
public class MockedAPIDatasource implements ACSDatasource {

  private ACSDatasource constantData; //the census handler
  private Map<String, Object> constantMap;

  public MockedAPIDatasource(Map<String, Object> map) {
    this.constantMap = map;
  }

  @Override
  public String sendRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException {
    return this.constantMap.toString();
  }

  @Override
  public void setDatasource(ACSDatasource datasource) {
    this.constantData = datasource;
  }
}
