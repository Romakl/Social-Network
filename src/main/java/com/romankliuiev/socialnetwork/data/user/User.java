package com.romankliuiev.socialnetwork.data.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "usr_id_seq", sequenceName = "usr_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_id_seq")
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    private String username;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(length = 64)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(targetClass = RoleName.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "person_role", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    private Set<RoleName> roles;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Boolean isBanned;

    @Column(nullable = false)
    private Boolean isPrivate;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    private OffsetDateTime updated;

    @PrePersist
    public void prePersist() {
        isActive = false;
        isDeleted = false;
        isBanned = false;
        isPrivate = false;
        OffsetDateTime now = OffsetDateTime.now();
        created = now;
        updated = now;
    }

    @PreUpdate
    public void preUpdate() {
        updated = OffsetDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
