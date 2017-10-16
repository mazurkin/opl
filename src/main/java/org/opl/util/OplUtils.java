package org.opl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OplUtils {

    private static final Logger LOGGER_CLOSE = LoggerFactory.getLogger(OplUtils.class.getName() + ".close");

    private OplUtils() {
        // nothing to do in an utility class
    }

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LOGGER_CLOSE.debug("Exception on close", e);
            }
        }
    }

    public static void checkNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkGreaterThanZero(long v, String message) {
        if (v <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkGreaterOrEqualZero(long v, String message) {
        if (v < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
