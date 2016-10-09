package com.edd.chat.account;

import com.edd.chat.token.Credentials;
import com.edd.chat.token.TokenService;
import com.edd.chat.token.TokenDetails;
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

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public TokenDetails authenticate(@RequestBody Credentials details) {
        return tokenService.createToken(details);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
        accountService.logout();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public AccountModel register(@RequestBody AccountModel details) {
        return AccountModel.create(accountService
                .register(details.toAccount()));
    }
}