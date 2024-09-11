package com.example.pract.repository;

import com.example.pract.entity.Criminal;
import com.example.pract.enums.CriminalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CriminalRepository extends JpaRepository<Criminal, Long> {
    Optional<Criminal> findById(Long id);

    @Query("SELECT c FROM Criminal c WHERE c.status = :status")
    List<Criminal> findByStatus(@Param("status") CriminalStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE Criminal c SET c.status = :status WHERE c.id = :id")
    int changeStatus(@Param("status") CriminalStatus status, @Param("id") Long id);

    @Query("SELECT c FROM Criminal c WHERE c.status <> :status")
    List<Criminal> findAllExceptStatus(@Param("status") CriminalStatus status);
}
