package com.ivanskyi.vacancyscrapingsystem.service.impl.techstars.vacancy;

import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class VacanciesPool {

    private static List<Vacancy> list = new ArrayList<>();

    static void add(Vacancy card) {
        list.add(card);
    }

    public static List<Vacancy> getAndRemove() {
        final List<Vacancy> copiedList = new ArrayList<>();
        copiedList.addAll(list);
        list.clear();
        return copiedList;
    }
}
