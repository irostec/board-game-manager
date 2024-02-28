package com.irostec.boardgamemanager.common.utility;

import org.apache.logging.log4j.Logger;

/**
 * LoggingUtils
 * A repository of convenient methods to provide a uniform logging mechanism
 */
public final class LoggingUtils {

    private LoggingUtils() {}

    private static String expandedMessage(String methodName, String message) {
        return "Method " + methodName + ": " + message;
    }

    public static void info(Logger logger, String methodName, String message) {
        logger.info(expandedMessage(methodName, message));
    }

    public static void error(Logger logger, String methodName, Exception ex) {
        logger.error(expandedMessage(methodName, "Exception: "), ex);
    }

    public static void warn(Logger logger, String methodName, Exception ex) {
        logger.warn(expandedMessage(methodName, "Exception: "), ex);
    }

}
