package edu.brown.cs.student.main.Census;

public class Census {
  private String state;
  private String county;
  private Integer percentageOfAccess;

  public Census() {}

  @Override
  public String toString() {
    return this.county + ", " + this.state + " results: " + this.percentageOfAccess;
  }
}
