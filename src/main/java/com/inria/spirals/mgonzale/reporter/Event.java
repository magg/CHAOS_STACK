package com.inria.spirals.mgonzale.reporter;

import java.util.List;
import java.util.UUID;

import com.inria.spirals.mgonzale.domain.Member;

/**
 * Encapsulates information about an event that should be reported
 */
public final class Event {

    private final UUID identifier;

    private final List<Member> members;

    /**
     * Create a new instance
     *
     * @param identifier the identifier of the event
     * @param members    the list of members destroyed during this event
     */
    public Event(UUID identifier, List<Member> members) {
        this.identifier = identifier;
        this.members = members;
    }

    /**
     * Returns the identifier of the event
     *
     * @return the identifier of the event
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the list of members destroyed during this event
     *
     * @return the list of members destroyed during this event
     */
    public List<Member> getMembers() {
        return this.members;
    }

}
