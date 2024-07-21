package com.irostec.boardgamemanager.common.utility;

import org.apache.logging.log4j.Logger;

/**
 * LoggingUtils
 * A repository of convenient methods to provide a uniform logging mechanism
 */
public final class Logging {

    private Logging() {}

    private static String expandedMessage(String methodName, String message) {
        return "Method " + methodName + ": " + message;
    }

    public static void info(Logger logger, String methodName, String message, Object... objects) {
        logger.info(expandedMessage(methodName, message), objects);
    }

    public static void info(Logger logger, String methodName, String message) {
        logger.info(expandedMessage(methodName, message));
    }

    public static void error(Logger logger, String methodName, Throwable exception) {
        error(logger, methodName, "Exception: ", exception);
    }

    public static void error(Logger logger, String methodName, String message) {
        logger.error(expandedMessage(methodName, message));
    }

    public static void error(Logger logger, String methodName, String message, Throwable exception) {
        logger.error(expandedMessage(methodName, message), exception);
    }

    public static void warn(Logger logger, String methodName, Throwable exception) {
        warn(logger, expandedMessage(methodName, "Exception: "), exception);
    }

    public static void warn(Logger logger, String methodName, String message, Throwable exception) {
        logger.warn(expandedMessage(methodName, message), exception);
    }

}
