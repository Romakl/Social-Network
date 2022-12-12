package com.romankliuiev.socialnetwork.dto.user;

import lombok.Data;

@Data
public class UserPublicDTO {
    private Long id;
    private String username;
    private String avatar;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private String role;
}
