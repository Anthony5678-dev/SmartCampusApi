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
import com.mycompany.smartcampusapi.exception.LinkedResourceNotFoundException;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.model.SensorReading;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private Map<String, Sensor> sensors = InMemoryDatabase.getSensors();
    private Map<String, Room> rooms = InMemoryDatabase.getRooms();

    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null || type.isEmpty()) {
            return sensors.values();
        }

        Collection<Sensor> filtered = new ArrayList<>();

        for (Sensor sensor : sensors.values()) {
            if (sensor.getType() != null
                    && sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }

        return filtered;
    }

    @POST
    public Response createSensor(Sensor sensor) {

        if (sensor == null) {
            return Response.status(400)
                    .entity("Sensor body is missing")
                    .build();
        }

        if (sensor.getId() == null || sensor.getType() == null || sensor.getRoomId() == null) {
            return Response.status(400)
                    .entity("Invalid sensor data")
                    .build();
        }

        if (!rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        if (sensors.containsKey(sensor.getId())) {
            return Response.status(409)
                    .entity("Sensor already exists")
                    .build();
        }

        sensors.put(sensor.getId(), sensor);

        Room room = rooms.get(sensor.getRoomId());
        room.getSensorIDs().add(sensor.getId());

        return Response.status(201)
                .entity(sensor)
                .build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

}
