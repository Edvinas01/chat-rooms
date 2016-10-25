package com.edd.chat.admin.account;

import com.edd.chat.ChatIntegrationTest;
import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
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

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

@ChatIntegrationTest
@RunWith(SpringRunner.class)
public class AccountManagementControllerIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private TokenHandler tokenHandler;

    @Value("${server.port}")
    private int port;

    private Account account;

    @Before
    public void setUp() {
        account = accountFactory.registerAdmin();

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenHandler.createToken(account).getToken())
                .setBaseUri("http://localhost")
                .setBasePath("/api/v1/admin/accounts")
                .setPort(port)
                .build();
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    public void getAccounts() {
        //@formatter:off
        get()
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", is(account.getId()))
            .body("[0].role", is(account.getRole().name()))
            .body("[0].enabled", is(account.isEnabled()))
            .body("[0].username", is(account.getUsername()));
        //@formatter:on
    }

    @Test
    public void getAccount() {

        // Create other user account to see if we can get it.
        Account other = create();

        //@formatter:off
        get("/{id}", other.getId())
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(other.getId()))
            .body("role", is(other.getRole().name()))
            .body("enabled", is(other.isEnabled()))
            .body("username", is(other.getUsername()));
        //@formatter:on
    }

    @Test
    public void updateAccount() {
        Account other = create();

        JSONObject json = new JSONObject();
        json.put("enabled", true);

        //@formatter:off
        given()
            .body(json.toString())
        .when()
            .post("/{id}", other.getId())
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(other.getId()))
            .body("role", is(other.getRole().name()))
            .body("enabled", is(true))
            .body("username", is(other.getUsername()));
        //@formatter:on
    }

    /**
     * Creates "other" disabled test user account.
     */
    private Account create() {
        Account other = AccountFactory
                .create("other")
                .password(AccountFactory.PASSWORD)
                .build();

        return accountRepository.save(other);
    }
}