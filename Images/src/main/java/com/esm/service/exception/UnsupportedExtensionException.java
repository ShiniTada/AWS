package com.epam.esm.service.exception;

public class UnsupportedExtensionException extends ServiceException {

    public UnsupportedExtensionException() {
    }

    public UnsupportedExtensionException(String message) {
        super(message);
    }

    public UnsupportedExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedExtensionException(Throwable cause) {
        super(cause);
    }
}
