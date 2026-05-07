package ch.samira.tesan.kitcord.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-client-secret}")
    private String adminClientSecret;

    public String createUser(String username, String password) {
        String accessToken = getAdminAccessToken();

        String url = serverUrl + "/admin/realms/" + realm + "/users";

        Map<String, Object> credential = new LinkedHashMap<>();
        credential.put("type", "password");
        credential.put("value", password);
        credential.put("temporary", false);

        Map<String, Object> userBody = new LinkedHashMap<>();
        userBody.put("username", username);
        userBody.put("enabled", true);
        userBody.put("credentials", List.of(credential));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userBody, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Void.class
            );

            String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);

            if (location == null || location.isBlank()) {
                throw new RuntimeException("Keycloak user was created, but no user id was returned");
            }

            return location.substring(location.lastIndexOf("/") + 1);

        } catch (HttpStatusCodeException exception) {
            throw new IllegalArgumentException("Keycloak error: " + exception.getResponseBodyAsString());
        }
    }

    public void resetPassword(String keycloakUserId, String newPassword) {
        String accessToken = getAdminAccessToken();

        String url = serverUrl + "/admin/realms/" + realm + "/users/" + keycloakUserId + "/reset-password";

        Map<String, Object> credential = new LinkedHashMap<>();
        credential.put("type", "password");
        credential.put("value", newPassword);
        credential.put("temporary", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credential, headers);

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    Void.class
            );
        } catch (HttpStatusCodeException exception) {
            throw new IllegalArgumentException("Keycloak error: " + exception.getResponseBodyAsString());
        }
    }

    public void deleteUser(String keycloakUserId) {
        String accessToken = getAdminAccessToken();

        String url = serverUrl + "/admin/realms/" + realm + "/users/" + keycloakUserId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );
        } catch (HttpStatusCodeException exception) {
            throw new IllegalArgumentException("Keycloak error: " + exception.getResponseBodyAsString());
        }
    }

    private String getAdminAccessToken() {
        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", adminClientId);
        form.add("client_secret", adminClientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, Object> body = response.getBody();

            if (body == null || body.get("access_token") == null) {
                throw new RuntimeException("Could not get Keycloak admin access token");
            }

            return body.get("access_token").toString();

        } catch (HttpStatusCodeException exception) {
            throw new IllegalArgumentException("Could not get Keycloak admin access token: " + exception.getResponseBodyAsString());
        }
    }
}