package com.inria.spirals.mgonzale.components;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import com.inria.spirals.mgonzale.controllers.TaskManager;
import com.inria.spirals.mgonzale.domain.Task;
import com.inria.spirals.mgonzale.domain.TaskUriBuilder;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public final class TaskResourceAssembler implements ResourceAssembler<Task, Resource<Task>>, TaskUriBuilder {

    @Override
    public URI getUri(Task task) {
        return getTaskLinkBuilder(task).toUri();
    }

    @Override
    public Resource<Task> toResource(Task task) {
        Resource<Task> resource = new Resource<>(task);

        resource.add(getTaskLinkBuilder(task).withSelfRel());

        return resource;
    }

    private ControllerLinkBuilder getTaskLinkBuilder(Task task) {
        return linkTo(TaskManager.class).slash(task.getId());
    }

}

