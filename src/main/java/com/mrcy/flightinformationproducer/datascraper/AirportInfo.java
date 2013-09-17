/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrcy.flightinformationproducer.datascraper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jwalton
 */
public class AirportInfo {
    private String name;
    private double lat;
    private double lon;
    
    public AirportInfo(String name, double lat, double lon){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
    
    @Override
    public String toString() {
        return "name=" + this.name + ", " + "lat=" + this.lat + ", " + "lon=" + this.lon;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> schedule = new HashMap();
        schedule.put("NAME", this.name);
        schedule.put("LATITUDE", this.lat);
        schedule.put("LONGITUDE", this.lon);

        return schedule;
    }
}
