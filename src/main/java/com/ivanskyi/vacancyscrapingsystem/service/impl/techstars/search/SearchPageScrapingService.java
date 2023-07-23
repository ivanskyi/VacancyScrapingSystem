package com.ivanskyi.vacancyscrapingsystem.service.impl.techstars.search;

import com.ivanskyi.vacancyscrapingsystem.constants.TechStarsPageElementsPaths;
import com.ivanskyi.vacancyscrapingsystem.entity.Tag;
import com.ivanskyi.vacancyscrapingsystem.entity.Vacancy;
import com.ivanskyi.vacancyscrapingsystem.repository.TagRepository;
import com.ivanskyi.vacancyscrapingsystem.repository.VacancyRepository;
import com.ivanskyi.vacancyscrapingsystem.service.ScrapingService;
import com.ivanskyi.vacancyscrapingsystem.utils.DelayUtil;
import com.ivanskyi.vacancyscrapingsystem.utils.SeleniumDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchPageScrapingService implements ScrapingService {

    public SearchPageScrapingService(final VacancyRepository vacancyRepository, final TagRepository tagRepository) {
        this.vacancyRepository = vacancyRepository;
        this.tagRepository = tagRepository;
    }

    private VacancyRepository vacancyRepository;
    private TagRepository tagRepository;

    private static final String PAGE_PARAM = "page=";
    private static final String MAIN_JOBS_PAGE_URL = "https://jobs.techstars.com/jobs";
    private static final Logger logger = LoggerFactory.getLogger(SearchPageScrapingService.class);
    private final List<Vacancy> vacanciesThatWereParsed = new ArrayList<>();
    private Integer lastParsedPage = 0;

    @Override
    public ResponseEntity<String> startCollectListOfVacancies() {
        try {
            final WebDriver driver = SeleniumDriverUtil.getDriverWithoutImageRendering();
            fillSearchParams(driver);
            extractAllVacanciesFromPage(driver);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to extract the list of vacancies.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void fillSearchParams(final WebDriver driver) {
        driver.get(MAIN_JOBS_PAGE_URL);
        DelayUtil.startOneSecondDelay();
        setJobFunction(driver);
        logger.info("Search params were set.");
    }

    private void setJobFunction(final WebDriver driver) {
        DelayUtil.startFiveSecondsDelay();
        closeCookiesBlock(driver);
        clickByJobFunctionDropdown(driver);
        fillJobFunctionField(driver);
        chooseJobFunctionOptionFromDropdown(driver);
    }

    private void clickByJobFunctionDropdown(final WebDriver driver) {
        try {
            driver.findElement(By.xpath(TechStarsPageElementsPaths.JOB_FUNCTION_DROPDOWN_FIRST_XPATH)).click();
        } catch (Exception e) {
            driver.findElement(By.xpath(TechStarsPageElementsPaths.JOB_FUNCTION_DROPDOWN_SECOND_XPATH)).click();
            logger.error("Error occurred while clicking on the job function dropdown: ", e);
        }
    }

    private void fillJobFunctionField(final WebDriver driver) {
        DelayUtil.startOneSecondDelay();
        driver.findElement(By.xpath(TechStarsPageElementsPaths.JOB_FUNCTION_SEARCH_FIELD_XPATH))
                .sendKeys(TechStarsPageElementsPaths.JOB_FUNCTION_TO_DO_SCRAPING);
        DelayUtil.startOneSecondDelay();
    }

    private void chooseJobFunctionOptionFromDropdown(final WebDriver driver) {
        List<WebElement> jobFunctionsThatWereFoundAfterSearch = driver
                .findElements(By.xpath(TechStarsPageElementsPaths.JOB_FUNCTIONS_VARIANTS));
        for (WebElement jobFunctionFromDropdown : jobFunctionsThatWereFoundAfterSearch) {
            if (jobFunctionFromDropdown.getText().trim().equals(TechStarsPageElementsPaths.JOB_FUNCTION_TO_DO_SCRAPING)) {
                DelayUtil.startTwoSecondsDelay();
                jobFunctionFromDropdown.click();
                DelayUtil.startTwoSecondsDelay();
            }
        }
    }

    private void closeCookiesBlock(final WebDriver driver) {
        driver.findElement(By.xpath(TechStarsPageElementsPaths.BUTTON_TO_CLOSE_BLOCK_THAT_REQUIRED_COOKIES)).click();
    }

    private void extractAllVacanciesFromPage(final WebDriver driver) {
        final String numberOfVacanciesForParsing = driver.findElement(By.xpath(TechStarsPageElementsPaths.SEARCH_PAGE_NUMBER_OF_VACANCIES_LABEL_XPATH)).getText();
        Integer countOfPagesNeedToParse = Integer.parseInt(numberOfVacanciesForParsing.replace(",", "")) / 20;
        logger.info("Need to parse: {} pages on {} pages", numberOfVacanciesForParsing, countOfPagesNeedToParse);
        removeExtraElementsFromPage(driver);
        while (getCurrentPageNumber(driver) <= countOfPagesNeedToParse) {
            clickByLoadMoreButton(driver);
            if (getCurrentPageNumber(driver) > lastParsedPage) {
                lastParsedPage = getCurrentPageNumber(driver);
                logger.info("Current page number is: {}", getCurrentPageNumber(driver));
                DelayUtil.startFiveSecondsDelay();
                parseGeneralInformationAboutVacancies(driver);
            }
        }
        logger.info("List of vacancies was extracted.");
    }

    private void removeExtraElementsFromPage(final WebDriver driver) {
        logger.info("Started removing extra elements from page.");
        final WebElement firstHeader = driver.findElement(By.className(TechStarsPageElementsPaths.EXTRA_ELEMENT_FIRST_HEADER_XPATH));
        final WebElement secondHeader = driver.findElement(By.className(TechStarsPageElementsPaths.EXTRA_ELEMENT_SECOND_HEADER_XPATH));
        final WebElement firstFooter = driver.findElement(By.className(TechStarsPageElementsPaths.EXTRA_ELEMENT_FIRST_FOOTER_XPATH));
        final WebElement secondFooter = driver.findElement(By.className(TechStarsPageElementsPaths.EXTRA_ELEMENT_SECOND_FOOTER_XPATH));
        final String jsScriptToRemoveExtraElements = TechStarsPageElementsPaths.JS_SCRIPT_TO_REMOVE_WEB_ELEMENTS;
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsScriptToRemoveExtraElements, firstHeader);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsScriptToRemoveExtraElements, firstFooter);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsScriptToRemoveExtraElements, secondFooter);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(jsScriptToRemoveExtraElements, secondHeader);
        logger.info("Finished removing extra elements from page.");
    }

    private int getCurrentPageNumber(final WebDriver driver) {
        return extractPageNumber(driver.getCurrentUrl());
    }

    private static int extractPageNumber(final String url) {
        final int pageIndex = url.lastIndexOf(PAGE_PARAM);
        if (pageIndex != -1) {
            final String pageNumberStr = url.substring(pageIndex + PAGE_PARAM.length());
            try {
                return Integer.parseInt(pageNumberStr);
            } catch (NumberFormatException e) {
                logger.error("Error parsing page number: {}", pageNumberStr);
            }
        }
        return 1;
    }

    private void clickByLoadMoreButton(final WebDriver driver) {
        final Actions actions = new Actions(driver);
        actions.sendKeys(Keys.END).perform();
        DelayUtil.startTwoSecondsDelay();
        actions.sendKeys(Keys.END).perform();
        DelayUtil.startTwoSecondsDelay();
        actions.sendKeys(Keys.END).perform();
        try {
            Optional<WebElement> loadMoreButton = getLoadMoreButton(driver);
            loadMoreButton.ifPresent(WebElement::click);
            logger.info("Clicked by load more button.");
        } catch (Exception e) {
            logger.error("Error clicking load more button.");
        }
    }

    private void parseGeneralInformationAboutVacancies(final WebDriver driver) {
        logger.info("Started scraping all vacancies after opening the whole page.");
        final List<WebElement> vacanciesInWebElementRepresentation = driver.findElements(By.className(TechStarsPageElementsPaths.SEARCH_PAGE_VACANCY_BLOCK));
        final List<Vacancy> vacancies = vacanciesInWebElementRepresentation.stream()
                .map(vacancy -> fillVacancyDataBasedOnSearchPage(driver, vacancy))
                .collect(Collectors.toList());
        vacanciesThatWereParsed.addAll(vacancies);
        saveVacanciesInDB(vacancies);
        logger.info("Finished scraping all vacancies after opening the whole page.");
    }

    private Vacancy fillVacancyDataBasedOnSearchPage(final WebDriver driver, final WebElement vacancyRepresentation) {
        final Vacancy vacancy = new Vacancy();
        vacancy.setVacancyURL(extractVacancyURL(vacancyRepresentation));
        vacancy.setPositionName(extractVacancyName(vacancyRepresentation));
        vacancy.setTags(extractVacancyTags(vacancyRepresentation));
        removeWebElement(driver, vacancyRepresentation);
        return vacancy;
    }

    private void removeWebElement(final WebDriver driver, final WebElement webElement) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(TechStarsPageElementsPaths.JS_SCRIPT_TO_REMOVE_WEB_ELEMENT, webElement);
    }

    private void saveVacanciesInDB(final List<Vacancy> vacancies) {
        for (Vacancy vacancy : vacancies) {
            vacancies.removeAll(vacancyRepository.findAllByVacancyURL(vacancy.getVacancyURL()));
        }
        vacancyRepository.saveAll(vacancies);
    }

    private Optional<WebElement> getLoadMoreButton(final WebDriver driver) {
        try {
            return driver.findElements(By.className(TechStarsPageElementsPaths.SEARCH_PAGE_LOAD_MORE_BUTTON_CLASS_NAME))
                    .stream()
                    .filter(a -> a.getText().equals("Load more"))
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String extractVacancyURL(final WebElement webElement) {
        WebElement linkElement = webElement.findElement(By.cssSelector(TechStarsPageElementsPaths.SEARCH_PAGE_VACANCY_LINK_CSS_SELECTOR));
        return linkElement.getAttribute(TechStarsPageElementsPaths.HREF_ATTRIBUTE);
    }

    private static String extractVacancyName(final WebElement webElement) {
        WebElement linkElement = webElement.findElement(By.cssSelector(TechStarsPageElementsPaths.SEARCH_PAGE_VACANCY_NAME_CSS_SELECTOR));
        return linkElement.getText();
    }

    private List<Tag> extractVacancyTags(final WebElement webElement) {
        final List<WebElement> elements = webElement.findElements(By.cssSelector(TechStarsPageElementsPaths.SEARCH_PAGE_VACANCY_TAGS_CSS_SELECTOR));
        return elements.stream()
                .map(WebElement::getText)
                .map(this::createNewTagOrGetExisted)
                .collect(Collectors.toList());
    }

    private Tag createNewTagOrGetExisted(final String tagName) {
        final Optional<Tag> existingTag = tagRepository.findByName(tagName);
        return existingTag.orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName(tagName.trim());
            return tagRepository.save(newTag);
        });
    }
}
