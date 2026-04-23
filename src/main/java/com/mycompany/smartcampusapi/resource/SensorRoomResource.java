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
import com.mycompany.smartcampusapi.exception.RoomNotEmptyException;
import com.mycompany.smartcampusapi.model.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;


@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    
    private Map<String, Room> rooms = InMemoryDatabase.getRooms();
    
    @GET
    public Collection<Room> getAllRooms(){
        return rooms.values();
    }
    
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId){
        
        Room room = rooms.get(roomId);
        
        if(room == null){
            return Response.status(404)
                    .entity("Room not found")
                    .build();
        }
        return Response.ok(room).build();
    }
    
    @POST
    public Response createRoom(Room room){
        
        if(room.getId()== null || room.getName()==null){
            return Response.status(400)
                    .entity("Invalid room data")
                    .build();
        }
        
        if(rooms.containsKey(room.getId())){
            return Response.status(409)
                    .entity("Room already exists")
                    .build();
        }
        
        rooms.put(room.getId(), room);
        
        return Response.status(201)
                .entity(room)
                .build();
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId){
        
        Room room = rooms.get(roomId);
        
        if(room == null){
            return Response.status(404)
                    .entity("Room not found")
                    .build();
        }
        
        if(!room.getSensorIDs().isEmpty()){
           throw new RoomNotEmptyException("Room has active sensors");
        }
        
        rooms.remove(roomId);
        
        return Response.status(200)
                .entity("Room deleted")
                .build();
    }
    
}
