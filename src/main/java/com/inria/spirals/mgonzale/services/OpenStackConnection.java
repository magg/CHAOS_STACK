package com.inria.spirals.mgonzale.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v3.Token;


@Component
public class OpenStackConnection {
	
	   private Token accessToken  = null;
	   
	   
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
			   
			   Server s = osc.compute().servers().get("73bb0591-8f01-47c4-bdd2-56d0dd59f090");
			   System.out.println(s.toString());
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
			   
			   
			   for (String s1 : flat  ) {
				   System.out.println(s1);
			   }

				   
				   //System.out.println(s.getAddresses().getAddresses().values().toArray() );
				   
			 

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
	   
  

}
