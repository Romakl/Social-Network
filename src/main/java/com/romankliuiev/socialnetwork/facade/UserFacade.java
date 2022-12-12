package com.romankliuiev.socialnetwork.facade;

import com.romankliuiev.socialnetwork.dto.user.UserRegisterDTO;
import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }


    public ResponseEntity<UserRegisterDTO> createUser(UserRegisterDTO userRegisterDTO) {
        User user = UserRegisterToUser(userRegisterDTO);
        return ResponseEntity.ok(UserToUserRegisterDTO(userService.createUser(user)));
    }

    private UserRegisterDTO UserToUserRegisterDTO(User user) {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        BeanUtils.copyProperties(user, userRegisterDTO);
        return userRegisterDTO;
    }

    private User UserRegisterToUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        return user;
    }

    public ResponseEntity<UserRegisterDTO> getMyUser(Authentication authentication) {
        return ResponseEntity.ok(UserToUserRegisterDTO((User) authentication.getPrincipal()));
    }
}
