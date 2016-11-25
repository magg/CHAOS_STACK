package com.inria.spirals.mgonzale.domain;

import java.util.Map;
import java.util.Set;

public interface DirectorUtils {

    /**
     * Returns a list of deployments on the Director
     *
     * @return a list of deployments on the Director
     */
    Set<String> getDeployments();

    /**
     * Returns a list of VMs in a deployment
     *
     * @param deployment the deployment
     * @return a list of VMs in a deployment
     */
    Set<Map<String, String>> getVirtualMachines(String deployment);

}