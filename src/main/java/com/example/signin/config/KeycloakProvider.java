package com.example.signin.config;


import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Data;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KeycloakProvider {
    @Value("${keycloak.server-url}")
    public String SERVER_URL;

    @Value("${keycloak.realm}")
    public String REALM;

    @Value("${keycloak.ressource}")
    public String CLIENT_ID;

    @Value("${keycloak.username}")
    public String USERNAME;

    @Value("${keycloak.password}")
    public String PASSWORD;

    @Value("${keycloak.credentials.secret}")
    public String SECRET;

    private static Keycloak keycloak = null;
    @Bean
    public AdapterConfig adapterConfig() {
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setRealm(REALM);
        adapterConfig.setResource(CLIENT_ID);
        adapterConfig.setAuthServerUrl(SERVER_URL );
        adapterConfig.setSslRequired("external");
        adapterConfig.setConfidentialPort(0);

        return adapterConfig;
    }
    @Bean
    public KeycloakDeployment keycloakDeployment(AdapterConfig adapterConfig) {
        return KeycloakDeploymentBuilder.build(adapterConfig);
    }

    public KeycloakProvider() {
    }

    public Keycloak getInstance() {
        if (keycloak == null) {

            return KeycloakBuilder.builder()
                    .realm(REALM)
                    .serverUrl(SERVER_URL)
                    .clientId(CLIENT_ID)
                    .clientSecret(SECRET)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();
        }
        return keycloak;
    }


    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder()
                .realm(REALM)
                .serverUrl(SERVER_URL)
                .clientId(CLIENT_ID)
                .clientSecret(SECRET)
                .username(USERNAME)
                .password(PASSWORD);
    }

  public JsonNode refreshToken(String refreshToken) throws UnirestException {
        String url = SERVER_URL + "/realms/" + REALM + "/protocol/openid-connect/token";
        return Unirest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", CLIENT_ID)
                .field("client_secret", SECRET)
                .field("refresh_token", refreshToken)
                .field("grant_type", "refresh_token")
                .asJson().getBody();
    }
}

