package com.inria.spirals.mgonzale.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "failure")
public abstract class FailureMode {
	
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
    	
    	
    	Random       random    = new Random();
    	String       randomKey = keys.get( random.nextInt(keys.size()) );
    	return randomKey;
    	
    }

}
