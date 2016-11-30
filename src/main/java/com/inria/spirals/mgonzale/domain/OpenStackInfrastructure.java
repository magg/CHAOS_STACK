package com.inria.spirals.mgonzale.domain;

import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public final class OpenStackInfrastructure extends InfrastructureCrawler {

    private final Token token;

    public OpenStackInfrastructure(Token token) {
        this.token = token;
    }

    public void terminateInstance(final String instanceId) throws DestructionException {
        try {
            if (this.token != null ){
        		OSClientV3 osc = OSFactory.clientFromToken(this.token);
    			osc.compute().servers().action(instanceId, Action.STOP);    
            }
        } catch (Exception e) {
            throw new DestructionException(String.format("Unable to destroy %s", instanceId), e);
        }
    }
    
    
    public String findSecurityGroup(final String instanceId,
            final String groupName) {
        String id = null;
		OSClientV3 osc = OSFactory.clientFromToken(this.token);

        List<? extends SecGroupExtension> security_groups = osc.compute().securityGroups().list();
        for (SecGroupExtension group: security_groups){
            if (group.getName() == groupName) {
                id = group.getId();
                break;
            }
        }
        return id;
    }
    
    public String  createSecurityGroup(final String instanceId,
            final String groupName, final String description) {
	   OSClientV3 osc = OSFactory.clientFromToken(this.token);
	   SecGroupExtension group = osc.compute().securityGroups().create(groupName, description);
	   
	   
	   return group.getId();
	   
   }
    
    
    public void setInstanceSecurityGroups(final String instanceId,
            final List<String> groupIds) {
    	
 	   	OSClientV3 osc = OSFactory.clientFromToken(this.token);
 	    removeSecurityGroupFromInstance(instanceId);    	
        for (final String groupId : groupIds) {
        	addSecurityGroupToInstance(instanceId, groupId);
        }
    }
    
    
	 public void removeSecurityGroupFromInstance (final String instanceId){
		   OSClientV3 osc = OSFactory.clientFromToken(this.token);
		   List<? extends SecGroupExtension> security_groups = osc.compute().securityGroups().listServerGroups(instanceId);
		   for (SecGroupExtension group: security_groups){
			   osc.compute().servers().removeSecurityGroup(instanceId,  group.getName());
		   }
	   }
	   
	   public void addSecurityGroupToInstance(final String instanceId,
       final String groupId){
		   OSClientV3 osc = OSFactory.clientFromToken(this.token);
		   SecGroupExtension group = osc.compute().securityGroups().get(groupId);
		   osc.compute().servers().addSecurityGroup(instanceId,  group.getName());
		   
	   }

	
    
}
