/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrcy.flightinformationproducer.datascraper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jwalton
 */
public class FlightScheduleData implements Serializable {

    private String origin;
    private String destination;
    private Long departureTime;
    private Long arrivalTime;
    private String aircraftType;
    private double destLat;
    private double destLon;

    public FlightScheduleData(String origin, String destination, Long departureTime, Long arrivalTime) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public FlightScheduleData(Map<String, Object> data) {
        this.origin = (String) data.get("ORIGIN");
        this.destination = (String) data.get("DESTINATION");
        this.departureTime = (Long) data.get("DEPARTURE_TIME");
        this.arrivalTime = (Long) data.get("ARRIVAL_TIME");
        this.aircraftType = (String) data.get("SCHEDULED_AIRCRAFT_TYPE");
        if (data.get("DESTINATION_LATITUDE") != null) {
            this.destLat = (Double) data.get("DESTINATION_LATITUDE");
        } else {
            System.out.println("Did not have destination latitude!");
        }
        if (data.get("DESTINATION_LONGITUDE") != null) {
            this.destLon = (Double) data.get("DESTINATION_LONGITUDE");
        } else {
            System.out.println("Did not have destination longitude!");
        }
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLon() {
        return destLon;
    }

    public void setDestLon(double destLon) {
        this.destLon = destLon;
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
        return "\n\nORIGIN = " + this.origin
                + "\nDESTINATION = " + this.destination
                + "\nDEPARTURE_TIME = " + this.departureTime
                + "\nARRIVAL_TIME = " + this.arrivalTime
                + "\nSCHEDULED_AIRCRAFT_TYPE = " + this.aircraftType;
    }

    public JSONObject getJSONObject() throws JSONException {
        JSONObject schedule = new JSONObject();
        schedule.put("ORIGIN", this.origin);
        schedule.put("DESTINATION", this.destination);
        schedule.put("DEPARTURE_TIME", this.departureTime);
        schedule.put("ARRIVAL_TIME", this.arrivalTime);
        schedule.put("SCHEDULED_AIRCRAFT_TYPE", this.aircraftType);
        schedule.put("DESTINATION_LATITUDE", this.destLat);
        schedule.put("DESTINATION_LONGITUDE", this.destLon);
        return schedule;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> schedule = new HashMap<String, Object>();
        schedule.put("ORIGIN", this.origin);
        schedule.put("DESTINATION", this.destination);
        schedule.put("DEPARTURE_TIME", this.departureTime);
        schedule.put("ARRIVAL_TIME", this.arrivalTime);
        schedule.put("SCHEDULED_AIRCRAFT_TYPE", this.aircraftType);
        schedule.put("DESTINATION_LATITUDE", this.destLat);
        schedule.put("DESTINATION_LONGITUDE", this.destLon);

        return schedule;
    }
}
