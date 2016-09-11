package com.edd.chat.util;

import com.edd.chat.domain.user.ChatUser;
import com.edd.chat.util.exception.InternalServerErrorException;
import com.edd.chat.util.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtils {

    /**
     * Get currently authenticated user details.
     *
     * @return currently authenticated user details.
     * @throws UnauthorizedException        if authentication is null.
     * @throws InternalServerErrorException if principal type is invalid.
     */
    public static ChatUser getUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("Must be authenticated to make this call");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof ChatUser) {
            return ((ChatUser) principal);
        } else {
            throw new InternalServerErrorException("Authentication principal type is invalid: %s",
                    principal.getClass());
        }
    }
}