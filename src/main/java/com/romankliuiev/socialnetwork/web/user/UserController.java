package com.romankliuiev.socialnetwork.web.user;

import com.romankliuiev.socialnetwork.dto.user.UserRegisterDTO;
import com.romankliuiev.socialnetwork.facade.UserFacade;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }


    @PostMapping
    public ResponseEntity<UserRegisterDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userFacade.createUser(userRegisterDTO);
    }

    @GetMapping
    public ResponseEntity<UserRegisterDTO> getMyUser(Authentication authentication) {
        return userFacade.getMyUser(authentication);
    }

    @PutMapping
    public ResponseEntity<UserRegisterDTO> updateUser(@RequestBody UserRegisterDTO userRegisterDTO, Authentication authentication) {
        return userFacade.updateUser(userRegisterDTO, authentication);
    }

}
