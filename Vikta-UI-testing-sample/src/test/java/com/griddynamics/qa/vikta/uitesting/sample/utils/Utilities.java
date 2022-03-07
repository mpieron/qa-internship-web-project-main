package com.griddynamics.qa.vikta.uitesting.sample.utils;

import com.github.javafaker.Faker;
import java.util.Random;

public class Utilities {

  Faker faker = new Faker();
  Random random = new Random();

  public String generateEmail() {
    return faker.bothify("?????##@gmail.com");
  }

  public String generateAddress() {
    return faker.address().fullAddress();
  }

  public String generateName() {
    return faker.name().firstName();
  }

  public String generateSurname() {
    return faker.name().lastName();
  }

  public String generateLoginName() {
    return faker.name().username();
  }

  public String generatePassword() {
    return faker.regexify("[a-z1-9]{5}");
  }

  public String generateRating() {
    return String.valueOf(faker.random().nextInt(0, 5));
  }

  public String generatePriceTo() {
    return String.valueOf(faker.random().nextInt(100) + faker.random().nextDouble());
  }

  public String generatePriceFrom() {
    return String.valueOf(0.654);
  }
}
