An extremely simple GUI test automation solution for Vikta application. Based on TestNG, Selenium and Selenide frameworks.
Contains the very basics of a test automation framework. 
Educational purposes only. Needs heavy refactoring prior to be used for a real project.

# How-to ...:
## ... configure your workstation
- Install Java, Maven, IntelijIdea and Lombok Idea plugin.
- Install Allure CLI - - https://docs.qameta.io/allure/#_installing_a_commandline

## ... run tests using maven
```
mvn clean test 
```
e.g. to run all the scenarios tagged as "login":
```
T B D
```

## ...run something using IntelijIdea
1. Create a TestNG Run/Debug Configuration
2. Set `Class` to the test class e.g. `com.griddynamics.qa.vikta.uitesting.sample.tests.LoginTest`. You could specify concrete method(test) as well
3. Save the configuration and use it to run or debug your tests.

## ...view test run report
1. Run the tests
2. Assuming Allure CLI is installed - execute:
```
allure serve
```
in the project's directory.

3. Enjoy.


# Hints:
- Configure TestRunner configuration in IntelijIdea for local runs and debugging.

# Tasks
- [TASKS.md](https://gitlab.griddynamics.net/kkhyzhniak/qa-internship-web-project/-/blob/main/Vikta-UI-testing-sample/TASKS.md) file contains certain steps to do with the project to gain the best practical expirience.

# Links:
- https://www.selenium.dev/documentation/ 
- https://selenide.org/documentation.html 
- https://testng.org/doc/
- https://www.baeldung.com/testng 
- https://www.baeldung.com/java-selenium-with-junit-and-testng 
- https://assertj.github.io/doc/
- https://projectlombok.org/
- PageObject related:
    * https://selenium.dev/documentation/en/guidelines_and_recommendations/page_object_models/
    * https://martinfowler.com/bliki/PageObject.html
    * https://www.baeldung.com/selenium-webdriver-page-object
    * https://www.pluralsight.com/guides/getting-started-with-page-object-pattern-for-your-selenium-tests



