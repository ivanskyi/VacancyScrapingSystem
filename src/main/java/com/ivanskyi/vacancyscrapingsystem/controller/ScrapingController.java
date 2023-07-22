package com.ivanskyi.vacancyscrapingsystem.controller;

import com.ivanskyi.vacancyscrapingsystem.service.ScrapingService;
import com.ivanskyi.vacancyscrapingsystem.service.impl.techstars.vacancy.VacancyParseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job-site-scraping")
public class ScrapingController {

    public ScrapingController(final ScrapingService scrapingService, final VacancyParseFactory vacancyParseFactory) {
        this.scrapingService = scrapingService;
        this.vacancyParseFactory = vacancyParseFactory;
    }

    private ScrapingService scrapingService;

    private VacancyParseFactory vacancyParseFactory;

    @GetMapping("parse-search-page")
    public ResponseEntity<String> parseSearchPage() {
        return scrapingService.startCollectListOfVacancies();
    }

    @GetMapping("parse-vacancies")
    public ResponseEntity<String> parseVacancies() {
        return vacancyParseFactory.startScrapingProcesses();
    }
}
