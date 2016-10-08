package com.edd.chat.security;

import com.edd.chat.account.Account;
import com.edd.chat.exception.ChatException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtils {

    /**
     * Get currently authenticated accounts internal username.
     *
     * @return account id.
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ChatException("Must be authenticated to get account username", HttpStatus.UNAUTHORIZED);
        }
        return ((Account) authentication.getPrincipal())
                .getInternalUsername();
    }
}