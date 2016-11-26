package com.inria.spirals.mgonzale.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.inria.spirals.mgonzale.services.OpenStackConnection;

abstract class AbstractDirectorUtilsInfrastructure implements Infrastructure {

	@Autowired
	private OpenStackConnection os;

    @Override
    public final Set<Member> getMembers() {
        Set<Member> members = new HashSet<>();
        
        os.findAllServers().forEach(
        		
        		virtualMachine -> {
                    String id = virtualMachine.getId();
                    String job = virtualMachine.getImageId();
                    String name = virtualMachine.getInstanceName();
                    
                    members.add(new Member(id, virtualMachine.getAvailabilityZone(), job, name));
                }
        		
        		
        		
        		);

        return members;
    }

 
}

