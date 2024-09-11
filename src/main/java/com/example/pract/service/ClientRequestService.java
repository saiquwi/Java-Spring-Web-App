package com.example.pract.service;

import com.example.pract.controller.MainController;
import com.example.pract.entity.ClientRequest;
import com.example.pract.entity.User;
import com.example.pract.enums.ReqStatus;
import com.example.pract.enums.Role;
import com.example.pract.repository.ClientRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ClientRequestService {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final ClientRequestRepository clientRequestRepository;

    @Autowired
    public ClientRequestService(ClientRequestRepository clientRequestRepository) {
        this.clientRequestRepository = clientRequestRepository;
    }

    public ClientRequest addMissingRequest(User user, String missFirstName, String missLastName, String locLastSeen, LocalDateTime missingSince, String otherInfo, Double reward) {

        ClientRequest clientRequest = new ClientRequest();
        log.info("addMissingRequest username: " + user.getUsername());
        clientRequest.setUser(user);
        clientRequest.setFirstName(missFirstName);
        clientRequest.setLastName(missLastName);
        clientRequest.setLocation(locLastSeen);
        clientRequest.setMissingSinceDate(missingSince);
        clientRequest.setOtherInfo(otherInfo);
        clientRequest.setReward(reward);

        if (user.getRole() == Role.ADMIN) {
            clientRequest.setStatus(ReqStatus.ACCEPTED);
            log.info("addMissingRequest status: " + ReqStatus.ACCEPTED);
        } else {
            clientRequest.setStatus(ReqStatus.REQUESTED);
            log.info("addMissingRequest status: " + ReqStatus.REQUESTED);
        }

        return clientRequestRepository.save(clientRequest);
    }

    public ClientRequest updateRequest(Long reqId, String missFirstName, String missLastName, String locLastSeen, LocalDateTime missingSince, String otherInfo, Double reward) {
        ClientRequest clientRequest = clientRequestRepository.findById(reqId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        clientRequest.setFirstName(missFirstName);
        clientRequest.setLastName(missLastName);
        clientRequest.setLocation(locLastSeen);
        clientRequest.setMissingSinceDate(missingSince);
        clientRequest.setOtherInfo(otherInfo);
        clientRequest.setReward(reward);

        return clientRequestRepository.save(clientRequest);
    }
}
