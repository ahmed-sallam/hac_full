package com.techpeak.hac.core.dtos;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.core.models.User}
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDtoShort implements Serializable {
    private Long id;
    private String username;
}