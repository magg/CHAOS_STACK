package com.inria.spirals.mgonzale.domain;

import java.util.List;
import java.util.Set;

import net.schmizz.sshj.SSHClient;

/**
 * An abstraction for interfacing with multiple infrastructures.
 */
public interface Infrastructure {

    /**
     * Destroys a {@link Member}
     *
     * @param member The member to destroy
     * @throws DestructionException
     */
    void terminateInstance(String instanceId) throws DestructionException;
    
    
    /**
     * Finds a security group with the given name, that can be applied to the given instance.
     *
     * For example, if it is a VPC instance, it makes sure that it is in the same VPC group.
     *
     * @param instanceId
     *            the instance that the group must be applied to
     * @param groupName
     *            the name of the group to find
     *
     * @return The group id, or null if not found
     */
    String findSecurityGroup(String instanceId, String groupName);

    /**
     * Creates an (empty) security group, that can be applied to the given instance.
     *
     * @param instanceId
     *            instance that group should be applicable to
     * @param groupName
     *            name for new group
     * @param description
     *            description for new group
     *
     * @return the id of the security group
     */
    String createSecurityGroup(String instanceId, String groupName, String description);
    
    /**
     * Sets the security groups for an instance.
     *
     * Note this is only valid for VPC instances.
     *
     * @param instanceId
     *            the instance id
     * @param groupIds
     *            ids of desired new groups
     *
     * @throws NotFoundException
     *             if the instance no longer exists or was already terminated after the crawler discovered it then you
     *             should get a NotFoundException
     */
    void setInstanceSecurityGroups(String instanceId, List<String> groupIds);

    


}