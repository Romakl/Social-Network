package com.romankliuiev.socialnetwork.facade;

import com.romankliuiev.socialnetwork.dto.user.UserRegisterDTO;
import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.dto.user.UserShortDTO;
import com.romankliuiev.socialnetwork.facade.exception.NullTokenException;
import com.romankliuiev.socialnetwork.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public ResponseEntity<UserRegisterDTO> updateUser(UserRegisterDTO userRegisterDTO, Authentication authentication) {
        if (authentication == null) {
            throw new NullTokenException("Token is null");
        }
        User user = UserRegisterToUser(userRegisterDTO);
        return ResponseEntity.ok(UserToUserRegisterDTO(userService.updateUser(user, authentication.getName())));
    }

    public ResponseEntity<List<UserShortDTO>> getFollowers(Integer page, Integer size, Authentication authentication) {
        return ResponseEntity.ok(userService.getFollowers(page, size, authentication.getName()));
    }
}
