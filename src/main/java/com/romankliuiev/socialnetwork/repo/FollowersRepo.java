package com.romankliuiev.socialnetwork.repo;

import com.romankliuiev.socialnetwork.data.Followers;
import com.romankliuiev.socialnetwork.data.user.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowersRepo extends JpaRepository<Followers, Long> {
    List<Followers> findByFrom(User from, PageRequest pageRequest);
    List<Followers> findByTo(User to, PageRequest pageRequest);
}
