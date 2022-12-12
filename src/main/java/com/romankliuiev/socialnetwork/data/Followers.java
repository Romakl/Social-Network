package com.romankliuiev.socialnetwork.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.romankliuiev.socialnetwork.data.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "followers")
public class Followers {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="from_user_fk")
    private User from;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="to_user_fk")
    private User to;
    @Column(name = "is_accepted")
    private Boolean isAccepted;
}
