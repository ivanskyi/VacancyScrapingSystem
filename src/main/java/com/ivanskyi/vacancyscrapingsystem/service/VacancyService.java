package com.ivanskyi.vacancyscrapingsystem.service;


import com.ivanskyi.vacancyscrapingsystem.dto.VacancyDTO;
import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import org.springframework.ui.Model;

import java.util.List;

public interface VacancyService {
    List<Vacancy> getAllVacancies();

    List<VacancyDTO> getAllVacanciesAsDTOs();

    List<VacancyDTO> getVacanciesByPositionName(String searchQuery, Model model);

    List<VacancyDTO> sortVacanciesByChosenParam(String sortBy, String sortOrder, Model model);
}
