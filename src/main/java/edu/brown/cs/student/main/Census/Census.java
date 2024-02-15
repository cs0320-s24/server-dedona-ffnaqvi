package edu.brown.cs.student.main.Census;

public class Census {
  private String NAME;
  private Double S2802_C03_022E;
  private String state;
  private String county;

  public Census() {}

  /**
   * Method to display the Census fields as a string
   *
   * @return String of census fields
   */
  @Override
  public String toString() {
    return "["
        + this.county
        + ", "
        + this.state
        + " has the estimated percent broadband internet subscription of: "
        + this.S2802_C03_022E
        + "%"
        + "]\n";
  }

  /**
   * Method to set the state field to the passed in String
   *
   * @param s
   */
  public void setState(String s) {
    this.state = s;
  }

  /**
   * Method to set the county field to the passed in String
   *
   * @param s
   */
  public void setCounty(String s) {
    this.county = s;
  }

  /**
   * Method to set the S2802_C03_022E field to the passed in double
   *
   * @param i
   */
  public void setPercentageOfAccess(double i) {
    this.S2802_C03_022E = i;
  }
}
