package Basic;

import PayloadBuilder.PayloadBuilder;
import RequestBuilder.apiRequestBuilder;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserRegistration {

    static String adminToken;
    static String userToken;
    static String userId;
    static String registeredEmail;

    @Test(priority = 1)
    public void adminLogin() {
        String apiPath = "/APIDEV/login";
        String payload = PayloadBuilder.getAdminLoginPayload();

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getCommonSpec()) // Replaces baseUri, header, and log
                .body(payload)
                .post(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Admin Login failed");
        adminToken = response.jsonPath().getString("data.token");
    }

    @Test(priority = 2)
    public void registerNewUser() {
        String apiPath = "/APIDEV/register";
        registeredEmail = Faker.instance().internet().emailAddress();
        String payload = PayloadBuilder.getRegisterUserPayload(registeredEmail);

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getCommonSpec())
                .body(payload)
                .post(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 201, "Registration failed");
        userId = response.jsonPath().getString("data.id");
    }

    @Test(priority = 3, dependsOnMethods = {"adminLogin", "registerNewUser"})
    public void approveUserRegistration() {
        String apiPath = "/APIDEV/admin/users/" + userId + "/approve";

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getAdminSpec(adminToken)) // Includes Authorization header
                .put(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Approval failed");
    }

    @Test(priority = 4, dependsOnMethods = {"approveUserRegistration"})
    public void loginWithNewUser() {
        String apiPath = "/APIDEV/login";
        String payload = PayloadBuilder.getNewUserLoginPayload(registeredEmail);

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getCommonSpec())
                .body(payload)
                .post(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "New User Login failed");
        userToken = response.jsonPath().getString("data.token");
    }

    @Test(priority = 5, dependsOnMethods = {"adminLogin", "registerNewUser"})
    public void makeNewUserAdmin() {
        String apiPath = "/APIDEV/admin/users/" + userId + "/role";
        String payload = PayloadBuilder.getUpdateRolePayload("admin");

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getAdminSpec(adminToken))
                .body(payload)
                .put(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to promote user to admin");
    }

    @Test(priority = 6, dependsOnMethods = {"makeNewUserAdmin"})
    public void verifyAdminAccount() {
        String apiPath = "/APIDEV/admin/users/" + userId;

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getAdminSpec(adminToken))
                .get(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200);
        String actualRole = response.jsonPath().getString("data.Role");
        Assert.assertEquals(actualRole, "admin");
    }

    @Test(priority = 7, dependsOnMethods = {"verifyAdminAccount"})
    public void deleteUserAdmin() {
        String apiPath = "/APIDEV/admin/users/" + userId;

        Response response = RestAssured.given()
                .spec(apiRequestBuilder.getAdminSpec(adminToken))
                .delete(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getBoolean("success"));
    }
}