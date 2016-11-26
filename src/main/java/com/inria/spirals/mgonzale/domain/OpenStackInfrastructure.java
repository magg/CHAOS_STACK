package com.inria.spirals.mgonzale.domain;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

public final class OpenStackInfrastructure extends AbstractDirectorUtilsInfrastructure {

    private final Token token;

    public OpenStackInfrastructure(Token token) {
        this.token = token;
    }

    @Override
    public void destroy(Member member) throws DestructionException {
        try {
        	terminateInstance(member.getId());
        } catch (Exception e) {
            throw new DestructionException(String.format("Unable to destroy %s", member), e);
        }
    }
    
    public void terminateInstance(final String instanceId) {
        if (this.token != null ){
    		OSClientV3 osc = OSFactory.clientFromToken(this.token);
			osc.compute().servers().action(instanceId, Action.STOP);    
        }
        
    }
}
