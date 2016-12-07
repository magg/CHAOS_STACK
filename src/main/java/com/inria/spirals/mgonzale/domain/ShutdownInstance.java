package com.inria.spirals.mgonzale.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ShutdownInstance {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    


    public void terminateNow(Member member) throws DestructionException {
    	
			member.getInfrastructure()
			.terminateInstance(member.getId());
    }
}
