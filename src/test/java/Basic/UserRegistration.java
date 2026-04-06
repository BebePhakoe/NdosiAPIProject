package Basic;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static common.Baseurl.baseURL;

public class UserRegistration {

    // --- Static Variables (Class Level) ---
    static String adminToken;      // Used to perform admin actions
    static String userToken;       // The token for the newly registered user
    static String userId;          // The ID of the newly registered user
    static String registeredEmail;

    @Test(priority = 1)
    public void adminLogin() {
        String apiPath = "/APIDEV/login";
        String payload = "{\n" +
                "    \"email\": \"admin@gmail.com\",\n" +
                "    \"password\": \"@12345678\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .body(payload)
                .log().all()
                .post(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Admin Login failed");

        // Save to adminToken so other tests can use it for Authorization
        adminToken = response.jsonPath().getString("data.token");
        System.out.println("Saved Admin Auth Token: " + adminToken);
    }

    @Test(priority = 2)
    public void registerNewUser() {
        String apiPath = "/APIDEV/register";
        registeredEmail = Faker.instance().internet().emailAddress();

        // Note: Using %% to escape the % in your password for String.format
        String payload = String.format("{\n" +
                "    \"firstName\": \"selena\",\n" +
                "    \"lastName\": \"Tloome\",\n" +
                "    \"email\": \"%s\",\n" +
                "    \"password\": \"B%%loved19\",\n" +
                "    \"confirmPassword\": \"B%%loved19\",\n" +
                "    \"groupId\": \"1deae17a-c67a-4bb0-bdeb-df0fc9e2e526\"\n" +
                "}", registeredEmail);

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .body(payload)
                .log().all()
                .post(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 201, "Registration failed");

        userId = response.jsonPath().getString("data.id");
        System.out.println("Captured Registered User ID: " + userId);
    }

    @Test(priority = 3, dependsOnMethods = {"adminLogin", "registerNewUser"})
    public void approveUserRegistration() {
        // Uses the userId and adminToken captured above
        String apiPath = "/APIDEV/admin/users/" + userId + "/approve";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .log().all()
                .put(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Approval failed");
    }

    @Test(priority = 4, dependsOnMethods = {"approveUserRegistration"})
    public void loginWithNewUser() {
        String apiPath = "/APIDEV/login";

        String payload = String.format("{\n" +
                "    \"email\": \"%s\",\n" +
                "    \"password\": \"B%%loved19\"\n" +
                "}", registeredEmail);

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .body(payload)
                .log().all()
                .post(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "New User Login failed");
        userToken = response.jsonPath().getString("data.token");
    }

    @Test(priority = 5, dependsOnMethods = {"adminLogin", "registerNewUser"})
    public void makeNewUserAdmin() {
        // Corrected path using 'userId' and 'adminToken'
        String apiPath = "/APIDEV/admin/users/" + userId + "/role";

        String payload = "{\n" +
                "    \"role\": \"admin\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(payload)
                .log().all()
                .put(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to promote user to admin");
        System.out.println("User " + userId + " is now an Admin.");
    }

    @Test(priority = 6, dependsOnMethods = {"makeNewUserAdmin"})
    public void verifyAdminAccount() {
        String apiPath = "/APIDEV/admin/users/" + userId;

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .log().all()
                .get(apiPath).prettyPeek();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // FIX: Changed "role" to "Role" to match the API response casing
        String actualRole = response.jsonPath().getString("data.Role");

        Assert.assertEquals(actualRole, "admin", "User role should be 'admin'");

        System.out.println("Verification Successful: User " + userId + " has Role: " + actualRole);
    }

    @Test(priority = 7, dependsOnMethods = {"verifyAdminAccount"})
    public void deleteUserAdmin() {
        // 1. Define the path using the captured userId
        String apiPath = "/APIDEV/admin/users/" + userId;

        // 2. The DELETE Request
        // We use the adminToken because deleting users usually requires admin rights
        Response response = RestAssured.given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .log().all()
                .delete(apiPath).prettyPeek();

        // 3. Validation (Matches your Postman Script)
        // pm.test("User deleted successfully")
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // pm.expect(response.success).to.be.true
        boolean isSuccess = response.jsonPath().getBoolean("success");
        Assert.assertTrue(isSuccess, "Response success field should be true");

        System.out.println("Cleanup Successful: User " + userId + " has been deleted.");
    }
}


