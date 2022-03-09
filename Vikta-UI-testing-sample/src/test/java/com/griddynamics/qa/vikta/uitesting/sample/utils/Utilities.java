package com.griddynamics.qa.vikta.uitesting.sample.utils;

import com.github.javafaker.Faker;
import java.util.Random;

public class Utilities {

  static Faker faker = new Faker();
  static String priceFrom = "0.654";
  static String priceTo = "122.03";

  private Utilities(){}

  public static String generateEmail() {
    return faker.bothify("?????##@gmail.com");
  }

  public static String generateStreetAddress() {
    return faker.address().streetAddress();
  }

  public static String generateCityAddress() {
    return faker.address().city();
  }

  public static String generateCountryAddress() {
    return faker.address().country();
  }

  public static String generatePostalCodeAddress() {
    return faker.address().zipCode();
  }

  public static String generateAdditionalStreetInfoAddress() {
    return faker.address().streetAddressNumber();
  }

  public static String generateNicknameAddress() {
    return faker.regexify("[a-z1-9]{4}");
  }

  public static String generateName() {
    return faker.name().firstName();
  }

  public static String generateSurname() {
    return faker.name().lastName();
  }

  public static String generateLoginName() {
    return faker.name().username();
  }

  public static String generatePassword() {
    return faker.regexify("[a-z1-9]{5}");
  }

  public static String generateRating() {
    return String.valueOf(faker.random().nextInt(0, 5));
  }

  public static String generatePriceTo() {
    return priceTo;
  }

  public static String generatePriceFrom() {
    return priceFrom;
  }
}
