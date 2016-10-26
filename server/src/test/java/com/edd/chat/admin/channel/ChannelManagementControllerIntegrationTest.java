package com.edd.chat.admin.channel;

import com.edd.chat.ChatIntegrationTest;
import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
import com.edd.chat.channel.Channel;
import com.edd.chat.channel.ChannelRepository;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.is;

@ChatIntegrationTest
@RunWith(SpringRunner.class)
public class ChannelManagementControllerIntegrationTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private TokenHandler tokenHandler;

    @Value("${chat.channels.main}")
    private String mainChannelName;

    @Value("${server.port}")
    private int port;

    private Channel channel;

    @Before
    public void setUp() {
        Account account = accountFactory.registerAdmin();
        channel = channelRepository.save(new Channel(mainChannelName + "other"));

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenHandler.createToken(account).getToken())
                .setBaseUri("http://localhost")
                .setBasePath("/api/v1/admin/channels")
                .setPort(port)
                .build();
    }

    @After
    public void tearDown() {
        channelRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void createChannel() {
        String name = channel.getName() + "new";

        JSONObject json = new JSONObject();
        json.put("name", name);

        //@formatter:off
        given()
            .body(json.toString())
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", isA(String.class))
            .body("name", is(name))
            .body("commentCount", is(0));
        //@formatter:on
    }

    @Test
    public void deleteChannel() {
        //@formatter:off
        when()
            .delete("/{id}", channel.getId())
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on

        assertThat(channelRepository.findOne(channel.getId()))
                .isNull();
    }
}