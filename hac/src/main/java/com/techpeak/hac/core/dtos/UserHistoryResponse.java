package com.techpeak.hac.core.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserHistoryResponse implements Serializable {
    private Long id;
    private String actionDetails;
    private String tableName;
    private Long recordId;
    private LocalDateTime dateTime = LocalDateTime.now();
    private UserDtoShort user;
}
