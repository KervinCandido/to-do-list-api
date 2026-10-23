package br.com.kervincandido.portfolio.domain.model;

import br.com.kervincandido.portfolio.domain.exception.DateBeforeDueDateException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Task {

    private final Long id;
    private final String title;
    private final String description;
    private final Status status;
    private final Priority priority;
    private final Instant createdAt;
    private final LocalDate dueDate;
    private final Instant completedAt;

    private Task(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.status = builder.status;
        this.priority = builder.priority;
        this.createdAt = builder.createdAt;
        this.dueDate = builder.dueDate;
        this.completedAt = builder.completedAt;
    }

    public static Task create(Clock clock, String title, String description, Priority priority, LocalDate dueDate) {
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(priority, "priority cannot be null");
        Objects.requireNonNull(dueDate, "due date cannot be null");

        var now = LocalDate.now(clock);
        if (dueDate.isBefore(now)) {
            throw new DateBeforeDueDateException(now, dueDate);
        }

        return builder()
                .title(title)
                .description(description)
                .status(Status.TO_DO)
                .priority(priority)
                .createdAt(Instant.now(clock))
                .dueDate(dueDate)
                .build();
    }

    private static Builder builder () {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Optional<Instant> getCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    private static final class Builder {
        private Long id;
        private String title;
        private String description;
        private Status status;
        private Priority priority;
        private Instant createdAt;
        private LocalDate dueDate;
        private Instant completedAt;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder completedAt(Instant completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
