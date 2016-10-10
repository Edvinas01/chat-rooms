package com.edd.chat.channel;

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
import static org.hamcrest.Matchers.isA;

@ChatIntegrationTest
@RunWith(SpringRunner.class)
public class ChannelControllerIntegrationTest {

    private static final String TEST_MESSAGE = "test";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private TokenHandler tokenHandler;

    @Value("${chat.channels.main}")
    private String mainChannel;

    @Value("${server.port}")
    private int port;

    private Account account;
    private Channel channel;

    @Before
    public void setUp() {

        // Re-init main channel after clean-up.
        channel = channelService.init();
        account = accountFactory.register();

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenHandler.createToken(account).getToken())
                .setBaseUri("http://localhost")
                .setBasePath("/api/v1/channels")
                .setPort(port)
                .build();
    }

    @After
    public void tearDown() {
        channelRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void getChannels() {
        //@formatter:off
        get()
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", is(channel.getId()))
            .body("[0].name", is(channel.getName()))
            .body("[0].commentCount", is(channel.getComments().size()));
        //@formatter:on
    }

    @Test
    public void comment() {
        JSONObject json = new JSONObject();
        json.put("message", TEST_MESSAGE);

        //@formatter:off
        given()
            .body(json.toString())
        .when()
            .post("/{id}/comments", channel.getId())
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("message", is(TEST_MESSAGE))
            .body("sentOn", isA(Long.class))
            .body("username", is(account.getUsername()));
        //@formatter:on
    }

    @Test
    public void getComments() {
        JSONObject json = new JSONObject();
        json.put("message", TEST_MESSAGE);

        int count = 1;

        //@formatter:off
        given()
            .body(json.toString())
            .post("/{id}/comments", channel.getId());

        given()
            .param("count", count)
            .get("/{id}/comments", channel.getId())
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].message", is(TEST_MESSAGE))
            .body("[0].username", is(account.getUsername()))
            .body("[0].sentOn", isA(Long.class));
        //@formatter:on
    }
}