package com.romankliuiev.socialnetwork.data.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Role {
    @Id
    @SequenceGenerator(name="role_id_seq", sequenceName="role_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="role_id_seq")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
