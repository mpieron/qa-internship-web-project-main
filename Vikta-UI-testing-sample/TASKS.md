1. Read the README first.
2. Read it again, just in case.
3. Go through the code, try to map what you see to what you were told in scope of "Test automation architecture" lecture.
4. As you go through the code - try to find answers:
    - where is test data, how it is used?
    - where are the tests?
    - where is the configuration and how it shall be used/changed?
5. Ask questions!
6. Try to run login tests:
```
mvn clean test -Dcucumber.options="--tags @login"
``` 
7. Any issues? Resolve them, please. 
8. Look at the login tests - could that be improved somehow?
9. Run the registration related tests:
```
mvn clean test -Dcucumber.options="--tags @signup"
```
10. Improve the registration tests by resolving TODOs there.
11. Find all other TODOs and try to resolve them.
12. Develop a plan and cover the rest of features with automated tests:
    - User management;
    - User search;
    - Image management;
    - Image search;
    - Category management;
    - Category search;
    - Purchasing;
    - Searching;
    - Payment methods;
    - Addresses.
Note, some features could include both Admin and RegularUser parts.