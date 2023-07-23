package com.ivanskyi.vacancyscrapingsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AddressDTO {

    private Long id;
    private String name;
    private List<VacancyDTO> vacancyDetails = new ArrayList<>();
}
