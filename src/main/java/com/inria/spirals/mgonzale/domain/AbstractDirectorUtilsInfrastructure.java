package com.inria.spirals.mgonzale.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractDirectorUtilsInfrastructure implements Infrastructure {

    private static final Pattern DEPLOYMENT_PATTERN = Pattern.compile("(.+)-[^-]+");

    private static final Pattern JOB_PATTERN = Pattern.compile("(.+)-partition-.+");

    private final DirectorUtils directorUtils;

    AbstractDirectorUtilsInfrastructure(DirectorUtils directorUtils) {
        this.directorUtils = directorUtils;
    }

    @Override
    public final Set<Member> getMembers() {
        Set<Member> members = new HashSet<>();

        this.directorUtils.getDeployments().stream().forEach(deployment -> {
            String normalizedDeployment = normalizeDeployment(deployment);

            this.directorUtils.getVirtualMachines(deployment).stream()
                .forEach(virtualMachine -> {
                    String id = virtualMachine.get("cid");
                    String job = normalizeJob(virtualMachine.get("job"));
                    String name = String.format("%s/%s", virtualMachine.get("job"), virtualMachine.get("index"));

                    members.add(new Member(id, normalizedDeployment, job, name));
                });
        });

        return members;
    }

    private static String normalize(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.find() ? matcher.group(1) : value;
    }

    private String normalizeDeployment(String value) {
        return normalize(value, DEPLOYMENT_PATTERN);
    }

    private String normalizeJob(String value) {
        return normalize(value, JOB_PATTERN);
    }
}

