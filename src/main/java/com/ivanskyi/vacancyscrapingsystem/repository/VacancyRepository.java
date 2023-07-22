package com.ivanskyi.vacancyscrapingsystem.repository;

import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Override
    Vacancy save(Vacancy entity);

    @Override
    List<Vacancy> findAll();

    List<Vacancy> findAllByVacancyURL(String url);
}
