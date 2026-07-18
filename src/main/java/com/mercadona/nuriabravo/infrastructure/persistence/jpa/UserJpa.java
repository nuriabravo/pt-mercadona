package com.mercadona.nuriabravo.infrastructure.persistence.jpa;

import java.time.Instant;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.InstantConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name= "password", nullable = false)
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "token_expiration")
    @Convert(converter = InstantConverter.class)
    private Instant tokenExpiration;
}
