package com.mrcy.flightinformationproducer;

import be.datablend.blueprints.impls.mongodb.MongoDBGraph;
import com.mrcy.flightinformationproducer.datascraper.FlightScheduleData;
import com.mrcy.flightinformationproducer.datascraper.FlightScheduleScraper;
import com.mrcy.flightinformationproducer.datascraper.ModesData;
import com.mrcy.flightinformationproducer.datascraper.ModesDataScraper;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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

    public static Graph graph = new MongoDBGraph("localhost", 27017);
    public static FlightScheduleScraper flightScheduleScraper = new FlightScheduleScraper();
    public static Set<String> badFlightNumbers = new HashSet<String>();

    public static List<FlightScheduleData> getFlightSchedule(String flightNumber) {
        List<FlightScheduleData> scheduleData = new ArrayList<FlightScheduleData>();
        GraphQuery query = graph.query();
        query.has("OBJECT_TYPE", "FLIGHT_SCHEDULE");
        query.has("FLIGHT_NUMBER", flightNumber);

        Iterable<Vertex> vertices = query.vertices();
        int count = 0;
        for (Vertex v : vertices) {
            List<Map<String, Object>> data = v.getProperty("FLIGHT_SCHEDULE_DATA");
            for (int i = 0; i < data.size(); i++) {
                scheduleData.add(new FlightScheduleData(data.get(i)));
            }

            count++;
        }

        if (count <= 0) {
            System.out.println("Did not find schedule for: " + flightNumber + " requesting from flight aware");
            scheduleData = flightScheduleScraper.getFlightSchedules(flightNumber);
            Vertex v = graph.addVertex(null);
            v.setProperty("OBJECT_TYPE", "FLIGHT_SCHEDULE");
            v.setProperty("FLIGHT_NUMBER", flightNumber);
            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < scheduleData.size(); i++) {
                mapList.add(scheduleData.get(i).toMap());
            }
            v.setProperty("FLIGHT_SCHEDULE_DATA", mapList);
        }


        return scheduleData;
    }

    // This will be painfully slow for a large number of flights since we have two nested loops
    public static void main(String[] args) throws IOException, JSONException, Exception {



        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        ModesDataScraper modesDataScraper = new ModesDataScraper();
//        Map<String, List<FlightScheduleData>> scheduleData = new HashMap<String, List<FlightScheduleData>>();
        long oldestTime = 1000 * 60 * 20;
        long totalEvents = 0;
        while (true) {
            long start = System.currentTimeMillis();
            List<ModesData> modesData = modesDataScraper.getModesData();


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

                    List<FlightScheduleData> flightScheduleData = getFlightSchedule(flightNumber);

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
                    e.printStackTrace();
                }

                if (i > 150) {
                    System.out.println("Exceeded test limit of 150 events");
                    break;
                }
            }

            long time = System.currentTimeMillis() - start;
            System.out.println("Emitted " + countEmitted + "/" + modesData.size() + " aircraft locations in " + time + " ms" + " time: " + new Date().toString() + " total events: " + totalEvents);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}
