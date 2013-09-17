package com.mrcy.flightinformationproducer.datascraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class implements a web scraper for http://planefinder.net
 *
 * @author cpfreem
 */
public class ModesDataScraper {

    public List<ModesData> getModesData() throws IOException, JSONException {
        List<ModesData> aircraftList = new ArrayList<ModesData>();

        HttpClient client = new DefaultHttpClient();
        String getURL = "http://planefinder.net/endpoints/update.php?faa=1&bounds=37.729817%2C-98.840955%2C52.451268%2C-76.868299";
        HttpGet get = new HttpGet(getURL);
        get.setHeader("Referer", "http://planefinder.net/");
        get.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:17.0) Gecko/20130807 Firefox/17.0");
        get.setHeader("X-Requested-With", "XMLHttpRequest");
        HttpResponse responseGet = client.execute(get);
        HttpEntity resEntityGet = responseGet.getEntity();

        if (resEntityGet != null) {
            //Return JSON with key of "planes" and a map for value
            // The map has a key of "1" with a map for value
            // The map has key value pairs where the map is the modes hex code
            // and the values are an array of values
            // [AIRCRAFT_TYPE, REGISTRATION_NUMBER, FLIGHT_NUMBER, LATITUDE, LONGITUDE, 
            //  ALTITUDE (FT), COURSE/HEADING (degrees), SPEED (KNOTS), TIME_STAMP (EPOCH), 
            //  AIRLINE (three characters), ORIG-DEST-FINAL (Using three letters with a dash in between),

            JSONObject response = new JSONObject(EntityUtils.toString(resEntityGet));

            for (int z = 0; z < response.getJSONArray("planes").length(); z++) {
//            for (int z = 0; z < 1; z++) {
                int count = 0;
                JSONObject aircraft = (JSONObject) response.getJSONArray("planes").get(z);

                Iterator keys = aircraft.keys();
                while (keys.hasNext()) {
                    String keyValue = keys.next().toString();
                    JSONArray values = aircraft.getJSONArray(keyValue);

                    ModesData modesData = new ModesData();

                    modesData.setHexCode(keyValue);
                    modesData.setAircraftType(values.get(0).toString());
                    modesData.setRegistrationNumber(values.get(1).toString());
                    modesData.setFlightNumber(values.get(2).toString());

                    modesData.setLatitude(new Double(values.get(3).toString()));
                    modesData.setLongitude(new Double(values.get(4).toString()));
                    modesData.setAltitude(new Double(values.get(5).toString()));
                    modesData.setHeading(new Double(values.get(6).toString()));
                    modesData.setSpead(new Double(values.get(7).toString()));
                    modesData.setTimeStamp(new Long(values.get(8).toString()));
                    modesData.setAirline(values.get(9).toString());
                    modesData.setStops(values.get(10).toString());

                    aircraftList.add(modesData);
                    count++;
                    keys.remove();
                }
                System.out.println("Found " + count + " aircraft at planes index: " + z);
            }
        }
        System.out.println("Returning " + aircraftList.size() + " aircraft");
        return aircraftList;
    }

    public static void main(String[] args) throws IOException, JSONException {
        ModesDataScraper modesDataScraper = new ModesDataScraper();

        List<ModesData> data = modesDataScraper.getModesData();
        
        System.out.println("Number of flights detected: "+ data.size());

        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i).getJSONObject().toString());
        }
    }
}
