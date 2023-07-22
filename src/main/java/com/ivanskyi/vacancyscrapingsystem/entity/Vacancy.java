package com.ivanskyi.vacancyscrapingsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000, columnDefinition = "Text", name = "vacancy_url")
    private String vacancyURL;

    @Column(length = 5000, columnDefinition = "Text", name = "position_name")
    private String positionName;

    @Column(length = 5000, columnDefinition = "Text", name = "organization_url")
    private String organizationURL;

    @Column(length = 5000, columnDefinition = "Text", name = "logo")
    private String logo;

    @Column(length = 5000, columnDefinition = "Text", name = "organization_title")
    private String organizationTitle;

    @Column(length = 5000, columnDefinition = "Text", name = "labor_function")
    private String laborFunction;

    @Column(length = 5000, columnDefinition = "BIGINT(20)", name = "posted_date")
    private Long postedDate;

    @Column(length = 7000, columnDefinition = "Text", name = "description")
    private String description;

    @ManyToMany(targetEntity = Address.class, cascade = {CascadeType.ALL})
    @JoinTable(name = "job_representation_card_addresses",
            joinColumns = {@JoinColumn(name = "job_representation_card_id")},
            inverseJoinColumns = {@JoinColumn(name = "addresse_id")})
    private List<Address> addresses = new ArrayList<>();

    @ManyToMany(targetEntity = Tag.class, cascade = {CascadeType.ALL})
    @JoinTable(name = "job_representation_card_tags",
            joinColumns = {@JoinColumn(name = "job_representation_card_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags = new ArrayList<>();
}
