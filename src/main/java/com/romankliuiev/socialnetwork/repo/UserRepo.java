package com.romankliuiev.socialnetwork.repo;

import com.romankliuiev.socialnetwork.data.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByBirthDate(LocalDate birthDate);
    List<User> findAllByIsActive(Boolean isActive);
    Boolean existsByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Page<User> findByUsernameContainingIgnoreCaseOrderByUsernameAsc(String username, Pageable pageable);

    Page<User> findAllByFollowersTo(User user, Pageable pageable);

    Page<User> findAllByFollowersFrom(User user, Pageable pageable);

}

