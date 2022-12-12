package com.romankliuiev.socialnetwork.service;

import com.romankliuiev.socialnetwork.facade.exception.UserAlreadyExists;
import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.repo.UserRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "user")
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        Optional<User> existingEmail = userRepo.findByEmail(user.getEmail());
        Optional<User> existingUsername = userRepo.findByUsername(user.getUsername());
        if (existingEmail.isPresent() || existingUsername.isPresent()) {
            throw new UserAlreadyExists(user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User getUserByUsername(String subject) {
        return userRepo.findByUsername(subject).orElseThrow(() -> new UsernameNotFoundException(subject));
    }
}
