package edu.brown.cs.student.main.Caching;

import java.io.IOException;
import java.net.URISyntaxException;

/** Interface for datasources which has the sendRequest and setDatasource methods */
public interface ACSDatasource {
  public String sendRequest(String stateCode, String countyCode)
      throws URISyntaxException, IOException, InterruptedException;

  void setDatasource(ACSDatasource datasource);
}
