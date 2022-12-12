package com.romankliuiev.socialnetwork.data.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_photo")
@Getter
@Setter
@RequiredArgsConstructor
public class UserPhotos {
    @Id
    @SequenceGenerator(name = "usr_photo_id_seq", sequenceName = "usr_photo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_photo_id_seq")
    private Long id;
    private String photo;
}
