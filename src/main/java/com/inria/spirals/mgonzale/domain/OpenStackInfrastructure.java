package com.inria.spirals.mgonzale.domain;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public final class OpenStackInfrastructure implements Infrastructure {

    private final Token token;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    	   
	private int sshTimeout = 11000;
	@Value ("${ssh.user}")
	private String sshUser; 
	@Value ("${ssh.key}")
	private String sshKey; 
   
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
            if (group.getName().equals(groupName)) {
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
	   
	   public List<Server> findAllServers(){
			OSClientV3 osc = OSFactory.clientFromToken(this.token);

		   List<? extends Server> servers = osc.compute().servers().list();
		    
		   return servers.stream()
			.parallel()
			.filter(p -> p.getStatus() == Server.Status.ACTIVE )
			.sequential()
			.collect(Collectors.toList());

	   }
	   
	   @Override
	    public final Set<Member> getMembers() {
	        Set<Member> members = new HashSet<>();
	        
	        findAllServers().forEach(
	        		
	        		virtualMachine -> {
	                    String id = virtualMachine.getId();
	                    String job = virtualMachine.getInstanceName();
	                    String name = virtualMachine.getName();
	                    
	                    members.add(new Member(id, virtualMachine.getAvailabilityZone(), job, name, this));
	                }
	        		
	        		);

	        return members;
	    }

		@Override
		public SSHClient connectSsh(String instanceId) {
			   final SSHClient ssh = new SSHClient();
	           ssh.addHostKeyVerifier(
	                   new HostKeyVerifier() {
	                       @Override
	                       public boolean verify(String s, int i, PublicKey publicKey) {
	                           return true;
	                       }
	                   });
	           
	           try {
	    	       ssh.setConnectTimeout(sshTimeout);
	    	       ssh.setTimeout(sshTimeout);
	        	   ssh.connect(getIPv4Addr(instanceId));
	        	   ssh.authPublickey(sshUser, sshKey);   
	           } 
	           
	           catch (TransportException t ){
		    	   try {
						ssh.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       } catch (ConnectionException c) {

		    	   try {
						ssh.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       } catch (Exception e) {

		    	    try {
						ssh.disconnect();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		       }
	         	
	           return ssh;
		}
		
		public String getIPv4Addr (final String instanceId) {
			   OSClientV3 osc = OSFactory.clientFromToken(this.token);
			
			   Server s = osc.compute().servers().get(instanceId);
			   ArrayList<ArrayList<String>> listOLists = new ArrayList<ArrayList<String>>(); 
			   s.getAddresses().getAddresses().entrySet().stream().forEach( e ->
			   listOLists.add( e.getValue()
				   .stream()
				   .filter(p -> p.getVersion() == 4)
				   .map(p -> p.getAddr())
				   .collect(Collectors.toCollection(ArrayList::new)))
				   
			   );
			   
			   List<String> flat = 
					   listOLists.stream()
					        .flatMap(List::stream)
					        .collect(Collectors.toList());
			   
			   return flat.get(0);
		}
		   
    
}
