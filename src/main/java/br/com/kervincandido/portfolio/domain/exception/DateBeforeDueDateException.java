package br.com.kervincandido.portfolio.domain.exception;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class DateBeforeDueDateException extends RuntimeException {

    public DateBeforeDueDateException(LocalDate date, LocalDate dueDate) {
        super(String.format("Date %s is before the due date %s", ISO_LOCAL_DATE.format(date), ISO_LOCAL_DATE.format(dueDate)));
    }
}
