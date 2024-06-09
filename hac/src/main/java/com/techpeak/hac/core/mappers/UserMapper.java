package com.techpeak.hac.core.mappers;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.models.User;

public class UserMapper {
    private UserMapper() {
    }
    public static UserDtoShort toDtoShort(User user) {
        return UserDtoShort.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
