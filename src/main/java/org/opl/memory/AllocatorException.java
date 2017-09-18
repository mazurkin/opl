package org.opl.memory;

/**
 * Allocator exception
 */
public class AllocatorException extends RuntimeException {

    public AllocatorException(String message) {
        super(message);
    }

    public AllocatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
