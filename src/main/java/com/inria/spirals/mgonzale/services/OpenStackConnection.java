package com.inria.spirals.mgonzale.services;

import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Token;


@Component
public class OpenStackConnection {
	
	   private Token accessToken  = null;
	   
	   private int timeout = 10;
	   private int sshTimeout = 11000;
	   
	   @Value ("${openstack.username}")
	   private String username;
	   @Value ("${openstack.password}")
	   private String password;  
	   @Value ("${openstack.endpoint}")
	   private String apiUrl; 
	   @Value ("${openstack.projectID}")
	   private String projectID; 
	   @Value ("${openstack.domainName}")
	   private String domainName; 
	   
	   @Value ("${ssh.user}")
	   private String sshUser; 
	   @Value ("${ssh.key}")
	   private String sshKey; 

	
	   private Token getAccessToken ()
	   {
	      if (null == accessToken)
	      {
	         login ();
	      }

	      return accessToken;
	   }
	   
	   public void test(){
		   connect();
		   if (this.accessToken != null ){
			   OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);
			   
			   List<? extends Server> servers = osc.compute().servers().list();
			   			 			   
			   for (Server s : servers){
				   System.out.println(s.getId());
				   
				   s.getAddresses().getAddresses().forEach((key, value) -> {
					    System.out.println("Key : " + key + " Value : " + value);
					});

				   
				   //System.out.println(s.getAddresses().getAddresses().values().toArray() );
				   
			   }

		   } else {
			   System.out.println("no data");
		   }
	   }
	   
	   
	   public void terminateInstance(final String instanceId) {
		    connect();
	        if (this.accessToken != null ){
				   OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);
				   osc.compute().servers().action(instanceId, Action.STOP);    
	        }
	        
	    }
	   
	   public List<Server> findAllServers(){
		    connect();
			OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);

		   List<? extends Server> servers = osc.compute().servers().list();
		   
		   return servers.stream().collect(Collectors.toList());
	   }
	   
	    public String findSecurityGroup(final String instanceId,
	            final String groupName) {
	        String id = null;
	        connect();
			OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);

	        List<? extends SecGroupExtension> security_groups = osc.compute().securityGroups().list();
	        for (SecGroupExtension group: security_groups){
	            if (group.getName() == groupName) {
	                id = group.getId();
	                break;
	            }
	        }
	        return id;
	    }
	   
	   public void removeSecurityGroupFromInstnce (final String instanceId){
		   OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);
		   List<? extends SecGroupExtension> security_groups = osc.compute().securityGroups().listServerGroups(instanceId);
		   for (SecGroupExtension group: security_groups){
			   osc.compute().servers().removeSecurityGroup(instanceId,  group.getName());
		   }
	   }
	   
	   public String  createSecurityGroup(final String instanceId,
	            final String groupName, final String description) {
		   connect();
		   OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);
		   SecGroupExtension group = osc.compute().securityGroups().create(groupName, description);
		   
		   
		   return group.getId();
		   
	   }
	   
	   public void addSecurityGroupToInstance(final String instanceId,
       final String groupId){
		   OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);
		   SecGroupExtension group = osc.compute().securityGroups().get(groupId);
		   osc.compute().servers().addSecurityGroup(instanceId,  group.getName());
		   
	   }
	   
	   private void login (){
		   try {
			   OSClientV3 osc = OSFactory.builderV3()
                       .endpoint(apiUrl)
                       .credentials(username, password, Identifier.byName(domainName))
                       .scopeToProject(Identifier.byId(projectID))
                       .authenticate();
		 
		   this.accessToken = osc.getToken();
		   		   
		   }  catch (Exception crs) {
	         
	           System.out.println(crs.getMessage());
	        }

	   }
	   
	   public void connect(){
		   
		   try{
			   
			   login();
		   } catch (Exception crs) {
	         
			   login();
	        }
		   
	   }
	   
	   
	   public String connectSSH (final String instanceId){
		   
		   String result = "ERROR - couldn't connect";
	       int exitStat;
		   OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);
	       Map<String, String> md = osc.compute().servers().getMetadata(instanceId);
		   
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
        	   ssh.connect("");
        	   ssh.authPublickey(sshUser, sshKey);
 	    	   
 	    	   Session session = ssh.startSession();
               try {
            	   	            	   
            	   Command cmd1 = session.exec("");
                   cmd1.join(timeout+10, TimeUnit.SECONDS);
                   exitStat = cmd1.getExitStatus();
                   
                   if (exitStat == 0){
                 	 result = "OK";
                   } else {
                	 result ="ERROR - Snapshot missing";
                   }
                   
                   
                   
               } finally{
            	   session.close();
               }
               ssh.disconnect();
           } 
           
           
           catch (TransportException t ){
        	   result = t.getMessage();
	    	   try {
					ssh.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       } catch (ConnectionException c) {
        	   result = c.getMessage();

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
         	
           return result;
	   }
  

}
