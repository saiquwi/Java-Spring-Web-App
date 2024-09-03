package com.example.pract.service;

import com.example.pract.controller.MainController;
import com.example.pract.entity.Criminal;
import com.example.pract.enums.CriminalStatus;
import com.example.pract.repository.CriminalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CriminalService {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final CriminalRepository criminalRepository;

    @Autowired
    public CriminalService(CriminalRepository criminalRepository) {
        this.criminalRepository = criminalRepository;
    }

    public Criminal addCriminal(String criminalFirstName, String criminalLastName, String location, String crime, LocalDateTime wantedSince, Double reward) {
        Criminal criminal = new Criminal();
        criminal.setFirstName(criminalFirstName);
        criminal.setLastName(criminalLastName);
        criminal.setLocation(location);
        log.info("Crime: " + crime);
        criminal.setCrime(crime);
        criminal.setWantedSinceDate(wantedSince);
        criminal.setReward(reward);
        criminal.setStatus(CriminalStatus.WANTED);

        return criminalRepository.save(criminal);
    }
}
