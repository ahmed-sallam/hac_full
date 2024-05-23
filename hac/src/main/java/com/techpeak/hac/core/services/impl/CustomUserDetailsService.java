package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("uuuuuser " + username);
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found !"));
        return user;
    }
}
