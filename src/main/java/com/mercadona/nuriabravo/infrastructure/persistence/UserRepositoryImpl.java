package com.mercadona.nuriabravo.infrastructure.persistence;

import java.util.Optional;

import com.mercadona.nuriabravo.domain.model.User;
import com.mercadona.nuriabravo.domain.repository.UserRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.UserJpa;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.UserJpaMapper;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpa;
    private final UserJpaMapper mapper;

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpa entity = mapper.toJpa(user);
        UserJpa savedEntity = jpa.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByToken(String token) {
        return jpa.findByToken(token)
                .map(mapper::toDomain);
    }

}
