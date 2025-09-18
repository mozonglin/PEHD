package com.example.pehd.repository;

import com.example.pehd.entity.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 器材分类Repository
 */
@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, String> {
    
    /**
     * 根据名称查找分类
     */
    Optional<EquipmentCategory> findByName(String name);
    
    /**
     * 根据排序字段升序获取所有分类
     */
    List<EquipmentCategory> findAllByOrderBySortAsc();
    
    /**
     * 检查分类名称是否存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, String id);
    
    /**
     * 检查分类名称是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 获取分类统计信息
     */
    @Query("""
        SELECT 
            c.id as categoryId,
            c.name as categoryName,
            c.description as description,
            c.icon as icon,
            c.sort as sort,
            c.createdAt as createdAt,
            c.updatedAt as updatedAt,
            COUNT(e.id) as equipmentCount,
            COALESCE(SUM(e.totalQuantity), 0) as totalQuantity,
            COALESCE(SUM(e.availableQuantity), 0) as availableQuantity,
            COALESCE(SUM(e.borrowedQuantity), 0) as borrowedQuantity,
            COALESCE(SUM(e.damagedQuantity), 0) as damagedQuantity
        FROM EquipmentCategory c
        LEFT JOIN EquipmentItem e ON c.id = e.categoryId AND e.isDeleted = false
        GROUP BY c.id, c.name, c.description, c.icon, c.sort, c.createdAt, c.updatedAt
        ORDER BY c.sort ASC
        """)
    List<Object[]> findCategoryStatistics();
}
