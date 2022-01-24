package com.griddynamics.qa.vikta.uitesting.sample.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Provides access to test data and properties.
 */
public class DataProvider {

  public static TestDataAndProperties get() {
    // If you are curious - http://owner.aeonbits.org/docs/singleton/
    return ConfigCache.getOrCreate(TestDataAndProperties.class);
  }
}
