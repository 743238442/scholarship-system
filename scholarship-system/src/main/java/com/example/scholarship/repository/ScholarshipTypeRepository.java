package com.example.scholarship.repository;

import com.example.scholarship.entity.ScholarshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScholarshipTypeRepository extends JpaRepository<ScholarshipType, Long> {
    boolean existsByName(String name);
}