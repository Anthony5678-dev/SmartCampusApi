/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.database;

/**
 *
 * 
 */
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.SensorReading;
import java.util.HashMap;
import java.util.Map;
import java.util.List;



public class InMemoryDatabase {
    
    private static Map<String, Room> rooms = new HashMap<>();
    
    public static Map<String, Room> getRooms(){
        return rooms;
        
    }
    
    private static Map<String, Sensor> sensors = new HashMap<>();
    
    public static Map<String, Sensor> getSensors(){
        return sensors;
    }
    
    private static Map<String, List<SensorReading>> readings = new HashMap<>();
    
    public static Map<String, List<SensorReading>> getReadings(){
        
        return readings;
    }
    
    static{
        rooms.put("COMP-C107", new Room("COMP-C107", "Computer Science lab",35));
        rooms.put("LAW-B35", new Room("LAW-B35", "Law Lecture Hall",200));
        rooms.put("DES-002", new Room("DES-002", "Dsign Studio",20));
        
        sensors.put("C02-102", new Sensor("C02-102", "C02", "ACTIVE", 420.5, "COMP-C107"));
        sensors.put("TEMP-23", new Sensor("TEMP-23", "Temperature", "ACTIVE", 21.7, "LAW-B35"));
 
    }
    
}
