package com.mrcy.flightinformationproducer.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpfreem
 */
public class FlightInformationParserBolt extends BaseRichBolt {
    public OutputCollector outputCollector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String messageStr = new String((byte[]) input.getValue(0));
        
        Logger.getLogger(FlightInformationParserBolt.class.getName()).log(Level.INFO, messageStr);
        
        this.outputCollector.emit(input, new Values(messageStr));
        this.outputCollector.ack(input);
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("flight"));
    }
}
