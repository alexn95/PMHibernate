/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.projectmanager;

import org.apache.log4j.Logger;
import ru.sfedu.projectmanager.model.IDataProvider;


/**
 *
 * @author alexey
 */
public class Client {
     private static Logger log = Logger.getLogger(Client.class);
     
     public Client (){
     }
     
     public void logBasicSystemInfo() {        
         log.info("Launching the application...");        
         log.info("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));        
         log.info("JRE: " + System.getProperty("java.version"));
         log.info("Java Launched From: " + System.getProperty("java.home"));
         log.info("Class Path: " + System.getProperty("java.class.path"));
         log.info("Library Path: " + System.getProperty("java.library.path"));
         log.info("User Home Directory: " + System.getProperty("user.home"));
         log.info("User Working Directory: " + System.getProperty("user.dir"));
         log.info("Test INFO logging."); } 
}
