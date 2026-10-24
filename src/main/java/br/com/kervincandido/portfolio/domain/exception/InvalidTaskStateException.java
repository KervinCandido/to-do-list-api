package br.com.kervincandido.portfolio.domain.exception;

public class InvalidTaskStateException extends BusinessException {

    public InvalidTaskStateException(String message) {
        super(message);
    }

    public InvalidTaskStateException() {
        this("The task is in an invalid state.");
    }
}
