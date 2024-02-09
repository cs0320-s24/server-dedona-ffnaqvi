package edu.brown.cs.student.main.Flower;

/** Flower enum class */
public enum Flower {
  TULIP,
  ROSE,
  DAISY,
  SPRIG;

  /**
   * Method to check if Flower objects are equal to strings
   *
   * @param s string to compare to
   * @return boolean
   */
  public boolean equals(String s) {
    return this.toString().equals(s);
  }
}
