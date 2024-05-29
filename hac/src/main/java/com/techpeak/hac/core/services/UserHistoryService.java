package com.techpeak.hac.core.services;

import com.techpeak.hac.core.dtos.CreateUserHistory;
import com.techpeak.hac.core.dtos.UserHistoryResponse;

import java.util.List;

public interface UserHistoryService {
    void create(CreateUserHistory createUserHistory);
    List<UserHistoryResponse> getItemHistory(String tableName, Long id);
}
