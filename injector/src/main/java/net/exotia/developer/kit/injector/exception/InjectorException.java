package net.exotia.developer.kit.injector.exception;

public class InjectorException extends RuntimeException {

    public InjectorException(String message) {
        super(message);
    }

    public InjectorException(String message, Throwable cause) {
        super(message, cause);
    }
}