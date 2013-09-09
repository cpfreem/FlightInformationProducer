package com.mrcy.flightinformationproducer.datascraper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cpfreem
 */
public class ModesData {
    private String hexCode;
    private String aircraftType;
    private String registrationNumber;
    private String flightNumber;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double heading;
    private Double spead;
    private Long  timeStamp;
    private String airline;
    private String stops;

    public ModesData() {
        this.hexCode = "";
        this.aircraftType = "";
        this.registrationNumber = "";
        this.flightNumber = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.heading = 0.0;
        this.spead = 0.0;
        this.timeStamp = 0L;
        this.airline = "";
        this.stops = "";        
    }
    
    public ModesData(String hexCode, String aircraftType, String registrationNumber, String flightNumber, Double latitude, Double longitude, Double altitude, Double heading, Double spead, Long timeStamp, String airline, String stops) {
        this.hexCode = hexCode;
        this.aircraftType = aircraftType;
        this.registrationNumber = registrationNumber;
        this.flightNumber = flightNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.heading = heading;
        this.spead = spead;
        this.timeStamp = timeStamp;
        this.airline = airline;
        this.stops = stops;
    }
    
    public String getHexCode() {
        return hexCode;
    }
    
    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }
    
    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Double getSpead() {
        return spead;
    }

    public void setSpead(Double spead) {
        this.spead = spead;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        //Convert to milli-seconds
        this.timeStamp = timeStamp*1000;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }
    
    @Override
    public String toString() {
        return "\n\nHEX_CODE = " + this.hexCode +
               "\nAIRCRAFT_TYPE = " + this.aircraftType +
               "\nREGISTRATION_NUMBER = " + this.registrationNumber +
               "\nFLIGHT_NUMBER = " + this.flightNumber + 
               "\nLATITUDE = " + this.latitude.toString() +
               "\nLONGITUDE = " + this.longitude.toString() +
               "\nALTITUDE = " + this.altitude.toString() +
               "\nHEADING = " + this.heading.toString() +
               "\nSPEAD = " + this.spead.toString() +
               "\nTIME = " + this.timeStamp.toString() +
               "\nAIRLINE = " + this.airline +
               "\nSTOPS = " + this.stops;
    }
    
    public JSONObject getJSONObject() throws JSONException {
        JSONObject flight = new JSONObject();
        flight.put("HEX_CODE", this.hexCode);
        flight.put("AIRCRAFT_TYPE", this.aircraftType);
        flight.put("REGISTRATION_NUMBER", this.registrationNumber);
        flight.put("FLIGHT_NUMBER", this.flightNumber);
        flight.put("LATITUDE", this.latitude);
        flight.put("LONGITUDE", this.longitude);
        flight.put("ALTITUDE", this.altitude);
        flight.put("HEADING", this.heading);
        flight.put("SPEED", this.spead);
        flight.put("TIME", this.timeStamp);
        flight.put("AIRLINE", this.airline);
        flight.put("STOPS", this.stops);
        
        return flight;
    }
   
}
