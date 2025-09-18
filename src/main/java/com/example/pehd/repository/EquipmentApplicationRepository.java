package com.example.pehd.repository;

import com.example.pehd.entity.EquipmentApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 器材借用申请Repository
 */
@Repository
public interface EquipmentApplicationRepository extends JpaRepository<EquipmentApplication, String> {
    
    /**
     * 根据借用人ID查找申请（按创建时间倒序）
     */
    Page<EquipmentApplication> findByBorrowerIdOrderByCreatedAtDesc(String borrowerId, Pageable pageable);
    
    /**
     * 根据器材ID查找申请
     */
    Page<EquipmentApplication> findByEquipmentIdOrderByCreatedAtDesc(String equipmentId, Pageable pageable);
    
    /**
     * 根据状态查找申请
     */
    Page<EquipmentApplication> findByStatusOrderByCreatedAtDesc(
        EquipmentApplication.ApplicationStatus status, Pageable pageable);
    
    /**
     * 根据借用人和状态查找申请
     */
    List<EquipmentApplication> findByBorrowerIdAndStatus(
        String borrowerId, EquipmentApplication.ApplicationStatus status);
    
    /**
     * 根据器材ID和状态查找申请
     */
    List<EquipmentApplication> findByEquipmentIdAndStatus(
        String equipmentId, EquipmentApplication.ApplicationStatus status);
    
    /**
     * 查找指定时间范围内的申请
     */
    List<EquipmentApplication> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查找超期未归还的申请
     */
    @Query("""
        SELECT a FROM EquipmentApplication a 
        WHERE a.status = 'approved' 
        AND a.expectedReturnDate < :currentTime 
        AND a.actualReturnDate IS NULL
        ORDER BY a.expectedReturnDate ASC
        """)
    List<EquipmentApplication> findOverdueApplications(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * 获取申请详细信息（包含器材和借用人信息）
     */
    @Query("""
        SELECT 
            a.id as applicationId,
            a.equipmentId as equipmentId,
            e.name as equipmentName,
            e.model as equipmentModel,
            c.name as categoryName,
            a.borrowerId as borrowerId,
            u.name as borrowerName,
            u.studentId as borrowerStudentId,
            u.phoneNumber as borrowerPhone,
            a.quantity as quantity,
            a.purpose as purpose,
            a.borrowDate as borrowDate,
            a.expectedReturnDate as expectedReturnDate,
            a.actualReturnDate as actualReturnDate,
            a.status as status,
            a.remark as remark,
            a.approvedBy as approvedBy,
            approver.name as approverName,
            a.approvedAt as approvedAt,
            a.actualQuantity as actualQuantity,
            a.returnCondition as returnCondition,
            a.returnedBy as returnedBy,
            returner.name as returnerName,
            a.createdAt as createdAt,
            a.updatedAt as updatedAt
        FROM EquipmentApplication a
        LEFT JOIN EquipmentItem e ON a.equipmentId = e.id
        LEFT JOIN EquipmentCategory c ON e.categoryId = c.id
        LEFT JOIN User u ON a.borrowerId = u.id
        LEFT JOIN User approver ON a.approvedBy = approver.id
        LEFT JOIN User returner ON a.returnedBy = returner.id
        WHERE a.id = :applicationId
        """)
    Optional<Object[]> findApplicationDetailsById(@Param("applicationId") String applicationId);
    
    /**
     * 根据借用人获取申请详细信息
     */
    @Query("""
        SELECT 
            a.id as applicationId,
            a.equipmentId as equipmentId,
            e.name as equipmentName,
            e.model as equipmentModel,
            c.name as categoryName,
            a.quantity as quantity,
            a.purpose as purpose,
            a.borrowDate as borrowDate,
            a.expectedReturnDate as expectedReturnDate,
            a.actualReturnDate as actualReturnDate,
            a.status as status,
            a.remark as remark,
            a.approvedAt as approvedAt,
            a.actualQuantity as actualQuantity,
            a.returnCondition as returnCondition,
            a.createdAt as createdAt,
            a.updatedAt as updatedAt
        FROM EquipmentApplication a
        LEFT JOIN EquipmentItem e ON a.equipmentId = e.id
        LEFT JOIN EquipmentCategory c ON e.categoryId = c.id
        WHERE a.borrowerId = :borrowerId
        ORDER BY a.createdAt DESC
        """)
    Page<Object[]> findApplicationDetailsByBorrowerId(@Param("borrowerId") String borrowerId, Pageable pageable);
    
    /**
     * 统计用户借用次数
     */
    @Query("SELECT COUNT(a) FROM EquipmentApplication a WHERE a.borrowerId = :borrowerId AND a.status = 'approved'")
    Long countByBorrowerIdAndStatusApproved(@Param("borrowerId") String borrowerId);
    
    /**
     * 统计器材借用次数
     */
    @Query("SELECT COUNT(a) FROM EquipmentApplication a WHERE a.equipmentId = :equipmentId AND a.status = 'approved'")
    Long countByEquipmentIdAndStatusApproved(@Param("equipmentId") String equipmentId);
    
    /**
     * 检查用户是否有未归还的同一器材借用
     */
    @Query("""
        SELECT COUNT(a) > 0 FROM EquipmentApplication a 
        WHERE a.borrowerId = :borrowerId 
        AND a.equipmentId = :equipmentId 
        AND a.status = 'approved' 
        AND a.actualReturnDate IS NULL
        """)
    boolean existsUnreturnedApplicationByBorrowerAndEquipment(
        @Param("borrowerId") String borrowerId, 
        @Param("equipmentId") String equipmentId);
    
    /**
     * 获取申请统计信息
     */
    @Query("""
        SELECT 
            DATE(a.createdAt) as applicationDate,
            a.status as status,
            COUNT(a) as applicationCount,
            SUM(a.quantity) as totalQuantity
        FROM EquipmentApplication a
        WHERE a.createdAt >= :startDate
        GROUP BY DATE(a.createdAt), a.status
        ORDER BY applicationDate DESC, a.status
        """)
    List<Object[]> findApplicationStatistics(@Param("startDate") LocalDateTime startDate);
}
