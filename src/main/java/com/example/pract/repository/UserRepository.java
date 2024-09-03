package com.example.pract.repository;

import com.example.pract.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    Optional<User> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :newUsername WHERE u.id = :userId")
    int updateUsername(@Param("userId") Long userId, @Param("newUsername") String newUsername);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.first_name = :newFirstName, u.last_name = :newLastName WHERE u.id = :userId")
    int updateProfile(@Param("userId") Long userId, @Param("newFirstName") String newFirstName, @Param("newLastName") String newLastName);
}
