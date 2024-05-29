package com.techpeak.hac.core.repositories;

import com.techpeak.hac.core.models.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findAllByTableNameAndRecordId(String tableName, Long id);
}
