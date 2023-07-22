package com.ivanskyi.vacancyscrapingsystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayUtil {
    private static final Logger logger = LoggerFactory.getLogger(DelayUtil.class);
    private static final int MILLIS_FOR_ONE_SECOND = 1000;
    private static final int MILLIS_FOR_TWO_SECONDS = 2000;
    private static final int MILLIS_FOR_FIVE_SECONDS = 5000;
    private static final String ERROR_MESSAGE = "Got an error when trying to do a delay.";

    public static void startOneSecondDelay() {
        try {
            Thread.sleep(MILLIS_FOR_ONE_SECOND);
        } catch (InterruptedException e) {
            logger.error(ERROR_MESSAGE);
        }
    }

    public static void startTwoSecondsDelay() {
        try {
            Thread.sleep(MILLIS_FOR_TWO_SECONDS);
        } catch (InterruptedException e) {
            logger.error(ERROR_MESSAGE);
        }
    }

    public static void startFiveSecondsDelay() {
        try {
            Thread.sleep(MILLIS_FOR_FIVE_SECONDS);
        } catch (InterruptedException e) {
            logger.error(ERROR_MESSAGE);
        }
    }
}
