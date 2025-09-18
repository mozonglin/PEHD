package com.example.pehd.repository;

import com.example.pehd.entity.EquipmentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 器材库存Repository
 */
@Repository
public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, String> {
    
    /**
     * 根据分类ID查找器材（未删除）
     */
    List<EquipmentItem> findByCategoryIdAndIsDeletedFalseOrderByCreatedAtDesc(String categoryId);
    
    /**
     * 根据分类ID分页查找器材（未删除）
     */
    Page<EquipmentItem> findByCategoryIdAndIsDeletedFalse(String categoryId, Pageable pageable);
    
    /**
     * 根据名称模糊查找器材（未删除）
     */
    Page<EquipmentItem> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);
    
    /**
     * 根据分类和名称模糊查找器材（未删除）
     */
    Page<EquipmentItem> findByCategoryIdAndNameContainingAndIsDeletedFalse(
        String categoryId, String name, Pageable pageable);
    
    /**
     * 查找所有未删除的器材
     */
    Page<EquipmentItem> findByIsDeletedFalse(Pageable pageable);
    
    /**
     * 根据ID查找未删除的器材
     */
    Optional<EquipmentItem> findByIdAndIsDeletedFalse(String id);
    
    /**
     * 查找可用库存大于0的器材
     */
    List<EquipmentItem> findByAvailableQuantityGreaterThanAndIsDeletedFalseOrderByAvailableQuantityDesc(Integer quantity);
    
    /**
     * 查找低库存器材（可用数量 <= 指定数量）
     */
    List<EquipmentItem> findByAvailableQuantityLessThanEqualAndIsDeletedFalseOrderByAvailableQuantityAsc(Integer quantity);
    
    /**
     * 获取器材详细信息（包含分类信息）
     */
    @Query("""
        SELECT 
            e.id as equipmentId,
            e.categoryId as categoryId,
            c.name as categoryName,
            e.name as equipmentName,
            e.model as model,
            e.specification as specification,
            e.totalQuantity as totalQuantity,
            e.availableQuantity as availableQuantity,
            e.borrowedQuantity as borrowedQuantity,
            e.damagedQuantity as damagedQuantity,
            e.unitPrice as unitPrice,
            e.purchaseDate as purchaseDate,
            e.warrantyPeriod as warrantyPeriod,
            e.storageLocation as storageLocation,
            e.createdBy as createdBy,
            e.createdAt as createdAt,
            e.updatedAt as updatedAt
        FROM EquipmentItem e
        LEFT JOIN EquipmentCategory c ON e.categoryId = c.id
        WHERE e.id = :equipmentId AND e.isDeleted = false
        """)
    Optional<Object[]> findEquipmentDetailsById(@Param("equipmentId") String equipmentId);
    
    /**
     * 获取热门器材排行
     */
    @Query("""
        SELECT 
            e.id as equipmentId,
            e.name as equipmentName,
            e.model as model,
            c.name as categoryName,
            e.availableQuantity as availableQuantity,
            e.totalQuantity as totalQuantity,
            COUNT(a.id) as borrowCount,
            COALESCE(SUM(a.quantity), 0) as totalBorrowedQuantity
        FROM EquipmentItem e
        LEFT JOIN EquipmentCategory c ON e.categoryId = c.id
        LEFT JOIN EquipmentApplication a ON e.id = a.equipmentId AND a.status = 'approved'
        WHERE e.isDeleted = false
        GROUP BY e.id, e.name, e.model, c.name, e.availableQuantity, e.totalQuantity
        ORDER BY borrowCount DESC, totalBorrowedQuantity DESC
        """)
    Page<Object[]> findPopularEquipment(Pageable pageable);
    
    /**
     * 根据存储位置查找器材
     */
    List<EquipmentItem> findByStorageLocationContainingAndIsDeletedFalse(String storageLocation);
    
    /**
     * 检查器材名称在同一分类下是否存在（排除指定ID）
     */
    boolean existsByCategoryIdAndNameAndIdNot(String categoryId, String name, String id);
    
    /**
     * 检查器材名称在同一分类下是否存在
     */
    boolean existsByCategoryIdAndName(String categoryId, String name);
}
