package com.example.pract.repository;

import com.example.pract.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT cr FROM Image cr WHERE cr.user.id = :clientId")
    List<Image> findByClientId(@Param("clientId") Long clientId);
}