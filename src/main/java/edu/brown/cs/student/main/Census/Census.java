package edu.brown.cs.student.main.Census;

public class Census {
  private String NAME;
  private Double S2802_C03_022E;
  private String state;
  private String county;

  public Census() {}

  @Override
  public String toString() {
    return "[" + this.county + ", " + this.state + " has the estimated percent broadband internet subscription of: " + this.S2802_C03_022E + "%" +"]\n";
  }

  public void setState(String s) {
    this.state = s;
  }

  public void setCounty(String s) {
    this.county = s;
  }

  public void setPercentageOfAccess(double i) {
    this.S2802_C03_022E = i;
  }
}
