package edu.brown.cs.student.main.Caching;

import edu.brown.cs.student.main.Census.CensusHandler;

import java.io.IOException;
import java.net.URISyntaxException;

/** Class that mocks the ACS Datasource */
public class MockedAPIDatasource implements ACSDatasource {

  private CensusHandler constantData;

  public void MockedAPIDataSource(CensusHandler constantData) {
    this.constantData = constantData;
  }

  @Override
  public String sendRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException {
    return this.constantData.toString();
  }

  @Override
  public void setDatasource(ACSDatasource datasource) {
    //unsure?
  }
}
