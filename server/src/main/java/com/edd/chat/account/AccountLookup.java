package com.edd.chat.account;

import java.util.List;

/**
 * Looks up accounts.
 */
public interface AccountLookup {

    /**
     * Get list of registered account.
     *
     * @return list of registered accounts.
     */
    List<Account> getAccounts();

    /**
     * Get account by id by. No authentication is required to make this call.
     *
     * @param id account id.
     * @return account.
     */
    Account getAccount(String id);
}