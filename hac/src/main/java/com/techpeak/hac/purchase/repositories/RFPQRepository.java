package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.RFPQ;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RFPQRepository extends JpaRepository<RFPQ, Long>, JpaSpecificationExecutor<RFPQ> {
    Optional<RFPQ> findTopByOrderByNumberDesc();

    @Query(value = "WITH rfpq_info AS ( " +
            "  SELECT mr.id AS rfpq_id, " +
            "         mr.number AS rfpq_number, " +
            "         mr.status AS rfpq_status, " +
            "         mr.date AS rfpq_date, " +
            "         mr.notes AS rfpq_notes, " +
            "         s.id AS store_id, " +
            "         s.name_ar AS store_name_ar, " +
            "         s.name_en AS store_name_en, " +
            "         u.username AS user_name, " +
            "         ir.id AS internal_ref_id, " +
            "         ir.current_phase AS internal_ref_phase, " +
            "         u.id AS user__id " +
            "  FROM rfpqs mr " +
            "  JOIN stores s ON mr.store_id = s.id " +
            "  JOIN internal_refs ir ON mr.internal_ref_id = ir.id " +
            "  JOIN users u ON mr.user_id = u.id " +
            "  WHERE mr.id = ?1 " +
            "), " +
            "rfpq_lines_info AS ( " +
            "  SELECT mrl.quantity, " +
            "         mrl.notes, " +
            "         mrl.rfpq_id, " +
            "         p.id AS product_id, " +
            "         p.product_number AS product_number, " +
            "         p.description_ar AS product_description_ar, " +
            "         p.description_en AS product_description_en, " +
            "         mb.name_ar AS main_brand_name_ar, " +
            "         mb.name_en AS main_brand_name_en, " +
            "         sb.name_ar AS sub_brand_name_ar, " +
            "         sb.name_en AS sub_brand_name_en " +
            "  FROM rfpq_lines mrl " +
            "  JOIN products p ON mrl.product_id = p.id " +
            "  JOIN brands mb ON p.main_brand_id = mb.id " +
            "  JOIN brands sb ON p.sub_brand_id = sb.id " +
//            "  LEFT JOIN inventory i ON p.id = i.product_id " +
            "  WHERE mrl.rfpq_id = ?1 " +
            "  GROUP BY mrl.quantity, mrl.notes, mrl.rfpq_id, p.id, p.product_number, " +
            "           p.description_ar, p.description_en, mb.name_ar, mb.name_en, sb.name_ar, sb.name_en " +
            "), " +
            "user_histories_info AS ( " +
            "  SELECT uh.id AS user_history_id, " +
            "         uh.user_id, " +
            "         u.username, " +
            "         uh.action_details, " +
            "         uh.date_time, " +
            "         uh.table_name, " +
            "         uh.record_id " +
            "  FROM user_histories uh " +
            "  JOIN users u ON uh.user_id = u.id " +
            "  WHERE uh.record_id = ?1 AND uh.table_name = 'rfpqs' " +
            ") " +
            "SELECT mri.rfpq_id AS id, " +
            "       mri.rfpq_number AS number, " +
            "       mri.rfpq_date AS date, " +
            "       mri.rfpq_status AS status, " +
            "       mri.rfpq_notes AS notes, " +
            "       jsonb_build_object('id', mri.store_id, 'nameAr', mri.store_name_ar, 'nameEn', mri.store_name_en) AS store, " +
            "       mri.internal_ref_id AS internalRef, " +
            "       mri.internal_ref_phase AS currentPhase, " +
            "       jsonb_build_object('id', mri.user__id, 'username', mri.user_name) AS userDto, " +
            "       COALESCE(json_agg(DISTINCT jsonb_build_object( " +
            "           'quantity', mrl.quantity, " +
            "           'notes', mrl.notes, " +
            "           'product', jsonb_build_object( " +
            "               'id', mrl.product_id, " +
            "               'productNumber', mrl.product_number, " +
            "               'descriptionAr', mrl.product_description_ar, " +
            "               'descriptionEn', mrl.product_description_en, " +
            "               'mainBrandAr', mrl.main_brand_name_ar, " +
            "               'mainBrandEn', mrl.main_brand_name_en, " +
            "               'subBrandAr', mrl.sub_brand_name_ar, " +
            "               'subBrandEn', mrl.sub_brand_name_en " +
            "           ) " +
            "       )) FILTER (WHERE mrl.rfpq_id IS NOT NULL), '[]') AS lines, " +
            "       COALESCE(json_agg(DISTINCT jsonb_build_object( " +
            "           'id', uhi.user_history_id, " +
            "           'actionDetails', uhi.action_details, " +
            "           'tableName', uhi.table_name, " +
            "           'recordId', uhi.record_id, " +
            "           'dateTime', uhi.date_time, " +
            "           'user', jsonb_build_object('id', uhi.user_id, 'username', uhi.username) " +
            "       )) FILTER (WHERE uhi.user_history_id IS NOT NULL), '[]') AS history " +
            "FROM rfpq_info mri " +
            "LEFT JOIN rfpq_lines_info mrl ON mri.rfpq_id = mrl.rfpq_id " +
            "LEFT JOIN user_histories_info uhi ON mri.rfpq_id = uhi.record_id " +
            "GROUP BY mri.rfpq_id, mri.rfpq_number, mri.rfpq_status, " +
            "         mri.rfpq_date, mri.rfpq_notes, mri.internal_ref_id, mri.internal_ref_phase, " +
            "         mri.store_id, mri.store_name_ar, mri.store_name_en, mri.user__id, mri.user_name;",
            nativeQuery = true)
    Tuple findByIdWithHistory(@Param("id") Long id);


}
