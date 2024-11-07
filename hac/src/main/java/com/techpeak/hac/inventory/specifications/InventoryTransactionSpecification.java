package com.techpeak.hac.inventory.specifications;

import com.techpeak.hac.inventory.enums.TransactionType;
import com.techpeak.hac.inventory.models.InventoryTransaction;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryTransactionSpecification {

    public static Specification<InventoryTransaction> search(
            String search, Long ref, Long store, Long user,
            String status, String transactionType,
            String transactionDate, Long desStore) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("number")),
                    "%" + search.toLowerCase() + "%"
                ));
            }

            if (ref != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("internalRef").get("id"), ref
                ));
            }

            if (store != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("store").get("id"), store
                ));
            }

            if (user != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("user").get("id"), user
                ));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    root.get("status"),
                    RequestStatus.valueOf(status)
                ));
            }

            if (transactionType != null && !transactionType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    root.get("transactionType"),
                    TransactionType.valueOf(transactionType)
                ));
            }

            if (transactionDate != null && !transactionDate.isEmpty()) {
                LocalDate date = LocalDate.parse(transactionDate);
                predicates.add(criteriaBuilder.equal(
                    root.get("transactionDate"), date
                ));
            }

            if (desStore != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("desiStore").get("id"), desStore
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
