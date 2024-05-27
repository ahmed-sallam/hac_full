package com.techpeak.hac.core.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.core.models.User}
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String username;
//    private Set<RoleDto> roles;
//
//    /**
//     * DTO for {@link com.techpeak.hac.core.models.Role}
//     */
//    @Value
//    public static class RoleDto implements Serializable {
//        Long id;
//        RoleEnum name;
//        Set<PrivilegeDto> privileges;
//
//        /**
//         * DTO for {@link com.techpeak.hac.core.models.Privilege}
//         */
//        @Value
//        public static class PrivilegeDto implements Serializable {
//            Long id;
//            String name;
//        }
//    }
}