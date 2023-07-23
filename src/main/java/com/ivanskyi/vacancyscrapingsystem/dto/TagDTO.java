package com.ivanskyi.vacancyscrapingsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TagDTO {

    private Long id;
    private String name;
    private List<VacancyDTO> vacancyRepresentations = new ArrayList<>();
}
