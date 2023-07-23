package com.ivanskyi.vacancyscrapingsystem.controller;

import com.ivanskyi.vacancyscrapingsystem.dto.VacancyDTO;
import com.ivanskyi.vacancyscrapingsystem.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class VacancyController {

    private final VacancyService vacancyService;

    @Autowired
    public VacancyController(final VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("/")
    public String showVacancyList(final Model model) {
        List<VacancyDTO> allVacancies = vacancyService.getAllVacanciesAsDTOs();
        model.addAttribute("vacancies", allVacancies);
        return "index";
    }

    @GetMapping("/search")
    public String searchVacancies(@RequestParam(value = "searchQuery", required = false) final String searchQuery,
                                  final Model model) {
        List<VacancyDTO> foundVacancies = vacancyService.getVacanciesByPositionName(searchQuery, model);
        model.addAttribute("vacancies", foundVacancies);
        model.addAttribute("searchQuery", searchQuery);
        return "index";
    }

    @GetMapping("/sort")
    public String sortVacancies(@RequestParam("sortBy") final String sortBy,
                                @RequestParam("sortOrder") final String sortOrder,
                                final Model model) {
        List<VacancyDTO> sortedVacancies = vacancyService.sortVacanciesByChosenParam(sortBy, sortOrder, model);
        model.addAttribute("vacancies", sortedVacancies);
        return "index";
    }
}
