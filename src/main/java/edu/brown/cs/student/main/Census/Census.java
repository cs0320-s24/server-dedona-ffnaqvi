package edu.brown.cs.student.main.Census;

public class Census {
  private String targetState;
  private String targetCounty;
  private Integer percentageOfAccess;

  public Census() {}

  @Override
  public String toString() {
    return this.targetCounty + ", " + this.targetState + " results: " + this.percentageOfAccess;
  }
}
