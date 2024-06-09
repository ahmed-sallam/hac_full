package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.dtos.CreateUserHistory;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.core.repositories.UserHistoryRepository;
import com.techpeak.hac.core.services.UserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryServiceImpl implements UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;

    private static UserHistory toUserHistory(CreateUserHistory createUserHistory) {
        return UserHistory.builder()
                .actionDetails(createUserHistory.getActionDetails())
                .tableName(createUserHistory.getTableName())
                .recordId(createUserHistory.getRecordId())
                .user(createUserHistory.getUser())
                .build();
    }

    @Override
    public void create(CreateUserHistory createUserHistory) {
        userHistoryRepository.save(toUserHistory(createUserHistory));
    }

    @Override
    public List<UserHistoryResponse> getItemHistory(String tableName, Long id) {
        List<UserHistory> all = userHistoryRepository.findAllByTableNameAndRecordId(tableName, id);
        return all.stream().map(this::toUserHistoryResponse).toList();
    }

    private UserHistoryResponse toUserHistoryResponse(UserHistory userHistory) {
        return new UserHistoryResponse(userHistory.getId(),
                userHistory.getActionDetails(),
                userHistory.getTableName(),
                userHistory.getRecordId(),
                userHistory.getDateTime(),
                new UserDtoShort(userHistory.getUser().getId(), userHistory.getUser().getUsername()));
    }
    @Override
    public void createUserHistory(User user, Long recordId, String actionDetails, String tableName) {
        CreateUserHistory createUserHistory = new CreateUserHistory(
                actionDetails,
                tableName,
                recordId,
                user
        );
        create(createUserHistory);
    }

}
