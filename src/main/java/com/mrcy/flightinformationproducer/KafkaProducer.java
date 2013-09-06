package com.mrcy.flightinformationproducer;

import com.mrcy.flightinformationproducer.datascraper.FlightScheduleData;
import com.mrcy.flightinformationproducer.datascraper.FlightScheduleScraper;
import com.mrcy.flightinformationproducer.datascraper.ModesData;
import com.mrcy.flightinformationproducer.datascraper.ModesDataScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cpfreem
 */
public class KafkaProducer {

    // This will be painfully slow for a large number of flights since we have three nested loops
    public static void main(String[] args) throws IOException, JSONException, Exception {
        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        ModesDataScraper modesDataScraper = new ModesDataScraper();
        List<ModesData> modesData = modesDataScraper.getModesData();

        FlightScheduleScraper flightScheduleScraper = new FlightScheduleScraper();

        for (int i = 0; i < modesData.size(); i++) {
            List<FlightScheduleData> flightScheduleData = new ArrayList<FlightScheduleData>();
            flightScheduleData = flightScheduleScraper.getFlightSchedules(modesData.get(i).getFlightNumber());

            // A flight number could have 1 to many flight schedules associated with it
            for (int j = 0; j < flightScheduleData.size(); j++) {
                JSONObject temp = modesData.get(i).getJSONObject();

                // Loop through each flight schedule and add it to the flight
                Iterator keys = flightScheduleData.get(j).getJSONObject().keys();
                while (keys.hasNext()) {
                    String keyValue = keys.next().toString();
                    temp.put(keyValue, flightScheduleData.get(j).getJSONObject().get(keyValue));
                    keys.remove();
                }

                // Put flight with each schedule attached on the Kafka Queue
                ProducerData<String, String> data = new ProducerData<String, String>("test", temp.toString());
                producer.send(data);

                System.out.println(temp.toString());
            }

        }

    }
}
