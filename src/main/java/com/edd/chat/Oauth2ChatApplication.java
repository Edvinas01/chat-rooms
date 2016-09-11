package com.edd.chat;

import com.edd.chat.domain.user.ChatUser;
import com.edd.chat.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableOAuth2Client
@SpringBootApplication
public class Oauth2ChatApplication {

    private final UserManager userManager;

    @Autowired
    public Oauth2ChatApplication(UserManager userManager) {
        this.userManager = userManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ChatApplication.class, args);
    }

    @RequestMapping("/user")
    public ChatUser user() {
        return userManager.getUser();
    }
}