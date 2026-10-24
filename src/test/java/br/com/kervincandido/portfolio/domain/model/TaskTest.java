package br.com.kervincandido.portfolio.domain.model;

import br.com.kervincandido.portfolio.domain.exception.DateBeforeDueDateException;
import br.com.kervincandido.portfolio.domain.exception.InvalidTaskStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    private static final ZoneId SAO_PAULO = ZoneId.of("America/Sao_Paulo");
    private static final String NOW_INSTANT_TEXT = "2026-10-22T15:00:00Z";
    private static final Clock CLOCK = Clock.fixed(Instant.parse(NOW_INSTANT_TEXT), SAO_PAULO);
    private static final LocalDate TODAY = LocalDate.now(CLOCK);
    private static final LocalDate FUTURE_DUE_DATE = LocalDate.now(CLOCK).plusDays(7);

    private static final String TASK_TITLE = "task title";
    private static final String TASK_DESCRIPTION = "task description";

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
                    .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), null , TASK_DESCRIPTION, Priority.MEDIUM, LocalDate.now()))
                    .withMessage("title cannot be null");
            }

            @Test
            @DisplayName("throws NullPointerException when description is null")
            void throwsWhenDescriptionIsNull() {
                assertThatNullPointerException()
                        .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), TASK_TITLE, null, Priority.MEDIUM, LocalDate.now()))
                        .withMessage("description cannot be null");
            }

            @Test
            @DisplayName("throws NullPointerException when priority is null")
            void throwsWhenPriorityIsNull() {
                assertThatNullPointerException()
                        .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), TASK_TITLE, TASK_DESCRIPTION, null, LocalDate.now()))
                        .withMessage("priority cannot be null");
            }

            @Test
            @DisplayName("throws NullPointerException when due date is null")
            void throwsWhenDueDateIsNull() {
                assertThatNullPointerException()
                        .isThrownBy(() -> Task.create(Clock.systemDefaultZone(), TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, null))
                        .withMessage("due date cannot be null");
            }
        }

        @Nested
        @DisplayName("with invalid data")
        class InvalidData{
            @Test
            @DisplayName("throws DateBeforeDueDateException when dueDate is yesterday")
            void throwsWhenDueDateIsInThePast() {
                var yesterday = TODAY.minusDays(1);

                assertThatExceptionOfType(DateBeforeDueDateException.class)
                        .isThrownBy(() -> Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, yesterday));
            }
        }

        @Nested
        @DisplayName("with valid data")
        class ValidData {

            @Test
            @DisplayName("creates task with TO_DO status and no completion date")
            void createsTaskInitialState() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.HIGH, FUTURE_DUE_DATE);

                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
                assertThat(task.getStatus()).isEqualTo(Status.TO_DO);
                assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
                assertThat(task.getCreatedAt()).isNotNull().isEqualTo(Instant.parse(NOW_INSTANT_TEXT));
                assertThat(task.getDueDate()).isEqualTo(FUTURE_DUE_DATE);
                assertThat(task.getCompletedAt()).isEmpty();
            }

            @Test
            @DisplayName("accepts dueDate equal to today")
            void acceptsDueDateToday() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, TODAY);
                assertThat(task.getDueDate()).isEqualTo(TODAY);
            }
        }
    }

    @Nested
    @DisplayName("behavior")
    public class Behavior {

        @Nested
        @DisplayName("doing")
        public class Doing {

            @DisplayName("change the status to DOING without changing other fields.")
            @Test
            void changeStatusToDoing() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, FUTURE_DUE_DATE);
                task.doing();

                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
                assertThat(task.getPriority()).isEqualTo(Priority.MEDIUM);
                assertThat(task.getCreatedAt()).isEqualTo(Instant.parse(NOW_INSTANT_TEXT));
                assertThat(task.getDueDate()).isEqualTo(FUTURE_DUE_DATE);
                assertThat(task.getStatus()).isEqualTo(Status.DOING);
            }

            @DisplayName("throws InvalidTaskStateException when task is already in process")
            @Test
            void throwsWhenTaskAlreadyDoing() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, FUTURE_DUE_DATE);
                task.doing();

                assertThatExceptionOfType(InvalidTaskStateException.class)
                        .isThrownBy(task::doing)
                        .withMessage("Task is already in progress");
            }

            @DisplayName("throws InvalidTaskStateException when task is already completed")
            @Test
            void throwsWhenTaskAlreadyCompleted() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, FUTURE_DUE_DATE);
                task.doing();
                task.done(CLOCK);

                assertThatExceptionOfType(InvalidTaskStateException.class)
                        .isThrownBy(task::doing)
                        .withMessage("Task is already completed");
            }
        }

        @Nested
        @DisplayName("done")
        public class Done {

            private static final String TASK_TITLE = "task title";
            private static final String TASK_DESCRIPTION = "task description";

            @DisplayName("change the status to DONE without changing other fields.")
            @Test
            void changeStatusDone() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, FUTURE_DUE_DATE);
                task.doing();
                task.done(CLOCK);

                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(task.getDescription()).isEqualTo(TASK_DESCRIPTION);
                assertThat(task.getPriority()).isEqualTo(Priority.MEDIUM);
                assertThat(task.getCreatedAt()).isEqualTo(Instant.parse(NOW_INSTANT_TEXT));
                assertThat(task.getDueDate()).isEqualTo(FUTURE_DUE_DATE);
                assertThat(task.getStatus()).isEqualTo(Status.DONE);
                assertThat(task.getCompletedAt()).isNotEmpty().isEqualTo(Optional.of(Instant.parse(NOW_INSTANT_TEXT)));
            }

            @DisplayName("throws InvalidTaskStateException when task is still to do")
            @Test
            void throwsWhenTaskStillToDo() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, FUTURE_DUE_DATE);
                assertThatExceptionOfType(InvalidTaskStateException.class)
                        .isThrownBy(() -> task.done(CLOCK))
                        .withMessage("Task is still to do");
            }

            @DisplayName("throws InvalidTaskStateException when task is already completed")
            @Test
            void throwsWhenTaskAlreadyCompleted() {
                var task = Task.create(CLOCK, TASK_TITLE, TASK_DESCRIPTION, Priority.MEDIUM, FUTURE_DUE_DATE);
                task.doing();
                task.done(CLOCK);

                assertThatExceptionOfType(InvalidTaskStateException.class)
                        .isThrownBy(() -> task.done(CLOCK))
                        .withMessage("Task is already completed");
            }
        }
    }
}