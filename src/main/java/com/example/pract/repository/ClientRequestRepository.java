package com.example.pract.repository;

import com.example.pract.entity.ClientRequest;
import com.example.pract.entity.Criminal;
import com.example.pract.enums.ReqStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.user.id = :clientId")
    List<ClientRequest> findByClientId(@Param("clientId") Long clientId);

    Optional<ClientRequest> findById(Long id);

    @Query("SELECT cr FROM ClientRequest cr WHERE cr.status = :status")
    List<ClientRequest> findByStatus(@Param("status") ReqStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE ClientRequest cr SET cr.status = :status WHERE cr.id = :id")
    int changeStatus(@Param("status") ReqStatus status, @Param("id") Long id);

    @Query("SELECT cr FROM ClientRequest cr WHERE cr.status IN :statuses")
    List<ClientRequest> findByStatuses(@Param("statuses") List<ReqStatus> statuses);
}
