package com.edd.chat.security.token;

import com.edd.chat.user.UsernameResolver;

import java.util.Map;

public class DefaultUsernameResolver implements UsernameResolver {

    private static final String NAME = "name";

    @Override
    public String resolve(Map<String, Object> details) {
        return (String) details.getOrDefault(NAME, "N/A");
    }
}