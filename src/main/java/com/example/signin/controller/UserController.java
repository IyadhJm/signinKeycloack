package com.example.signin.controller;


import com.example.signin.config.KeycloakProvider;
import com.example.signin.httpRequest.CreateUserRequest;
import com.example.signin.httpRequest.LoginRequest;
import com.example.signin.service.KeycloakAdminClientService;
import jakarta.annotation.security.RolesAllowed;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/key")
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {
    private final KeycloakAdminClientService kcAdminClient;
    private final KeycloakProvider kcProvider;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);
    public UserController(KeycloakAdminClientService kcAdminClient, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
    }
    @PostMapping(value = "/create")

    public ResponseEntity<CreateUserRequest> createUser(@RequestBody CreateUserRequest user) {
        Response createdResponse = kcAdminClient.createKeycloakUser(user);
        return ResponseEntity.status(createdResponse.getStatus()).build();
    }

    @PostMapping("/login")

    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody  LoginRequest loginRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(), loginRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }

    }



}
