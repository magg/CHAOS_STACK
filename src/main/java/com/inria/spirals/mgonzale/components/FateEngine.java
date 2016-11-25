package com.inria.spirals.mgonzale.components;

import com.inria.spirals.mgonzale.domain.Member;

public interface FateEngine {

    /**
     * Determines whether a {@link Member} should live or die
     *
     * @param member The {@link Member} to evaluate
     * @return Whether a member should live or die
     */
    Boolean shouldDie(Member member);

}
