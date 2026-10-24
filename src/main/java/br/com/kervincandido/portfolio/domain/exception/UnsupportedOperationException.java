package br.com.kervincandido.portfolio.domain.exception;

public class UnsupportedOperationException extends BusinessException {

    public UnsupportedOperationException(String message) {
        super(message);
    }

    public UnsupportedOperationException() {
        this("Unsupported Operation");
    }
}
