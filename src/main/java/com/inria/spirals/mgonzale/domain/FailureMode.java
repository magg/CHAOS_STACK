package com.inria.spirals.mgonzale.domain;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "failure")
public class FailureMode {
	
    @Autowired
    private BlockAllNetworkTraffic block;
    
    @Autowired
    private ShutdownInstance si;
	
	private HashMap<String, Boolean> modes;

    public HashMap<String, Boolean> getModes() {
        return modes;
    }

    public void setModes(HashMap<String, Boolean> modes) {
        this.modes = modes;
    }
    
    public String pickFailureMode(){
    	
    	List<String> keys = modes.entrySet().stream()
                .filter(map -> map.getValue() == true)
                .map(map->map.getKey())
                .collect(Collectors.toList());

    	int random = 0;
    	if (keys.size() >=2 ){
    	random = ThreadLocalRandom.current().nextInt(0, keys.size());
    	} else {
    		random = 0;
    	}
    	
    	String       randomKey = keys.get(random );
    	return randomKey;
    	
    }
    
    public void destroy(Member member) throws DestructionException{
    	switch (pickFailureMode()){
    	case "shutdowninstance":
    			si.terminateNow(member);
    		break;
    	case "blockallnetworktraffic":
    			block.blockAllNetworkTraffic(member);
    		break;
        default:
        	break;
    		
    	}
    	
    }

}
