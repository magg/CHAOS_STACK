package com.inria.spirals.mgonzale.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class BlockAllNetworkTraffic {
	


      private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	   
	   public void blockAllNetworkTraffic(Member member){
		   

		   String blockedSecurityGroupName = "blocked-network";

	        String groupId = member.getInfrastructure().findSecurityGroup(member.getId(), blockedSecurityGroupName );

	        if (groupId == null) {
	        	logger.info("Auto-creating security group {}", blockedSecurityGroupName);

	            String description = "Empty security group for blocked instances";
	            groupId = member.getInfrastructure().createSecurityGroup(member.getId(), blockedSecurityGroupName, description);
	        }

	        logger.info("Blocking network traffic by applying security group {} to instance {}", groupId, member.getId());

	        List<String> groups = Lists.newArrayList();
	        groups.add(groupId);
	        member.getInfrastructure().setInstanceSecurityGroups(member.getId(), groups);
	   }
}


