/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.config;

/**
 *
 * 
 */

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("com.mycompany.smartcampusapi");
    }
}