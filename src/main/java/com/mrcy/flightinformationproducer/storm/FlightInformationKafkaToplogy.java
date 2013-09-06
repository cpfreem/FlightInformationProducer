package com.mrcy.flightinformationproducer.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import java.util.ArrayList;
import java.util.List;
import storm.kafka.KafkaConfig;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;

/**
 *
 * @author cpfreem
 */
public class FlightInformationKafkaToplogy {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        List<String> hosts = new ArrayList<String>();
        hosts.add("127.0.0.1");
        
        SpoutConfig spoutConfig = new SpoutConfig(
                KafkaConfig.StaticHosts.fromHostString(hosts, 1),
                "test",
                "/kafkastorm",
                "discovery");
        
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout", new KafkaSpout(spoutConfig), 1);
        builder.setBolt("flightInformationParserBolt", new FlightInformationParserBolt(), 1)
                .shuffleGrouping("kafkaSpout");
        
        Config conf = new Config();
        conf.setDebug(true);
        
        if(args != null && args.length > 0) {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("flightInformationTest", conf, builder.createTopology());
//            Utils.sleep(10000);
//            cluster.killTopology("flightInformationTest");
//            cluster.shutdown();            
        }
    }
}
