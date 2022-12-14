package com.romankliuiev.socialnetwork.web.user;

import com.romankliuiev.socialnetwork.dto.user.UserRegisterDTO;
import com.romankliuiev.socialnetwork.dto.user.UserShortDTO;
import com.romankliuiev.socialnetwork.facade.UserFacade;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/followers")
    public ResponseEntity<List<UserShortDTO>> getFollowers(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Authentication authentication) {
        return userFacade.getFollowers(page, size, authentication);
    }
//    @GetMapping("/following")
//    public ResponseEntity<List<UserShortDTO>> getFollowing(
//            @RequestParam(required = false, defaultValue = "1") Integer page,
//            @RequestParam(required = false, defaultValue = "10") Integer size,
//            Authentication authentication) {
//        return userFacade.getFollowing(page, size, authentication);
//    }
//    @PatchMapping("/follow")
//    public ResponseEntity<UserRegisterDTO> followUser(@RequestParam Long userId, Authentication authentication) {
//        return userFacade.followUser(userId, authentication);
//    }
//
//    @GetMapping("/search-by-username")
//    public ResponseEntity<List<UserShortDTO>> searchUsers(
//            @RequestParam(required = false, defaultValue = "1") Integer page,
//            @RequestParam(required = false, defaultValue = "10") Integer size,
//            @RequestParam String username,
//            Authentication authentication) {
//        return userFacade.searchUsers(page, size, username, authentication);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteUser(Authentication authentication) {
//        return userFacade.deleteUser(authentication);
//    }


}
