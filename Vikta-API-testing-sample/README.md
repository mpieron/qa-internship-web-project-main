Simple API test automation solution for Vikta application based on RestAssured framework.
Educational purposes only.

## How to:

### configure lombok plugin in IntelijIDEA
follow steps by link https://projectlombok.org/setup/intellij

### run one test using maven
```
mvn clean test surefire-report:report -Dtest=<test class>#<test method>
```

### receive test report in html format
1. Use maven to run the tests
```
mvn clean test surefire-report:report
```
2. Go to `./target/site`
3. Open `surefire-report.html` in a browser


## Links:
- https://github.com/rest-assured/rest-assured/wiki/GettingStarted
- https://github.com/rest-assured/rest-assured/wiki/usage
- https://assertj.github.io/doc/
- http://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html
- https://maven.apache.org/surefire/maven-surefire-report-plugin/usage.html
- https://projectlombok.org/features/all
- https://projectlombok.org/setup/intellij