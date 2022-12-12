package com.romankliuiev.socialnetwork.data;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    private Long id;
    private String text;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        created = now;
        updated = now;
    }
    @PreUpdate
    public void preUpdate() {
        updated = OffsetDateTime.now();
    }
}
