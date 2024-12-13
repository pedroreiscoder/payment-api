package io.ezycollect.paymentapi.exception;

public class AESEncryptionException extends RuntimeException {
    public AESEncryptionException(String message) {
        super(message);
    }
}
