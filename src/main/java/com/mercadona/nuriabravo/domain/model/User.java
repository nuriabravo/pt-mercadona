package com.mercadona.nuriabravo.domain.model;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String token;
    private Instant tokenExpiration;
}