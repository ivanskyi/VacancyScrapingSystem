package com.ivanskyi.vacancyscrapingsystem.service.impl.techstars.vacancy;

import com.ivanskyi.vacancyscrapingsystem.entity.Address;
import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import com.ivanskyi.vacancyscrapingsystem.repository.AddressRepository;
import com.ivanskyi.vacancyscrapingsystem.repository.VacancyRepository;
import com.ivanskyi.vacancyscrapingsystem.utils.SeleniumDriverUtil;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class VacancyParseFactory {

    public VacancyParseFactory(AddressRepository addressRepository, VacancyRepository vacancyRepository) {
        this.addressRepository = addressRepository;
        this.vacancyRepository = vacancyRepository;
    }

    private AddressRepository addressRepository;
    private VacancyRepository vacancyRepository;

    private static final Logger logger = LoggerFactory.getLogger(VacancyParseFactory.class);
    private static final String APPLYING_LINK_PART_OF_VACANCY = "https://jobs.techstars.com/companies/";
    private static final int NUMBER_OF_THREADS = 3;


    public ResponseEntity<String> startScrapingProcesses() {
        try {
            final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            final List<Vacancy> vacanciesForParsing = getVacanciesForParsing();
            logger.info("Need to parse: {} vacancies. ", vacanciesForParsing.size());
            List<WebDriver> webDriverPool = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                webDriverPool.add(SeleniumDriverUtil.getDriver());
            }
            for (int i = 0; i < vacanciesForParsing.size(); i++) {
                Vacancy vacancyRepresentation = vacanciesForParsing.get(i);
                WebDriver driver = webDriverPool.get(i % NUMBER_OF_THREADS);
                executorService.submit(() -> {
                    VacancyParseProcess vacancyParseProcess = new VacancyParseProcess(driver, vacancyRepresentation);
                    vacancyParseProcess.run();
                    saveNewVacancy();
                });
            }
            executorService.shutdown();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to extract the list of vacancies.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveNewVacancy() {
        final List<Vacancy> vacancies = VacanciesPool.getAndRemove();
        logger.info("Count vacancies for saving: {} ", vacancies.size());
        for (Vacancy vacancy : vacancies) {
            List<Address> addresses = vacancy.getAddresses()
                    .stream()
                    .map(this::saveAddressOrGetExisted)
                    .collect(Collectors.toList());
            vacancy.setAddresses(addresses);
            vacancyRepository.save(vacancy);
            logger.info("Saved vacancy with url: {}", vacancy.getVacancyURL());
        }
    }

    private Address saveAddressOrGetExisted(Address address) {
        if (addressRepository.findByName(address.getName()).isPresent()) {
            return addressRepository.findByName(address.getName()).get();
        }
        return addressRepository.save(address);
    }

    private List<Vacancy> getVacanciesForParsing() {
        return vacancyRepository.findAll()
                .stream()
                .filter(a -> Objects.nonNull(a.getVacancyURL()))
                .filter(a -> Objects.isNull(a.getPostedDate()))
                .filter(a -> a.getVacancyURL().contains(APPLYING_LINK_PART_OF_VACANCY))
                .collect(Collectors.toList());
    }
}
