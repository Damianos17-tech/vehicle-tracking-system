package com.damianos.fleet.simulator.kafka;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaRawTest {

    public static void main(String[] args) throws Exception {


        Properties props = new Properties();

        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "192.168.1.130:9092"
        );

        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName()
        );

        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName()
        );


        KafkaProducer<String,String> producer =
                new KafkaProducer<>(props);


        ProducerRecord<String,String> record =
                new ProducerRecord<>(
                        "truck-state",
                        "TEST",
                        "HELLO FROM RAW KAFKA"
                );


        producer.send(record, (metadata, exception) -> {


            if(exception != null){

                System.out.println("❌ ERROR");
                exception.printStackTrace();

            } else {

                System.out.println(
                        "✅ SUCCESS topic="
                                + metadata.topic()
                                + " partition="
                                + metadata.partition()
                                + " offset="
                                + metadata.offset()
                );
            }

        });


        producer.flush();
        producer.close();

    }
}