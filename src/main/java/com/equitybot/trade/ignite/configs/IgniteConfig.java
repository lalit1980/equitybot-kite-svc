package com.equitybot.trade.ignite.configs;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IgniteConfig {
	private static Ignite ignite;
	private IgniteConfig() {
		
	}
	static{
        try{
        	IgniteConfiguration cfg = new IgniteConfiguration();
        	ignite = Ignition.start(cfg);
        	Ignition.setClientMode(true);
        }catch(Exception e){
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
    }
    
    public static Ignite getInstance(){
        return ignite;
    }
}
