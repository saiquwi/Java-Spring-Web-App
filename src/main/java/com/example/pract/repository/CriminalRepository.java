package com.example.pract.repository;

import com.example.pract.entity.Criminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriminalRepository extends JpaRepository<Criminal, Long> {
}
