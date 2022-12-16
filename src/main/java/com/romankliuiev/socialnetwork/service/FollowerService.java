package com.romankliuiev.socialnetwork.service;

import com.romankliuiev.socialnetwork.data.Followers;
import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.repo.FollowersRepo;
import com.romankliuiev.socialnetwork.repo.UserRepo;
import org.springframework.beans.PropertyValues;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "follower")

public class FollowerService {
    private final FollowersRepo followersRepo;
    private final UserRepo userRepo;

    public FollowerService(FollowersRepo followersRepo, UserRepo userRepo) {
        this.followersRepo = followersRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public User followUser(String followTo, String followFrom) {
        User user = userRepo.findByUsername(followTo).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        User follower = userRepo.findByUsername(followFrom).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        Followers followers = new Followers();
        followers.setFrom(follower);
        followers.setTo(user);
        if (followersRepo.existsByFromAndTo(follower, user)) {
            followersRepo.deleteByFromAndTo(follower, user);
        } else {
            followersRepo.save(followers);
        }

        return user;
    }


    public Page<User> getFollowers(Integer page, Integer size, String name) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        User user = userRepo.findByUsername(name).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        return followersRepo.findAllByToAndIsAccepted(user, true, pageable).map(Followers::getFrom);
    }

    public Page<User> getFollowing(Integer page, Integer size, String name) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        User user = userRepo.findByUsername(name).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        return followersRepo.findAllByFromAndIsAccepted(user, true, pageable).map(Followers::getTo);
    }
}
