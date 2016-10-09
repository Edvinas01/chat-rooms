package com.edd.chat.account;

import com.edd.chat.ChatIntegrationTest;
import com.edd.chat.test.AccountFactory;
import com.edd.chat.token.TokenHandler;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ChatIntegrationTest
@RunWith(SpringRunner.class)
public class AccountControllerIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private TokenHandler tokenHandler;

    private Account account;

    @Value("${server.port}")
    private int port;

    @Before
    public void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost")
                .setBasePath("/api/v1/accounts")
                .setPort(port)
                .build();

        // Setup test account.
        account = accountFactory.register();
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    public void getProfileDetails() throws Exception {
        String token = tokenHandler.createToken(account)
                .getToken();

        //@formatter:off
        given()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(account.getId()))
            .body("role", is(account.getRole().name()))
            .body("username", is(account.getUsername()));
        //@formatter:on
    }

    @Test
    public void getProfileDetailsUnauthorized() throws Exception {
        //@formatter:off
        get()
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .body("error", isA(String.class));
        //@formatter:on
    }

    @Test
    public void authenticateSuccessfully() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", account.getUsername());
        json.put("password", AccountFactory.PASSWORD);

        //@formatter:off
        String token = given()
                .body(json.toString())
            .when()
                .post("/auth")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", isA(String.class))
                .body("expires", isA(Long.class))
            .extract()
                .path("token");
        //@formatter:on

        String username = tokenHandler
                .parse(token)
                .get()
                .getUsername();

        assertThat(username).isEqualTo(account.getUsername());
    }

    @Test
    public void registerSuccessfully() throws Exception {
        String username = "CoolGuy";

        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", AccountFactory.PASSWORD);

        //@formatter:off
        given()
            .body(json.toString())
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", isA(String.class))
            .body("username", is(username))
            .body("password", nullValue())
            .body("role", is(Account.Role.ROLE_USER.name()));
        //@formatter:on
    }

    @Test
    public void registerExistingAccount() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", account.getUsername());
        json.put("password", AccountFactory.PASSWORD);

        //@formatter:off
        given()
            .body(json.toString())
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", isA(String.class));
        //@formatter:on
    }

    @Test
    public void logoutAndGetAccount() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", account.getUsername());
        json.put("password", AccountFactory.PASSWORD);

        UUID old = account.getTokenVersion();

        //@formatter:off
        String token = given()
            .body(json.toString())
            .post("/auth")
            .path("token");

        given()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .post("/logout")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // Check if token is really invalid.
        given()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .get()
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .body("error", isA(String.class));
        //@formatter:on

        assertThat(accountRepository.findOne(account.getId()).getTokenVersion())
                .isNotEqualTo(old);
    }

    @Test
    public void logoutUnauthorized() throws Exception {
        //@formatter:off
        given()
            .post("/logout")
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .body("error", isA(String.class));
        //@formatter:on
    }
}