package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long>, JpaSpecificationExecutor<MaterialRequest> {
    Optional<MaterialRequest> findTopByOrderByNumberDesc();
    @Query(value = "WITH material_request_info AS ( " +
            "  SELECT mr.id AS material_request_id, " +
            "         mr.number AS material_request_number, " +
            "         mr.status AS material_request_status, " +
            "         mr.date AS material_request_date, " +
            "         mr.notes AS material_request_notes, " +
            "         s.id AS store_id, " +
            "         s.name_ar AS store_name_ar, " +
            "         s.name_en AS store_name_en, " +
            "         u.username AS user_name, " +
            "         ir.id AS internal_ref_id, " +
            "         ir.current_phase AS internal_ref_phase, " +
            "         u.id AS user__id " +
            "  FROM material_requests mr " +
            "  JOIN stores s ON mr.store_id = s.id " +
            "  JOIN internal_refs ir ON mr.internal_ref_id = ir.id " +
            "  JOIN users u ON mr.user_id = u.id " +
            "  WHERE mr.id = ?1 " +
            "), " +
            "material_request_lines_info AS ( " +
            "  SELECT mrl.quantity, " +
            "         mrl.notes, " +
            "         mrl.material_request_id, " +
            "         p.id AS product_id, " +
            "         p.product_number AS product_number, " +
            "         p.description_ar AS product_description_ar, " +
            "         p.description_en AS product_description_en, " +
            "         mb.name_ar AS main_brand_name_ar, " +
            "         mb.name_en AS main_brand_name_en, " +
            "         sb.name_ar AS sub_brand_name_ar, " +
            "         sb.name_en AS sub_brand_name_en, " +
            "         i2.quantity AS store_quantity, " +
            "         SUM(i.quantity) AS total_inventory_quantity " +
            "  FROM material_request_lines mrl " +
            "  JOIN products p ON mrl.product_id = p.id " +
            "  JOIN brands mb ON p.main_brand_id = mb.id " +
            "  JOIN brands sb ON p.sub_brand_id = sb.id " +
            "  JOIN inventory i ON p.id = i.product_id " +
            "  LEFT JOIN inventory i2 ON p.id = i2.product_id AND i2.store_id = ?2 " +
            "  WHERE mrl.material_request_id = ?1 " +
            "  GROUP BY mrl.material_request_id, mrl.quantity, mrl.notes, p.id, p.product_number, " +
            "           p.description_ar, p.description_en, mb.name_ar, mb.name_en, sb.name_ar, sb.name_en, i2.quantity " +
            "), " +
            "user_histories_info AS ( " +
            "  SELECT DISTINCT uh.id AS user_history_id, " +
            "         uh.user_id, " +
            "         u.username, " +
            "         uh.action_details, " +
            "         uh.date_time, " +
            "         uh.table_name, " +
            "         uh.record_id " +
            "  FROM user_histories uh " +
            "  JOIN users u ON uh.user_id = u.id " +
            "  WHERE uh.record_id = ?1 AND uh.table_name = 'material_requests' " +
            ") " +
            "SELECT mri.material_request_id AS id, " +
            "       mri.material_request_number AS number, " +
            "       mri.material_request_date AS date, " +
            "       mri.material_request_status AS status, " +
            "       jsonb_build_object('id', mri.store_id, 'nameAr', mri.store_name_ar, 'nameEn', mri.store_name_en) AS store, " +
            "       mri.internal_ref_id AS internalRef, " +
            "       mri.internal_ref_phase AS currentPhase, " +
            "       jsonb_build_object('id', mri.user__id, 'username', mri.user_name) AS userDto, " +
            "       json_agg(json_build_object( " +
            "           'quantity', mrl.quantity, " +
            "           'notes', mrl.notes, " +
            "           'product', json_build_object( " +
            "               'id', mrl.product_id, " +
            "               'productNumber', mrl.product_number, " +
            "               'descriptionAr', mrl.product_description_ar, " +
            "               'descriptionEn', mrl.product_description_en, " +
            "               'mainBrandAr', mrl.main_brand_name_ar, " +
            "               'mainBrandEn', mrl.main_brand_name_en, " +
            "               'subBrandAr', mrl.sub_brand_name_ar, " +
            "               'subBrandEn', mrl.sub_brand_name_en, " +
            "               'totalInventory', mrl.total_inventory_quantity " +
            "           ), " +
            "           'storeInventory', mrl.store_quantity " +
            "       )) AS lines, " +
            "       json_agg( DISTINCT jsonb_build_object( " +
            "           'id', uhi.user_history_id, " +
            "           'actionDetails', uhi.action_details, " +
            "           'tableName', uhi.table_name, " +
            "           'recordId', uhi.record_id, " +
            "           'dateTime', uhi.date_time, " +
            "           'user', json_build_object('id', uhi.user_id, 'username', uhi.username) " +
            "       )) AS history " +
            "FROM material_request_info mri " +
            "LEFT JOIN material_request_lines_info mrl ON mri.material_request_id = mrl.material_request_id " +
            "LEFT JOIN user_histories_info uhi ON mri.material_request_id = uhi.record_id " +
            "GROUP BY mri.material_request_id, mri.material_request_number, mri.material_request_status, " +
            "         mri.material_request_date, mri.material_request_notes, mri.internal_ref_id, mri.internal_ref_phase, " +
            "         mri.store_id, mri.store_name_ar, mri.store_name_en, mri.user__id, mri.user_name;",
            nativeQuery = true)
    Tuple findByIdWithStock(@Param("id") Long id, @Param("storeId") Long storeId);


    @Query("SELECT mr FROM MaterialRequest mr WHERE mr.status = :status")
    List<MaterialRequest> findByStatus(@Param("status") RequestStatus status);
    Page<MaterialRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT mr FROM MaterialRequest mr WHERE mr.isActive = :isActive")
    List<MaterialRequest> findByIsActive(@Param("isActive") Boolean isActive);
    Page<MaterialRequest> findByIsActive(Boolean isActive, Pageable pageable);

    List<MaterialRequest> findByUser(User user);
    Page<MaterialRequest> findByUser(User user, Pageable pageable);

    @Query("SELECT COUNT(mr) FROM MaterialRequest mr WHERE mr.status = :status")
    Long countByStatus(@Param("status") RequestStatus status);
    @Modifying
    @Query("UPDATE MaterialRequest mr SET mr.status = :status WHERE mr.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") RequestStatus status);


}
