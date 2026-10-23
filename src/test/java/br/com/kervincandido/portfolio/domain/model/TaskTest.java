package br.com.kervincandido.portfolio.domain.model;

import br.com.kervincandido.portfolio.domain.exception.DateBeforeDueDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    private static final ZoneId SAO_PAULO = ZoneId.of("America/Sao_Paulo");
    private static final String INSTANT_TEXT = "2026-10-22T15:00:00Z";
    private static final Clock CLOCK = Clock.fixed(Instant.parse(INSTANT_TEXT), SAO_PAULO);
    private static final LocalDate TODAY = LocalDate.now(CLOCK);
    private static final LocalDate FUTURE_DUE_DATE = LocalDate.now(CLOCK).plusDays(7);

    @Nested
    @DisplayName("create")
    public class Create {

        @Nested
        @DisplayName("with null required fields")
        class NullFields {

            @Test
            @DisplayName("throws NullPointerException when title is null")
            void throwsWhenTitleIsNull() {
                assertThatNullPointerException()
                    .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), null , "task description", Priority.MEDIUM, LocalDate.now()))
                    .withMessage("title cannot be null");
            }

            @Test
            @DisplayName("throws NullPointerException when description is null")
            void throwsWhenDescriptionIsNull() {
                assertThatNullPointerException()
                        .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), "title" , null, Priority.MEDIUM, LocalDate.now()))
                        .withMessage("description cannot be null");
            }

            @Test
            @DisplayName("throws NullPointerException when priority is null")
            void throwsWhenPriorityIsNull() {
                assertThatNullPointerException()
                        .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), "title" , "task description", null, LocalDate.now()))
                        .withMessage("priority cannot be null");
            }

            @Test
            @DisplayName("throws NullPointerException when due date is null")
            void throwsWhenDueDateIsNull() {
                assertThatNullPointerException()
                        .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), "title" , "task description", Priority.MEDIUM, null))
                        .withMessage("due date cannot be null");
            }

            @Test
            @DisplayName("throws DateBeforeDueDateException when dueDate is yesterday")
            void throwsWhenDueDateIsInThePast() {
                var yesterday = TODAY.minusDays(1);

                assertThatThrownBy(() -> Task.create(CLOCK, "title", "description", Priority.MEDIUM, yesterday))
                        .isInstanceOf(DateBeforeDueDateException.class);
            }

            @Test
            @DisplayName("accepts dueDate equal to today")
            void acceptsDueDateToday() {
                var task = Task.create(CLOCK, "title", "description", Priority.MEDIUM, TODAY);
                assertThat(task.getDueDate()).isEqualTo(TODAY);
            }

        }

        @Nested
        @DisplayName("with valid data")
        class ValidData {
            @Test
            @DisplayName("creates task with TO_DO status and no completion date")
            void createsTaskInitialState() {
                var task = Task.create(CLOCK, "title", "description", Priority.HIGH, FUTURE_DUE_DATE);

                assertThat(task.getTitle()).isEqualTo("title");
                assertThat(task.getDescription()).isEqualTo("description");
                assertThat(task.getStatus()).isEqualTo(Status.TO_DO);
                assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
                assertThat(task.getCreatedAt()).isNotNull().isEqualTo(Instant.parse(INSTANT_TEXT));
                assertThat(task.getDueDate()).isEqualTo(FUTURE_DUE_DATE);
                assertThat(task.getCompletedAt()).isEmpty();
            }
        }
    }
}