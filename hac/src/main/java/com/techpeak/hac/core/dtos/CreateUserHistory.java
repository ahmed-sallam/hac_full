package com.techpeak.hac.core.dtos;

import com.techpeak.hac.core.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserHistory implements Serializable {

    private String actionDetails;
    private String tableName;
    private Long recordId;
    private User user;

}
