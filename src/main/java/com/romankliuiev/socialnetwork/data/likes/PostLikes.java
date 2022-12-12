package com.romankliuiev.socialnetwork.data.likes;

import com.romankliuiev.socialnetwork.data.Post;
import com.romankliuiev.socialnetwork.data.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "post-like")
public class PostLikes {
    @Id
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User userLike;
    private OffsetDateTime created;

    @PrePersist
    public void prePersist() {
        created = OffsetDateTime.now();
    }
}
