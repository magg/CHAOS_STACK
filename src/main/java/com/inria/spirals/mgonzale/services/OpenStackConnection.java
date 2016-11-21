package com.inria.spirals.mgonzale.services;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;

@Component
public class OpenStackConnection {
	
		private Token accessToken  = null;
	   
	   @Value ("${openstack.username}")
	   private String username;
	   @Value ("${openstack.password}")
	   private String password;  
	   @Value ("${openstack.url}")
	   private String apiUrl; 
	   @Value ("${openstack.tenant}")
	   private String tenant; 
	
	   private Token getAccessToken ()
	   {
	      if (null == accessToken)
	      {
	         login ();
	      }

	      return accessToken;
	   }
	   
	   public void test(){
		   getAccessToken ();
		   System.out.println(this.accessToken.toString());
	   }
 
	   private void login (){
		   OSClientV3 os = OSFactory.builderV3()
                   .endpoint(apiUrl)
                   .credentials(username, password)
                   .scopeToDomain(Identifier.byName(tenant))
                   .authenticate();
		   this.accessToken = os.getToken();
	   }


}
