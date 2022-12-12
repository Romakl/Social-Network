package com.romankliuiev.socialnetwork.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class UserRegisterDTO {
private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}
