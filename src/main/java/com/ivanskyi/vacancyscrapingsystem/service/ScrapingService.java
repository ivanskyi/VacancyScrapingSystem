package com.ivanskyi.vacancyscrapingsystem.service;

import org.springframework.http.ResponseEntity;

public interface ScrapingService {

    ResponseEntity<String> startCollectListOfVacancies();
}
