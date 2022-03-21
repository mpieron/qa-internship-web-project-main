package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.UserManagementSteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserManagementTest extends BaseTest {

  @BeforeMethod(onlyForGroups = "onlyLogin")
  public void loginAsAdmin() {
    loginSteps.openLoginPage();
    loginSteps.loginAsAdmin();
  }

  @BeforeMethod(onlyForGroups = "needNewUser")
  public void createNewUser() {
    loginAsAdmin();

    userManagementSteps.clickCreateUser();
    userManagementSteps.fillAllUserFields();
    userManagementSteps.clickSaveButton();
  }

  @AfterMethod(onlyForGroups = "cleanUp")
  public void clean() {
    userManagementSteps.clickUsers();
    userManagementSteps.deleteAddedUsers();
  }

  @Test(groups = "onlyLogin")
  public void checkIfFirstUserAndAdminHaveCorrectData() {
    userManagementSteps.clickUsers();
    userManagementSteps.verifyIfFirstUserAndAdminHaveCorrectData();
  }

  //    Searching for users by term doesn't work for any user field
  @Test(groups = "onlyLogin")
  public void canFindUserByLoginName() {
//        Click Users
    userManagementSteps.clickUsers();

//        Get first user login name
    String loginName = userManagementSteps.getFirstUserLoginName();

//        Search user by login name
    userManagementSteps.searchUserByLoginName(loginName);

//        Verify if found user
    userManagementSteps.verifyIfFoundUserByLoginName();
  }

  @Test(groups = { "needNewUser", "cleanUp" })
  public void canEditUser() {
//        Click Users
    userManagementSteps.clickUsers();

//        Click last user
    userManagementSteps.clickLastUser();

//        Change user fields
    String userInfo = userManagementSteps.fillAllUserFields();

//        Save changes
    userManagementSteps.clickSaveButton();

//        Go to the list of users
    userManagementSteps.clickBackToChat();

//        Verify if user was changed
    userManagementSteps.verifyLastUserFields(userInfo);
  }

  @Test(groups = "needNewUser")
  public void canDeleteUser() {
//        Click Users
    userManagementSteps.clickUsers();

//        Delete last user
    userManagementSteps.deleteAddedUsers();

//        Verify if user was deleted
    userManagementSteps.verifyIfUserWasDeleted();
  }

//  we can not create user with any empty field except avatar image url
//  without login name, email or firstname - nothing will happen
//  with empty any else - will appear communicate under empty field and table with errors
  @Test(groups = "onlyLogin")
  public void canNotCreateUserWithoutLoginName() {
//    Click create user
    userManagementSteps.clickCreateUser();

//    Fill email and name
    userManagementSteps.fillUserField(UserManagementSteps.UserField.EMAIL);
    userManagementSteps.fillUserField(UserManagementSteps.UserField.FIRST_NAME);

//    Click save button
    userManagementSteps.clickSaveButton();

//    Verify if user was not created
    userManagementSteps.verifyIfCanNotCreateUserWithoutLoginName();
  }

  @Test(groups = "onlyLogin")
  public void canNotCreateUserWithoutPassword() {
//    Click create user
    userManagementSteps.clickCreateUser();

//    Fill all without password and avatar image url
    userManagementSteps.fillUserField(UserManagementSteps.UserField.EMAIL);
    userManagementSteps.fillUserField(UserManagementSteps.UserField.FIRST_NAME);
    userManagementSteps.fillUserField(UserManagementSteps.UserField.LOGIN_NAME);
    userManagementSteps.fillUserField(UserManagementSteps.UserField.MIDDLE_NAME);
    userManagementSteps.fillUserField(UserManagementSteps.UserField.SURNAME);

//    Click save button
    userManagementSteps.clickSaveButton();

//    Verify if user was not created
    userManagementSteps.verifyIfCanNotCreateUserWithoutPassword();
  }

  @Test(groups = { "onlyLogin", "cleanUp" })
  public void canCreateUser() {
//    Click create user
    userManagementSteps.clickCreateUser();

//    Fill all fields
    String userInfo = userManagementSteps.fillAllUserFields();

//    Click save button
    userManagementSteps.clickSaveButton();

//        Go to the list of users
    userManagementSteps.clickBackToChat();

//    Verify if user was created
    userManagementSteps.verifyLastUserFields(userInfo);
  }
}
