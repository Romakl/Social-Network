package com.romankliuiev.socialnetwork.service;

import com.romankliuiev.socialnetwork.data.user.RoleName;
import com.romankliuiev.socialnetwork.facade.exception.UserAlreadyExists;
import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.repo.UserRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        user.setRoles(Collections.singleton(RoleName.ROLE_USER));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User getUserByUsername(String subject) {
        return userRepo.findByUsername(subject).orElseThrow(() -> new UsernameNotFoundException(subject));
    }


    public User updateUser(User newUserDetails, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        // existsByUsernameOrEmail
        if (userRepo.existsByUsername(newUserDetails.getUsername()) && !user.getUsername().equals(newUserDetails.getUsername())) {
            throw new UserAlreadyExists(newUserDetails.getUsername());
        }
        if (userRepo.existsByEmail(newUserDetails.getEmail()) && !user.getEmail().equals(newUserDetails.getEmail())) {
            throw new UserAlreadyExists(newUserDetails.getEmail());
        }
        newUserDetails.setPassword(passwordEncoder.encode(newUserDetails.getPassword()));
        BeanUtils.copyProperties(newUserDetails, user, getNullPropertyNames(newUserDetails));
        return userRepo.save(user);
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
