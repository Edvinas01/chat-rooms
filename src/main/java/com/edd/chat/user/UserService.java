package com.edd.chat.user;

import com.edd.chat.domain.user.AuthMethod;
import com.edd.chat.domain.user.AuthMethodRepository;
import com.edd.chat.domain.user.ChatUser;
import com.edd.chat.domain.user.ChatUserRepository;
import com.edd.chat.util.AuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final AuthMethodRepository authMethodRepository;
    private final ChatUserRepository chatUserRepository;

    @Autowired
    public UserService(AuthMethodRepository authMethodRepository,
                       ChatUserRepository chatUserRepository) {

        this.authMethodRepository = authMethodRepository;
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public ChatUser getUser() {
        long id = AuthUtils.getUser()
                .getId();

        ChatUser user = chatUserRepository.findOne(id);
        Hibernate.initialize(user.getAuthMethods());
        return user;
    }

    @Override
    @Transactional
    public ChatUser getUser(String username,
                            String identifier,
                            String type) {

        ChatUser chatUser = authMethodRepository
                .findByIdentifierAndType(identifier, type)
                .map(a -> {
                    ChatUser user = a.getChatUser();

                    if (StringUtils.isNotBlank(username)) {
                        LOGGER.debug("Updating username of user with id: {} to: {}",
                                user.getId(), username);

                        // For now, always use current authentication method username if possible.
                        user.setUsername(username);
                    }
                    return user;
                })
                // If user does not exist, create a new one.
                .orElseGet(() -> createUser(username, identifier, type));

        Hibernate.initialize(chatUser.getAuthMethods());
        return chatUser;
    }

    /**
     * Create a user when given a username, authentication method identifier and type.
     *
     * @param username   username to set when creating the user.
     * @param identifier authentication identifier which is given by the provider.
     * @param type       provider type, for example facebook.
     * @return created user.
     */
    private ChatUser createUser(String username,
                                String identifier,
                                String type) {

        ChatUser user = chatUserRepository.save(new ChatUser(username));
        user.getAuthMethods().add(authMethodRepository
                .save(new AuthMethod(user, identifier, type)));

        LOGGER.debug("Created chat user with name: {}, identifier: {}, type: {}",
                user.getUsername(), identifier, type);

        return user;
    }
}