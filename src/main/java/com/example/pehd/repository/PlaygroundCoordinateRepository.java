package com.example.pehd.repository;

import com.example.pehd.entity.PlaygroundCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 操场坐标数据访问层
 */
@Repository
public interface PlaygroundCoordinateRepository extends JpaRepository<PlaygroundCoordinate, Integer> {

    /**
     * 根据学校和操场名称查询坐标
     */
    @Query("SELECT pc FROM PlaygroundCoordinate pc WHERE pc.school = :school AND pc.playgroundName = :playgroundName")
    Optional<PlaygroundCoordinate> findBySchoolAndPlaygroundName(@Param("school") String school,
                                                               @Param("playgroundName") String playgroundName);

    /**
     * 查询所有不同的学校名称
     */
    @Query("SELECT DISTINCT pc.school FROM PlaygroundCoordinate pc ORDER BY pc.school")
    List<String> findAllDistinctSchools();

    /**
     * 查询指定学校的所有操场
     */
    @Query("SELECT pc FROM PlaygroundCoordinate pc WHERE pc.school = :school ORDER BY pc.playgroundName")
    List<PlaygroundCoordinate> findBySchoolOrderByPlaygroundName(@Param("school") String school);
}