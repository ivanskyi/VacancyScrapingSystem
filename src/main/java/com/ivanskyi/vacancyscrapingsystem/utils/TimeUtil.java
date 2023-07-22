package com.ivanskyi.vacancyscrapingsystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);
    private static final String PATTERN_TO_PARSE_DATE = "EEEE, MMMM dd, yyyy";
    private static final String ERROR_MESSAGE = "Got an error when trying to parse: {}.";
    private static final int DEFAULT_VALUE = 0;
    private static final int MILLIS_IN_ONE_SECOND = 1000;

    public static long convertToUnixTimestamp(final String dateString) {
        try {
            final SimpleDateFormat format = new SimpleDateFormat(PATTERN_TO_PARSE_DATE, Locale.US);
            return format.parse(dateString).getTime() / MILLIS_IN_ONE_SECOND;
        } catch (ParseException e) {
            logger.error(ERROR_MESSAGE, dateString);
            return DEFAULT_VALUE;
        }
    }
}
