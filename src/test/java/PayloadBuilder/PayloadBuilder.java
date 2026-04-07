package PayloadBuilder;

public class PayloadBuilder {
    // Method for Admin Login
    public static String getAdminLoginPayload() {
        return "{\n" +
                "    \"email\": \"admin@gmail.com\",\n" +
                "    \"password\": \"@12345678\"\n" +
                "}";
    }

    // Method for Registering a New User
    public static String getRegisterUserPayload(String email) {
        return String.format("{\n" +
                "    \"firstName\": \"selena\",\n" +
                "    \"lastName\": \"Tloome\",\n" +
                "    \"email\": \"%s\",\n" +
                "    \"password\": \"B%%loved19\",\n" +
                "    \"confirmPassword\": \"B%%loved19\",\n" +
                "    \"groupId\": \"1deae17a-c67a-4bb0-bdeb-df0fc9e2e526\"\n" +
                "}", email);
    }

    // Method for New User Login
    public static String getNewUserLoginPayload(String email) {
        return String.format("{\n" +
                "    \"email\": \"%s\",\n" +
                "    \"password\": \"B%%loved19\"\n" +
                "}", email);
    }

    // Method for Updating Role
    public static String getUpdateRolePayload(String role) {
        return "{\n" +
                "    \"role\": \"" + role + "\"\n" +
                "}";
    }
}