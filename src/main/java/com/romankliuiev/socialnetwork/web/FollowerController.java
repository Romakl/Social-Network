package com.romankliuiev.socialnetwork.web;

import com.romankliuiev.socialnetwork.dto.user.UserShortDTO;
import com.romankliuiev.socialnetwork.facade.FollowerFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;


@RestController
@RequestMapping("/api/v1/followers")
public class FollowerController {

    private final FollowerFacade followerFacade;

    public FollowerController(FollowerFacade followerFacade) {
        this.followerFacade = followerFacade;
    }

    @PostMapping("/follow")
    public ResponseEntity<UserShortDTO> followUser(@RequestParam String username, Authentication authentication) {
        return followerFacade.followUser(username, authentication);
    }

    @GetMapping("/my-followers")
    public ResponseEntity<Page<UserShortDTO>> getFollowers(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size, Authentication authentication) {
        return followerFacade.getFollowers(page, size, authentication);
    }
    @GetMapping("/my-following")
    public ResponseEntity<Page<UserShortDTO>> getFollowing(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Authentication authentication) {
        return followerFacade.getFollowing(page, size, authentication);
    }

}
