package com.example.pract.repository;

import com.example.pract.entity.ClientRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.user.id = :clientId")
    List<ClientRequest> findByClientId(@Param("clientId") Long clientId);

    Optional<ClientRequest> findById(Long id);
}
