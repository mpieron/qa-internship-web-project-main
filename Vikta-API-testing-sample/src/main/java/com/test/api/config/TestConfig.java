package com.test.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test.properties")
//@PropertySource(value = "classpath:local.properties", ignoreResourceNotFound = true)
@ComponentScan("com.test.api")
public class TestConfig {
}
