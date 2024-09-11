package com.example.pract.repository;

import com.example.pract.entity.News;
import com.example.pract.enums.NewsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findById(Long id);

    @Query("SELECT n FROM News n WHERE n.type = :type")
    List<News> findByStatus(@Param("type") NewsType type);
}
