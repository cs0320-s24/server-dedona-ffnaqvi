package edu.brown.cs.student.main.Income;

public class Income {
  private String city;
  private String medianHouseIncome;
  private String medianFamilyIncome;
  private String perCapitaIncome;

  public Income(
      String pCity,
      String pMedianHouseIncome,
      String pMedianFamilyIncome,
      String pPerCapitaIncome) {
    this.city = pCity;
    this.medianHouseIncome = pMedianHouseIncome;
    this.medianFamilyIncome = pMedianFamilyIncome;
    this.perCapitaIncome = pPerCapitaIncome;
  }

  public String getMedianHouseIncome() {
    return this.medianHouseIncome;
  }

  public String getCity() {
    return this.city;
  }

  public String getMedianFamilyIncome() {
    return this.medianFamilyIncome;
  }

  public String getPerCapitaIncome() {
    return this.perCapitaIncome;
  }

  public String toString() {
    // TODO
    return null;
  }
}
