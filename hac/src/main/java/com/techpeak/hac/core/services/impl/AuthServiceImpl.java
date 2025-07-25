package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.dtos.LoginDto;
import com.techpeak.hac.core.dtos.LoginResponse;
import com.techpeak.hac.core.dtos.RegisterDto;
import com.techpeak.hac.core.dtos.UserDto;
import com.techpeak.hac.core.exception.BadRequestException;
import com.techpeak.hac.core.models.Role;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.repositories.RoleRepository;
import com.techpeak.hac.core.repositories.UserRepository;
import com.techpeak.hac.core.security.JwtUtilities;
import com.techpeak.hac.core.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    private static UserDto getUserDto(User user) {
        return new UserDto().builder()
                .id(user.getId())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .build();
    }

    @Override
    public ResponseEntity<LoginResponse> register(RegisterDto registerDto) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerDto.getUsername()))) {
            throw new BadRequestException("username is already taken !");
        } else {
            User user = new User();
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
             userRepository.save(user);
//            String token = jwtUtilities.generateToken(registerDto.getUsername());
            UserDto userDto = getUserDto(user);
            return new ResponseEntity<>(new LoginResponse("token", "Bearer", userDto), HttpStatus.CREATED);
        }
    }

    @Override
    public LoginResponse authenticate(LoginDto loginDto) {
       Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtilities.generateToken((UserDetails) user);
        UserDto userDto = getUserDto(user);
        return new LoginResponse(token, "Bearer", userDto);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User saverUser(User user) {
        return userRepository.save(user);
    }
}
