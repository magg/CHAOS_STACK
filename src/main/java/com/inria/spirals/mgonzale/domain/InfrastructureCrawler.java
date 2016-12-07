package com.inria.spirals.mgonzale.domain;

import java.util.Set;

public interface InfrastructureCrawler {
	

	/**
     * Destroys a {@link Member}
     *
     * @param member The member to destroy
     * @throws DestructionException
     */
    void destroy(Member member) throws DestructionException;



}
