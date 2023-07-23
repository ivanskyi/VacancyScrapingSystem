package com.ivanskyi.vacancyscrapingsystem.dto;

import com.ivanskyi.vacancyscrapingsystem.entity.Address;
import com.ivanskyi.vacancyscrapingsystem.entity.Tag;
import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import com.ivanskyi.vacancyscrapingsystem.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class VacancyDTO {

    private Long id;
    private String vacancyURL;
    private String positionName;
    private String organizationURL;
    private String logo;
    private String organizationTitle;
    private String laborFunction;
    private String postedDate;
    private String description;
    private List<Long> addressIds = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public static VacancyDTO fromEntity(final Vacancy vacancy) {
        VacancyDTO vacancyDTO = new VacancyDTO();
        vacancyDTO.setId(vacancy.getId());
        vacancyDTO.setVacancyURL(vacancy.getVacancyURL());
        vacancyDTO.setPositionName(vacancy.getPositionName());
        vacancyDTO.setOrganizationURL(vacancy.getOrganizationURL());
        vacancyDTO.setLogo(vacancy.getLogo());
        vacancyDTO.setOrganizationTitle(vacancy.getOrganizationTitle());
        vacancyDTO.setLaborFunction(vacancy.getLaborFunction());
        vacancyDTO.setPostedDate(convertToPostedDate(vacancy.getPostedDate()));
        vacancyDTO.setDescription(vacancy.getDescription());
        vacancyDTO.setAddressIds(convertAddressListToIds(vacancy.getAddresses()));
        vacancyDTO.setTags(convertTagListToNames(vacancy.getTags()));
        return vacancyDTO;
    }

    private static String convertToPostedDate(final Long postedDate) {
        return Objects.nonNull(postedDate) ? TimeUtil.convertFromUnixTimestamp(postedDate) : "";
    }

    private static List<Long> convertAddressListToIds(final List<Address> addresses) {
        List<Long> addressIds = new ArrayList<>();
        for (Address address : addresses) {
            addressIds.add(address.getId());
        }
        return addressIds;
    }

    private static List<String> convertTagListToNames(final List<Tag> tags) {
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }
        return tagNames;
    }
}
