package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.core.models.UserHistory;

public class UserHistoryMapper {
    private UserHistoryMapper() {
    }

    public static UserHistoryResponse mapToDto(UserHistory userHistory) {
        return UserHistoryResponse.builder()
                .id(userHistory.getId())
                .recordId(userHistory.getRecordId())
                .tableName(userHistory.getTableName())
                .actionDetails(userHistory.getActionDetails())
                .dateTime(userHistory.getDateTime())
                .user(UserMapper.toDtoShort(userHistory.getUser()))
                .build();
    }
}
