package com.inria.spirals.mgonzale.domain;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.xfer.FileSystemFile;

public abstract class ScriptChaos {
	
	@Autowired
	private ResourceLoader resourceLoader;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	private final String name;
    private final int timeout = 10;
	
	public String getName() {
		return name;
	}

	public ScriptChaos(String name){
		this.name = name;
	}
	
	public void apply (Member member){
		SSHClient ssh = member.getInfrastructure().connectSsh(member.getId());
		int exitStat;
		String result = null;

		String filename = getName().toLowerCase() + ".sh";
		try {
			ssh.useCompression();
		} catch (TransportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Resource res = resourceLoader.getResource("classpath:" +"/scripts/"+ filename);
		try {
			ssh.newSCPFileTransfer().upload(new FileSystemFile(res.getFile().getAbsolutePath()),"/tmp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        try {

	    	   
	    	   Session session = ssh.startSession();
            try {
         	   	            	   
         	   Command cmd = session.exec("/bin/bash /tmp/" + filename);
                cmd.join(timeout+10, TimeUnit.SECONDS);
                exitStat = cmd.getExitStatus();
                
                if (exitStat != 0){
                	logger.warn("Got non-zero output from running script: {}", exitStat);
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
      	
        if (!result.isEmpty()){
        	System.out.println(result);
        }
		
	}


}
