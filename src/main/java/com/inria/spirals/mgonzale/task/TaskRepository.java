package com.inria.spirals.mgonzale.task;

public interface TaskRepository {

    /**
     * Creates a {@code Task}
     *
     * @param trigger The {@code Trigger} type of the task
     * @return The {@code Task}
     */
    Task create(Trigger trigger);

}
