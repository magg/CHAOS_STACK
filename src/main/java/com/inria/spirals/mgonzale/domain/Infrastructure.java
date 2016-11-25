package com.inria.spirals.mgonzale.domain;

import java.util.Set;

/**
 * An abstraction for interfacing with multiple infrastructures.
 */
public interface Infrastructure {

    /**
     * Destroys a {@link Member}
     *
     * @param member The member to destroy
     * @throws DestructionException
     */
    void destroy(Member member) throws DestructionException;

    /**
     * Returns a {@link Set} of all {@link Member}s
     *
     * @return a {@link Set} of all {@link Member}s
     */
    Set<Member> getMembers();

}