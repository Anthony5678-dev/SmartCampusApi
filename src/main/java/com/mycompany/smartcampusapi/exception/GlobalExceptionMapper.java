/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.exception;

/**
 *
 * 
 */
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import java.util.*;

public class GlobalExceptionMapper implements ExceptionMapper<Throwable>{
    
    @Override
    public Response toResponse(Throwable ex){
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("error", "Internal Server Error");
        error.put("message", "Something went wrong");
        
        return Response.status(500)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
