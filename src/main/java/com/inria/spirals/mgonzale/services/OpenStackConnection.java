package com.inria.spirals.mgonzale.services;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import org.openstack4j.model.compute.Server;
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

	
	   public Token getAccessToken ()
	   {
	      if (null == accessToken || accessToken.getExpires().before(new Date()))
	      {
	         login ();
	      }
	      
	      return accessToken;
	   }
	   
	   public void test(){
		   getAccessToken();
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
	   
	   
	   
	   public List<Server> findAllServers(){
		    getAccessToken();
			OSClientV3 osc = OSFactory.clientFromToken(this.accessToken);

		   List<? extends Server> servers = osc.compute().servers().list();
		    
		   return servers.stream()
			.parallel()
			.filter(p -> p.getStatus() == Server.Status.ACTIVE )
			.sequential()
			.collect(Collectors.toList());

	   }
	   
	   
	   private void login (){
		   try {
			   this.accessToken = connect();   
		   }  catch (Exception crs) {
	           System.out.println(crs.getMessage());
	           this.accessToken = connect();
	        }
	   }
	   
	   private Token connect(){
		   
		   OSClientV3 osc = OSFactory.builderV3()
                   .endpoint(apiUrl)
                   .credentials(username, password, Identifier.byName(domainName))
                   .scopeToProject(Identifier.byId(projectID))
                   .authenticate();
		   
		   return osc.getToken();
		   
	   }
	   
	   
	   public String connectSSH (final String instanceId){
		   String result = null;
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
