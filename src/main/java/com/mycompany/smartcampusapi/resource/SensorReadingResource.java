/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.resource;

/**
 *
 * 
 */
import com.mycompany.smartcampusapi.database.InMemoryDatabase;
import com.mycompany.smartcampusapi.exception.SensorUnavailableException;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.SensorReading;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    
    private String sensorId;
    private Map<String, Sensor> sensors = InMemoryDatabase.getSensors();
    private Map<String, List<SensorReading>> readings = InMemoryDatabase.getReadings();

    
    public SensorReadingResource(String sensorId){
        this.sensorId = sensorId;
    }
    
    @GET
    public List<SensorReading> getReadings(){
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }
    
    @POST
    public Response addReading(SensorReading reading){
        
        if (reading == null){
            return Response.status(400)
                    .entity("Sensor not found")
                    .build();
        }
        
        Sensor sensor = sensors.get(sensorId);
        
        if(sensor.getStatus()!= null && sensor.getStatus().equalsIgnoreCase("MAINTENANCE")){
            throw new SensorUnavailableException("Sensor is under maintenance");
        }
        readings.putIfAbsent(sensorId, new ArrayList<>());
        readings.get(sensorId).add(reading);
        
        sensor.setCurrentValue(reading.getValue());
        
        return Response.status(201)
                .entity(reading)
                .build();
    }
    
    
    
    
    
}
