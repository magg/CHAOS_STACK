package com.inria.spirals.mgonzale.domain;

import org.springframework.util.Assert;

/**
 * Represents a running instance
 */
public final class Member implements Comparable<Member> {

    private final String deployment;

    private final String id;

    private final String job;

    private final String name;
    
    private final Infrastructure infrastructure;
    /**
     * Creates an instance
     *
     * @param id         the ID of the {@link Member}
     * @param deployment the deployment the {@link Member} belongs to
     * @param job        the job the {@link Member} belongs to
     * @param name       the name of the {@link Member}
     */
    public Member(String id, String deployment, String job, String name, Infrastructure infrastructure) {
        Assert.hasText(id, "id must have text");
        Assert.hasText(deployment, "deployment must have text");
        Assert.hasText(job, "job must have text");
        Assert.hasText(name, "name must have text");

        this.id = id;
        this.deployment = deployment;
        this.job = job;
        this.name = name;
        this.infrastructure = infrastructure;
    }

    @Override
    public int compareTo(Member member) {
        return this.name.compareTo(member.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return name == null ? member.name == null : name.equals(member.name);
    }

    /**
     * Returns the deployment the {@link Member} belongs to
     *
     * @return the deployment the {@link Member} belongs to
     */
    public String getDeployment() {
        return this.deployment;
    }

    /**
     * Returns the ID of the {@link Member}
     *
     * @return the ID of the {@link Member}
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the job the {@link Member} belongs to
     *
     * @return the job the {@link Member} belongs to
     */
    public String getJob() {
        return this.job;
    }

    /**
     * Returns the name of the {@link Member}
     *
     * @return the name of the {@link Member}
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("[id: %s, deployment: %s, job: %s, name: %s]", this.id, this.deployment, this.job, this.name);
    }

	public Infrastructure getInfrastructure() {
		return infrastructure;
	}

}

