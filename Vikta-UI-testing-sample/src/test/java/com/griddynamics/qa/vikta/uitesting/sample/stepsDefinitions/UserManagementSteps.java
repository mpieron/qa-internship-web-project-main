package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.*;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import java.util.List;
import java.util.StringJoiner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserManagementSteps extends BaseSteps {

  public UserManagementSteps(WebDriver driver) {
    super(driver);
  }

  public enum UserField {
    LOGIN_NAME,
    PASSWORD,
    EMAIL,
    SURNAME,
    FIRST_NAME,
    MIDDLE_NAME,
  }

  @Step
  public String fillUserField(UserManagementSteps.UserField userField) {
    String valueToReturn;
    switch (userField) {
      case LOGIN_NAME:
        valueToReturn = Utilities.generateLoginName();
        userEditAddPage().typeLoginName(valueToReturn);
        break;
      case PASSWORD:
        valueToReturn = Utilities.generatePassword();
        userEditAddPage().typePassword(valueToReturn);
        break;
      case EMAIL:
        valueToReturn = Utilities.generateEmail();
        userEditAddPage().typeEmail(valueToReturn);
        break;
      case SURNAME:
        valueToReturn = Utilities.generateSurname();
        userEditAddPage().typeSurname(valueToReturn);
        break;
      case FIRST_NAME:
        valueToReturn = Utilities.generateName();
        userEditAddPage().typeFirstName(valueToReturn);
        break;
      case MIDDLE_NAME:
        valueToReturn = Utilities.generateName();
        userEditAddPage().typeMiddleName(valueToReturn);
        break;
      default:
        throw new IllegalArgumentException("Unsupported Address Add/Edit page field name: " + userField);
    }
    return valueToReturn;
  }

  @Step
  public void clickUsers() {
    adminBasePage().clickUsers();
  }

  @Step
  public void clickCreateUser() {
    adminBasePage().clickCreateUser();
  }

  @Step
  public String getFirstUserLoginName() {
    return usersListPage().getAllUsersHyperlinksList().get(0).getText();
  }

  @Step
  public void clickSearchButton() {
    usersListPage().clickSearchButton();
  }

  @Step
  public void clickSaveButton() {
    userEditAddPage().clickSaveButton();
  }

  @Step
  public void clickBackToChat() {
    userEditAddPage().clickToTheListOfItems();
  }

  @Step
  public void clickLastUser() {
    scroll(usersListPage().getLastUserFromList());
    List<WebElement> usersList = usersListPage().getAllUsersHyperlinksList();

    usersList.get(usersList.size() - 1).click();
  }

  @Step
  public void searchUserByLoginName(String loginName) {
    usersListPage().typeSearchTerm(loginName);
    clickSearchButton();
  }

  @Step
  public void deleteAddedUsers() {
    scroll(usersListPage().getLastUserFromList());
    usersListPage().removeLastUser();
  }

  @Step
  public void verifyIfFoundUserByLoginName() {
    assertThat(usersListPage().getAllUsersHyperlinksList().size())
      .as("User should be found by login name")
      .isGreaterThan(0);
  }

  @Step
  public void verifyLastUserFields(String userData) {
    scroll(usersListPage().getLastUserFromList());
    String lastUser = usersListPage().getLastUserFromList().getText();

    assertThat(lastUser)
      .as("User \"%s\" has wrong data, should contain %s", lastUser, userData)
      .contains(userData);
  }

  @Step
  public void verifyIfUserWasDeleted() {
    assertThat(usersListPage().messageDeleteIsDisplayed())
      .as("Deletion message should be displayed")
      .isTrue();
  }

  @Step
  public void verifyIfFirstUserAndAdminHaveCorrectData() {
    String firstUser = usersListPage().getUser_qq().getText();
    String admin = usersListPage().getUser_admin().getText();

    assertThat(firstUser)
      .as("First user \"%s\" should have data: %s.", firstUser, getUserData())
      .contains(getUserData());

    assertThat(admin)
      .as("Second user \"%s\" should have data: %s.", admin, getAdminData())
      .contains(getAdminData());
  }

  @Step
  public String fillAllUserFields() {
    StringJoiner userInfo = new StringJoiner(" ");
    userInfo.add(fillUserField(UserField.LOGIN_NAME));
    fillUserField(UserField.PASSWORD);
    userInfo.add(fillUserField(UserField.EMAIL));
    userInfo.add(fillUserField(UserField.SURNAME));
    userInfo.add(fillUserField(UserField.FIRST_NAME));
    userInfo.add(fillUserField(UserField.MIDDLE_NAME));

    return userInfo.toString();
  }

  @Step
  public void verifyIfCanNotCreateUserWithoutLoginName() {
    String correctAction = "Create a user";
    assertThat(userEditAddPage().getActionHeader())
      .as("Action should be \" %s \", but was \" %s \"", correctAction, userEditAddPage().getActionHeader())
      .isEqualTo(correctAction);
  }

  @Step
  public void verifyIfCanNotCreateUserWithoutPassword() {
    String correctAction = "Create a user";
    assertThat(userEditAddPage().getActionHeader())
      .as("Action should be \" %s \", but was \" %s \"", correctAction, userEditAddPage().getActionHeader())
      .isEqualTo(correctAction);

    assertThat(userEditAddPage().avatarIsDisplayed()).as("Avatar should be displayed").isTrue();

    String onePart = userEditAddPage().getNoPasswordMessageFirstPart();
    String secondPart = userEditAddPage().getNoPasswordMessageSecondPart();
    String displayed = userEditAddPage().getNoPasswordDisplayedCommunicat();

    assertThat(userEditAddPage().getNoPasswordDisplayedCommunicat())
      .as("Message about no password should contains %s and %s, but contains %s",
              onePart, secondPart, displayed)
      .contains(onePart)
      .contains(secondPart);

    assertThat(userEditAddPage().tableWithErrorsIsDisplayed())
      .as("Table with errors should be displayed")
      .isTrue();
  }

  private String getUserData() {
    StringJoiner userData = new StringJoiner(" ");
    userData.add(getData().userName());
    userData.add(getData().userMail());
    userData.add(getData().userSurname());
    userData.add(getData().userFirstName());
    userData.add(getData().userMiddleName());

    return userData.toString();
  }

  private String getAdminData() {
    StringJoiner adminData = new StringJoiner(" ");
    adminData.add(getData().adminName());
    adminData.add(getData().adminMail());
    adminData.add(getData().adminSurname());
    adminData.add(getData().adminFirstName());
    adminData.add(getData().adminMiddleName());

    return adminData.toString();
  }

  UsersListPage usersListPage() {
    return getPage(UsersListPage.class);
  }

  UserEditAddPage userEditAddPage() {
    return getPage(UserEditAddPage.class);
  }

  AdminBasePage adminBasePage() {
    return getPage(AdminBasePage.class);
  }
}
