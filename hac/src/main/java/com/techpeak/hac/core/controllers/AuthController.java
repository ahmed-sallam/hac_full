package com.techpeak.hac.core.controllers;


import com.techpeak.hac.core.dtos.LoginDto;
import com.techpeak.hac.core.dtos.LoginResponse;
import com.techpeak.hac.core.dtos.RegisterDto;
import com.techpeak.hac.core.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDto loginDto) {
        return authService.authenticate(loginDto);
    }
}
