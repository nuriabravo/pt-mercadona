package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.User;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.UserJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserJpaMapper {
    UserJpa toJpa(User user);
    User toDomain(UserJpa jpa);
}