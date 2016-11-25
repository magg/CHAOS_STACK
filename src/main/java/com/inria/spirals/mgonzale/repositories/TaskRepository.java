package com.inria.spirals.mgonzale.repositories;

import com.inria.spirals.mgonzale.domain.Task;
import com.inria.spirals.mgonzale.domain.Trigger;

public interface TaskRepository {

    /**
     * Creates a {@code Task}
     *
     * @param trigger The {@code Trigger} type of the task
     * @return The {@code Task}
     */
    Task create(Trigger trigger);

}
