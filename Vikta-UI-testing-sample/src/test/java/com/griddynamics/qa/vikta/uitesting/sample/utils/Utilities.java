package com.griddynamics.qa.vikta.uitesting.sample.utils;

import com.github.javafaker.Faker;
import java.util.Random;

public class Utilities {

  private static final Faker faker = new Faker();
  private static final String priceFrom = "0.654";
  private static final String priceTo = "122.03";

  private Utilities() {}

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

  public static String generateNickname() {
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

  public static String generateCardNumber() {
    return faker.regexify("[1-9]{15}");
  }

  public static String generateCardCode() {
    return faker.regexify("[1-9]{3}");
  }

  public static String generateCardOwner() {
    return String.format("%s %s", generateName(), generateSurname()).toUpperCase();
  }

  public static String generateExpirationDate() {
    return faker.numerify("2022-10-10");
  }

  public static String generateURL(){ return faker.bothify("https://?????/????.jpg");}

  public static String generateTitle(){ return faker.book().title();}
}
