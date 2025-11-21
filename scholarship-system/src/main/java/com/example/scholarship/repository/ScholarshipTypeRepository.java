package com.example.scholarship.repository;

import com.example.scholarship.entity.ScholarshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 奖学金类型数据访问接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface ScholarshipTypeRepository extends JpaRepository<ScholarshipType, Long> {

    /**
     * 根据名称查询奖学金类型
     * 
     * @param name 奖学金名称
     * @return 奖学金类型对象
     */
    Optional<ScholarshipType> findByName(String name);

    /**
     * 检查是否存在指定名称的奖学金类型
     * 
     * @param name 奖学金名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查是否存在除指定ID外的同名奖学金类型
     * 
     * @param name 奖学金名称
     * @param id 排除的ID
     * @return 是否存在
     */
    boolean existsByNameAndIdNot(String name, Long id);
}