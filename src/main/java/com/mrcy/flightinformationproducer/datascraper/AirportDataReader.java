/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrcy.flightinformationproducer.datascraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jwalton
 */
public class AirportDataReader {

    private String dataFile = "allAirports.csv";
    private Map<String, AirportInfo> airportInfoMap = new HashMap<String,AirportInfo>();

    public AirportDataReader(){
        readFile();
    }
    
    public AirportInfo getAirportInfo(String airportName){
        return airportInfoMap.get(airportName);
    }
    
    private void readFile() {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + dataFile)));
        try {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("\\t");
//                System.out.println("line split length: "+lineSplit.length+" "+lineSplit[2]);
                String airport = lineSplit[2].substring(1, lineSplit[2].length());                
                String latString = lineSplit[22].substring(0, lineSplit[22].length()-1);
                String lonString = lineSplit[24].substring(0, lineSplit[22].length()-1);
                
                double lat = parseLatitude(latString);
                double lon = parseLongitude(lonString);
                AirportInfo airportInfo = new AirportInfo(airport, lat, lon);
                airportInfoMap.put(airport, airportInfo);
                //System.out.println(airport+" lat: "+lat+" ; "+lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private double parseLatitude(String latString){
        String[] latStringSplit = latString.split("-");
        double deg = Double.parseDouble(latStringSplit[0]);
        double min = Double.parseDouble(latStringSplit[1]);
        double sec = Double.parseDouble(latStringSplit[2]);
        
        double lat = deg+(min/60.0)+(sec/3600.0);
        
        return lat;
    }
    
    private double parseLongitude(String lonString){
        String[] lonStringSplit = lonString.split("-");
        String degString = lonStringSplit[0];
        if(degString.indexOf("0") == 0){
            degString = degString.substring(1, degString.length());
        }
        double deg = Double.parseDouble(degString);
        double min = Double.parseDouble(lonStringSplit[1]);
        double sec = Double.parseDouble(lonStringSplit[2]);
        
        double lon = deg+(min/60.0)+(sec/3600.0);
        lon*=-1.0;
        return lon;
    }
    
    public static void main(String[] args){
        AirportDataReader dataReader = new AirportDataReader();
        System.out.println(dataReader.getAirportInfo("DTW").toString());
        System.out.println(dataReader.getAirportInfo("YOW").toString());
    }
}
