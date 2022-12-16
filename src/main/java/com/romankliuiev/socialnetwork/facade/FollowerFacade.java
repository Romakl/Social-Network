package com.romankliuiev.socialnetwork.facade;

import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.dto.user.UserShortDTO;
import com.romankliuiev.socialnetwork.facade.exception.FollowYourselfException;
import com.romankliuiev.socialnetwork.facade.exception.NullTokenException;
import com.romankliuiev.socialnetwork.service.FollowerService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerFacade {
    private final FollowerService followerService;

    public FollowerFacade(FollowerService followerService) {
        this.followerService = followerService;
    }


    public ResponseEntity<UserShortDTO> followUser(String username, Authentication authentication) {
        if (authentication == null) {
            throw new NullTokenException("Token is null");
        }
        if (authentication.getName().equals(username)) {
            throw new FollowYourselfException("You can't follow yourself");
        }
        return ResponseEntity.ok(UserToUserShortDTO(followerService.followUser(username, authentication.getName())));
    }

    private UserShortDTO UserToUserShortDTO(User followUser) {
        UserShortDTO userShortDTO = new UserShortDTO();
        BeanUtils.copyProperties(followUser, userShortDTO);
        return userShortDTO;
    }

    public ResponseEntity<Page<UserShortDTO>> getFollowers(Integer page, Integer size, Authentication authentication) {
        if (authentication == null) {
            throw new NullTokenException("Token is null");
        }
        return ResponseEntity.ok(followerService.getFollowers(page, size, authentication.getName()).map(this::UserToUserShortDTO));
    }

    public ResponseEntity<Page<UserShortDTO>> getFollowing(Integer page, Integer size, Authentication authentication) {
        if (authentication == null) {
            throw new NullTokenException("Token is null");
        }
        return ResponseEntity.ok(followerService.getFollowing(page, size, authentication.getName()).map(this::UserToUserShortDTO));
    }
}
