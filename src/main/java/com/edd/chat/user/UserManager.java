package com.edd.chat.user;

import com.edd.chat.domain.user.ChatUser;

/**
 * Registers and allows to lookup users.
 */
public interface UserManager {

    /**
     * Get currently authenticated user details.
     *
     * @return currently authenticated user.
     */
    ChatUser getUser();

    /**
     * Get a user using authentication identifier and type or create a new one if it does not exist.
     *
     * @param username   username to set upon creation.
     * @param identifier authentication identifier.
     * @param type       authentication type.
     * @return user object, never null.
     */
    ChatUser getUser(String username,
                     String identifier,
                     String type);
}