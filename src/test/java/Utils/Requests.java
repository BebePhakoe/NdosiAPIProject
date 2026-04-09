package Utils;

import common.Baseurl; // Connects to your Baseurl class
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Requests {

    // 1. Setup common settings for every request
    public static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(Baseurl.baseURL) // Uses your specific URL: https://ndosiautomation.co.za
                .contentType("application/json")
                .log().all(); // Logs the request details to your IntelliJ console
    }

    // 2. Generic POST method
    public static Response post(String endpoint, String body) {
        Response response = getRequestSpec()
                .body(body) // This is where your PayloadBuilder strings go
                .when()
                .post(endpoint)
                .then()
                .log().body() // Prints the server's response automatically
                .extract()
                .response();

        return response;
    }

    // 3. Generic GET method
    public static Response get(String endpoint) {
        return getRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .log().body()
                .extract()
                .response();
    }

    // 4. Generic PUT method (For your Role Updates)
    public static Response put(String endpoint, String body) {
        return getRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .log().body()
                .extract()
                .response();
    }
}
