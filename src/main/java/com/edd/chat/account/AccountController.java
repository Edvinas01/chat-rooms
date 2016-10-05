package com.edd.chat.account;

import com.edd.chat.domain.account.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public AccountModel profile() {
        return AccountModel.create(accountService.getAccount());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public AccountModel register(@RequestBody AccountModel details) {
        return AccountModel.create(accountService
                .register(details.toAccount()));
    }
}