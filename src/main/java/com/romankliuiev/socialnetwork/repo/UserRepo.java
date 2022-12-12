package com.romankliuiev.socialnetwork.repo;

import com.romankliuiev.socialnetwork.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
//    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);
//    List<User> findAll(Pageable pageable);
    List<User> findAllByBirthDate(LocalDate birthDate);
    List<User> findAllByIsActive(Boolean isActive);
}

