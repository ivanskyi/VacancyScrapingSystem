package com.ivanskyi.vacancyscrapingsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "addresses", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Vacancy> vacancyDetails = new ArrayList<>();
}
