package com.romankliuiev.socialnetwork.repo;

import com.romankliuiev.socialnetwork.data.Followers;
import com.romankliuiev.socialnetwork.data.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowersRepo extends JpaRepository<Followers, Long> {
    Page<Followers> findByFrom(User from, PageRequest pageRequest);
    Page<Followers> findByTo(User to, Pageable pageable);

    Boolean existsByFromAndTo(User from, User to);

    void deleteByFromAndTo(User follower, User user);


    Page<Followers> findAllByToAndIsAccepted(User user, Boolean isAccepted, Pageable pageable);

    Page<Followers> findAllByFromAndIsAccepted(User user, Boolean isAccepted, Pageable pageable);
}
