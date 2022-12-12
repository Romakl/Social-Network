package com.romankliuiev.socialnetwork.data.likes;

import com.romankliuiev.socialnetwork.data.Comment;
import com.romankliuiev.socialnetwork.data.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "comment-like")
public class CommentLike {
    @Id
    private Long id;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private User userLike;

    private OffsetDateTime created;

    @PrePersist
    public void prePersist() {
        created = OffsetDateTime.now();
    }
}
