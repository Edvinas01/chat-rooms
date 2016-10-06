package com.edd.chat.account;

import com.edd.chat.token.Credentials;
import com.edd.chat.token.TokenService;
import com.edd.chat.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TokenService tokenService;

    @Autowired
    public AccountController(AccountService accountService,
                             TokenService tokenService) {

        this.accountService = accountService;
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public AccountModel profile() {
        return AccountModel.create(accountService.getAccount());
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Token authenticate(@RequestBody Credentials details) {
        return tokenService.createToken(details);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public AccountModel register(@RequestBody AccountModel details) {
        return AccountModel.create(accountService
                .register(details.toAccount()));
    }
}