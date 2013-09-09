package com.mrcy.flightinformationproducer;

import com.mrcy.flightinformationproducer.datascraper.FlightScheduleData;
import com.mrcy.flightinformationproducer.datascraper.FlightScheduleScraper;
import com.mrcy.flightinformationproducer.datascraper.ModesData;
import com.mrcy.flightinformationproducer.datascraper.ModesDataScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cpfreem
 */
public class KafkaProducer {

    // This will be painfully slow for a large number of flights since we have two nested loops
    public static void main(String[] args) throws IOException, JSONException, Exception {
        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        ModesDataScraper modesDataScraper = new ModesDataScraper();
        Map<String, List<FlightScheduleData>> scheduleData = new HashMap<String, List<FlightScheduleData>>();
        long oldestTime = 1000 * 60 * 20;
        long totalEvents = 0;
        while (true) {
            long start = System.currentTimeMillis();
            List<ModesData> modesData = modesDataScraper.getModesData();

            FlightScheduleScraper flightScheduleScraper = new FlightScheduleScraper();
            long current = System.currentTimeMillis();
            System.out.println("Have " + modesData.size() + " aircraft to emit");
            int countEmitted = 0;

            for (int i = 0; i < modesData.size(); i++) {
                try {
                    long diff = current - modesData.get(i).getTimeStamp();
                    if (diff > oldestTime) {
                        System.out.println("Skipping " + modesData.get(i).getFlightNumber() + " position is too old");
                        continue;
                    }
                    String flightNumber = modesData.get(i).getFlightNumber();
                    List<FlightScheduleData> flightScheduleData = scheduleData.get(flightNumber);
                    if (flightScheduleData == null) {
                        try {
                            if (i < 100) {
                                System.out.println("Retrieving flight schedule for flight: "+flightNumber+" index: "+i);
                                flightScheduleData = flightScheduleScraper.getFlightSchedules(flightNumber);
                            } else {
                                flightScheduleData = new ArrayList<FlightScheduleData>();
                            }
                            scheduleData.put(flightNumber, flightScheduleData);
                        } catch (Exception e) {
                            System.out.println("Error getting flight schedule, putting empty list");
                            //e.printStackTrace();
                            scheduleData.put(flightNumber, new ArrayList<FlightScheduleData>());
                        }
                    }

                    JSONArray fsd = new JSONArray();
                    for (int j = 0; j < flightScheduleData.size(); j++) {
                        fsd.put(flightScheduleData.get(j).getJSONObject());
                    }

                    JSONObject merge = modesData.get(i).getJSONObject();
                    merge.put("SCHEDULE", fsd);
                    merge.put("UUID", totalEvents);
                    // Put flight with each schedule attached on the Kafka Queue
                    ProducerData<String, String> data = new ProducerData<String, String>("tracks2", merge.toString());
                    if ((i % 100) == 0) {
                        System.out.println("Emitting event for index " + i);
                    }
                    producer.send(data);
                    countEmitted++;
                    totalEvents++;
//                    System.out.println(merge.toString());
                } catch (Exception e) {
                    System.out.println("Exception processing aircraft " + e.getLocalizedMessage());
                }
                
                if(i > 150){
                    System.out.println("Exceeded test limit of 150 events");
                    break;
                }
            }

            long time = System.currentTimeMillis() - start;
            System.out.println("Emitted " + countEmitted + "/" + modesData.size() + " aircraft locations in " + time + " ms"+" time: "+new Date().toString()+" total events: "+totalEvents);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}
