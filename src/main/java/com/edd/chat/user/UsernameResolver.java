package com.edd.chat.user;


import java.util.Map;

/**
 * Resolves user names from details objects.
 */
public interface UsernameResolver {

    /**
     * Resolve username from details map.
     *
     * @param details details map.
     * @return resolved username.
     */
    String resolve(Map<String, Object> details);
}