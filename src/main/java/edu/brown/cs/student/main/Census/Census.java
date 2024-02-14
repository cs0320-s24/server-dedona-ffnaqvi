package edu.brown.cs.student.main.Census;

public class Census {
  private String NAME;
  private String S2802_C03_022E;
  private String state;
  private String county;

  public Census() {}

  @Override
  public String toString() {
    return this.county + ", " + this.state + " results: " + this.S2802_C03_022E;
  }
}
