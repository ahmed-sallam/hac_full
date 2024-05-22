package com.techpeak.hac.core.services;

import com.techpeak.hac.core.dtos.BearerToken;
import com.techpeak.hac.core.dtos.LoginDto;
import com.techpeak.hac.core.dtos.RegisterDto;
import com.techpeak.hac.core.models.Role;
import com.techpeak.hac.core.models.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<BearerToken> register(RegisterDto registerDto);

    BearerToken authenticate(LoginDto loginDto);
    Role saveRole(Role role);
    User saverUser(User user);
}
