package com.ivanskyi.vacancyscrapingsystem.service.impl;

import com.ivanskyi.vacancyscrapingsystem.dto.VacancyDTO;
import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import com.ivanskyi.vacancyscrapingsystem.repository.VacancyRepository;
import com.ivanskyi.vacancyscrapingsystem.service.VacancyService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacancyServiceImpl implements VacancyService {

    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    private VacancyRepository vacancyRepository;

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<VacancyDTO> getAllVacanciesAsDTOs() {
        return getAllVacancies().stream()
                .map(VacancyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<VacancyDTO> getVacanciesByPositionName(final String searchQuery, final Model model) {
        List<VacancyDTO> filteredVacancies;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            filteredVacancies = getAllVacancies().stream()
                    .filter(vacancy -> vacancy.getPositionName().toLowerCase().contains(searchQuery.toLowerCase().trim()))
                    .map(VacancyDTO::fromEntity)
                    .collect(Collectors.toList());
        } else {
            filteredVacancies = getAllVacancies().stream()
                    .map(VacancyDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        return filteredVacancies;
    }

    @Override
    public List<VacancyDTO> sortVacanciesByChosenParam(final String sortBy, final String sortOrder, final Model model) {
        final List<Vacancy> allVacancies = new ArrayList<>(getAllVacancies());
        Comparator<Vacancy> comparator = (v1, v2) -> switch (sortBy) {
            case "positionName" -> compareStrings(v1.getPositionName(), v2.getPositionName());
            case "organizationTitle" -> compareStrings(v1.getOrganizationTitle(), v2.getOrganizationTitle());
            case "laborFunction" -> compareStrings(v1.getLaborFunction(), v2.getLaborFunction());
            case "postedDate" -> compareDates(v1.getPostedDate(), v2.getPostedDate());
            default -> 0;
        };
        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }
        List<Vacancy> sortedVacancies = allVacancies.stream()
                .sorted(comparator)
                .toList();
        return sortedVacancies.stream()
                .map(VacancyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private int compareStrings(final String firstString, final String secondString) {
        if (firstString == null && secondString == null) {
            return 0;
        } else if (firstString == null) {
            return -1;
        } else if (secondString == null) {
            return 1;
        } else {
            return firstString.compareToIgnoreCase(secondString);
        }
    }

    private int compareDates(final Long firstDate, final Long secondDate) {
        if (firstDate == null && secondDate == null) {
            return 0;
        } else if (firstDate == null) {
            return -1;
        } else if (secondDate == null) {
            return 1;
        } else {
            return firstDate.compareTo(secondDate);
        }
    }
}
