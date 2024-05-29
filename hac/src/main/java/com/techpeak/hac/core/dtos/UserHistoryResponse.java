package com.techpeak.hac.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserHistoryResponse implements Serializable {
    private Long Id;
    private String actionDetails;
    private String tableName;
    private Long recordId;
    private LocalDateTime dateTime = LocalDateTime.now();
    private UserDtoShort user;
}
