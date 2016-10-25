package com.edd.chat.admin.account;

import com.edd.chat.account.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/admin/accounts")
public class AccountManagementController {

    private final AccountManagementService managementService;

    @Autowired
    public AccountManagementController(AccountManagementService managementService) {
        this.managementService = managementService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AccountModel> getAccounts() {
        return managementService.getAccounts()
                .stream()
                .map(AccountModel::create)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AccountModel getAccount(@PathVariable String id) {
        return AccountModel.create(managementService.getAccount(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public AccountModel update(@PathVariable String id,
                               @RequestBody AccountManagementDetails details) {

        return AccountModel.create(managementService
                .updateAccount(id, details));
    }
}