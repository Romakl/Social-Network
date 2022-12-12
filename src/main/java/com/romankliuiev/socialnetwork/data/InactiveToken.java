package com.romankliuiev.socialnetwork.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class InactiveToken {
    @Id
    private String token;
    private OffsetDateTime expiration;
}
