package RequestBuilder;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static common.Baseurl.baseURL;

public class apiRequestBuilder {
    // Common setup for public endpoints (Login, Register)
    public static RequestSpecification getCommonSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .setContentType("application/json")
                .build()
                .log().all();
    }

    // Setup for Admin endpoints (Requires the Bearer Token)
    public static RequestSpecification getAdminSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .setContentType("application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build()
                .log().all();
    }
}
