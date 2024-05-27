package com.techpeak.hac.core.utils;


import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtils {
    private AuthUtils() {
    }

    public static Long getCurrentUserId(){
    User user = getCurrentUser();
    return user.getId();
}
    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }else{
            throw new NotFoundException("No Auth") ;
        }
    }
}
