package com.romankliuiev.socialnetwork.data.content;

import com.romankliuiev.socialnetwork.data.Comment;
import com.romankliuiev.socialnetwork.data.Conversation;
import com.romankliuiev.socialnetwork.data.Message;
import com.romankliuiev.socialnetwork.data.Post;
import com.romankliuiev.socialnetwork.data.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "media_content")
public class MediaContent {
    @Id
    @SequenceGenerator(name = "media_content_id_seq", sequenceName = "media_content_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_content_id_seq")
    private Long id;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private LocalDateTime uploadDate;
    @Column(nullable = false)
    private MediaContentType type;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Conversation chat;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @PrePersist
    public void prePersist() {
        uploadDate = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        uploadDate = LocalDateTime.now();
    }
}
