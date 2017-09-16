package org.opl.memory;

/**
 * Allocator exception
 */
public class AllocatorException extends RuntimeException {

    public AllocatorException() {
    }

    public AllocatorException(String message) {
        super(message);
    }

    public AllocatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllocatorException(Throwable cause) {
        super(cause);
    }

    public AllocatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
