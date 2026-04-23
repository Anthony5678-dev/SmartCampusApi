/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.resource;

/**
 *
 * 
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getApiInfo(){
        
        Map<String, Object> response = new HashMap<>();
        
        response.put("version", "1.0");
        response.put("developer", "w2092985");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("rooms", "/api/v1/rooms");
        
        response.put("endpoints", endpoints);
        
        return response;
    }
    
    
}
