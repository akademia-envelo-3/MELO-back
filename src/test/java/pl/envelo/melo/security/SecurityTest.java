package pl.envelo.melo.security;

import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.envelo.melo.authorization.AuthFailed;
import pl.envelo.melo.authorization.AuthStatus;
import pl.envelo.melo.authorization.AuthSucceded;
import pl.envelo.melo.authorization.AuthorizationService;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityTest {
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    MockMvc mockMvc;
    @Value("${test.fetch-token-url}")
    String fetchTokenUrl;
    @Value("${melo.employee-role}")
    private String employeeRole;
    @Value("${melo.client-id}")
    private String clientId;
    @Value("${test.client-secret}")
    private String clientSecret;
    @Value("${melo.admin-role}")
    private String adminRole;
    @Value("${test.admin.name}")
    private String adminLogin;
    @Value("${test.employee.name}")
    private String employeeLogin;
    @Value("${test.superadmin.name}")
    private String superAdminLogin;
    @Value("${test.no-roles-user.name}")
    private String noRolesUserLogin;
    @Value("${test.admin.password}")
    private String adminPassword;
    @Value("${test.employee.password}")
    private String employeePassword;
    @Value("${test.superadmin.password}")
    private String superAdminPassword;
    @Value("${test.no-roles-user.password}")
    private String noRolesUserPassword;

    @Test
    void rolesTest() throws Exception {
        mockMvc.perform(get("/test/security").header("Authorization", "Bearer " + getToken(superAdminLogin,superAdminPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$[0].authority", is(employeeRole)))
                .andExpect(jsonPath("$[1].authority", is(adminRole)));
        mockMvc.perform(get("/test/security").header("Authorization", "Bearer " + getToken(adminLogin,adminPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$[0].authority", is(adminRole)));
        mockMvc.perform(get("/test/security").header("Authorization", "Bearer " + getToken(employeeLogin,employeePassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$[0].authority", is(employeeRole)));
        mockMvc.perform(get("/test/security").header("Authorization", "Bearer " + getToken(noRolesUserLogin,noRolesUserPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$[0].authority").doesNotExist());
    }
    @Transactional
    @Test
    void userTest() throws Exception{
        mockMvc.perform(get("/test/security/auth").header("Authorization", "Bearer " + getToken(superAdminLogin,superAdminPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$", is(AuthSucceded.ADMIN_AND_EMPLOYEE_CREATED.name())));
        mockMvc.perform(get("/test/security/auth").header("Authorization", "Bearer " + getToken(superAdminLogin,superAdminPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$", is(AuthSucceded.USER_EXISTS.name())));
        mockMvc.perform(get("/test/security/auth").header("Authorization", "Bearer " + getToken(adminLogin,adminPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$", is(AuthSucceded.ADMIN_CREATED.name())));
        mockMvc.perform(get("/test/security/auth").header("Authorization", "Bearer " + getToken(employeeLogin,employeePassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$", is(AuthSucceded.EMPLOYEE_CREATED.name())));
        mockMvc.perform(get("/test/security/auth").header("Authorization", "Bearer " + getToken(noRolesUserLogin,noRolesUserPassword))
                ).andExpect(status().is(200))
                .andExpect(jsonPath("$", is(AuthFailed.PRINCIPAL_NOT_ALLOWED.name())));
    }
    private String getToken(String username, String password){
        HttpClient httpClient = HttpClient.newHttpClient();
        String params = Map.of(
                        "client_id", clientId,
                        "username", username,
                        "password", password,
                        "grant_type", "password",
                        "client_secret", clientSecret).entrySet()
                .stream()
                .map(entry -> String.join("=",
                        URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8),
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                ).collect(Collectors.joining("&"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fetchTokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .build();
        try {
            HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return (new JSONObject(response.body().toString())).getString("access_token");
        }catch (IOException | InterruptedException | JSONException e){
            return "";
        }
    }
}
