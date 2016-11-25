package com.inria.spirals.mgonzale.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Tracks the status of a long-running event.
 */
public final class Task {

    private final Long id;

    private final LocalDateTime startDate = LocalDateTime.now();

    private final Trigger trigger;

    private volatile TaskStatus status = TaskStatus.IN_PROGRESS;

    /**
     * Create a new task
     *
     * @param id      Unique id for the task
     * @param trigger The {@code Trigger} for the task
     */
    public Task(Long id, Trigger trigger) {
        this.id = id;
        this.trigger = trigger;
    }

    /**
     * Returns the id of the task
     *
     * @return the id of the task
     */
    @JsonIgnore
    public Long getId() {
        return this.id;
    }

    /**
     * Returns the start time of the task
     *
     * @return the start time of the task
     */
    public LocalDateTime getStart() {
        return this.startDate;
    }

    /**
     * Returns the status of the task
     *
     * @return the {@code TaskStatus} of the task
     */
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * Returns the trigger of the task
     *
     * @return the {@code Trigger} of the task
     */
    public Trigger getTrigger() {
        return this.trigger;
    }

    /**
     * Stops the task.
     */
    public void stop() {
        this.status = TaskStatus.COMPLETE;
    }

}
