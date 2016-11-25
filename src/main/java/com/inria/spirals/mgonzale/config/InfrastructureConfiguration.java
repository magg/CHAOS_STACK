package com.inria.spirals.mgonzale.config;

import com.inria.spirals.mgonzale.domain.DirectorUtils;
import com.inria.spirals.mgonzale.domain.OpenStackInfrastructure;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class InfrastructureConfiguration {

    @Autowired
    DirectorUtils directorUtils;
/*
    @Bean
    @ConditionalOnProperty("aws.accessKeyId")
    AmazonEC2Client amazonEC2(@Value("${aws.accessKeyId}") String accessKeyId,
                              @Value("${aws.secretAccessKey}") String secretAccessKey,
                              @Value("${aws.region:us-east-1}") String regionName) {

        AmazonEC2Client amazonEC2Client = new AmazonEC2Client(new BasicAWSCredentials(accessKeyId, secretAccessKey));
        Region region = Region.getRegion(Regions.fromName(regionName));
        amazonEC2Client.setEndpoint(region.getServiceEndpoint("ec2"));

        return amazonEC2Client;
    }

    @Bean
    @ConditionalOnBean(AmazonEC2.class)
    AwsInfrastructure awsInfrastructure(DirectorUtils directorUtils, AmazonEC2 amazonEC2) {
        return new AwsInfrastructure(directorUtils, amazonEC2);
    }

    @Bean
    @ConditionalOnProperty("vsphere.host")
    StandardInventoryNavigatorFactory inventoryNavigatorFactory(@Value("${vsphere.host}") String host,
                                                                @Value("${vsphere.username}") String username,
                                                                @Value("${vsphere.password}") String password) throws MalformedURLException {

        return new StandardInventoryNavigatorFactory(host, username, password);
    }
*/
    @Bean
    @ConditionalOnProperty("openstack.endpoint")
    Token novaApi(@Value("${openstack.endpoint}") String endpoint,
                    @Value("${openstack.domainName}") String projectID,
                    @Value("${openstack.domainName}") String domainName,
                    @Value("${openstack.username}") String username,
                    @Value("${openstack.password}") String password) {

    	Token token = null;
    	
    	try {
			   OSClientV3 osc = OSFactory.builderV3()
                    .endpoint(endpoint)
                    .credentials(username, password, Identifier.byName(domainName))
                    .scopeToProject(Identifier.byId(projectID))
                    .authenticate();
		 
			   token = osc.getToken();
			   
			   
		   }  catch (Exception crs) {
	         
	           System.out.println(crs.getMessage());
	        }

    	
    	return token;

    }

    @Bean
    @ConditionalOnBean(Token.class)
    OpenStackInfrastructure openStackInfrastructure(DirectorUtils directorUtils, Token token) {
        return new OpenStackInfrastructure(directorUtils, token);
    }
/*
    @Bean
    @ConditionalOnProperty("simple.infrastructure")
    SimpleInfrastructure simpleInfrastructure() {
        return new SimpleInfrastructure();
    }

    @Bean
    @ConditionalOnBean(InventoryNavigatorFactory.class)
    VSphereInfrastructure vSphereInfrastructure(DirectorUtils directorUtils, InventoryNavigatorFactory inventoryNavigatorFactory) {
        return new VSphereInfrastructure(directorUtils, inventoryNavigatorFactory);
    }
*/
}
