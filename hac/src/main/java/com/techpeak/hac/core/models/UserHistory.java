package com.techpeak.hac.core.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "UserHistory")
@Table(name = "user_histories", indexes = {
        @Index(name="idx_user_histories_table_name_id", columnList = "table_name, record_id"),
        @Index(name = "idx_user_histories_user_id", columnList = "user_id")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserHistory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;
        @Column(name="action_details", nullable = false, columnDefinition = "TEXT")
        private String actionDetails;
        @Column(name="table_name", nullable = false)
        private String tableName;
        @Column(name="record_id", nullable = false)
        private Long recordId;
        @CreationTimestamp
        @Column(updatable = false, nullable = false, name="date_time")
        private LocalDateTime dateTime = LocalDateTime.now();
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
}
