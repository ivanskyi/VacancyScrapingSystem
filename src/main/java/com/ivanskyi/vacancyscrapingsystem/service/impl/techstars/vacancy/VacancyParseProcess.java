package com.ivanskyi.vacancyscrapingsystem.service.impl.techstars.vacancy;

import com.ivanskyi.vacancyscrapingsystem.constants.TechStarsPageElementsPaths;
import com.ivanskyi.vacancyscrapingsystem.entity.Address;
import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import com.ivanskyi.vacancyscrapingsystem.utils.TimeUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VacancyParseProcess implements Runnable {

    public VacancyParseProcess(WebDriver driver, Vacancy vacancy) {
        this.vacancy = vacancy;
        this.driver = driver;
    }

    private static final String POSTED_ON_LABEL = "Posted on";
    private final Vacancy vacancy;
    private WebDriver driver;

    @Override
    public void run() {
        final Vacancy vacancyForSaving = scrapeVacancy(vacancy);
        VacanciesPool.add(vacancyForSaving);
    }

    public Vacancy scrapeVacancy(Vacancy vacancy) {
        driver.get(vacancy.getVacancyURL());
        vacancy.setPositionName(findElementText(TechStarsPageElementsPaths.VACANCY_PAGE_POSITION_NAME_XPATH));
        vacancy.setOrganizationURL(findElementAttribute(TechStarsPageElementsPaths.VACANCY_PAGE_POSITION_ORGANIZATION_URL_XPATH, "href"));
        vacancy.setLogo(findElementAttribute(TechStarsPageElementsPaths.VACANCY_PAGE_LOGO_XPATH, "src"));
        vacancy.setOrganizationTitle(findElementText(TechStarsPageElementsPaths.VACANCY_PAGE_ORGANIZATION_TITLE_XPATH));
        vacancy.setLaborFunction(findElementText(TechStarsPageElementsPaths.VACANCY_PAGE_LABOR_FUNCTION_XPATH));
        vacancy.setAddresses(getAddress());
        vacancy.setPostedDate(getPostedDate());
        vacancy.setDescription(findElementAttribute(TechStarsPageElementsPaths.VACANCY_PAGE_DESCRIPTION_XPATH, "outerHTML"));
        return vacancy;
    }

    private String findElementText(String xpath) {
        return driver.findElement(By.xpath(xpath)).getText();
    }

    private String findElementAttribute(String xpath, String attribute) {
        return driver.findElement(By.xpath(xpath)).getAttribute(attribute);
    }

    private List<Address> getAddress() {
        final String addressesInOneLine = findElementText(TechStarsPageElementsPaths.VACANCY_PAGE_ADDRESS_XPATH);
        return Arrays.stream(addressesInOneLine.split(","))
                .map(String::trim)
                .map(this::createAddress)
                .collect(Collectors.toList());
    }

    private Address createAddress(String tagName) {
        final Address newAddress = new Address();
        newAddress.setName(tagName.trim());
        return newAddress;
    }

    private long getPostedDate() {
        final String postedDate = findElementText(TechStarsPageElementsPaths.VACANCY_PAGE_POSTED_DATE_XPATH)
                .replace(POSTED_ON_LABEL, "").trim();
        long unixTimestamp = TimeUtil.convertToUnixTimestamp(postedDate);
        return unixTimestamp;
    }
}
