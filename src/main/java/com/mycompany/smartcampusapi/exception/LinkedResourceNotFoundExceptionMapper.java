/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.exception;

/**
 *
 * 
 */
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException>{
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex){
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", 422);
        error.put("error", "Unprocessable Entity");
        error.put("message", ex.getMessage());
        
        return Response.status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
