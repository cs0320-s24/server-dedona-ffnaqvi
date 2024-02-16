package edu.brown.cs.student.main.Caching;

import edu.brown.cs.student.main.Census.CensusHandler;

import java.io.IOException;
import java.net.URISyntaxException;

/** Class that mocks the ACS Datasource */
public class MockedAPIDatasource implements ACSDatasource {

  private ACSDatasource constantData; //the census handler

  @Override
  public String sendRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException {
    return this.constantData.toString();
  }

  @Override
  public void setDatasource(ACSDatasource datasource) {
    this.constantData = datasource;
  }
}
