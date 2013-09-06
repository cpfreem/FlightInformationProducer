/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrcy.flightinformationproducer.datascraper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jwalton
 */
public class FlightScheduleData {
    private String origin;
    private String destination;
    private Long departureTime;
    private Long arrivalTime;
    private String aircraftType;
    
    public FlightScheduleData(String origin, String destination, Long departureTime, Long arrivalTime){
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Long departureTime) {
        this.departureTime = departureTime;
    }

    public Long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }
    
    @Override
    public String toString() {
        return "\n\nORIGIN = " + this.origin +
               "\nDESTINATION = " + this.destination +
               "\nDEPARTURE_TIME = " + this.departureTime +
               "\nARRIVAL_TIME = " + this.arrivalTime + 
               "\nSCHEDULED_AIRCRAFT_TYPE = " + this.aircraftType;
    }
    
    public JSONObject getJSONObject() throws JSONException {
        JSONObject schedule = new JSONObject();
        schedule.put("ORIGIN", this.origin);
        schedule.put("DESTINATION", this.destination);
        schedule.put("DEPARTURE_TIME", this.departureTime);
        schedule.put("ARRIVAL_TIME", this.arrivalTime);
        schedule.put("SCHEDULED_AIRCRAFT_TYPE", this.aircraftType);
        
        return schedule;
    }

}
