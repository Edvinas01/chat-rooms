package com.edd.chat.domain.user;

import com.edd.chat.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthMethodRepository extends BaseRepository<AuthMethod> {

    @Query("SELECT a FROM AuthMethod a WHERE " +
            "a.identifier = ?1 AND " +
            "a.type = ?2")
    Optional<AuthMethod> findByIdentifierAndType(String identifier, String type);
}